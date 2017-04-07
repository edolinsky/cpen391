package cpen391.resty.resty.Objects;

import com.google.gson.Gson;

/**
 * Created by annal on 2017-04-06.
 */

public class respTable {
    String attendant_id;
    String table_id;
    String table_name;
    String email;

    public respTable(String attendant_id, String table_id, String table_name, String email){
        this.attendant_id = attendant_id;
        this.table_id = table_id;
        this.table_name = table_name;
        this.email = email;
    }

    public static respTable create(String attendant_id, String table_id, String table_name, String email){
        respTable ret = new respTable(attendant_id, table_id, table_name, email);

        return ret;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getAttendant_id(){
        return attendant_id;
    }

    public String getTable_id(){
        return table_id;
    }

    public String getTable_name(){
        return table_name;
    }

    public String getEmail(){
        return email;
    }
}
