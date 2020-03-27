/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.requisicao;

import modelo.banco.Conectar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Mensagem;
import modelo.Usuario;
import modelo.execoes.NaoAtualizou;

/**
 *
 * @author Joel
 */
public final class MensagemBanco {
    private Connection conexao;
    
    public MensagemBanco(){
        this.conexao = new Conectar().getConexao();
    }
    
    // de, para  e mensagem
    public void adicionarMensagem(Usuario contexto, int id_para, String titulo, String mensagem){
        if(conexao == null){
            contexto.rEnvioMensagem(false, 0);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt = null;
                try{
                    stmt = conexao.prepareStatement("INSERT INTO mensagem (id_de, id_para, mensagem, titulo) VALUES(?,?,?, ?)", Statement.RETURN_GENERATED_KEYS);

                    stmt.setInt(1, contexto.getId());
                    stmt.setInt(2, id_para);
                    stmt.setString(3, mensagem);
                    stmt.setString(4, titulo);
                    // executa a query
                    stmt.executeUpdate();

                    // pega o resultadoRU da chave gerada automaticamente
                    ResultSet rs = stmt.getGeneratedKeys();

                    if (rs.next()) {
                        contexto.rEnvioMensagem(true, rs.getInt(1));
                    }else{
                        contexto.rEnvioMensagem(false, 0);
                    }
                    stmt.close();
                }catch(SQLException e){

                    throw new RuntimeException(e);

                }finally {
                    
                    

                }
            }
        }.start();
    }
    
    // para mensagens do systema
    public boolean adicionarMensagem(int id_para, String titulo, String mensagem){
        if(conexao == null){
            return false;
        }
        PreparedStatement stmt = null;
        try{
            stmt = conexao.prepareStatement("INSERT INTO mensagem (id_de, id_para, mensagem, titulo) VALUES(?,?,?, ?)", Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, 1);
            stmt.setInt(2, id_para);
            stmt.setString(3, mensagem);
            stmt.setString(4, titulo);
            // executa a query
            stmt.executeUpdate();
            stmt.close();
        }catch(SQLException e){
            e.printStackTrace();
            try {
                if(stmt != null)
                    stmt.close();
                conexao.close();
            } catch (SQLException ex) {
                System.out.println("Erro ao fechar a conexão");
                Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }finally {

            try {
                if(stmt != null)
                    stmt.close();
                conexao.close();
            } catch (SQLException ex) {
                System.out.println("Erro ao fechar a conexão");
                Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        return true;
    }
    
// pegar todas as mensagens que tiver o id do cara
    public void getMensagens(RequisicaoMensagem contexto, int id){
        if(conexao == null){
            contexto.rGetMensagens(new HashMap<>());
        }
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt = null;
                HashMap<Integer, Mensagem>   mensagens = new HashMap<>();
                try{
                    // prepara a conexão
                    String sql = "SELECT * FROM mensagem WHERE id_para = ?";
                    stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, id);

                    // resultado da requisicão
                    ResultSet rs = stmt.executeQuery();
                    // lista de mensagens desse usuario
                    
                    UsuarioBanco ub;
                    while(rs.next()){
                        ub          = new UsuarioBanco();
                        Mensagem m  = new Mensagem();
                        m.setId(rs.getInt("id"));
                        m.setDe(ub.getWhere(rs.getInt("id_de")));
                        
                        ub = new UsuarioBanco();
                        m.setPara(ub.getWhere(rs.getInt("id_para")));
                        m.setMensagem(rs.getString("mensagem"));
                        m.setTitulo(rs.getString("titulo"));
                        mensagens.put(m.getId(), m);
                        
                    }
                    //rs.close();
                   //pegar o resto
                   
                    sql = "SELECT * FROM mensagem WHERE id_de = ?";
                    stmt = conexao.prepareStatement(sql);
                    stmt.setInt(1, id);
                    // resultado da requisicão
                    ResultSet rs2 = stmt.executeQuery();
                    while(rs2.next()){
                        ub          = new UsuarioBanco();
                        // se o id da mensagem não for o mesmo
                        
                        if(!mensagens.containsKey(rs2.getInt("id"))){
                            Mensagem m  = new Mensagem();
                            m.setId(rs2.getInt("id"));
                            m.setDe(ub.getWhere(rs2.getInt("id_de")));

                            ub = new UsuarioBanco();
                            m.setPara(ub.getWhere(rs2.getInt("id_para")));
                            m.setMensagem(rs2.getString("mensagem"));
                            m.setTitulo(rs2.getString("titulo"));
                            
                            mensagens.put(m.getId(), m);
                        }
                        
                    }
                }catch(SQLException e){
                     contexto.rGetMensagens(mensagens);
                    //throw new RuntimeException(e);
                    e.printStackTrace();
                }finally {
                    
                    try {
                        if(stmt != null)
                            stmt.close();
                        conexao.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    contexto.rGetMensagens(mensagens);
                }
            }
        }.start();
    }
    
    public void atualizarMensagem(RequisicaoMensagem contexto, int id, String mensagem){
        if(conexao == null){
            contexto.rEnvioMensagem(false, 0);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt = null;
                try{
                    stmt = conexao.prepareStatement("UPDATE mensagem SET mensagem = CONCAT(mensagem, '"+mensagem+"') WHERE id = "+id+"");
                    
                    if(stmt.executeUpdate() != 1){
                        throw new NaoAtualizou();
                    }
                    contexto.rEnvioMensagem(true, id);
                    
                }catch(SQLException | NaoAtualizou e){
                    e.printStackTrace();
                    contexto.rEnvioMensagem(false, 0);
                }finally {
                    
                    fecharDB(stmt);
                }
            }
        }.start();
        
    }
    
    private void fecharDB(PreparedStatement stmt){
        try {
            if(stmt != null)
                stmt.close();
            conexao.close();
        } catch (SQLException ex) {
            Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
