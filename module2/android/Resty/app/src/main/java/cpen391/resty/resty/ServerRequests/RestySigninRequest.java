package cpen391.resty.resty.ServerRequests;

import android.content.Context;
import android.content.Intent;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import cpen391.resty.resty.HubAuthenticationActivity;
import cpen391.resty.resty.ServerRequests.ServerRequestConstants.Endpoint;

public class RestySigninRequest {

    private static class LoginRequest{
        String user;
        String password;

        LoginRequest(String name, String password){
            this.user = name;
            this.password = password;
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
                (Endpoint.LOGIN.getMethod(), LOGIN_REQUEST_URL, requestObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Intent intent = new Intent(context, HubAuthenticationActivity.class);
                        context.startActivity(intent);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("Hallo we have an error");
                    }
        });

        RequestQueue mRequestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

        // Add the request to the RequestQueue.
        mRequestQueue.add(jsObjRequest);

    }

    /*public void signIn(final String username, final String password){

        Thread requestThread = new Thread(new Runnable() {
            @Override
            public void run() {

            final String LOGIN_REQUEST_URL = "http://piquemedia.me/login";
                LoginRequest request = new LoginRequest(username, password);
                String jsonRequest = request.toJson();
                System.out.println("Request is: " + jsonRequest);
                LoginResponse response = null;

                try {

                    HttpURLConnection connection;
                    URL url = new URL(LOGIN_REQUEST_URL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.connect();

                    int status = connection.getResponseCode();
                    System.out.println("Response Code 1 is " + status);

                    OutputStream ostream = connection.getOutputStream();
                    ostream.write(jsonRequest.getBytes("UTF-8"));

                    InputStream inStream = new BufferedInputStream(connection.getInputStream());
                    BufferedReader inBuf = new BufferedReader(new InputStreamReader(inStream));

                    String line;
                    StringBuilder responseString = new StringBuilder();
                    while ((line = inBuf.readLine()) != null)
                        responseString.append(line);

                    Gson gson = new Gson();
                    System.out.println("Response is " + responseString.toString());
                    response = gson.fromJson(responseString.toString(), LoginResponse.class);

                    inStream.close();
                    ostream.flush();
                    ostream.close();
                    connection.disconnect();

                }catch (Exception e){
                    e.printStackTrace();

                } finally {

                    if (response != null){
                        System.out.println("Success! Returned ID is ..");
                        System.out.println(response.id);
                    }else{
                        System.out.println("Failed, response is null");
                    }
                }
            }
        });

        requestThread.start();

    }*/

}
