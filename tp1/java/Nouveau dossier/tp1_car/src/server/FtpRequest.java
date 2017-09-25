/*
   Marouane ABAKARIM
   Mohamed ADANSAR
 */
package server;

import Admin.Commandes;
import Admin.DifinitionServer;
import Admin.UserClass;
import Exception.NotAuthentificationException;
import Exception.NotExistCommandException;
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;

/*  TRANSFER recevoir repertoir 
    transfer recevoir bianry
    activer les exeception
    test dans java */
public class FtpRequest extends Thread {

    public boolean connected = false;
    public Socket s;
    public BufferedReader in;
    public String login, password, reponse, renameOrigine, renameDestination;
    //public Map<String, String> users;
    public boolean passive = false;
    public int ActivPort;
    public InetAddress ActiAdrr;
    private Socket socketData;
    private ServerSocket serverSocket;
    //public String rep;
    /**/
    public UserClass auth;
    public DifinitionServer toolServer;

    public FtpRequest(Socket socket, String rep) throws IOException {
        this.s = socket;
        this.auth = new UserClass();
        toolServer = new DifinitionServer();
        //this.users = user;
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        //this.rep = rep;

    }

    public boolean isPassive() {
        return this.passive;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void envoyerMessage(String Msg) throws IOException {
        this.s.getOutputStream().write(Msg.getBytes());
        this.s.getOutputStream().flush();
    }

    public String recevoirMessage() throws IOException {
        return in.readLine();
    }

    public void processRequest(String reponse) throws IOException {

        System.out.println("serv:  " + reponse + "\n");

        try {
            if (reponse != null) {
                String tab[] = reponse.split(" ");
                if (tab[0].equals("USER")) {
                    processUSER(tab[1]);
                } else if (tab[0].equals("PASS")) {
                    processPASS(tab[1]);
                } else if (tab[0].equals("QUIT")) {
                    processQUIT();
                } else if (tab[0].equals("OPTS")) {
                    processOPTS();
                } else if (tab[0].equals("PORT")) {
                    processPORT(tab[1]);
                } else if (tab[0].equals("PASV")) {
                    processPASV();
                } else if (tab[0].equals("LIST") || tab[0].equals("NLST")) {
                    processLIST();
                } else if (tab[0].equals("RETR")) {
                    processRetr(tab[1]);
                } else if (tab[0].equals("STOR")) {
                    processStor(tab[1]);
                } else if (tab[0].equals("CWD")) {
                    processCWD(tab[1]);
                } else if (tab[0].equals("PWD") || tab[0].equals("XPWD")) {
                    processPWD();
                } else if (tab[0].equals("TYPE")) {
                    processTYPE(tab[1]);
                } else if (tab[0].equals("CDUP")) {
                    processCDUP();
                } else if (tab[0].equals("FEAT")) {
                    processFEAT();
                } else if (tab[0].equals("EPSV")) {
                    System.out.println("*****");
                } else if (tab[0].equals("EPRT")) {
                    System.out.println("*****");
                } else if (tab[0].equals("XMKD")) {
                    processXMKD(tab[1]);
                } else if (tab[0].equals("XRMD")) {
                    processXRMD(tab[1]);
                } else if (tab[0].equals("RNFR")) {
                    processRNFR(tab[1]);
                } else if (tab[0].equals("RNTO")) {
                    processRNTO(tab[1]);
                } else {
                    this.envoyerMessage(Commandes.ERR_BAD_CMD);
                    System.out.println("others de switch\n");
                    throw new NotExistCommandException();
                }
            }
        } catch (NotAuthentificationException b) {
            System.out.println("erreur: " + b);
        } catch (NotDirectoryException ND) {
            System.out.println("erreur : " + ND);
        } catch (NotExistCommandException ExCmd) {
            System.out.println("Erreur : " + ExCmd);
        }
    }

    public void processUSER(String user) throws IOException {

        if (!this.connected) {

            this.login = user;
            if (auth.UserIsCorrect(user)) {
                this.envoyerMessage(Commandes.USER_OK);
            } else {
                this.envoyerMessage(Commandes.USER_KO);
            }

        } else {
            this.envoyerMessage(Commandes.ERR_CHANGE_USER);
        }
    }

    public void processPASS(String password) throws NotAuthentificationException, IOException {
        this.password = password;

        if (auth.PassIsCorrect(password)) {
            this.connected = true;

            this.envoyerMessage(Commandes.PASS_OK);
        } else {
            this.connected = false;

            this.envoyerMessage(Commandes.PASS_KO);
        }
    }

    public void processQUIT() throws IOException {
        this.connected = false;

        this.envoyerMessage(Commandes.QUIT_OK);
        s.close();
    }

    public String[] Spplit(String cmd) {

        String[] tab;
        tab = cmd.split(" ");
        return tab;
    }

    public void processPORT(String tab) throws IOException {
        String AdrrPort[] = tab.split(",");
        ActivPort = (Integer.parseInt(AdrrPort[4]) * 256) + Integer.parseInt(AdrrPort[5]);
        // System.out.println("("+AdrrPort[0]+","+AdrrPort[1]+","+AdrrPort[2]+","+AdrrPort[3]+","+AdrrPort[4]+","+AdrrPort[5]);
        ActiAdrr = InetAddress.getByName(AdrrPort[0] + "." + AdrrPort[1] + "." + AdrrPort[2] + "." + AdrrPort[3]);
        this.socketData = new Socket(this.ActiAdrr, this.ActivPort);

        this.envoyerMessage("200 PORT command successful. Consider using PORT.\r\n");
    }

    public void processPASV() throws IOException {
        this.serverSocket = new ServerSocket(0);
        this.passive = true;

        byte[] Adress = this.s.getInetAddress().getAddress();
        String addr = "";
        for (byte bit : Adress) {
            addr += bit + ",";
        }
        int port = serverSocket.getLocalPort();
        this.envoyerMessage("227 Entering Passive Mode (" + addr + (port / 256) + "," + (port % 256) + ")\r\n");
        this.socketData = this.serverSocket.accept();

    }

    public void processLIST() throws NotAuthentificationException, IOException {

        //System.out.println("afficher list de fichier \n");
        String FileName = "";
        String permission = "";
        String resultat = "";
        Date date = null;
        String userName = "";

        if (isConnected()) {

            envoyerMessage("150 Opening data channel for directory list.\r\n");

            //System.out.print("list reçu\n");
            //System.out.println("porte : "+ActivPort+"\n adress: "+ActiAdrr+"\n");
            OutputStream out = socketData.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);

            //File[] files = new File(System.getProperty("user.dir")).listFiles();
            File[] files = new File(toolServer.getCurrentDirectory()).listFiles();
            for (File file : files) {
                FileName = file.getName();
                date = new Date(file.lastModified());
                userName = Files.getOwner(file.toPath()).toString();

                if (file.isFile()) {
                    permission = "-rw-rw-rw-";
                } else {
                    permission = "drw-rw-rw-";
                }

                resultat += permission + "\t" + userName + "\t" + file.length() + "\t" + date + "\t" + FileName + "\n";
            }

            dataOut.writeBytes(resultat + "\n");
            dataOut.close();
            this.socketData.close();
            out.close();
            if (this.serverSocket != null) {
                this.serverSocket.close();
                this.serverSocket = null;
            }
            envoyerMessage("226 Directory send OK.\r\n");
        } else {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        }
    }

