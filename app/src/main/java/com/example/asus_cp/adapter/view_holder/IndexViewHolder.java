package com.example.asus_cp.adapter.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.asus_cp.activity.R;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-13.
 */

public class IndexViewHolder extends BaseViewHolder<String> {

    private TextView mTextView;

    public IndexViewHolder(View itemView) {
        super(itemView);
        mTextView= (TextView) itemView.findViewById(R.id.text_index_address_list);
    }

    @Override
    public void bindData(List<String> datas,int position) {
        super.bindData(datas,position);
        mTextView.setText(datas.get(position));
    }
}
