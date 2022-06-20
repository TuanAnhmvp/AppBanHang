package com.tashop.appbanhang.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tashop.appbanhang.Interface.IImageClickListenner;
import com.example.appbanhang.R;
import com.tashop.appbanhang.model.Cart;
import com.tashop.appbanhang.model.EventBus.SumEvent;
import com.tashop.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.item_cart_nameproduct.setText(cart.getNameproduct());
        holder.item_cart_soluong.setText(cart.getSoluong()+ " ");
        Glide.with(context).load(cart.getImgeproduct()).into(holder.item_cart_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        holder.item_cart_price.setText(decimalFormat.format((cart.getPriceproduct())));
        long gia = cart.getSoluong() * cart.getPriceproduct();
        holder.item_cart_pricetong.setText(decimalFormat.format(gia));
        holder.checkBox_cart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    Utils.mangmuahang.add(cart);
                    EventBus.getDefault().postSticky(new SumEvent());
                }else {
                    for (int i = 0; i<Utils.mangmuahang.size(); i++){
                        if (Utils.mangmuahang.get(i).getIdproduct() == cart.getIdproduct()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new SumEvent());
                        }

                    }

                }
            }
        });

        holder.setListenner(new IImageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1) {
                    if (cartList.get(pos).getSoluong() >1) {
                        int soluongmoi = cartList.get(pos).getSoluong()-1;
                        cartList.get(pos).setSoluong(soluongmoi);

                        holder.item_cart_soluong.setText(cartList.get(pos).getSoluong()+ " ");
                        long gia = cartList.get(pos).getSoluong() * cartList.get(pos).getPriceproduct();
                        holder.item_cart_pricetong.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new SumEvent());

                    }else if (cartList.get(pos).getSoluong() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new SumEvent());

                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                        builder.show();

                    }

                }else if (giatri == 2) {
                    if (cartList.get(pos).getSoluong() <110) {
                        int soluongmoi = cartList.get(pos).getSoluong() + 1;
                        cartList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.item_cart_soluong.setText(cartList.get(pos).getSoluong()+ " ");
                    long gia = cartList.get(pos).getSoluong() * cartList.get(pos).getPriceproduct();
                    holder.item_cart_pricetong.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new SumEvent());

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_cart_image, imgtru, imgcong;
        TextView item_cart_nameproduct, item_cart_price, item_cart_soluong, item_cart_pricetong;
        IImageClickListenner listenner;
        CheckBox checkBox_cart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_cart_image = itemView.findViewById(R.id.item_cart_image);
            item_cart_nameproduct = itemView.findViewById(R.id.item_cart_nameproduct);
            item_cart_price = itemView.findViewById(R.id.item_cart_price);
            item_cart_soluong = itemView.findViewById(R.id.item_cart_soluong);
            item_cart_pricetong = itemView.findViewById(R.id.item_cart_pricetong);
            imgtru = itemView.findViewById(R.id.item_cart_delete);
            imgcong = itemView.findViewById(R.id.item_cart_add);
            checkBox_cart = itemView.findViewById(R.id.item_cart_check);

            // event click
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setListenner(IImageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View view) {
            if (view == imgtru) {
                listenner.onImageClick(view, getAdapterPosition(), 1);
                //1 tru
            }else if (view == imgcong){
                //2 cong
                listenner.onImageClick(view, getAdapterPosition(), 2);

            }

        }
    }
}