    public void processStor(String fichier) throws NotAuthentificationException, IOException {

        if (this.isConnected() != true) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else {
            if (this.socketData == null) {
                this.envoyerMessage("425 la connexion data n'est pas realisée\r\n");
            } else {

                InputStream in = this.socketData.getInputStream();
                File f = new File(toolServer.getCurrentDirectory());
                //File f = new File("C:/Users/abk/ftptest");
                String chemin = f.toPath().toAbsolutePath().toString();
                Path tar = Paths.get(chemin + "/" + fichier);

                this.envoyerMessage("125 Starting transfer.\r\n");
                Files.copy(in, tar, StandardCopyOption.REPLACE_EXISTING);
                in.close();
                this.socketData.close();

                //this.serverSocket.close();
                this.envoyerMessage("226 \r\n");
              
            }

            System.out.println("on a sortie de la fonctin stor");
        }
    }

    public void processOPTS() throws IOException {

        this.envoyerMessage("200 Welcome on the FTP Server\r\n");
    }

    public void processRetr(String fichier) throws NotAuthentificationException, IOException {

        if (this.isConnected() != true) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else if (this.socketData == null) {
            this.envoyerMessage("425 la connexion data n'est pas realisée\r\n");
        } else {

            //this.envoyerMessage("125 Starting transfer.\r\n");

            File f = new File(toolServer.getCurrentDirectory());

            String chemin = f.toPath().toAbsolutePath().toString();
            Path tar = Paths.get(chemin + "/" + fichier);
            
            File ftest = new File(toolServer.getCurrentDirectory() + File.separator + fichier);
            
            if(ftest.exists()){
                this.envoyerMessage("125 Starting transfer.\r\n");
                
                OutputStream os = this.socketData.getOutputStream();
                Files.copy(tar, os);
                os.flush();
                System.out.println("etape 1");
                this.envoyerMessage("226 RETR Transfer completed.\r\n");
                
                this.socketData.close();
                this.serverSocket = null;
            }else{
                
                this.envoyerMessage("550 " + fichier + " Is Not Exist.\r\n");
            }
        }
    }

