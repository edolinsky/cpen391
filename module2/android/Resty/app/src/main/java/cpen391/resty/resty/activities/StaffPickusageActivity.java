package cpen391.resty.resty.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cpen391.resty.resty.R;

public class StaffPickusageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_pickusage);

        Button pickCustomerButton = (Button) findViewById(R.id.staffpickCustomerButton);
        Button pickStaffButton = (Button) findViewById(R.id.staffpickStaffButton);
        final Context context = this;

        pickCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HubAuthenticationFragment.class);
                startActivity(intent);
            }
        });

        pickStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StaffMainActivity.class);
                startActivity(intent);
            }
        });

    }

}