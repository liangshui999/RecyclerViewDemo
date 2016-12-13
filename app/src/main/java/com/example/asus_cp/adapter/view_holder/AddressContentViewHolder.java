package com.example.asus_cp.adapter.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus_cp.activity.R;
import com.example.asus_cp.modle.Address;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-13.
 */

public class AddressContentViewHolder extends BaseViewHolder<Address>{

    private ImageView mImageView;

    private TextView mTextView;

    public AddressContentViewHolder(View itemView) {
        super(itemView);
        mImageView= (ImageView) itemView.findViewById(R.id.img_avatar_address_list);
        mTextView= (TextView) itemView.findViewById(R.id.text_name_address_list);
    }

    @Override
    public void bindData(List<Address> datas, int position) {
        super.bindData(datas, position);
        mImageView.setBackgroundColor(datas.get(position).getAvatar());
        mTextView.setText(datas.get(position).getName());
    }
}
