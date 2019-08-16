package com.example.onlineshop.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineshop.Interface.ItemClickListener;
import com.example.onlineshop.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public  ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {

        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.new_product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.new_product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.new_product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.new_product_price);
    }

    public  void  setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
