package com.example.tiki.unispace_app;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.classViewHolder> {

    private ArrayList<ClassroomObject> mClassList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onOccupyClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class classViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView freeuntil;
        public TextView building;
        public TextView classroom;
        public ImageView occupy;

        public classViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.imageView);
            building = itemView.findViewById(R.id.building);
            classroom = itemView.findViewById(R.id.classroom);
            freeuntil = itemView.findViewById(R.id.freeuntil);
            occupy = itemView.findViewById(R.id.image_occupy);
            occupy.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if (listener != null) {
                                                  int position = getAdapterPosition();
                                                  if (position != RecyclerView.NO_POSITION) {
                                                      listener.onOccupyClick(position);
                                                  }
                                              }
                                          }
                                      });
        }

    }


    public ClassAdapter(ArrayList<ClassroomObject> list) {
        mClassList = list;
    }

    @Override
    public classViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        classViewHolder cvh = new classViewHolder(v, mListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(classViewHolder holder, int position) {
        ClassroomObject currentClassroom = mClassList.get(position);
        holder.classroom.setText(Integer.toString(currentClassroom.getClassroom()));
        holder.building.setText(Integer.toString(currentClassroom.getBuilding()));
        holder.freeuntil.setText(currentClassroom.getFreeUntil());
        //holder.imageView.setImageResource(currentClassroom.getImageResource());
        //holder.occupy.setImageResource(R.drawable.ic_weekend_black_24dp);
    }

    @Override
    public int getItemCount() {
        return mClassList.size();
    }
}
