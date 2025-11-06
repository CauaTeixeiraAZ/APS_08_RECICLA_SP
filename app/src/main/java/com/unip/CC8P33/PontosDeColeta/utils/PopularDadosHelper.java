package com.unip.CC8P33.PontosDeColeta.utils;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.unip.CC8P33.PontosDeColeta.model.PontoColeta;
import com.unip.CC8P33.PontosDeColeta.repository.PontoColetaRepository;
import java.util.Arrays;
import java.util.Objects;

public class PopularDadosHelper {

    public static void popularDadosExemplo(Context context) {
        PontoColetaRepository repository = new PontoColetaRepository();
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        // Ponto 1
        PontoColeta ponto1 = new PontoColeta(
                "Ecoponto Parque Ibirapuera",
                "Av. Pedro Álvares Cabral - Vila Mariana, São Paulo",
                -23.5872, -46.6572,
                Arrays.asList("plastico", "papel", "vidro", "metal"),
                "(11) 3456-7890",
                "Segunda a Sexta: 8h às 17h",
                userId
        );
        ponto1.setStatus("validado");

        // Ponto 2
        PontoColeta ponto2 = new PontoColeta(
                "Ponto de Coleta Paulista",
                "Av. Paulista, 1578 - Bela Vista, São Paulo",
                -23.5614, -46.6562,
                Arrays.asList("papel", "plastico", "metal"),
                "(11) 2345-6789",
                "Segunda a Sábado: 9h às 18h",
                userId
        );
        ponto2.setStatus("validado");

        // Ponto 3
        PontoColeta ponto3 = new PontoColeta(
                "Coleta Mercadão",
                "Rua da Cantareira, 306 - Centro, São Paulo",
                -23.5410, -46.6299,
                Arrays.asList("organico", "plastico", "papel"),
                "(11) 3214-5678",
                "Terça a Domingo: 6h às 16h",
                userId
        );
        ponto3.setStatus("validado");

        // Ponto 4
        PontoColeta ponto4 = new PontoColeta(
                "Ecoponto Iguatemi",
                "Av. Brg. Faria Lima, 2232 - Jardim Paulistano, São Paulo",
                -23.5798, -46.6870,
                Arrays.asList("eletronicos", "plastico", "metal"),
                "(11) 4567-8901",
                "Segunda a Domingo: 10h às 22h",
                userId
        );
        ponto4.setStatus("validado");

        // Ponto 5
        PontoColeta ponto5 = new PontoColeta(
                "Centro de Reciclagem USP",
                "Av. Prof. Luciano Gualberto - Butantã, São Paulo",
                -23.5596, -46.7312,
                Arrays.asList("plastico", "papel", "vidro", "metal", "eletronicos"),
                "(11) 3091-1234",
                "Segunda a Sexta: 8h às 18h",
                userId
        );
        ponto5.setStatus("validado");

        // Cadastrar todos
        repository.cadastrarPonto(ponto1);
        repository.cadastrarPonto(ponto2);
        repository.cadastrarPonto(ponto3);
        repository.cadastrarPonto(ponto4);
        repository.cadastrarPonto(ponto5)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context,
                                "5 pontos de exemplo cadastrados!",
                                Toast.LENGTH_LONG).show()
                );
    }
}