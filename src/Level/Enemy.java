/*
 *
 *
 * 
 *
 *
 *
 */
package Level;

import Graphics.CollisionData;
import Graphics.CollisionID;
import Graphics.Rotation;
import Graphics.Sprite;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Enemy {
    public enum MOVE_DIR {
        NONE,
        LEFT,
        RIGHT
    };
    
    public class Data {           
        public int x, y;
        public CollisionData collision;
        public boolean alive;
        public Sprite sprite;
        public int animSize;
        public int currAnim;
        public int timer;
        public MOVE_DIR moveDir;
        public int moveSpeed;
        
        public Data() {
            sprite = new Sprite();
            collision = new CollisionData();
        }
        
        public void save(ObjectOutputStream os) throws IOException {
            os.writeInt(x);
            os.writeInt(y);
            os.writeBoolean(alive);
            sprite.save(os);
            os.writeInt(animSize);
            os.writeInt(currAnim);
            os.writeInt(timer);
            os.writeInt(moveDir.ordinal());
            os.writeInt(moveSpeed);
        }
        
        public void load(ObjectInputStream is) throws IOException {
            x = is.readInt();
            y = is.readInt();
            alive = is.readBoolean();
            sprite.load(is);
            animSize = is.readInt();
            currAnim = is.readInt();
            timer = is.readInt();
            moveDir = MOVE_DIR.values()[is.readInt()];
            moveSpeed = is.readInt();
        }     
        
        public void animate() {
            ++timer;
            if(timer == 25) {
                timer = 0;
                ++currAnim;
                ++sprite.number;
                if(currAnim == animSize) {
                    sprite.number -= animSize;
                    currAnim = 0;
                }
            }
        }
        
        public void collisionCheck(Level level) {
            collision.reset();
            collision.xpos = x;
            collision.ypos = y;
            collision.size = 48;
            level.collisionCheck(collision);
            
            if(moveDir == MOVE_DIR.LEFT) {
                if(collision.leftBottom == CollisionID.NONE)
                    moveDir = MOVE_DIR.RIGHT;
            }
            else {
                if(collision.rightBottom == CollisionID.NONE)
                    moveDir = MOVE_DIR.LEFT;
            }
        }
    };
    
    boolean mLoaded = false;
    Level mLevel;
    public Data[] mData;
            
    
    public Enemy(Level level) {
        mLevel = level;
    }
    
    public void n() {
        mData = new Data[5];
               for(int i =0 ;i < 5; ++i)
                   mData[i]=new Data();
               
    }
    
    
    public void update() {
        if(mData == null) return;
        
        for(int i = 0; i < mData.length; ++i) {
            if(mData[i] != null && mData[i].alive == true) {                
                mData[i].animate();
                mData[i].collisionCheck(mLevel);
                
                if(mData[i].moveDir == MOVE_DIR.LEFT)
                    mData[i].x -= mData[i].moveSpeed;
                else mData[i].x += mData[i].moveSpeed;
            }
        }
    }
    
    public void draw() {
        if(mData == null) return;
        
        for(int i = 0; i < mData.length; ++i) {
            if(mData[i] != null && mData[i].alive == true) {
                if(mData[i].moveDir == MOVE_DIR.LEFT) 
                    mData[i].sprite.rotation = Rotation.NONE;
                else mData[i].sprite.rotation = Rotation.X;
                    
                mData[i].sprite.draw(mData[i].x - mLevel.mXOffset, mData[i].y - mLevel.mYOffset);
            }
        }
    }
    
    public void save(String filename) {
        try {
            File file = new File(filename);
            if(!file.exists())
                file.createNewFile();
            
            FileOutputStream fileos = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fileos);
            
            if(mData != null) {
                int c = 0;
                if(mData != null) {
                    for(int i = 0; i < mData.length; ++i){
                        if(mData[i] != null)
                            ++c;
                    }
                }
                
                os.writeInt(c);
                
                for(int i = 0; i < mData.length; ++i) {
                    if(mData[i] != null) {
                        mData[i].save(os);
                    }
                }
            }
            
            os.close();
        }
        catch(IOException e) {}
    }
    
    public boolean load(String filename) {
        mLoaded = false;
        try {
            File file = new File(filename);
            if(!file.exists())
                return false;
            
            FileInputStream fileIs = new FileInputStream(file);
            ObjectInputStream is = new ObjectInputStream(fileIs);
            
            int c = is.readInt();
            mData = new Data[c];
            
            for(int i = 0; i < mData.length; ++i) {
                mData[i] = new Data();
                mData[i].load(is);
            }
            
            is.close();
            mLoaded = true;
        }
        catch(IOException e) {}
        
        return mLoaded;
    }
    
}
