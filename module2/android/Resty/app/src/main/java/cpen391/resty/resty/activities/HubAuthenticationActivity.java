package cpen391.resty.resty.activities;

import android.os.Bundle;
import android.view.View;

import cpen391.resty.resty.R;
import cpen391.resty.resty.serverRequests.RestyMenuRequest;
import cpen391.resty.resty.utils.TestDataUtils;

public class HubAuthenticationActivity extends MainActivityBase {

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