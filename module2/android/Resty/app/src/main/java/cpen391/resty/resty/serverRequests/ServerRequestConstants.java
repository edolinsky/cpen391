package cpen391.resty.resty.serverRequests;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PATCH;
import static com.android.volley.Request.Method.POST;

public class ServerRequestConstants {

    public enum Endpoint {
        /** workflow endpoints */
        LOGIN ("login", POST),
        SIGNUP("signup", POST),
        MENU("menu?restaurant_id=%1$s", GET),
        GET_ORDER("order?order_id=%1$s&customer_id=%2$s&restaurant_id=%3$s&table_id=%4$s", GET),
        PLACE_ORDER("order", POST),
        VIEW_ORDERS("orders?restaurant_id=%1$s&query=%2$s", GET),
        UPDATE_ORDERS("orders", PATCH),
        GET_TABLE_ATTENDANTS("server_hub_map?restaurant_id=%1$s", GET),
        EDIT_TABLE_ATTENDANTS("server_hub_map", POST),
        REMOVE_ATTENDANT_FROM_TABLES("server_hub_map?restaurant_id=%1$s&attendant_id=%2$s", DELETE),

        /** Internal Endpoints **/
        UPDATE_FCM_ID("update_fcm_id", POST),

        /** etc */
        HELLO("hello", GET),
        TEAPOT("teapot", GET);

        private final String endpoint;
        private final int method;

        Endpoint(String endpoint, int method) {
            this.endpoint = endpoint;
            this.method = method;
        }
        public static final String SERVER_URL = "http://piquemedia.me/";

        public String getUrl() {
            return SERVER_URL + endpoint;
        }

        public int getMethod() {
            return method;
        }
    }
}
