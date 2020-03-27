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
public class NaoGerou extends Exception {
    
    public NaoGerou(){
    }
    @Override
    public String toString(){
        return "Não gerou um id automático!";
    }
}
