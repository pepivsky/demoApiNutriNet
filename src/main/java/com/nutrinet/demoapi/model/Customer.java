package com.nutrinet.demoapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Document
//@CompoundIndex(def = "{'nombreUsuario':1, 'email':1}", unique = true)
//@Component
public class Customer {
    @Id
    private String id;
   // @Indexed(unique = true)
    private String nombreUsuario;
    private String nombre;
    private String apellidos;
    //@Indexed(unique = true)
    private String email;
    private Integer edad;
    private Double estatura;
    private Double peso;
    //private Double sexo;
    private Double imc;
    private Double geb;
    private Double eta;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Customer() {
        fechaCreacion = LocalDateTime.now();
        //imc = peso / Math.pow(estatura, 2);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Double getEstatura() {
        return estatura;
    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Double getGeb() {
        return geb;
    }

    public void setGeb(Double geb) {
        this.geb = geb;
    }

    public Double getEta() {
        return eta;
    }

    public void setEta(Double eta) {
        this.eta = eta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /*public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }*/

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
