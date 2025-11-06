package com.unip.CC8P33.PontosDeColeta;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.unip.CC8P33.PontosDeColeta.ui.auth.LoginActivity;
import com.unip.CC8P33.PontosDeColeta.utils.FirebaseConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // CRÍTICO: Configurar Firebase ANTES de qualquer outra coisa
        FirebaseConfig.initialize(this);

        // Redirecionar para tela de login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}