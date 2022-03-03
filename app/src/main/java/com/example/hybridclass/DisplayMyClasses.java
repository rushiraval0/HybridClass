package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DisplayMyClasses extends AppCompatActivity{

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<Classroom> list;
    ArrayList<String> myClass;

    MyAdapter.onClassListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_classes);

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Classroom");
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myClass = new ArrayList<>();

        setOnClickListener();

        myAdapter = new MyAdapter(this,list,listener);
        recyclerView.setAdapter(myAdapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot: snapshot.child("classList").getChildren()) {
                    myClass.add(childSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                        Classroom classroom = dataSnapshot.getValue(Classroom.class);
                        for (int i = 0; i < myClass.size(); i++) {
                            System.out.println("In loop class :: wknd");
                            try {
                            if (myClass.get(i) == null) {
                                continue;
                            }

                            if (classroom.getClassCode().equalsIgnoreCase(myClass.get(i))) {
                                list.add(classroom);
                            }
                            HashMap<String,String> Email = classroom.getStudentList();
                            System.out.println("TAGRUSY "+Email.values());
                        }

                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        System.out.println("Out loop class :: wknd");
                    }

                myAdapter.notifyDataSetChanged();

            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setOnClickListener() {
        listener = new MyAdapter.onClassListener() {
            @Override
            public void onClassClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(),DisplayStudentActivity.class);
                System.out.println("TAGRUSY :: "+list.get(position).getStudentList().values());
                intent.putStringArrayListExtra("studentList",new ArrayList<>(list.get(position).getStudentList().values()));
                startActivity(intent);
            }
        };
    }


}