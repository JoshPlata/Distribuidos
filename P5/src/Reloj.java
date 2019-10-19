/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Josh Plata
 */
public class Reloj extends Thread{
    private int Horas,Minutos,Segundos;
    private float tiempo;
    private JLabel Hora_completa;
    private Thread reloj;
    
    
    public Reloj(int Horas, int Minutos, int Segundos, float tiempo, JLabel Hora_completa) {
        this.Horas = Horas;
        this.Minutos = Minutos;
        this.Segundos = Segundos;
        this.tiempo = tiempo;
        this.Hora_completa = Hora_completa;
        reloj= new Thread(this);
    }
    public void setHoras(int Horas) {
        this.Horas = Horas;
    }

    public void setMinutos(int minutos) {
        this.Minutos = minutos;
    }

    public void setSegundo(int segundo) {
        this.Segundos = segundo;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public void setHora_completa(JLabel Hora_completa) {
        this.Hora_completa = Hora_completa;
    }  

    public JLabel getHora_completa() {
        return Hora_completa;
    }

    public int getHoras() {
        return Horas;
    }

    public float getTiempo() {
        return tiempo;
    }

    public int getMinutos() {
        return Minutos;
    }

    public int getSegundo() {
        return Segundos;
    }
    
    @Override
    public void run() {
        
        while(true){
            Segundos++;
                if(Segundos>=60){
                    Minutos++;
                    Segundos=0;
                }
                if(Minutos>=60){
                    Horas++;
                    Minutos=0;
                }
                if(Horas>=24){
                    Horas=0;
                }            
            Hora_completa.setText(Horas+":"+Minutos+":"+Segundos);  
            
            try {       
                long milis=(long) ((long)1000*tiempo);
                Thread.sleep(milis);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Reloj.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    
    
    
    
    
}
