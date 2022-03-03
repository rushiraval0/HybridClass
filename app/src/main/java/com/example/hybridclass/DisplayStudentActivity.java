package com.example.hybridclass;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class DisplayStudentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DisplayStudentAdapter displayStudentAdapter;
    ArrayList<String> studentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_student);

        studentList= getIntent().getStringArrayListExtra("studentList");
        System.out.println("TAGRUSY :: "+studentList);

        recyclerView = findViewById(R.id.recyclerViewS);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayStudentAdapter = new DisplayStudentAdapter(this,studentList);
        recyclerView.setAdapter(displayStudentAdapter);

    }
}