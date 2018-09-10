package com.example.parasrastogi.tordown;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    private WebView webview;
    private EditText srch;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private ProgressBar pb;



    private AlphaAnimation buttonclick=new AlphaAnimation(1F,0.4F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for hiding navigation bar

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //ends
        srch=(EditText)findViewById(R.id.search_bar);



        srch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                     pb=findViewById(R.id.progressBar);

                    int i=0;

                    while(i<=50)
                    {
                        pb.setProgress(i);

                        i++;
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            pb.setProgress(100);
                        }}, 500);

                    pass(srch.getText().toString());
                }

                return false;
            }
        });

    }


    public void bb(final View view) {
        ImageButton menu=findViewById(R.id.downloads);

        if(menu.isPressed())
        {
            Handler h3=new Handler();
            h3.post(new Runnable() {
                @Override
                public void run() {
                    view.startAnimation(buttonclick);
                    Intent u=new Intent(MainActivity.this,DownloadActivity.class);
                    startActivity(u);
                    finish();
                }
            });
        }

    }



    public void mm(final View view) {

    }

    public void gg(View view) {
    }


    public void facebookin(View view) {

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        vibe.vibrate(50);

        view.startAnimation(buttonClick);

        Toast.makeText(this,"Opening Facebook",Toast.LENGTH_SHORT).show();

        pass("https://www.facebook.com");

    }

    public void twitterin(View view) {


        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        vibe.vibrate(100);


        view.startAnimation(buttonClick);

        Toast.makeText(this,"Opening Twitter",Toast.LENGTH_SHORT).show();

        pass("https://www.twitter.com");

    }

    public void instagramin(View view) {


        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        vibe.vibrate(100);


        view.startAnimation(buttonClick);

        Toast.makeText(this,"Opening Instagram",Toast.LENGTH_SHORT).show();

        pass("https://www.instagram.com");

    }

    public void youtubein(View view) {


        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        vibe.vibrate(100);


        view.startAnimation(buttonClick);
        Toast.makeText(this,"Opening Youtube",Toast.LENGTH_SHORT).show();

        pass("https://www.youtube.com");
    }

    public void linkedin(View view) {

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        vibe.vibrate(100);


        view.startAnimation(buttonClick);
        Toast.makeText(this,"Opening LinkedIn",Toast.LENGTH_SHORT).show();

        pass("https://www.linkedin.com");

    }

    public void quorain(View view) {

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        vibe.vibrate(100);


        view.startAnimation(buttonClick);

        Toast.makeText(this,"Opening Quora",Toast.LENGTH_SHORT).show();

        pass("https://www.quora.com");
    }


    //searchbargoogle



    //making a universal function


    public void pass(final String url)
    {

        Handler hn=new Handler();
        hn.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,SearchBarActivity.class);
                i.putExtra("searchval",url);
                startActivity(i);

            }
        },600);

    }



}
