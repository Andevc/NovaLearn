package com.proyecto.novalearn.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.novalearn.R;
import com.proyecto.novalearn.data.Curso;

import java.util.ArrayList;
import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.ViewHolder> {

    public interface OnCursoClick { void onClick(Curso curso); }

    private List<Curso> listaOriginal;
    private List<Curso> listaFiltrada;
    private final OnCursoClick listener;

    public CursoAdapter(List<Curso> lista, OnCursoClick listener) {
        this.listaOriginal = lista;
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Curso curso = listaFiltrada.get(position);
        holder.tvNombre.setText(curso.getNombre());
        holder.tvInstructor.setText(curso.getInstructor());
        holder.tvDuracion.setText(curso.getDuracion());
        holder.tvCategoria.setText(curso.getCategoria());
        holder.itemView.setOnClickListener(v -> listener.onClick(curso));
    }

    @Override
    public int getItemCount() { return listaFiltrada.size(); }

    public void filtrar(String texto) {
        listaFiltrada.clear();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            String lower = texto.toLowerCase();
            for (Curso c : listaOriginal) {
                if (c.getNombre().toLowerCase().contains(lower) ||
                        c.getCategoria().toLowerCase().contains(lower)) {
                    listaFiltrada.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvInstructor, tvDuracion, tvCategoria;

        ViewHolder(View v) {
            super(v);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvInstructor = v.findViewById(R.id.tvInstructor);
            tvDuracion = v.findViewById(R.id.tvDuracion);
            tvCategoria = v.findViewById(R.id.tvCategoria);
        }
    }
}