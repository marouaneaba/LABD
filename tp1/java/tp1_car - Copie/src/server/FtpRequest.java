/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Command.Commandes;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Vector;
import java.lang.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

public class FtpRequest extends Thread {

    public boolean connected = false;
    public Socket s;
    public BufferedReader in;
    public String login, password, reponse,renameOrigine,renameDestination;
    public Map<String, String> users;
    public boolean passive = false;
    public int ActivPort;
    public InetAddress ActiAdrr;
    private Socket socketData;
    private ServerSocket serverSocket;
    public String rep;

    public FtpRequest(Socket socket, Map user, String rep) throws IOException {
        this.s = socket;
        this.users = user;
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.rep = rep;

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

        System.out.println("serv:  "+reponse + "\n");
        
        if (reponse != null) {
            String tab[] = reponse.split(" ");

            switch (tab[0]) {
                case "USER":
                    processUSER(tab[1]);
                    break;
                case "PASS":
                    processPASS(tab[1]);
                    break;
                case "QUIT":
                    processQUIT();
                    break;
                case "OPTS":
                    processOPTS();
                    break;
                case "PORT":
                    /* probleme de connexion dans port*/
                    processPORT(tab[1]);
                    break;
                case "PASV":
                    processPASV();
                    break;
                case "LIST" :
                    processLIST();
                    break;
                case "NLST":
                    processLIST();
                    break;
                case "RETR":
                    /* utilisé pour prendre un fichier du répertoire distant et le déposer dans le répertoire local */
                    processRetr(tab[1]);
                    break;
                case "STOR":
                    /* utilisé pour déposer un fichier venant du répertoire local dans le répertoire distant*/
                    processStor(tab[1]);
                    break;
                case "CWD":
                    processCWD(tab[1]);
                    break;
                case "XPWD" :
                    processPWD();
                    break;
                case "PWD":
                    processPWD();
                    break;
                case "TYPE":
                    this.envoyerMessage("200 TYPE OK.\r\n");
                    break;
                case "CDUP":
                    /* Je peux quitter un répertoire distant */
                    processCDUP();
                    break;
                case "FEAT":
                    processFEAT();
                    break;
                case "EPSV":
                    //processEPSV();
                    break;
                case "EPRT":
                    //processEPRT();
                    break;
                case "XMKD":
                    processXMKD(tab[1]);
                    break;
                case "XRMD":
                    processXRMD(tab[1]);
                    break;
                case "RNFR":
                    processRNFR(tab[1]);
                    break;
                case "RNTO":
                    processRNTO(tab[1]);
                    break;
                default:
                    this.envoyerMessage("502 Command Invalid.\r\n");
                    System.out.println("others de switch\n");
                    break;
            }
        }
    }

    public void processUSER(String user) throws IOException {

        if (!this.connected) {

            this.login = user;
            if (server.users.containsKey(user)) {
                this.s.getOutputStream().write(new String("331 User name okay, need password\r\n").getBytes());
                this.s.getOutputStream().flush();
            } else {
                this.s.getOutputStream().write(new String("530 Bad User Name\r\n").getBytes());
            }

        } else {
            this.s.getOutputStream().write(new String("530 Can't change from guest user\r\n").getBytes());
            this.s.getOutputStream().flush();
        }
    }

    public void processPASS(String password) throws IOException {
        this.password = password;

        if (server.users.get(this.login).equals(password)) {
            this.connected = true;

            this.s.getOutputStream().write(new String("230 Login successful\r\n").getBytes());
            this.s.getOutputStream().flush();
        } else {
            this.connected = false;

            this.s.getOutputStream().write(new String("430 Not logged in\r\n").getBytes());
            this.s.getOutputStream().flush();
        }
    }

    public void processQUIT() throws IOException {
        this.connected = false;

        this.s.getOutputStream().write(new String("221 QUIT bye bye.\r\n").getBytes());
        this.s.getOutputStream().flush();
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

    public void processLIST() throws IOException {
        
        
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
            File[] files = new File(this.rep).listFiles();
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
        }
    }

