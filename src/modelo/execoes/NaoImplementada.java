/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.execoes;

/**
 *
 * @author Joel
 */
public class NaoImplementada extends Exception {
    public NaoImplementada(){
        super("Método não implementado!");
    }
    
    public NaoImplementada(String msg){
        super(msg);
    }
}
