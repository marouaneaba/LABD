/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

/**
 *
 * @author abk
 */
public class Commandes {

    public static final String USER_OK = "331 User name okay, need password";
    public static final String USER_KO = "530 Bad username";
    public static final String USER_DEJA_CONNECTED = "230 Already logged in";
    public static final String PASS_KO = "530 Not logged in";
    public static final String PASS_OK = "257 \"";
    public static final String QUIT = "231 Bye";
    public static final String LIST = "226 LIST Done";
    public static final String RETR = "226 Transfer complete.";
    public static final String STOR = "226 Transfer complete.";
    public static final String UNKOWN_CMD = "226 Transfer complete.";

}
