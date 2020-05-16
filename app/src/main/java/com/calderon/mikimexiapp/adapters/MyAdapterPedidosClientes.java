package com.calderon.mikimexiapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.models.PedidoC;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyAdapterPedidosClientes extends FirestoreRecyclerAdapter<PedidoC, MyAdapterPedidosClientes.ViewHolder> {

    private OnItemClickListener listener;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapterPedidosClientes(@NonNull FirestoreRecyclerOptions<PedidoC> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pedido_clientes, parent, false);
        return new ViewHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull PedidoC pedidoC) {
        viewHolder.descripcion.setText(pedidoC.getDescripcion());
        viewHolder.precio.setText("$"+pedidoC.getPrecio());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView descripcion;
        TextView precio;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            descripcion = itemView.findViewById(R.id.pedidoDescripcionCliente);
            precio = itemView.findViewById(R.id.pedidoPrecioCliente);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(PedidoC pedidoC, int position);
    }
}
