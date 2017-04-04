package cpen391.resty.resty.Objects;


import com.google.gson.Gson;

abstract class GsonSerializable {

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
