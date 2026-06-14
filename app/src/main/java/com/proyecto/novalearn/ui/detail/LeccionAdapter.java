package com.proyecto.novalearn.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.novalearn.R;
import com.proyecto.novalearn.data.Leccion;

import java.util.List;

public class LeccionAdapter extends RecyclerView.Adapter<LeccionAdapter.ViewHolder> {

    private final List<Leccion> lista;

    public LeccionAdapter(List<Leccion> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leccion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Leccion leccion = lista.get(position);
        holder.tvNumero.setText(String.valueOf(position + 1));
        holder.tvTitulo.setText(leccion.getTitulo());
        holder.tvContenido.setText(leccion.getContenido());
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvTitulo, tvContenido;

        ViewHolder(View v) {
            super(v);
            tvNumero    = v.findViewById(R.id.tvNumero);
            tvTitulo    = v.findViewById(R.id.tvTitulo);
            tvContenido = v.findViewById(R.id.tvContenido);
        }
    }
}