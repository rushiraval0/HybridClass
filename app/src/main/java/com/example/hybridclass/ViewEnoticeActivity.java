package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewEnoticeActivity extends AppCompatActivity {

    WebView webView;
    private Spinner spinner;
    DatabaseReference databaseReference;
    Activity activity ;
    private ProgressDialog pDialog;
    CustomTabsIntent.Builder builder;
    CustomTabsIntent customTabsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_enotice);

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("E-Notice");
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });

//
//        activity = this;
//
//        progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);
//        builder = new CustomTabsIntent.Builder();
//        customTabsIntent = builder.build();
//        customTabsIntent.launchUrl(this, Uri.parse(""));

        spinner = findViewById(R.id.spinnerNoticeStudent);
        databaseReference = FirebaseDatabase.getInstance().getReference("notice");
        populateSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            ENotice eNotice = dataSnapshot.getValue(ENotice.class);
                            System.out.println("RUSYR");
                            System.out.println(eNotice.getNoticeUrl());
                            if(eNotice.getClassCode().equalsIgnoreCase(spinner.getSelectedItem().toString()))
                            {
//                                System.out.println(eNotice.getNoticeUrl());
//                                webView.setWebViewClient(new WebViewClient(){
//                                    @Override
//                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                        progDailog.show();
//                                        view.loadUrl(url);
//                                        return true;
//                                    }
//                                    @Override
//                                    public void onPageFinished(WebView view, final String url) {
//                                        progDailog.dismiss();
//                                    }
//                                });
//                                webView.loadUrl(eNotice.getNoticeUrl());
                                webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+eNotice.getNoticeUrl());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }




    private void populateSpinner() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final List<String> myClassList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, myClassList);
        adapter.setDropDownViewResource(R.layout.custom_spinner);
        spinner.setAdapter(adapter);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int c = 0;
                for (DataSnapshot childSnapshot : snapshot.child("classList").getChildren()) {
                    myClassList.add(childSnapshot.getValue(String.class));
                    System.out.println("RUSY " + c + " " + myClassList);
                    c++;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}