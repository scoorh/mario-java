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
import Graphics.Transition;
import Level.Enemy;
import Level.Level;
import Level.Player;


public class Game extends State {
    
    int mCurrLevel = 0;
    Level mLevel;
    Player mPlayer;
    Enemy mEnemy;
    Transition mTransition;
    boolean newLevel = true;
    boolean levelCompleted = false;
    boolean loadNextLevel = true;
    
    public Game(App app) {
       super(app);
       mLevel = new Level();
       mEnemy = new Enemy(mLevel);
       mPlayer = new Player(mLevel,mEnemy);
       mTransition = new Transition();
    }
   
    @Override
    void onDraw() {        
        mLevel.draw();
        mEnemy.draw();
        mPlayer.draw();    
        
        if(newLevel) {
        	if(mTransition.processFadeIn())
        		newLevel = false;
        }
        if(levelCompleted) {
        	if(mTransition.processFadeOut()) {
        		loadNextLevel = true;
        	}
        }
    }

    @Override
    boolean onUpdate() {     
    	if(loadNextLevel) {
    		++mCurrLevel;
    		newLevel = true;
    		loadNextLevel = false;
    		levelCompleted = false;
    		mTransition.reset();
    		mLevel.load("res/Level/Level" + mCurrLevel);
            mEnemy.load("res/Level/Enemy" + mCurrLevel);
            mPlayer.setSpawn(mLevel.startPointX,mLevel.startPointY);
            Sound.MainTheme();
    		return false;
    	}
    	
        mEnemy.update();
        
        if(!levelCompleted)
        	mPlayer.update();
             
        mLevel.update(mPlayer.x-PixelData.WIDTH/2+16,mPlayer.y-PixelData.HEIGHT/2+16);
        
        if(mPlayer.x >= mLevel.endLine+32) {
        	levelCompleted = true;
        }
  
        return false;
    }

    @Override
    void onPause() {
        
    }

    @Override
    void onResume() {        
    }
}
