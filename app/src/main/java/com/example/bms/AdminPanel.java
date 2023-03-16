package com.example.bms;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private AccountViewModel accountViewModel;
    private BranchViewModel branchViewModel;
    private EmployeeViewModel employeeViewModel;
    private ConstraintLayout progressCard;
    private TextView progressText;
    private DataViewModel dataViewModel;
    private LoanViewModel loanViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);

        progressCard = findViewById(R.id.progress);
        progressText = findViewById(R.id.progressText);

        // get data
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        db.collection("Customers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            UserEmailPhoneIds userEmailPhoneIds = new UserEmailPhoneIds();
                            ArrayList<Customer> customers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Customer customer = document.toObject(Customer.class);
                                customer.setCustomerId(document.getId());
                                customers.add(customer);

                                userEmailPhoneIds.addPhone(customer.getPhone());
                                userEmailPhoneIds.addEmail(customer.getEmail());
                                userEmailPhoneIds.addUserId(customer.getUserid());
                            }
                            viewModel.setCustomers(customers);
                            dataViewModel.setData(userEmailPhoneIds);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        db.collection("Accounts")
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
//                            Toast.makeText(AdminPanel.this, "Accounts Size: " + accounts.size(), Toast.LENGTH_SHORT).show();
                            accountViewModel.setAccounts(accounts);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);
        db.collection("Employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Employee> employees = new ArrayList<>();
                            UserEmailPhoneIds userEmailPhoneIds = new UserEmailPhoneIds();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Employee employee = document.toObject(Employee.class);
                                employee.setEmployeeID(document.getId());
                                employees.add(employee);

                                userEmailPhoneIds.addPhone(employee.getPhone());
                                userEmailPhoneIds.addEmail(employee.getEmail());
                                userEmailPhoneIds.addUserId(employee.getUserid());
                            }
                            employeeViewModel.setEmployees(employees);
                            dataViewModel.setData(userEmailPhoneIds);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        branchViewModel = new ViewModelProvider(this).get(BranchViewModel.class);
        db.collection("Branch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Branch> branches = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Branch branch = document.toObject(Branch.class);
                                branch.setId(document.getId());
                                branches.add(branch);
                            }
                            branchViewModel.setBranches(branches);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        loanViewModel = new ViewModelProvider(this).get(LoanViewModel.class);
        db.collection("LoanApplications")
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
                case "LoanApplications":
                    bottomNavigationView.setSelectedItemId(R.id.loans);
                    break;
                default:
                    bottomNavigationView.setSelectedItemId(R.id.customers);
                    break;
            }
        } else {
            bottomNavigationView.setSelectedItemId(R.id.customers);
        }

    }
    CustomerFrag firstFragment = new CustomerFrag();
    EmployeeFrag secondFragment = new EmployeeFrag();
    BranchFrag thirdFragment = new BranchFrag();
    AccountFrag fourthFragment = new AccountFrag();
    LoanApplicationsFrag fifthFragment = new LoanApplicationsFrag();
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

            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fourthFragment).commit();
                return true;

            case R.id.loans:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fifthFragment).commit();
                return true;
        }
        return false;
    }
}
