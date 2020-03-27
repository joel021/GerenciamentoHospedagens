/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Joel
 */

public class Lugar implements Comparable<Lugar> {
    private int id, id_espaco; // dias -> dias em que o usuario está ou vai ficar no espaco, se acabou de fazer checkin, vai ficar, se fez checkout, ficou
    private Usuario locatario;
    private String status;
    private Double diaria, dias;

    public double getDias() {
        return dias;
    }

    public void setDias(double dias) {
        this.dias = dias;
    }

    public Double getDiaria() {
        return diaria;
    }

    public void setDiaria(Double diaria) {
        this.diaria = diaria;
    }

    
    public Usuario getLocatario() {
        return locatario;
    }

    
    public void setLocatario(Usuario locatario) {
        this.locatario = locatario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_espaco() {
        return id_espaco;
    }

    public void setId_espaco(int id_espaco) {
        this.id_espaco = id_espaco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
    @Override
    public int compareTo(Lugar t) {
        if(id > t.getId())
            return 1;
        else if(id < t.getId())
            return -1;
        return 0;
    }
    
    
}
