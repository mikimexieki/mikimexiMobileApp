package com.calderon.mikimexiapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calderon.mikimexiapp.R;
import com.calderon.mikimexiapp.models.Tienda;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import static com.calderon.mikimexiapp.utils.Util.*;

public class MyAdapterTiendas extends FirestoreRecyclerAdapter<Tienda ,MyAdapterTiendas.ViewHolder> {

    private OnItemClickListener listener;

    public MyAdapterTiendas(@NonNull FirestoreRecyclerOptions<Tienda> options, OnItemClickListener listener){
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tienda, parent, false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, final int i, final Tienda tienda) {
        viewHolder.nombre.setText(tienda.getNombre());
        viewHolder.dom.setText(tienda.getDireccion());
        viewHolder.horario.setText(tienda.getHorario());
        viewHolder.tel.setText(tienda.getTelefono());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(tienda,i,PEDIDO);
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(tienda,i,CALL);
            }
        });
        viewHolder.smsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(tienda,i,SMS);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre;
        TextView dom;
        TextView horario;
        TextView tel;
        ImageView imageView;
        ImageView smsImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.tienda_nombre);
            dom = itemView.findViewById(R.id.tienda_dom);
            horario = itemView.findViewById(R.id.tienda_horario);
            tel = itemView.findViewById(R.id.tienda_tel);
            imageView = itemView.findViewById(R.id.phonePick);
            smsImageView = itemView.findViewById(R.id.smsPick);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Tienda tienda,int position,int action);
    }
}
