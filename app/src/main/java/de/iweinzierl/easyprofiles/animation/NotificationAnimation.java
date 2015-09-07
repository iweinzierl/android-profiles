package de.iweinzierl.easyprofiles.animation;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationAnimation {

    private final int HIDE_NOTIFICATION = 100;

    private final Handler handler = new NotificationMessageHandler(Looper.getMainLooper());

    private final View notificationContainer;
    private final RelativeLayout.LayoutParams layoutParams;

    private final int initBottomMargin;

    private boolean animationRunning = false;

    public NotificationAnimation(View notificationContainer, int bottomMargin) {
        this.notificationContainer = notificationContainer;
        this.layoutParams = (RelativeLayout.LayoutParams) notificationContainer.getLayoutParams();
        this.initBottomMargin = bottomMargin;
    }

    public void start() {
        if (!animationRunning) {
            animationRunning = true;

            show();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(HIDE_NOTIFICATION);
                }
            }, 3000);
        }
    }

    private void show() {
        ValueAnimator animator = ValueAnimator.ofInt(initBottomMargin, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
                notificationContainer.requestLayout();
            }
        });
        animator.start();
    }

    private void hide() {
        animationRunning = false;

        ValueAnimator animator = ValueAnimator.ofInt(0, initBottomMargin);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
                notificationContainer.requestLayout();
            }
        });
        animator.start();
    }

    private class NotificationMessageHandler extends Handler {

        public NotificationMessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HIDE_NOTIFICATION) {
                hide();
            }
        }
    }
}
