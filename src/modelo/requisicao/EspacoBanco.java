/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.requisicao;

import modelo.banco.Conectar;
import modelo.execoes.NaoAtualizou;
import modelo.execoes.NaoGerou;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import modelo.Espaco;
import modelo.Locador;
import modelo.Lugar;
import modelo.Principal;
import modelo.Usuario;

/**
 *
 * @author Joel
 */

// faz todas as requisições ao banco de dados referente a espacos
public final class EspacoBanco {
    private final Connection connection;
    
    public EspacoBanco(){
        this.connection = new Conectar().getConexao();
    }
    // remove um espaco do banco, para remover um espaço de umbanco, tem de remover os lugares do cadastro também!
    public void remover(Locador contexto, int id){
        
        // queria verificar se esse espaço realmente é do locador, depois remover
        new Thread () {
            @Override
            public void run(){
                PreparedStatement pstmt = null;
                if(connection != null){
                    try {

                        pstmt = connection.prepareStatement("DELETE FROM espaco WHERE id = ?");

                        pstmt.setInt(1, id);

                        // executa a query
                        pstmt.executeUpdate();

                        LugarBanco lB = new LugarBanco();

                        if(lB.removerLugares(id))
                            contexto.rAtualizarE(true);
                        else
                            contexto.rAtualizarE(false);

                    }catch(SQLException e){
                        contexto.rAtualizarE(false);
                        // lancar erro para eu ver e consertar
                        e.printStackTrace();
                    }finally {

                        // caso dê algum erro, o fechamento será depois
                        fecharDB(pstmt);

                    }
                }else
                    contexto.rAtualizarE(false);
                
            }
        }.start();
    }
    
