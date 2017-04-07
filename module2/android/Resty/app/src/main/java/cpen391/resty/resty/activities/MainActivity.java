package cpen391.resty.resty.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.Fragments.HubAuthenticationFragment;
import cpen391.resty.resty.activities.Fragments.HubAuthenticationFragment.HubAuthListener;
import cpen391.resty.resty.activities.Fragments.MapsFragment;
import cpen391.resty.resty.activities.Fragments.MenuFragment;
import cpen391.resty.resty.activities.Fragments.MenuFragment.MenuBackListener;
import cpen391.resty.resty.activities.Fragments.SettingsFragment;
import cpen391.resty.resty.dataStore.RestyStore;

public class MainActivity extends AppCompatActivity implements HubAuthListener, MenuBackListener {

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
        if(RestyStore.getInstance().getBoolean(RestyStore.Key.HUB_AUTH)) {
            gotoMenu();
        } else {
            gotoAuth();
        }
    }

    public void mapsClick(View view) {
        gotoMaps();
    }

    public void settingsClick(View view) {
        gotoSettings();
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

    private void gotoAuth() {
        loadFragment(new HubAuthenticationFragment());
    }

    private void gotoMenu() {
        loadFragment(new MenuFragment());
    }

    private void gotoMaps() {
        loadFragment(new MapsFragment());
    }

    private void gotoSettings() {
        loadFragment(new SettingsFragment());
    }

    @Override
    public void onAuth() {
        gotoMenu();
    }

    @Override
    public void backFromMenu() {
        RestyStore.getInstance().put(RestyStore.Key.HUB_AUTH, false);
        gotoAuth();
    }
}
