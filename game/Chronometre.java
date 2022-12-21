/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

/**
 *
 * @author khlil
 */
public class Chronometre {

    private long begin;
    private long end;
    private long current;
    private int limite;

    public Chronometre(int limite) {
        // intialisation
        this.limite = limite;
    }

    public void start() {
        begin = System.currentTimeMillis();
    }

    public void stop() {
        end = System.currentTimeMillis();
    }

    public long getTime() {
        return end - begin;
    }

    public long getMilliseconds() {
        return end - begin;
    }

    public int getSeconds() {
        return (int) ((end - begin) / 1000.0);
    }

    public double getMinutes() {
        return (end - begin) / 60000.0;
    }

    public double getHours() {
        return (end - begin) / 3600000.0;
    }

    /**
     * Method to know if it remains time.
     * @return 
     */
   public boolean remainsTime() {
        return (getTimeSpent() < limite);
    }
        public long getCurrentMilliseconds(){
        return (System.currentTimeMillis() - begin);
    }
         public int getTimeSpent(){
        return (int) (getCurrentMilliseconds()/1000.0);
    }
        
   public String getRemainingTimeString(){
        String timeStr = "";
        int timeInt = (int) (limite - getCurrentMilliseconds());
        timeStr = String.valueOf(timeInt);
        return timeStr;
    }
}
