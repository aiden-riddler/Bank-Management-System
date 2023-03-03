package com.example.bms;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AccountController {
    public static void createAccount(Account account, Context context, FirebaseAuth mAuth) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Accounts")
                .add(account)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // create account
                        Toast.makeText(context, "Account Created Successfully. Sending verification email", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Bank Account creation failed! Contact Admin", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static boolean updateAccount() {
        return false;
    }

    public static boolean removeAccount() {
        return false;
    }

    public static List<Account> getAccountsOfCustomer() {
        return null;
    }

    public static String getAccountDetails() {
        return null;
    }
}
