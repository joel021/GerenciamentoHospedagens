/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.banco;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import modelo.Principal;

/**
 *
 * @author Joel
 */
public class Conectar {
    
    public Connection getConexao(){
        try {
            // importa a biblioteca
            Class.forName("com.mysql.jdbc.Driver");
            // retorna a coneccao
            Connection conexao = DriverManager.getConnection("jdbc:mysql://sql10.freesqldatabase.com:3306/sql10299828", Principal.DB_USER, Principal.DB_PASS);
            return conexao;
        } catch(ClassNotFoundException | SQLException e){
            // lança para eu corrigir, ClassNotFound é erro de programação...
            e.printStackTrace();
            return null;
        }
    }
}
