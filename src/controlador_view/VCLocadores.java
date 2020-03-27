/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador_view;

import java.util.ArrayList;
import java.util.HashMap;
import modelo.Espaco;
import modelo.Lugar;
import modelo.Mensagem;
import modelo.Usuario;

/**
 *
 * @author Joel
 */
public class VCLocadores extends ControladorCliente {

    @Override
    public ArrayList<Espaco> getRPesquisa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setContatos(ArrayList<? extends Usuario> contatos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMensagens(ArrayList<Mensagem> mensagens) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAgendamentos(HashMap<Lugar, Espaco> agendamentos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setREspacosP(ArrayList<Espaco> espacos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setEspacosLocador(ArrayList<Espaco> espacos){
        assert false : "Todos os filhos irão implementar";
    }
    
    public void setLucro(String lucro){
        
    }
}
