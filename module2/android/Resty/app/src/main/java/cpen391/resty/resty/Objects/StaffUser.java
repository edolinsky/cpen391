package cpen391.resty.resty.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class StaffUser extends User {

    private String restaurantID;
    private String affinity;

    public StaffUser(String username, String password, String restaurantID, String affinity){
        super(username, password);
        this.affinity = affinity;
        this.restaurantID = restaurantID;

        validAffinityCheck();
        validResaurantIDCheck();
    }

    String toJson(){
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

    private void validResaurantIDCheck(){
        // TODO: IMPLEMENT THIS
    }

    public static String getSignupJsonObject(String username, String password, String restaurantID, String affinity){
        return new StaffUser(username, password, restaurantID, affinity).toJson();
    }
}
