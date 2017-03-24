package cpen391.resty.resty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivityBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void menuClick(View view) {
        if(!(this instanceof HubAuthenticationActivity)){
            // if not authenticated
            Intent intent = new Intent(this, HubAuthenticationActivity.class);
            startActivity(intent);

            // if authenticated
//            Intent intent = new Intent(this, MenuActivity.class);
//            startActivity(intent);
        }

    }

    public void mapsClick(View view) {
        if(!(this instanceof MapsActivity)){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

    }

    public void settingsClick(View view) {
        // not yet implemented
    }
}
