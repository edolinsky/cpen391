package cpen391.resty.resty.serverRequests.serverCallbacks;

import com.android.volley.VolleyError;

import java.util.ArrayList;

import cpen391.resty.resty.Objects.Table;
import cpen391.resty.resty.Objects.respTable;


public interface RestyTableCallback {

    void tablesRetrieved(ArrayList<respTable> table);
    //void tablesRetrieved(String table);
    void tablesError(VolleyError error);
}