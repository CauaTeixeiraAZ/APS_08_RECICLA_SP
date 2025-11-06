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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import com.unip.CC8P33.PontosDeColeta.repository.PontoColetaRepository;
import java.util.ArrayList;
import java.util.List;

public class PontosPendentesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvEmpty;
    private PontosPendentesAdapter adapter;
    private PontoColetaRepository repository;
    private List pontosList = new ArrayList<>();

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
        adapter = new PontosPendentesAdapter(pontosList, this::onValidarClick, this::onRejeitarClick);
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::carregarPontosPendentes);

        carregarPontosPendentes();

        return view;
    }

    private void carregarPontosPendentes() {
        swipeRefresh.setRefreshing(true);

        repository.getPontosPendentes()
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(e -> {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(getContext(), "Erro ao carregar: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void onValidarClick(PontoColeta ponto) {
        String adminUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        repository.validarPonto(ponto.getId(), adminUid)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Ponto validado!", Toast.LENGTH_SHORT).show();
                    carregarPontosPendentes();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Erro: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void onRejeitarClick(PontoColeta ponto) {
        repository.rejeitarPonto(ponto.getId())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Ponto rejeitado!", Toast.LENGTH_SHORT).show();
                    carregarPontosPendentes();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Erro: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        pontosList.clear();
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            PontoColeta ponto = document.toObject(PontoColeta.class);
            pontosList.add(ponto);
        }
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);

        tvEmpty.setVisibility(pontosList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}