package com.example.bms;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextInputEditText phoneView;
    private TextInputEditText otpCodeView;
    private TextView counter;
    private Button sendOtp;
    private String mVerificationID;
    private Button verifyOtp;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private Timer timer;
    private int secs = 120;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }

        db = FirebaseFirestore.getInstance();

        timer = new Timer();
        phoneView = findViewById(R.id.phone);
        otpCodeView = findViewById(R.id.otp_code);
        counter = findViewById(R.id.timer);
        sendOtp = findViewById(R.id.send_otp);
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
                counter.setVisibility(View.INVISIBLE);
                counter.setTextColor(Color.GRAY);
            }
        });

        verifyOtp = findViewById(R.id.verify_otp);
        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
    }

    private void verify() {
        otpCodeView.setError(null);

        String otp = otpCodeView.getText().toString();

        if (TextUtils.isEmpty(otp)){
            otpCodeView.setError("PLease enter OTP you received");
            otpCodeView.requestFocus();
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, otp);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                db.collection("Users")
                                        .whereEqualTo("phone", mAuth.getCurrentUser().getPhoneNumber())
                                        .limit(1)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                User user = null;
                                                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                                                    user = documentSnapshot.toObject(User.class);
                                                }

                                                Intent intent = new Intent(MainActivity.this, Login.class);
                                                intent.putExtra("User", user);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "User does not exist! Contact Admin.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                Intent intent = new Intent(MainActivity.this, Login.class);
                            } else {
                                Toast.makeText(MainActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void validateInput() {
        phoneView.setError(null);

        boolean cancel = false;

        String phone = phoneView.getText().toString();

        if (!InputValidation.phoneValidate(phone)){
            cancel = true;
            phoneView.setError("Phone number should be 9 characters long");
        }

        if (cancel){
            phoneView.requestFocus();
        } else {
            otpSend(phone);
        }
    }

    private void otpSend(String phone) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("BMS", "onCodeSent:" + verificationId);

                verifyOtp.setEnabled(true);
                otpCodeView.setEnabled(true);
                mVerificationID = verificationId;
                counter.setVisibility(View.VISIBLE);

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (secs == 0){
                            timer.cancel();
                        } else {
                            if (secs == 20){
                                counter.setTextColor(Color.RED);
                            }
                            counter.setText(secToTime(secs));
                            secs--;
                        }
                    }
                }, 1000, 1000);

            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+254" + phone.trim())
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public String secToTime(int seconds) {
        String min = String.valueOf(Math.floor(seconds/60));
        String sec = String.valueOf(seconds % 60);

        if (min.length() == 1)
            min = "0" + min;

        if (sec.length() == 1)
            sec = "0" + sec;

        return min + ":" + sec;
    }
}
