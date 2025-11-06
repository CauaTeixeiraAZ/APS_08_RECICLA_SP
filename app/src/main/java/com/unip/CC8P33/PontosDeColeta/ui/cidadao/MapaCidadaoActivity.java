package com.unip.CC8P33.PontosDeColeta.ui.cidadao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import com.unip.CC8P33.PontosDeColeta.repository.PontoColetaRepository;
import com.unip.CC8P33.PontosDeColeta.utils.PopularDadosHelper;
import com.unip.CC8P33.PontosDeColeta.ui.admin.AdminPanelActivity;
import com.unip.CC8P33.PontosDeColeta.utils.UserSession;
import com.unip.CC8P33.PontosDeColeta.model.Usuario;
import com.unip.CC8P33.PontosDeColeta.utils.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaCidadaoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private PontoColetaRepository repository;

    private List<PontoColeta> todosPontos = new ArrayList<>();
    private Map<Marker, PontoColeta> markerPontoMap = new HashMap<>();

    private Chip chipTodos, chipPlastico, chipPapel, chipVidro, chipMetal, chipEletronicos;
    private FloatingActionButton fabSugerirPonto, fabMinhaLocalizacao;

    private FloatingActionButton fabAdmin;

    private String filtroAtual = "todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cidadao);

        // ADICIONE ESTAS LINHAS:
        // Carregar dados do usuário
        UserSession.carregarUsuario(new UserSession.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(Usuario usuario) {
                if (usuario.isAdmin()) {
                    mostrarOpcoesAdmin();
                }
            }

            @Override
            public void onError(String erro) {
                Log.e("MapaCidadao", "Erro ao carregar usuário: " + erro);
            }
        });

        // resto do código onCreate...
    }

    // Adicione este método no final da classe:
    private void mostrarOpcoesAdmin() {
        // Adicionar FAB para painel admin
        FloatingActionButton fabAdmin = new FloatingActionButton(this);
        fabAdmin.setImageResource(android.R.drawable.ic_menu_manage);
        // ... configurar e adicionar à tela

        // Ou mostrar item no menu
        Toast.makeText(this, "Modo Administrador ativado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configurar mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Configurar clique no marcador
        mMap.setOnMarkerClickListener(marker -> {
            PontoColeta ponto = markerPontoMap.get(marker);
            if (ponto != null) {
                mostrarDetalhesPonto(ponto);
            }
            return false;
        });

        // Habilitar localização
        habilitarLocalizacao();

        // Carregar pontos
        carregarPontos();

        // Centralizar em São Paulo por padrão
        LatLng saoPaulo = new LatLng(-23.5505, -46.6333);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 12));
    }

    private void habilitarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        mMap.setMyLocationEnabled(true);
        irParaMinhaLocalizacao();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                habilitarLocalizacao();
            }
        }
    }

    private void irParaMinhaLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng minhaLocalizacao = new LatLng(
                                location.getLatitude(),
                                location.getLongitude()
                        );
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                minhaLocalizacao, 14));
                    }
                });
    }

    private void configurarFiltros() {
        chipTodos.setOnClickListener(v -> aplicarFiltro("todos"));
        chipPlastico.setOnClickListener(v -> aplicarFiltro("plastico"));
        chipPapel.setOnClickListener(v -> aplicarFiltro("papel"));
        chipVidro.setOnClickListener(v -> aplicarFiltro("vidro"));
        chipMetal.setOnClickListener(v -> aplicarFiltro("metal"));
        chipEletronicos.setOnClickListener(v -> aplicarFiltro("eletronicos"));
    }

    private void aplicarFiltro(String filtro) {
        filtroAtual = filtro;

        // Atualizar visual dos chips
        chipTodos.setChecked(filtro.equals("todos"));
        chipPlastico.setChecked(filtro.equals("plastico"));
        chipPapel.setChecked(filtro.equals("papel"));
        chipVidro.setChecked(filtro.equals("vidro"));
        chipMetal.setChecked(filtro.equals("metal"));
        chipEletronicos.setChecked(filtro.equals("eletronicos"));

        // Limpar marcadores
        mMap.clear();
        markerPontoMap.clear();

        // Adicionar marcadores filtrados
        for (PontoColeta ponto : todosPontos) {
            if (filtro.equals("todos") || ponto.getTiposMateriais().contains(filtro)) {
                adicionarMarcador(ponto);
            }
        }
    }

    private void carregarPontos() {
        repository.getPontosValidados()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    todosPontos.clear();
                    mMap.clear();
                    markerPontoMap.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        PontoColeta ponto = document.toObject(PontoColeta.class);
                        todosPontos.add(ponto);
                        adicionarMarcador(ponto);
                    }

                    if (todosPontos.isEmpty()) {
                        Toast.makeText(this,
                                "Nenhum ponto de coleta encontrado. Adicione o primeiro!",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Erro ao carregar pontos: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void adicionarMarcador(PontoColeta ponto) {
        LatLng posicao = new LatLng(ponto.getLatitude(), ponto.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions()
                .position(posicao)
                .title(ponto.getNome())
                .snippet(ponto.getEndereco())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        Marker marker = mMap.addMarker(markerOptions);
        if (marker != null) {
            markerPontoMap.put(marker, ponto);
        }
    }

    private void mostrarDetalhesPonto(PontoColeta ponto) {
        Intent intent = new Intent(this, DetalhesPontoActivity.class);
        intent.putExtra("pontoId", ponto.getId());
        intent.putExtra("pontoNome", ponto.getNome());
        intent.putExtra("pontoEndereco", ponto.getEndereco());
        intent.putExtra("pontoTelefone", ponto.getTelefone());
        intent.putExtra("pontoHorario", ponto.getHorarioFuncionamento());
        intent.putExtra("pontoLat", ponto.getLatitude());
        intent.putExtra("pontoLng", ponto.getLongitude());
        startActivity(intent);
    }

    private void abrirCadastroPonto() {
        Intent intent = new Intent(this, CadastrarPontoActivity.class);
        startActivity(intent);
    }

    private void abrirPainelAdmin() {
        Intent intent = new Intent(this, AdminPanelActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarregar pontos ao voltar para a tela
        if (mMap != null) {
            carregarPontos();
        }
    }
}
