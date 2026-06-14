package com.proyecto.novalearn.ui.mycourses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.novalearn.R;
import com.proyecto.novalearn.data.Curso;
import com.proyecto.novalearn.ui.detail.CourseDetailActivity;

import java.util.List;

public class MisCursosAdapter extends RecyclerView.Adapter<MisCursosAdapter.ViewHolder> {

    private final List<Curso> lista;

    public MisCursosAdapter(List<Curso> lista) {
        this.lista = lista;
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
        Curso curso = lista.get(position);
        holder.tvNombre.setText("✓ " + curso.getNombre());
        holder.tvInstructor.setText(curso.getInstructor());
        holder.tvDuracion.setText(curso.getDuracion());

        Context ctx = holder.itemView.getContext();
        int resId = ctx.getResources().getIdentifier(
                curso.getIcono(), "drawable", ctx.getPackageName());
        if (resId != 0) {
            holder.ivIcono.setImageResource(resId);
        } else {
            holder.ivIcono.setImageResource(R.drawable.ic_android);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, CourseDetailActivity.class);
            intent.putExtra("curso_id", curso.getId());
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvInstructor, tvDuracion;
        ImageView ivIcono;

        ViewHolder(View v) {
            super(v);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvInstructor = v.findViewById(R.id.tvInstructor);
            tvDuracion = v.findViewById(R.id.tvDuracion);
            ivIcono = v.findViewById(R.id.ivIcono);
        }
    }
}