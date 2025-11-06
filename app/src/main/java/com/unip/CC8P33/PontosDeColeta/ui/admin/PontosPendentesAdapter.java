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

public class PontosPendentesAdapter extends RecyclerView.Adapter<PontosPendentesAdapter.ViewHolder> {

    private List<PontoColeta> pontos;
    private OnValidarClickListener validarListener;
    private OnRejeitarClickListener rejeitarListener;

    public interface OnValidarClickListener {
        void onValidarClick(PontoColeta ponto);
    }

    public interface OnRejeitarClickListener {
        void onRejeitarClick(PontoColeta ponto);
    }

    public PontosPendentesAdapter(List<PontoColeta> pontos,
                                  OnValidarClickListener validarListener,
                                  OnRejeitarClickListener rejeitarListener) {
        this.pontos = pontos;
        this.validarListener = validarListener;
        this.rejeitarListener = rejeitarListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ponto_pendente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PontoColeta ponto = pontos.get(position);
        holder.bind(ponto, validarListener, rejeitarListener);
    }

    @Override
    public int getItemCount() {
        return pontos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvEndereco, tvMateriais;
        MaterialButton btnValidar, btnRejeitar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvEndereco = itemView.findViewById(R.id.tvEndereco);
            tvMateriais = itemView.findViewById(R.id.tvMateriais);
            btnValidar = itemView.findViewById(R.id.btnValidar);
            btnRejeitar = itemView.findViewById(R.id.btnRejeitar);
        }

        public void bind(PontoColeta ponto,
                         OnValidarClickListener validarListener,
                         OnRejeitarClickListener rejeitarListener) {
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

            btnValidar.setOnClickListener(v -> {
                if (validarListener != null) {
                    validarListener.onValidarClick(ponto);
                }
            });

            btnRejeitar.setOnClickListener(v -> {
                if (rejeitarListener != null) {
                    rejeitarListener.onRejeitarClick(ponto);
                }
            });
        }
    }
}