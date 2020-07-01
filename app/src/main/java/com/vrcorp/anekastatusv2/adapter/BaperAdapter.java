package com.vrcorp.anekastatusv2.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.anekastatusv2.DetailActivity;
import com.vrcorp.anekastatusv2.R;
import com.vrcorp.anekastatusv2.db.DBHelper;
import com.vrcorp.anekastatusv2.model.BaperModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        TextView tDes;
        int tFav;
        ImageView gArtikel, gFav, gIsi;
        CardView cArtikel;
        LinearLayout btnShare, btnLike,btnSave;
        public MyViewHolder(View view) {
            super(view);
            tJudul = view.findViewById(R.id.art_judul);
            tDes= view.findViewById(R.id.art_des);
            tWaktu= view.findViewById(R.id.art_tanggal);
            gArtikel= view.findViewById(R.id.art_photo);
            cArtikel = view.findViewById(R.id.card_artikel);
            gIsi = view.findViewById(R.id.img_status);
            btnSave = view.findViewById(R.id.btn_save);
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
    private void saveImage(Bitmap bitmap, @NonNull String name){
        try{
            boolean saved;
            OutputStream fos;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "Aneka_status");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
            }else{
                String imagesDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ).toString() + File.separator + "Aneka_status";
                File file = new File(imagesDir);
                if(!file.exists()){
                    file.mkdirs();
                }
                File image = new File(imagesDir, name+".png");
                fos = new FileOutputStream(image);
            }
            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if(saved){
                Toast.makeText(context,"Gambar berhasil tersimpan di galeri",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"Menyimpan Gagal",Toast.LENGTH_LONG).show();
            }
        }catch (IOException e){
            e.printStackTrace();
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
        holder.tDes.setText(stripHtml(islamiDes.get(position)));
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
        holder.tDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("url",islamiurlList.get(position));
                intent.putExtra("gambar",islamiphotoList.get(position));
                context.startActivity(intent);
            }
        });
        if(islamikategoriList.get(position).equals("")){
            holder.btnSave.setVisibility(View.GONE);
        }else{
            holder.btnSave.setVisibility(View.VISIBLE);
            Glide.with(holder.gIsi.getContext())
                    .load(Uri.parse(islamikategoriList.get(position)))
                    .apply(RequestOptions.centerCropTransform())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.gIsi.setImageDrawable(resource);
                        }
                    });


        }
        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
                final String currentTimeStamp = dateFormat.format(new Date());
                Glide.with(context)
                        .asBitmap()
                        .load(Uri.parse(islamikategoriList.get(position)))
                        .into(new SimpleTarget<Bitmap>(100,100) {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                saveImage(resource,"aneka-status"+currentTimeStamp);
                            }
                        });
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
                            islamiDes.get(position),islamikategoriList.get(position),"1",
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
