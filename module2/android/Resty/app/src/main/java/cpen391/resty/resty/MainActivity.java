package cpen391.resty.resty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private Button signupButton;

    private static class LoginRequest{
        String name;
        String password;

        LoginRequest(String name, String password){
            this.name = name;
            this.password = password;
        }

        String toJson(){
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameText = (EditText) findViewById(R.id.loginNameText);
        passwordText = (EditText) findViewById(R.id.loginPasswordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.loginSignupButton);
    }

    public void loginOnClick(View view){

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        signIn(new LoginRequest(username, password));
    }

    public void signupOnClick(View view){

    }

    public void signIn(LoginRequest request){

        String jsonRequest = request.toJson();

        try{
           // HttpPost

        }catch (Exception e){
            System.out.println(e);
        }

        // pretend sign-in request was successful
        // switch to main screen

        Intent intent = new Intent(this, HubAuthenticationActivity.class);
        startActivity(intent);

    }
}
