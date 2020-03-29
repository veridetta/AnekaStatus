package com.vrcorp.anekastatus.layout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.vrcorp.anekastatus.R;
import com.vrcorp.anekastatus.adapter.IslamiAdapter;
import com.vrcorp.anekastatus.adapter.LucuAdapter;
import com.vrcorp.anekastatus.model.IslamiModel;
import com.vrcorp.anekastatus.model.LucuModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;


public class IslamiFragment extends Fragment {
    ShimmerLayout sh_art;
    View view;
    RecyclerView Baper_art;
    NestedScrollView scrollView;
    LinearLayout kosong;
    Document mBlogDocument  = null, cardDoc = null;
    String Nama, gambara, urlPosting, waktu, penerbit, des, NextPage;
    private List<IslamiModel> baperDataList;
    IslamiAdapter mDataAdapter;
    String url="https://www.status.co.id/aneka/category/islami/";
    private ArrayList<String> islamijudulList= new ArrayList<>();
    private ArrayList<String> islamigambarList= new ArrayList<String>();
    private ArrayList<String> islamipenerbitList = new ArrayList<>();
    private ArrayList<String> islamiwaktuList = new ArrayList<>();
    private ArrayList<String> islamiurlList = new ArrayList<>();
    private ArrayList<String> islamikategoriList = new ArrayList<>();
    private ArrayList<String> islamiDesList = new ArrayList<>();
    private ArrayList<Integer> islamifavList = new ArrayList<Integer>();
    ProgressDialog pDialog;
    int success=0, dialogShow=0, total=0;
    String next;
    private static final boolean GRID_LAYOUT = false;
    Elements eList;
    public IslamiFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static IslamiFragment newInstance(String param1, String param2) {
        IslamiFragment fragment = new IslamiFragment();
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
        view = inflater.inflate(R.layout.fragment_islami, container, false);
        Baper_art = view.findViewById(R.id.rc_art);
        sh_art = view.findViewById(R.id.sh_art);
        kosong = view.findViewById(R.id.kosong);
        BaperCek();

        Baper_art.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItemCount = new LinearLayoutManager(getActivity()).getChildCount();
                int totalItemCount = new LinearLayoutManager(getActivity()).getItemCount();
                int firstVisibleItemPosition = new LinearLayoutManager(getActivity()).findFirstVisibleItemPosition();
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("-----","end");
                    url = NextPage;
                    if(next.equals("Next »")){
                        new Islami().execute();
                        pDialog = new ProgressDialog(getActivity());
                        pDialog.setMessage("Memuat data ...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                        dialogShow=1;
                    }
                }
            }
        });
        return view;
    }
    private void BaperCek(){
        baperDataList= new ArrayList<IslamiModel>();
        if(baperDataList.isEmpty()){
            new Islami().execute();
        }else{
            //baperDataList.clear();
            new Islami().execute();
        }
        sh_art.startShimmerAnimation();
    }
    private class Islami extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            baperDataList= new ArrayList<IslamiModel>();
            mDataAdapter = new IslamiAdapter( getActivity(), islamijudulList,
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
                Edes.select("img").remove();
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
            Elements eNext = mBlogPagination.select("nav[id=nav-index] ul[class=pagination] li");
            //eList = eNext.select("li");
            int pageList = eNext.size();
            if(pageList>0){

            }else{
                next="";
            }
            System.out.println("data next "+ pageList);
            for(int n=0;n<pageList;n++){
                if(pageList-n==1){
                    NextPage=eNext.eq(n).select("a").eq(0).attr("href");
                    next=eNext.eq(n).select("a").eq(0).text().trim();
                }

            }
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
            if(islamijudulList.size()>0){
                Baper_art.setHasFixedSize(false);
                success=1;
                if (GRID_LAYOUT) {
                    Baper_art.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                } else {
                    Baper_art.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                Baper_art.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                Baper_art.setAdapter(mDataAdapter);
                //rc_art.setAdapter(mDataAdapter);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
            }else{
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
