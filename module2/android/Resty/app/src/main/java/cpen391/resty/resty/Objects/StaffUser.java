package cpen391.resty.resty.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class StaffUser extends User {

    private String restaurantID;
    private String affinity;

    public StaffUser(){
        super();
        this.restaurantID = "";
        this.affinity = "";
    }

    public StaffUser(String username, String restaurantID, String affinity){
        super(username);
        this.affinity = affinity;
        this.restaurantID = restaurantID;

        validAffinityCheck();
        validRestaurantIDCheck();
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getAffinity() {
        return affinity;
    }

    private void validAffinityCheck(){
        if (!this.affinity.matches("staff") && !this.affinity.matches("staff_only"))
            throw new IllegalArgumentException();
    }

    private void validRestaurantIDCheck(){
        // TODO: Implement this
    }

    public static boolean validRestaurantIDCheck(String resID){
        // TODO: Implement this
        return !resID.isEmpty();
    }
}
