package com.calderon.mikimexiapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.models.PedidoR;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import static com.calderon.mikimexiapp.utils.Util.*;

public class MyAdapterPedidosR extends FirestoreRecyclerAdapter<PedidoR, MyAdapterPedidosR.ViewHolder> {

    private OnItemClickListener listener;
    private int tipo;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public MyAdapterPedidosR(@NonNull FirestoreRecyclerOptions<PedidoR> options, OnItemClickListener listener ,int tipo) {
        super(options);
        this.listener = listener;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_repas, parent, false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i, @NonNull final PedidoR pedidoR) {
        viewHolder.descripcion.setText(pedidoR.getDescripcion());
        viewHolder.direccionTienda.setText(pedidoR.getDireccionTienda());
        viewHolder.direccionEntrega.setText(pedidoR.getDireccionEntrega());
        viewHolder.cliente.setText(pedidoR.getDestinatario());
        viewHolder.precio.setText("$"+pedidoR.getPrecio());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(pedidoR,i);
            }
        });

        if(tipo==1)
            viewHolder.layout.setVisibility(View.VISIBLE);
        else
            viewHolder.layout.setVisibility(View.GONE);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView descripcion;
        TextView direccionTienda;
        TextView direccionEntrega;
        TextView cliente;
        TextView precio;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.descripcionR);
            direccionEntrega = itemView.findViewById(R.id.direccionEntrega);
            direccionTienda = itemView.findViewById(R.id.direccionTienda);
            cliente = itemView.findViewById(R.id.cliente);
            precio = itemView.findViewById(R.id.precioRepa);
            layout = itemView.findViewById(R.id.layoutAsignado);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(PedidoR pedidoR, int position);
    }
}

