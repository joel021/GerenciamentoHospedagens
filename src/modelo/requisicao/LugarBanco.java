/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.requisicao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import modelo.Espaco;
import modelo.Lugar;
import modelo.Principal;
import modelo.Usuario;
import modelo.banco.Conectar;
import modelo.execoes.NaoAtualizou;
import modelo.execoes.NaoGerou;

/**
 *
 * @author Joel
 */

/// @Está relacionada a EspacoBanco, não são uma só porque tratam tabelas diferentes no banco de dados
public final class LugarBanco {
    // para chamar esse método tem de ser de uma thread secundária, requisião pesada!
    private final Connection connection;
    
    public LugarBanco(){
        this.connection = new Conectar().getConexao();
    }
    
    public HashSet<Lugar> getLugaresWhere(int id_espaco){
        PreparedStatement stmt = null;
        HashSet<Lugar> lugares = new HashSet<>();
        
        if(connection == null){
            return lugares;
        }
        try{
            stmt = connection.prepareStatement("SELECT * FROM lugar WHERE id_espaco = ?");
            stmt.setInt(1, id_espaco);
            
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                Lugar lugar = new Lugar();
                lugar.setId(rs.getInt("id"));
                lugar.setId_espaco(id_espaco);
                lugar.setStatus(rs.getString("status"));
                lugar.setDias(rs.getDouble("dias"));
                lugar.setDiaria(rs.getDouble("diaria"));
                
                // pegar o locatario
                UsuarioBanco ub = new UsuarioBanco();
                Usuario u2      = ub.getWhere(rs.getInt("id_locatario"));
                
                lugar.setLocatario(u2);
                
                // finalmente adicionar lugar
                lugares.add(lugar);
            }

        }catch(SQLException er){

            er.printStackTrace();
            
        } finally {

            closeBD(stmt);

        }
        
