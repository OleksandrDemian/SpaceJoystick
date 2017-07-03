package com.zemian.spacejoystick;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.oleksandr.spacejoystick.R;

public class HomeActivity extends AppCompatActivity {

    public ConnectionScreen connectionFragment; //Connection fragment
    public ShipScreen shipFragment;             //Ship customization fragment
    public static HomeActivity instance;        //Instance of the activity

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        instance = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_how_to_play:
                String strGame = "This app is the joystick for Space Shooter Party. You can download it from official website:\nhttp://infinitysasha.altervista.org\nor Gamejolt:\nhttp://gamejolt.com/games/spaceshooter/265336.\n";
                String howTo = "\nControl that all the devices(PC and joysticks) are connected to the same network.\n";
                String initialize = "\nTo join the game type the IP shown by the game into IP's field and click join the game.";
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("How to play")
                        .setMessage(strGame + howTo + initialize)
                        .setCancelable(true)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.menu_ship_customization:
                mViewPager.setCurrentItem(1);
                break;
        }

        return true;
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