/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.requisicao;

import java.util.ArrayList;
import java.util.HashMap;
import modelo.Espaco;
import modelo.Lugar;
import modelo.Usuario;

/**
 *
 * @author Joel
 */
public interface RequisicaoUsuario {
    
    // resultado de requisição de um usuário no banco
    public void resultadoRU(Usuario usuario);
    
    // resultado de requisição de vários usuários
    public void resultadoRU(ArrayList<Usuario> usuarios);
    
    // resultado do pedido de atualização de alguma coisa
    public void rAtualizacao(boolean agendou);
    
    // resultado de pedido da lista de lugares agendados por mim
    public void rGetAgendamentos(HashMap<Lugar, Espaco> lugares);
    
    
}
