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

public class Mensagem implements Comparable <Mensagem> {
    private int id;
    private Usuario de, para;
    private String mensagem, titulo;

    public Usuario getDe() {
        return de;
    }

    public void setDe(Usuario de) {
        this.de = de;
    }

    public Usuario getPara() {
        return para;
    }

    public void setPara(Usuario para) {
        this.para = para;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // ordena para as mais recentes primeiro
    @Override
    public int compareTo(Mensagem t) {
        if(this.id > t.getId())
            return 1;
        else if(id < t.getId())
            return -1;
        else
            return 0;
    }
    
}
