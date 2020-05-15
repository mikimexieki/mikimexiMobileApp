package com.calderon.mikimexiapp.activities;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import static com.calderon.mikimexiapp.utils.Util.*;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, MainActivity.class));
        }else{
           switch (getTypeUser(prefs)){
               case CLIENTES:
                   startActivity(new Intent(this, ClientesActivity.class));
                   break;
               case VENDEDORES:
                   startActivity(new Intent(this, VendedoresActivity.class));
                   break;
               default:
                   startActivity(new Intent(this, MainActivity.class));

           }
        }
        finish();
    }
}