        return lugares;
    }
    
    // manda espacos reservados ou pedidos a reservar desse usuario
    public void getAgendamentos(Usuario contexto){
        if(contexto.getUsuario().equals(Principal.ADMIN))
            buscarLugares(contexto, true);
        else
            buscarLugares(contexto, false);
    }
    
    // buscar os lugares onde estão marcados em aguardo para o admin decidir se vai ou não confirmar ou pegar os lugares que um locatário em específico pediu
    private void buscarLugares(Usuario contexto, boolean admin){
        if(connection == null){
            contexto.rGetAgendamentos(new HashMap<>());
            return;
        }
        new Thread(){
            @Override
            public void run(){
                
                PreparedStatement stmt = null;
                try{
                    if(admin){
                        stmt = connection.prepareStatement("SELECT * FROM lugar WHERE status = '"+Principal.AGUARDO+"'");
                    }
                    else{
                        stmt = connection.prepareStatement("SELECT * FROM lugar WHERE id_locatario = ?");
                        stmt.setInt(1, contexto.getId());
                    }
                    HashMap<Lugar,Espaco> lugares = lugares(stmt);
                    // envia para quem pediu
                    contexto.rGetAgendamentos(lugares);

                }catch(SQLException e){
                    e.printStackTrace();
                    //throw new RuntimeException(e);
                    contexto.rGetAgendamentos(new HashMap<>());
                }finally {

                    closeBD(stmt);

                }
            }
        }.start();
    }
    
    // só chamada por uma thread secundária!
    // pega os espacos, não os lugares
    private HashMap<Lugar, Espaco> lugares(PreparedStatement stmt) throws SQLException{
        ResultSet rs = stmt.executeQuery();
                    
        // cria a lista para mandar para o locador que pediu
        HashMap<Lugar, Espaco> lugares = new HashMap<>();;
        
        while(rs.next()){
            // adiciona os atributos da lista
            Lugar lugar = new Lugar();
            lugar.setId(rs.getInt("id"));
            lugar.setId_espaco(rs.getInt("id_espaco"));
            lugar.setStatus(rs.getString("status"));
            lugar.setDiaria(rs.getDouble("diaria"));
            lugar.setDias(rs.getDouble("dias"));
            
            // pegar locador
            UsuarioBanco ub = new UsuarioBanco();
            Usuario us      = ub.getWhere(rs.getInt("id_locatario"));
               
            lugar.setLocatario(us);
            
            // pegar o espaco correspondente
            EspacoBanco eb = new EspacoBanco();
            Espaco e       = eb.getEspacoWhere(lugar.getId_espaco());
            
            // adiciona o espaco e o lugar
            lugares.put(lugar, e);

        }
        
        return lugares;
    }
    
    // adicionar os lugares de um espaco, retora uma string de ids
    public String adiconarLugares(int id_espaco, double diaria, String status, int id_locatario, int n){
        System.out.println("Adicionar lugares");
        if(connection == null){
            return "0";
        }
        
        String ids_lugares = "";
        
        PreparedStatement pstmt = null;
        try{
            String sql = "INSERT INTO lugar (id_espaco, status, diaria, id_locatario) VALUES ";
            for(int i = 0; i < n; i++){
                if(i == 0)
                    sql = sql.concat("("+id_espaco+",'"+status+"',"+diaria+","+id_locatario+")");
                else
                    sql = sql.concat(",("+id_espaco+",'"+status+"',"+diaria+","+id_locatario+")");
            }
            
            pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // executa a query
            
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            int i = 0;
            while(rs.next()){
                if(i == 0)
                    ids_lugares += ""+rs.getInt(1); // pega o valor do a primeira coluna do dado gerado, no caso, o valor da coluna id, que é a primeira
                else
                    ids_lugares += ","+rs.getInt(1);
                i++;
            }

        }catch(SQLException e){
            closeBD(pstmt);
            e.printStackTrace();
        }finally {

            closeBD(pstmt);

        }
        
        return ids_lugares;
        
    }
    // atualizar os lugares de um espaco
    public boolean atualizarLugares(String ids, int id_locatario, double dias, String status){
        if(connection == null){
            return false;
        }
        PreparedStatement pstmt = null;
        try{
            System.out.println("ids = "+ids);
            pstmt = connection.prepareStatement("UPDATE lugar SET id_locatario = "+id_locatario+", status = '"+status+"', dias = "+dias+" WHERE id IN ("+ids+")");
            
           if(pstmt.executeUpdate() != 1){
               throw new NaoAtualizou();
           }

        }catch(SQLException | NaoAtualizou e){
            closeBD(pstmt);
            e.printStackTrace();
            return false;
        }finally {

            closeBD(pstmt);

        }
                
        return true;
    }
    
    // atualizar os lugares de um espaco
    public int atualizarLugares(int id_espaco, int id_locatario, String status){
        if(connection == null){
            return 0;
        }
        int i                   = 0;
        PreparedStatement pstmt = null;
        try{
            
            if(status.equals(Principal.CHECKIN))
                pstmt = connection.prepareStatement("UPDATE lugar SET status = '"+status+"' WHERE id_espaco = "+id_espaco+" AND id_locatario = "+id_locatario, Statement.RETURN_GENERATED_KEYS);
            else
                pstmt = connection.prepareStatement("UPDATE lugar SET id_locatario = 0, status = '"+status+"' WHERE id_espaco = "+id_espaco+" AND id_locatario = "+id_locatario, Statement.RETURN_GENERATED_KEYS);
            
           if(pstmt.executeUpdate() != 1){
               throw new NaoAtualizou();
           }
           
           ResultSet rs = pstmt.getGeneratedKeys();
            
            while(rs.next()){
                i++;
            }
            
        }catch(SQLException | NaoAtualizou e){
            closeBD(pstmt);
            e.printStackTrace();
        }finally {

            closeBD(pstmt);

        }
                
        return i;
    }
    
    // para chamar esse método tem de ser de uma thread secundária
    // pega um lugar em específico
    public Lugar getLugarWhere(int id){
        if(connection == null){
            return null;
        }
        PreparedStatement stmt  = null;
        Lugar lugar             = null;
        try{
            stmt = connection.prepareStatement("SELECT * FROM lugar WHERE id = ?");
            stmt.setInt(1, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                lugar = new Lugar();
                lugar.setId(id);
                lugar.setId_espaco(rs.getInt("id_espaco"));
                lugar.setStatus(rs.getString("status"));
                lugar.setDiaria(rs.getDouble("diaria"));
                lugar.setDias(rs.getDouble("dias"));
                lugar.setId_espaco(rs.getInt("id_espaco"));
                
                // pegar o locatario
                UsuarioBanco ub = new UsuarioBanco();
                Usuario u2      = ub.getWhere(rs.getInt("id_locatario"));
                
                
                lugar.setLocatario(u2);
                
            }

        }catch(SQLException er){
            closeBD(stmt);
            er.printStackTrace();
            return null;

        } finally {

            closeBD(stmt);

        }
        
        return lugar;
    }

    // apenas atualiza o lugar mesmo, faça o resto do trabalho por conta própria
    // tem de ser chamada uma thread secundária
    public boolean atualizarLugar(int id_lugar, int id_locatario, String status) {
        PreparedStatement stmt = null;
        
        if(connection == null){
            return false;
        }
        try{
            stmt  = connection.prepareStatement("UPDATE lugar SET status = '"+status+"', id_locatario = "+id_locatario+" WHERE id = "+id_lugar);
            int a = stmt.executeUpdate();
            if(a != 1)
                throw new NaoAtualizou();
            
        }catch(SQLException | NaoAtualizou e){
            closeBD(stmt);
            e.printStackTrace();
            return false;
        }finally {
            
            closeBD(stmt);
        }
        
        return true;
    }

    // remove *todos* os lugares de um determinado espaco
    boolean removerLugares(int id_espaco) {
        if(connection == null){
            return false;
        }
        PreparedStatement pstmt = null;
        
        try {
            pstmt = connection.prepareStatement("DELETE FROM lugar WHERE id_espaco = ?");

            pstmt.setInt(1, id_espaco);

            // executa a query
            pstmt.executeUpdate();

        }catch(SQLException e){

            // lancar erro para eu ver e consertar
            closeBD(pstmt);
            e.printStackTrace();
            return false;
            
        }finally {
            closeBD(pstmt);
        }
        
        return true;
    }
    
    private void closeBD(PreparedStatement pstmt){
        try {
            if(pstmt != null)
                pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // pega todos os lugares que estão 
    // get
    public ArrayList<Lugar> getCheckins(){
        PreparedStatement stmt = null;
        ArrayList<Lugar> lugares = new ArrayList<>();
        
        if(connection == null){
            return lugares;
        }
        try{
            stmt = connection.prepareStatement("SELECT * FROM lugar WHERE status = '"+Principal.CHECKIN+"' ");
            
            ResultSet rs = stmt.executeQuery();
            
            // adiciona todas as diárias na lista
            while(rs.next()){
                Lugar lugar = new Lugar();
                lugar.setId(rs.getInt("id"));
                
                lugar.setDiaria(rs.getDouble("diaria"));
                lugar.setDias(rs.getInt("dias"));
                lugar.setId_espaco(rs.getInt("id_espaco"));
                lugar.setStatus(rs.getString("status"));
                
                UsuarioBanco ub = new UsuarioBanco();
                lugar.setLocatario(ub.getWhere(rs.getInt("id_locatario")));
                
                lugares.add(lugar);
            }

        }catch(SQLException er){
            closeBD(stmt);
            er.printStackTrace();

        } finally {

            closeBD(stmt);

        }
        
        return lugares;
    }
    
    public boolean adiconarLugar(int id_espaco, int id_locatario, String status){
        
        if(connection == null){
            return false;
        }
        
        PreparedStatement pstmt = null;
        try{
            pstmt = connection.prepareStatement("INSERT INTO lugar (id_espaco, id_locatario, status) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, id_espaco);
            pstmt.setInt(2, id_locatario);
            pstmt.setString(3, status);
            // executa a query      
            
            pstmt.execute();
            // pega o resultadoRU da chave gerada automaticamente
            ResultSet rs = pstmt.getGeneratedKeys();

            if(!rs.next()){
                throw new NaoGerou();
            }
        }catch(SQLException | NaoGerou e){
            
            closeBD(pstmt);
            
            e.printStackTrace();
            return false;

        }finally {

            closeBD(pstmt);

        }
                
        return true;
    }
}
