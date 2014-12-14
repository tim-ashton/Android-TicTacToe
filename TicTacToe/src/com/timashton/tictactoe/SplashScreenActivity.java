package com.timashton.tictactoe;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity{
	
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 2500;
    
    //Animation time for title fade out
    private static final int FADE_OFFSET = 2000;
    private static final int FADE_DURATION = 300;
    
    //relative offset amount to make animations center
    private static final float POS_OFFSET = 0.5F;
    
    //ImageViews
    private ImageView splashTitle;
    private ImageView crossOne;
    private ImageView noughtOne;
    private ImageView crossThree;
    
    //Animators
    private Animation titleFadeOut;
    private Animation crossOneIn;
    private Animation noughtOneIn;
    private Animation crossThreeIn;
    
    private AnimationSet crossOneSet;
    private AnimationSet noughtOneSet;
    private AnimationSet crossThreeSet;

       
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	Log.i(this.getClass().getName(), "Enter: OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        
        //set up animators
        titleFadeOut = new AlphaAnimation(1, 0);
        titleFadeOut.setStartOffset(FADE_OFFSET);
        titleFadeOut.setDuration(FADE_DURATION);
        
        crossOneIn = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f,
        		Animation.RELATIVE_TO_SELF, POS_OFFSET, Animation.RELATIVE_TO_SELF, POS_OFFSET);
        crossOneIn.setStartOffset(200);
        crossOneIn.setDuration(300);

        noughtOneIn = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f,
        		Animation.RELATIVE_TO_SELF, POS_OFFSET, Animation.RELATIVE_TO_SELF, POS_OFFSET);
        noughtOneIn.setStartOffset(500);
        noughtOneIn.setDuration(300);
        
        crossThreeIn = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f,
        		Animation.RELATIVE_TO_SELF, POS_OFFSET, Animation.RELATIVE_TO_SELF, POS_OFFSET);
        crossThreeIn.setStartOffset(800);
        crossThreeIn.setDuration(300);

        //animation set for crossOne
        crossOneSet = new AnimationSet(true);
        crossOneSet.addAnimation(crossOneIn);
        crossOneSet.addAnimation(titleFadeOut);
        
        //animation set for noughtOne
        noughtOneSet = new AnimationSet(true);
        noughtOneSet.addAnimation(noughtOneIn);
        noughtOneSet.addAnimation(titleFadeOut);
        
        //animation set for crossThree
        crossThreeSet = new AnimationSet(true);
        crossThreeSet.addAnimation(crossThreeIn);
        crossThreeSet.addAnimation(titleFadeOut);
        
        //the title on the splash screen
        splashTitle = new ImageView(this);
        splashTitle = (ImageView)findViewById(R.id.splash_title);
        splashTitle.setImageResource(R.drawable.title);
        splashTitle.startAnimation(titleFadeOut);
        splashTitle.setVisibility(View.INVISIBLE);  // don't want this to reappear after it is faded out

        //crossOne Image view to be animated
        crossOne = new ImageView(this);
        crossOne = (ImageView)findViewById(R.id.cross_1);
        crossOne.setImageResource(R.drawable.cross_1);
        crossOne.startAnimation(crossOneSet);
        crossOne.setVisibility(View.INVISIBLE); //stay faded out
        
        //noughtOne Image view to be animated
        noughtOne = new ImageView(this);
        noughtOne = (ImageView)findViewById(R.id.nought_1);
        noughtOne.setImageResource(R.drawable.nought_1);
        noughtOne.startAnimation(noughtOneSet);
        noughtOne.setVisibility(View.INVISIBLE);
        
        //crossThree Image view to be animated
        crossThree = new ImageView(this);
        crossThree = (ImageView)findViewById(R.id.cross_3);
        crossThree.setImageResource(R.drawable.cross_3);
        crossThree.startAnimation(crossThreeSet);
        crossThree.setVisibility(View.INVISIBLE);
        
        
        /*
         * Bind a handler to this thread
         * With a new Runnable that will launch the menu intent
         * and destroy this splash screen after the 
         * SPLASH_TIME_OUT time amount.
         */
        new Handler().postDelayed(new Runnable(){
        	
            @Override
            public void run(){
            	
            	Log.i(this.getClass().getName(), "Running animation thread");
                Intent i = new Intent(SplashScreenActivity.this, MenuActivity.class);
                startActivity(i);
 
                // close this activity
            	finish();
        	}
    	}, SPLASH_TIME_OUT);
    }	 


	// Ensure that SplashScreenActivity is 
    // destroyed when the game moves from the splash
    // screen to the Menu Activity.
	@Override
	public void onPause()
	{
		Log.i(this.getClass().getName(), "Enter: onPause(). Calling finish() to terminate splash screen.");
		super.onPause();
		finish();
    }
	
}
