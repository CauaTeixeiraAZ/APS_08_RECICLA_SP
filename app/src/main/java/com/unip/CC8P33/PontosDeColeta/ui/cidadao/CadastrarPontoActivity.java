package com.unip.CC8P33.PontosDeColeta.ui.cidadao;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import com.unip.CC8P33.PontosDeColeta.repository.PontoColetaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import android.os.Handler;

public class CadastrarPontoActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 101;

    private TextInputEditText etNome, etEndereco, etLatitude, etLongitude,
            etTelefone, etHorario;
    private CheckBox cbPlastico, cbPapel, cbVidro, cbMetal, cbEletronicos, cbOrganico;
    private MaterialButton btnCadastrar, btnUsarLocalizacaoAtual;
    private ProgressBar progressBar;

    private PontoColetaRepository repository;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_ponto);

        // Inicializar
        repository = new PontoColetaRepository();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Inicializar views
        etNome = findViewById(R.id.etNome);
        etEndereco = findViewById(R.id.etEndereco);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        etTelefone = findViewById(R.id.etTelefone);
        etHorario = findViewById(R.id.etHorario);

        cbPlastico = findViewById(R.id.cbPlastico);
        cbPapel = findViewById(R.id.cbPapel);
        cbVidro = findViewById(R.id.cbVidro);
        cbMetal = findViewById(R.id.cbMetal);
        cbEletronicos = findViewById(R.id.cbEletronicos);
        cbOrganico = findViewById(R.id.cbOrganico);

        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnUsarLocalizacaoAtual = findViewById(R.id.btnUsarLocalizacaoAtual);
        progressBar = findViewById(R.id.progressBar);

        // Configurar listeners
        btnCadastrar.setOnClickListener(v -> cadastrarPonto());
        btnUsarLocalizacaoAtual.setOnClickListener(v -> usarLocalizacaoAtual());
    }

    private void usarLocalizacaoAtual() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        etLatitude.setText(String.valueOf(location.getLatitude()));
                        etLongitude.setText(String.valueOf(location.getLongitude()));
                        Toast.makeText(this, "Localização obtida!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this,
                                "Não foi possível obter a localização",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                usarLocalizacaoAtual();
            }
        }
    }

    private void cadastrarPonto() {
        // Validar campos
        String nome = Objects.requireNonNull(etNome.getText()).toString().trim();
        String endereco = Objects.requireNonNull(etEndereco.getText()).toString().trim();
        String latStr = Objects.requireNonNull(etLatitude.getText()).toString().trim();
        String lngStr = Objects.requireNonNull(etLongitude.getText()).toString().trim();
        String telefone = Objects.requireNonNull(etTelefone.getText()).toString().trim();
        String horario = Objects.requireNonNull(etHorario.getText()).toString().trim();

        if (TextUtils.isEmpty(nome)) {
            etNome.setError("Digite o nome do local");
            return;
        }

        if (TextUtils.isEmpty(endereco)) {
            etEndereco.setError("Digite o endereço");
            return;
        }

        if (TextUtils.isEmpty(latStr) || TextUtils.isEmpty(lngStr)) {
            Toast.makeText(this, "Digite as coordenadas ou use a localização atual",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latStr);
            longitude = Double.parseDouble(lngStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Coordenadas inválidas", Toast.LENGTH_SHORT).show();
            return;
        }

        // Coletar tipos de materiais
        List<String> tiposMateriais = new ArrayList<>();
        if (cbPlastico.isChecked()) tiposMateriais.add("plastico");
        if (cbPapel.isChecked()) tiposMateriais.add("papel");
        if (cbVidro.isChecked()) tiposMateriais.add("vidro");
        if (cbMetal.isChecked()) tiposMateriais.add("metal");
        if (cbEletronicos.isChecked()) tiposMateriais.add("eletronicos");
        if (cbOrganico.isChecked()) tiposMateriais.add("organico");

        if (tiposMateriais.isEmpty()) {
            Toast.makeText(this,
                    "Selecione pelo menos um tipo de material",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar objeto PontoColeta
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        PontoColeta ponto = new PontoColeta(
                nome, endereco, latitude, longitude,
                tiposMateriais, telefone, horario, userId
        );

        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);
        btnCadastrar.setEnabled(false);

        // Timeout de 15 segundos
        final Handler handler = new Handler();
        final Runnable timeoutRunnable = () -> {
            progressBar.setVisibility(View.GONE);
            btnCadastrar.setEnabled(true);
            Toast.makeText(CadastrarPontoActivity.this,
                    "Conexão lenta. Tente novamente.",
                    Toast.LENGTH_LONG).show();
        };
        handler.postDelayed(timeoutRunnable, 15000);

        // Cadastrar no Firebase
        repository.cadastrarPonto(ponto)
                .addOnSuccessListener(aVoid -> {
                    handler.removeCallbacks(timeoutRunnable);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this,
                            "Ponto cadastrado! Aguarde validação do administrador.",
                            Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    handler.removeCallbacks(timeoutRunnable);
                    progressBar.setVisibility(View.GONE);
                    btnCadastrar.setEnabled(true);
                    Toast.makeText(this,
                            "Erro ao cadastrar: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
}