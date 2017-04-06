package cpen391.resty.resty.Objects;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import static android.R.attr.name;
import static android.R.attr.password;


public class User extends GsonSerializable {

    // after sign in, use User.getCurrentUser to get this object from anywhere
    // cast to StaffUser if signed in as staff
    private static User currentUser = null;

    private String user;
            String affinity;
    private String android_reg_id;

    public String getUser() {
        return user;
    }

    public String getAndroid_reg_id() {
        return android_reg_id;
    }

    User(){
        user = "";
        affinity = "";
        this.android_reg_id = FirebaseInstanceId.getInstance().getToken();
    }

    User(String name) {
        this.user = name;
        this.affinity = "";
        this.android_reg_id = FirebaseInstanceId.getInstance().getToken();
    }

    public String getAffinity() {
        return affinity;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     *
     * @param username for login
     * @param password for login
     * @return
     * A static method to return a json request object to be used when signing in using username and password
     */
    public static String getLoginJsonObject(String username, String password){
        try {
            JSONObject loginObj = new JSONObject(new User(username).toJson());
            loginObj.put("password", password);
            loginObj.remove("affinity"); // dont need for login
            return loginObj.toString();
        }catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the current user, or null if not signed in
     * Note: cast current user to StaffUser if signed in as staff
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set current user after login
     * @param currentUser the newly logged in user
     */
    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

}
