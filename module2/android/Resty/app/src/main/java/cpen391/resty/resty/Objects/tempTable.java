package cpen391.resty.resty.Objects;

/**
 * Created by annal on 2017-04-05.
 */

public class tempTable {

    int id;
    String servers;
    boolean selected;

    public tempTable(int id, String servers, boolean selected){
        this.id = id;
        this.servers = servers;
        this.selected = selected;
    }

    public void setId(int i){
        this.id = i;
    }

    public void setServers(String s){
        this.servers = s;
    }

    public void setSelected(boolean b){
        this.selected = b;
    }

    public int getId(){
        return id;
    }

    public String getServers(){
        return servers;
    }

    public boolean isSelected(){
        return selected;
    }
}
