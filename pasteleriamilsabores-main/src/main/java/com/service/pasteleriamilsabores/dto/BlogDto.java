package com.service.pasteleriamilsabores.dto;

public class BlogDto {
    private String id;
    private String titulo;
    private String resumen;
    private String imagen;
    private String fecha;
    private String autor;
    private String link;

    public BlogDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}
