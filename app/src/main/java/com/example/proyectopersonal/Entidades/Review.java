package com.example.proyectopersonal.Entidades;

public class Review {

    private String author;
    private String content;
    private String id;
    private String url;
    private String idPelicula;
    private String nombreRaro;
    private String nombrePelícula;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getNombreRaro() {
        return nombreRaro;
    }

    public void setNombreRaro(String nombreRaro) {
        this.nombreRaro = nombreRaro;
    }

    public String getNombrePelícula() {
        return nombrePelícula;
    }

    public void setNombrePelícula(String nombrePelícula) {
        this.nombrePelícula = nombrePelícula;
    }
}
