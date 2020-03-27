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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Cadastrar;
import modelo.Usuario;

/**
 *
 * @author Joel
 */
public final class UsuarioBanco {
    private final Connection conexao;
    
    public UsuarioBanco(){
        this.conexao = new Conectar().getConexao();
    }
    
    //adicionar um usuario
    public void adicionar(String nome, String email, String senha, String usuario, Cadastrar cadastrar){
        if(conexao == null){
            cadastrar.resultadoCadastro(false, 0);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt = null;
                try{
                    stmt = conexao.prepareStatement("INSERT INTO usuario (nome, email, senha, tipo_usuario) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

                    stmt.setString(1, nome);
                    stmt.setString(2, email);
                    stmt.setString(3, senha);
                    stmt.setString(4, usuario);
                    // executa a query
                    stmt.executeUpdate();

                    // pega o resultadoRU da chave gerada automaticamente
                    ResultSet rs = stmt.getGeneratedKeys();

                    if (rs.next()) {
                        cadastrar.resultadoCadastro(true, rs.getInt(1));
                    }else{
                        cadastrar.resultadoCadastro(false, 0);
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                    cadastrar.resultadoCadastro(false, 0);

                }finally {
                    
                    try {
                        if(stmt != null)
                            stmt.close();
                        conexao.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }.start();
    }
    
// pegar usuario por característica igual a nome_característica
    public void getUsuarioWhere(RequisicaoUsuario contexto, String c1, String nome_c1){
        if(conexao == null){
            Usuario u = null;
            contexto.resultadoRU(u);
            return;
        }
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt = null;
                try{
                    String sql = "SELECT * FROM usuario WHERE "+c1+" = ?";
                    stmt = conexao.prepareStatement(sql);
                    
                    stmt.setString(1, nome_c1);

                    ResultSet rs = stmt.executeQuery();
                    
                    if(rs.next()){
                        Usuario u = new Usuario();
                        u.setNome(rs.getString("nome"));
                        u.setEmail(rs.getString("email"));
                        u.setSenha(rs.getString("senha"));
                        u.setUsuario(rs.getString("tipo_usuario"));
                        u.setId(rs.getInt("id"));
                        
                        // finaliza
                        contexto.resultadoRU(u);
                    }else{
                        
                        Usuario u = null;
                        contexto.resultadoRU(u);
                    }
                }catch(SQLException e){
                     
                     e.printStackTrace();
                     Usuario u = null;
                     contexto.resultadoRU(u);
                }finally {
                    
                    try {
                        if(stmt != null)
                            stmt.close();
                        conexao.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }.start();
    }
    
    // retorna um usuario, onde o id tem que o passado no argumento. nnulo se der errro ou nao encontrar
    public Usuario getWhere(int id){
        if(conexao == null){
            return null;
        }
        PreparedStatement stmt  = null;
        Usuario u              = null;
        try{
            String sql = "SELECT * FROM usuario WHERE id = ?";
            stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                u = new Usuario();
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setUsuario(rs.getString("tipo_usuario"));
                u.setId(rs.getInt("id"));
            }else{
                System.out.println("UsuarioBanco.getWhere(): 154 : Não foi encontrado um usuário com esse id");
            }
        }catch(SQLException e){
            
            e.printStackTrace();

        }finally {
                    
            try {
                if(stmt != null)
                    stmt.close();
                conexao.close();
            } catch (SQLException ex) {
                Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        return u;
    }
    
    // envia ao invéz de retornar
    public void getUsuarioWhere(RequisicaoUsuario contexto, int id){
        if(conexao == null){
            Usuario u = null;
            contexto.resultadoRU(u);
        }
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt = null;
                try{
                    stmt = conexao.prepareStatement("SELECT * FROM usuario WHERE id = ?");
                    stmt.setInt(1, id);

                    ResultSet rs = stmt.executeQuery();
                    
                    if(rs.next()){
                        Usuario u = new Usuario();
                        u.setNome(rs.getString("nome"));
                        u.setEmail(rs.getString("email"));
                        u.setSenha(rs.getString("senha"));
                        u.setUsuario(rs.getString("tipo_usuario"));
                        u.setId(rs.getInt("id"));
                        
                        // finaliza
                        contexto.resultadoRU(u);
                    }else{
                        Usuario u = null;
                        contexto.resultadoRU(u);
                    }
                }catch(SQLException e){

                    Usuario usuario = null;
                    contexto.resultadoRU(usuario);
                    
                    e.printStackTrace();

                }finally {
                    
                    try {
                        if(stmt != null)
                            stmt.close();
                        conexao.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }.start();
    }
    
    // envia ao invéz de retornar
    public void getUsuarios(RequisicaoUsuario contexto){
        if(conexao == null){
            contexto.resultadoRU(new Usuario());
            return;
        }
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt      = null;
                ArrayList<Usuario> usuarios = new ArrayList<>();
                try{
                    // preparaa a conexão
                    stmt = conexao.prepareStatement("SELECT * FROM usuario");

                    // prepara  executa
                    ResultSet rs = stmt.executeQuery();
                    
                    while(rs.next()){
                        Usuario u = new Usuario();
                        u.setNome(rs.getString("nome"));
                        u.setEmail(rs.getString("email"));
                        u.setSenha(rs.getString("senha"));
                        u.setUsuario(rs.getString("tipo_usuario"));
                        u.setId(rs.getInt("id"));
                        
                        // finaliza
                        usuarios.add(u);
                    }
                    contexto.resultadoRU(usuarios);
                }catch(SQLException e){
                    
                    contexto.resultadoRU(usuarios);
                    
                    e.printStackTrace();
                }finally {
                    
                    try {
                        if(stmt != null)
                            stmt.close();
                        conexao.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(EspacoBanco.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }.start();
    }
}
