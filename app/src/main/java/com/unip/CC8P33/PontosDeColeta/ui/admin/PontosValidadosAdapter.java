package com.unip.CC8P33.PontosDeColeta.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import java.util.List;

public class PontosValidadosAdapter extends RecyclerView.Adapter<PontosValidadosAdapter.ViewHolder> {

    private List<PontoColeta> pontos;
    private OnDeletarClickListener listener;

    public interface OnDeletarClickListener {
        void onDeletarClick(PontoColeta ponto);
    }

    // Construtor simples - apenas um listener
    public PontosValidadosAdapter(List<PontoColeta> pontos, OnDeletarClickListener listener) {
        this.pontos = pontos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ponto_validado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PontoColeta ponto = pontos.get(position);
        holder.bind(ponto, listener);
    }

    @Override
    public int getItemCount() {
        return pontos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvEndereco, tvMateriais;
        MaterialButton btnDeletar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvEndereco = itemView.findViewById(R.id.tvEndereco);
            tvMateriais = itemView.findViewById(R.id.tvMateriais);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }

        public void bind(PontoColeta ponto, OnDeletarClickListener listener) {
            tvNome.setText(ponto.getNome());
            tvEndereco.setText(ponto.getEndereco());

            // Formatar lista de materiais
            String materiais = "Materiais: ";
            if (ponto.getTiposMateriais() != null && !ponto.getTiposMateriais().isEmpty()) {
                materiais += String.join(", ", ponto.getTiposMateriais());
            } else {
                materiais += "Não especificado";
            }
            tvMateriais.setText(materiais);

            btnDeletar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeletarClick(ponto);
                }
            });
        }
    }
}