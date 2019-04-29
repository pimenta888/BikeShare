package com.example.bikeshare.Users;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bikeshare.R;
import com.example.bikeshare.RidesDB;

public class UserSettings extends AppCompatActivity {

    private TextView userText;
    private EditText currentBalance;
    private EditText addMoney;
    private Button addMoneyButton;
    private Button logOut;
    private User mUser;

    private static RidesDB sRidesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUser = userSession();

        userText = (TextView) findViewById(R.id.userText);
        userText.setText("User " + mUser.getEmail() + " Settings");
        currentBalance = (EditText) findViewById(R.id.currentMoney);
        currentBalance.setKeyListener(null);
        addMoney = (EditText) findViewById(R.id.addMoney);
        addMoneyButton = (Button) findViewById(R.id.addMoneyButton);
        logOut = (Button) findViewById(R.id.logoutButton);
        updateUI();

        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try{
                        Double addToBalance = Double.parseDouble(addMoney.getText().toString());
                        Double newBalance = addToBalance + mUser.getMoney();
                        mUser.setMoney(newBalance);
                        sRidesDB.updateUser(mUser);
                        updateUI();
                    }catch (Exception e){

                    }
                }
            }
        );

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setStatus(false);
                sRidesDB.updateUser(mUser);
                Intent intent = SignUp_In.newIntent(getApplicationContext());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public User userSession(){
        sRidesDB = RidesDB.get(this);
        for (User userOnline: sRidesDB.getUsers()) {
            if(userOnline.isStatus() == true){
                return userOnline;
            }
        }
        return null;
    }

    public void updateUI(){

        currentBalance.setText(userSession().moneyString() + "€");
        addMoney.setText("0.0");

    }


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, UserSettings.class);
        return intent;
    }
}
