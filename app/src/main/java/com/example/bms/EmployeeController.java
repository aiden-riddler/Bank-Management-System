package com.example.bms;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployeeController {

    public static void createEmployee(Employee employee, Context context, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                        .add(new User("+254" + employee.getPhone(), employee.getPosition(), employee.getPin()))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Employee Add Failed!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public static void updateEmployee(Employee employee, Context context, Employee prevEmp) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customers")
                .document(employee.getEmployeeID())
                .update("email", employee.getEmail(),
                        "phone", employee.getPhone(),
                        "firstName", employee.getFirstName(),
                        "lastName", employee.getLastName(),
                        "branch", employee.getBranch())
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

        if (!prevEmp.getPhone().equals(employee.getPhone())) {
            db.collection("Users")
                    .document(employee.getEmployeeID())
                    .update("phone", employee.getPhone())
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

    public static void removeEmployee(Employee employee, Context context) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Employees")
                    .document(employee.getEmployeeID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Employee deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error deleting employee", Toast.LENGTH_SHORT).show();
                        }
                    });

            db.collection("Users")
                    .document(employee.getEmployeeID())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Error deleting user", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    public static boolean setManager() {
        return false;
    }

}
