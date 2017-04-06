package cpen391.resty.resty.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;

import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.R;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.RestySignupRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestySignupCallback;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private static final int SERVER_ERROR = 500;

    private EditText usernameText;
    private EditText passwordText;
    private EditText restaurantIDField;
    private CompoundButton staffOnlyCheckbox;
    private Switch staffSwitch;
    private RestyStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dataStore = RestyStore.getInstance();

        usernameText = (EditText) findViewById(R.id.signupNameText);
        passwordText = (EditText) findViewById(R.id.signupPasswordText);
        restaurantIDField = (EditText) findViewById(R.id.SignupRestaurantIDText);
        staffOnlyCheckbox = (CompoundButton) findViewById(R.id.checkBox);
        staffSwitch = (Switch) findViewById(R.id.signupStaffSwitch);

        // Staff fields default to hidden.
        restaurantIDField.setVisibility(View.GONE);
        staffOnlyCheckbox.setVisibility(View.GONE);

        // Show/Hide relevant fields based on status of switch.
        staffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show restaurant ID field, staff account checkbox.
                    restaurantIDField.setVisibility(View.VISIBLE);
                    staffOnlyCheckbox.setVisibility(View.VISIBLE);
                } else {
                    // Hide restaurant ID field, staff account checkbox.
                    restaurantIDField.setVisibility(View.GONE);
                    staffOnlyCheckbox.setVisibility(View.GONE);
                }
            }
        });

    }

    public void signupOnClick(View view){

        String username = usernameText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String resID = restaurantIDField.getText().toString().trim();

        int duration = Toast.LENGTH_LONG;
        CharSequence text;
        if(!LoginActivity.isValidEmail(username)) {
            text = "Invalid email address.";
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        } else if (password.isEmpty()) {
            text = "Please enter a password.";
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        }else if (staffSwitch.isChecked() && !StaffUser.validRestaurantIDCheck(resID)){
            text = "Invalid restaurant ID";
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            return;
        }

        // If successful, hash password
        final String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();

        // Store username for later use.
        dataStore.put(RestyStore.Key.USER, username);

        if (staffSwitch.isChecked()){ // staff account

            RestySignupRequest signupRequest = new RestySignupRequest(signupCallback);
            signupRequest.staffSignUp(username, hashedPassword, resID, staffOnlyCheckbox.isChecked());

        }else{ // customer account

            RestySignupRequest signupRequest = new RestySignupRequest(signupCallback);
            signupRequest.signUp(username, hashedPassword);
        }
    }

    public void signupOnSuccess(User user, boolean isStaff){
        String affinity = user.getAffinity();
        Intent intent;
        switch (affinity){
            case "customer":
                intent = new Intent(this, HubAuthenticationActivity.class);
                break;
            case "staff":
                intent = new Intent(this, StaffPickusageActivity.class);
                break;
            case "staff_only":
                intent = new Intent(this, StaffMainActivity.class);
                break;
            default:
                throw new IllegalArgumentException();
        }
        startActivity(intent);
    }

    private void handleSignupError(VolleyError error){
        CharSequence text;
        int duration = Toast.LENGTH_LONG;

        switch (error.networkResponse.statusCode){
            case SERVER_ERROR:
                    text =  "Server Error. Please try again.";
                break;
            default:
                try {
                    JsonObject jsonObject = new JsonParser().parse(new String(error.networkResponse.data)).getAsJsonObject();
                    text = jsonObject.get("error").getAsString();
                } catch (JsonParseException e) {
                    text = "Something went wrong. Please try again.";
                }
        }

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    private RestySignupCallback signupCallback = new RestySignupCallback() {
        @Override
        public void signupCompleted(User user, boolean isStaff) {
            signupOnSuccess(user, isStaff);
        }

        @Override
        public void signupError(VolleyError error) {
            handleSignupError(error);
        }
    };

}
