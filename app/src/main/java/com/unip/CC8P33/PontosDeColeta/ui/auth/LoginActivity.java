package com.unip.CC8P33.PontosDeColeta.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.Usuario;
import com.unip.CC8P33.PontosDeColeta.ui.cidadao.MapaCidadaoActivity;
import android.os.Handler;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etSenha;
    private MaterialButton btnLogin, btnRegistrar;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar views
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        progressBar = findViewById(R.id.progressBar);

        // Verificar se já está logado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            irParaMapa();
        }

        // Configurar listeners
        btnLogin.setOnClickListener(v -> fazerLogin());
        btnRegistrar.setOnClickListener(v -> irParaRegistro());
    }

    private void fazerLogin() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String senha = Objects.requireNonNull(etSenha.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Digite o email");
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            etSenha.setError("Digite a senha");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnRegistrar.setEnabled(false);

        // Timeout de 15 segundos
        final Handler handler = new Handler();
        final Runnable timeoutRunnable = () -> {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            btnRegistrar.setEnabled(true);
            Toast.makeText(LoginActivity.this,
                    "Conexão muito lenta. Verifique sua internet.",
                    Toast.LENGTH_LONG).show();
        };
        handler.postDelayed(timeoutRunnable, 15000); // 15 segundos

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    handler.removeCallbacks(timeoutRunnable); // Cancelar timeout
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                    btnRegistrar.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login realizado com sucesso!",
                                Toast.LENGTH_SHORT).show();
                        irParaMapa();
                    } else {
                        String erro = task.getException() != null ?
                                task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(this, "Erro: " + erro,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void irParaRegistro() {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    private void irParaMapa() {
        Intent intent = new Intent(this, MapaCidadaoActivity.class);
        startActivity(intent);
        finish();
    }
}