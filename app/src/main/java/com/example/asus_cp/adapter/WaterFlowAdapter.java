package com.example.asus_cp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus_cp.listener.OnclickListnerMy;
import com.example.asus_cp.modle.Product;
import com.example.asus_cp.activity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-cp on 2016-12-06.
 */
public class WaterFlowAdapter extends RecyclerView.Adapter {

    private static final String TAG = "WaterFlowAdapter";
    private Context context;
    private List<Product> products;
    private LayoutInflater inflater;
    private OnclickListnerMy onclickListnerMy;

    private List<Integer> heights;

    public WaterFlowAdapter(Context context, List<Product> products,OnclickListnerMy onclickListnerMy) {
        this.context = context;
        this.products = products;
        inflater=LayoutInflater.from(context);
        this.onclickListnerMy=onclickListnerMy;
        initHeights();
    }

    private void initHeights() {
        heights=new ArrayList<>();
        for(int i=0;i<products.size();i++){
            heights.add((int) (180+Math.random()*50));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=inflater.inflate(R.layout.item_recyler_view,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(itemView);
        myViewHolder.imageView= (ImageView) itemView.findViewById(R.id.img_item_recyler);
        myViewHolder.textView= (TextView) itemView.findViewById(R.id.text_item_recyler);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG,"height的大小："+heights.size()+"....."+"position="+position);
        final MyViewHolder myViewHolder= (MyViewHolder) holder;
        ImageView imageView=myViewHolder.imageView;
        ViewGroup.LayoutParams layoutParams=imageView.getLayoutParams();
        layoutParams.height=heights.get(position);
        imageView.setLayoutParams(layoutParams);
        //imageView.setImageResource(R.mipmap.ic_launcher);
        Glide.with(context).load(products.get(position).getImg()).into(imageView);
        myViewHolder.textView.setText(products.get(position).getName());

        //final int layoutPositon=myViewHolder.getAdapterPosition();
        if(onclickListnerMy!=null){
            myViewHolder.itemViewMy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListnerMy.onClick(v, myViewHolder.getAdapterPosition());
                }
            });

            myViewHolder.itemViewMy.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG,"长按的位置是："+myViewHolder.getAdapterPosition());
                    onclickListnerMy.onLongClick(v, myViewHolder.getAdapterPosition());
                    return false;
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void add(List<Product> insertP){
        for(int i=0;i<insertP.size();i++){
            heights.add((int) (180+Math.random()*50));
        }
        products.addAll(insertP);
        notifyItemRangeInserted(products.size(), insertP.size());
    }

    public void delete(int position){
        products.remove(position);
        notifyItemRemoved(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        View itemViewMy;
        ImageView imageView;
        TextView textView;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemViewMy=itemView;
        }
    }
}
