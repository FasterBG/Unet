package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterTalksList extends RecyclerView.Adapter<AdapterTalksList.MyHolder> {

    Context context;
    List<String> uids;
    List<String> profileNames;

    public AdapterTalksList(Context context, List<String> uids, List<String> profileNames) {
        this.context = context;
        this.uids = uids;
        this.profileNames = profileNames;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.talks_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent to_chat_activity = new Intent(context, BusinessChatActivity.class);
                to_chat_activity.putExtra("userUid", mUserUid);
                to_chat_activity.putExtra("userName", mUserName);
                to_chat_activity.putExtra("companyName", companyName);
                context.startActivity(to_chat_activity);
                */
            }
        });
        holder.profileName.setText(profileNames.get(position));
    }

    @Override
    public int getItemCount() {
        return uids.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        private TextView profileName;

        public MyHolder(@NonNull View itemView){
            super(itemView);

            profileName = itemView.findViewById(R.id.profileName);
        }
    }
}

