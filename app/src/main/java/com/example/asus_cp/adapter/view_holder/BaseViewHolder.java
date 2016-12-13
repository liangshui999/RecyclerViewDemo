package com.example.asus_cp.adapter.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-13.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder{

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 给view绑定数据
     * @param datas
     */
    public void bindData(List<T> datas,int position){

    }
}
