package com.calderon.mikimexiapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.calderon.mikimexiapp.adapters.MyAdapterPedidosR;
import com.calderon.mikimexiapp.adapters.MyAdapterPedidosVendedores;
import com.calderon.mikimexiapp.models.PedidoR;
import com.calderon.mikimexiapp.models.PedidoV;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.calderon.mikimexiapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private MyAdapterPedidosR myAdapterPedidosR_A;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);
        toolbar = findViewById(R.id.toolbar);
        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        prefsEnviado = getSharedPreferences("pedidoEnviado",Context.MODE_PRIVATE);

        id = getIdUser(prefs);
        nombre = getNombre(prefs);
        toolbar.setTitle("Bienvenido "+nombre);
        setSupportActionBar(toolbar);

        setRecyclerView();
        setRecyclerViewA();


    }

    private void setRecyclerView() {
        pedidosRef = db.collection("pedidos");
        Query query = pedidosRef.orderBy("destinatario", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PedidoR> options = new FirestoreRecyclerOptions.Builder<PedidoR>()
                .setQuery(query, PedidoR.class)
                .build();

        myAdapterPedidosR = new MyAdapterPedidosR(options, new MyAdapterPedidosR.OnItemClickListener() {
            @Override
            public void onItemClick(PedidoR pedidoR, int position) {
                Log.i("#########3333333",pedidoR.toString());
                showConfirmdialog(pedidoR,position);
            }
        }, 0);

        RecyclerView recyclerView = findViewById(R.id.rv_pedidossR);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterPedidosR);

    }

    private void setRecyclerViewA() {
        pedidosRef = db.collection("repartidores").document(id).collection("pedidos");
        Query query = pedidosRef.orderBy("destinatario", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PedidoR> options = new FirestoreRecyclerOptions.Builder<PedidoR>()
                .setQuery(query, PedidoR.class)
                .build();

        myAdapterPedidosR_A = new MyAdapterPedidosR(options, new MyAdapterPedidosR.OnItemClickListener() {
            @Override
            public void onItemClick(PedidoR pedidoR, int position) {
                Log.i("#########3333333",pedidoR.toString());
                showConfirmdialogDelete(pedidoR,position);
            }
        }, 1);

        RecyclerView recyclerView = findViewById(R.id.rv_repasAsignados);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterPedidosR_A);

    }

    private void asignarPedido(PedidoR pedidoR) {
        db.collection("repartidores")
                .document(id)
                .collection("pedidos")
                .document(pedidoR.getIdC()).set(pedidoR);
    }

    private void showConfirmdialog(final PedidoR pedidoR, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Asignar pedido de "+pedidoR.getDestinatario());
        builder.setMessage("¿Desea asignarse el pedido?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                asignarPedido(pedidoR);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void showConfirmdialogDelete(final PedidoR pedidoR, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confimar entrega de pedido de "+pedidoR.getDestinatario());
        builder.setMessage("¿Ya entregó el pedido?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarrPedido(pedidoR);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void eliminarrPedido(PedidoR pedidoR) {
        db.collection("repartidores")
                .document(id)
                .collection("pedidos")
                .document(pedidoR.getIdC())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("++++++++++++++++++++", "DocumentSnapshot successfully deleted!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("-----------------------", "Error deleting document", e);
                    }
                });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut(RepartidorActivity.this,prefs);
                return true;
            case R.id.sync:
                //onRestart();
                onStop();
                onRestart();
                //myAdapterPedidosClientes.startListening();
                Toast.makeText(this, "Dashboard actualizado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.myAdapterPedidosR.startListening();
        this.myAdapterPedidosR_A.startListening();
    }
}
