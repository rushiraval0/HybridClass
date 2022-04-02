package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetDetailReport extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView textView1;
    TextView textView5;
    TextInputEditText textInputEditText;
    CardView cardView;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser users;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_detail_report);
        Bundle bundle = getIntent().getExtras();

        System.out.println("tag   "+bundle.getString("USERID"));
        System.out.println("tag   "+bundle.getString("DetailID"));
        System.out.println("tag   "+bundle.getString("TestNAME"));
        System.out.println("tag   "+bundle.getString("Marks"));

        Toolbar toolbar =  findViewById(R.id.toolbartst);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        circleImageView = findViewById(R.id.USERIMAGE);
        textView1 = findViewById(R.id.StudentEmailGen);
        textInputEditText = findViewById(R.id.reviewText);
        cardView = findViewById(R.id.subReview);
        textView5 = findViewById(R.id.this_Section);
        String temp = getIntent().getStringExtra("USERID");

        myRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        users = auth.getCurrentUser();

        textView1.setText(getIntent().getStringExtra("USERID"));
        String Temp = getIntent().getStringExtra("TestNAME") + " results is: " +
                getIntent().getStringExtra("Marks");
        textView5.setText(Temp);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textInputEditText.getText().toString().isEmpty()) {
                    textInputEditText.setError("Error");
                    textInputEditText.requestFocus();
                }

                String review = textInputEditText.getText().toString();
                String test = getIntent().getStringExtra("TestNAME");
                String user = getIntent().getStringExtra("USERID");
                HashMap<String,String> rev = new HashMap<>();
                rev.put("Reviewed By",users.getEmail());
                rev.put("Review",review);

                myRef.child("Review").child(test).child(user).setValue(rev).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Review updated",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"error :"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

