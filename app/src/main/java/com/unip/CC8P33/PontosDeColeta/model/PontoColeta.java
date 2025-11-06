package com.unip.CC8P33.PontosDeColeta.model;

import java.util.ArrayList;
import java.util.List;

public class PontoColeta {
    private String id;
    private String nome;
    private String endereco;
    private double latitude;
    private double longitude;
    private List<String> tiposMateriais;
    private String telefone;
    private String horarioFuncionamento;
    private String status; // "pendente", "validado", "rejeitado"
    private String criadoPor;
    private long dataCriacao;
    private String validadoPor;
    private Long dataValidacao;

    // Construtor vazio necessário para Firestore
    public PontoColeta() {
        this.tiposMateriais = new ArrayList<>();
        this.status = "pendente";
        this.dataCriacao = System.currentTimeMillis();
    }

    // Construtor completo
    public PontoColeta(String nome, String endereco, double latitude, double longitude,
                       List<String> tiposMateriais, String telefone, String horarioFuncionamento,
                       String criadoPor) {
        this();
        this.nome = nome;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tiposMateriais = tiposMateriais != null ? tiposMateriais : new ArrayList<>();
        this.telefone = telefone;
        this.horarioFuncionamento = horarioFuncionamento;
        this.criadoPor = criadoPor;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public List<String> getTiposMateriais() { return tiposMateriais; }
    public void setTiposMateriais(List<String> tiposMateriais) {
        this.tiposMateriais = tiposMateriais;
    }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCriadoPor() { return criadoPor; }
    public void setCriadoPor(String criadoPor) { this.criadoPor = criadoPor; }

    public long getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(long dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getValidadoPor() { return validadoPor; }
    public void setValidadoPor(String validadoPor) { this.validadoPor = validadoPor; }

    public Long getDataValidacao() { return dataValidacao; }
    public void setDataValidacao(Long dataValidacao) { this.dataValidacao = dataValidacao; }
}