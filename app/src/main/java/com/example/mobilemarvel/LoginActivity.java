package com.example.mobilemarvel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail1,etPassword1;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar1;
    TextView textView1;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail1 = findViewById(R.id.etEmail1);
        etPassword1 = findViewById(R.id.etPassword1);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar1 = findViewById(R.id.progressBar1);
        textView1 = findViewById(R.id.registerNow);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar1.setVisibility(View.VISIBLE);
                String email,password;
                email = String.valueOf(etEmail1.getText());
                password = String.valueOf(etPassword1.getText());

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar1.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                // Store all value

                if(!validateEmail() | !validatePassword())
                {

                }
                else
                    checkUser();
            }
        });
    }

    public boolean validateEmail()
    {
        String val = etEmail1.getText().toString();
        if(val.isEmpty())
        {
            etEmail1.setError("Email cannot be Empty");
            return false;

        }
        else
        {
            etEmail1.setError(null);
            return true;
        }
    }

    public boolean validatePassword()
    {
        String val = etPassword1.getText().toString();
        if(val.isEmpty())
        {
            etPassword1.setError("Password cannot be Empty");
            return false;

        }
        else
        {
            etPassword1.setError(null);
            return true;
        }
    }

    public void checkUser()
    {
        String userEmail = etEmail1.getText().toString().trim();
        String userPassword = etPassword1.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("Email").equalTo(userEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    etEmail1.setError(null);
                    String PasswordFromDB = snapshot.child(userEmail).child("Password").getValue(String.class);

                    if(!Objects.equals(PasswordFromDB,userPassword))
                    {
                        etEmail1.setError(null);
                        Intent intent=  new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        etPassword1.setError("Invalid Credentials");
                        etPassword1.requestFocus();
                    }
                }
                else
                {
                    etEmail1.setError("User does not exist");
                    etEmail1.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}