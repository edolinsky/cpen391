package cpen391.resty.resty.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.R;

public class StaffMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        TextView topBar = (TextView) findViewById(R.id.StaffMainHeaderTextview);
        StaffUser user = (StaffUser) User.getCurrentUser();
        String title = "Welcome, " + user.getUser().split("@")[0];
        topBar.setText(title);
        topBar.setMaxLines(1);

    }


}
