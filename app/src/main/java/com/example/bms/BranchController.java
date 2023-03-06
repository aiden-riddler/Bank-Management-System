package com.example.bms;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BranchController {
    public static void createBranch(Branch branch, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Branch")
                .add(branch)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Branch Added Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Branch Add Failed!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static void updateBranch(Branch branch, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Branch")
                .document(branch.getId())
                .update("address", branch.getAddress(),
                        "phone", branch.getPhone())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Branch updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error updating branch!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static void removeBranch(Branch branch, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Branch")
                .document(branch.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Branch deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error deleting branch", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static String getBranchDetails() {
        return null;
    }

    public static List<Employee> getBranchEmployees() {
        return null;
    }
}
