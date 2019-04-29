package com.example.bikeshare.Users;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikeshare.R;
import com.example.bikeshare.RidesDB;

public class SignUp extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignUp;

    private static RidesDB sRidesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sRidesDB = RidesDB.get(this);

        mEmail = (EditText) findViewById(R.id.emailRegister);
        mPassword = (EditText) findViewById(R.id.passwordRegister);
        mSignUp = (Button) findViewById(R.id.registerRegister);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmail.getText().toString().length() > 0 && mPassword.getText().toString().length() > 0){
                    User user = new User(mEmail.getText().toString(),mPassword.getText().toString());
                    sRidesDB.addUsers(user);
                    Intent intent = SignUp_In.newIntent(getApplicationContext());
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"New account created - " + mEmail.getText().toString(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Mandatory to fill Email and Password fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignUp.class);
        return intent;
    }
}
