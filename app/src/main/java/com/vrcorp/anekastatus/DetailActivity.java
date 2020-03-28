package com.vrcorp.anekastatus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vrcorp.anekastatus.adapter.CommentAdapter;
import com.vrcorp.anekastatus.adapter.IslamiAdapter;
import com.vrcorp.anekastatus.app.AppController;
import com.vrcorp.anekastatus.db.DBHelper;
import com.vrcorp.anekastatus.db.DBModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class DetailActivity extends AppCompatActivity {
    LinearLayout desKategori, desKonten, btn_comment;
    CardView cFav, cShare, cBack;
    ImageView bgDes, desFav;
    String paragraph="", comment_id;
    TextView des_judul, des_penerbit, des_waktu;
    EditText input_com_isi,input_com_nama, input_com_email;
    WebView des_isi;
    ShimmerLayout sh_des;
    DBHelper helper;
    RecyclerView rc_comment;
    ProgressDialog pDialog;
    String urlnya, Nama, gambara, urlPosting, waktu, penerbit, isi, comment_name, comment_isi="", comment_waktu, comment_gambar;
    int success=0, favoritStatus=0;
    private ArrayList<String> judulList= new ArrayList<>();
    private ArrayList<String> gambarList= new ArrayList<String>();
    private ArrayList<String> waktuList = new ArrayList<>();
    private ArrayList<String> desList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        desKategori = findViewById(R.id.des_kategori);
        cFav = findViewById(R.id.des_fav);
        cShare = findViewById(R.id.des_share);
        cBack = findViewById(R.id.des_back);
        desKonten = findViewById(R.id.des_content);
        bgDes = findViewById(R.id.des_bg_art);
        des_judul = findViewById(R.id.des_art_judul);
        des_penerbit = findViewById(R.id.des_penerbit);
        des_waktu = findViewById(R.id.des_waktu);
        des_isi = findViewById(R.id.des_isi);
        sh_des = findViewById(R.id.sh_des);
        desFav = findViewById(R.id.img_des_fav);
        rc_comment = findViewById(R.id.rc_comment);
        helper = new DBHelper(this);
        Intent intent = getIntent();
        urlnya = intent.getStringExtra("url");
        gambara = intent.getStringExtra("gambar");
        success = helper.cekFav(urlnya);
        input_com_email =findViewById(R.id.input_comment_email);
        input_com_isi=findViewById(R.id.input_comment_isi);
        input_com_nama =findViewById(R.id.input_comment_name);
        btn_comment = findViewById(R.id.btn_comment);
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_com_email.getText().toString().trim().length() > 0 && input_com_isi.getText().toString().trim().length() > 0 && input_com_nama.getText().toString().trim().length()>0) {
                    checkLogin("", "");
                    new CardGet().execute();
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
        if(success>0){
            Glide.with(desFav)
                    .load(getResources()
                            .getIdentifier("fav", "drawable", this.getPackageName()))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            desFav.setImageDrawable(resource);
                        }
                    });
            favoritStatus=1;
        }
        cFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritStatus>0){
                    Glide.with(desFav)
                            .load(getResources()
                                    .getIdentifier("nofav", "drawable", getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    desFav.setImageDrawable(resource);
                                }
                            });
                    helper.deletDB(urlnya);
                    favoritStatus=0;
                }else{
                    Glide.with(desFav)
                            .load(getResources()
                                    .getIdentifier("fav", "drawable", getPackageName()))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    desFav.setImageDrawable(resource);
                                }
                            });
                    helper.insertIntoDB(1,Nama,gambara,urlnya,paragraph,"","1",waktu,penerbit);
                    favoritStatus=1;
                }
            }
        });
        cBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent;
                shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Baca artikel "+Nama+", Download aplikasi Resep Memasak secara gratis " + "https://play.google.com/store/apps/details?id=" +getPackageName());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent,"Share with"));
            }
        });
        new CardGet().execute();
        //dialog.show();
        sh_des.startShimmerAnimation();
    }
    private class CardGet extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            // NO CHANGES TO UI TO BE DONE HERE
            String url = urlnya;
            Document mBlogPagination = null;
            try {
                mBlogPagination = Jsoup.parse(new URL(url),50000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mBlogPagination.outputSettings().prettyPrint(false);
            //Log.d(TAG, "doInBackground: "+mBlogPagination);
            // Using Elements to get the Meta data
            // -------------- RECENTT ---------------
            //----------------
            Elements mElementDataSize = mBlogPagination.select("article.ktz-single");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();
            int max = 0;
            if(mElementSize>10){
                max=10;
            }else{
                max=mElementSize;
            }
            System.out.println("jumlah data"+mElementDataSize);
            for (int i = 0; i < max; i++) {
                //Judul
                Elements ElemenJudul = mElementDataSize.select("div[class=ktz-single-box] div[class=entry-body]").eq(i);
                Nama= ElemenJudul.select("h1[class=entry-title clearfix]").text();

                Elements eCommentID = mBlogPagination.select("input[id=comment_post_ID]").eq(0);
                comment_id=eCommentID.attr("value");
                System.out.println("id c"+comment_id);
                //gambar
                //Elements elGambar = ElemenJudul.select("div[class=entry-content ktz-wrap-content-single clearfix ktz-post] p img");
               // gambara = elGambar.attr("src");

                urlPosting = ElemenJudul.select("a").eq(0).attr("href");

                Elements elIsi = ElemenJudul.select("div[class=entry-content ktz-wrap-content-single clearfix ktz-post]");
                Elements listIsi = elIsi.select("p");
                for(int is=0;is<listIsi.size();is++){
                    if (is>0){
                        paragraph=paragraph+"<p>"+listIsi.eq(is).text().trim()+"</p>";
                    }else{

                    }
                }
                isi = elIsi.text().trim();

                Elements eMeta = ElemenJudul.select("div[class=metasingle-aftertitle] div[class=ktz-inner-metasingle]");
                penerbit = eMeta.select("span[class=entry-author vcard] a").text().trim();
                waktu = eMeta.select("span[class=entry-date updated] a").text().trim();
                //STATUS
            }
            Elements eComment = mBlogPagination.select("div[id=comtemplate] div[class=wrapcomment] ol[class=commentlist] li");
            if(eComment.size()>0){
                for(int e=0;e<eComment.size();e++){
                    Elements eCPhoto = eComment.select("div[class=author-card pull-left clearfix]").eq(e);
                    comment_gambar = eCPhoto.select("img").eq(0).attr("src");
                    Elements eData = eComment.select("div[class=comment_data]").eq(e);
                    Elements eDataList = eData.select("p");
                    String commente="";
                    for(int dl=0;dl<eDataList.size();dl++){
                        if (dl>0){
                            commente=commente+"<p>"+eDataList.eq(dl).text().trim()+"</p>";
                        }else{
                            comment_name=eDataList.select("span[class=comment_author_link]").text().trim();
                            comment_waktu=eDataList.select("span[class=comment-date]").text().trim();
                        }
                    }
                    judulList.add(comment_name);
                    waktuList.add(comment_waktu);
                    desList.add(commente);
                    gambarList.add(comment_gambar);
                }
            }
            //---------------------------
            //--------------------------

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //This is where we update the UI with the acquired data
            // Set description into TextView
            Log.d("EAEA", "onPostExecute: "+result);
            //--------------RECENT -
            //----------------------
            desKonten.setVisibility(View.VISIBLE);
            des_judul.setText(Nama);
            des_waktu.setText(waktu);
            des_penerbit.setText(penerbit);
            des_isi.loadDataWithBaseURL(null, paragraph, "text/html", "utf-8", null);
            Glide.with(bgDes)
                    .load(Uri.parse(gambara))
                    .apply(RequestOptions.centerCropTransform())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            bgDes.setImageDrawable(resource);
                        }
                    });
            System.out.println("HTML   "+isi);
            cFav.setVisibility(View.VISIBLE);
            cShare.setVisibility(View.VISIBLE);
            sh_des.stopShimmerAnimation();
            sh_des.setVisibility(View.GONE);
            if(judulList.size()>0){
                CommentAdapter mDataAdapter = new CommentAdapter( DetailActivity.this, judulList,
                        gambarList, waktuList,desList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1, LinearLayoutManager.VERTICAL, false);
                rc_comment.setLayoutManager(mLayoutManager);
                rc_comment.setAdapter(mDataAdapter);
                System.out.println("Mentok"+judulList);
            }

            //--------------------------
            //dialog.dismiss();
        }

    }
    //function commment
    private void checkLogin(final String semail, final String spassword) {
        pDialog = new ProgressDialog(DetailActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mengirim Komentar ...");
        pDialog.show();
        String url = "https://www.status.co.id/aneka/wp-comments-post.php";
        Log.e("komentar", "Login Response: " + url);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("komentar", "Login Response string: " + response.toString());
                pDialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("komentar", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("comment", input_com_isi.getText().toString());
                params.put("author", input_com_nama.getText().toString());
                params.put("email", input_com_email.getText().toString());
                params.put("wp-comment-cookies-consent", "no");
                params.put("submit", "Kirim Komentar");
                params.put("comment_post_ID", comment_id);
                params.put("comment_parent", "0");
                return params;
            }

        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");
    }
    public void onResume() {
        sh_des.startShimmerAnimation();
        super.onResume();
    }

    @Override
    public void onPause() {
        sh_des.stopShimmerAnimation();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
