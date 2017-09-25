/*
   Marouane ABAKARIM
   Mohamed ADANSAR
 */

package server;

import Admin.DifinitionServer;
import java.io.*;
import java.net.*;
import java.util.logging.*;


public class server {
	

    public void executer()
    {
        DifinitionServer toolServer=new DifinitionServer();
        try {
            ServerSocket serverSocket = new ServerSocket(toolServer.getPort());
            System.out.println("le serveur à l'ecoute sur le port : ");
            
            while(true)
            {
                Socket socket = serverSocket.accept();
                Thread T = new Thread(new FtpRequest(socket,toolServer.getDirectoryServer()));
                T.start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
	
	public static void main(String[] args) {
            
            server sr =new server();
            //sr.init_map();
            sr.executer();
	}
	
	
}
