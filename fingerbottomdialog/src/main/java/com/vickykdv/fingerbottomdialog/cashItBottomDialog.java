package com.vickykdv.fingerbottomdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;


/**
 * Created by Omar on 07/12/2017.
 */

@SuppressWarnings("unchecked")
public class cashItBottomDialog<T extends cashItBottomDialog> {



    protected Context context;
    protected String title;
    protected String message;

    protected boolean cancelOnTouchOutside;
    protected boolean cancelOnPressBack;
    protected boolean dimBackground;


    LayoutInflater layoutInflater;
    BottomSheetDialog dialog;
    View dialogView;

    public cashItBottomDialog(Context context){
        this.context = context;
        this.title = "";
        this.message = "";
        this.cancelOnTouchOutside = false;
        this.cancelOnPressBack = false;
        this.dimBackground = true;
        this.layoutInflater = LayoutInflater.from(context);

        this.dialog = new BottomSheetDialog(context,R.style.BottomSheetDialog);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    public T title(String title){
        this.title = title;
        return (T) this;
    }

    public T message(String message){
        this.message = message;
        return (T) this;
    }

    public T title(int resTitle){
        this.title = context.getResources().getString(resTitle);
        return (T) this;
    }

    public T message(int resMessage){
        this.message = context.getResources().getString(resMessage);
        return (T) this;
    }

    public T cancelOnTouchOutside(boolean cancelOnTouchOutside) {
        this.cancelOnTouchOutside = cancelOnTouchOutside;
        return (T) this;
    }

    public T cancelOnPressBack(boolean cancelOnPressBack){
        this.cancelOnPressBack = cancelOnPressBack;
        return (T) this;
    }

    public T dimBackground(boolean dimBackground){
        this.dimBackground = dimBackground;
        return (T) this;
    }
}
