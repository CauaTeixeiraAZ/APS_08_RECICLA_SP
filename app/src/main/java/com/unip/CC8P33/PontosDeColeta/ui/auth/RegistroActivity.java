package com.unip.CC8P33.PontosDeColeta.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.Usuario;
import android.os.Handler;

import java.util.Objects;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etNome, etEmail, etSenha;
    private RadioButton rbCidadao, rbAdmin;
    private MaterialButton btnCriarConta, btnVoltar;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicializar views
        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        rbCidadao = findViewById(R.id.rbCidadao);
        rbAdmin = findViewById(R.id.rbAdmin);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnVoltar = findViewById(R.id.btnVoltar);
        progressBar = findViewById(R.id.progressBar);

        // Configurar listeners
        btnCriarConta.setOnClickListener(v -> criarConta());
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void criarConta() {
        String nome = Objects.requireNonNull(etNome.getText()).toString().trim();
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String senha = Objects.requireNonNull(etSenha.getText()).toString().trim();
        String tipo = rbAdmin.isChecked() ? "admin" : "cidadao";

        // Validações
        if (TextUtils.isEmpty(nome)) {
            etNome.setError("Digite seu nome");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Digite o email");
            return;
        }

        if (TextUtils.isEmpty(senha)) {
            etSenha.setError("Digite a senha");
            return;
        }

        if (senha.length() < 6) {
            etSenha.setError("A senha deve ter no mínimo 6 caracteres");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnCriarConta.setEnabled(false);
        btnVoltar.setEnabled(false);

        // Timeout de 20 segundos
        final Handler handler = new Handler();
        final Runnable timeoutRunnable = () -> {
            progressBar.setVisibility(View.GONE);
            btnCriarConta.setEnabled(true);
            btnVoltar.setEnabled(true);
            Toast.makeText(RegistroActivity.this,
                    "Conexão muito lenta. Tente novamente.",
                    Toast.LENGTH_LONG).show();
        };
        handler.postDelayed(timeoutRunnable, 20000);

        // Criar usuário no Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Salvar dados adicionais no Firestore
                            Usuario usuario = new Usuario(
                                    firebaseUser.getUid(),
                                    nome,
                                    email,
                                    tipo
                            );

                            db.collection("usuarios")
                                    .document(firebaseUser.getUid())
                                    .set(usuario)
                                    .addOnSuccessListener(aVoid -> {
                                        handler.removeCallbacks(timeoutRunnable);
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(RegistroActivity.this,
                                                "Conta criada com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        handler.removeCallbacks(timeoutRunnable);
                                        progressBar.setVisibility(View.GONE);
                                        btnCriarConta.setEnabled(true);
                                        btnVoltar.setEnabled(true);
                                        Toast.makeText(RegistroActivity.this,
                                                "Erro ao salvar dados: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        handler.removeCallbacks(timeoutRunnable);
                        progressBar.setVisibility(View.GONE);
                        btnCriarConta.setEnabled(true);
                        btnVoltar.setEnabled(true);
                        String erro = task.getException() != null ?
                                task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(RegistroActivity.this, "Erro: " + erro,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}