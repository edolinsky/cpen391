package cpen391.resty.resty.Objects;

/*
This class is for the Order items as received from the orders get method,
called from the restaurant side of the app, hence the RS in the name
 */

import java.util.ArrayList;
import java.util.List;

public class RSOrder {

    private String customer_name;
    private String id;
    private String menu_id;
    private String name;
    private String status;
    private String table_id;

    public RSOrder(){
        customer_name = "";
        id = "";
        menu_id = "";
        name = "";
        status = "";
        table_id = "";
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getId() {
        return id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTable_id() {
        return table_id;
    }

}
