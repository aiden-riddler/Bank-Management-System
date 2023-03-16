package com.example.bms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LoanApplicationsFrag extends Fragment {

    public LoanApplicationsFrag() {
        // Required empty public constructor
    }

    private RecyclerView loanApplicationsRecycler;
    private LoanViewModel loanViewModel;
    private LoanApplicationsAdapter loanApplicationsAdapter;
    private User user;
    private ConstraintLayout progressCard;
    private TextView progressText;
    private RecyclerView loansRecycler;
    private LoanAdapter loanAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) getActivity().getIntent().getSerializableExtra("User");
        progressCard = getActivity().findViewById(R.id.progress);
        progressText = getActivity().findViewById(R.id.progressText);

        loanApplicationsRecycler = view.findViewById(R.id.loanApplications);
        loanApplicationsRecycler.setHasFixedSize(true);
        loanApplicationsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        loansRecycler = view.findViewById(R.id.loans);
        loansRecycler.setHasFixedSize(true);
        loansRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        //get data
        loanApplicationsAdapter = new LoanApplicationsAdapter(new ArrayList<>(), getContext(), progressCard, progressText);
        loanApplicationsAdapter.setUser(user);
        loanViewModel = new ViewModelProvider(requireActivity()).get(LoanViewModel.class);
        loanViewModel.getLoanApplications().observe(getViewLifecycleOwner(), new Observer<ArrayList<LoanApplication>>() {
            @Override
            public void onChanged(ArrayList<LoanApplication> loanApplications) {
                loanApplicationsAdapter.setLoanApplications(loanApplications);
            }
        });
        loanApplicationsRecycler.setAdapter(loanApplicationsAdapter);

        loanAdapter = new LoanAdapter(new ArrayList<>(), getContext());
        loanAdapter.setUser(user);
        loanViewModel.getLoans().observe(getViewLifecycleOwner(), new Observer<ArrayList<Loan>>() {
            @Override
            public void onChanged(ArrayList<Loan> loans) {
                loanAdapter.setLoans(loans);
            }
        });

        loansRecycler.setAdapter(loanAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan_applications, container, false);
    }
}