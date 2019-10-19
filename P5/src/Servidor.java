/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Josh Plata
 */
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Servidor extends Thread implements ActionListener{
    
    private int Horas,Minutos,Segundos;
    private float tiempo;
    private JLabel Hora_completa;
    private Thread reloj;
    private static String host_servidor_enlace;
    private static Reloj reloj1;
    
    public static void main(String[]args) {
            //Declaracion de variables
            int Suma,porcentaje,n,horas_aux,minutos_aux,segundos_aux;
            long recibidos=0;
            
            JFrame f = new JFrame("Servidor");
            f.setSize(300,300);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLocationRelativeTo(null);
            JPanel panel = (JPanel) f.getContentPane();
            panel.setLayout(null);
            
            //Creacion JLabels        
            JLabel R1 = new JLabel("Reloj1"); 
            JLabel Conexion = new JLabel("Conexion: ");
            JLabel Conexion2 = new JLabel("                       ");
            JLabel Datos = new JLabel("Datos: "); 
            JLabel Datos2 = new JLabel("   ");
            JButton boton1=new JButton("Reloj 1");             
            //añadir JLabels
            panel.add( R1 ); 
            panel.add( Conexion );  
            panel.add( Conexion2 );  
            panel.add( Datos );  
            panel.add( Datos2 );                        
            panel.add( boton1 );            
            //Dimension de los Jlabels
            Dimension size = R1.getPreferredSize(); 
            Dimension sizea = Conexion.getPreferredSize();
            Dimension sizeb = Conexion2.getPreferredSize();
            Dimension sizec = Datos.getPreferredSize();
            Dimension sized = Datos2.getPreferredSize();               
            Dimension sizeb1 = boton1.getPreferredSize();            
            R1.setBounds(115, 60, size.width+30, size.height);   
            Conexion.setBounds(30, 100, sizea.width+30, sizea.height);
            Conexion2.setBounds(80, 100, sizeb.width+30, sizeb.height);
            Datos.setBounds(30, 170, sizec.width+30, sizec.height);
            Datos2.setBounds(80, 170, sized.width+30, sized.height);                 
            boton1.setBounds(100, 210, sizeb1.width+10, sizeb1.height);
            f.setVisible(true);            
            
            //hilo para corrar el reloj de la interfaz                
            reloj1= new Reloj(10,30,15,1,R1);                        
            reloj1.start();
                        
            //hilo para comunicación entre servidores
            Servidor sa=new Servidor();
            sa.start();
            host_servidor_enlace=JOptionPane.showInputDialog(null, "Escriba la dirección del Servidor Enlace");

            //Cambiar hora
            boton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        reloj1.setHoras(Integer.parseInt(JOptionPane.showInputDialog(null, "Horas")));
                        reloj1.setMinutos(Integer.parseInt(JOptionPane.showInputDialog(null, "Minutos")));
                        reloj1.setSegundo(Integer.parseInt(JOptionPane.showInputDialog(null, "Segundo")));
                        reloj1.setTiempo(Float.parseFloat(JOptionPane.showInputDialog(null, "Velocidad")));
                    }catch(HeadlessException | NumberFormatException en){}       
                }
            });              
                
            //Seccion del Servidor  para jugadores     
                try{
                    //Creamos Socket
                    ServerSocket s= new ServerSocket(7000);
                    //Iniciamos el ciclo infinito
                    for (;;) {
                        //Esperamos una conexión 
                        Socket cl= s.accept();
                        System.out.println("Conexión establecida desde"+ cl.getInetAddress()+" :" +cl.getPort());
                        DataInputStream dis= new DataInputStream(cl.getInputStream());
                        byte []b= new byte[1024];
                        int num= dis.readInt();  //numero de archivos

                        for (int i = 0; i < num; i++) {                                  
                            String nombre=dis.readUTF();
                            System.out.println("Recibimos el archivo"+ nombre);
                            long tam=dis.readLong();
                            FileOutputStream fos=new FileOutputStream(nombre);
                            DataOutputStream dos= new DataOutputStream(fos);
                            //Sección para recibir el archivo                     
                                while(recibidos<tam){
                                    n=dis.read(b);                        
                                    dos.write(b,0,n);
                                    dos.flush();
                                    recibidos=recibidos+n;
                                    porcentaje=(int)(recibidos*100/tam);
                                    System.out.print("Recibido:"+porcentaje+"%\r");

                                }//while
                                
                            System.out.println("\n\n Archivo recibido.\n");
                            fos.close();
                            dos.close();
                            dis.close();                    
                            Conexion2.setText(String.valueOf(cl.getInetAddress()));                            
                            
                            horas_aux=reloj1.getHoras();
                            minutos_aux=reloj1.getMinutos();
                            segundos_aux=reloj1.getSegundo();
                            //Colocar ip y suma en interfaz                    
                            Suma=Contar(nombre);
                            Datos2.setText(String.valueOf(Suma));
                            
                            //Registrar en BD
                            Operacion1(String.valueOf(cl.getInetAddress()), Suma, horas_aux, minutos_aux,segundos_aux); 
                            //Enviar a Servidor
                            Comunicar_Servidor(String.valueOf(cl.getInetAddress()), Suma, horas_aux, minutos_aux, segundos_aux);                            
                            cl.close();
                        }
                        recibidos=0;
                    }
                }catch(Exception e){e.printStackTrace();}        
        }    
    
    @Override//Servidor para comunicación entre servidores
    public void run() {
        
        while(true){
            try {
                ServerSocket s2= new ServerSocket(7001);
                for (;;) 
                    {
                        //Esperamos una conexión 
                        Socket cl= s2.accept();
                        System.out.println("Conexión establecida desde"+ cl.getInetAddress()+" :" +cl.getPort());
                        DataInputStream dis= new DataInputStream(cl.getInputStream());                        
                            
                            String direccion=dis.readUTF();                                                                            
                            int suma= dis.readInt(); 
                            
                        dis.close();                    
                        cl.close();                                
                        //Registrar en BD
                        Operacion1(direccion, suma, reloj1.getHoras(),reloj1.getMinutos(),reloj1.getMinutos());                    
                    }                                            
            } catch (Exception ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    public static int Contar(String archivo) throws FileNotFoundException, IOException{
        String cadena;
        int cuenta=0;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                cuenta+=Integer.parseInt(cadena);
            }
        f.close();
        b.close();
        return cuenta;
    }
    
    public static void Operacion1(String ip,int Suma,int horas,int minutos,int segundos) throws SQLException, ClassNotFoundException{
    //Insertar dato
        Conector con=new Conector();
        con.Insertar(ip, Suma, horas, minutos,segundos);                        
        
    }
    public static void Comunicar_Servidor(String ip,int suma,int horas,int minutos,int segundos) throws IOException{
            
            int pto=7001;                                                           
            Socket cl=new Socket(host_servidor_enlace,pto);
            DataOutputStream dos= new DataOutputStream(cl.getOutputStream());                                
                
                dos.writeUTF(ip);
                dos.flush();
                dos.writeInt(suma);
                dos.flush();
                
            dos.close();
            cl.close();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
