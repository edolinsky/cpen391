package cpen391.resty.resty.serverRequests;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cpen391.resty.resty.Objects.Table;
import cpen391.resty.resty.Objects.respTable;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyTableCallback;

public class RestyTableRequest {
    public static final String TABLES = "cpen391.resty.resty.TABLES";

    private final RestyTableCallback tablesCallback;
    private RestyStore dataStore;

    public RestyTableRequest(RestyTableCallback callback){
        this.tablesCallback = callback;
        this.dataStore = RestyStore.getInstance();
    }

    public void getTables(final String table_id){

        String url = String.format(ServerRequestConstants.Endpoint.GET_TABLE_ATTENDANTS.getUrl(), table_id);
        StringRequest request = new StringRequest(ServerRequestConstants.Endpoint.GET_TABLE_ATTENDANTS.getMethod(), url,
                listener, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RestyRequestManager.getInstance().makeRequest(request);

    }

    private final Response.Listener<String> listener = new Response.Listener<String>(){
        @Override
        public void onResponse(String response) {

            Log.i("Fetched tables", response);
            JsonParser parser = new JsonParser();
            JsonObject jsonMenu = (JsonObject)parser.parse(response);

            Gson gson = new Gson();
            Type rstableType = new TypeToken<List<respTable>>(){}.getType();

            ArrayList<respTable> tables = gson.fromJson(jsonMenu.getAsJsonArray("mappings"), rstableType);


            dataStore.put("Tables", tables.size());

            for(int i = 1; i <= tables.size(); i++){
                dataStore.put("Table" + Integer.toString(i), tables.get(i-1).getEmail());
                //dataStore.put(Integer.toString(i), tables.get(i-1).getAttendant_id());
            }

            tablesCallback.tablesRetrieved(tables);
        }
    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            tablesCallback.tablesError(error);
        }
    };
}