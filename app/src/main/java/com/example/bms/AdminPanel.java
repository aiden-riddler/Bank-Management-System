package com.example.bms;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private CustomerViewModel viewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);

        // get data
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        db.collection("Customers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Customer> customers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customer customer = document.toObject(Customer.class);
                                customer.setCustomerID(document.getId());
                                customers.add(customer);
                            }
                            viewModel.setCustomers(customers);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.customers);
    }
    CustomerFrag firstFragment = new CustomerFrag();
    TellerFrag secondFragment = new TellerFrag();
    BranchFrag thirdFragment = new BranchFrag();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.customers:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();
                return true;

            case R.id.tellers:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, secondFragment).commit();
                return true;

            case R.id.branch:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, thirdFragment).commit();
                return true;
        }
        return false;
    }
}
