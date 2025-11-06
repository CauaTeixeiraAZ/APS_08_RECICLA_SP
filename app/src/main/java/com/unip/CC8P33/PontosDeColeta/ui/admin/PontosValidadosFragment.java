package com.unip.CC8P33.PontosDeColeta.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import com.unip.CC8P33.PontosDeColeta.repository.PontoColetaRepository;
import java.util.ArrayList;
import java.util.List;

public class PontosValidadosFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvEmpty;
    private PontosValidadosAdapter adapter;
    private PontoColetaRepository repository;
    private List<PontoColeta> pontosList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pontos_pendentes, container, false);

        repository = new PontoColetaRepository();

        recyclerView = view.findViewById(R.id.recyclerViewPendentes);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // CORRIGIDO: Apenas um listener
        adapter = new PontosValidadosAdapter(pontosList, this::onDeletarClick);
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::carregarPontosValidados);

        carregarPontosValidados();

        return view;
    }

    private void carregarPontosValidados() {
        swipeRefresh.setRefreshing(true);

        repository.getPontosValidados()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    pontosList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        PontoColeta ponto = document.toObject(PontoColeta.class);
                        pontosList.add(ponto);
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);

                    tvEmpty.setVisibility(pontosList.isEmpty() ? View.VISIBLE : View.GONE);
                    if (pontosList.isEmpty()) {
                        tvEmpty.setText("Nenhum ponto validado");
                    }
                })
                .addOnFailureListener(e -> {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(getContext(), "Erro ao carregar: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void onDeletarClick(PontoColeta ponto) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Deletar Ponto")
                .setMessage("Tem certeza que deseja deletar este ponto?")
                .setPositiveButton("Deletar", (dialog, which) -> {
                    repository.deletarPonto(ponto.getId())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Ponto deletado!",
                                        Toast.LENGTH_SHORT).show();
                                carregarPontosValidados();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Erro: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}