package com.xiachen.fatter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xiachen on 15/12/9.
 */
public class SplashActivity extends AppCompatActivity {

    ImageView ivLogo;
    TextView tvLeft;
    TextView tvRight;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(i);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            mHandler.removeMessages(-1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.sendEmptyMessageDelayed(-1, 3000);

        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);

        animateSplash();

    }

    private void animateSplash() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1, 1.5f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 1.5f);

        PropertyValuesHolder alpha2 = PropertyValuesHolder.ofFloat("alpha", 0, 0.6f, 1f, 0);
        PropertyValuesHolder translationY1 = PropertyValuesHolder.ofFloat("translationY", -50, 0, 30);
        PropertyValuesHolder translationY2 = PropertyValuesHolder.ofFloat("translationY", 50, 0, -30);
        ObjectAnimator ivLogoAnimator = ObjectAnimator.ofPropertyValuesHolder(ivLogo, alpha, scaleX, scaleY).setDuration(3500);
        ObjectAnimator tvLeftAnimator = ObjectAnimator.ofPropertyValuesHolder(tvLeft, alpha2, translationY1).setDuration(3500);
        ObjectAnimator tvRightAnimator = ObjectAnimator.ofPropertyValuesHolder(tvRight, alpha2, translationY2).setDuration(3500);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ivLogoAnimator).with(tvLeftAnimator).with(tvRightAnimator);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            mHandler.sendEmptyMessage(-1);
            finish();
        }
        return true;
    }


}
