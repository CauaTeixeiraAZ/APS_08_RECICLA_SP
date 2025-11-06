package com.unip.CC8P33.PontosDeColeta.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;

public class PontoColetaRepository {

    private final FirebaseFirestore db;
    private static final String COLLECTION_PONTOS = "pontosColeta";

    public PontoColetaRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    // Buscar todos os pontos validados
    public Task<QuerySnapshot> getPontosValidados() {
        return db.collection(COLLECTION_PONTOS)
                .whereEqualTo("status", "validado")
                .get();
    }

    // Buscar pontos por tipo de material
    public Task<QuerySnapshot> getPontosPorMaterial(String material) {
        return db.collection(COLLECTION_PONTOS)
                .whereEqualTo("status", "validado")
                .whereArrayContains("tiposMateriais", material)
                .get();
    }

    // Buscar pontos pendentes (para admin)
    public Task<QuerySnapshot> getPontosPendentes() {
        return db.collection(COLLECTION_PONTOS)
                .whereEqualTo("status", "pendente")
                .orderBy("dataCriacao", Query.Direction.DESCENDING)
                .get();
    }

    // Cadastrar novo ponto
    public Task<Void> cadastrarPonto(PontoColeta ponto) {
        String id = db.collection(COLLECTION_PONTOS).document().getId();
        ponto.setId(id);
        return db.collection(COLLECTION_PONTOS)
                .document(id)
                .set(ponto);
    }

    // Validar ponto (admin)
    public Task<Void> validarPonto(String pontoId, String adminUid) {
        return db.collection(COLLECTION_PONTOS)
                .document(pontoId)
                .update(
                        "status", "validado",
                        "validadoPor", adminUid,
                        "dataValidacao", System.currentTimeMillis()
                );
    }

    // Rejeitar ponto (admin)
    public Task<Void> rejeitarPonto(String pontoId) {
        return db.collection(COLLECTION_PONTOS)
                .document(pontoId)
                .update("status", "rejeitado");
    }

    // Deletar ponto (admin)
    public Task<Void> deletarPonto(String pontoId) {
        return db.collection(COLLECTION_PONTOS)
                .document(pontoId)
                .delete();
    }

    // Atualizar ponto (admin)
    public Task<Void> atualizarPonto(PontoColeta ponto) {
        return db.collection(COLLECTION_PONTOS)
                .document(ponto.getId())
                .set(ponto);
    }
}
