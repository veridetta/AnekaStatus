package com.vrcorp.anekastatus.layout;

import android.os.AsyncTask;
import android.os.Bundle;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class IslamiFragment extends Fragment {
    ShimmerLayout sh_art;
    View view;
    RecyclerView islami_art;
    NestedScrollView scrollView;
    LinearLayout kosong;
    Document mBlogDocument  = null, cardDoc = null;
    String Nama, gambara, urlPosting, waktu, penerbit;
    private ArrayList<String> islamijudulList= new ArrayList<>();
    private ArrayList<String> islamigambarList= new ArrayList<String>();
    private ArrayList<String> islamipenerbitList = new ArrayList<>();
    private ArrayList<String> islamiwaktuList = new ArrayList<>();
    private ArrayList<String> islamiurlList = new ArrayList<>();
    private ArrayList<String> islamikategoriList = new ArrayList<>();
    private ArrayList<Integer> islamifavList = new ArrayList<Integer>();
    int success=0;
    private static final boolean GRID_LAYOUT = false;
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
        view = inflater.inflate(R.layout.fragment_rekomendasi, container, false);
        islami_art = view.findViewById(R.id.rc_art);
        sh_art = view.findViewById(R.id.sh_art);
        kosong = view.findViewById(R.id.kosong);
        IslamiCek();
        return view;
    }
    private void IslamiCek(){
        if(islamijudulList.size()>0){
            islamijudulList.clear();
            islamigambarList.clear();
            islamipenerbitList.clear();
            islamiwaktuList.clear();
            islamiurlList.clear();
            islamikategoriList.clear();
            islamifavList.clear();
            new Islami().execute();
        }else{
            new Islami().execute();
        }
        sh_art.startShimmerAnimation();
    }
    private class Islami extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = "https://mydemoblog19.blogspot.com/search/label/hobi";
            Document islamiBlogPagination = null;

            try {
                islamiBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            //Log.d(TAG, "doInBackground: "+mBlogPagination);
            //System.out.println(mBlogPagination);
            // Using Elements to get the Meta data
            // -------------- RECENTT ---------------
            //----------------
            Elements islamiElementDataSize = islamiBlogPagination.select("div[class=blog-posts hfeed container] article[class=post-outer-container]");
            // Locate the content attribute
            int islamiElementSize = islamiElementDataSize.size();
            int max = 0;
            if(islamiElementSize>10){
                max=10;
            }else{
                max=islamiElementSize;
            }
            //System.out.println("jumlah data"+mElementSize);
            for (int i = 0; i < max; i++) {
                //Judul
                Elements ElemenJudul = islamiElementDataSize.select("h3[class=post-title entry-title]").eq(i);
                Nama= ElemenJudul.text();
                //gambar
                Elements elGambar = islamiElementDataSize.select("div[class=container post-body entry-content] div[class=snippet-thumbnail]").eq(i);
                gambara = elGambar.select("img").eq(0).attr("src");
                urlPosting = ElemenJudul.select("a").eq(0).attr("href");
                Elements elWaktu = islamiElementDataSize.select("div[class=post-header]").eq(i);
                waktu = elWaktu.text().trim();
                penerbit = "MyBlog";
                //STATUS
                islamijudulList.add(Nama);
                islamiurlList.add(urlPosting);
                islamipenerbitList.add(penerbit);
                islamigambarList.add(gambara);
                islamiwaktuList.add(waktu);
                islamikategoriList.add("");
                islamifavList.add(1);
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
            System.out.println(islamigambarList);
            IslamiAdapter mDataAdapter = new IslamiAdapter( getActivity(), islamijudulList,
                    islamikategoriList,
                    islamigambarList, islamiurlList,islamipenerbitList
                    ,islamiwaktuList,islamifavList);
            //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
            //attachToRecyclerView

            if(islamijudulList.size()>0){
                islami_art.setHasFixedSize(true);
                success=1;
                //Use this now
                //rc_art.setLayoutManager(mLayoutManager);
                if (GRID_LAYOUT) {
                    islami_art.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                } else {
                    islami_art.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                islami_art.addItemDecoration(new MaterialViewPagerHeaderDecorator());
                islami_art.setAdapter(mDataAdapter);
                //rc_art.setAdapter(mDataAdapter);
                sh_art.stopShimmerAnimation();
                sh_art.setVisibility(View.GONE);
            }else{
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