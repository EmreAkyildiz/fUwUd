package com.impostors.fuwud.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.impostors.fuwud.Adapter.RVReplyCommentAdapter;
import com.impostors.fuwud.Model.Comment;
import com.impostors.fuwud.R;

public class FragmentRestaurantComments extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Activity context;
    RecyclerView recyclerView;
    TextView textReply,restName;
    Button buttonreply;
    private RVReplyCommentAdapter adapter;
    FirebaseAuth auth;
    FirebaseUser User;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_comments, container, false);

        context = getActivity();

        recyclerView = view.findViewById(R.id.allcommentsrest);
        buttonreply=view.findViewById(R.id.buttonReply);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        auth = FirebaseAuth.getInstance();
        User = auth.getCurrentUser();
        textReply=view.findViewById(R.id.textReply);
        restName=view.findViewById(R.id.restName);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        final String restaurantId=getActivity().getIntent().getStringExtra("restaurant_id");



        FirebaseRecyclerOptions<Comment> options = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(FirebaseDatabase.getInstance().getReference("restaurants").child(User.getUid()).child("comments"),(Comment.class)).build();


        adapter = new RVReplyCommentAdapter(options,context,getActivity());
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

