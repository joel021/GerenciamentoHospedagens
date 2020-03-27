/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador_view.ControladorMain;
import java.util.ArrayList;
import modelo.execoes.NaoImplementada;
import modelo.requisicao.EspacoBanco;
import modelo.requisicao.RequisicaoEspaco;

/**
 *
 * @author Joel
 */
public final class Principal implements RequisicaoEspaco {

    private ControladorMain controlador;
    public static final String DB_USER = "sql10299828", DB_PASS = "lkPT1zavEK", CHECKIN = "Checkin", ADMIN = "Admin", LOCADOR = "Locador", LOCATARIO = "Locatario", RESERVADO = "Reservado", LIVRE = "Livre", AGUARDO = "Aguardo";
    public static final double PROPORCAO_EMBOLSO = 0.5;
    
    public Principal(){
        
    }
    
    public void getEspacosWhere(ControladorMain controlador, String tag){
        this.controlador    = controlador;
        EspacoBanco ub      = new EspacoBanco();
        ub.getEspacosWhere(this, tag);
    }

    @Override
    public void rAdicionarE(boolean adicionou, int id) {
        try {
            throw new NaoImplementada(); //To change body of generated methods, choose Tools | Templates.
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void rGetEspacosWhere(ArrayList<Espaco> espacos) {
        try {
            throw new NaoImplementada(); //To change body of generated methods, choose Tools | Templates.
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void rAtualizarE(boolean atualizou) {
        try {
            throw new NaoImplementada(); //To change body of generated methods, choose Tools | Templates.
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void rBuscar(ArrayList<Espaco> espacos) {
        controlador.setREspacosP(espacos);
    }
    
    
}
