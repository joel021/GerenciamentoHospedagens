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
 * @author aluno
 */
public final class Cadastrar implements RequisicaoUsuario {
    private String snome, semail, ssenha, susuario;
    private final ControladorMain contexto;
    
    public Cadastrar(ControladorMain contexto){
        this.contexto = contexto;
    }
    
    // verificação se o usuario já não existe
    @Override
    public void resultadoRU(Usuario usuario){
        if(usuario != null){
            // o usuario já existe!
            contexto.cadastrar("Usuario já existe!"); // envia a tela novamente
        } else {
            // pode cadastrar            
            UsuarioBanco banco = new UsuarioBanco();
            banco.adicionar(snome, semail, ssenha, this.susuario, this);
        }
    }
    
    // resultadoRU do cadastro
    public void resultadoCadastro(boolean cadastrou, int id){
        if(cadastrou){
            // tenta entrar ué
            Entrar e = new Entrar(contexto);
            e.setEmail(semail);
            e.setSenha(ssenha);
            e.entrar();
        }else{
            contexto.cadastrar("Não foi possível cadastrar, tente novamente!");
        }
    }
    public void cadastrar(){
        new Thread(){
            @Override
            public void run(){
                UsuarioBanco banco = new UsuarioBanco();
                banco.getUsuarioWhere(Cadastrar.this, "email", Cadastrar.this.semail);
            }
        }.start();
    }
   
    public void setSnome(String snome) {
        this.snome = snome;
    }

    public void setSemail(String semail) {
        this.semail = semail;
    }

    public void setSsenha(String ssenha) {
        this.ssenha = ssenha;
    }
    public void setSusuario(String susuario){
        this.susuario = susuario;
    }

    @Override
    public void resultadoRU(ArrayList<Usuario> usuarios) {
        try {
            throw new NaoImplementada();
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void rAtualizacao(boolean agendou) {
        try {
            throw new NaoImplementada();
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void rGetAgendamentos(HashMap<Lugar, Espaco> espacos) {
        try {
            throw new NaoImplementada();
        } catch (NaoImplementada ex) {
            ex.printStackTrace();
        }
    }
    
}
