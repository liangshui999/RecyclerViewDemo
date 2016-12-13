package com.example.asus_cp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asus_cp.adapter.AddressListAdapter;
import com.example.asus_cp.modle.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通讯录的activity
 * Created by asus-cp on 2016-12-12.
 */

public class AddressListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        initView();
        initData();
    }


    private void initView() {
        mRecyclerView= (RecyclerView) findViewById(R.id.recycler_view_address_list);
    }

    private void initData() {

        LinearLayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);

        List<String> index= new ArrayList<>();
        index.add("a");
        index.add("b");
        index.add("c");
        index.add("d");
        index.add("e");
        List<Address> address=new ArrayList<>();

        address.add(new Address(Color.BLUE,"a1111"));
        address.add(new Address(Color.BLUE,"a2222"));
        address.add(new Address(Color.BLUE,"a3333"));
        address.add(new Address(Color.BLUE,"a4444"));
        address.add(new Address(Color.BLUE,"a5555"));
        address.add(new Address(Color.RED,"b11"));
        address.add(new Address(Color.RED,"b22"));
        address.add(new Address(Color.GREEN,"c1"));
        address.add(new Address(Color.GREEN,"c2"));
        address.add(new Address(Color.GREEN,"c3"));
        address.add(new Address(Color.GRAY,"d1111111"));
        address.add(new Address(Color.YELLOW,"e123456"));
        address.add(new Address(Color.YELLOW,"e789654"));



        AddressListAdapter adapter=new AddressListAdapter(this,index,address);
        mRecyclerView.setAdapter(adapter);
    }
}
