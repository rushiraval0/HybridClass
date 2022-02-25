package com.example.hybridclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayMaterial extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MaterialAdapter myAdapter;
    ArrayList<FileMaterial> list;
    ArrayList<String> myClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_material);

        recyclerView = findViewById(R.id.recyclerView1);
        databaseReference = FirebaseDatabase.getInstance().getReference("pdf");
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myClass = new ArrayList<>();

        myAdapter = new MaterialAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    FileMaterial fileMaterial = dataSnapshot.getValue(FileMaterial.class);

                    System.out.println("RUSYR");
                    System.out.println(fileMaterial);
                    list.add(fileMaterial);

                }

                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}