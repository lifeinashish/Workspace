package com.aks.matchnmove;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class FirstPage extends Activity {
	
	private Boolean isPlayServicesAvailable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_page);
		isPlayServicesAvailable = new MapActivity().checkPlayServices(this);
		ImageView logo = (ImageView) findViewById(R.id.logo); 
		

		Animation animTranslate  = AnimationUtils.loadAnimation(FirstPage.this, R.anim.translate);
		logo.startAnimation(animTranslate);
		
        
        animTranslate.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) { }

            @Override
            public void onAnimationEnd(Animation arg0) {
            	if (isPlayServicesAvailable) {
            	Intent i = new Intent(FirstPage.this, MainActivity.class);   
                startActivity(i);
            	}
                
            }
        });
        	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	}
}



