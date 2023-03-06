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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

public class CustomerController {
    public static void createCustomer(Customer customer, Context context, ProgressBar progressBar, Branch branch, FirebaseAuth mAuth) {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                        .add(new User("+254" + customer.getPhone(), "Customer", customer.getPin()))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error inserting record!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public static void removeCustomer(Customer customer, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers")
                .document(customer.getCustomerId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Customer deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error deleting customer", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("Users")
                .document(customer.getCustomerId())
                .delete();

        db.collection("Accounts")
                .whereEqualTo("customer", customer.getCustomerId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                            String id = documentSnapshot.getId();
                            db.collection("Accounts").document(id).delete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Deleting accounts failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void updateCustomer(Customer customer, Context context, Customer prevCust){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers")
                .document(customer.getCustomerId())
                .update("email", customer.getEmail(),
                        "phone", customer.getPhone(),
                        "firstName", customer.getFirstName(),
                        "lastName", customer.getLastName())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Customer updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error updating record!", Toast.LENGTH_LONG).show();
                    }
                });

        if (!prevCust.getPhone().equals(customer.getPhone())) {
            db.collection("Users")
                    .document(customer.getCustomerId())
                    .update("phone", customer.getPhone())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Phone number updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error updating phone number!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
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
