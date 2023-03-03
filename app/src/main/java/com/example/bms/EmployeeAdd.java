package com.example.bms;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeAdd extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextInputEditText emailView;
    private TextInputEditText firstNameView;
    private TextInputEditText phoneView;
    private TextInputEditText lastNameView;
    private TextInputEditText idView;
    private TextInputEditText positionView;
    private ProgressBar progressBar;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> arrayAdapter;
    private Button submit;
    private List<Branch> branches;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialise firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        branches = new ArrayList<>();
        db.collection("Branch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Branch branch = document.toObject(Branch.class);
                                branch.setId(document.getId());
                                branches.add(branch);
                            }

                            //add branches to string
                            String[] branchList = new String[branches.size()];
                            for (int i=0; i<branches.size(); i++)
                                branchList[i] = branches.get(i).getAddress();

                            // add items to dropdown
                            arrayAdapter = new ArrayAdapter<>(EmployeeAdd.this, R.layout.list_item, branchList);
                            autoCompleteTextView = findViewById(R.id.autoComplete);
                            autoCompleteTextView.setAdapter(arrayAdapter);
                        } else {
                            Log.d("BMS", "Error getting documents: ", task.getException());
                        }
                    }
                });

        progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);



        emailView = findViewById(R.id.email);
        firstNameView = findViewById(R.id.firstname);
        lastNameView = findViewById(R.id.firstname);
        phoneView = findViewById(R.id.phone);
        idView = findViewById(R.id.id_num);
        positionView = findViewById(R.id.position);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate input
                validateInput();
            }
        });
    }

    private void validateInput() {
        firstNameView.setError(null);
        lastNameView.setError(null);
        emailView.setError(null);
        phoneView.setError(null);
        idView.setError(null);
        autoCompleteTextView.setError(null);
        positionView.setError(null);

        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        String email = emailView.getText().toString();
        String phone = phoneView.getText().toString();
        String id = idView.getText().toString();
        String branchName = autoCompleteTextView.getText().toString();
        String position = positionView.getText().toString();

        boolean isValid = true;
        View focusView = null;

        if (TextUtils.isEmpty(branchName)){
            isValid = false;
            focusView = autoCompleteTextView;
            autoCompleteTextView.setError("Please select branch");
        }

        if (!InputValidation.phoneValidate(phone)){
            isValid = false;
            focusView = phoneView;
            phoneView.setError("Phone number should be 9 characters long");
        }

        if (TextUtils.isEmpty(position)){
            isValid = false;
            focusView = positionView;
            positionView.setError("This field is required");
        }

        if (!InputValidation.emailValidate(email)){
            isValid = false;
            focusView = emailView;
            emailView.setError("Invalid Email Address");
        }

        if (TextUtils.isEmpty(id)){
            isValid = false;
            focusView = idView;
            idView.setError("This field is required");
        }

        if (TextUtils.isEmpty(lastName)){
            isValid = false;
            focusView = lastNameView;
            lastNameView.setError("This field is required");
        }

        if (TextUtils.isEmpty(firstName)){
            isValid = false;
            focusView = firstNameView;
            firstNameView.setError("This field is required");
        }

        if (isValid){
            Employee employee = new Employee();
            employee.setUserid(Integer.parseInt(id));
            employee.setEmail(email);
            employee.setPhone(phone);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setRegistrationDate(new Date());

            Branch branch = null;
            for (Branch b:branches){
                if (b.getAddress().equals(branchName))
                    branch = b;
            }
            employee.setBranch(branch.getId());

            EmployeeController.createEmployee(employee, this, progressBar, branch, mAuth);
        } else {
            focusView.requestFocus();
        }
    }
}
