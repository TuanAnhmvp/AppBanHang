package com.tashop.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.tashop.appbanhang.model.Item;

import java.util.List;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.MyViewHolder> {
    Context context;
    List<Item> itemList;

    public DetailOrderAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_view_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txten_detail_view_order.setText(item.getTensp() + "");
        holder.txsoluong_view_order.setText("Số lượng: " + item.getSoluong() + "");
        Glide.with(context).load(item.getHinhanhnew()).into(holder.image_detail_view_order);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image_detail_view_order;
        TextView txten_detail_view_order, txsoluong_view_order;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_detail_view_order = itemView.findViewById(R.id.img_detail_view_order);
            txten_detail_view_order = itemView.findViewById(R.id.item_tenspchitiet);
            txsoluong_view_order = itemView.findViewById(R.id.item_soluongchitiet);
        }
    }
}
