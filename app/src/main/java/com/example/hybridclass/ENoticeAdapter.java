//package com.example.hybridclass;
//
//import android.app.DownloadManager;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.ArrayList;
//
//public class ENoticeAdapter extends RecyclerView.Adapter<ENoticeAdapter.MyViewHolder> {
//
//    public ENoticeAdapter(Context context, ArrayList<ENotice> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    Context context;
//    ArrayList<ENotice> list;
//
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.view_file_card,parent,false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//        ENotice eNotice = list.get(position);
//        holder.fileName.setText(fileMaterial.getPdfName());
//        holder.fileClassCode.setText(fileMaterial.getClassCode());
//        holder.fileDownloadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
////                Uri uri = Uri.parse(fileMaterial.getPdfUrl());
////                DownloadManager.Request request = new DownloadManager.Request(uri);
////                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
////                long reference = manager.enqueue(request);
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(fileMaterial.getPdfUrl()));
//                context.startActivity(i);
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//
//
//        TextView fileName;
//        TextView fileClassCode;
//        Button fileDownloadButton;
//
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            fileName = itemView.findViewById(R.id.fileNameGen);
//            fileClassCode = itemView.findViewById(R.id.fileClassCodeGen);
//            fileDownloadButton = itemView.findViewById(R.id.fileDownloadButton);
//        }
//    }
//}
