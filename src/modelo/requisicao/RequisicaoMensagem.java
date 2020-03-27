/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.requisicao;

import java.util.HashMap;
import modelo.Mensagem;

/**
 *
 * @author Joel
 */
public interface RequisicaoMensagem {
    // resultado envio mensagem
    public void rEnvioMensagem(boolean enviou, int id_mensagem);
    // resultado get mesnagens
    public void rGetMensagens(HashMap<Integer, Mensagem> mensagens);
}