    public void processPWD() throws NotAuthentificationException, IOException {

        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else {

            String tmp = new File(toolServer.getCurrentDirectory()).getCanonicalPath();
            String chemin = tmp.substring(toolServer.getDirectoryServer().length());
            chemin = chemin.replace(File.separator, new String("/"));
            //chemin.replace(File.separator,new String("/"));
            //System.out.println(" :  "+new File(this.rep).getCanonicalPath());
            //System.out.println(" :  "+chemin);

            if (chemin.length() == 0) {
                chemin = chemin + "/";
            }

            this.envoyerMessage("257 " + chemin + " \r\n");

        }
    }

    public void processCWD(String chemin) throws IOException, NotAuthentificationException {
        //System.out.println("chemin : ,"+chemin+",rep : "+this.rep+" ,l: "+chemin.length());

        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else if (!chemin.equals("..")) {
            if (chemin.indexOf("..") == -1) {
                if(chemin.equals("//")){
                    processCDUP();
                }else{
                    if (!chemin.startsWith("/")) {
                        chemin = toolServer.getCurrentDirectory() + "/" + chemin;
                        System.out.println("im here 1");
                    }else{
                        chemin = toolServer.getCurrentDirectory() + chemin;
                        System.out.println("im here 2");
                    }
                 
                    File newDirectory = new File(chemin);

                //System.out.println("avant : "+server.inc);
                    if (!newDirectory.isDirectory()) {
                        this.envoyerMessage("550 erreur is not directorRy\r\n");

                    } else {
                        toolServer.AddCurrentDirectory(chemin);
                        this.envoyerMessage("250 Directory is changed.\r\n");
                    }
         
                }
            } else {
                this.envoyerMessage("550 Failed to change directory.\r\n");
            }
        } else if (chemin.equals("..")) {
            
            processCDUP();
        }else if(chemin.equals("//")){
            this.envoyerMessage("550 Failed directorty // .\r\n");
        }
        /*else{
            this.envoyerMessage("550 EREUR.\r\n");
        }*/
    }

    public void processCDUP() throws NotDirectoryException, NotAuthentificationException, IOException {
        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else {

            //System.out.println("avent -- rep : " + toolServer.getCurrentDirectory() + " ; ServerDir:" + toolServer.getDirectoryServer());
            //System.out.println("1 : "+File.separator+" ;2 : "+File.pathSeparatorChar);
            String tmp = toolServer.getCurrentDirectory();
            this.toolServer.AddCurrentDirectory(tmp.replace(File.separator, new String("/")));
            //System.out.println("after -- arep : "+this.rep+" ; ServerDir:"+server.directoryServer);
            if (toolServer.getCurrentDirectory().equals(toolServer.getDirectoryServer())) {
                System.out.println("pas de change de directory");
                this.envoyerMessage("250 No Permission Change Directory.\r\n");
                throw new NotDirectoryException("");
            } else {

                String ch = new File(toolServer.getCurrentDirectory()).getParent();
                //System.out.println("parent : " + ch + " ,ServerDirectory: " + server.directoryServer);
                //System.out.println("nbr: " + ch.indexOf(server.directoryServer.toString()));

                toolServer.AddCurrentDirectory(ch);
                //System.out.println("on peux chager repertoire");
                this.envoyerMessage("250 Directory is changed.\r\n");
            }
        }
    }

