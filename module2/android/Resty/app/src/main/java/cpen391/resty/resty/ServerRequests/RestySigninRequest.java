package cpen391.resty.resty.ServerRequests;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONObject;

import cpen391.resty.resty.HubAuthenticationActivity;
import cpen391.resty.resty.ServerRequests.ServerRequestConstants.Endpoint;

public class RestySigninRequest extends RestyRequest {

    private static class LoginRequest{
        String user;
        String password;
        String android_reg_id;

        LoginRequest(String name, String password){
            this.user = name;
            this.password = password;
            this.android_reg_id = FirebaseInstanceId.getInstance().getToken();
        }

        String toJson(){
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    private static class LoginResponse{
        String affinity;
        String id;
        String restaurant_id;
        String user;
    }

    public void signIn(final String username, final String password, final Context context){

        final String LOGIN_REQUEST_URL = Endpoint.LOGIN.getUrl();
        LoginRequest request = new LoginRequest(username, password);
        String jsonRequest = request.toJson();
        JSONObject requestObject = null;

        try{
            requestObject = new JSONObject(jsonRequest);
        }catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Endpoint.LOGIN.getMethod(), LOGIN_REQUEST_URL, requestObject,
                        createSuccessListener(context), createErrorListener());
        makeRequest(jsObjRequest, context);

    }

    @Override
    public Response.Listener createSuccessListener(final Context context) {
        return new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Intent intent = new Intent(context, HubAuthenticationActivity.class);
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
