package com.unip.CC8P33.PontosDeColeta.ui.cidadao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.unip.CC8P33.PontosDeColeta.R;

public class DetalhesPontoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "DetalhesPonto";

    private TextView tvNome, tvEndereco, tvTelefone, tvHorario;
    private MaterialButton btnVerRota, btnFechar;

    private String nome, endereco, telefone, horario;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_ponto);

        Log.d(TAG, "DetalhesPontoActivity iniciada");

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalhes do Ponto");
        }

        // Receber dados
        Intent intent = getIntent();
        if (intent != null) {
            nome = intent.getStringExtra("pontoNome");
            endereco = intent.getStringExtra("pontoEndereco");
            telefone = intent.getStringExtra("pontoTelefone");
            horario = intent.getStringExtra("pontoHorario");
            latitude = intent.getDoubleExtra("pontoLat", 0);
            longitude = intent.getDoubleExtra("pontoLng", 0);

            Log.d(TAG, "Dados recebidos: " + nome + " em " + latitude + "," + longitude);
        } else {
            Log.e(TAG, "Intent é null!");
        }

        // Inicializar views
        tvNome = findViewById(R.id.tvNome);
        tvEndereco = findViewById(R.id.tvEndereco);
        tvTelefone = findViewById(R.id.tvTelefone);
        tvHorario = findViewById(R.id.tvHorario);
        btnVerRota = findViewById(R.id.btnVerRota);
        btnFechar = findViewById(R.id.btnFechar);

        // Preencher dados
        if (tvNome != null) tvNome.setText(nome != null ? nome : "Nome não disponível");
        if (tvEndereco != null) tvEndereco.setText(endereco != null ? endereco : "Endereço não disponível");

        if (!TextUtils.isEmpty(telefone)) {
            tvTelefone.setText("📞 " + telefone);
            tvTelefone.setVisibility(View.VISIBLE);
        } else {
            tvTelefone.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(horario)) {
            tvHorario.setText("🕐 " + horario);
            tvHorario.setVisibility(View.VISIBLE);
        } else {
            tvHorario.setVisibility(View.GONE);
        }

        // Configurar mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaMini);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "MapFragment é null!");
        }

        // Configurar botões
        if (btnVerRota != null) {
            btnVerRota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirGoogleMaps();
                }
            });
        }

        if (btnFechar != null) {
            btnFechar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Mapa pronto!");

        if (latitude == 0 && longitude == 0) {
            Log.e(TAG, "Coordenadas inválidas!");
            return;
        }

        LatLng posicao = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions()
                .position(posicao)
                .title(nome)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicao, 15));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void abrirGoogleMaps() {
        if (latitude == 0 && longitude == 0) {
            Log.e(TAG, "Coordenadas inválidas para navegação!");
            return;
        }

        String uri = "google.navigation:q=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Se Google Maps não estiver instalado, abrir no navegador
            String url = "https://www.google.com/maps/dir/?api=1&destination="
                    + latitude + "," + longitude;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}