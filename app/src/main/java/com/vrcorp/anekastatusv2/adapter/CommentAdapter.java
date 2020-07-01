package com.vrcorp.anekastatusv2.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.anekastatusv2.R;
import com.vrcorp.anekastatusv2.db.DBHelper;
import com.vrcorp.anekastatusv2.model.FavModel;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private ArrayList<String> islamijudulList = new ArrayList<>();
    private ArrayList<String> islamiphotoList = new ArrayList<>();
    private ArrayList<String> islamiwaktuList = new ArrayList<>();
    private ArrayList<String> islamiDes = new ArrayList<>();

    private Context context;
    DBHelper helper;
    int success=0, favoritStatus=0, total;
    boolean isLoadingAdded;
    public CommentAdapter(Context context, ArrayList<String> islamijudulList,
                          ArrayList<String> islamiphotoList,
                          ArrayList<String> islamiwaktuList,
                          ArrayList<String> islamiDes) {
        this.context = context;
        //this.baperModelList = baperModelList;
        this.islamijudulList = islamijudulList;
        this.islamiphotoList = islamiphotoList;
        this.islamiwaktuList = islamiwaktuList;
        this.islamiDes= islamiDes;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tJudul, tWaktu;
        WebView tDes;
        ImageView gArtikel;
        public MyViewHolder(View view) {
            super(view);
            tJudul = view.findViewById(R.id.nama_comment);
            tDes= view.findViewById(R.id.isi_comment);
            tWaktu= view.findViewById(R.id.waktu_comment);
            gArtikel= view.findViewById(R.id.gambar_comment);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //final BaperModel Baper = baperModelList.get(position);
        holder.tJudul.setText(islamijudulList.get(position));
        holder.tDes.loadDataWithBaseURL(null, islamiDes.get(position), "text/html", "utf-8", null);
        holder.tWaktu.setText(islamiwaktuList.get(position));
        Glide.with(holder.gArtikel.getContext())
                .load(Uri.parse(islamiphotoList.get(position)))
                .apply(RequestOptions.centerCropTransform())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.gArtikel.setImageDrawable(resource);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return islamijudulList.size();
    }
    public void swap(List<FavModel> datas){
        notifyDataSetChanged();
    }

}
