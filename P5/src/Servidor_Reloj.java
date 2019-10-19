import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Servidor_Reloj extends Thread{
    private static String [] Ips={"",""};
    private int Referencia;
    private int Zigma;
    private static int [] Horas={0,0};
    private static int [] Minutos={0,0};
    private static int [] Segundos={0,0};
    private static int [] Ajuste={0,0};
    
    public static void main(String [] args) throws IOException, InterruptedException{
                
        Ips[0]= JOptionPane.showInputDialog(null,"Servidor 1");
        Ips[1]= JOptionPane.showInputDialog(null,"Servidor 2");
   
        for (;;) {                                            
            for (int i = 0; i < 2; i++) {                            
                Socket cl=new Socket(Ips[i],7002);                                                
                DataInputStream dis= new DataInputStream(cl.getInputStream());                                              
                    
                    Horas[i]= dis.readInt(); 
                    Minutos[i]= dis.readInt(); 
                    Segundos[i]= dis.readInt();                                 
                    
                dis.close();                
                cl.close();
            }
            
            Ajuste();
            
            for (int i = 0; i < 2; i++) {                            
                Socket cl=new Socket(Ips[i],7002);
                DataOutputStream dos= new DataOutputStream(cl.getOutputStream());                                                
                
                    dos.writeInt(Ajuste[i]);
                    dos.flush();
                    
                dos.close();
                cl.close();
            }
            
            
            
        }      
    }
    public static void Ajuste(){
        
    }
    
   
  }
