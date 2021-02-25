package com.impostors.fuwud.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Model.Comment;
import com.impostors.fuwud.R;

import org.w3c.dom.Text;


public class RVReplyCommentAdapter extends FirebaseRecyclerAdapter<Comment, RVReplyCommentAdapter.viewholder> {
    public RVReplyCommentAdapter(@NonNull FirebaseRecyclerOptions<Comment> options, Context mcontext, Activity activity)
    {
        super(options);
        this.mcontext=mcontext;
        this.activity=activity;

    }

    Context mcontext;
    Activity activity;

    TextView textReply2;
    Button buttonreply;








    @Override
    protected void onBindViewHolder(@NonNull RVReplyCommentAdapter.viewholder viewholder, int i, @NonNull Comment comment) {

        viewholder.comment.setText(comment.getComments());
        viewholder.textReply.setText(comment.getReply());
        viewholder.textViewUserNameOnR.setText(comment.getOwnerName());



    }


    @NonNull
    @Override
    public RVReplyCommentAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reply_comment,parent,false);


        return new viewholder(view);

    }

    class viewholder extends RecyclerView.ViewHolder{
        TextView comment;
        TextView textReply;
        TextView textViewUserNameOnR;



        public viewholder(@NonNull View itemView) {
            super(itemView);
            init();







            buttonreply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View editTextAlert = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.reply_alert, null);
                 AlertDialog.Builder ad = new AlertDialog.Builder(mcontext);
                ad.setTitle("Reply");
                ad.setView(editTextAlert);
                ad.setPositiveButton("Reply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        textReply2=editTextAlert.findViewById(R.id.editTextReply);
                        getRef(getAdapterPosition()).child("reply").setValue(textReply2.getText().toString());

                    }
                });
                ad.create().show();





            }
        });

            //ownerId = (TextView)itemView.findViewById(R.id.OwnerRV);



        }
        public void init(){
            FirebaseDatabase firebaseDatabase;
            DatabaseReference databaseReference;
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            FirebaseAuth auth;
            FirebaseUser currentUser;
            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
            comment = (TextView)itemView.findViewById(R.id.ownerComment);
            buttonreply=itemView.findViewById(R.id.buttonReply);
            textReply=itemView.findViewById(R.id.textReply);
            textViewUserNameOnR=itemView.findViewById(R.id.textViewUserNameOnR);
        }
    }
}


