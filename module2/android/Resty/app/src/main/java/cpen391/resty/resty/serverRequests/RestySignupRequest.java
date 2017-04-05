package cpen391.resty.resty.serverRequests;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.serverRequests.ServerRequestConstants.Endpoint;
import org.json.JSONObject;

import java.util.IllegalFormatException;

import cpen391.resty.resty.activities.HubAuthenticationActivity;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyLoginCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestySignupCallback;

import static com.android.volley.Request.Method.HEAD;

public class RestySignupRequest{

    final RestySignupCallback signupCallback;

    public RestySignupRequest(RestySignupCallback signupCallback){
        this.signupCallback = signupCallback;
    }

    /**
     * Signup for a basic account for customers
     * @param username username TODO: check for username uniqueness
     * @param password  password TODO: add restrictions on password length and characters
     */
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

    /**
     * TODO: TEST THIS
     * Signup for restaurant staff
     * @param username username
     * @param password password
     * @param restaurant_id the id assigned to the restaurant
     * @param staffonly pass true if user wants to use account as staff only, and never as a customer
     */
    public void staffSignUp(final String username, final String password, final String restaurant_id, boolean staffonly){

        final String SIGNUP_REQUEST_URL = Endpoint.SIGNUP.getUrl();
        JSONObject requestObject = null;

        String affinity = staffonly ? "staff_only":"staff";

        try{
            requestObject = new JSONObject(getSignupJsonObject(username, password, restaurant_id, affinity));
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

            try {
                User resultUser;
                Gson gson = new Gson();
                String affinity = (String) response.get("affinity");

                switch (affinity){
                    case "staff":
                    case "staff_only":
                        resultUser = gson.fromJson(response.toString(), StaffUser.class);
                        signupCallback.signupCompleted(resultUser, true);
                        break;
                    case "customer":
                        resultUser = gson.fromJson(response.toString(), User.class);
                        signupCallback.signupCompleted(resultUser, false);
                        break;
                    default:
                        // this should never happen
                        throw new IllegalArgumentException();
                }

            }catch (Exception e) {
                // this should never happen
                throw new IllegalArgumentException();
            }
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            signupCallback.signupError(error);
        }
    };

    private static String getSignupJsonObject(String username, String password, String restaurantID, String affinity){
        try {
            JSONObject signupObj = new JSONObject(new StaffUser(username, restaurantID, affinity).toJson());
            signupObj.put("password", password);
            return signupObj.toString();
        }catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

}
