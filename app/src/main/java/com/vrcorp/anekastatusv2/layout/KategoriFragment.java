package com.vrcorp.anekastatusv2.layout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrcorp.anekastatusv2.R;
import com.vrcorp.anekastatusv2.adapter.BaperAdapter;
import com.vrcorp.anekastatusv2.adapter.MemeAdapter;
import com.vrcorp.anekastatusv2.model.BaperModel;
import com.vrcorp.anekastatusv2.model.MemeModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

import static com.android.volley.VolleyLog.TAG;

public class KategoriFragment extends Fragment {
    private ShimmerLayout sh_art;
    private View view;
    private RecyclerView Baper_art;
    private NestedScrollView scrollView;
    private LinearLayout kosong,ly_utama,ly_button;
    private Document mBlogDocument  = null, cardDoc = null;
    private String Nama, gambara, urlPosting, waktu, penerbit, des, NextPage, gambarBesar="";
    private List<BaperModel> baperDataList;
    private List<MemeModel> memeDateList;
    private BaperAdapter mDataAdapter;
    private MemeAdapter memeAdapter;
    private CardView cardNext, cardPrev;
    private String url="https://www.status.co.id/aneka/category/baper/";
    LinearLayout baper, islami, lucu, meme, motivasi, remaja,romantis, life, love;
    private ArrayList<String> islamijudulList;
    private ArrayList<String> islamigambarList;
    private ArrayList<String> islamipenerbitList;
    private ArrayList<String> islamiwaktuList;
    private ArrayList<String> islamiurlList;
    private ArrayList<String> islamikategoriList;
    private ArrayList<String> islamiDesList;
    private ArrayList<Integer> islamifavList;
    private ProgressDialog pDialog;
    int success=0, dialogShow=0, total=0;
    private String next, nextUrl,  prev, prevUrl;
    private static final boolean GRID_LAYOUT = false;
    TextView menampilkan;
    Elements eList;
    public KategoriFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static KategoriFragment newInstance(String param1, String param2) {
        KategoriFragment fragment = new KategoriFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getActivity(),"Pertamaa",Toast.LENGTH_LONG).show();
        //dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kategori, container, false);
        Baper_art = view.findViewById(R.id.rc_art);

