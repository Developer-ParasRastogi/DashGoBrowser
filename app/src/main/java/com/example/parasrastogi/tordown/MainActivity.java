package com.example.parasrastogi.tordown;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    private WebView webview;
    private EditText srch;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private ProgressBar pb;
    private GestureDetector gestureScanner;
    DatabaseRecent mdatabaserecent;
    private ListView listview;



    private AlphaAnimation buttonclick=new AlphaAnimation(1F,0.4F);


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting color of action bar
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));


        //fetching listview
        listview=(ListView)findViewById(R.id.recentlistview);
        mdatabaserecent=new DatabaseRecent(this);
        //call to retrieve lists from database

        populatelistview();

        //setting listener on what to do when item is clicked on the list that has been retreieved.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String name = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"Opening"+name,Toast.LENGTH_SHORT).show();

                Handler h=new Handler();
                h.post(new Runnable() {
                    //sending the clicked bookmark to webview on another activity to load it !
                    @Override
                    public void run() {
                        Intent i=new Intent(MainActivity.this,SearchBarActivity.class);
                        i.putExtra("searchval",name);
                        startActivity(i);

                    }
                });



            }
        });




        //for hiding navigation bar

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        //checking internet connectivity first and then calling methods

        if (hasConnection(MainActivity.this))
        {
            srch=(EditText)findViewById(R.id.search_bar);
            srch.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if(!srch.getText().toString().isEmpty()) {
                            pb = findViewById(R.id.progressBar);
                            int i = 0;
                            while (i <= 50) {
                                //setting initial progress bar status
                                pb.setProgress(i);
                                i++;
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    pb.setProgress(100);
                                }
                            }, 500);

                            pass(srch.getText().toString());

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No Search Entered",Toast.LENGTH_SHORT).show();
                        }


                    }
                    return false;
                    }
                });

        }
        else
            {

            //no internet connectivity
            showNetDisabledAlertToUser(MainActivity.this);
            }


    }



    //to display lists of recent history
    private void populatelistview()
    {
        Cursor data=mdatabaserecent.getdata();
        ArrayList<String> Listdata=new ArrayList<>();

        while(data.moveToNext())
        {
            Listdata.add(data.getString(1));
        }
        ListAdapter adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Listdata);
        listview.setAdapter(adapter);
    }




    //configuring three dot menu for search bar page

    //calling the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashgomenu,menu);

        return true;
    }

    //choosing what to display and what to leave in menu
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        super.onPrepareOptionsMenu(menu);


            MenuItem home = menu.findItem(R.id.home);
            home.setEnabled(false);
            MenuItem bookmarkthispage=menu.findItem(R.id.bookmark);
            bookmarkthispage.setEnabled(false);

        return true;
    }

    //coding for reach individual item in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.bookmarkedpages:
            {
                Intent i=new Intent(MainActivity.this,ListBookmarks.class);
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


        //configuring the speeddials
    public void facebookin(View view) {
        //giving a small vibrating feedback
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        vibe.vibrate(50);

        view.startAnimation(buttonClick);
        //checking for internet connectivity first and then passing string to next activity intent to load
        if (hasConnection(MainActivity.this))
        {
            Toast.makeText(this,"Opening Facebook",Toast.LENGTH_SHORT).show();
            pass("https://www.facebook.com");

        }
        else
            {
            showNetDisabledAlertToUser(MainActivity.this);
            }
    }

    //twitter speeddial
    public void twitterin(View view) {

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        view.startAnimation(buttonClick);

        if (hasConnection(MainActivity.this))
        {
            Toast.makeText(this,"Opening Twitter",Toast.LENGTH_SHORT).show();

            pass("https://www.twitter.com");

        }
        else{
            showNetDisabledAlertToUser(MainActivity.this);
        }
    }

    //instagram speeddial
    public void instagramin(View view) {

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        view.startAnimation(buttonClick);

        if (hasConnection(MainActivity.this))
        {
            Toast.makeText(this,"Opening Instagram",Toast.LENGTH_SHORT).show();
            pass("https://www.instagram.com");

        }
        else
            {
            showNetDisabledAlertToUser(MainActivity.this);
            }

    }

    //youtube speeddial
    public void youtubein(View view)
    {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        view.startAnimation(buttonClick);

        if (hasConnection(MainActivity.this))
        {
            Toast.makeText(this,"Opening Youtube",Toast.LENGTH_SHORT).show();

            pass("https://www.youtube.com");

        }
        else
            {
            showNetDisabledAlertToUser(MainActivity.this);
            }
    }


    //linkedin speeddial
    public void linkedin(View view) {

        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        view.startAnimation(buttonClick);

        if (hasConnection(MainActivity.this))
        {
            Toast.makeText(this,"Opening LinkedIn",Toast.LENGTH_SHORT).show();
            pass("https://www.linkedin.com");
        }
        else
            {
            showNetDisabledAlertToUser(MainActivity.this);
            }

    }

    //quora speeddial
    public void quorain(View view) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        view.startAnimation(buttonClick);

        if (hasConnection(MainActivity.this))
        {
            Toast.makeText(this,"Opening Quora",Toast.LENGTH_SHORT).show();
            pass("https://www.quora.com");
        }
        else
            {
            showNetDisabledAlertToUser(MainActivity.this);
            }
    }


    //searchbargoogle code

    public boolean hasConnection(Context context){
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()){
            return true;
        }
        NetworkInfo mobileNetwork=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()){
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()){
            return true;
        }
        return false;
    }

    //functions to detect that internet is disabled and show dialog to open it
    public static void showNetDisabledAlertToUser(final Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setMessage("Would you like to enable it?")
                .setTitle("No Internet Connection")
                .setPositiveButton(" Turn on Internet ", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(dialogIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //making a universal function to pass intent from all speeddials and searchbars

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
