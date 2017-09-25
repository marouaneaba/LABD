package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import java.util.Map;


public class server {
	
	public static final Map<String, String> users = new HashMap<>();
        protected static String directoryServer = "C:/Users/abk/server";
    public int port;
    public boolean start;
    
    public void init_map()
    {
        users.put("MAROUANE","MAROUANE");
        users.put("ABA","ABA");
        users.put("a","a");
    }
    
    public server()
    {
        this.port = 21;
        this.start = true;
    }
    
    public static void ajouterUser(String us,String pass)
    {
        users.put(us,pass);
    }
    public static Map<String,String> getUsers()
    {
        return users;
    }
    
    public String getDirectoryServer()
    {
        return directoryServer;
    }
    
    public void executer()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("le serveur à l'ecoute sur le port : "+port);
            
            while(true)
            {
                Socket socket = serverSocket.accept();
                Thread T = new Thread(new FtpRequest(socket,users,directoryServer));
                T.start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
	
	public static void main(String[] args) {
            
            server sr =new server();
            sr.init_map();
            sr.executer();
	}
	
	
}
