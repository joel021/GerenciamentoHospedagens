/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.requisicao;

import java.util.ArrayList;
import modelo.Espaco;

/**
 *
 * @author Joel
 */
public interface RequisicaoEspaco {
    
    // resultado da requisição de adicionar o espaco, toda vez que o processo de adicionar espaco banco de dados
    public void rAdicionarE(boolean adicionou, int id);
    // resultado de alguma 
    public void rGetEspacosWhere(ArrayList<Espaco> espacos);
    
    // resultado de requisição de atualizar, remover, ou qualquer tipo de atualização na tabela espaco no banco de dados
    public void rAtualizarE(boolean atualizou);
    
    // rsultado de busca de um espaço
    public void rBuscar(ArrayList<Espaco> espacos);
}
