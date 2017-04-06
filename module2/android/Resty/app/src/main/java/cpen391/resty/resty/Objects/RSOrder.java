package cpen391.resty.resty.Objects;

/*
This class is for the Order items as received from the orders get method,
called from the restaurant side of the app, hence the RS in the name
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.order;

public class RSOrder{

    private String customer_name;
    private String id;
    private String order_id;
    private String menu_id;
    private String name;
    private String status;
    private String table_id;

    public RSOrder(){
        customer_name = "";
        id = "";
        order_id = "";
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

    public String getOrder_id() {
        return order_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static JSONObject createJSONOrdersPatch(RSOrder[] orders, String restaurant_id){
        JSONObject obj = new JSONObject();
        try {

            obj.put("restaurant_id", restaurant_id);
            Gson gson = new Gson();
            String updates = gson.toJson(getpatches(orders)).replaceAll("/", "");
            obj.put("items", new JSONArray(updates));

        }catch (Exception e){
            throw new IllegalArgumentException();
        }

        return obj;
    }

    private static OrderPatch[] getpatches(RSOrder[] orders) {
        OrderPatch[] retval = new OrderPatch[orders.length];
        int i = 0;
        for (RSOrder order : orders) {
            retval[i] = new OrderPatch(order.id, order.order_id, order.status);
            i++;
        }
        return retval;
    }


    private static class OrderPatch{

        String id;
        String order_id;
        String status;

        OrderPatch(String id, String order_id, String status){
            this.id = id;
            this.order_id = order_id;
            this.status = status;
        }
    }

}
