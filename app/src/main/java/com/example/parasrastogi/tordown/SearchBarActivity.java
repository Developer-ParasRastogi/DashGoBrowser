package com.example.parasrastogi.tordown;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SearchBarActivity extends AppCompatActivity{

    private WebView weber;
    private EditText srch;
    private ProgressDialog progressDialog;
    private Databasehelper mdatabasehelper;
    private DatabaseRecent mdatabaserecent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);


        //setting color of action bar
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));



        //for hiding navigation bar
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //navigation hidden

        //getting intenet sent by previous activities
        Intent i1=getIntent();
        String url = i1.getStringExtra("searchval");
       // Toast.makeText(this,url,Toast.LENGTH_SHORT).show();

        int REQUEST_CODE=1;
        //download permission during search
        ActivityCompat.requestPermissions(this, new String[]
                {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE
        );
        //download permission ends
        //calling webview to load page
        loadpageonweb(url);

    }


    //configuring three dot menu for search bar page

    //preparing to show what items are to be shown
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        super.onPrepareOptionsMenu(menu);
        //all items are valid
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashgomenu,menu);
        return true;
    }


    //coding each item individually of the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch(id)
        {
            case R.id.home:
            {
                Handler h1=new Handler();
                h1.post(new Runnable() {
                    @Override
                    public void run() {
                   Intent i=new Intent(SearchBarActivity.this,MainActivity.class);
                   startActivity(i);
                   finish();
                    }
                });
            }
            break;
            case R.id.bookmark:
            {

                String url=weber.getUrl().toString();

                if(!url.isEmpty())
                {
                  //  Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
                    //logic for bookmark
                    //insert into database !

                    mdatabasehelper=new Databasehelper(this);
                    adata(url);
                    url="";
                }
                else
                {
                    Toast.makeText(this,"Can't Bookmark, SearchBar is Empty !",Toast.LENGTH_SHORT).show();
                }

            }
            break;
            case R.id.bookmarkedpages:
            {
                Intent i=new Intent(SearchBarActivity.this,ListBookmarks.class);
                startActivity(i);
            }
            break;
            case R.id.downloads:
            {

                //thinking what to do
            }
            break;
            case R.id.exit:
            {
                //exiting the app (or just giving an impression of it )
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
            break;
        }
        return true;
    }

    //three dot menu ends



    //function to add data into databse

    public void adata(String url)
    {
        boolean insertdata=mdatabasehelper.addData(url);

        if(insertdata)
        {
            Toast.makeText(this,"Bookmark Created ! ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Sorry Couldn't Create Bookmark ! ",Toast.LENGTH_SHORT).show();
        }
    }


    public void adatarecent(String url)
    {
        boolean insertdata=mdatabaserecent.addData(url);
    }



    @Override
    public void onBackPressed()
    {

            if(weber.canGoBack())
            {
                weber.goBack();
            }
            else
            {
                Toast.makeText(this,"End Of Navigation Reached",Toast.LENGTH_SHORT).show();
            }
    }



    public void searchcurrent(View view) {
        //searchbar on current page
        srch=(EditText)findViewById(R.id.search_bar);
        if(srch.getText().toString().isEmpty())
        {
            Toast.makeText(this,"No Search Entered.",Toast.LENGTH_SHORT).show();
        }
        else
            {
            loadpageonweb(srch.getText().toString());
            }
        }



        public void loadpageonweb(final String url) {
            if (url.isEmpty()) {
                Toast.makeText(this,"No Search Entered",Toast.LENGTH_SHORT).show();
            }
            else
                {
                //loading message
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                //logic to pass
                if (!url.isEmpty()) {


                    //insert entry into recent_base database and then load url in webview
                    mdatabaserecent=new DatabaseRecent(this);
                    adatarecent(url);

                    //now loading webview
                    weber = (WebView) findViewById(R.id.webview);
                    WebSettings wbs = weber.getSettings();
                    wbs.setJavaScriptEnabled(true);
                    weber.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                    wbs.setBuiltInZoomControls(true);
                    wbs.setSupportZoom(true);
                    wbs.setUseWideViewPort(true);
                    weber.getSettings().setUseWideViewPort(true);
                    weber.getSettings().setLoadWithOverviewMode(true);
                    weber.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            if (url.matches("^(https?|ftp)://.*$")) {
                                view.loadUrl(url);
                            } else {
                                view.loadUrl("https://www.google.com/search?q=" + url);
                            }
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    });


                    if (url.matches("^(https?|ftp)://.*$")) {
                        weber.loadUrl(url);
                    } else {
                        weber.loadUrl("https://www.google.com/search?q=" + url);
                    }




                } else {
                    Toast.makeText(this, "No Search Entered,Try Again.", Toast.LENGTH_SHORT).show();
                }

            }





        }


    public void goforward(View view) {

        ImageButton forward=(ImageButton)findViewById(R.id.forwardbutton);

        if(forward.isPressed()) {
            if (weber.canGoForward()) {
                weber.goForward();
            } else {
                Toast.makeText(this, "End Of Navigation Reached", Toast.LENGTH_SHORT).show();
            }
        }
    }


}




