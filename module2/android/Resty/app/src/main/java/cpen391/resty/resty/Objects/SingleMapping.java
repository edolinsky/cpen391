package cpen391.resty.resty.Objects;

/**
 * Created by annal on 2017-04-07.
 */

public class SingleMapping {
    String attendant_id;
    String table_id;

    public SingleMapping(String attendant_id, String table_id){
        this.attendant_id = attendant_id;
        this.table_id = table_id;
    }

    public String getAttendant_id(){
        return attendant_id;
    }

    public String getTable_id(){
        return table_id;
    }
}
