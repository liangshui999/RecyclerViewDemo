package com.example.asus_cp.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 加载json格式的txt文件，并将其转换成string
 * Created by asus-cp on 2016-05-20.
 */
public class JsonHelper {
    private Context context=MyApplication.getContext();
    private String tag="JsonHelper";
    private static final int MESSAGE_KEY=1;
    private static final String BUNDLE_KEY="bundle_key";
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_KEY:
                    ConvertTxtCallBack callBack= (ConvertTxtCallBack) msg.obj;
                    Bundle bundle=msg.getData();
                    String str=bundle.getString(BUNDLE_KEY);
                    callBack.handleString(str);
                    break;
            }
        }
    };
    /**
     * 将assets文件夹中的文件加载出来，转换成string
     * @param fileName 文件名称
     */
    public void convertTxtToString(final String fileName, final ConvertTxtCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str=null;
                AssetManager assetManager=context.getAssets();
                InputStream in=null;
                ByteArrayOutputStream out=null;
                try {
                    out=new ByteArrayOutputStream();
                    in=new BufferedInputStream(assetManager.open(fileName));
                    int len=0;
                    byte[] buf=new byte[1024];
                    while((len=in.read(buf))!=-1){
                        out.write(buf,0,len);
                    }
                    str=out.toString();
                    Message message=handler.obtainMessage();
                    message.what=MESSAGE_KEY;
                    message.obj=callBack;
                    Bundle bundle=new Bundle();
                    bundle.putString(BUNDLE_KEY,str);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(out!=null){
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(in!=null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }



    /**
     * 将Unicode转换成中文
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * 将txt数据转换成string的回调接口
     */
    public interface ConvertTxtCallBack{
        public void handleString(String s);//将返回的string交给需要处理string的方法
    }
}
