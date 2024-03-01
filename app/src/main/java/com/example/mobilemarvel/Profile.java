package com.example.mobilemarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    TextView txtname,txtnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtname = findViewById(R.id.txtname);
        txtnumber = findViewById(R.id.txtnumber);
    }

    public void showUserData()
    {
        Intent intent = getIntent();

        String nameUser = intent.getStringExtra("name");
        String numberUser = intent.getStringExtra("number");

        txtname.setText(nameUser);
        txtnumber.setText(numberUser);
    }
}