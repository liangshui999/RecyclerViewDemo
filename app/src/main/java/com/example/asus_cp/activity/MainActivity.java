package com.example.asus_cp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.asus_cp.adapter.MyRecyclerAdapter;
import com.example.asus_cp.adapter.WaterFlowAdapter;
import com.example.asus_cp.listener.OnclickListnerMy;
import com.example.asus_cp.modle.Product;
import com.example.asus_cp.util.JsonHelper;
import com.example.asus_cp.view.GoogleLoadMoreFooterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        OnRefreshListener,OnLoadMoreListener{

    private static final String TAG = "MainActivity";

    private SwipeToLoadLayout swipeToLoadLayout;
    private GoogleLoadMoreFooterView googleLoadMoreFooterView;
    private ListView slidMemuListView;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private List<Product> products;
    private MyRecyclerAdapter adapter;
    private WaterFlowAdapter waterFlowAdapter;

    public static final int LIST_FLAG=0;
    public static final int GRID_FLAG=1;
    public static final int STAGGED_FLAG=2;
    public static final int ADDRESS_LIST_FLAG=3;


    public static final int FETCH_DATA=1;
    public static final int LOAD_MORE=2;
    public static final int REFRESH=3;


    private String searchUrl="http://www.zmobuy.com/PHP/?url=/search";
    private int page=1;//加载的页数

    private MyHandler handler=new MyHandler(this);

    static class MyHandler extends Handler{

        SoftReference softReference;

        public MyHandler(MainActivity mainActivity) {
            softReference=new SoftReference(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity= (MainActivity) softReference.get();
            switch (msg.what){
                case FETCH_DATA:
                    String s= (String) msg.obj;
                    mainActivity.adapter.add(mainActivity.parse(s));
                    break;
                case LOAD_MORE:
                    String sMore= (String) msg.obj;
                    if(mainActivity.recyclerView.getAdapter() instanceof  MyRecyclerAdapter){
                        mainActivity.adapter.add(mainActivity.parse(sMore));
                    }else{
                        mainActivity.waterFlowAdapter.add(mainActivity.parse(sMore));
                    }
                    mainActivity.swipeToLoadLayout.setLoadingMore(false);
                    break;
                case REFRESH:
                    mainActivity.swipeToLoadLayout.setRefreshing(false);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout= (DrawerLayout) findViewById(R.id.draw_layout);
        toolbar.setTitle("RecyclerView测试");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//让toolbar显示返回键
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open_desc,R.string.close_desc);
        actionBarDrawerToggle.syncState();//让toolbar上的返回键随着侧滑菜单的出入而改变
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
        initData();
    }



    /**
     * 初始化视图
     */
    private void initView() {
        slidMemuListView= (ListView) findViewById(R.id.list_menu_sliding);
        recyclerView= (RecyclerView) findViewById(R.id.swipe_target);
        swipeToLoadLayout= (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        //googleLoadMoreFooterView= (GoogleLoadMoreFooterView) findViewById(R.id.foot_load_more);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initSlideMenu();
        initRecylerView();

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(RecyclerView.SCROLL_STATE_IDLE==newState){
                    if(!ViewCompat.canScrollVertically(recyclerView,1)){
                        swipeToLoadLayout.setLoadingMore(true);
                        page++;
                        loadMoreData(page);
                        //handler.sendEmptyMessageDelayed(LOAD_MORE,3000);
                    }
                }
            }
        });


    }



    /**
     * 初始化侧滑菜单
     */
    private void initSlideMenu() {
        //初始化侧滑菜单
        final List<String> menus=new ArrayList<>();
        menus.add("listview");
        menus.add("gridview");
        menus.add("waterfullview");
        menus.add("addressList");
//        menus.add("add");
//        menus.add("delete");
        ArrayAdapter menuAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,
                menus);
        slidMemuListView.setAdapter(menuAdapter);
        slidMemuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(MainActivity.this, menus.get(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case LIST_FLAG:
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,
                                false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        toggleDrawLayout();
                        break;
                    case GRID_FLAG:
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,
                                2, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        toggleDrawLayout();
                        break;
                    case STAGGED_FLAG:
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                                2, StaggeredGridLayoutManager.VERTICAL);
                        waterFlowAdapter = new WaterFlowAdapter(MainActivity.this, products, new OnclickListnerMy() {
                            @Override
                            public void onClick(View v, int position) {
                                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLongClick(View v, int position) {
                                //WaterFlowAdapter waterFlowAdapter1 = (WaterFlowAdapter) recyclerView.getAdapter();
                                Log.d(TAG, "删除的位置：" + position);
                                waterFlowAdapter.delete(position);

                            }
                        });
                        recyclerView.setAdapter(waterFlowAdapter);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        toggleDrawLayout();
                        break;

                    case ADDRESS_LIST_FLAG:
                        Intent intent=new Intent(MainActivity.this,AddressListActivity.class);
                        startActivity(intent);
                        break;



//                    case ADD_FLAG:
//                        MyRecyclerAdapter myRecyclerAdapter = (MyRecyclerAdapter) recyclerView.getAdapter();
//                        List<Product> tempProducts = new ArrayList<Product>();
//                        for (int i = 0; i < 10; i++) {
//                            Product product = new Product();
//                            product.setName("新增：" + i);
//                            tempProducts.add(product);
//                        }
//                        myRecyclerAdapter.add(tempProducts);
//                        toggleDrawLayout();
//                        break;
//                    case DELETE_FLAG:
//                        myRecyclerAdapter = (MyRecyclerAdapter) recyclerView.getAdapter();
//                        myRecyclerAdapter.delete(1);
//                        toggleDrawLayout();
//                        break;
                }
            }
        });
    }


    /**
     * 初始化recylerview
     */
    private void initRecylerView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map=new HashMap<>();
                map.put("json","{\"filter\":{\"keywords\":\"\",\"category_id\":\"\",\"price_range\":\"\",\"brand_id\":\"\",\"intro\":\"\",\"sort_by\":\"id_desc\"},\"pagination\":{\"page\":\"1\",\"count\":\"20\"}}");
                String s=fetchData(searchUrl,map);
                Message msg=handler.obtainMessage();
                msg.what=FETCH_DATA;
                msg.obj=s;
                handler.sendMessage(msg);
            }
        }).start();

        products=new ArrayList<>();
