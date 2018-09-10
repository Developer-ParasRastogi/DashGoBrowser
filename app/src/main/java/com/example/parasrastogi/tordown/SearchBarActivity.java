package com.example.parasrastogi.tordown;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SearchBarActivity extends AppCompatActivity {

    private WebView weber;
    private EditText srch;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        //for hiding navigation bar

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //navigation hidden


        Intent i1=getIntent();
        String url = i1.getStringExtra("searchval");
       // Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
        loadpageonweb(url);

    }

    @Override
    public void onBackPressed() {

        Handler h=new Handler();

        h.post(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(SearchBarActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void backmethod(View view) {

        ImageButton iback=findViewById(R.id.backkey);

        if(iback.isPressed())
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

    }

    public void forwardmethod(View view) {

        ImageButton iforward=findViewById(R.id.forward);

        if(iforward.isPressed())
        {
            if(weber.canGoForward())
            {
                weber.goForward();
            }
            else
            {
                Toast.makeText(this,"End Of Navigation Reached",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void searchcurrent(View view) {

        //searchbar on current page


        srch=(EditText)findViewById(R.id.search_bar);
        if(srch.getText().toString().isEmpty())
        {
            Toast.makeText(this,"No Search Entered.",Toast.LENGTH_SHORT).show();
        }
        else {
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
                    weber = (WebView) findViewById(R.id.webview);


                    WebSettings wbs = weber.getSettings();

                    wbs.setJavaScriptEnabled(true);
                    weber.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);


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
    }

