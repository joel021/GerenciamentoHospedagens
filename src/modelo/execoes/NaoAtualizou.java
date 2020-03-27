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
public class NaoAtualizou extends Exception {
    @Override
    public String toString(){
        return "Não atualizou o banco de dados!";
    }
}
