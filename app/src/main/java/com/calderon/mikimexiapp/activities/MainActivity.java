package com.calderon.mikimexiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.calderon.mikimexiapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnClientes;
    private Button btnVendedores;
    private Button btnRepas;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        btnClientes = findViewById(R.id.btnClientes);
        btnClientes.setOnClickListener(this);

        btnVendedores = findViewById(R.id.btnVendedores);
        btnVendedores.setOnClickListener(this);

        btnRepas = findViewById(R.id.btnRepartidores);
        btnRepas.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnClientes:
                intent = new Intent(MainActivity.this, LoginCliente.class);
                startActivity(intent);
                break;
            case R.id.btnVendedores:
                intent = new Intent(MainActivity.this, LoginVendedor.class);
                startActivity(intent);
                break;
            case R.id.btnRepartidores:
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("TEST2","test2");
                editor.apply();
                break;
            default:
                Toast.makeText(this, "Seleccione una Opción", Toast.LENGTH_SHORT).show();
        }
    }
}