package com.othello.neo.android;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Tomoyuki on 2017/03/04.
 */

public class SelectMenuActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);
        //setContentView(R.layout.activity_main);

        Log.d("activity", "intent SelectMenuActivity");
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
    }
}