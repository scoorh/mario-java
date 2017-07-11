/*
 *
 *
 * 
 *
 *
 *
 */
package App;

import Graphics.PixelData;
import java.awt.Canvas;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class App extends Canvas implements Runnable {
    Thread mAppThread = null;
    String mTitle = null;
    boolean mAppThreadRunning = false;
    
    JFrame mWindow = null;
    JPanel mPanel = null;
    Input mInput = null;
    PixelData mPixelData = null;
    Game mGame = null;
    Menu mMenu = null;
    State mCurrState = null;
        
    public JFrame getWindow() { return mWindow; }
    public JPanel getPanel() { return mPanel; }
    public Input getInput() { return mInput; }
 
    public App(String title, int windowWidth, int windowHeight) {
        setBounds(0, 0, windowWidth, windowHeight);
        
        mTitle = title;
          
        mInput = new Input();
        addKeyListener(mInput);
        addMouseListener(mInput);
        addMouseMotionListener(mInput);
        
        mPanel = new JPanel();
        mPanel.setLayout(null);
        mPanel.setBounds(0, 0, windowWidth, windowHeight);
      
        mWindow = new JFrame();
        mWindow.setResizable(false);
        mWindow.setSize(windowWidth, windowHeight);
        mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mWindow.setLocationRelativeTo(null);
        mWindow.setLayout(null);
        mWindow.show();
          
        mWindow.add(mPanel);
        
        mPixelData = new PixelData();
        mGame = new Game(this);
        mMenu = new Menu(this);
     
    }
    
    @Override
    public void run() {
        requestFocus();
        
        mCurrState = mMenu;
      
        
        /*
        */
  //  mMenu.onPause();
  //  mCurrState =  new Edit(this);
        
        try {
        
        Timer.start();
        while(mAppThreadRunning) {
            Timer.tick();
            
            while(Timer.deltaTime >= 1) {
                Input.update();
         
                if(mCurrState.onUpdate()) {
                    mCurrState.onPause();
                    mCurrState = mCurrState.equals(mMenu) ?
                            mGame : mMenu;                    
                    mCurrState.onResume();
                    requestFocus();
                }
                
                --Timer.deltaTime;
                ++Timer.ups;
            }
            
            PixelData.clear();
            mCurrState.onDraw();
            PixelData.present(this);
            ++Timer.fps;
            
            
            if(Timer.secPassed()) {
                mWindow.setTitle(mTitle + " | " + Timer.ups + "ups, " + Timer.fps + " fps");
                Timer.ups=0;
                Timer.fps=0;
            }
        }
        
        }catch(Exception e) {}
    }
    
    public void start() {
        mAppThreadRunning = true;
        mAppThread = new Thread(this, "AppThread");
        mAppThread.start();
    }
    
    public void stop() {
        mAppThreadRunning = false;
        try {
            mAppThread.join();
        }
        catch(InterruptedException e) {
        }
    }
    
    public static void main(String[] args) {
    	App app = new App("FAJNA GIERA v1.0",32*30, 32*15);//*20*2,32*10*2);
        app.start();
    }

   
    

}
