/*
 *
 *
 * 
 *
 *
 *
 */
package App;


public class Timer {
    
    public static final double NANO_SEC = 1000000000 / 60;
    
    public static long ups = 0;
    public static long fps = 0;
    public static double deltaTime = 0;
    
    public static long lastTime;
    public static long currTime;
    
    static long secTimer = 0;
    
    public static void start() {
        lastTime = System.nanoTime();
        currTime = 0;
        deltaTime = 0;
        secTimer = System.currentTimeMillis();
    }
    
    public static void tick() {
        currTime = System.nanoTime();
        deltaTime += (currTime - lastTime) / NANO_SEC;
        lastTime = currTime;
    }
    
    public static boolean secPassed() {
        if(System.currentTimeMillis() - secTimer > 1000) {
            secTimer+=1000;
            return true;
        }
        return false;
    }
    
    
}