    public void processXMKD(String name) throws IOException, NotAuthentificationException {

        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else {
            boolean result_creer = new File(toolServer.getCurrentDirectory() + File.separator + name).mkdir();

            if (result_creer) {
                this.envoyerMessage("257 Repertoire et bien créer.\r\n");
            } else {
                this.envoyerMessage("550 Erreur Creation de Repertoir.\r\n");
            }
        }
    }

    public void processXRMD(String name) throws IOException, NotAuthentificationException {

        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else {
            File file = new File(toolServer.getCurrentDirectory() + File.separator + name);
            if (file.exists()) {
                boolean result_del = file.delete();

                if (result_del) {
                    this.envoyerMessage("200 Delete " + name + ".\r\n");
                } else {
                    this.envoyerMessage("550 Probleme to Delete " + name + ".\r\n");
                }
            } else {
                this.envoyerMessage("550 " + name + " Is Not Exist.\r\n");
            }
        }
    }

    public void processRNFR(String name) throws IOException, NotAuthentificationException {
        //System.out.println("je suis dans RNFR");
        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else {
            this.renameOrigine = name;
            File file = new File(toolServer.getCurrentDirectory() + File.separator + name);

            if (file.exists()) {
                this.envoyerMessage("350 exists RNFR.\r\n");
                /*    
        if((file.isDirectory() && "MALK.TXT".indexOf(".") ==-1 ) || (!file.isDirectory() && "MALK.TXT".indexOf(".") !=-1 ) ){    
            boolean result_rename = file.renameTo(new File(this.rep+File.separator+"MALK.TXT"));
            if(result_rename){
                this.envoyerMessage("250 exist et renomer.\r\n");
            }else     
                this.envoyerMessage("550 Exist mais problem de rename.\r\n");
        }*/
            } else {
                this.envoyerMessage("550 Not exist RNFR.\r\n");
            }
        }
    }

    public void processRNTO(String name) throws IOException, NotAuthentificationException {
        //System.out.println("je suis dans RNTO");
        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification.\r\n");
            throw new NotAuthentificationException();
        } else {
            this.renameDestination = name;
            File file = new File(toolServer.getCurrentDirectory() + File.separator + this.renameOrigine);
            if ((file.isDirectory() && name.indexOf(".") == -1) || (file.isFile() && name.indexOf(".") != -1)) {
                //System.out.println("entrer dans repertoire");
                boolean result_rename = file.renameTo(new File(toolServer.getCurrentDirectory() + File.separator + name));
                if (result_rename) {
                    this.envoyerMessage("250 exist et renomer RNTO.\r\n");
                } else {
                    this.envoyerMessage("550 Exist mais problem de rename RNTO.\r\n");
                }
            } else {
                this.envoyerMessage("550 Not exist RNTO.\r\n");
            }
        }
    }

    public void processTYPE(String msg) throws IOException, NotExistCommandException {
        if ("I".equals(msg) || "A".equals(msg)) {
            this.envoyerMessage("200 Type OK.\r\n");
        } else {
            this.envoyerMessage("502 Type Not Implemented.\r\n");
            throw new NotExistCommandException();
        }
    }

    public void processFEAT() throws IOException {
        this.envoyerMessage("211 \r\n");
    }

    @Override
    public void run() {
        try {

            this.envoyerMessage("220 Welcome on the FTP Server\r\n");

            //while((reponse = br.readLine()) != null){
            while (true) {

                // this.s.getOutputStream().write(new String("bien").getBytes());
                reponse = in.readLine();
                if (reponse != null) {
                    processRequest(reponse);
                }
            }

        } catch (IOException er) {
            System.out.println("erreur : " + er);
        }
    }

}
