package com.proyecto.novalearn.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.novalearn.data.DBHelper;
import com.proyecto.novalearn.data.Leccion;
import com.proyecto.novalearn.utils.SessionManager;
import com.proyecto.novalearn.R;

import java.util.List;

public class LeccionAdapter extends RecyclerView.Adapter<LeccionAdapter.ViewHolder> {

    public interface OnProgresoListener {
        void onProgresoActualizado();
    }

    private final List<Leccion> lista;
    private final int cursoId;
    private final String usuario;
    private final DBHelper dbHelper;
    private final OnProgresoListener listener;
    private int expandedPosition = -1;

    public LeccionAdapter(List<Leccion> lista, int cursoId, Context context,
                          OnProgresoListener listener) {
        this.lista    = lista;
        this.cursoId  = cursoId;
        this.usuario  = new SessionManager(context).getEmail();
        this.dbHelper = new DBHelper(context);
        this.listener = listener;
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
        boolean leida   = dbHelper.leccionLeida(usuario, leccion.getId());
        boolean expandida = expandedPosition == position;

        // Número con check si ya fue leída
        holder.tvNumero.setText(leida ? "✓" : String.valueOf(position + 1));
        holder.tvNumero.setAlpha(leida ? 0.6f : 1f);
        holder.tvTitulo.setText(leccion.getTitulo());
        holder.tvContenido.setText(leccion.getContenido());
        holder.tvContenido.setVisibility(expandida ? View.VISIBLE : View.GONE);
        holder.tvFlecha.setText(expandida ? "˅" : "›");

        holder.itemView.setOnClickListener(v -> {
            int anterior = expandedPosition;
            expandedPosition = expandida ? -1 : position;

            // Marcar como leída al expandir
            if (!expandida) {
                dbHelper.marcarLeccionLeida(usuario, cursoId, leccion.getId());
                if (listener != null) listener.onProgresoActualizado();
            }

            if (anterior != -1) notifyItemChanged(anterior);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvTitulo, tvContenido, tvFlecha;

        ViewHolder(View v) {
            super(v);
            tvNumero    = v.findViewById(R.id.tvNumero);
            tvTitulo    = v.findViewById(R.id.tvTitulo);
            tvContenido = v.findViewById(R.id.tvContenido);
            tvFlecha    = v.findViewById(R.id.tvFlecha);
        }
    }
}