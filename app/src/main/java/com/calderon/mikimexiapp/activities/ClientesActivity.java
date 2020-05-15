package com.calderon.mikimexiapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.adapters.MyAdapterTiendas;
import com.calderon.mikimexiapp.models.Tienda;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.calderon.mikimexiapp.utils.Util.*;

public class ClientesActivity extends AppCompatActivity {

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private CollectionReference tiendasRef  = db.collection("tiendas");
    private DocumentReference doc;

    private SharedPreferences prefs;

    private MyAdapterTiendas myAdapterTiendas;
    private Toolbar toolbar;

    private String email;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        toolbar = findViewById(R.id.toolbar);

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        setRecyclerView();

        email = getIdUser(prefs);

        doc = db.document("clientes/" + email);
        doc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    Toast.makeText(ClientesActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                else {
                    nombre = documentSnapshot.getString("nombre");
                    toolbar.setTitle("Bienvenido "+ nombre);
                }
            }
        });


        setSupportActionBar(toolbar);
    }

    private void setRecyclerView() {
        Query query = tiendasRef.orderBy("nombre", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Tienda> options = new FirestoreRecyclerOptions.Builder<Tienda>()
                .setQuery(query, Tienda.class)
                .build();

        myAdapterTiendas = new MyAdapterTiendas(options, new MyAdapterTiendas.OnItemClickListener() {
            @Override
            public void onItemClick(Tienda tienda, int position, int action) {
                if(action == CALL){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+tienda.getTelefono()));
                    if(intent.resolveActivity(getPackageManager())!=null)
                        startActivity(intent);
                }
                if(action == SMS){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + tienda.getTelefono()));
                    intent.putExtra("sms_body", "");
                    startActivity(intent);
                }
                if(action == PEDIDO){
                    Map<String, Object> pedido = new HashMap<>();
                    pedido.put("descripción", "2 kl de carne de Res");
                    pedido.put("cliente", nombre);
                    pedido.put("direccion","Av allende No.34");

                    db.collection("vendedores").document(tienda.getId()).collection("pedidos").document(email)
                            .set(pedido)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Testing!!!!", "DocumentSnapshot successfully written!");
                                    Toast.makeText(ClientesActivity.this, "Bien", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ClientesActivity.this, "Error "+e, Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_tiendas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterTiendas);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut(ClientesActivity.this,prefs);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapterTiendas.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // myAdapterTiendas.stopListening();
    }

}
