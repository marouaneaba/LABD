/*
   Marouane ABAKARIM
   Mohamed ADANSAR
 */
package Admin;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author amk
 */
public class UserClass {

    public final Map<String, String> users = new HashMap<>();
    public String user, passwrd;

    public UserClass() {
        users.put("MAROUANE", "MAROUANE");
        users.put("ABA", "ABA");
        users.put("a", "a");
    }

    public boolean UserIsCorrect(String user) {
        this.user = user;
        return this.users.containsKey(user);
    }

    public boolean PassIsCorrect(String pass) {
        this.passwrd = pass;
        return this.users.get(this.user).equals(pass);
    }

    public String getCurrentPass() {
        return this.passwrd;
    }

    public String getCurrentUser() {
        return this.user;
    }

}
