package com.unip.CC8P33.PontosDeColeta;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unip.CC8P33.PontosDeColeta.ui.auth.LoginActivity;
import com.unip.CC8P33.PontosDeColeta.ui.cidadao.MapaCidadaoActivity;
import com.unip.CC8P33.PontosDeColeta.utils.FirebaseConfig;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Configurar Firebase
        FirebaseConfig.initialize(this);

        // Aguardar 2 segundos e verificar autenticação
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            Intent intent;
            if (currentUser != null) {
                // Usuário já logado, ir direto para o mapa
                intent = new Intent(SplashActivity.this, MapaCidadaoActivity.class);
            } else {
                // Usuário não logado, ir para login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, 2000);
    }
}