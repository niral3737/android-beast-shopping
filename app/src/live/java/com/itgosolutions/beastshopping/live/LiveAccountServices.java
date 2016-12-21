package com.itgosolutions.beastshopping.live;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.itgosolutions.beastshopping.activities.LoginActivity;
import com.itgosolutions.beastshopping.activities.MainActivity;
import com.itgosolutions.beastshopping.entities.User;
import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.AccountServices;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public class LiveAccountServices extends BaseLiveService {

    public LiveAccountServices(BeastShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void registerUserRequest(final AccountServices.RegisterUserRequest request) {
        AccountServices.RegisterUserResponse response = new AccountServices.RegisterUserResponse();

        if (request.userEmail.isEmpty()) {
            response.setPropertyErrors("userEmail", "Please enter email.");
        }

        if (request.userName.isEmpty()) {
            response.setPropertyErrors("userName", "Please enter username.");
        }

        if (response.didSucceed()) {
            request.progressDialog.show();

            SecureRandom random = new SecureRandom();
            final String randomPassword = new BigInteger(32, random).toString();

            auth.createUserWithEmailAndPassword(request.userEmail, randomPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                request.progressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {

                                auth.sendPasswordResetEmail(request.userEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (!task.isSuccessful()) {
                                                    request.progressDialog.dismiss();
                                                    Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    DatabaseReference reference = databaseReference.child("users").child(Utils.encodeEmail(request.userEmail));

                                                    HashMap<String, Object> dateJoined = new HashMap<>();
                                                    dateJoined.put("dateJoined", ServerValue.TIMESTAMP);

//                                                    reference.child("email").setValue(request.userEmail);
//                                                    reference.child("name").setValue(request.userName);
//                                                    reference.child("hasLoggedInWithPassword").setValue(false);
//                                                    reference.child("dateJoined").setValue(dateJoined);
                                                    User user = new User(request.userEmail, request.userName, dateJoined, false);
                                                    reference.setValue(user);

                                                    Toast.makeText(application.getApplicationContext(), "Please check your email", Toast.LENGTH_SHORT).show();

                                                    request.progressDialog.dismiss();

                                                    if (auth.getCurrentUser() != null)
                                                        auth.signOut();

                                                    Intent intent = new Intent(application.getApplicationContext(), LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    application.startActivity(intent);
                                                }

                                            }
                                        });

                            }

                        }
                    });
        }

        bus.post(response);
    }


    @Subscribe
    public void loginUserRequest(final AccountServices.LoginUserRequest request) {
        final AccountServices.LoginUserResponse response = new AccountServices.LoginUserResponse();

        if (request.userEmail.isEmpty())
            response.setPropertyErrors("userEmail", "Please enter email");

        if (request.password.isEmpty())
            response.setPropertyErrors("password", "Please enter password");

        if (response.didSucceed()) {
            request.progressDialog.show();

            auth.signInWithEmailAndPassword(request.userEmail, request.password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                request.progressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                final DatabaseReference userReference = databaseReference.child("users").child(Utils.encodeEmail(request.userEmail));

                                userReference.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);

                                        if (user != null) {

                                            SharedPreferences sharedPreferences = request.sharedPreferences;
                                            sharedPreferences.edit().putString(Utils.USER_EMAIL, Utils.encodeEmail(user.getUserEmail())).apply();
                                            sharedPreferences.edit().putString(Utils.USERNAME, user.getUserName()).apply();

                                            userReference.child("hasLoggedInWithPassword").setValue(true);

                                            request.progressDialog.dismiss();
                                            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            application.startActivity(intent);

                                        } else {
                                            request.progressDialog.dismiss();
                                            Toast.makeText(application.getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        request.progressDialog.dismiss();
                                        Toast.makeText(application.getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
        }

        bus.post(response);
    }
}
