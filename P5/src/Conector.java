
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Josh Plata
 */
public class Conector {
    private static final String URL="jdbc:mysql://localhost:3306/Distribuidos";
    private static final String USER="root";
    private static final String PASSWORD="root";

    public Conector() {
    }
    
    public static Connection getConnection(){
        Connection con=null;
        try{
        Class.forName("com.mysql.jdbc.Driver");
        con=(Connection) DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(Exception e){
           System.out.println(e); 
        }
        return con;
    }
    public void Insertar(String ip,int suma,int hora,int minutos,int segundos) throws SQLException, ClassNotFoundException{
        Connection con=null;
        try{
            con=getConnection();
            PreparedStatement ps;
            ps=con.prepareStatement("insert into datos(resultado,ip,hora,minutos,segundos) values (?,?,?,?,?)");
            ps.setInt(1, suma);
            ps.setString(2, ip);
            ps.setInt(3, hora);
            ps.setInt(4, minutos);
            ps.setInt(5, segundos);
            

            int res= ps.executeUpdate();
            if(res>0){
                JOptionPane.showMessageDialog(null, "Guardado");
            }else{
                JOptionPane.showMessageDialog(null, "Error al Guardadar");
            }
            con.close();
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
}
