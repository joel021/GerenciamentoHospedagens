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
public class ValoresNegativos extends Exception {
    @Override
    public String toString(){
        return "Valores negativos para qauntidade de vagas ou para a diária!";
    }
}