    private void fecharDB(PreparedStatement pstmt){
        try {
            if(pstmt != null)
                pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    // adiciona um espaco, note que ele tem vários espacos dentro dele, caso lugares for maor que 1
    public void adicionar(Locador contexto, Espaco espaco){
        new Thread () {
            @Override
            public void run(){
                PreparedStatement pstmt = null;
                if(connection != null){
                    try{
                        pstmt = connection.prepareStatement("INSERT INTO espaco (lugares, diaria, tipo, endereco, id_locador) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                        pstmt.setInt(1, espaco.getnLugares());
                        pstmt.setDouble(2, espaco.getDiaria());
                        pstmt.setString(3, espaco.getTipo());
                        pstmt.setString(4, espaco.getEndereco());
                        pstmt.setInt(5, contexto.getId());
                        // executa a query
                        pstmt.execute();
                        // pega o resultadoRU da chave gerada automaticamente
                        ResultSet rs = pstmt.getGeneratedKeys();

                        if (rs.next()) {
                            // se adicionou, tenta aí adicionar os lugares no banco
                            LugarBanco lb = new LugarBanco();
                            lb.adiconarLugares(rs.getInt(1), espaco.getDiaria(), Principal.LIVRE, 0, espaco.getnLugares());
                            contexto.rAdicionarE(true, rs.getInt(1));
                            return;
                        }else{
                            throw new NaoGerou();
                        }

                    }catch(SQLException | NaoGerou e){
                        // lancar erro para eu ver e consertar
                        contexto.rAdicionarE(false, -1);
                        e.printStackTrace();
                    } finally {

                        fecharDB(pstmt);

                    }
                }else
                    contexto.rAdicionarE(false, -1);
                
            }
        }.start();
    }
    

// só chamada por uma thread secundária!
// pega os espacos, não os lugares
    private ArrayList<Espaco> espacos(PreparedStatement stmt) throws SQLException{
        ResultSet rs = stmt.executeQuery();
                    
        // cria a lista para mandar para o locador que pediu
        ArrayList<Espaco> espacos = new ArrayList<>();
        
        while(rs.next()){
            // adiciona os atributos da lista
            Espaco e = new Espaco();
            e.setDiaria(rs.getDouble("diaria"));
            e.setEndereco(rs.getString("endereco"));
            e.setId(rs.getInt("id"));
            e.setTipo(rs.getString("tipo"));
            e.setnLugares(rs.getInt("lugares"));
            
            // pegar locador
            UsuarioBanco ub = new UsuarioBanco();
            Usuario us      = ub.getWhere(rs.getInt("id_locador"));
            
            Locador l = new Locador();
            l.setEmail(us.getEmail());
            l.setId(us.getId());
            l.setNome(us.getNome());
            l.setSenha(us.getSenha());
            l.setUsuario(us.getUsuario());
               
            e.setLocador(l);
            
            // pega todos os lugares desse espaco
            LugarBanco lb           = new LugarBanco();
            HashSet<Lugar> lugares  = lb.getLugaresWhere(e.getId());
            
            e.setLugares(lugares);
            // adiciona o espaco
            espacos.add(e);

        }
        
        return espacos;
    }
    
    // pegar os espacos de um locador em específico
    // é preciso implementar o método rGetEspacosWhere()
    public void getEspacosWhere(Locador contexto){
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt = null;
                
                if(connection != null){
                    try {
                        stmt = connection.prepareStatement("SELECT * FROM espaco WHERE id_locador = ?");

                        stmt.setInt(1, contexto.getId());

                        ArrayList<Espaco> espacos = espacos(stmt);
                        contexto.rGetEspacosWhere(espacos);
                    }catch(SQLException e){
                        // retorna para continuar o fluxo

                        // avisa que não foi possível
                        MensagemBanco mb = new MensagemBanco();
                        mb.adicionarMensagem(contexto.getId(), "Pegar espacos", "Não responda, mas não foi possível obter seus espacos.");
                        e.printStackTrace();

                        contexto.rGetEspacosWhere(new ArrayList<>());
                    }finally {

                       fecharDB(stmt);
                    }
                }else{
                    // avisa que não foi possível
                        MensagemBanco mb = new MensagemBanco();
                        mb.adicionarMensagem(contexto.getId(), "Pegar espacos", "Não responda, mas não foi possível obter seus espacos.");

                        contexto.rGetEspacosWhere(new ArrayList<>());
                }
                    
                
            }
        }.start();
    }
    
    // esse recebe um usuario, é designado a buscas feitas por todos os tipos de usuarios
    public void getEspacosWhere(RequisicaoEspaco contexto, String tag){
        new Thread() {
            @Override
            public void run() {
                PreparedStatement stmt      = null;
                if(connection != null){
                    try{
                        stmt = connection.prepareStatement("SELECT * FROM espaco WHERE endereco LIKE '%"+tag+"%'");
                        ArrayList<Espaco> espacos   = espacos(stmt);

                        // envia para quem pediu
                        contexto.rBuscar(espacos);

                    }catch(SQLException e){
                        contexto.rBuscar(new ArrayList<>());
                        e.printStackTrace();
                    } finally {

                        fecharDB(stmt);

                    }
                }else
                    contexto.rBuscar(new ArrayList<>());
            }
        }.start();
    }
    
    // para chamar esse método tem de ser de uma thread secundária, requisião pesada!
    public Espaco getEspacoWhere(int id){
        if(connection == null){
            return null;
        }
        
        PreparedStatement stmt  = null;
        Espaco e                = null;
        
        try{
            stmt = connection.prepareStatement("SELECT * FROM espaco WHERE id = ?");
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                e = new Espaco();
                e.setDiaria(rs.getDouble("diaria"));
                e.setEndereco(rs.getString("endereco"));
                e.setId(rs.getInt("id"));
                e.setTipo(rs.getString("tipo"));
                e.setnLugares(rs.getInt("lugares"));

                // pegar o locador
                UsuarioBanco ub = new UsuarioBanco();
                Usuario u2      = ub.getWhere(rs.getInt("id_locador"));
                
                Locador l = new Locador();
                l.setEmail(u2.getEmail());
                l.setId(u2.getId());
                l.setNome(u2.getNome());
                l.setSenha(u2.getSenha());
                l.setUsuario(u2.getUsuario());
                
                // adiciona locador
                e.setLocador(l);
                
                // pegar os lugares desse espaco
                LugarBanco lB = new LugarBanco();
                HashSet<Lugar> lugares = lB.getLugaresWhere(id);
                
                e.setLugares(lugares);
            }

        }catch(SQLException er){
            er.printStackTrace();
            return null;

        } finally {

            try {
                if(stmt != null)
                    stmt.close();
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        
        return e;
    }
    
    // atualizar algo
    //(cara que está pedindo operação, id do espaco, id do cliente e o status
    public void atualizarEspaco(Locador contexto, int id, double diaria, String titulo, int qlugares, String endereco){
        if(connection == null){
            contexto.rAtualizarE(false);
            return;
        }
        new Thread(){
            @Override
            public void run(){
                PreparedStatement stmt = null;
                try{
                    stmt = connection.prepareStatement("UPDATE espaco SET diaria = "+diaria+", tipo = '"+titulo+"', lugares = "+qlugares+", endereco = '"+endereco+"' WHERE id = "+id+"");
                    
                    if(stmt.executeUpdate() != 1){
                        throw new NaoAtualizou();
                    }
                    contexto.rAtualizarE(true);
                    
                }catch(SQLException | NaoAtualizou e){
                    
                    e.printStackTrace();
                    contexto.rAtualizarE(false);
                }finally {
                    
                    fecharDB(stmt);

                }
            }
        }.start();
    }
    
    // atualizar algo
    //(cara que está pedindo operação, id do espaco,
    // lugares ocupados recebe 1 ou -1
    public void atualizarEspaco(Usuario contexto, int id, int lugares_ocupados){
        if(connection == null){
            contexto.rAtualizacao(false);
            return;
        }
        new Thread(){
            @Override
            public void run(){
                PreparedStatement stmt = null;
                try{
                    String sql;
                    if(lugares_ocupados > 0)
                        sql = "UPDATE espaco SET lugares_reservados = lugares_reservados +"+lugares_ocupados+" WHERE id = "+id;
                    else
                        sql = "UPDATE espaco SET lugares_reservados = lugares_reservados "+lugares_ocupados+" WHERE id = "+id;
                    
                    stmt = connection.prepareStatement(sql);
                    stmt.execute();
                    
                    if(stmt.executeUpdate() != 1)
                        throw new NaoAtualizou();
                    
                }catch(SQLException | NaoAtualizou e){
                    e.printStackTrace();
                    contexto.rAtualizacao(false);
                    fecharDB(stmt);
                    return;
                }finally {
                    
                    fecharDB(stmt);

                }
                
                contexto.rAtualizacao(true);
            }
        }.start();
    }
    
    
}
