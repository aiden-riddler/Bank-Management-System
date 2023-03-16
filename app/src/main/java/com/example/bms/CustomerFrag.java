package com.example.bms;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CustomerFrag extends Fragment {
    public CustomerFrag() {
        // Required empty public constructor
    }

    private FloatingActionButton addCustomer;
    private CustomerViewModel customerViewModel;
    private AccountViewModel accountViewModel;
    private DataViewModel dataViewModel;
    private RecyclerView customerRecycler;
    private CustomerAdapter customerAdapter;
    private UserEmailPhoneIds emailPhoneIds;
    private BranchViewModel branchViewModel;

    private User user;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = (User) getActivity().getIntent().getSerializableExtra("User");

        customerRecycler = view.findViewById(R.id.customerRecycler);
        customerRecycler.setHasFixedSize(true);
        customerRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // get data
        customerAdapter = new CustomerAdapter(new ArrayList<>(), new ArrayList<>(), getContext());
        customerAdapter.setUser(user);
        emailPhoneIds = new UserEmailPhoneIds();
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        dataViewModel.getUserEmailPhoneIds().observe(getViewLifecycleOwner(), new Observer<UserEmailPhoneIds>() {
            @Override
            public void onChanged(UserEmailPhoneIds userEmailPhoneIds) {
                emailPhoneIds.addPhoneNumbers(userEmailPhoneIds.getPhoneNumbers());
                emailPhoneIds.addEmails(userEmailPhoneIds.getEmails());
                emailPhoneIds.addUserIds(userEmailPhoneIds.getUserIds());
                customerAdapter.setUserEmailPhoneIds(emailPhoneIds);
            }
        });
        customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
        customerViewModel.getCustomers().observe(getViewLifecycleOwner(), new Observer<ArrayList<Customer>>() {
            @Override
            public void onChanged(ArrayList<Customer> customers) {
                customerAdapter.setCustomers(customers);
            }
        });

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);
        accountViewModel.getAccounts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Account>>() {
            @Override
            public void onChanged(ArrayList<Account> accounts) {
                customerAdapter.setAccounts(accounts);
            }
        });

        branchViewModel = new ViewModelProvider(requireActivity()).get(BranchViewModel.class);
        branchViewModel.getBranches().observe(getViewLifecycleOwner(), new Observer<ArrayList<Branch>>() {
            @Override
            public void onChanged(ArrayList<Branch> branches) {
                customerAdapter.setBranches(branches);
            }
        });

        customerRecycler.setAdapter(customerAdapter);
        addCustomer = view.findViewById(R.id.add_customer);
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomerAdd.class);
                intent.putExtra("UserEmailIDs", emailPhoneIds);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customers, container, false);
    }
}