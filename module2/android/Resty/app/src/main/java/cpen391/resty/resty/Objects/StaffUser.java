package cpen391.resty.resty.Objects;

public class StaffUser extends User {

    public enum affinity{

    }

    private String restaurantID;
    private String affinity;

    public StaffUser(String username, String password){
        super(username, password);

    }
}
