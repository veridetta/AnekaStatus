package com.vrcorp.anekastatus;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.vrcorp.anekastatus.adapter.ArtikelAdapter;
import com.vrcorp.anekastatus.layout.IslamiFragment;
import com.vrcorp.anekastatus.layout.MemeFragment;
import com.vrcorp.anekastatus.layout.RemajaFragment;
import com.vrcorp.anekastatus.layout.BaperFragment;
import com.vrcorp.anekastatus.layout.LucuFragment;
import com.vrcorp.anekastatus.layout.MotivasiFragment;
import com.vrcorp.anekastatus.layout.RomantisFragment;


public class MainActivity extends AppCompatActivity {private ArtikelAdapter adapter;
    //private TabLayout tabLayout;
    private MaterialViewPager viewPager;
    String TAG = "Tag";
    Toolbar toolbar;
    ActionBar actionBar;
    NestedScrollView scroll;
    private AdView adView;
    AdRequest adRequest;
    private static int CODE_WRITE_SETTINGS_PERMISSION;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);

        adView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //scroll = findViewById(R.id.scRoll);
        ViewPager viewpager = viewPager.getViewPager();
        toolbar = viewPager.getToolbar();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        //actionBar.hide();
        //tabLayout = findViewById(R.id.tabLayout);
        isWriteStoragePermissionGranted();
        isReadStoragePermissionGranted();
        if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
            Toast.makeText(MainActivity.this, "Penyimpanan diijinkan", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Silahkan beri izin penyimpanan untuk dapat menikmati aplikasi ini, Restart aplikasi", Toast.LENGTH_LONG).show();
        }
        adapter = new ArtikelAdapter(getSupportFragmentManager());
        adapter.addFragment(new RemajaFragment(), "Remaja");
        adapter.addFragment(new BaperFragment(), "Baper");
        adapter.addFragment(new MemeFragment(), "Meme Comic");
        adapter.addFragment(new LucuFragment(), "Lucu");
        adapter.addFragment(new MotivasiFragment(), "Motivasi");
        adapter.addFragment(new RomantisFragment(), "Romantis");
        adapter.addFragment(new IslamiFragment(), "Islami");
        //MaterialViewPagerHelper.registerScrollView(this, scroll);
        viewpager.setAdapter(adapter);
        viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());
        viewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.orange_400,
                                "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue_400,
                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red_400,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green_400,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    case 4:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.yellow_400,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
        //tabLayout.setupWithViewPager(viewpager);
        if (toolbar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_fav) {
            Intent intent = new Intent(getApplicationContext(), FavActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.nav_cari) {
            Intent intent = new Intent(getApplicationContext(), CariActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    //loadFragment(new HomeFragment());
                    //io++;
                }else{

                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    //loadFragment(new HomeFragment());
                }else{

                }
                break;
        }
    }
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.CODE_WRITE_SETTINGS_PERMISSION && Settings.System.canWrite(this)){
            Log.d("TAG", "MainActivity.CODE_WRITE_SETTINGS_PERMISSION success");
            //do your code
        }
    }
}