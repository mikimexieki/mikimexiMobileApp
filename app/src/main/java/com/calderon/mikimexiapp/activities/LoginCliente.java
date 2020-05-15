package com.calderon.mikimexiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.calderon.mikimexiapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.calderon.mikimexiapp.utils.Util.*;

public class LoginCliente extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputEditText pass;

    private TextInputLayout emalLy;
    private TextInputLayout passLy;

    private TextView textView;

    private Button btnSingIn;

    private FirebaseAuth mAuth;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente);

        mAuth = FirebaseAuth.getInstance();

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        sendBind();

        textView.setPaintFlags(textView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://www.google.com.mx");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditText(emalLy, LoginCliente.this) && validateEditText(passLy, LoginCliente.this)) {
                    String emailS = email.getText().toString();
                    String passS = pass.getText().toString();
                    signIn(LoginCliente.this,ClientesActivity.class,emailS,emailS,passS,CLIENTES,prefs);
                }
            }
        });
    }

    private void sendBind(){
        email = findViewById(R.id.edit_email_clientes);
        pass = findViewById(R.id.edit_pass_clientes);

        emalLy = findViewById(R.id.layout_email_clientes);
        passLy = findViewById(R.id.layout_pass_clientes);

        btnSingIn = findViewById(R.id.btn_entrar_clientes);

        textView = findViewById(R.id.signUoClientes);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginCliente.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
