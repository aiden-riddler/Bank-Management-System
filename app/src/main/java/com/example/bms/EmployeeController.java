package com.example.bms;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class EmployeeController {

    public static void createEmployee(Employee employee, Context context, ProgressBar progressBar, Branch branch, FirebaseAuth mAuth) {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Employees")
                .add(employee)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Employee Added Successfully!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Employee Add Failed!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static boolean updateEmployee() {
        return false;
    }

    public static boolean removeEmployee() {
        return false;
    }

    public static boolean setManager() {
        return false;
    }

}
