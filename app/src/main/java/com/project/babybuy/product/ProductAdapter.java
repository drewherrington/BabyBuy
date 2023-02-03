package com.project.babybuy.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.babybuy.R;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<ProductData> {


    public ProductAdapter(Context context, ArrayList<ProductData> productData) {
        super(context, 0, productData);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_product, parent, false);
        }

        TextView product_title = (TextView) convertView.findViewById(R.id.product_title);
        ImageView iv_share = (ImageView) convertView.findViewById(R.id.iv_share);
        TextView product_description = (TextView) convertView.findViewById(R.id.product_description);
        TextView product_Price = (TextView) convertView.findViewById(R.id.product_Price);
        ImageView ivProductImage = (ImageView) convertView.findViewById(R.id.ivProductImage);

        ProductData productData = getItem(position);
        product_title.setText("Name - " + productData.getTitle());
        product_description.setText("Description - " + productData.getDescription());
        product_Price.setText("£ " + productData.getPrice());
        Glide.with(getContext())
                .load(productData.getFilePath())
                .placeholder(R.drawable.dummy_product)
                .into(ivProductImage);

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = product_title.getText().toString() +"\n"+product_Price.getText().toString()
                        +"\n"+product_description.getText().toString();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, productData.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                getContext().startActivity(Intent.createChooser(sharingIntent,
                        getContext().getResources().getString(R.string.app_name)));
            }
        });

        Log.e("TAG", "getView: " + productData.getFilePath());
        return convertView;
    }
}