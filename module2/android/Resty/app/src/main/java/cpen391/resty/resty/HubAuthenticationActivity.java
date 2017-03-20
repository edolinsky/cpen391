package cpen391.resty.resty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cpen391.resty.resty.ServerRequests.RestyMenuRequest;
import cpen391.resty.resty.utils.TestDataUtils;

public class HubAuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubverification);
    }

    /** Called when user taps authenticate button */
    public void authenticate(View view) {
        // pretend authentication successful

        // get menu
        RestyMenuRequest menuRequest = new RestyMenuRequest();
        menuRequest.getMenu(TestDataUtils.TEST_RESTAURANT, null, this);
    }

}
