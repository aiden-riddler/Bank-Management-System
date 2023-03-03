package com.example.bms;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class CustomerController {
    public static void createCustomer(Customer customer, Context context, ProgressBar progressBar, Branch branch, FirebaseAuth mAuth) {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers")
                .add(customer)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // create account
                        Account account = new Account(documentReference.getId(), branch.getId(), new Date(), 50.00, 17.5);
                        AccountController.createAccount(account, context, mAuth);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Bank Account creation failed!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static boolean removeCustomer() {
        return false;
    }

    public static Customer getCustomerOfAccount(Account account) {
        return null;
    }

    public static Customer findCustomerByID(int id) {
        return null;
    }

    public static List<Customer> findCustomersByName(String name) {
        return null;
    }

}
