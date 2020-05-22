package com.calderon.mikimexiapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.adapters.MyAdapterPedidosVendedores;
import com.calderon.mikimexiapp.models.PedidoV;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import static com.calderon.mikimexiapp.utils.Util.*;

public class VendedoresActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences prefsEnviado;
    private SharedPreferences dirSP;
    private DocumentReference doc;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pedidosRef;
    private CollectionReference contadorRef;

    private String id;
    private String nombreTienda;

    private MyAdapterPedidosVendedores myAdapterPedidosVendedores;
    private Toolbar toolbar;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedores);
        toolbar = findViewById(R.id.toolbar);
        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        prefsEnviado = getSharedPreferences("pedidoEnviado",Context.MODE_PRIVATE);
        dirSP = getSharedPreferences("direccion",Context.MODE_PRIVATE);

        id = getIdUser(prefs);
        pedidosRef = db.collection("vendedores").document(id).collection("pedidos");
        nombreTienda = getNombre(prefs);
        toolbar.setTitle("Bienvenido "+nombreTienda);
        setSupportActionBar(toolbar);

        setRecyclerView();

    }

    private void setRecyclerView() {
        pedidosRef = db.collection("vendedores").document(id).collection("pedidos");
        Query query = pedidosRef.orderBy("destinatario", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PedidoV> options = new FirestoreRecyclerOptions.Builder<PedidoV>()
                .setQuery(query, PedidoV.class)
                .build();

        myAdapterPedidosVendedores = new MyAdapterPedidosVendedores(options, new MyAdapterPedidosVendedores.OnItemClickListener() {
            @Override
            public void onItemClick(PedidoV pedidoV, int position) {

                if(!pedidoV.isEnviando())
                    showPialogPedidoV(pedidoV).show();
                Log.i("$$$$$$$$$$$$$$$$$$$$$",pedidoV.toString());
            }
        },this);

        RecyclerView recyclerView = findViewById(R.id.rv_pedidosV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterPedidosVendedores);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut(VendedoresActivity.this, prefs);
                return true;
            case R.id.sync:
                onResume();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.myAdapterPedidosVendedores.startListening();
    }

    private AlertDialog showPialogPedidoV(final PedidoV pedidoV){
        builder = new AlertDialog.Builder(VendedoresActivity.this);
        builder.setTitle("Enviar Pedido");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pedido_vendedores, null);

        final EditText prec = dialogView.findViewById(R.id.dialog_precio);
        Button confirm = dialogView.findViewById(R.id.dialog_confirmV);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String precio = prec.getText().toString()+"";
                if(!precio.isEmpty()) {
                    pedidoV(pedidoV, precio);
                    dialog.dismiss();
                }
                else
                    Toast.makeText(VendedoresActivity.this, "Ingrese los campos solicitados", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setCancelable(true);
        builder.setCancelable(true);
        return dialog;
    }

    private void pedidoV(PedidoV pedidoV, String precio){

        Map<String, Object> pedido = new HashMap<>();
        pedido.put("id",id);
        pedido.put("descripcion", pedidoV.getDescripcion());
        pedido.put("precio",precio );
        pedido.put("direccion", pedidoV.getDireccion());
        pedido.put("email",pedidoV.getId());

        doc = db.collection("clientes").document(pedidoV.getId()).collection("pedidos").document(id);
        doc.set(pedido)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Testing!!!!", "DocumentSnapshot successfully written!");
                        Toast.makeText(VendedoresActivity.this, "Pedido Enviado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VendedoresActivity.this, "Error "+e, Toast.LENGTH_SHORT).show();
                    }
                });

        final String[] dirrecion = new String[1];

        doc =  db.document("tiendas/"+id);
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                dirrecion[0] = documentSnapshot.getString("direccion");
                SharedPreferences.Editor editor = dirSP.edit();
                editor.putString("direccion",dirrecion[0]);
                editor.apply();

            }
        });
        Log.i("·················",dirSP.getString("direccion",""));

        String adds = dirSP.getString("direccion","");

        Map<String, Object> pedidoR= new HashMap<>();
        pedidoR.put("idV", id);
        pedidoR.put("idC", pedidoV.getId());
        pedidoR.put("destinatario",pedidoV.getDestinatario());
        pedidoR.put("descripcion", pedidoV.getDescripcion());
        pedidoR.put("precio",precio );
        pedidoR.put("direccionTienda",adds);
        pedidoR.put("direccionEntrega",pedidoV.getDireccion());


        db.collection("pedidos").document().set(pedidoR)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VendedoresActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.i("XXXXXXXXXXXXX", e.toString());
                    }
                });

        SharedPreferences.Editor ed = dirSP.edit();

        pedidoV.setEnviando(true);
        pedidoV.setPrecio(precio);
        SharedPreferences.Editor editor = prefsEnviado.edit();
        editor.putBoolean(pedidoV.getId(),true);
        editor.putString("precio",precio);
        editor.apply();
        myAdapterPedidosVendedores.notifyDataSetChanged();
    }
}
