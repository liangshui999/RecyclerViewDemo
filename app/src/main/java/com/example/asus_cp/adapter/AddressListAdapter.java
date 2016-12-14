package com.example.asus_cp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus_cp.activity.R;
import com.example.asus_cp.adapter.view_holder.AddressContentViewHolder;
import com.example.asus_cp.adapter.view_holder.BaseViewHolder;
import com.example.asus_cp.adapter.view_holder.IndexViewHolder;
import com.example.asus_cp.modle.Address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by asus-cp on 2016-12-13.
 */

public class AddressListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "AddressListAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mIndexs;
    private List<Address> mAddresses;

    private List<Integer> mTypies;

    /**
     * 用2个map来记录真实位置和在对应的集合中的位置之间的关系
     */
    private Map<Integer,Integer> mIndexMap;//前一个是position，后一个是所在集合中的真实位置
    private Map<Integer,Integer> mContentMap;
    private int pos;

    private static final int INDEX=1;
    private static final int CONTENT=2;

    public AddressListAdapter(Context mContext, List<String> mIndexs, List<Address> mAddresses) {
        this.mContext = mContext;
        this.mIndexs = mIndexs;
        this.mAddresses = mAddresses;
        mLayoutInflater=LayoutInflater.from(mContext);

        initTypies();
    }

    private void initTypies() {

        mIndexMap=new HashMap<>();
        mContentMap=new HashMap<>();

        mTypies =new ArrayList<>();
        for(int i = 0; i< mIndexs.size(); i++){
            String currentL= mIndexs.get(i);
            mIndexMap.put(mTypies.size(),i);
            mTypies.add(INDEX);
            for(int j=0;j<mAddresses.size();j++){
                if(mAddresses.get(j).getName().startsWith(currentL)){
                    mContentMap.put(mTypies.size(),pos++);
                    mTypies.add(CONTENT);
                }
            }
        }
        Log.d(TAG,"mIndex="+mIndexs.toString());
        Log.d(TAG,"mAddresses="+mAddresses.toString());
        Log.d(TAG,"mTypies="+mTypies.toString());
        Log.d(TAG,"mIndexMap"+mIndexMap.toString());
        Log.d(TAG,"mContentMap="+mContentMap.toString());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case INDEX:
                View itemView= mLayoutInflater.inflate(R.layout.item_address_list_index,parent,false);
                BaseViewHolder baseViewHolder= new IndexViewHolder(itemView);
                return baseViewHolder;
            case CONTENT:
                View itemView2= mLayoutInflater.inflate(R.layout.item_address_list_content,parent,false);
                BaseViewHolder baseViewHolder2= new AddressContentViewHolder(itemView2);
                return baseViewHolder2;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type=getItemViewType(position);
        switch (type){
            case INDEX:
                IndexViewHolder indexViewHolder= (IndexViewHolder) holder;
                int indexPos=mIndexMap.get(position);
                indexViewHolder.bindData(mIndexs,indexPos);
                break;
            case CONTENT:
                AddressContentViewHolder contentViewHolder= (AddressContentViewHolder) holder;
                int contentPos=mContentMap.get(position);
                contentViewHolder.bindData(mAddresses,contentPos);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTypies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mTypies.get(position);
    }
}