//        for(int i=0;i<20;i++){
//            Product product=new Product();
//            product.setImg("");
//            product.setName("位置："+i);
//            products.add(product);
//        }
        adapter=new MyRecyclerAdapter(this, products, new OnclickListnerMy() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View v, int positon) {
                adapter.delete(positon);
            }
        });
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,
                false );
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLoadMore() {
        //Toast.makeText(this,"加载更多",Toast.LENGTH_SHORT).show();
        page++;
        loadMoreData(page);
        //handler.sendEmptyMessageDelayed(LOAD_MORE,3000);
    }

    @Override
    public void onRefresh() {
        //Toast.makeText(this,"刷新",Toast.LENGTH_SHORT).show();
        handler.sendEmptyMessageDelayed(REFRESH,3000);
    }


    public void toggleDrawLayout(){
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    /**
     * 从网络上获取数据
     * @param urlString
     * @param map 参数列表
     */
    public String fetchData(String urlString,Map<String,String> map){
        URL url=null;
        HttpURLConnection conn=null;
        OutputStream out=null;
        InputStream in=null;
        String result=null;
        try {
            url=new URL(urlString);
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setReadTimeout(10*1000);

            StringBuilder sb=new StringBuilder();
            for(Map.Entry<String,String> entry:map.entrySet()){
                sb.append(entry.getKey());
                sb.append("=");//这个地方不需要进行urlencoder
                sb.append(URLEncoder.encode(entry.getValue(),"utf-8"));//只有这儿需要进行urlencode
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);

            Log.d(TAG,"请求参数："+sb.toString());

            out=conn.getOutputStream();
            out.write(sb.toString().getBytes("utf-8"));
            out.flush();//这儿记得要刷新，不刷新的话，根本就没有发出去

            StringBuilder sbr=new StringBuilder();
            int responseCode=conn.getResponseCode();
            Log.d(TAG,"responseCode="+responseCode);
            if(responseCode==200){
                in=conn.getInputStream();
                int len=0;
                byte[] buf=new byte[1024];
                while((len=in.read(buf))!=-1){
                    sbr.append(new String(buf,0,len));
                    Log.d(TAG,"内容："+new String(buf,0,len));
                }
                Log.d(TAG,"len="+len);
               result=sbr.toString().trim();
                Log.d(TAG,"result="+result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }
        return result;
    }


    /**
     * 从json数据中解析product
     * @param s
     * @return
     */
    public List<Product>  parse(String s){

        Log.d(TAG,"需要解析的数据："+s);

        List<Product> products=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsObj=jsonArray.getJSONObject(i);
                Product product=new Product();
                product.setName(JsonHelper.decodeUnicode(jsObj.getString("name")));

                JSONObject imgObj=jsObj.getJSONObject("img");
                product.setImg(imgObj.getString("url"));
                products.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"异常信息："+e.toString());
        }

        Log.d(TAG,"数组的长度："+products.size());
        return products;
    }


    public void loadMoreData(final int page){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map=new HashMap<>();
                map.put("json","{\"filter\":{\"keywords\":\"\",\"category_id\":\"\",\"price_range\":\"\",\"brand_id\":\"\",\"intro\":\"\",\"sort_by\":\"id_desc\"},\"pagination\":{\"page\":\""+page+"\",\"count\":\"20\"}}");
                String s=fetchData(searchUrl,map);
                Message msg=handler.obtainMessage();
                msg.what=LOAD_MORE;
                msg.obj=s;
                handler.sendMessage(msg);
            }
        }).start();
    }

}
