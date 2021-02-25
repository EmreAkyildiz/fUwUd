package com.impostors.fuwud.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.impostors.fuwud.Model.Comment;
import com.impostors.fuwud.R;


public class RVCommentAdapter extends FirebaseRecyclerAdapter<Comment, RVCommentAdapter.viewholder> {
    public RVCommentAdapter(@NonNull FirebaseRecyclerOptions<Comment> options) {
        super(options);


    }



    @Override
    protected void onBindViewHolder(@NonNull RVCommentAdapter.viewholder viewholder, int i, @NonNull Comment comment) {

        viewholder.comment.setText(comment.getComments());
        viewholder.restaurantName.setText("Restaurant");
        viewholder.ownerName.setText(comment.getOwnerName());
        viewholder.restaurantReply.setText(comment.getReply());




    }


    @NonNull
    @Override
    public RVCommentAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card,parent,false);


        return new viewholder(view);

    }

    class viewholder extends RecyclerView.ViewHolder{
        TextView ownerName, comment, restaurantName, restaurantReply;


        public viewholder(@NonNull View itemView) {
            super(itemView);

            ownerName = (TextView)itemView.findViewById(R.id.OwnerRV);

            comment = (TextView)itemView.findViewById(R.id.ownerComment);

            restaurantReply = (TextView)itemView.findViewById(R.id.restaurantReply);

            restaurantName = (TextView)itemView.findViewById(R.id.restaurantName);




        }
    }
}

