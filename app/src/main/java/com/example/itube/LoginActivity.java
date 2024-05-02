package com.example.itube;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonSignUp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(view -> loginUser());
        buttonSignUp.setOnClickListener(view -> signUpUser());
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkUserCredentials(username, password)) {
            SharedPreferences.Editor editor = getSharedPreferences("AppPrefs", MODE_PRIVATE).edit();
            editor.putBoolean("LoggedIn", true);
            editor.apply();

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUpUser() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
}
