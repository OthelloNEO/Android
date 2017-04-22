package com.othello.neo.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
 //       textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
