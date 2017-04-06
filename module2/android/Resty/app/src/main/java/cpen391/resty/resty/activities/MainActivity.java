package cpen391.resty.resty.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cpen391.resty.resty.R;
import cpen391.resty.resty.menu.OrderDialog;

public class MainActivity extends AppCompatActivity implements HubAuthenticationFragment.HubAuthListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            HubAuthenticationFragment firstFragment = new HubAuthenticationFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void menuClick(View view) {
        Fragment fragment = new HubAuthenticationFragment();
        loadFragment(fragment);
    }

    public void mapsClick(View view) {
        // not yet implemented

    }

    public void settingsClick(View view) {
        // not yet implemented
    }

    private void loadFragment(Fragment newFragment) {
        Bundle args = new Bundle();
//        args.putInt(ArticleFragment.ARG_POSITION, position);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAuth() {
        Fragment fragment = new MenuFragment();
        loadFragment(fragment);
    }
}
