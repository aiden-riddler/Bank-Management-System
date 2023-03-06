package com.example.bms;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextView phone;
    private FirebaseAuth mAuth;
    private User user;
    private Button login;
    private TextInputEditText pinView;
    private String phoneNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        user = (User) getIntent().getSerializableExtra("User");
        phoneNumber = user.getPhone();

        phone = findViewById(R.id.phone);
        phone.setText(phoneNumber);
        login = findViewById(R.id.login);
        pinView = findViewById(R.id.pin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinView.setError(null);
                String pin = pinView.getText().toString();
                if (TextUtils.isEmpty(pin) || pin.length() != 4){
                    pinView.setError("Enter 4 digit pin");
                    pinView.requestFocus();
                } else {
                    if (Integer.parseInt(pin) == user.getPin()){
                        Intent intent = new Intent();
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Incorrect Pin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
