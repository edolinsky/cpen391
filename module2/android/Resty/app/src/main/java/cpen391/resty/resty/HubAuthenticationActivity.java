package cpen391.resty.resty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cpen391.resty.resty.utils.TestDataUtils;

public class HubAuthenticationActivity extends AppCompatActivity {

    public static final String MENU = "cpen391.resty.resty.MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubverification);
    }

    /** Called when user taps authenticate button */
    public void authenticate(View view) {
        // pretend authentication successful
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(MENU, TestDataUtils.TEST_MENU);
        startActivity(intent);
    }

}
