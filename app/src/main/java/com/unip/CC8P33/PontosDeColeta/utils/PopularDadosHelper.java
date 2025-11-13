package com.unip.CC8P33.PontosDeColeta.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import com.unip.CC8P33.PontosDeColeta.repository.PontoColetaRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PopularDadosHelper {
    private static final String TAG = "PopularPontos";

    public static void gerarPontosEmSaoPaulo(Context context) {
        PontoColetaRepository repository = new PontoColetaRepository();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG, "Iniciando geração de pontos em São Paulo...");

        // Array com todos os pontos
        PontoColeta[] pontos = {
                // ZONA CENTRAL
                criarPonto("Ecoponto República", "Praça da República - República, São Paulo",
                        -23.5431, -46.6438,
                        Arrays.asList("plastico", "papel", "vidro", "metal"),
                        "(11) 3241-5555", "Seg-Sex: 8h-17h", userId),

                criarPonto("Reciclagem Centro", "Rua Boa Vista, 150 - Centro, São Paulo",
                        -23.5475, -46.6361,
                        Arrays.asList("papel", "plastico", "metal", "eletronicos"),
                        "(11) 3106-4400", "Seg-Sex: 9h-18h", userId),

                criarPonto("Coleta Seletiva Sé", "Praça da Sé - Sé, São Paulo",
                        -23.5505, -46.6333,
                        Arrays.asList("plastico", "vidro", "metal"),
                        "(11) 3107-7000", "Seg-Dom: 6h-22h", userId),

                // ZONA OESTE
                criarPonto("Ecoponto Pinheiros", "Rua dos Pinheiros, 498 - Pinheiros, São Paulo",
                        -23.5620, -46.6820,
                        Arrays.asList("plastico", "papel", "vidro", "metal", "eletronicos"),
                        "(11) 3813-2200", "Seg-Sáb: 8h-18h", userId),

                criarPonto("Coleta Vila Madalena", "Rua Harmonia, 150 - Vila Madalena, São Paulo",
                        -23.5500, -46.6890,
                        Arrays.asList("plastico", "vidro", "papel"),
                        "(11) 3032-4500", "Seg-Sex: 9h-17h", userId),

                criarPonto("Reciclagem Lapa", "Rua Clélia, 355 - Lapa, São Paulo",
                        -23.5280, -46.6920,
                        Arrays.asList("metal", "plastico", "papel", "madeira"),
                        "(11) 3862-0400", "Ter-Sáb: 8h-18h", userId),

                criarPonto("Ecoponto Perdizes", "Rua Cardoso de Almeida, 1100 - Perdizes, São Paulo",
                        -23.5380, -46.6730,
                        Arrays.asList("plastico", "papel", "vidro", "metal"),
                        "(11) 3872-2100", "Seg-Sex: 8h-17h", userId),

                // ZONA SUL
                criarPonto("Parque Ibirapuera", "Av. Pedro Álvares Cabral - Vila Mariana, São Paulo",
                        -23.5872, -46.6572,
                        Arrays.asList("plastico", "papel", "vidro", "metal", "organico"),
                        "(11) 5574-5177", "Seg-Dom: 5h-0h", userId),

                criarPonto("Ecoponto Moema", "Av. Ibirapuera, 2907 - Moema, São Paulo",
                        -23.6010, -46.6620,
                        Arrays.asList("plastico", "papel", "vidro", "metal"),
                        "(11) 5051-8800", "Seg-Sáb: 8h-18h", userId),

                criarPonto("Coleta Vila Mariana", "Rua Domingos de Morais, 2564 - Vila Mariana, São Paulo",
                        -23.5950, -46.6390,
                        Arrays.asList("papel", "plastico", "vidro"),
                        "(11) 5549-7400", "Seg-Sex: 9h-18h", userId),

                criarPonto("Reciclagem Jabaquara", "Av. Jabaquara, 1380 - Jabaquara, São Paulo",
                        -23.6420, -46.6410,
                        Arrays.asList("plastico", "metal", "papel"),
                        "(11) 5012-2100", "Seg-Sex: 8h-17h", userId),

                criarPonto("Ecoponto Santo Amaro", "Av. Santo Amaro, 6166 - Santo Amaro, São Paulo",
                        -23.6580, -46.7080,
                        Arrays.asList("plastico", "papel", "vidro", "metal", "eletronicos"),
                        "(11) 5181-7300", "Seg-Sáb: 8h-18h", userId),

                // ZONA NORTE
                criarPonto("Ecoponto Santana", "Av. Cruzeiro do Sul, 1100 - Santana, São Paulo",
                        -23.5020, -46.6280,
                        Arrays.asList("plastico", "papel", "vidro", "metal"),
                        "(11) 2971-3500", "Seg-Sex: 8h-17h", userId),

                criarPonto("Coleta Tucuruvi", "Av. Tucuruvi, 808 - Tucuruvi, São Paulo",
                        -23.4800, -46.6020,
                        Arrays.asList("papel", "plastico", "metal"),
                        "(11) 2283-1400", "Seg-Sáb: 9h-18h", userId),

                criarPonto("Reciclagem Casa Verde", "Av. Casa Verde, 3355 - Casa Verde, São Paulo",
                        -23.5140, -46.6540,
                        Arrays.asList("plastico", "vidro", "papel", "madeira"),
                        "(11) 3857-0800", "Ter-Dom: 8h-17h", userId),

                // ZONA LESTE
                criarPonto("Ecoponto Tatuapé", "Rua Tuiuti, 2100 - Tatuapé, São Paulo",
                        -23.5380, -46.5750,
                        Arrays.asList("plastico", "papel", "vidro", "metal", "eletronicos"),
                        "(11) 2097-0500", "Seg-Sáb: 8h-18h", userId),

                criarPonto("Coleta Penha", "Av. Amador Bueno da Veiga, 2564 - Penha, São Paulo",
                        -23.5290, -46.5410,
                        Arrays.asList("plastico", "papel", "metal"),
                        "(11) 2097-2100", "Seg-Sex: 9h-18h", userId),

                criarPonto("Reciclagem Aricanduva", "Av. Aricanduva, 5555 - Aricanduva, São Paulo",
                        -23.5640, -46.5180,
                        Arrays.asList("papel", "plastico", "vidro", "metal"),
                        "(11) 2723-4800", "Seg-Dom: 10h-22h", userId),

                criarPonto("Ecoponto São Mateus", "Av. Sapopemba, 9064 - São Mateus, São Paulo",
                        -23.6050, -46.4790,
                        Arrays.asList("plastico", "metal", "papel"),
                        "(11) 2031-7200", "Seg-Sex: 8h-17h", userId),

                criarPonto("Coleta Itaquera", "Av. Itaquera, 8266 - Itaquera, São Paulo",
                        -23.5420, -46.4560,
                        Arrays.asList("plastico", "papel", "vidro", "metal"),
                        "(11) 2523-9300", "Seg-Sáb: 8h-18h", userId),

                // AVENIDA PAULISTA E REGIÃO
                criarPonto("Ecoponto Paulista", "Av. Paulista, 1578 - Bela Vista, São Paulo",
                        -23.5614, -46.6562,
                        Arrays.asList("papel", "plastico", "metal", "eletronicos"),
                        "(11) 3266-7100", "Seg-Dom: 9h-20h", userId),

                criarPonto("Coleta Consolação", "Rua da Consolação, 2697 - Consolação, São Paulo",
                        -23.5540, -46.6600,
                        Arrays.asList("plastico", "papel", "vidro"),
                        "(11) 3256-4200", "Seg-Sex: 9h-18h", userId),

                criarPonto("Reciclagem Jardins", "Rua Augusta, 2690 - Jardins, São Paulo",
                        -23.5580, -46.6600,
                        Arrays.asList("papel", "plastico", "vidro", "metal"),
                        "(11) 3062-3800", "Seg-Sáb: 10h-19h", userId),

                // MERCADOS E CENTROS COMERCIAIS
                criarPonto("Mercadão Centro", "Rua da Cantareira, 306 - Centro, São Paulo",
                        -23.5410, -46.6299,
                        Arrays.asList("organico", "plastico", "papel"),
                        "(11) 3313-3365", "Ter-Dom: 6h-16h", userId),

                criarPonto("Shopping Iguatemi", "Av. Brg. Faria Lima, 2232 - Jardim Paulistano, São Paulo",
                        -23.5798, -46.6870,
                        Arrays.asList("eletronicos", "plastico", "metal", "papel"),
                        "(11) 3816-6116", "Seg-Dom: 10h-22h", userId),

                criarPonto("Shopping Morumbi", "Av. Roque Petroni Júnior, 1089 - Morumbi, São Paulo",
                        -23.6230, -46.6980,
                        Arrays.asList("plastico", "papel", "vidro", "metal"),
                        "(11) 3740-8900", "Seg-Dom: 10h-22h", userId),

                // UNIVERSIDADES
                criarPonto("USP - Cidade Universitária", "Av. Prof. Luciano Gualberto - Butantã, São Paulo",
                        -23.5596, -46.7312,
                        Arrays.asList("plastico", "papel", "vidro", "metal", "eletronicos", "madeira"),
                        "(11) 3091-3116", "Seg-Sex: 8h-18h", userId),

                criarPonto("PUC-SP Consolação", "Rua Monte Alegre, 984 - Perdizes, São Paulo",
                        -23.5370, -46.6650,
                        Arrays.asList("papel", "plastico", "metal"),
                        "(11) 3670-8000", "Seg-Sex: 7h-22h", userId),

                criarPonto("UNINOVE Memorial", "Av. Francisco Matarazzo, 612 - Barra Funda, São Paulo",
                        -23.5270, -46.6660,
                        Arrays.asList("plastico", "papel", "vidro", "metal"),
                        "(11) 3665-9300", "Seg-Sex: 8h-22h", userId),

                // PARQUES
                criarPonto("Parque Villa-Lobos", "Av. Prof. Fonseca Rodrigues, 2001 - Alto de Pinheiros, São Paulo",
                        -23.5460, -46.7200,
                        Arrays.asList("plastico", "papel", "vidro", "organico"),
                        "(11) 2683-6302", "Seg-Dom: 5h30-19h", userId)
        };

        // Cadastrar todos os pontos
        int total = pontos.length;
        int[] contador = {0};

        Toast.makeText(context, "Iniciando cadastro de " + total + " pontos...",
                Toast.LENGTH_LONG).show();

        for (PontoColeta ponto : pontos) {
            // Definir como validado direto (pular fila de aprovação)
            ponto.setStatus("validado");
            ponto.setValidadoPor(userId);
            ponto.setDataValidacao(System.currentTimeMillis());

            repository.cadastrarPonto(ponto)
                    .addOnSuccessListener(aVoid -> {
                        contador[0]++;
                        Log.d(TAG, "Ponto " + contador[0] + "/" + total + " cadastrado: " +
                                ponto.getNome());

                        if (contador[0] == total) {
                            Toast.makeText(context,
                                    "✅ " + total + " pontos cadastrados com sucesso!",
                                    Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Todos os pontos foram cadastrados!");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Erro ao cadastrar ponto: " + ponto.getNome(), e);
                    });
        }
    }

    private static PontoColeta criarPonto(String nome, String endereco,
                                          double lat, double lng,
                                          List materiais,
                                          String telefone, String horario,
                                          String userId) {
        PontoColeta ponto = new PontoColeta(nome, endereco, lat, lng,
                materiais, telefone, horario, userId);
        return ponto;
    }
}