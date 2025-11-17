package com.unip.CC8P33.PontosDeColeta.ui.cidadao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.unip.CC8P33.PontosDeColeta.R;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import com.unip.CC8P33.PontosDeColeta.model.Usuario;
import com.unip.CC8P33.PontosDeColeta.repository.PontoColetaRepository;
import com.unip.CC8P33.PontosDeColeta.ui.admin.AdminPanelActivity;
import com.unip.CC8P33.PontosDeColeta.ui.auth.LoginActivity;
import com.unip.CC8P33.PontosDeColeta.utils.PopularDadosHelper;
import com.unip.CC8P33.PontosDeColeta.utils.UserSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaCidadaoActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MapaCidadao";
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    // UI Components
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private EditText etPesquisa;
    private ImageButton btnLimparPesquisa;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    // Map Components
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private PontoColetaRepository repository;

    // Data
    private List<PontoColeta> todosPontos = new ArrayList<PontoColeta>();
    private List<PontoColeta> pontosFiltrados = new ArrayList<PontoColeta>();
    private Map<Marker, PontoColeta> markerPontoMap = new HashMap<Marker, PontoColeta>();

    // Chips (Filtros)
    private Chip chipTodos, chipPlastico, chipPapel, chipVidro, chipMetal,
            chipEletronicos, chipOrganico, chipMadeira;

    // FABs
    private FloatingActionButton fabSugerirPonto, fabMinhaLocalizacao, fabFiltros;

    private String filtroAtual = "todos";
    private String termoPesquisa = "";
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cidadao);

        // Inicializar componentes
        inicializarViews();
        configurarToolbar();
        configurarNavigationDrawer();
        configurarMapa();
        configurarPesquisa();
        configurarFiltros();
        configurarFABs();

        //PopularDadosHelper.gerarPontosEmSaoPaulo(this);

        // Carregar dados do usuário
        carregarDadosUsuario();

        // Inicializar serviços
        repository = new PontoColetaRepository();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void inicializarViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        etPesquisa = findViewById(R.id.etPesquisa);
        btnLimparPesquisa = findViewById(R.id.btnLimparPesquisa);

        // Chips
        chipTodos = findViewById(R.id.chipTodos);
        chipPlastico = findViewById(R.id.chipPlastico);
        chipPapel = findViewById(R.id.chipPapel);
        chipVidro = findViewById(R.id.chipVidro);
        chipMetal = findViewById(R.id.chipMetal);
        chipEletronicos = findViewById(R.id.chipEletronicos);
        chipOrganico = findViewById(R.id.chipOrganico);
        chipMadeira = findViewById(R.id.chipMadeira);

        // FABs
        fabSugerirPonto = findViewById(R.id.fabSugerirPonto);
        fabMinhaLocalizacao = findViewById(R.id.fabMinhaLocalizacao);
        fabFiltros = findViewById(R.id.fabFiltros);

        // Bottom Sheet
        View bottomSheet = findViewById(R.id.bottomSheetFiltros);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    private void configurarNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void carregarDadosUsuario() {
        UserSession.carregarUsuario(new UserSession.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(Usuario usuario) {
                isAdmin = usuario.isAdmin();

                // Atualizar header do drawer
                View headerView = navigationView.getHeaderView(0);
                TextView tvNome = headerView.findViewById(R.id.tvNomeUsuario);
                TextView tvEmail = headerView.findViewById(R.id.tvEmailUsuario);
                tvNome.setText(usuario.getNome());
                tvEmail.setText(usuario.getEmail());

                // Mostrar/ocultar item admin no menu
                MenuItem adminItem = navigationView.getMenu().findItem(R.id.nav_admin);
                if (adminItem != null) {
                    adminItem.setVisible(isAdmin);
                }

                if (isAdmin) {
                    Toast.makeText(MapaCidadaoActivity.this,
                            "Modo Administrador ativado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String erro) {
                Log.e(TAG, "Erro ao carregar usuário: " + erro);
            }
        });
    }

    private void configurarMapa() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Log.d(TAG, "Mapa pronto!");

        // Configurar mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        // Configurar clique no marcador
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                PontoColeta ponto = markerPontoMap.get(marker);
                if (ponto != null) {
                    mostrarDetalhesPonto(ponto);
                }
                return false;
            }
        });

        // Habilitar localização
        habilitarLocalizacao();

        // Carregar pontos
        carregarPontos();

        // Centralizar em São Paulo por padrão
        LatLng saoPaulo = new LatLng(-23.5505, -46.6333);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 12));
    }

    private void configurarPesquisa() {
        etPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                termoPesquisa = s.toString().toLowerCase();
                btnLimparPesquisa.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                aplicarFiltrosEPesquisa();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnLimparPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPesquisa.setText("");
            }
        });
    }

    private void configurarFiltros() {
        chipTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("todos");
            }
        });

        chipPlastico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("plastico");
            }
        });

        chipPapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("papel");
            }
        });

        chipVidro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("vidro");
            }
        });

        chipMetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("metal");
            }
        });

        chipEletronicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("eletronicos");
            }
        });

        chipOrganico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("organico");
            }
        });

        chipMadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltro("madeira");
            }
        });
    }

    private void configurarFABs() {
        fabSugerirPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroPonto();
            }
        });

        fabMinhaLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaMinhaLocalizacao();
            }
        });

        fabFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBottomSheet();
            }
        });
    }

    private void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
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
        chipOrganico.setChecked(filtro.equals("organico"));
        chipMadeira.setChecked(filtro.equals("madeira"));

        aplicarFiltrosEPesquisa();

        // Esconder bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void aplicarFiltrosEPesquisa() {
        if (mMap == null) {
            return;
        }

        // Limpar mapa
        mMap.clear();
        markerPontoMap.clear();
        pontosFiltrados.clear();

        // Aplicar filtros e pesquisa
        int count = 0;
        for (PontoColeta ponto : todosPontos) {
            // Verificar filtro de material
            boolean passaFiltro = filtroAtual.equals("todos") ||
                    (ponto.getTiposMateriais() != null &&
                            ponto.getTiposMateriais().contains(filtroAtual));

            // Verificar pesquisa (busca em nome E endereço)
            boolean passaPesquisa = true;
            if (!termoPesquisa.isEmpty()) {
                String nomeMinusculo = ponto.getNome() != null ?
                        ponto.getNome().toLowerCase() : "";
                String enderecoMinusculo = ponto.getEndereco() != null ?
                        ponto.getEndereco().toLowerCase() : "";

                passaPesquisa = nomeMinusculo.contains(termoPesquisa) ||
                        enderecoMinusculo.contains(termoPesquisa);
            }

            // Adicionar ponto se passar nos dois critérios
            if (passaFiltro && passaPesquisa) {
                pontosFiltrados.add(ponto);
                adicionarMarcador(ponto);
                count++;
            }
        }

        Log.d(TAG, "Filtro: '" + filtroAtual + "', Pesquisa: '" + termoPesquisa +
                "', Resultados: " + count + "/" + todosPontos.size());

        // Feedback para o usuário
        if (!termoPesquisa.isEmpty() || !filtroAtual.equals("todos")) {
            if (count == 0) {
                Toast.makeText(this, "Nenhum ponto encontrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, count + " ponto(s) encontrado(s)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void carregarPontos() {
        Log.d(TAG, "Carregando pontos do Firestore...");

        repository.getPontosValidados()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    todosPontos.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        PontoColeta ponto = document.toObject(PontoColeta.class);
                        todosPontos.add(ponto);
                        Log.d(TAG, "Ponto carregado: " + ponto.getNome());
                    }

                    Log.d(TAG, "Total de pontos carregados: " + todosPontos.size());

                    if (todosPontos.isEmpty()) {
                        Toast.makeText(this,
                                "Nenhum ponto de coleta encontrado. Cadastre o primeiro!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        aplicarFiltrosEPesquisa();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao carregar pontos", e);
                    Toast.makeText(this,
                            "Erro ao carregar pontos: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void adicionarMarcador(PontoColeta ponto) {
        try {
            LatLng posicao = new LatLng(ponto.getLatitude(), ponto.getLongitude());

            // Cor baseada no primeiro material
            float cor = BitmapDescriptorFactory.HUE_GREEN;
            if (ponto.getTiposMateriais() != null && !ponto.getTiposMateriais().isEmpty()) {
                String primeiroMaterial = ponto.getTiposMateriais().get(0);
                switch (primeiroMaterial) {
                    case "plastico":
                        cor = BitmapDescriptorFactory.HUE_BLUE;
                        break;
                    case "papel":
                        cor = BitmapDescriptorFactory.HUE_YELLOW;
                        break;
                    case "vidro":
                        cor = BitmapDescriptorFactory.HUE_GREEN;
                        break;
                    case "metal":
                        cor = BitmapDescriptorFactory.HUE_ORANGE;
                        break;
                    case "eletronicos":
                        cor = BitmapDescriptorFactory.HUE_RED;
                        break;
                    case "organico":
                        cor = BitmapDescriptorFactory.HUE_VIOLET;
                        break;
                    case "madeira":
                        cor = BitmapDescriptorFactory.HUE_ROSE;
                        break;
                }
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(posicao)
                    .title(ponto.getNome())
                    .snippet(ponto.getEndereco())
                    .icon(BitmapDescriptorFactory.defaultMarker(cor));

            Marker marker = mMap.addMarker(markerOptions);
            if (marker != null) {
                markerPontoMap.put(marker, ponto);
                Log.d(TAG, "Marcador adicionado: " + ponto.getNome());
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao adicionar marcador", e);
        }
    }

    private void habilitarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        try {
            mMap.setMyLocationEnabled(true);
            irParaMinhaLocalizacao();
        } catch (SecurityException e) {
            Log.e(TAG, "Erro ao habilitar localização", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                habilitarLocalizacao();
            } else {
                Toast.makeText(this, "Permissão de localização negada",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void irParaMinhaLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng minhaLocalizacao = new LatLng(
                                location.getLatitude(),
                                location.getLongitude()
                        );
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                minhaLocalizacao, 14));
                        Toast.makeText(this, "Localização obtida!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Não foi possível obter sua localização",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erro ao obter localização", e);
                    Toast.makeText(this, "Erro ao obter localização",
                            Toast.LENGTH_SHORT).show();
                });
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_mapa) {
            // Já está no mapa
        } else if (id == R.id.nav_admin) {
            Intent intent = new Intent(this, AdminPanelActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            fazerLogout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fazerLogout() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Deseja realmente sair da sua conta?")
                .setPositiveButton("Sair", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MapaCidadaoActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            carregarPontos();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }
}