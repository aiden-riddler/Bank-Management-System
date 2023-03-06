package com.example.bms;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class BranchAdd extends AppCompatActivity {
    private TextInputEditText addressView;
    private TextInputEditText phoneView;
    private Button submit;
    private boolean isUpdate = false;
    private Branch prevBranch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_branch);

        addressView = findViewById(R.id.address);
        phoneView = findViewById(R.id.phone);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });

        // get intents
        prevBranch = (Branch) getIntent().getSerializableExtra("Branch");
        if (prevBranch != null) {
            isUpdate = true;
            submit.setText("UPDATE");
            addressView.setText(prevBranch.getAddress());
            phoneView.setText(prevBranch.getPhone());
        }
    }

    private void validateInput() {
        phoneView.setError(null);
        phoneView.setError(null);

        boolean cancel = false;
        View focusView = null;

        String address = addressView.getText().toString();
        String phone = phoneView.getText().toString();

        if (!InputValidation.phoneValidate(phone)){
            cancel = true;
            focusView = phoneView;
            phoneView.setError("Phone number should be 9 characters long");
        }

        if (TextUtils.isEmpty(address)){
            cancel = true;
            focusView = addressView;
            addressView.setError("This field is required");
        }

        if (cancel)
            focusView.requestFocus();
        else {
            if (!isUpdate)
                BranchController.createBranch(new Branch(address, phone), BranchAdd.this);
            else
                BranchController.updateBranch(new Branch(address, phone), BranchAdd.this);
        }
    }
}
