package com.example.bms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText emailView;
    private TextInputEditText passwordView;
//    private ProgressBar progressBar;

    private CircularProgressIndicator progressBar;
    private Button login;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified() == true){
            Intent intent = new Intent(MainActivity.this, AdminPanel.class);
            startActivity(intent);
        }

        progressBar = new CircularProgressIndicator(this);
        progressBar.setIndeterminate(true);
        progressBar.setTag("Please Wait");



        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });
    }

    private void validateInput() {
        emailView.setError(null);
        passwordView.setError(null);

        boolean isValid = true;
        View focusView = null;

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (!InputValidation.emailValidate(email)){
            isValid = false;
            focusView = emailView;
            emailView.setError("Invalid Email Address");
        }

        if (!InputValidation.passwordValidate(password)){
            isValid = false;
            focusView = passwordView;
            passwordView.setError("Invalid Password. Password should be > 4 characters.");
        }

        if (isValid){
            signIn(email, password);
        } else {
            focusView.requestFocus();
        }
    }

    private void signIn(String email, String password) {
        progressBar.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("BMS", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            if (user.isEmailVerified()){
                                Intent intent = new Intent(MainActivity.this, AdminPanel.class);
                                startActivity(intent);
                            } else {
                                new MaterialAlertDialogBuilder(MainActivity.this)
                                        .setTitle("Email Verification")
                                        .setMessage("Email not verified.Open " + user.getEmail() + " and click verification link. Click below if you didn't receive link.")
                                        .setPositiveButton("SEND LINK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                sendEmailVerification();
                                            }
                                        })
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("BMS", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Email Verification")
                            .setMessage("Verification email sent to " + user.getEmail() + ". Open email and click link to verify.")
                            .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    new MaterialAlertDialogBuilder(MainActivity.this)
                            .setTitle("Email Verification")
                            .setMessage("Failed to send verification email.")
                            .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    sendEmailVerification();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setCancelable(true)
                            .show();
                }
            }
        });
    }
}
