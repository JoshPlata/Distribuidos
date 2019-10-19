/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Josh Plata
 */
public class Cliente implements ActionListener{

    /**
     * @param args the command line arguments
     */
    static String Nombre;
    static String Archivo;
    static long tam;
    static String host="";
    static int pto=7000;
    
    public static void main(String[] args) {
        // TODO code application logic here
            
            //Formar el JFrame        
            JFrame f = new JFrame("Relojes");
            f.setSize(300,250);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLocationRelativeTo(null);
            JPanel panel = (JPanel) f.getContentPane();
            panel.setLayout(null);
            //En este JLabel se inserta la hora
            JLabel R1 = new JLabel("Reloj1");                                             
            JLabel Conexion = new JLabel("Conexion: ");
            JLabel Conexion2 = new JLabel("");
            JLabel Datos = new JLabel("Datos: "); 
            JLabel Datos2 = new JLabel("");
            //Botones
            JButton boton1=new JButton("Cargar"); 
            JButton boton2=new JButton("Enviar");           
            //Añadir elementos al JFrame
            panel.add( R1 ); 
            panel.add( Conexion );  
            panel.add( Conexion2 );  
            panel.add( Datos );  
            panel.add( Datos2 );  
            panel.add( boton1 );
            panel.add( boton2);
                                    
            Dimension size = R1.getPreferredSize();              
            Dimension sizeb1 = boton1.getPreferredSize();
            Dimension sizeb2 = boton2.getPreferredSize();            
            
            
            R1.setBounds(110, 80, size.width+30, size.height);            
            boton1.setBounds(50, 130, sizeb1.width+10, sizeb1.height);
            boton2.setBounds(150, 130, sizeb2.width+10, sizeb2.height);
                        
            f.setVisible(true);
            
            Reloj reloj1= new Reloj(0,0,0,1,R1);      
            host=JOptionPane.showInputDialog(null, "Escriba la dirección del Servidor");    
            reloj1.start();
            
            //Boton Cargar
            boton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        JFileChooser jf=new JFileChooser();
                        jf.setMultiSelectionEnabled(true);            
                        int r= jf.showOpenDialog(null);                        
                        if (r==JFileChooser.APPROVE_OPTION) {
                            File[] files = jf.getSelectedFiles();
                            for (int i = 0; i < files.length; i++) {
                                Archivo=files[i].getAbsolutePath();
                                Nombre=files[i].getName();
                                tam=files[i].length();        
                            }                                                
                        }
                    
                }
            });
            //Boton Enviar
            boton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {                                                            
                    int porcentaje=0,n=0;              
                    byte[]b= new byte[1024];
                    long enviados=0;                       
                    try {                                                                                            
                        Socket cl=new Socket(host,pto);
                        DataOutputStream dos= new DataOutputStream(cl.getOutputStream());
                        DataInputStream dis=new DataInputStream( new FileInputStream(Archivo));                                           
                            dos.writeInt(1);
                            dos.flush();
                            dos.writeUTF(Nombre);
                            dos.flush();
                            dos.writeLong(tam);
                            dos.flush();
                        //Sección para el envío del archivo                       
                                while(enviados<tam){
                                    n=dis.read(b);
                                    dos.write(b,0,n);
                                    dos.flush();
                                    enviados=enviados+n;
                                    porcentaje=(int)(enviados*100/tam);
                                    System.out.print("Enviados:"+porcentaje+"%\r");
                                }//while
                                System.out.println("\n\nArchivo enviado ");
                                dis.close();                
                                dos.close();
                                cl.close();
                    
                    } catch (IOException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                }
            });
           
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
