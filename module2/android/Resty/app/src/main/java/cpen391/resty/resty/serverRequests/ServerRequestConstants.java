package cpen391.resty.resty.serverRequests;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.PATCH;
import static com.android.volley.Request.Method.POST;

public class ServerRequestConstants {

    public enum Endpoint {
        /** workflow endpoints */
        LOGIN ("login", POST),
        SIGNUP("signup", POST),
        MENU("menu?restaurant_id=%1$s", GET),
        GET_ORDER("order", GET),
        PLACE_ORDER("order", POST),
        VIEW_ORDERS("orders", GET),
        UPDATE_ORDERS("orers", PATCH),

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
