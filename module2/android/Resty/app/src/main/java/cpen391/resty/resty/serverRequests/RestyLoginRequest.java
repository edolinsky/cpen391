package cpen391.resty.resty.serverRequests;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.activities.HubAuthenticationActivity;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyLoginCallback;


public class RestyLoginRequest {

    private final RestyLoginCallback loginCallback;

    public RestyLoginRequest(RestyLoginCallback callback){
        this.loginCallback = callback;
    }

    public void signIn(final String username, final String password){

        final String LOGIN_REQUEST_URL = Endpoint.LOGIN.getUrl();
        JSONObject requestObject = null;


        try{
            requestObject = new JSONObject(User.getLoginJsonObject(username, password));
        }catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Endpoint.LOGIN.getMethod(), LOGIN_REQUEST_URL, requestObject,
                        listener, errorListener);
        RestyRequestManager.getInstance().makeRequest(jsObjRequest);

    }

    private final Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            loginCallback.loginCompleted(null);
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("Login Error", error.toString());
            loginCallback.loginError(error);
        }
    };

}
