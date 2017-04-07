package cpen391.resty.resty.Objects;

import com.google.gson.Gson;

public class StaffUser extends User {

    private String restaurant_id;

    public StaffUser(){
        super();
        this.restaurant_id = "";
        this.affinity = "";
    }

    public StaffUser(String username, String restaurantID, String affinity){
        super(username);
        this.affinity = affinity;
        this.restaurant_id = restaurantID;

        validAffinityCheck();
        validRestaurantIDCheck();
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static StaffUser getCurrentUser(){
        return (StaffUser) User.getCurrentUser();
    }

    public String getRestaurant_id() {
        return restaurant_id;
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
