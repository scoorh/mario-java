/*
 *
 *
 * 
 *
 *
 *
 */
package App;

import java.applet.Applet;
import java.io.*;
import sun.audio.*;



public class Sound extends Applet{
    
    public enum PLAYING {
        NONE,
        MAIN_THEME,
        PLAYER_DIED,
    };
    
    private static final AudioPlayer MGP = AudioPlayer.player;
    private static AudioStream BGM;   
    private static AudioStream minorBGM;
    private static PLAYING playing = PLAYING.NONE;
    
    
    public static void Coin() {
    	
        try {
            minorBGM = new AudioStream(new FileInputStream("res/Sound/coin.wav"));
            MGP.start(minorBGM);
        }
        catch(FileNotFoundException e) {}
        catch(IOException e) {}
        
    }
    
    public static void Jump() {
    	
        try {
            minorBGM = new AudioStream(new FileInputStream("res/Sound/jump.wav"));
            MGP.start(minorBGM);
        }
        catch(FileNotFoundException e) {}
        catch(IOException e) {}
        
        
    }
    
    public static void MainTheme()
    {
    	
        if(playing != PLAYING.MAIN_THEME) {
            
            if(playing != PLAYING.NONE)
                MGP.stop(BGM);
            
            try {
                BGM = new AudioStream(new FileInputStream("res/Sound/maintheme.wav"));               
                MGP.start(BGM);
                playing = PLAYING.MAIN_THEME;
            }
            catch(FileNotFoundException e) {}
            catch(IOException e) {}
        }    
        
    }
    
    public static void PlayerDied() {
    	
        if(playing != PLAYING.PLAYER_DIED) {
            if(playing != PLAYING.NONE)
                MGP.stop(BGM);
            
            try {
                BGM = new AudioStream(new FileInputStream("res/Sound/died.wav"));
                MGP.start(BGM);
                playing = PLAYING.PLAYER_DIED;
            }
            catch(FileNotFoundException e) {}
            catch(IOException e) {}
        } 
        
    }
        
    
}
