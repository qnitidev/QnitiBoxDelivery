package com.badrul.qnitiboxdelivery;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {


    private Context mCtx;
    private List<Job> jobList;
    private OnItemClicked onClick;


    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public JobAdapter(Context mCtx, List<Job> jobList) {
        this.mCtx = mCtx;
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.job_list, null);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder,final int position) {
        Job job = jobList.get(position);


        holder.textSellerName.setText(job.getSellerName()); //getName
        holder.textSellerLocation.setText("Pickup: "+job.getSellerLocation()); //GetICnum
        holder.textReceiverName.setText(job.getReceiverName());
        holder.textReceiverLocation.setText("Send: "+job.getReceiverLocation()); //getStatus
        holder.textFood.setText(job.getFoodName());  //getTotalPrice

        holder.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class JobViewHolder extends RecyclerView.ViewHolder {

        TextView textSellerName, textSellerLocation, textReceiverName, textReceiverLocation,textFood;
        // ImageView imageView;
        RelativeLayout test;

        public JobViewHolder(View itemView) {
            super(itemView);

            test=itemView.findViewById(R.id.testing);
            textSellerName = itemView.findViewById(R.id.textSellerName);
            textSellerLocation = itemView.findViewById(R.id.textPickupLocation);
            textReceiverName = itemView.findViewById(R.id.textReceiverName);
            textReceiverLocation = itemView.findViewById(R.id.textReceiverLocation);
            textFood = itemView.findViewById(R.id.textFood);
            //imageView = itemView.findViewById(R.id.imageView);
        }
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}

