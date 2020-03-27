/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Joel
 */
public class Espaco implements Comparable<Espaco> {
    private int id, nLugares;
    private Usuario locador;
    private String endereco, tipo;
    private double diaria;
    // todos os lugares que estão vinculados a este espaco, independente do status
    private HashSet<Lugar> lugares; //  lugares vai estar preenchida pelo menos com lugares reservados para frente.

    public Espaco(){
        lugares = new HashSet<>();
    }
    
    public int getnLugares() {
        return nLugares;
    }

    public void setnLugares(int nLugares) {
        this.nLugares = nLugares;
    }

    public HashSet<Lugar> getLugares() {
        return lugares;
    }

    public void setLugares(HashSet<Lugar> lugares) {
        this.lugares = lugares;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDiaria() {
        return diaria;
    }

    public void setDiaria(double diaria) {
        this.diaria = diaria;
    }
    
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    

    public Usuario getLocador() {
        return locador;
    }

    public void setLocador(Locador locador) {
        this.locador = locador;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int compareTo(Espaco t) {
        return t.getEndereco().compareTo(endereco);
    }
    
    // retorna a comissão confirmada ao locador
    public double getComissao(){
        double total = 0.0;
        
        for(Lugar e: getLugaresOcupados()){
            total += e.getDias()*diaria*Principal.PROPORCAO_EMBOLSO;
        }
        
        return total;
    }
    // retorna todos lugares que estão com status "aguardo" desse Espaço
    public ArrayList<Lugar> getEmAguardo(){
        return getLugares(Principal.AGUARDO);
    }
    
    public ArrayList<Lugar> getLugaresReservados(){
        
        return getLugares(Principal.RESERVADO);
    }
    
    public ArrayList<Lugar> getLugaresOcupados(){
        return getLugares(Principal.CHECKIN);
    }
    
    private ArrayList<Lugar> getLugares(String tag){
        
        ArrayList<Lugar> lista_lugares = new ArrayList<>();
        
        if(lugares != null){
            lista_lugares = new ArrayList<>();
            for(Lugar l: lugares){
                if(l.getStatus().equals(tag)){
                    lista_lugares.add(l);
                }
            }
            
        }
        return lista_lugares;
    }
    
    public int getLugaresLivres(){
        return nLugares - (getLugaresReservados().size() + getLugaresOcupados().size());
    }
}
