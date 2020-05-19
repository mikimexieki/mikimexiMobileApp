package com.calderon.mikimexiapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.models.PedidoV;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyAdapterPedidosVendedores extends FirestoreRecyclerAdapter<PedidoV, MyAdapterPedidosVendedores.ViewHolder> {

    private OnItemClickListener listener;

    private SharedPreferences sp;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapterPedidosVendedores(@NonNull FirestoreRecyclerOptions<PedidoV> options, OnItemClickListener listener, Activity activity) {
        super(options);
        this.listener = listener;
        sp = activity.getSharedPreferences("pedidoEnviado", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pedido_vendedores, parent, false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i, @NonNull final PedidoV pedidoV) {
        viewHolder.destinatario.setText(pedidoV.getDestinatario());
        viewHolder.direccion.setText(pedidoV.getDireccion());
        viewHolder.descripcion.setText(pedidoV.getDescripcion());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(pedidoV,i);
            }
        });
        pedidoV.setEnviando(sp.getBoolean(pedidoV.getId(),false));
        if(pedidoV.isEnviando()){
            viewHolder.icon.setImageResource(R.drawable.ic_local_shipping_black_24dp);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView descripcion;
        TextView direccion;
        TextView destinatario;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            descripcion = itemView.findViewById(R.id.pedidoDescripcion);
            direccion = itemView.findViewById(R.id.pedidoDireccion);
            destinatario = itemView.findViewById(R.id.pedidoID);
            icon = itemView.findViewById(R.id.sending_icon);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(PedidoV pedidoV, int position);
    }
}

