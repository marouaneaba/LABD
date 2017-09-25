/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import Command.Commandes;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Vector;
import java.lang.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Date;
import javafx.scene.chart.PieChart.Data;

public class FtpRequest extends Thread {

    public boolean connected = false;
    public Socket s;
    public BufferedReader in;
    public String login, password, reponse;
    public Map<String, String> users;
    public boolean passive = false;

    public FtpRequest(Socket socket, Map user) throws IOException {
        this.s = socket;
        this.users = user;
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
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

        System.out.println(reponse + "\n");

        if (reponse != null) {
            String tab[] = reponse.split(" ");

            switch (tab[0]) {
                case "USER":
                    processUSER(tab[1], s);
                    break;
                case "PASS":
                    processPASS(tab[1], s);
                    break;
                case "QUIT":
                    processQUIT();
                    break;
                case "LIST":
                    processList(tab[1], s);
                    break;
                default:
                    envoyerMessage(Commandes.UNKOWN_CMD);
                    break;
            }
        }
    }

    public void processUSER(String user, Socket s) throws IOException {

        if (!this.connected) {

            this.login = user;

            
            this.s.getOutputStream().write(new String("\n********** Vous etes connecter ************\n").getBytes());
            this.s.getOutputStream().flush();

        } else {
            this.s.getOutputStream().write(new String("\n********** Vous etes déja connecter ************\n").getBytes());
            this.s.getOutputStream().flush();
        }
    }

    public void processPASS(String password, Socket s) throws IOException {
        this.password = password;
        this.s.getOutputStream().write(new String("\n********* Serveur recu password ********\n").getBytes());
        this.s.getOutputStream().flush();

        if (server.users.get(this.login).equals(password)) {
            this.connected = true;
            this.s.getOutputStream().write(new String("\n******** Vous etes connecter *********\n").getBytes());
            this.s.getOutputStream().flush();

            this.s.getOutputStream().write(new String("230").getBytes());
            this.s.getOutputStream().flush();
        } else {
            this.connected = false;

            this.s.getOutputStream().write(new String("\n******* Erreur login or password **********\n").getBytes());
            this.s.getOutputStream().flush();

            this.s.getOutputStream().write(new String("430").getBytes());
            this.s.getOutputStream().flush();
        }
    }

    public void processQUIT() throws IOException {
        this.connected = false;

        this.s.getOutputStream().write(new String("\n******** bye,bye : " + this.login + " *********\n").getBytes());
        this.s.getOutputStream().flush();
        s.close();
    }

    public String[] Spplit(String cmd) {

        String[] tab;
        tab = cmd.split(" ");
        return tab;
    }

    public void processRetr(String distance, Socket s) {
        System.out.println(".....encore de la réalisation -- RETR --");
    }

    public void processStor(String distance, Socket s) {
        System.out.println("...encore la réalisation -- STOR --");
    }

    public Socket initDataConnection() throws IOException {
        Socket SocketData = null;

        envoyerMessage("150 File status okay; about to open data connection.");

        if (isPassive()) {
            //le cas passive : le serveur qui détermine le port 
            //d'écoute et joue le role de Serveur pour le canal de données
            SocketData = new ServerSocket(0).accept();//recupérer port disponible
        } else {
            //le client FTP détermine le port d'écoute 
            //et joue le role de serveur pour le canal de données

            SocketData = new Socket();
            SocketData.connect(new InetSocketAddress(s.getInetAddress(), s.getPort()));

        }

        return SocketData;
    }

    public void processList(String fichier, Socket s) throws IOException {

        String FileName = "";
        String permission = "";
        String resultat = "";
        Date date =null;
        String userName = "";
        
        if (isConnected()) {
            envoyerMessage("150 Opening data channel for directory list.");
            
            Socket socketData = initDataConnection();

            
            OutputStream out = socketData.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);

            in.readLine();
            String tab[] = Spplit("");
            File[] files = new File(System.getProperty("user.dir")).listFiles();
            
            for(File file : files)
            {
                FileName =file.getName();
                date =new Date(file.lastModified());
                userName = Files.getOwner(file.toPath()).toString();
                
                if(file.isFile())
                    permission = "-rw-rw-rw-";
                else
                    permission = "drw-rw-rw-";
             
                resultat +=permission+"\t"+userName+"\t"+file.length()+"\t"+date+"\t"+FileName+"\n";   
            }
            
            dataOut.writeBytes(resultat+"\n");
            dataOut.close();
            
            
            envoyerMessage("226 Closing data connection.");
        }
    }

    public void run() {
        try {
            reponse = "";
            while (reponse != null) {

                //this.s.getOutputStream().write(new String("\n********* D *********\n").getBytes());
                //this.s.getOutputStream().flush();

                reponse = in.readLine();
                processRequest(reponse);

            }

        } catch (IOException er) {
            System.out.println("erreur : " + er);
        }
    }
}
