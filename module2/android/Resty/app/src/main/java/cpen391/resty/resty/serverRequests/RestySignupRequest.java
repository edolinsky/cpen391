package cpen391.resty.resty.serverRequests;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import org.json.JSONObject;
import cpen391.resty.resty.activities.HubAuthenticationActivity;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyLoginCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestySignupCallback;

public class RestySignupRequest{

    final RestySignupCallback signupCallback;

    public RestySignupRequest(RestySignupCallback signupCallback){
        this.signupCallback = signupCallback;
    }

    public void signUp(final String username, final String password){

        final String SIGNUP_REQUEST_URL = Endpoint.SIGNUP.getUrl();
        JSONObject requestObject = null;

        try{
            requestObject = new JSONObject(User.getLoginJsonObject(username, password));
        }catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Endpoint.SIGNUP.getMethod(), SIGNUP_REQUEST_URL, requestObject,
                        listener, errorListener);
        RestyRequestManager.getInstance().makeRequest(jsObjRequest);

    }

    private final Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){
        @Override
        public void onResponse(JSONObject response) {
            signupCallback.signupCompleted(new User());
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("Login Error", error.toString());
            signupCallback.signupError(RestySignupCallback.SignupError.unknownError);
        }
    };



}
