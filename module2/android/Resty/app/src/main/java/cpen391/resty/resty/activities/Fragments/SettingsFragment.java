package cpen391.resty.resty.activities.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.LoginActivity;
import cpen391.resty.resty.dataStore.RestyStore;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        View view = inflater.inflate(R.layout.settings, container, false);
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RestyStore.getInstance().clear();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
