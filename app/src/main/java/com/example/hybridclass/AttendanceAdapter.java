package com.example.hybridclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {

        Context context;

    public AttendanceAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }
    ArrayList<String> list;


@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_rv_layout,
            parent, false);
        return new MyViewHolder(v);
        }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    String s = list.get(position);
    holder.studentName.setText(s);
}

@Override
public int getItemCount() {
        return list.size();
        }

public static class MyViewHolder extends RecyclerView.ViewHolder{


    TextView studentName;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        studentName = itemView.findViewById(R.id.studentTV);
    }
}
}
