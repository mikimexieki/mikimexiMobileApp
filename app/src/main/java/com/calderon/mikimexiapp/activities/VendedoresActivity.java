package com.calderon.mikimexiapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.adapters.MyAdapterPedidosVendedores;
import com.calderon.mikimexiapp.models.PedidoV;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.calderon.mikimexiapp.utils.Util.getIdUser;
import static com.calderon.mikimexiapp.utils.Util.signOut;

public class VendedoresActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DocumentReference doc;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pedidosRef;

    private String id;
    private String nombreTienda;

    private List<String> list = new ArrayList<>();
    private MyAdapterPedidosVendedores myAdapterPedidosVendedores;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedores);
        toolbar = findViewById(R.id.toolbar);
        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        id = getIdUser(prefs);
        enviarNombre();
        setSupportActionBar(toolbar);

        pedidosRef = db.collection("vendedores").document(id).collection("pedidos");
        /*pedidosRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.getId());
                            }
                        } else {
                           Log.d("Testing!!!!", "Error getting documents: ", task.getException());
                        }
                    }
                });*/

        setRecyclerView();
    }

    private void setRecyclerView() {
        Query query = pedidosRef.orderBy("destinatario", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<PedidoV> options = new FirestoreRecyclerOptions.Builder<PedidoV>()
                .setQuery(query, PedidoV.class)
                .build();


        myAdapterPedidosVendedores = new MyAdapterPedidosVendedores(options, new MyAdapterPedidosVendedores.OnItemClickListener() {
            @Override
            public void onItemClick(PedidoV pedidoV, int position) {
                Toast.makeText(VendedoresActivity.this, pedidoV.getDestinatario(), Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rv_pedidosV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterPedidosVendedores);

    }

    private void enviarNombre(){
        doc = db.document("vendedores/"+id);
        doc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    Toast.makeText(VendedoresActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                else {
                    nombreTienda = documentSnapshot.getString("nombre");
                    toolbar.setTitle(nombreTienda);
                }
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut(VendedoresActivity.this, prefs);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
