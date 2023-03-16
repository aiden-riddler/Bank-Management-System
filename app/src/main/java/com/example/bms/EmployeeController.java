package com.example.bms;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

public class EmployeeController {

    public static void createEmployee(Employee employee, Context context, ConstraintLayout progressCard, Button submit, User user1) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        DocumentReference employeeRef = db.collection("Employees").document();
        batch.set(employeeRef, employee);
        User user = new User(employee.getPhone(), employee.getPosition(), employee.getPin(), employeeRef.getId(), employee.getEmail());
        batch.set(db.collection("Users").document(employeeRef.getId()), user);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressCard.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Employee Added Successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, AdminPanel.class);
                    intent.putExtra("User", user1);
                    context.startActivity(intent);
                } else {
                    progressCard.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Employee Add Failed!", Toast.LENGTH_LONG).show();
                    submit.setEnabled(true);
                }
            }
        });
    }

    public static void updateEmployee(Employee employee, Context context, Employee prevEmp, ConstraintLayout progressCard, Button submit, User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        WriteBatch batch = db.batch();
        DocumentReference employeeRef = db.collection("Employees").document(prevEmp.getEmployeeID());
        batch.update(employeeRef, "email", employee.getEmail());
        batch.update(employeeRef, "phone", employee.getPhone());
        batch.update(employeeRef, "firstName", employee.getFirstName());
        batch.update(employeeRef, "lastName", employee.getLastName());
        batch.update(employeeRef, "branch", employee.getBranch());
        batch.update(employeeRef, "userid", employee.getUserid());


        if (!prevEmp.getPhone().equals(employee.getPhone())) {
            DocumentReference userRef = db.collection("Users").document(employee.getEmployeeID());
            batch.update(userRef, "phone", employee.getPhone());
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressCard.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Employee updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AdminPanel.class);
                    intent.putExtra("User", user);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Error updating employee!", Toast.LENGTH_SHORT).show();
                    progressCard.setVisibility(View.INVISIBLE);
                    submit.setEnabled(true);
                }
            }
        });

    }

    public static void removeEmployee(Employee employee, Context context, User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        DocumentReference employeeRef = db.collection("Employees").document(employee.getEmployeeID());
        batch.delete(employeeRef);
        DocumentReference userRef = db.collection("Users").document(employee.getEmployeeID());
        batch.delete(userRef);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Employee removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error deleting employee!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AdminPanel.class);
                    intent.putExtra("User", user);
                    context.startActivity(intent);
                }
            }
        });
    }

    public static boolean setManager() {
        return false;
    }

}
