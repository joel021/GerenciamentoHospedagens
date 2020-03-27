/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador_view.ControladorMain;
import java.util.ArrayList;
import java.util.HashMap;
import modelo.execoes.NaoImplementada;
import modelo.requisicao.UsuarioBanco;
import modelo.requisicao.RequisicaoUsuario;

/**
 *
 * @author Joel
 */
public final class Entrar implements RequisicaoUsuario {
    private String senha, email;
    private Usuario usuario;
    
    private final ControladorMain contexto;
    
    public Entrar(ControladorMain contexto){
        this.contexto = contexto;
    }
    
    // faz a busca no banco de dados para a verificação da entrada
    public void entrar(){
        new Thread(){
            @Override
            public void run(){
                UsuarioBanco banco = new UsuarioBanco();
                banco.getUsuarioWhere(Entrar.this, "email", Entrar.this.email);
            }
        }.start();
    }
    
    // verifica resultadoRU da requisição, essa função é chamada do controle do banco
    @Override
    public void resultadoRU(Usuario usuario){
        if(usuario != null){
            this.usuario = usuario;
            
            if(email.equals(usuario.getEmail()) && senha.equals(usuario.getSenha()) ){

                switch (usuario.getUsuario()) {
                    case Principal.LOCADOR:
                        contexto.crudLocador(usuario);
                        break;
                    case Principal.LOCATARIO:
                        contexto.crudLocatario(usuario);
                        break;
                    case Principal.ADMIN:
                        contexto.crudAdmin(usuario);
                        break;
                }
                return;
            }
            contexto.entrar("Senha incorreta");
            return;
        }
        
        contexto.entrar("Usuario não encontrado");
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void resultadoRU(ArrayList<Usuario> usuarios) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rAtualizacao(boolean agendou) {
        try {
            throw new NaoImplementada();//To change body of generated methods, choose Tools | Templates.
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void rGetAgendamentos(HashMap<Lugar, Espaco> espacos) {
        try {
            throw new NaoImplementada();//To change body of generated methods, choose Tools | Templates.
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }

    
}
