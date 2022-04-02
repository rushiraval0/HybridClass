package com.example.hybridclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class PersonalAttendance extends AppCompatActivity  implements PersonalAttFragInterface  {

    private String module;
    private String studentID;
    TextView toolbarText;
    Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_attendance);

        TabLayout tabLayout = findViewById(R.id.personalAttTabs);

        toolbar = findViewById(R.id.toolbar);
        toolbarText = toolbar.findViewById(R.id.personalToolbarTV);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.personalVP);
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdapter.addFragment(new PersonalAttendedFragment(), "Attended");
        vpAdapter.addFragment(new PersonalAbsentFragment(), "Absent");

        viewPager.setAdapter(vpAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        module = bundle.getString("MOD_ID");
        studentID = bundle.getString("STU_ID");

        toolbarText.setText(module);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    @Override
    public String getModuleName() {
        return module;
    }

    @Override
    public String getStudentID() {
        return studentID;
    }
}