    public void processStor(String fichier) throws IOException {

        if (this.socketData == null) {
            this.envoyerMessage("425 la connexion data n'est pas realisée\r\n");
        } else {
            /*
            int donnees;
            /* Si le fichier précisé n'existe pas, il sera créé. 
            Si il existe et qu'il contient des données celles-ci seront écrasées.
            se fichier FichierOut il est créer dans le serveur */
 /*
            FileOutputStream FichierOut=new FileOutputStream(fichier,true);
          
            this.envoyerMessage("150 \r\n");
            DataInputStream in ;
            
                while((donnees=this.in.read() )!= 1)
                {
                    FichierOut.write(donnees);
                }
            FichierOut.close();
             */
            InputStream in = this.socketData.getInputStream();
            File f = new File(this.rep);
            //File f = new File("C:/Users/abk/ftptest");
            String chemin = f.toPath().toAbsolutePath().toString();
            Path tar = Paths.get(chemin + "/" + fichier);
            System.out.println("etape 1");
            this.envoyerMessage("125 Starting transfer.\r\n");
            Files.copy(in, tar, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("etape 2");
            in.close();
            this.socketData.close();

            //this.serverSocket.close();
            this.envoyerMessage("226 \r\n");
        }

        System.out.println("on a sortie de la fonctin stor");
    }

    public void processOPTS() throws IOException {

        this.envoyerMessage("200 Welcome on the FTP Server\r\n");
    }

    public void processRetr(String fichier) throws IOException {

        if (this.socketData == null) {
            this.envoyerMessage("425 la connexion data n'est pas realisée\r\n");
        } else {

            

            File f = new File(this.rep);
            String chemin = f.toPath().toAbsolutePath().toString();
            Path tar = Paths.get(chemin + "/" + fichier);

this.envoyerMessage("125 Starting transfer.\r\n");
            OutputStream os = this.socketData.getOutputStream();
            this.envoyerMessage("226 RETR Transfer completed.\r\n");
            Files.copy(tar, os);
            
            os.flush();

            

            this.socketData.close();
            this.serverSocket = null;

        }

    }

    public void processPWD() throws IOException {
        
        if(!this.isConnected()){
            envoyerMessage("530 Not logged in\r\n");
        }else{
            
            String tmp = new File(this.rep).getCanonicalPath();
            String chemin = tmp.substring(server.directoryServer.length());
            chemin = chemin.replace(File.separator, new String("/"));
            //chemin.replace(File.separator,new String("/"));
            //System.out.println(" :  "+new File(this.rep).getCanonicalPath());
            //System.out.println(" :  "+chemin);

            if(chemin.length() == 0) chemin = chemin+"/";
            
                this.envoyerMessage("257 "+chemin+" \r\n");
                
            }
        }
    

    public void processCWD(String chemin) throws IOException {
        //System.out.println("chemin : ,"+chemin+",rep : "+this.rep+" ,l: "+chemin.length());

        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification\r\n");
        } else if (!chemin.equals("..")) {
            if(chemin.indexOf("..") == -1){
                if (!chemin.startsWith("/")) {
                    chemin = this.rep + "/" + chemin;
                }else{
                    chemin = this.rep+chemin;
                }
                File newDirectory = new File(chemin);

                //System.out.println("avant : "+server.inc);
                if (!newDirectory.isDirectory()) {
                    this.envoyerMessage("550 erreur is not directorRy\r\n");

                } else {
                    this.rep = chemin;
                    this.envoyerMessage("250 Directory is changed.\r\n");
                }
            }else{
                this.envoyerMessage("550 Failed to change directory.\r\n");
            }
        } else if (chemin.equals("..")) {
            processCDUP();
        }
        /*else{
            this.envoyerMessage("550 EREUR.\r\n");
        }*/
    }

