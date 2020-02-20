package ysn.com.demo.welcomeactivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.ImageView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @Author yangsanning
 * @ClassName WelcomeActivity
 * @Description 一句话概括作用
 * @Date 2020/2/20
 * @History 2020/2/20 author: description:
 */
public class WelcomeActivity extends Activity {


    private static final int ANIM_TIME_MILLISECONDS = 2000;
    private static final float SCALE_END = 1.15F;
    private static final int[] WELCOMES = {R.mipmap.welcome1, R.mipmap.welcome2, R.mipmap.welcome3, R.mipmap.welcome4};

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_welcome);

        imageView = findViewById(R.id.welcome_activity_bg);

        startMainActivity();
    }

    private void startMainActivity() {
        // SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
        Random random = new Random(SystemClock.elapsedRealtime());

        imageView.setImageResource(WELCOMES[random.nextInt(WELCOMES.length)]);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Long>() {

                @Override
                public void call(Long aLong) {
                    startAnim();
                }
            });
    }

    private void startAnim() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME_MILLISECONDS).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                WelcomeActivity.this.finish();
            }
        });
    }

    /**
     * 屏蔽物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
