package cpen391.resty.resty.Objects;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import static android.R.attr.name;
import static android.R.attr.password;


public class User {

    private String user;
    private String android_reg_id;

    public String getUser() {
        return user;
    }

    public String getAndroid_reg_id() {
        return android_reg_id;
    }

    public User(){
        user = "";
        this.android_reg_id = FirebaseInstanceId.getInstance().getToken();
    }

    User(String name) {
        this.user = name;
        this.android_reg_id = FirebaseInstanceId.getInstance().getToken();
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
            return loginObj.toString();
        }catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

}
