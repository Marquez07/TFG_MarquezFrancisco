/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Francisco
 */
public class Conectar {
    private EntityManager em;
    private EntityManagerFactory emf;
    private String connect;
    private String user;
    private String password;
    private Connection conexion;
    
    public Conectar() throws SQLException{
        
        try{
            connect = em.getProperties().get("javax.persistence.jdbc.url").toString();
            user = em.getProperties().get("javax.persistence.jdbc.user").toString();
            password = "";
            conexion = DriverManager.getConnection(connect, user, password);

        }catch(Exception e){e.printStackTrace();}
    }
    
    public Connection getConexion(EntityManager entityManager){
        this.em = entityManager;
        return this.conexion;
    }
}