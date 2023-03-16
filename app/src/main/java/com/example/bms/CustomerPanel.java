package com.example.bms;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerPanel extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    private AccountViewModel accountViewModel;
    private LoanViewModel loanViewModel;
    private FirebaseFirestore db;
    private User user;
    private CustomerViewModel customerViewModel;
    HomeFrag firstFragment = new HomeFrag();
    AccountsFrag secondFragment = new AccountsFrag();
    AccountFrag thirdFragment = new AccountFrag();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_panel);
        user = (User) getIntent().getSerializableExtra("User");

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        db = FirebaseFirestore.getInstance();
        db.collection("Accounts")
                .whereEqualTo("customer", user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Account> accounts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Account account = document.toObject(Account.class);
                                account.setAccountNumber(document.getId());
                                accounts.add(account);
                            }
                            accountViewModel.setAccounts(accounts);
                        } else
                            Log.d("BMS", "Error getting documents: ", task.getException());

                    }
                });

        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        loanViewModel = new ViewModelProvider(this).get(LoanViewModel.class);
        db.collection("LoanApplications")
                .whereEqualTo("customer", user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<LoanApplication> loanApplications = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LoanApplication loanApplication = document.toObject(LoanApplication.class);
                                loanApplication.setLoanNumber(document.getId());
                                loanApplications.add(loanApplication);
                            }
                            loanViewModel.setLoanApplications(loanApplications);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("Loans")
                .whereEqualTo("customer", user.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Loan> loans = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Loan loan = document.toObject(Loan.class);
                                loan.setId(document.getId());
                                loans.add(loan);
                            }
                            loanViewModel.setLoans(loans);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        String openFragment = getIntent().getStringExtra("OpenFragment");
        if (openFragment != null){
            switch (openFragment){
                case "Accounts":
                    bottomNavigationView.setSelectedItemId(R.id.accounts);
                    break;
                default:
                    bottomNavigationView.setSelectedItemId(R.id.home);
                    break;
            }
        } else {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();
                return true;

            case R.id.accounts:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, secondFragment).commit();
                return true;

            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, thirdFragment).commit();
                return true;
        }
        return false;
    }
}
