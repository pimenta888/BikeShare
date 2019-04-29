package com.example.bikeshare.Users;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bikeshare.BikeShareActivity;
import com.example.bikeshare.R;
import com.example.bikeshare.RidesDB;

import java.util.List;

public class SignUp_In extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private TextView mRegister;
    private boolean userFound;
    private boolean userOnline;

    private static RidesDB sRidesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__in);

        sRidesDB = RidesDB.get(this);
        for (User user: sRidesDB.getUsers()) {
            if(user.isStatus() == true){
                userOnline = true;
                break;
            }
        }

        if(!userOnline){
            mEmail = (EditText) findViewById(R.id.emailUser);
            mPassword = (EditText) findViewById(R.id.passwordUser);
            mLogin = (Button) findViewById(R.id.loginUser);
            mRegister = (TextView) findViewById(R.id.registerUser);

            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mEmail.getText().toString().length() > 0 && mPassword.getText().toString().length() > 0){
                        List<User> users = sRidesDB.getUsers();
                        userFound = false;
                        for (User user : users) {
                            if(user.getEmail().equals(mEmail.getText().toString()) && user.getPassword().equals(mPassword.getText().toString())){
                                userFound = true;
                                user.setStatus(true);
                                Log.d("ola", user.isStatus() + "");
                                sRidesDB.updateUser(user);
                                Intent intent = BikeShareActivity.newIntent(getApplicationContext());
                                startActivity(intent);
                                break;
                            }
                        }
                        if (!userFound){
                            Toast.makeText(getApplicationContext(),"User not register, create a account first!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Mandatory to fill Email and Password fields!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SignUp.newIntent(getApplicationContext());
                    startActivity(intent);
                }
            });
        } else {
            Intent intent = BikeShareActivity.newIntent(getApplicationContext());
            startActivity(intent);
        }
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignUp_In.class);
        return intent;
    }
}
