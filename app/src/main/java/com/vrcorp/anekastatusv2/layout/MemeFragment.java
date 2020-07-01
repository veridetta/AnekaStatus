package com.vrcorp.anekastatusv2.layout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.vrcorp.anekastatusv2.R;
import com.vrcorp.anekastatusv2.adapter.MemeAdapter;
import com.vrcorp.anekastatusv2.model.MemeModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;


public class MemeFragment extends Fragment {
    private ShimmerLayout sh_art;
    private View view;
    private RecyclerView Baper_art;
    private NestedScrollView scrollView;
    private LinearLayout kosong,ly_utama,ly_button;
    private  Document mBlogDocument  = null, cardDoc = null;
    private  String Nama, gambara, urlPosting, waktu, penerbit, des, NextPage, gambarBesar="";
    private List<MemeModel> baperDataList;
    private MemeAdapter mDataAdapter;
    private CardView cardNext, cardPrev;
    private String url="https://www.status.co.id/aneka/category/meme-comic/";
    private ArrayList<String> islamijudulList;
    private ArrayList<String> islamigambarList;
    private ArrayList<String> islamipenerbitList;
    private ArrayList<String> islamiwaktuList;
    private ArrayList<String> islamiurlList;
    private ArrayList<String> islamikategoriList;
    private ArrayList<String> islamiDesList;
    private ArrayList<Integer> islamifavList;
    private  ProgressDialog pDialog;
    int success=0, dialogShow=0, total=0;
    private String next, nextUrl,  prev, prevUrl;
    private static final boolean GRID_LAYOUT = false;
    Elements eList;
    public MemeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MemeFragment newInstance(String param1, String param2) {
        MemeFragment fragment = new MemeFragment();
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
        view = inflater.inflate(R.layout.fragment_meme, container, false);
        Baper_art = view.findViewById(R.id.rc_art);
        sh_art = view.findViewById(R.id.sh_art);
        kosong = view.findViewById(R.id.kosong);
        ly_utama = view.findViewById(R.id.ly_utama);
        ly_button = view.findViewById(R.id.ly_buton);
        cardNext = view.findViewById(R.id.card_next);
        cardPrev= view.findViewById(R.id.card_prev);
        scrollView = view.findViewById(R.id.mscroll);
        MaterialViewPagerHelper.registerScrollView(getActivity(), scrollView);
        BaperCek();
        cardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = nextUrl;
                if(next.equals("next")){
                    new Remaja().execute();
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Memuat data ...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    dialogShow=1;
                }
            }
        });
        cardPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = prevUrl;
                if(prev.equals("previous")){
                    new Remaja().execute();
                    pDialog = new ProgressDialog(getActivity());
                    pDialog.setMessage("Memuat data ...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    dialogShow=1;
                }
            }
        });
        return view;
    }
    private void BaperCek(){
        baperDataList= new ArrayList<MemeModel>();
        if(baperDataList.isEmpty()){
            new Remaja().execute();
        }else{
            //baperDataList.clear();
            new Remaja().execute();
        }
        sh_art.startShimmerAnimation();
    }
    private class Remaja extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            islamijudulList= new ArrayList<>();
            islamigambarList= new ArrayList<String>();
            islamipenerbitList = new ArrayList<>();
            islamiwaktuList = new ArrayList<>();
            islamiurlList = new ArrayList<>();
            islamikategoriList = new ArrayList<>();
            islamiDesList = new ArrayList<>();
            islamifavList = new ArrayList<Integer>();
            baperDataList= new ArrayList<MemeModel>();
            mDataAdapter = new MemeAdapter( getActivity(), islamijudulList,
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
            Elements BaperElementDataSize = mBlogPagination.select("section[class=new-content] article.box-post");
            // Locate the content attribute
            int BaperElementSize = BaperElementDataSize.size();

            //System.out.println("jumlah data"+mElementSize);
            for (int i = 0; i < BaperElementSize; i++) {
                Elements ElemenJudul = BaperElementDataSize.select("h2[class=entry-title]").eq(i);
                Nama= ElemenJudul.text();
                urlPosting = ElemenJudul.select("a").eq(0).attr("href");
                Elements elGambar = BaperElementDataSize.select("div[class=entry-body media] div[class=clearfix] div[class=ktz-featuredimg] a[class=ktz_thumbnail]").eq(i);
                gambara = elGambar.select("img").eq(0).attr("src");
                Elements Edes = BaperElementDataSize.select("div[class=entry-body media] div[class=media-body ktz-post]").eq(i);
                //des = Edes.html();
                Elements elWaktu = BaperElementDataSize.select("div[class=meta-post]").eq(i);
                waktu = elWaktu.select("div").eq(0).select("span[class=entry-date updated] a").text().trim();
                penerbit = elWaktu.select("div").eq(0).select("span[class=entry-author vcard] a").text().trim();
                String paragraph="";
                gambarBesar=Edes.select("img").eq(1).attr("src");
                Edes.select("img").remove();
                des = Edes.html();
                /*
                for(int is=0;is<listIsi.size();is++){
                    if (is>0){
                        paragraph=paragraph+"<p>"+listIsi.eq(is).text().trim()+"</p>";
                    }else{

                    }
                }
                */
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
            //---------------------------
            //--------------------------
            //--------------------------
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("total data"+islamijudulList.size());
            System.out.println("Meme des "+islamikategoriList);
            if(islamijudulList.size()>0){
                Baper_art.setHasFixedSize(false);
                if(prev.equals("previous")){
                    cardPrev.setVisibility(View.VISIBLE);
                }else{
                    cardPrev.setVisibility(View.GONE);
                }
                if(next.equals("next")){
                    cardNext.setVisibility(View.VISIBLE);
                }else{
                    cardNext.setVisibility(View.GONE);
                }
                ly_button.setVisibility(View.VISIBLE);
                success=1;
                if (GRID_LAYOUT) {
                    Baper_art.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                } else {
                    Baper_art.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                Baper_art.setAdapter(mDataAdapter);
                //rc_art.setAdapter(mDataAdapter);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
            }else{
                ly_button.setVisibility(View.GONE);
                kosong.setVisibility(View.VISIBLE);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
            }
            if(dialogShow>0){
                pDialog.dismiss();
                dialogShow=0;
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
