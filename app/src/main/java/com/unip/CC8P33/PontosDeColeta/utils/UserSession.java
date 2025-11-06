package com.unip.CC8P33.PontosDeColeta.utils;

import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unip.CC8P33.PontosDeColeta.model.Usuario;

import java.util.Objects;

public class UserSession {

    private static final String TAG = "UserSession";
    private static Usuario usuarioAtual = null;

    public interface OnUserLoadedListener {
        void onUserLoaded(Usuario usuario);
        void onError(String erro);
    }

    public static void carregarUsuario(OnUserLoadedListener listener) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    usuarioAtual = documentSnapshot.toObject(Usuario.class);
                    if (usuarioAtual != null) {
                        Log.d(TAG, "Usuário carregado: " + usuarioAtual.getTipo());
                        listener.onUserLoaded(usuarioAtual);
                    } else {
                        listener.onError("Usuário não encontrado");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao carregar usuário", e);
                    listener.onError(e.getMessage());
                });
    }

    public static boolean isAdmin() {
        return usuarioAtual != null && usuarioAtual.isAdmin();
    }

    public static Usuario getUsuarioAtual() {
        return usuarioAtual;
    }
}