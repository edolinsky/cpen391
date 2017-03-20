package cpen391.resty.resty.ServerRequests;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import cpen391.resty.resty.MenuActivity;
import cpen391.resty.resty.ServerRequests.ServerRequestConstants.Endpoint;

public class RestyMenuRequest extends RestyRequest {

    public static final String MENU = "cpen391.resty.resty.MENU";

    public void getMenu(final String restaurant_id, @Nullable String item_type, final Context context){

        if(item_type == null) {
            item_type = "";
        }

        String url = String.format(Endpoint.MENU.getUrl(), restaurant_id);
        StringRequest request = new StringRequest(Endpoint.MENU.getMethod(), url,
                createSuccessListener(context), createErrorListener());

        request.setRetryPolicy(new DefaultRetryPolicy(50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        makeRequest(request, context);

    }

    @Override
    public Response.Listener createSuccessListener(final Context context) {
        return new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra(MENU, response);
                context.startActivity(intent);

            }
        };
    }

    @Override
    public Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                 System.out.println("Hallo we have an error");
            }
        };
    }
}
