package com.impostors.fuwud.Activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.impostors.fuwud.Adapter.RVCommentAdapter;

import com.impostors.fuwud.Model.Comment;
import com.impostors.fuwud.Model.Restaurant;
import com.impostors.fuwud.Model.User;
import com.impostors.fuwud.R;

import java.util.logging.LoggingMXBean;

public class FragmentRDComments extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Activity context;
    RecyclerView recyclerView;
    private RVCommentAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    private ImageButton sendButton;
    EditText commentSection;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_r_d_comments, container, false);

        context = getActivity();
        sendButton = view.findViewById(R.id.sendButton);
        recyclerView = view.findViewById(R.id.allcommentsrest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        commentSection = view.findViewById(R.id.commentSec);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        final String restaurantId=getActivity().getIntent().getStringExtra("restaurant_id");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Comment comment = new Comment();
                comment.setOwnerId(currentUser.getUid());
                comment.setComments(commentSection.getText().toString());

            /*    Query query=databaseReference.child("restaurants").orderByKey().equalTo(restaurantId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d:snapshot.getChildren()){
                            Restaurant a=d.getValue(Restaurant.class);
                            comment.setRestaurantName(d.getValue().toString());



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
                Query queryforUser=databaseReference.child("users").orderByKey().equalTo(currentUser.getUid());
                queryforUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d:snapshot.getChildren()){

                            User user=d.getValue(User.class);
                            comment.setOwnerName(user.getName());
                            databaseReference.child("restaurants").child(restaurantId).child("comments").push().setValue(comment);
                            commentSection.setText(null);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }






        });

        FirebaseRecyclerOptions<Comment>options = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(FirebaseDatabase.getInstance().getReference("restaurants").child(restaurantId).child("comments"),(Comment.class)).build();


        adapter = new RVCommentAdapter(options);
        recyclerView.setAdapter(adapter);


        return view;
    }
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}