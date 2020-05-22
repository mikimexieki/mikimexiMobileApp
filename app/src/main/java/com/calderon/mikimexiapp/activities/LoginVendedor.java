package com.calderon.mikimexiapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.calderon.mikimexiapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static com.calderon.mikimexiapp.utils.Util.*;

public class LoginVendedor extends AppCompatActivity {

    private TextInputEditText rfc;
    private TextInputEditText pass;

    private TextInputLayout rfcLy;
    private TextInputLayout passLy;

    private Button btnSingIn;

    private TextView textView;

    private FirebaseAuth mAuth;
    private DocumentReference doc;
    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_vendedor);

        mAuth = FirebaseAuth.getInstance();

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);


        rfc = findViewById(R.id.edit_rfc_vendedor);
        pass = findViewById(R.id.edit_pass_vendedor);

        rfcLy = findViewById(R.id.layout_rfc_vendedor);
        passLy = findViewById(R.id.layout_pass_vendedor);

        textView = findViewById(R.id.signUoVendedor);

        final SharedPreferences sp = getSharedPreferences("url",Context.MODE_PRIVATE);
        db.collection("ruta").document("cliente").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("vendedor",documentSnapshot.getString("url"));
                editor.apply();
            }
        });

        final String finalUrl = sp.getString("vendedor","");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse(finalUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        btnSingIn = findViewById(R.id.btn_entrar_vendedor);
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (validateEditText(rfcLy, LoginVendedor.this) && validateEditText(passLy, LoginVendedor.this)) {
                    final String rfcS = rfc.getText().toString();
                    final String passS = pass.getText().toString();
                    final String[] email = new String[1];
                    final String[] password = new String[1];
                    doc = bd.document("vendedores/" + rfcS);
                    doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            email[0] = documentSnapshot.getString("email");

                            password[0] = documentSnapshot.getString("password");
                            if(email[0] == null){
                                Snackbar.make(v, "Revise su RFC",Snackbar.LENGTH_LONG).show();
                                return;
                            }

                            if(passS.equals(password[0])){
                                signIn(LoginVendedor.this,VendedoresActivity.class,rfcS,email[0],passS,VENDEDORES,prefs);
                            }else{
                                Snackbar.make(v, "Contraseña incorrecta",Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                    System.out.println(email[0]);

                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginVendedor.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
