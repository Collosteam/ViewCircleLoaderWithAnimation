package com.collosteam.ViewCircleLoaderWithAnimation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.*;

import static android.graphics.Color.*;

public class TestActivity extends Activity implements SeekBar.OnSeekBarChangeListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        CircleLoaderView.PersentChangeListener,
        CircleLoaderView.AnimationListener {
    private static final String TAG = "{TestActivity}";
    TextView persentText;
    SeekBar seekBarStart;
    SeekBar seekBarEnd;
    CircleLoaderView circleLoaderView;
    CircleLoaderView circleLoaderView2;
    CircleLoaderView circleLoaderView3;
    NavigationDrawerFragment mNavigationDrawerFragment;
    Button buttonStartAnimation;
    Switch switchPosetive;

    int [] colors = new int[]{WHITE, YELLOW, GREEN, BLUE};

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
     //   mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        circleLoaderView = (CircleLoaderView) findViewById(R.id.circleLoaader);
        circleLoaderView2 = (CircleLoaderView) findViewById(R.id.circleLoaader2);
        circleLoaderView3 = (CircleLoaderView) findViewById(R.id.circleLoaader3);

        circleLoaderView.setStartAngle(0);
        circleLoaderView.setEndAngle(0);

        circleLoaderView2.setStartAngle(0);
        circleLoaderView2.setEndAngle(188);
        circleLoaderView2.setColor(Color.CYAN);

        circleLoaderView3.setStartAngle(0);
        circleLoaderView3.setEndAngle(55);
        circleLoaderView3.setColor(Color.MAGENTA);

        circleLoaderView.addPersentChangeListener(this);
        seekBarStart = (SeekBar) findViewById(R.id.seekBarStart);
        seekBarStart.setOnSeekBarChangeListener(this);
        seekBarEnd = (SeekBar) findViewById(R.id.seekBarEnd);
        seekBarEnd.setOnSeekBarChangeListener(this);
        persentText = (TextView) findViewById(R.id.textViewPersent);
        buttonStartAnimation = (Button) findViewById(R.id.buttonStartAnimation);
        buttonStartAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                circleLoaderView.startAnimation(1000);
                circleLoaderView2.startAnimation(1000);
                circleLoaderView3.startAnimation(1000);
            }
        });
        switchPosetive = (Switch)findViewById(R.id.switch1);
        switchPosetive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circleLoaderView.setPosetive(isChecked);
                circleLoaderView2.setPosetive(isChecked);
                circleLoaderView3.setPosetive(isChecked);

            }
        });

        circleLoaderView.addAnimationListener(this);
        circleLoaderView2.addAnimationListener(this);
        circleLoaderView3.addAnimationListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()){
            case R.id.seekBarStart:
               /* circleLoaderView.setStartAngle(progress);*/
                circleLoaderView.setStrokeWidth(progress);
                circleLoaderView2.setStrokeWidth(progress);
                circleLoaderView3.setStrokeWidth(progress);
                break;
            case R.id.seekBarEnd:
                circleLoaderView.setEndAngle(progress);
                break;

        }

        persentText.setText(String.valueOf(circleLoaderView.getPersent()).concat("%"));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(circleLoaderView!=null)
        circleLoaderView.setColor(colors[position]);
    }

    @Override
    public void onChange(int persent) {
        Log.d(TAG,"onChange : " + persent );
        if(persentText!=null)
            persentText.setText(String.valueOf(persent).concat("%"));
    }

    long startTime = 0;
    long stopTime = 0;

    @Override
    public void startAnimation(CircleLoaderView circleLoaderView) {
         Log.d(TAG, "Animation Start : "+ (startTime = System.currentTimeMillis()));
    }

    @Override
    public void onProgressAnimation(CircleLoaderView circleLoaderView, float progress) {
        Log.d(TAG, "Animation progres : "+progress);
    }

    @Override
    public void stopAnimation(CircleLoaderView circleLoaderView) {
        Log.d(TAG, "Animation Stop : "+((stopTime = System.currentTimeMillis())-startTime) + "  id : " + circleLoaderView.getId() );
    }
}
