package com.vrcorp.anekastatusv2.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vrcorp.anekastatusv2.R;
import com.vrcorp.anekastatusv2.adapter.IslamiAdapter;
import com.vrcorp.anekastatusv2.db.DBHelper;
import com.vrcorp.anekastatusv2.db.DBModel;

import java.util.ArrayList;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class FavFragment extends Fragment {
    private View view;
    String Nama, gambara, urlPosting, waktu, penerbit, cariVal;
    private ArrayList<String> judulList= new ArrayList<>();
    private ArrayList<String> gambarList= new ArrayList<String>();
    private ArrayList<String> penerbitList = new ArrayList<>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<String> urlList = new ArrayList<>();
    private ArrayList<String> kategoriList = new ArrayList<>();
    private ArrayList<Integer> favList = new ArrayList<Integer>();
    private ArrayList<String> desList = new ArrayList<String>();
    RecyclerView rc_fav;
    int totalData=0, mentok=0;
    DBHelper helper;
    LinearLayout no_result;
    ShimmerLayout sh_fav;
    TextView favJudul;
    List<DBModel> dbList;
    public FavFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FavFragment newInstance(String param1, String param2) {
        FavFragment fragment = new FavFragment();
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
        view = inflater.inflate(R.layout.activity_fav, container, false);
        rc_fav = view.findViewById(R.id.rc_fav);
        no_result = view.findViewById(R.id.no_result);
        favJudul = view.findViewById(R.id.fav_judul);
        sh_fav = view.findViewById(R.id.shimmer_fav);
        sh_fav.startShimmerAnimation();
        //Cursor cursor = db.fetchMahasiswa(tnim);
        helper = new DBHelper(getContext());
        totalData = helper.getTotalFav();
        dbList= new ArrayList<DBModel>();
        dbList = helper.getFromDB();
        System.out.println("List"+totalData);
        if(totalData<1){
            favJudul.setVisibility(View.GONE);
            sh_fav.stopShimmerAnimation();
            sh_fav.setVisibility(View.GONE);
            rc_fav.setVisibility(View.GONE);
            no_result.setVisibility(View.VISIBLE);
        }else{
            for (int i = 0; i < totalData; i++) {
                judulList.add(dbList.get(i).getJudul());
                gambarList.add(dbList.get(i).getGambar());
                penerbitList.add(dbList.get(i).getPenerbit());
                waktuList.add(dbList.get(i).getWaktu());;
                urlList.add(dbList.get(i).getUrl());
                kategoriList.add(dbList.get(i).getKategori());
                favList.add(Integer.parseInt(dbList.get(i).getFavorit()));
                desList.add(dbList.get(i).getDes());
                System.out.println("Masuk while "+judulList);
                if(totalData - i == 1){
                    mentok=1;
                }
            }
            if(mentok>0){
                IslamiAdapter mDataAdapter = new IslamiAdapter( getActivity(), judulList, kategoriList,
                        gambarList, urlList,penerbitList,waktuList,desList,favList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),1, LinearLayoutManager.VERTICAL, false);
                rc_fav.setLayoutManager(mLayoutManager);
                rc_fav.setAdapter(mDataAdapter);
                sh_fav.stopShimmerAnimation();
                sh_fav.setVisibility(View.GONE);
                System.out.println("Mentok"+judulList);

            }

        }

        return view;
    }



}