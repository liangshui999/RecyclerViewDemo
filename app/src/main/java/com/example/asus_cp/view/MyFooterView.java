package com.example.asus_cp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.example.asus_cp.activity.R;

/**
 * Created by asus-cp on 2016-12-09.
 */

public class MyFooterView extends SwipeLoadMoreFooterLayout{

    private static final String TAG = "MyFooterView";

    private ProgressBar progressBar;
    private TextView textView;

    public MyFooterView(Context context) {
        super(context);
    }

    public MyFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressBar= (ProgressBar) findViewById(R.id.progress_footer);
        textView= (TextView) findViewById(R.id.text_footer);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        textView.setText("加载中...");
    }


    @Override
    public void onComplete() {
        super.onComplete();
        textView.setText("加载完成...");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        super.onMove(y, isComplete, automatic);
        Log.d(TAG,"y="+y+"........"+"isComplete="+isComplete);
        if(!isComplete){
            y=Math.abs(y);
            if(y>getHeight()){
                textView.setText("释放加载更多...");
            }else{
                textView.setText("上拉加载更多...");
            }
        }

    }
}
