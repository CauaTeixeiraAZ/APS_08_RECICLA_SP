package com.unip.CC8P33.PontosDeColeta.model;

public class Usuario {
    private String uid;
    private String nome;
    private String email;
    private String tipo; // "cidadao" ou "admin"
    private long dataCriacao;

    // Construtor vazio necessário para Firestore
    public Usuario() {
        this.tipo = "cidadao";
        this.dataCriacao = System.currentTimeMillis();
    }

    public Usuario(String uid, String nome, String email, String tipo) {
        this();
        this.uid = uid;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    // Getters e Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public long getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(long dataCriacao) { this.dataCriacao = dataCriacao; }

    public boolean isAdmin() {
        return "admin".equals(this.tipo);
    }
}