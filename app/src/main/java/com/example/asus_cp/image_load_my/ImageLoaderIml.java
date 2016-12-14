package com.example.asus_cp.image_load_my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by asus-cp on 2016-12-14.
 */

public class ImageLoaderIml implements ImageLoader {

    private ImageView imageView;

    private static final int SET_IMG=0;

    private static final String IMGVIEW_TAG="imgview";

    private static final String BITMAP_KEY="bitmap";

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);



    private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           switch (msg.what){
               case SET_IMG:
                   Bundle bundle = msg.getData();
                   String imgviewTag = bundle.getString(IMGVIEW_TAG);
                   Bitmap bitmap=bundle.getParcelable(BITMAP_KEY);
                   if(imgviewTag.equals(imageView.getTag())){
                       imageView.setImageBitmap(bitmap);
                   }
                   break;
           }
       }
   };

    @Override
    public void load(final String urlString, final ImageView imageView) {
        this.imageView=imageView;
        final String imgTag= (String) imageView.getTag();

        //使用线程池，如果不知道怎么配置线程池的话，直接把AsyncTask的拷贝过来就ok了
        THREAD_POOL_EXECUTOR.execute( new Runnable() {
            @Override
            public void run() {
                getDataFromIntent(urlString, imageView, imgTag);
            }
        });


    }


    /**
     * 从网络获取数据
     * @param urlString
     * @param imageView
     * @param imgTag
     */
    private void getDataFromIntent(String urlString, ImageView imageView, String imgTag) {
        URL url=null;
        HttpURLConnection conn=null;
        try {
            url=new URL(urlString);
            conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10*1000);
            conn.setReadTimeout(10*1000);
            InputStream in=conn.getInputStream();


            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(in,null,options);
            int width=options.outWidth;
            int height=options.outHeight;
            int ratio=getRatio(width,height,imageView.getWidth(),imageView.getHeight());

            options.inJustDecodeBounds=false;
            options.inSampleSize=ratio;
            conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10*1000);
            conn.setReadTimeout(10*1000);
            InputStream in2=conn.getInputStream();
            Bitmap bitmap=BitmapFactory.decodeStream(in2,null,options);
            Message message=handler.obtainMessage(SET_IMG);
            Bundle bundle=new Bundle();
            bundle.putString(IMGVIEW_TAG,imgTag);
            bundle.putParcelable(BITMAP_KEY,bitmap);
            message.setData(bundle);
            handler.sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(conn!=null){
                conn.disconnect();
            }
        }
    }


    /**
     * 求取缩放的比率
     * @param realWidth
     * @param realHeight
     * @param expectWidth
     * @param expectHeight
     * @return
     */
    private int getRatio(int realWidth,int realHeight,int expectWidth,int expectHeight){

        if(realWidth<=0 || realHeight<=0 || expectWidth<=0 || expectHeight<=0){
            throw new IllegalArgumentException("宽高不能小于0");
        }

        float widthRatio=realWidth/expectWidth;
        float heightRatio=realHeight/expectHeight;

        widthRatio = widthRatio>= 1 ? widthRatio : 1;
        heightRatio = heightRatio >= 1 ? heightRatio :1;

        return (int) (widthRatio > heightRatio ? heightRatio : widthRatio);

    }
}
