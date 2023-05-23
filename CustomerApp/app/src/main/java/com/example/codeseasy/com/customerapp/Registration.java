package com.example.codeseasy.com.customerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextInputEditText editTextIdentifiant, editTextPassword, editTextEmail;
    String identifiant,  password, email;
    TextView textViewError, textViewLogin;
    Button buttonSubmit;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        editTextIdentifiant = findViewById(R.id.identifiant);
        editTextPassword = findViewById(R.id.password);
        editTextEmail = findViewById(R.id.email);
        textViewError = findViewById(R.id.error);
        textViewLogin = findViewById(R.id.loginNow);
        buttonSubmit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.loading);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identifiant = editTextIdentifiant.getText().toString();
                password = editTextPassword.getText().toString();
                email = editTextEmail.getText().toString();

                String url = "https://webapitsbddandroid.azurewebsites.net/api/Account/AddAccount";
                String AddAccount = url+"?identifiant="+identifiant+"&password="+password+"&email="+email;

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AddAccount,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (Integer.parseInt(response )> 0)  {
                                    Toast.makeText(getApplicationContext(), "Compte crée", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("identifiant", identifiant);
                        paramV.put("password", password);
                        paramV.put("email", email);
                        return paramV;
                    }
                };
                queue.add(stringRequest);

            }
        });
    }
}