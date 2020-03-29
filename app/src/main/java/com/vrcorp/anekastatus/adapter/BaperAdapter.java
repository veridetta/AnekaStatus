package com.vrcorp.anekastatus.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.anekastatus.DetailActivity;
import com.vrcorp.anekastatus.R;
import com.vrcorp.anekastatus.db.DBHelper;
import com.vrcorp.anekastatus.model.BaperModel;

import java.util.ArrayList;
import java.util.List;

public class BaperAdapter extends RecyclerView.Adapter<BaperAdapter.MyViewHolder> {
    //List<BaperModel> baperModelList;
    private ArrayList<String> islamijudulList = new ArrayList<>();
    private ArrayList<String> islamikategoriList = new ArrayList<>();
    private ArrayList<String> islamiphotoList = new ArrayList<>();
    private ArrayList<String> islamiurlList = new ArrayList<>();
    private ArrayList<String> islamipenerbitList = new ArrayList<>();
    private ArrayList<String> islamiwaktuList = new ArrayList<>();
    private ArrayList<String> islamiDes = new ArrayList<>();
    private ArrayList<Integer> islamifavList = new ArrayList<>();

    private Context context;
    DBHelper helper;
    int success=0, favoritStatus=0, total;
    boolean isLoadingAdded;
    public BaperAdapter(Context context, ArrayList<String> islamijudulList,
                        ArrayList<String> islamikategoriList,
                        ArrayList<String> islamiphotoList,
                        ArrayList<String> islamiurlList,
                        ArrayList<String> islamipenerbitList,
                        ArrayList<String> islamiwaktuList,
                        ArrayList<String> islamiDes,
                        ArrayList<Integer> islamifavList) {
        this.context = context;
        //this.baperModelList = baperModelList;
        this.islamijudulList = islamijudulList;
        this.islamikategoriList = islamikategoriList;
        this.islamiphotoList = islamiphotoList;
        this.islamiurlList = islamiurlList;
        this.islamipenerbitList = islamipenerbitList;
        this.islamiwaktuList = islamiwaktuList;
        this.islamiDes= islamiDes;
        this.islamifavList=islamifavList;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tJudul, tWaktu;
        WebView tDes;
        int tFav;
        ImageView gArtikel, gFav;
        CardView cArtikel;
        LinearLayout btnShare, btnLike;
        public MyViewHolder(View view) {
            super(view);
            tJudul = view.findViewById(R.id.art_judul);
            tDes= view.findViewById(R.id.art_des);
            tWaktu= view.findViewById(R.id.art_tanggal);
            gArtikel= view.findViewById(R.id.art_photo);
            cArtikel = view.findViewById(R.id.card_artikel);
            gFav = view.findViewById(R.id.img_like);
            btnLike = view.findViewById(R.id.btn_like);
            btnShare= view.findViewById(R.id.btn_share);
        }
    }
    public String stripHtml(String html){
        if(Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.N){
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        }else{
            return  Html.fromHtml(html).toString();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //final BaperModel Baper = baperModelList.get(position);
        this.total = total;
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
        holder.cArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("url",islamiurlList.get(position));
                intent.putExtra("gambar",islamiphotoList.get(position));
                context.startActivity(intent);
            }
        });
        helper = new DBHelper(context);
        success = helper.cekFav(islamiurlList.get(position));
        if(success>0){
            Glide.with(holder.gFav)
                    .load(context.getResources()
                            .getIdentifier("fav", "drawable", context.getPackageName()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.gFav.setImageDrawable(resource);
                        }
                    });
            favoritStatus=1;
        }
        holder.gFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritStatus>0){
                    Glide.with(holder.gFav)
                            .load(context.getResources()
                                    .getIdentifier("nofav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.gFav.setImageDrawable(resource);
                                }
                            });
                    helper.deletDB(islamiurlList.get(position));
                    favoritStatus=0;
                }else{
                    Glide.with(holder.gFav)
                            .load(context.getResources()
                                    .getIdentifier("fav", "drawable", context.getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    holder.gFav.setImageDrawable(resource);
                                }
                            });
                    helper.insertIntoDB(1,islamijudulList.get(position),
                            islamiphotoList.get(position),islamiurlList.get(position),
                            islamiDes.get(position),"","1",
                            islamiwaktuList.get(position),islamipenerbitList.get(position));
                    favoritStatus=1;
                }
            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent;
                shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_TEXT,stripHtml(islamiDes.get(position))+" Download aplikasi Aneka Status secara gratis " + "https://play.google.com/store/apps/details?id=" +context.getPackageName());
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent,"Share with"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return islamijudulList.size();
    }
    public void swap(List<BaperModel> datas){
        //baperModelList.clear();
        //baperModelList.addAll(datas);
        notifyDataSetChanged();
    }

}
