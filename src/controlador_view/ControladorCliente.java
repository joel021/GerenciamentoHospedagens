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
 * @author aluno
 */
public abstract class ControladorCliente extends VC {

    //retorna a lista de espacos obtidos na pesquisa por endereço
    public abstract ArrayList<Espaco> getRPesquisa();
    //insere na lista de contatos para encher o Jlist
    public abstract void setContatos(ArrayList <? extends Usuario> contatos);
    
    // coloca as mensagens na view
    public abstract void setMensagens(ArrayList<Mensagem> mensagens);
    
    // coloca os agendamentos na view
    public abstract void setAgendamentos(HashMap<Lugar, Espaco> agendamentos);
    
    // coloca os espacos procurados na lista de espacos de pesquisa
    public abstract void setREspacosP(ArrayList<Espaco> espacos);
}
