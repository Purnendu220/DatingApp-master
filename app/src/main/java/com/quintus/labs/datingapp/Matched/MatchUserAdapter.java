package com.quintus.labs.datingapp.Matched;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.SplashActivity;
import com.quintus.labs.datingapp.chat.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class MatchUserAdapter extends RecyclerView.Adapter<MatchUserAdapter.MyViewHolder> {
    List<Users> usersList;
    Context context;

    public MatchUserAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchUserAdapter.MyViewHolder holder, int position) {
        Users users = usersList.get(position);
        holder.name.setText(users.getName());
        holder.profession.setText(users.getBio());
        if (users.getProfileImageUrl() != null) {
            Picasso.get().load(users.getProfileImageUrl()).into(holder.imageView);
        }
        holder.layoutMatchedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, ChatActivity.class);
                in.setFlags(FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("user",users);
                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name, profession;
        LinearLayout layoutMatchedUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            profession = itemView.findViewById(R.id.mui_profession);
            layoutMatchedUser = itemView.findViewById(R.id.layoutMatchedUser);
        }
    }
}
