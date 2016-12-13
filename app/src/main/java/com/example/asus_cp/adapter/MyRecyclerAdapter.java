package com.example.asus_cp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus_cp.listener.OnclickListnerMy;
import com.example.asus_cp.modle.Product;
import com.example.asus_cp.activity.R;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-05.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MyRecyclerAdapter";

    private Context context;
    private List<Product> products;
    private OnclickListnerMy onclickListnerMy;

    public MyRecyclerAdapter(Context context,List<Product> products,OnclickListnerMy onclickListnerMy){
        this.context=context;
        this.products=products;
        this.onclickListnerMy=onclickListnerMy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.item_recyler_view,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(itemView);
        myViewHolder.imageView= (ImageView) itemView.findViewById(R.id.img_item_recyler);
        myViewHolder.textView= (TextView) itemView.findViewById(R.id.text_item_recyler);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder= (MyViewHolder) holder;
        ImageView imageView=myViewHolder.imageView;

        Glide.with(context).load(products.get(position).getImg()).into(imageView);
        //imageView.setImageResource();
        myViewHolder.textView.setText(products.get(position).getName());

        //final int layoutPosition=myViewHolder.getLayoutPosition();//点击的时候再去获取，不能在这里先获取,切记切记
        //Log.d(TAG,"layoutPositi="+layoutPosition+"........"+"adapterPosition="+myViewHolder.getAdapterPosition());
        if(onclickListnerMy!=null){
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListnerMy.onClick(v,myViewHolder.getAdapterPosition());//点击的时候进行获取，而不是在上面就获取，这里时间上的先后顺序
                }
            });

            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onclickListnerMy.onLongClick(v,myViewHolder.getAdapterPosition());
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
        products.addAll(insertP);
        notifyItemRangeInserted(products.size(),insertP.size());
    }

    public void delete(int position){
        products.remove(position);
        notifyItemRemoved(position);
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        View myItemView;
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            myItemView=itemView;
        }
    }
}
