/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  Boolean signUpModeActive = true;
  TextView changeSignupModeTextView;
  EditText passwordEditText;
  public void showUserList(){
      Intent intent =new Intent(getApplicationContext(),UserListActivity.class);
      startActivity(intent);
  }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if (i == keyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
      signUp(view);
    }

    return false;

  }

  @Override
  public void onClick(View view) {

    if(view.getId()==R.id.changeSignupModeTextView)
    {Button signupButton =(Button) findViewById(R.id.signupButton);


      if(signUpModeActive==true)
      { signUpModeActive =  false;
      signupButton.setText("LOGIN");
      changeSignupModeTextView.setText("Or, Signup");

      }
      else {
        signUpModeActive =  true;
        signupButton.setText("SIGNUP");
        changeSignupModeTextView.setText("Or, LogIn");
      }


    }else if (view.getId()==R.id.backgroundLinearLayout || view.getId()==R.id.imageView){

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);}
  }

  public void signUp (View view){
         EditText usernameEditText =(EditText) findViewById(R.id.usernameEditText);
         passwordEditText =(EditText) findViewById(R.id.passwordEditText);

         if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) // matches are used to java syntax error toast
         {


           Toast.makeText(this, "A username amd password required", Toast.LENGTH_SHORT).show();
         }else
         {
          if (signUpModeActive){




             final ParseUser user = new ParseUser();
             user.setUsername(usernameEditText.getText().toString());
             user.setPassword(passwordEditText.getText().toString());
             user.signUpInBackground(new SignUpCallback() {
               @Override
               public void done(ParseException e) {
                 if(e==null)
                 {
                   Log.i("Signup","successful");
                   showUserList();

                 }
                 else
                 {
                   Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
               }
             });
       }
         else{

            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {
                if (user!=null){
                  Log.i("singnup", "LoginSuccessfull");
                  showUserList();
                } else {

                  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              }
            });
          }
         }}
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Social Media");


      changeSignupModeTextView= (TextView) findViewById(R.id.changeSignupModeTextView);
      changeSignupModeTextView.setOnClickListener(this);


      LinearLayout backgroundLinearLayout = (LinearLayout) findViewById(R.id.backgroundLinearLayout);
      ImageView logoImageView = (ImageView) findViewById(R.id.imageView);

      backgroundLinearLayout.setOnClickListener(this);
      logoImageView.setOnClickListener(this);




      passwordEditText = (EditText) findViewById(R.id.passwordEditText);
      passwordEditText.setOnKeyListener(this);

      if(ParseUser.getCurrentUser()!=null)
      {
          showUserList();
      }



    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}