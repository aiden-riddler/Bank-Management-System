package com.example.bms;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerFrag extends Fragment {
    public CustomerFrag() {
        // Required empty public constructor
    }

    private FloatingActionButton addCustomer;
    private TextView t1;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addCustomer = view.findViewById(R.id.add_customer);
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomerAdd.class);
                startActivity(intent);
            }
        });

        t1 = view.findViewById(R.id.textView);
        t1.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customers, container, false);
    }
}