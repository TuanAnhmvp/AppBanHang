package com.tashop.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tashop.appbanhang.Interface.ItemClickListener;
import com.example.appbanhang.R;
import com.tashop.appbanhang.activity.DetailActivity;
import com.tashop.appbanhang.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public PhoneAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            SanPhamMoi productphone = array.get(position);
            myViewHolder.namephone.setText(productphone.getTensp().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.pricephone.setText("Giá: "+decimalFormat.format(Double.parseDouble(productphone.getGiasp()))+ "Đ" );
            myViewHolder.desciptionphone.setText(productphone.getMota());

            Glide.with(context).load(productphone.getHinhanhnew()).into(myViewHolder.image_phone);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if (!isLongClick){
                        // click
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("chitiet", productphone);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) ==null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA ;
    }



    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namephone, pricephone, desciptionphone, idphone;
        ImageView image_phone;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namephone = itemView.findViewById(R.id.item_name_phone);
            pricephone = itemView.findViewById(R.id.item_price_phone);
            desciptionphone = itemView.findViewById(R.id.item_description_phone);

            image_phone = itemView.findViewById(R.id.item_image_phone);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
