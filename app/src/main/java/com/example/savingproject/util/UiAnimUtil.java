package com.example.savingproject.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.RecyclerView;

import com.example.savingproject.R;

public final class UiAnimUtil {

    private UiAnimUtil() {
    }

    public static void fadeInUp(View view) {
        if (view == null) return;
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        if (view.getTag(R.id.tag_entrance_animated) != null) return;
        view.setTag(R.id.tag_entrance_animated, true);
        view.setAlpha(0f);
        view.setTranslationY(24f);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(view.getResources().getInteger(R.integer.anim_duration_medium))
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();
    }

    public static void fadeIn(View view, long startDelayMs) {
        if (view == null) return;
        view.setAlpha(0f);
        view.animate()
                .alpha(1f)
                .setStartDelay(startDelayMs)
                .setDuration(view.getResources().getInteger(R.integer.anim_duration_medium))
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();
    }

    public static void playRecyclerLayoutAnimation(RecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getAdapter() == null) return;
        if (recyclerView.getAdapter().getItemCount() == 0) return;
        Animation animation = AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.item_fall_down);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setDelay(0.12f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    public static void pulseOnce(View view) {
        if (view == null) return;
        view.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .setDuration(80)
                .withEndAction(() -> view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start())
                .start();
    }
}