        islamijudulList= new ArrayList<>();
        islamigambarList= new ArrayList<String>();
        islamipenerbitList = new ArrayList<>();
        islamiwaktuList = new ArrayList<>();
        islamiurlList = new ArrayList<>();
        islamikategoriList = new ArrayList<>();
        islamiDesList = new ArrayList<>();
        islamifavList = new ArrayList<Integer>();
        baperDataList= new ArrayList<BaperModel>();
        sh_art = view.findViewById(R.id.sh_art);
        baper = view.findViewById(R.id.baper);
        islami= view.findViewById(R.id.islami);
        lucu= view.findViewById(R.id.lucu);
        meme= view.findViewById(R.id.meme);
        motivasi= view.findViewById(R.id.motivasi);
        remaja= view.findViewById(R.id.remaja);
        romantis= view.findViewById(R.id.romantis);
        life= view.findViewById(R.id.life);
        love= view.findViewById(R.id.love);
        kosong = view.findViewById(R.id.kosong);
        ly_utama = view.findViewById(R.id.ly_utama);
        ly_button = view.findViewById(R.id.ly_buton);
        cardNext = view.findViewById(R.id.card_next);
        cardPrev= view.findViewById(R.id.card_prev);
        scrollView = view.findViewById(R.id.mscroll);
        menampilkan = view.findViewById(R.id.menampilkan);
        url = "https://jagokata.com/kata-bijak/kata-ilmuwan.html";
        baper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(baper, "Menampilkan Kategori Baper",
                        "https://www.status.co.id/aneka/category/baper/" );
            }
        });
        islami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(islami, "Menampilkan Kategori Islami",
                        "https://www.status.co.id/aneka/category/islami/");
            }
        });
        lucu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(lucu, "Menampilkan Kategori Lucu",
                        "https://www.status.co.id/aneka/category/lucu/");
            }
        });
        meme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(meme,"Menampilkan Kategori Meme",
                        "https://www.status.co.id/aneka/category/meme-comic/");
            }
        });
        motivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(motivasi, "Menampilkan Kategori Motivasi",
                        "https://www.status.co.id/aneka/category/motivasi/");
            }
        });
        remaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(remaja,"Menampilkan Kategori Remaja",
                        "https://www.status.co.id/aneka/category/remaja/");
            }
        });
        romantis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(romantis,"Menampilkan Kategori Romantis",
                        "https://www.status.co.id/aneka/category/romantis/");
            }
        });
        life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(life,"Menampilkan Kategori Kehidupan",
                        "https://www.status.co.id/aneka/category/kehidupan/");
            }
        });
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoriKlik(love,"Menampilkan Kategori Love",
                        "https://www.status.co.id/aneka/category/love/");
            }
        });
        BaperCek();
        cardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = nextUrl;
                if(next.equals("next")){
                    new Baper().execute();

                }
            }
        });
        cardPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = prevUrl;
                if(prev.equals("previous")){
                    new Baper().execute();

                }
            }
        });

        return view;
    }
    private void BaperCek(){
        baperDataList= new ArrayList<BaperModel>();
        if(baperDataList.isEmpty()){
            kategoriKlik(baper, "Menampilkan Kategori Baper",
                    "https://www.status.co.id/aneka/category/baper/" );
        }else{
            //baperDataList.clear();
            kategoriKlik(baper, "Menampilkan Kategori Baper",
                    "https://www.status.co.id/aneka/category/baper/" );
        }
        sh_art.startShimmerAnimation();
    }
    public void kategoriKlik(final LinearLayout cardView, final String Kategori,
                             final String urlKategori){
        url=urlKategori;
        float alpha = (float) 0.5;
        baper.setAlpha(alpha);
        islami.setAlpha(alpha);
        lucu.setAlpha(alpha);
        meme.setAlpha(alpha);
        motivasi.setAlpha(alpha);
        remaja.setAlpha(alpha);
        romantis.setAlpha(alpha);
        cardView.setAlpha(1);
        menampilkan.setText(Kategori);
        sh_art.startShimmerAnimation();
        islamijudulList= new ArrayList<>();
        islamigambarList= new ArrayList<String>();
        islamipenerbitList = new ArrayList<>();
        islamiwaktuList = new ArrayList<>();
        islamiurlList = new ArrayList<>();
        islamikategoriList = new ArrayList<>();
        islamiDesList = new ArrayList<>();
        islamifavList = new ArrayList<Integer>();
        baperDataList= new ArrayList<BaperModel>();
        if (Kategori.equals("Menampilkan Kategori Meme")){
            new Baper().execute();
            Log.d(TAG, "kategoriKlik: Meme");
        }else{
            new Baper().execute();
        }
    }
    private class Baper extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            mDataAdapter = new BaperAdapter( getActivity(), islamijudulList,
                    islamikategoriList,
                    islamigambarList, islamiurlList,islamipenerbitList
                    ,islamiwaktuList,islamiDesList, islamifavList);
            Document mBlogPagination = null;
            System.out.println("Next List"+url);
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            //Log.d(TAG, "doInBackground: "+mBlogPagination);
            //System.out.println(mBlogPagination);
            // Using Elements to get the Meta data
            // -------------- RECENTT ---------------
            //----------------
            Elements BaperElementDataSize = mBlogPagination.select("section[class=new-content] article.box-post");
            // Locate the content attribute
            int BaperElementSize = BaperElementDataSize.size();

            //System.out.println("jumlah data"+mElementSize);
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
                gambarBesar=Edes.select("img").eq(1).attr("src");
                Edes.select("img").remove();
                des = Edes.html();
                Elements elWaktu = BaperElementDataSize.select("div[class=meta-post]").eq(i);
                waktu = elWaktu.select("div").eq(0).select("span[class=entry-date updated] a").text().trim();
                penerbit = elWaktu.select("div").eq(0).select("span[class=entry-author vcard] a").text().trim();
                Elements eNext = mBlogPagination.select("nav[id=nav-index] ul[class=pager]");
                //eList = eNext.select("li");
                int pageList = eNext.size();
                prev="";
                next="";
                prev = eNext.select("li[class=previous]").attr("class");
                prevUrl = eNext.select("li[class=previous] a").attr("href");
                next = eNext.select("li[class=next]").attr("class");
                nextUrl = eNext.select("li[class=next] a").attr("href");
                System.out.println("data next "+ prev + next);
                System.out.println("Next List"+next);
                System.out.println("next Page "+NextPage);
                islamijudulList.add(Nama);
                islamiurlList.add(urlPosting);
                islamipenerbitList.add(penerbit);
                islamigambarList.add(gambara);
                islamiwaktuList.add(waktu);
                islamikategoriList.add(gambarBesar);
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
            //This is where we update the UI with the acquired data
            // Set description into TextView
            //Log.d(TAG, "onPostExecute: "+result);
            //--------------RECENT -
            //--------------------
            System.out.println("total data"+islamijudulList.size());
            //mDataAdapter = new BaperAdapter( getActivity(), baperDataList);
            //mDataAdapter.getItemCount();
            //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
            //attachToRecyclerView

            if(islamijudulList.size()>0){
                Baper_art.setHasFixedSize(false);
                if(prev.equals("previous")){
                    cardPrev.setVisibility(View.VISIBLE);
                }else{
                    cardPrev.setVisibility(View.GONE);
                }
                if(next.equals("next")){
                    //cardNext.setVisibility(View.VISIBLE);
                }else{
                    cardNext.setVisibility(View.GONE);
                }
                ly_button.setVisibility(View.VISIBLE);
                success=1;
                //Use this now
                //rc_art.setLayoutManager(mLayoutManager);
                if (GRID_LAYOUT) {
                    Baper_art.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                } else {
                    Baper_art.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                Baper_art.setAdapter(mDataAdapter);
                //rc_art.setAdapter(mDataAdapter);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
                kosong.setVisibility(View.GONE);
                Baper_art.setVisibility(View.VISIBLE);
            }else{
                Baper_art.setVisibility(View.GONE);
                ly_button.setVisibility(View.GONE);
                kosong.setVisibility(View.VISIBLE);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
            }

            //--------------------------
            //-------------------------
            //dialog.dismiss();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        sh_art.startShimmerAnimation();

    }

    @Override
    public void onPause() {
        sh_art.stopShimmerAnimation();
        super.onPause();
    }

}