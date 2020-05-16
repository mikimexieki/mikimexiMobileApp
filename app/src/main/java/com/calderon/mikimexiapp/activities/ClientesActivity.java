package com.calderon.mikimexiapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.adapters.MyAdapterPedidosClientes;
import com.calderon.mikimexiapp.adapters.MyAdapterTiendas;
import com.calderon.mikimexiapp.models.PedidoC;
import com.calderon.mikimexiapp.models.Tienda;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import static com.calderon.mikimexiapp.utils.Util.*;
public class ClientesActivity extends AppCompatActivity {

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private CollectionReference tiendasRef  = db.collection("tiendas");
    private CollectionReference pedidosRef;
    private DocumentReference doc;

    private SharedPreferences prefs;

    private MyAdapterTiendas myAdapterTiendas;
    private MyAdapterPedidosClientes myAdapterPedidosClientes;
    private Toolbar toolbar;

    private String email;
    private String nombre;

    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        toolbar = findViewById(R.id.toolbar);

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        email = getIdUser(prefs);
        setRecyclerView();

        nombre = getNombre(prefs);

        toolbar.setTitle("Bienvenido "+nombre);
        setSupportActionBar(toolbar);

        setRecyclerViewPedidos();
    }

    private void setRecyclerViewPedidos() {
        pedidosRef = db.collection("clientes").document(email).collection("pedidos");
        Query query = pedidosRef.orderBy("id", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PedidoC> options = new FirestoreRecyclerOptions.Builder<PedidoC>()
                .setQuery(query, PedidoC.class)
                .build();

        myAdapterPedidosClientes = new MyAdapterPedidosClientes(options,null);
        RecyclerView recyclerView = findViewById(R.id.rv_pedidos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterPedidosClientes);
    }

    private void setRecyclerView() {
        tiendasRef  = db.collection("tiendas");
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
                    showPialogPedidoC(tienda).show();
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
        myAdapterPedidosClientes.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // myAdapterTiendas.stopListening();
    }

    private AlertDialog showPialogPedidoC(final Tienda tienda){
        builder = new AlertDialog.Builder(ClientesActivity.this);
        builder.setTitle("Crear Pedido");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pedido_clientes, null);

        final EditText desc = dialogView.findViewById(R.id.dialog_descripcion);
        final EditText dom = dialogView.findViewById(R.id.dialog_domicilio);
        Button confirm = dialogView.findViewById(R.id.dialog_confirmC);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = desc.getText().toString() + "";
                String domicilio = dom.getText().toString() + "";
                if (!descripcion.isEmpty() && !domicilio.isEmpty()){
                    pedidoC(tienda, descripcion, domicilio);
                    dialog.dismiss();
                }
                else
                    Toast.makeText(ClientesActivity.this, "Ingrese los campos solicitados", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setCancelable(true);
        dialog.setCancelable(true);
        return dialog;
    }

    private void pedidoC(Tienda tienda, String decripcion, String domicilio){
        Map<String, Object> pedido = new HashMap<>();
        pedido.put("id",email);
        pedido.put("descripcion", decripcion);
        pedido.put("destinatario", nombre);
        pedido.put("direccion",domicilio);

        Log.i("#######################", nombre);

        db.collection("vendedores").document(tienda.getId()).collection("pedidos").document(email)
                .set(pedido)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Testing!!!!", "DocumentSnapshot successfully written!");
                        Toast.makeText(ClientesActivity.this, "Pedido Solicitado", Toast.LENGTH_SHORT).show();
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
