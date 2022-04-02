package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class ViewResultDetailed extends AppCompatActivity {

    private DatabaseReference myRef;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private MaterialButton button;

    private ViewResultDetailed.TestAdapter testAdapter;
    ArrayList<TestResults> result=new ArrayList<>();
    private String testName;
    Spinner spinner;
    private int lastPos = -1;
    public boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result_detailed);


        isAdmin = getIntent().getBooleanExtra("ISAdmin",false);
        testName=getIntent().getStringExtra("test");
        spinner = findViewById(R.id.spinnerViewQuiz);
        spinner.setVisibility(View.INVISIBLE);
        if(!isAdmin) {
            setTitle("Result");
        }


        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        avLoadingIndicatorView = findViewById(R.id.loader1);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        avLoadingIndicatorView.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef= database.getReference();
        ListView listView = findViewById(R.id.test_listview);
        testAdapter= new TestAdapter(ViewResultDetailed.this,result);
        listView.setAdapter(testAdapter);
        getSupportActionBar().setTitle(testName);
        getResults();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public boolean isExternalStorageReadOnly() {
        return "mounted_ro".equals(Environment.getExternalStorageState());
    }

    public boolean isExternalStorageAvailable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public void getResults(){

        myRef.child("Results").child(testName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                result.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    TestResults t=new TestResults();
                    t.userID=snapshot.getKey();
                    t.score= Objects.requireNonNull(snapshot.getValue()).toString();
                    result.add(t);
                }

                Collections.sort(result, new Comparator<TestResults>() {
                    @Override
                    public int compare(TestResults o1, TestResults o2) {
                        return o2.score.compareTo(o1.score);
                    }
                });

                getDetails();
                Log.e("The read success: " ,"su"+result.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
    }


    private void getDetails(){
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i=0;i<result.size();i++){
                    if(dataSnapshot.child(result.get(i).userID).exists())
                        result.get(i).user=dataSnapshot.child(result.get(i).userID).getValue(User.class);
                    else {
                        User user1 = new User();
                        user1.setName(result.get(i).userID);
                        result.get(i).user = user1;
                    }

                }
                testAdapter.dataList=result;
                testAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                avLoadingIndicatorView.hide();
            }
        });
    }


    class TestResults{

        public String userID,score;
        public User user;
    }


    class TestAdapter extends ArrayAdapter<TestResults> {
        private Context mContext;
        ArrayList<TestResults> dataList;

        public TestAdapter( Context context,ArrayList<TestResults> list) {
            super(context, 0 , list);
            mContext = context;
            dataList = list;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.test_item,parent,false);
            ((ImageView)listItem.findViewById(R.id.item_imageView))
                    .setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ranking));
            if(dataList.get(position).user!=null)
                ((TextView)listItem.findViewById(R.id.item_textView)).setText(dataList.get(position).user.getName());
            else {
                ((TextView) listItem.findViewById(R.id.item_textView)).setText("Details not added yet");
            }

            Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPos) ? R.anim.up_from_bottom : R.anim.down_from_top);
            (listItem).startAnimation(animation);
            lastPos = position;
            if(isAdmin ) {
                ((TextView) listItem.findViewById(R.id.item_textView)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dataList.get(position).user.getName() != null) {

                            Intent intent = new Intent(ViewResultDetailed.this, GetDetailReport.class);
                            intent.putExtra("USERID", dataList.get(position).userID);
                            intent.putExtra("TestNAME", testName);
                            intent.putExtra("Marks", dataList.get(position).score);
                            startActivity(intent);
                        }
                    }
                });
            }
            ((Button)listItem.findViewById(R.id.item_button)).setText(dataList.get(position).score);
            return listItem;
        }
    }
}
