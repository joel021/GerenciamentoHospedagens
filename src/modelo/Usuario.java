/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import controlador_view.ControladorCliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import modelo.requisicao.EspacoBanco;
import modelo.requisicao.LugarBanco;
import modelo.requisicao.MensagemBanco;
import modelo.requisicao.RequisicaoEspaco;
import modelo.requisicao.RequisicaoMensagem;
import modelo.requisicao.RequisicaoUsuario;
import modelo.requisicao.UsuarioBanco;

/**
 *
 * @author Joel
 */
public class Usuario implements RequisicaoUsuario, RequisicaoMensagem, Comparable<Usuario>, RequisicaoEspaco {
    private String nome, email, senha, usuario;
    private ControladorCliente controlador;
    private int id;
    
    // construtores
    public Usuario(){
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
   
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    // envar mensagem a partir de um contado
    public void enviarMensagem(ControladorCliente controller, int id_para, String titulo, String mensagem){
        this.controlador    = controller;
        
        mensagem = "De <"+email+"> \n\n"+mensagem;
        MensagemBanco m     = new MensagemBanco();
        m.adicionarMensagem(this, id_para, titulo, mensagem);
    }
    
    // enviar mensagem para um destinatário que enviou para esse usuário
    public void responderMensagem(ControladorCliente controller, int id, String mensagem){
        this.controlador    = controller;
        MensagemBanco m     = new MensagemBanco();
        
        String men  = "\n \nResposta de ";
        men        = men.concat("<");
        men         = men.concat(email);
        men        = men.concat(">");
        men         = men.concat("\n \n");
        mensagem    = men.concat(mensagem);
        m.atualizarMensagem(this, id, mensagem);
    }
    
    // pega os contatos desse u (nesse projeto, são todos os u mesmo)
    public void getContatos(ControladorCliente controlador){
        this.controlador = controlador;
        UsuarioBanco ub  = new UsuarioBanco();
        
        // pede para enviar todos os usuários
        ub.getUsuarios(this);
    }
    
    // resultado de requisiação para buscar *um* usuario
    @Override
    public void resultadoRU(Usuario usuario) {
        // manda o usuário para o controlador
        assert false : "Nunca usado aqui";
    }

    // resultado de envio de uma mensagem
    @Override
    public void rEnvioMensagem(boolean enviou, int id_mensagem) {
       // faz as operações necessárias caso enviou ou não
       MensagemBanco m = new MensagemBanco();
       // pegar todas as mensagens desse usuário
       m.getMensagens(this, id);
       
       // para esse projeto, não vou tratar o caso em que não enviou a mensagemS
       if(enviou)
           controlador.setMensagem("Menagem enviada com sucesso!");
       else
           controlador.setMensagem("Menagem não enviada...");
    }

    // resultado de requisição de lista de usuarios
    @Override
    public void resultadoRU(ArrayList<Usuario> usuarios) {
        // manda a lista de usuarios pra controlador
        // vetrificações
        controlador.setContatos(usuarios);
    }

    // pega as mensagens desse usuário
    public void getMensagens(ControladorCliente controlador){
        this.controlador    = controlador;
        MensagemBanco m     = new MensagemBanco();
        m.getMensagens(this, this.getId());
    }
    // resultado de requisição de pegar todas as mensagens desse usuário
    @Override
    public void rGetMensagens(HashMap<Integer, Mensagem> mensagens) {
        ArrayList<Mensagem> m = new ArrayList<>();;        
        
        if(mensagens != null){
            Iterator<Mensagem> it = mensagens.values().iterator();
            while(it.hasNext())
                m.add(it.next());
        }
        
        controlador.setMensagens(m);
    }
    
    // seta "aguardo" em algum lugar de espaco
    public void reservar(ControladorCliente controlador, int id_espaco, String q_pessoas, String q_dias){
        this.controlador = controlador;
        int n_lugares;
        double dias;
        
        try{
            n_lugares   = Integer.parseInt(q_pessoas);
            dias        = Integer.parseInt(q_dias);
        }catch(NumberFormatException e){
            controlador.setMensagem("Números inválidos para lugares ou dias!");
            rAtualizacao(false);
            return;
        }
        
        new Thread () {
            @Override
            public void run(){
                // buscar na base de dados esse espaco
                EspacoBanco eb  = new EspacoBanco();
                Espaco espaco   = eb.getEspacoWhere(id_espaco);
                
                // se não haver espaco, não há o que fazer
                if(espaco == null){
                    rAtualizacao(false);
                    return;
                }else if(espaco.getLugaresLivres() - n_lugares < 0){
                    controlador.setMensagem("Desculpe, o espaço não tem lugares suficientes");
                    rAtualizacao(false);
                    return;
                }
                
                // pegar um lugar vazio
                LugarBanco lB               = new LugarBanco();
                HashSet<Lugar> lugares      = lB.getLugaresWhere(id_espaco); // pega n_lugares onde o ide_espaco é igual a esse
                
                String ids_lugar    = "0"; // nao existe lugar com id zero
                int i               = 0;
                
                // pegar todos os n_lugares livres
                for(Lugar lugar: lugares){
                    if(lugar.getStatus().equals(Principal.LIVRE)){
                        if(i == 0){
                            ids_lugar = ""+lugar.getId();
                        }else{
                            ids_lugar += ","+lugar.getId();
                        }
                        i++;
                    }
                }
                
                if(i == n_lugares){
                    
                    lB          = new LugarBanco();
                    boolean a   = lB.atualizarLugares(ids_lugar, id, dias, Principal.AGUARDO);
                                        
                    if(!a){
                        controlador.setMensagem("Não foi possível fazer reserva!");
                        rAtualizacao(false);
                        return;
                    }
                }else{
                    /// exitem lugares, mas não estavam no banco de dados! então adiciona esses que faltam
                    lB              = new LugarBanco();
                    String ids      = lB.adiconarLugares(id_espaco, espaco.getDiaria(), Principal.AGUARDO, id, n_lugares -i);
                    
                    // atualiza os lugares encontrados
                    if(i != 0){
                        lB              = new LugarBanco();
                        boolean a       = lB.atualizarLugares(ids_lugar, id, dias, Principal.AGUARDO);
                        if(!a){
                            controlador.setMensagem("Não foi possível fazer reserva!");
                            rAtualizacao(false);
                            return;
                        }
                    }
                    
                }
                
                eb = new EspacoBanco();
                 // seta mais lugar ocupado
                eb.atualizarEspaco(Usuario.this, id_espaco, +n_lugares);
                
            }
        }.start();
            
    }
    
    
    // esta função faz che
    public void checkIn(ControladorCliente controlador, int id_lugar){
        System.out.println("id_lugar = "+id_lugar);
        this.controlador = controlador;
        // ele vai ter que verificar se o espaco é dele mesmo
        new Thread () {
            @Override
            public void run(){
                LugarBanco lB   = new LugarBanco();
                
                // só dá cehckin se estiver reservado
                Lugar lugar = lB.getLugarWhere(id_lugar);

                if(lugar.getStatus().equals(Principal.RESERVADO) && lugar.getLocatario().getId() == id){
                    boolean a       = lB.atualizarLugar(id_lugar, id, Principal.CHECKIN); // retorna a quantide de itens atualizados
                    // se setou, termina
                    if(a){
                        rAtualizacao(true);
                        return;
                    }
                }else
                    rAtualizacao(false);
                
            }
        }.start();
    }
    
    public void checkOut(ControladorCliente controlador, int id_lugar){
        this.controlador = controlador;
        
        // ele vai ter que verificar se o espaco é dele mesmo
        new Thread () {
            @Override
            public void run(){
                LugarBanco lB   = new LugarBanco();
                boolean a       = lB.atualizarLugar(id_lugar, 0, Principal.LIVRE); // retorna a quantide de itens atualizados

                rAtualizacao(a);
            }
        }.start();
        
    }
    @Override
    public void rAtualizacao(boolean atualizou){
        // manda para o controlador usuario
        getAgendamentos(controlador);
        
        if(!atualizou)
            controlador.setMensagem("Não foi possível fazer atualização!");
        else
            controlador.setMensagem("Atualizado com sucesso!");
    }
    
    public void getAgendamentos(ControladorCliente controlador){
        this.controlador    = controlador;
        LugarBanco lb       = new LugarBanco();
        lb.getAgendamentos(this);
    }

    // alguém agenda um lugar, não o espaco todo
    @Override
    public void rGetAgendamentos(HashMap<Lugar,Espaco> lugares) {
        controlador.setAgendamentos(lugares);
    }
    
    // buscar espacos
    public void buscar(ControladorCliente controlador, String pesquisa){
        this.controlador = controlador;
        EspacoBanco eb = new EspacoBanco();
        eb.getEspacosWhere(this, pesquisa);
    }
    
    @Override
    public void rBuscar(ArrayList<Espaco> espacos){
        controlador.setREspacosP(espacos);
    }

    @Override
    public int compareTo(Usuario t) {
        if(id > t.getId())
            return 1;
        else if(id < t.getId())
            return -1;
        return 0;
    }

    @Override
    public void rAdicionarE(boolean adicionou, int id) {
        assert false : "Todos os filhos irão implementar";
    }

    @Override
    public void rGetEspacosWhere(ArrayList<Espaco> espacos) {
        assert false : "Todos os filhos irão implementar";
    }

    @Override
    public void rAtualizarE(boolean atualizou) {
        System.out.println("rAtualizarE em Usuario");
        assert false : "Todos os filhos irão implementar";
    }

}
