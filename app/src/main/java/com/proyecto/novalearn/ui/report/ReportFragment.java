package com.proyecto.novalearn.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.proyecto.novalearn.R;
import com.proyecto.novalearn.data.DBHelper;
import com.proyecto.novalearn.utils.SessionManager;

public class ReportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DBHelper dbHelper = new DBHelper(requireContext());
        SessionManager sessionManager = new SessionManager(requireContext());
        String email = sessionManager.getEmail();

        int total = dbHelper.contarCursos();
        int inscritos = dbHelper.contarInscritos(email);
        int porcentaje = total > 0 ? (inscritos * 100) / total : 0;

        ((TextView) view.findViewById(R.id.tvTotalCursos)).setText(String.valueOf(total));
        ((TextView) view.findViewById(R.id.tvInscritos)).setText(String.valueOf(inscritos));
        ((TextView) view.findViewById(R.id.tvPorcentaje)).setText(porcentaje + "%");
        ((ProgressBar) view.findViewById(R.id.progressBar)).setProgress(porcentaje);
    }
}