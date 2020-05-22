package com.calderon.mikimexiapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.calderon.mikimexiapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static com.calderon.mikimexiapp.utils.Util.*;

public class LoginRepartidor extends AppCompatActivity {

    private TextInputEditText lic;
    private TextInputEditText pass;

    private TextInputLayout emalLy;
    private TextInputLayout passLy;

    private TextView textView;

    private Button btnSingIn;

    private FirebaseAuth mAuth;
    private SharedPreferences prefs;
    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_repartidor);

        mAuth = FirebaseAuth.getInstance();

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        sendBind();

        textView.setPaintFlags(textView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        final SharedPreferences sp = getSharedPreferences("url",Context.MODE_PRIVATE);
        db.collection("ruta").document("cliente").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("repartidor",documentSnapshot.getString("url"));
                editor.apply();
            }
        });

        final String finalUrl = sp.getString("repartidor","");
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



        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (validateEditText(emalLy, LoginRepartidor.this) && validateEditText(passLy, LoginRepartidor.this)) {

                    final String licS = lic.getText().toString();
                    final String passS = pass.getText().toString();
                    final String[] email = new String[1];
                    final String[] password = new String[1];
                    doc = bd.document("repartidores/" + licS);
                    doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            email[0] = documentSnapshot.getString("email");

                            password[0] = documentSnapshot.getString("password");
                            if(email[0] == null){
                                Snackbar.make(v, "Revise su Licencia",Snackbar.LENGTH_LONG).show();
                                return;
                            }

                            if(passS.equals(password[0])){
                                signIn(LoginRepartidor.this,VendedoresActivity.class,licS,email[0],passS,REPARTIDORES,prefs);
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

    private void sendBind(){
        lic = findViewById(R.id.edit_lic_repartidor);
        pass = findViewById(R.id.edit_pass_repartidor);

        emalLy = findViewById(R.id.layout_email_repartidor);
        passLy = findViewById(R.id.layout_pass_repartidor);

        btnSingIn = findViewById(R.id.btn_entrar_repartidor);

        textView = findViewById(R.id.signUoRepartidor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginRepartidor.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