    public void processCDUP() throws IOException {
        if (!this.isConnected()) {
            this.envoyerMessage("550 errour non ethentification\r\n");
        } else{
            
            System.out.println("avent -- rep : "+this.rep+" ; ServerDir:"+server.directoryServer);
            //System.out.println("1 : "+File.separator+" ;2 : "+File.pathSeparatorChar);
            this.rep = this.rep.replace(File.separator, new String("/"));
            //System.out.println("after -- arep : "+this.rep+" ; ServerDir:"+server.directoryServer);
            if (this.rep.equals(server.directoryServer)) {
                //System.out.println("pas de change de directory");
                this.envoyerMessage("250 No Permission Change Directory.\r\n");

            } else {

                String ch = new File(this.rep).getParent();
                //System.out.println("parent : " + ch + " ,ServerDirectory: " + server.directoryServer);
                //System.out.println("nbr: " + ch.indexOf(server.directoryServer.toString()));

                this.rep = ch;
                //System.out.println("on peux chager repertoire");
                this.envoyerMessage("250 Directory is changed.\r\n");
            }
        }
    }
    
    public void processXMKD(String name) throws IOException
    {
        
        boolean result_creer = new File(this.rep+File.separator+name).mkdir();
        
        if(result_creer)
            this.envoyerMessage("257 Repertoire et bien créer.\r\n");
        else
            this.envoyerMessage("550 Erreur Creation de Repertoir.\r\n");
    }
    
    public void processXRMD(String name) throws IOException
    {
        File file = new File(this.rep+File.separator+name);
        if(file.exists()){
            boolean result_del = file.delete();
            
            if(result_del){
                this.envoyerMessage("200 Delete "+name+".\r\n");
            }else{
                this.envoyerMessage("550 Probleme to Delete "+name+".\r\n");
            }
        }else{
            this.envoyerMessage("550 "+name+" Is Not Exist.\r\n");
        }
    }
    
    public void processRNFR(String name) throws IOException
    {
        //System.out.println("je suis dans RNFR");
        
        this.renameOrigine = name;
        File file = new File(this.rep+File.separator+name);

        if(file.exists())
        {
            this.envoyerMessage("350 exists RNFR.\r\n");
        /*    
        if((file.isDirectory() && "MALK.TXT".indexOf(".") ==-1 ) || (!file.isDirectory() && "MALK.TXT".indexOf(".") !=-1 ) ){    
            boolean result_rename = file.renameTo(new File(this.rep+File.separator+"MALK.TXT"));
            if(result_rename){
                this.envoyerMessage("250 exist et renomer.\r\n");
            }else     
                this.envoyerMessage("550 Exist mais problem de rename.\r\n");
        }*/
        }else{
            this.envoyerMessage("550 Not exist RNFR.\r\n");
        }
    }
    
    public void processRNTO(String name) throws IOException
    {
        //System.out.println("je suis dans RNTO");
        
        this.renameDestination = name;
        File file =new File(this.rep+File.separator+this.renameOrigine);
        if((file.isDirectory() && name.indexOf(".") == -1) ||(file.isFile() && name.indexOf(".")!=-1) )
        {
            //System.out.println("entrer dans repertoire");
            boolean result_rename = file.renameTo(new File(this.rep+File.separator+name));
            if(result_rename){
                this.envoyerMessage("250 exist et renomer RNTO.\r\n");
            }else{
                this.envoyerMessage("550 Exist mais problem de rename RNTO.\r\n");
            }
        }else{
            this.envoyerMessage("550 Not exist RNTO.\r\n");
        }
    }
    
    public void processFEAT() throws IOException
    {
        this.envoyerMessage("211 \r\n");
    }
    
    public void run() {
        try {

            this.envoyerMessage("220 Welcome on the FTP Server\r\n");
            
            //while((reponse = br.readLine()) != null){
            while (true) {

                // this.s.getOutputStream().write(new String("bien").getBytes());
                reponse = in.readLine();
                if(reponse != null){
                    processRequest(reponse);
                }
            }

        } catch (IOException er) {
            System.out.println("erreur : " + er);
        }
    }

}
