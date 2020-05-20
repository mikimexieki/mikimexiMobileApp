package com.calderon.mikimexiapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.calderon.mikimexiapp.adapters.MyAdapterPedidosR;
import com.calderon.mikimexiapp.adapters.MyAdapterPedidosVendedores;
import com.calderon.mikimexiapp.models.PedidoV;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.calderon.mikimexiapp.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.calderon.mikimexiapp.utils.Util.*;

public class RepartidorActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences prefsEnviado;
    private DocumentReference doc;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pedidosRef;

    private String id;
    private String nombre;

    private MyAdapterPedidosR myAdapterPedidosR;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);
        toolbar = findViewById(R.id.toolbar);
        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        prefsEnviado = getSharedPreferences("pedidoEnviado",Context.MODE_PRIVATE);

        id = getIdUser(prefs);
        pedidosRef = db.collection("vendedores").document(id).collection("pedidos");
        nombre = getNombre(prefs);
        toolbar.setTitle("Bienvenido "+nombre);
        setSupportActionBar(toolbar);

        setRecyclerView();


    }

    private void setRecyclerView() {
        pedidosRef = db.collection("vendedores").document(id).collection("pedidos");
        Query query = pedidosRef.orderBy("destinatario", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PedidoV> options = new FirestoreRecyclerOptions.Builder<PedidoV>()
                .setQuery(query, PedidoV.class)
                .build();

        myAdapterPedidosR = new MyAdapterPedidosR(options, new MyAdapterPedidosR.OnItemClickListener() {
            @Override
            public void onItemClick(PedidoV pedidoV, int position) {
                Log.i("##############",nombre);
            }
        }, this);

        RecyclerView recyclerView = findViewById(R.id.rv_pedidossR);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterPedidosR);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.myAdapterPedidosR.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.myAdapterPedidosR.startListening();
    }
}
