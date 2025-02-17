package com.msc.speaker_cleaner.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.msc.speaker_cleaner.R;

public class UtilRate {
    public static final String TAG = "rate";
    static int star = 0;

    public static boolean showDialogRate(Activity activity){
        Log.i(TAG, "showDialogRate: ");
        if(activity.isDestroyed()){
            return false;
        }

        boolean canShow = SpManager.Companion.getInstance(activity).getBoolean("can_show_rate1", true);
        Log.i(TAG, "_can_show_" + canShow);
        if(!canShow){
            return false;
        }

        showCustomRate(activity);

        return true;
    }

    private static void showCustomRate(Activity activity) {
        Log.i(TAG, "showCustomRate: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialog = LayoutInflater.from(activity).inflate(R.layout.dialog_rate_in_app, null, false);
        builder.setView(dialog);
        Dialog bottomSheetDialog = builder.create();
        bottomSheetDialog.setCancelable(false);

        View star1 = dialog.findViewById(R.id.star1);
        View star2 = dialog.findViewById(R.id.star2);
        View star3 = dialog.findViewById(R.id.star3);
        View star4 = dialog.findViewById(R.id.star4);
        View star5 = dialog.findViewById(R.id.star5);
        View starS1 = dialog.findViewById(R.id.starSelect1);
        View starS2 = dialog.findViewById(R.id.starSelect2);
        View starS3 = dialog.findViewById(R.id.starSelect3);
        View starS4 = dialog.findViewById(R.id.starSelect4);
        View starS5 = dialog.findViewById(R.id.starSelect5);


        star1.setOnClickListener(v -> {
            star = 1;
            bottomSheetDialog.dismiss();
            SpManager.Companion.getInstance(activity).putBoolean("can_show_rate1", false);
        });

        star2.setOnClickListener(v -> {
            star = 2;
            starS1.setVisibility(View.VISIBLE);
            starS2.setVisibility(View.VISIBLE);
            starS3.setVisibility(View.INVISIBLE);
            starS4.setVisibility(View.INVISIBLE);
            starS5.setVisibility(View.INVISIBLE);
            bottomSheetDialog.dismiss();
            SpManager.Companion.getInstance(activity).putBoolean("can_show_rate1", false);
        });

        star3.setOnClickListener(v -> {
            star = 3;
            starS1.setVisibility(View.VISIBLE);
            starS2.setVisibility(View.VISIBLE);
            starS3.setVisibility(View.VISIBLE);
            starS4.setVisibility(View.INVISIBLE);
            starS5.setVisibility(View.INVISIBLE);
            bottomSheetDialog.dismiss();
            SpManager.Companion.getInstance(activity).putBoolean("can_show_rate1", false);
        });

        star4.setOnClickListener(v -> {
            star = 4;
            bottomSheetDialog.dismiss();
            openStore(activity);
            SpManager.Companion.getInstance(activity).putBoolean("can_show_rate1", false);
        });

        star5.setOnClickListener(v -> {
            star = 5;
            bottomSheetDialog.dismiss();
            openStore(activity);
            SpManager.Companion.getInstance(activity).putBoolean("can_show_rate1", false);
        });

        bottomSheetDialog.show();

        starS5.animate().rotation(360).setDuration(2000).start();
        star5.animate().rotation(360).setDuration(2000).start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 5);
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    int index = (int) animation.getAnimatedValue();
                    switch (index){
                        case 1:
                            starS1.setVisibility(View.VISIBLE);
                            starS2.setVisibility(View.INVISIBLE);
                            starS3.setVisibility(View.INVISIBLE);
                            starS4.setVisibility(View.INVISIBLE);
                            starS5.setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            starS1.setVisibility(View.VISIBLE);
                            starS2.setVisibility(View.VISIBLE);
                            starS3.setVisibility(View.INVISIBLE);
                            starS4.setVisibility(View.INVISIBLE);
                            starS5.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            starS1.setVisibility(View.VISIBLE);
                            starS2.setVisibility(View.VISIBLE);
                            starS3.setVisibility(View.VISIBLE);
                            starS4.setVisibility(View.INVISIBLE);
                            starS5.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            starS1.setVisibility(View.VISIBLE);
                            starS2.setVisibility(View.VISIBLE);
                            starS3.setVisibility(View.VISIBLE);
                            starS4.setVisibility(View.VISIBLE);
                            starS5.setVisibility(View.INVISIBLE);
                            break;
                        case 5:
                            starS1.setVisibility(View.VISIBLE);
                            starS2.setVisibility(View.VISIBLE);
                            starS3.setVisibility(View.VISIBLE);
                            starS4.setVisibility(View.VISIBLE);
                            starS5.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        starS1.setVisibility(View.INVISIBLE);
                        starS2.setVisibility(View.INVISIBLE);
                        starS3.setVisibility(View.INVISIBLE);
                        starS4.setVisibility(View.INVISIBLE);
                        starS5.setVisibility(View.INVISIBLE);
                    }, 500);
                }
            });
            valueAnimator.start();
        }, 1000);
    }

    public static void openStore(Activity activity) {
        final String appPackageName = activity.getPackageName();
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        }
    }
}
