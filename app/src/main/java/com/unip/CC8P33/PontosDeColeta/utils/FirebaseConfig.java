package com.unip.CC8P33.PontosDeColeta.utils;

import android.content.Context;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class FirebaseConfig {

    private static final String TAG = "FirebaseConfig";
    private static boolean initialized = false;

    public static void initialize(Context context) {
        if (initialized) {
            return;
        }

        try {
            // Inicializar Firebase
            FirebaseApp.initializeApp(context);

            // Configurar Firestore para melhor performance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true) // Cache local
                    .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                    .build();

            db.setFirestoreSettings(settings);

            initialized = true;
            Log.d(TAG, "Firebase configurado com sucesso");

        } catch (Exception e) {
            Log.e(TAG, "Erro ao configurar Firebase", e);
        }
    }
}