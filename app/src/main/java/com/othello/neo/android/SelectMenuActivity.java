package com.othello.neo.android;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by Tomoyuki on 2017/03/04.
 */

public class SelectMenuActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);
        Log.d("activity", "intent SelectMenuActivity");

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        //ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);
        //viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        //tabLayout.setupWithViewPager(viewPager);

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case 0:
                    Log.d("activity", "Page 0");
                    //fragment = new FirstFragment();
                    break;
                case 1:
                    Log.d("activity", "Page 1");
                    //fragment = new SecondFragment();
                    break;
                case 2:
                    Log.d("activity", "Page 2");
                    //fragment = new ThirdFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "タブ"+ (position+1);
        }
    }

}