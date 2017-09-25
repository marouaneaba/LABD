/*
   Marouane ABAKARIM
   Mohamed ADANSAR
 */
package Admin;

/**
 *
 * @author amk
 */
public class Commandes {

    /* Welcome */
    public final static String WELCOME = "220 Welcome on the FTP Server.\r\n";

    /* Commande USER */
    public final static String USER_OK = "331 User name okay, need password\r\n";
    public final static String USER_KO = "530 Bad User Name.\r\n";
    /* Commande PASS */
    public final static String PASS_OK = "230 Login successful.\r\n";
    public final static String PASS_KO = "430 authentication failed.\r\n";
    public final static String ERR_CHANGE_USER ="530 Can't change from guest user\r\n";

    /* Commande LIST */
    public final static String LIST_MSG_BAD_REP = "504 Paramêtre invalide :(.\r\n";
    public final static String LIST_OK = "150 listing.\r\n";
    public final static String LIST_FIN = "226 Directory send OK..\r\n";

    /* Commande SYST */
    public final static String SYST_OK = "215 UNIX.\r\n";

    /* Commande CDUP */
    public final static String CDUP_OK = "250 Vous êtes maintenant dans .\r\n";

    /* Commande CWD */
    public final static String CWD_OK = "250 Vous êtes dans le répertoire .\r\n";
    public final static String CWD_MSG_NOT_REP = "550 Le répertoire %s n'existe pas :(.\r\n";

    /* Commande PORT */
    public final static String PORT_MSG_OK = "200 Ouverture du port .\r\n";

    /* Commande PWD */
    public final static String PWD_MSG_OK = "257 .\r\n";

    /* Commande QUIT */
    public final static String QUIT_OK = "221 QUIT bye bye.\r\n";

    /* Commande RETR */
    public final static String RETR_IS_DIRECTORY = "Oh noze, c'est un répertoire :(.\r\n";
    public final static String RETR_START_TRANSFERT = "150 Début du transfert en mode ASCII. Amazing!.\r\n";
    public final static String RETR_NOT_EXIST = "550 Oh noze, le fichier n'existe pas :(.\r\n";
    public final static String RETR_END_TRANSFER = "226 Transfert terminé!.\r\n";

    /* Commande STOR */
    public final static String STOR_START_TRANSFERT = "125 Début du transfert.\r\n";
    public final static String STOR_END_TRANSFERT = "226 Transfert fini :).\r\n";
    public final static String STOR_ERR_TRANSFERT = "451 Une erreur est survenue .\r\n";

    /* Commande MKDIR */
    public final static String MKD_OK = "257 Répertoire crée.\r\n";
    public final static String MKD_KO = "550 Impossible de crée le répertoire %s :( .\r\n";

    /* Commande DELETE */
    public final static String DELE_OK = "200 Répertoire supprimé.\r\n";
    public final static String DELE_KO = "550 Impossible de supprimer le fichier/répertoire.\r\n";
    public final static String DELE_ERR = "550 Le fichier/répertoire n'existe pas.\r\n";

    /* Strange */
    public final static String STRANGE = "421 Une erreur est survenue.\r\n";

    /* Error  */
    public final static String ERR_ABORTED = "451 Une erreur est survenue :(. On annule..\r\n";
    public final static String ERR_BAD_CMD = "502 Command Invalid.\r\n";
    public final static String ERR_CANNOT_OPEN_DATA = "425 Argh, j'arrive pas à ouvrir le canal de data.\r\n";

}
