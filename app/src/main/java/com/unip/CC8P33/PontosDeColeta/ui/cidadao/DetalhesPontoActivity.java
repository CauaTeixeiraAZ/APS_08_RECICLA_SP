package com.unip.CC8P33.PontosDeColeta.ui.cidadao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

    private TextView tvNome, tvEndereco, tvTelefone, tvHorario;
    private MaterialButton btnVerRota, btnFechar;

    private String nome, endereco, telefone, horario;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_ponto);

        // Receber dados
        nome = getIntent().getStringExtra("pontoNome");
        endereco = getIntent().getStringExtra("pontoEndereco");
        telefone = getIntent().getStringExtra("pontoTelefone");
        horario = getIntent().getStringExtra("pontoHorario");
        latitude = getIntent().getDoubleExtra("pontoLat", 0);
        longitude = getIntent().getDoubleExtra("pontoLng", 0);

        // Inicializar views
        tvNome = findViewById(R.id.tvNome);
        tvEndereco = findViewById(R.id.tvEndereco);
        tvTelefone = findViewById(R.id.tvTelefone);
        tvHorario = findViewById(R.id.tvHorario);
        btnVerRota = findViewById(R.id.btnVerRota);
        btnFechar = findViewById(R.id.btnFechar);

        // Preencher dados
        tvNome.setText(nome);
        tvEndereco.setText(endereco);

        if (!TextUtils.isEmpty(telefone)) {
            tvTelefone.setText(telefone);
            tvTelefone.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(horario)) {
            tvHorario.setText(horario);
            tvHorario.setVisibility(View.VISIBLE);
        }

        // Configurar mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapaMini);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Configurar botões
        btnVerRota.setOnClickListener(v -> abrirGoogleMaps());
        btnFechar.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng posicao = new LatLng(latitude, longitude);

        googleMap.addMarker(new MarkerOptions()
                .position(posicao)
                .title(nome)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicao, 15));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }

    private void abrirGoogleMaps() {
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
}