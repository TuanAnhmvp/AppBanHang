package com.tashop.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.tashop.appbanhang.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHoldel> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHoldel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_order, parent, false);
        return new MyViewHoldel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoldel holder, int position) {
        Order order = orderList.get(position);
        holder.txtdonhang_view_order.setText("Đơn hàng: " + order.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rcvdetailview_order.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(order.getItem().size());
        // adapter chitiet
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(context, order.getItem());
        holder.rcvdetailview_order.setLayoutManager(layoutManager);
        holder.rcvdetailview_order.setAdapter(detailOrderAdapter);
        holder.rcvdetailview_order.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHoldel extends RecyclerView.ViewHolder{
        TextView txtdonhang_view_order;
        RecyclerView rcvdetailview_order;
        public MyViewHoldel(@NonNull View itemView) {
            super(itemView);
            txtdonhang_view_order = itemView.findViewById(R.id.iddonhang_vieworder);
            rcvdetailview_order = itemView.findViewById(R.id.recycleview_detail_view_order);
        }
    }
}
