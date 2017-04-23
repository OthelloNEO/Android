package com.othello.neo.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tomoyuki on 2017/04/22.
 */

public class SelectGameActivity extends Fragment {
    public SelectGameActivity(){

    }
    private static final String ARG_SECTION_NUMBER = "section_number";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flagment_select_game, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button button = (Button)getActivity().findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), GameMainActivity.class);
                //startActivity(intent);
                Toast.makeText(getActivity(), "Intent to GameMainActivity", Toast.LENGTH_SHORT).show();


            }
        });

    }
}
