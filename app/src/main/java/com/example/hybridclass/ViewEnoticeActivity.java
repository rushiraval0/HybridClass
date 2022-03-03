package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_enotice);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        activity = this;

        progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);

        spinner = findViewById(R.id.spinnerNoticeStudent);
        databaseReference = FirebaseDatabase.getInstance().getReference("notice");
        populateSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        webView.clearView();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            ENotice eNotice = dataSnapshot.getValue(ENotice.class);
                            System.out.println("RUSYR");
                            System.out.println(eNotice);
                            if(eNotice.getClassCode().equalsIgnoreCase(spinner.getSelectedItem().toString()))
                            {
                                System.out.println(eNotice.getNoticeUrl());
                                webView.setWebViewClient(new WebViewClient(){
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        progDailog.show();
                                        view.loadUrl(url);
                                        return true;
                                    }
                                    @Override
                                    public void onPageFinished(WebView view, final String url) {
                                        progDailog.dismiss();
                                    }
                                });
                                webView.loadUrl(eNotice.getNoticeUrl());
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