package com.example.proyectopersonal.Entidades;

import java.util.ArrayList;

public class Movie {

    private ArrayList<Genero> listaGeneros;
    private ArrayList<Cast> listaCast;

    private int id;
    private String titulo;
    private String descripcion;
    private String lenguajeOriginal;
    private String popularidad;
    private String poster;
    private String fechaEstreno;
    private String duracion;
    private String fraseRepresentativa;
    private String promedioVotos;
    private String cantidadVotos;

    public ArrayList<Genero> getListaGeneros() {
        return listaGeneros;
    }

    public void setListaGeneros(ArrayList<Genero> listaGeneros) {
        this.listaGeneros = listaGeneros;
    }

    public ArrayList<Cast> getListaCast() {
        return listaCast;
    }

    public void setListaCast(ArrayList<Cast> listaCast) {
        this.listaCast = listaCast;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLenguajeOriginal() {
        return lenguajeOriginal;
    }

    public void setLenguajeOriginal(String lenguajeOriginal) {
        this.lenguajeOriginal = lenguajeOriginal;
    }

    public String getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(String popularidad) {
        this.popularidad = popularidad;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getFraseRepresentativa() {
        return fraseRepresentativa;
    }

    public void setFraseRepresentativa(String fraseRepresentativa) {
        this.fraseRepresentativa = fraseRepresentativa;
    }

    public String getPromedioVotos() {
        return promedioVotos;
    }

    public void setPromedioVotos(String promedioVotos) {
        this.promedioVotos = promedioVotos;
    }

    public String getCantidadVotos() {
        return cantidadVotos;
    }

    public void setCantidadVotos(String cantidadVotos) {
        this.cantidadVotos = cantidadVotos;
    }
}
