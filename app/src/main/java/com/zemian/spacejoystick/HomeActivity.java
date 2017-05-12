package com.zemian.spacejoystick;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.oleksandr.spacejoystick.R;

public class HomeActivity extends AppCompatActivity {

    public ConnectionScreen connectionFragment; //Connection fragment
    public ShipScreen shipFragment;             //Ship customization fragment
    public static HomeActivity instance;        //Instance of the activity

    private SectionsPagerAdapter mSectionsPagerAdapter;     //Fragments adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        instance = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragments adapter
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    connectionFragment = new ConnectionScreen();
                    return connectionFragment;
                case 1:
                    shipFragment = new ShipScreen();
                    return shipFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
