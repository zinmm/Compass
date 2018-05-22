package com.zin.compass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.panel.ADSpreadController;
import com.android.panel.event.AdViewSpreadListener;
import com.android.panel.pojo.ADError;

import java.lang.ref.WeakReference;

public class SplashActivity extends Activity {

    private final static int JUMP = 23478;

    private ADSpreadController adSpreadController;
    private FinishHandler finishHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Button button = findViewById(R.id.skip);
        finishHandler = new FinishHandler(this);
        finishHandler.sendEmptyMessageDelayed(JUMP, 1000 * 7);

        adSpreadController = new ADSpreadController.Builder(this)
                .setKey("szn_an_kp001")
                .setSkipView(button)
                .setSkipClickListener(onClickListener)
                .setContainer((FrameLayout) findViewById(R.id.fl))
                .setSpreadListener(new AdViewSpreadListener() {
                    @Override
                    public void onAdDisplay() {
                        // 广告成功返回并展示在当前页

                        final WeakReference<Button> textViewWeakReference = new WeakReference<>(button);
                        textViewWeakReference.get().setVisibility(View.VISIBLE);
                        textViewWeakReference.get().setClickable(false);

                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1f);
                        alphaAnimation.setFillAfter(true);
                        alphaAnimation.setDuration(3000);
                        textViewWeakReference.get().startAnimation(alphaAnimation);

                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                textViewWeakReference.get().setClickable(true);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                    @Override
                    public void onAdClose() {
                        // 广告被关闭
                    }

                    @Override
                    public void onADTick(long ms) {
                        // 倒计时回调
                    }

                    @Override
                    public void onAdClick() {
                        //广告被点击时监听
                    }

                    @Override
                    public void onAdFailed(ADError error) {
                        // 广告异常、失败，中断时会调用
                    }
                }).build();
        // 请求广告
        adSpreadController.requestSpread();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startMainActivityAndKillOneself();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adSpreadController != null) {
            adSpreadController.destroy();
        }

        if (finishHandler != null) {
            finishHandler.removeMessages(JUMP);
            finishHandler.removeCallbacksAndMessages(null);
            finishHandler = null;
        }
    }

    private void startMainActivityAndKillOneself() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        onBackPressed();
    }

    //防止用户返回键退出APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static class FinishHandler extends Handler {

        private WeakReference<SplashActivity> splashActivityWeakReference;

        public FinishHandler(SplashActivity splashActivity) {
            this.splashActivityWeakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            splashActivityWeakReference.get().startMainActivityAndKillOneself();
        }
    }
}
