package com.sangharsh.adminpanel_sangharsh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sangharsh.adminpanel_sangharsh.Model.Banner;
import com.sangharsh.adminpanel_sangharsh.Model.Chat;
import com.sangharsh.adminpanel_sangharsh.R;
import com.sangharsh.adminpanel_sangharsh.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder> {

    private ArrayList<Banner> banners;
    private Listener listener;

    public interface Listener {
        void delete(Banner banner);
    }

    public BannerAdapter(ArrayList<Banner> banners, Listener listener) {
        this.banners = banners;
        this.listener = listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_banner_layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Picasso.get()
                .load(banners.get(position).getImageUrl())
                .into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.delete(banners.get(position));
                }
            });
        }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


    }
}