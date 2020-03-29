package com.vrcorp.anekastatus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrcorp.anekastatus.adapter.BaperAdapter;
import com.vrcorp.anekastatus.adapter.FavAdapter;
import com.vrcorp.anekastatus.adapter.IslamiAdapter;
import com.vrcorp.anekastatus.model.BaperModel;
import com.vrcorp.anekastatus.model.FavModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class CariActivity extends AppCompatActivity {
    String urlnya, txtJudul, shareenya="";
    private ActionBar toolbar;
    TextView jdul;
    LinearLayout no_result;
    FavAdapter mDataAdapter;
    SearchView cari;
    private List<FavModel> baperDataList;
    String Nama, gambara, urlPosting, waktu, penerbit, des, NextPage, cariVal;
    private ArrayList<String> islamijudulList= new ArrayList<>();
    private ArrayList<String> islamigambarList= new ArrayList<String>();
    private ArrayList<String> islamipenerbitList = new ArrayList<>();
    private ArrayList<String> islamiwaktuList = new ArrayList<>();
    private ArrayList<String> islamiurlList = new ArrayList<>();
    private ArrayList<String> islamikategoriList = new ArrayList<>();
    private ArrayList<String> islamiDesList = new ArrayList<>();
    private ArrayList<Integer> islamifavList = new ArrayList<Integer>();
    RecyclerView rc_cari;
    int data=0;
    ShimmerLayout sh_cari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        Intent intent = getIntent();
        urlnya = intent.getStringExtra("url");
        txtJudul =  intent.getStringExtra("judul");
        rc_cari = findViewById(R.id.rc_cari);
        sh_cari = findViewById(R.id.shimmer_cari);
        jdul = findViewById(R.id.cari_judul);
        cari = findViewById(R.id.cari_input);
        jdul.setText(txtJudul);
        no_result = findViewById(R.id.no_result);
        cari.setQueryHint("Masukan Kata Kunci");
        cari.onActionViewExpanded();
        cari.setIconified(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cari.clearFocus();
            }
        }, 300);
        cari.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cariVal = cari.getQuery().toString();
                if(cariVal.length()<3){
                    Toast.makeText(CariActivity.this,"Minimal kata minimal 3 huruf",Toast.LENGTH_LONG).show();
                }else if(cariVal.length()>10){
                    Toast.makeText(CariActivity.this,"Maksimal 10 huruf",Toast.LENGTH_LONG).show();
                }else{
                    no_result.setVisibility(View.GONE);
                    islamijudulList.clear();
                    islamigambarList.clear();
                    islamipenerbitList.clear();
                    islamiwaktuList.clear();
                    islamiurlList.clear();
                    islamikategoriList.clear();
                    islamifavList.clear();
                    new CardGet().execute();
                    sh_cari.setVisibility(View.VISIBLE);
                    sh_cari.startShimmerAnimation();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = "https://www.status.co.id/aneka/search/"+cariVal;
            baperDataList= new ArrayList<FavModel>();
            mDataAdapter = new FavAdapter( CariActivity.this, islamijudulList,
                    islamikategoriList,
                    islamigambarList, islamiurlList,islamipenerbitList
                    ,islamiwaktuList,islamiDesList, islamifavList);
            Document mBlogPagination = null;
            System.out.println(url);
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements BaperElementDataSize = mBlogPagination.select("section[class=new-content] article.box-post");
            // Locate the content attribute
            int BaperElementSize = BaperElementDataSize.size();

            System.out.println("jumlah data"+BaperElementSize);
            for (int i = 0; i < BaperElementSize; i++) {
                //Judul old-----
                //Elements ElemenJudul = BaperElementDataSize.select("div[class=entry-body media] h2[class=entry-title ktz-titlemini]").eq(i);
                //Nama= ElemenJudul.text();
                //urlPosting = ElemenJudul.select("a").eq(0).attr("href");
                Elements ElemenJudul = BaperElementDataSize.select("h2[class=entry-title]").eq(i);
                Nama= ElemenJudul.text();
                urlPosting = ElemenJudul.select("a").eq(0).attr("href");
                //gambar old -----
                //Elements elGambar = BaperElementDataSize.select("div[class=entry-body media] a[class=ktz_thumbnail pull-left]").eq(i);
                //gambara = elGambar.select("img").eq(0).attr("src");
                Elements elGambar = BaperElementDataSize.select("div[class=entry-body media] div[class=clearfix] div[class=ktz-featuredimg] a[class=ktz_thumbnail]").eq(i);
                gambara = elGambar.select("img").eq(0).attr("src");

                Elements Edes = BaperElementDataSize.select("div[class=entry-body media] div[class=media-body ktz-post]").eq(i);
                Edes.select("img").remove();
                shareenya=Edes.text().trim();
                des = Edes.html();
                Elements elWaktu = BaperElementDataSize.select("div[class=meta-post]").eq(i);
                waktu = elWaktu.select("div").eq(0).select("span[class=entry-date updated] a").text().trim();
                penerbit = elWaktu.select("div").eq(0).select("span[class=entry-author vcard] a").text().trim();
                islamijudulList.add(Nama);
                islamiurlList.add(urlPosting);
                islamipenerbitList.add(penerbit);
                islamigambarList.add(gambara);
                islamiwaktuList.add(waktu);
                islamikategoriList.add("");
                islamiDesList.add(des);
                islamifavList.add(1);
                mDataAdapter.notifyDataSetChanged();
            }
            //---------------------------
            //--------------------------
            //--------------------------
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
            //attachToRecyclerView
            if(islamijudulList.size()>0){
                //Use this now
                rc_cari.setLayoutManager(mLayoutManager);
                rc_cari.setAdapter(mDataAdapter);
                //rc_art.setAdapter(mDataAdapter);
                no_result.setVisibility(View.GONE);
                sh_cari.stopShimmerAnimation();
                sh_cari.setVisibility(View.GONE);
            }else{
                jdul.setVisibility(View.GONE);
                sh_cari.stopShimmerAnimation();
                sh_cari.setVisibility(View.GONE);
                rc_cari.setVisibility(View.GONE);
                no_result.setVisibility(View.VISIBLE);
            }
            System.out.println("Mentok"+islamijudulList);
            //--------------------------
            //-------------------------
            //dialog.dismiss();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        sh_cari.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        sh_cari.stopShimmerAnimation();
        super.onPause();
    }
}
