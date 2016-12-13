package com.example.asus_cp.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.example.asus_cp.activity.R;

/**
 * Created by asus-cp on 2016-12-09.
 */

public class MyHeadView extends SwipeRefreshHeaderLayout {

    private ImageView imageView;

    private RingProgressDrawable ringProgressDrawable;

    public MyHeadView(Context context) {
        //super(context);
        this(context,null);
    }

    public MyHeadView(Context context, AttributeSet attrs) {
        //super(context, attrs);
        this(context, attrs, 0);
    }

    public MyHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        ringProgressDrawable=new RingProgressDrawable(context);
        ringProgressDrawable.setColors(Color.GREEN,Color.BLUE,Color.YELLOW);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView= (ImageView) findViewById(R.id.img_head);
        imageView.setBackgroundDrawable(ringProgressDrawable);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        super.onMove(y, isComplete, automatic);
        if(!isComplete){
            ringProgressDrawable.setPercent(y/(dp2Px(40)));
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        ringProgressDrawable.start();
    }

    @Override
    public void onComplete() {
        super.onComplete();
        ringProgressDrawable.stop();
    }


    public int dp2Px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,getContext().getResources().getDisplayMetrics());
    }
}
