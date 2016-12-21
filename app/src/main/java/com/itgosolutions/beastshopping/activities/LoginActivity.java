package com.itgosolutions.beastshopping.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.AccountServices;
import com.squareup.otto.Subscribe;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Attempting to login");
        mProgressDialog.setCancelable(false);

        mSharedPreferences = getSharedPreferences(Utils.BEAST_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginUserRequest();
                break;
            case R.id.btn_register:
                startRegisterActivity();
                break;
        }
    }

    private void loginUserRequest() {
        bus.post(new AccountServices.LoginUserRequest(etEmail.getText().toString(), etPassword.getText().toString(), mProgressDialog, mSharedPreferences));
    }

    @Subscribe
    public void loginUserResponse(AccountServices.LoginUserResponse response) {
        if (!response.didSucceed()) {
            etEmail.setError(response.getPropertyError("userEmail"));
            etPassword.setError(response.getPropertyError("password"));
        }
    }

    private void startRegisterActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}
