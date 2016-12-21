package com.itgosolutions.beastshopping.services;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.itgosolutions.beastshopping.infrastructure.ServiceResponse;

public class AccountServices {

    private AccountServices() {
    }

    public static class RegisterUserRequest{
        public String userName;
        public String userEmail;
        public ProgressDialog progressDialog;

        public RegisterUserRequest(String userName, String userEmail, ProgressDialog progressDialog) {
            this.userName = userName;
            this.userEmail = userEmail;
            this.progressDialog = progressDialog;
        }
    }

    public static class RegisterUserResponse extends ServiceResponse{

    }

    public static class LoginUserRequest{
        public String userEmail;
        public String password;
        public ProgressDialog progressDialog;
        public SharedPreferences sharedPreferences;

        public LoginUserRequest(String userEmail, String password, ProgressDialog progressDialog, SharedPreferences sharedPreferences) {
            this.userEmail = userEmail;
            this.password = password;
            this.progressDialog = progressDialog;
            this.sharedPreferences = sharedPreferences;
        }
    }

    public static class LoginUserResponse extends ServiceResponse{

    }


}
