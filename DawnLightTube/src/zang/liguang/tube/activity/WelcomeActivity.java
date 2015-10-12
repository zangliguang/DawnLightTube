package zang.liguang.tube.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import zang.liguang.tube.R;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

@Fullscreen
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends Activity {
	@ViewById(R.id.welcome_image)
    protected RelativeLayout welcomeView;
    @AfterViews
    public void initView() {
    	AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
  		aa.setDuration(2000);
  		welcomeView.startAnimation(aa);
  		aa.setAnimationListener(new AnimationListener()
  		{
  			@Override
  			public void onAnimationEnd(Animation arg0) {
  				redirectTo();
  			}
  			@Override
  			public void onAnimationRepeat(Animation animation) {}
  			@Override
  			public void onAnimationStart(Animation animation) {}
  			
  		});
    }

    private void redirectTo(){        
//        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//        startActivity(intent);
    	 MainActivity_.intent(this).start();
    	finish();
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
