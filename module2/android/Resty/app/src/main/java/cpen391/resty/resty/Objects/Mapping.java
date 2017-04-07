package cpen391.resty.resty.Objects;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by anna on 2017-04-07.
 */

public class Mapping {
    String restaurant_id;
    List<SingleMapping> mappings;


    public Mapping(String restaurant_id, List<SingleMapping> mapping){
        this.restaurant_id = restaurant_id;
        this.mappings = mapping;
    }

    public String getRestaurant_id(){
        return restaurant_id;
    }

    public List<SingleMapping> getMappings(){
        return mappings;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
