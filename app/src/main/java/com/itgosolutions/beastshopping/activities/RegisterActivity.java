package com.itgosolutions.beastshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.services.AccountServices;
import com.squareup.otto.Subscribe;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    EditText etUserName;
    EditText etUserEmail;
    Button btnRegister;
    Button btnLogin;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText) findViewById(R.id.et_name);
        etUserEmail = (EditText) findViewById(R.id.et_email);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Attempting to register...");
        mProgressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                registerUser();
                break;
            case R.id.btn_login:
                startLoginActivity();
                break;
            default:
        }
    }

    private void registerUser() {
        bus.post(new AccountServices.RegisterUserRequest(etUserName.getText().toString(), etUserEmail.getText().toString(), mProgressDialog));
    }

    @Subscribe
    public void registerUserResponse(AccountServices.RegisterUserResponse response) {
        if (!response.didSucceed()) {
            etUserEmail.setError(response.getPropertyError("userEmail"));
            etUserName.setError(response.getPropertyError("userName"));
        }
    }

    private void startLoginActivity() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
