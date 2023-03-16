package com.example.bms;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFrag extends Fragment {
    public AccountFrag() {
        // Required empty public constructor
    }

    private ImageView logout;
    private TextView logoutText;
    private FirebaseAuth mAuth;
    private TextView fullnameView;
    private TextView emailView;
    private TextView phoneView;
    private User user;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = (User) getActivity().getIntent().getSerializableExtra("User");

        fullnameView = view.findViewById(R.id.fullname);
        emailView = view.findViewById(R.id.email);
        emailView.setText(user.getEmail());
        phoneView = view.findViewById(R.id.phone);
        phoneView.setText(user.getPhone());
        logout = view.findViewById(R.id.logout);
        logoutText = view.findViewById(R.id.logoutText);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

    }
    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
}