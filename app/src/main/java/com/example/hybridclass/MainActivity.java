package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ImageView ivTop,ivBottom;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay=200;
    Handler handler = new Handler();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String role;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        ivTop = findViewById(R.id.iv_top);
        ivBottom = findViewById(R.id.iv_bottom);

        textView = findViewById(R.id.appName);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.top_wave);
        ivTop.setAnimation(animation1);
        if(firebaseUser != null)
        {
            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());

            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    role = snapshot.child("role").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            startHeavyProcessing();
        }
        else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }


        animateText("HybridClass");
        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.bottom_wave);

        ivBottom.setAnimation(animation2);
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(charSequence.subSequence(0, index++));
            if (index <= charSequence.length()) {
                handler.postDelayed(runnable, delay);
            }
        }
    };

    public void animateText(CharSequence cs)
    {
        charSequence= cs;

        index = 0;

        textView.setText("");

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,delay);

    }

    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }



    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //some heavy processing resulting in a Data String
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "whatever result you have";
        }

        @Override
        protected void onPostExecute(String result) {

            if(role.equalsIgnoreCase("teacher"))
            {
                Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                i.putExtra("role",role);
                startActivity(i);
            }
            else
            {
                Intent i = new Intent(MainActivity.this,DashboardActivityUser.class);
                i.putExtra("role",role);
                startActivity(i);
            }
            finish();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}