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
import Graphics.PixelData;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.Transition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Level {
    boolean mLoaded = false; 
    Transition mTransition;
    
    int mWidth = 0;
    int mHeight = 0;
    int mXOffset = 0;
    int mYOffset = 0;
    Node mRoot;     

    public int startPointX, startPointY;
    public int endLine;
     
    public Level() {
        mTransition = new Transition();
    }
    
     
    public void setStartPoint(int x, int y) {
        startPointX = x;
        startPointY = y;
    }
    
    public void setEndLine(int x) {
        endLine = x;
    }
    
    public Entity addEntity(Entity entity) {
        return Node.addEntity(mRoot, entity);
    }
    
    public Sprite pickTile(int x, int y) {
        return Node.pickTile(mRoot, x, y);
    }
    
    public Entity pickEntity(int x, int y, boolean destroy) {
        return Node.pickEntity(mRoot, x, y, destroy);
    }
    
    public void collisionCheck(CollisionData data) {
        data.reset();
        Node.collisionCheck(mRoot, data);
    }

    public void draw(){            
        if(mLoaded == false) return;      
        Node.drawNodes(mRoot);       
    }
    
    public void update(int xoffset, int yoffset) {
        if(mLoaded == false) return;
                  
        mXOffset = xoffset;
        if(mXOffset < 0) mXOffset = 0;
        if(mXOffset > mWidth*32-PixelData.WIDTH) mXOffset = mWidth*32-PixelData.WIDTH;
        
        mYOffset = yoffset;          
        if(mYOffset < 0) mYOffset = 0;
        if(mYOffset > mHeight*32-PixelData.HEIGHT) mYOffset = mHeight*32-PixelData.HEIGHT;
        
        Node.updateNodes(mRoot);    
    }
    
    public boolean load(String filename) {   
        mLoaded = false;
        mTransition.reset();
        
        try {
            File file = new File(filename);
            if(!file.exists())
                return false;
            
            FileInputStream fileIs = new FileInputStream(file);
            ObjectInputStream is = new ObjectInputStream(fileIs);
            
            mWidth = is.readInt();
            mHeight = is.readInt();
            startPointX = is.readInt();
            startPointY = is.readInt();
            endLine = is.readInt();
            
            mRoot = new Node();
            mRoot.leftTopX = 0;
            mRoot.leftTopY = 0;
            mRoot.rightBotX = mWidth * SpriteSheet.level.SPRITE_SIZE;
            mRoot.rightBotY = mHeight * SpriteSheet.level.SPRITE_SIZE;
            Node.createNodes(mRoot,this);
            
            Node.loadNodes(mRoot, is);
            mLoaded = true;
        }
        catch(IOException e) {
        	System.out.println("ZONK");
        	
        }
        
        
        return mLoaded;
    }
    
    public void save(String filename) {        
        try {
            File file = new File(filename);
            if(!file.exists())
                file.createNewFile();
            
            FileOutputStream fileos = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fileos);
            
            os.writeInt(mWidth);
            os.writeInt(mHeight);
            os.writeInt(startPointX);
            os.writeInt(startPointY);
            os.writeInt(endLine);
            
            Node.saveNodes(mRoot, os);
            
            os.close();
        }
        catch(IOException e) {}
    }
    
    public void createNew(int width, int height) {
        mWidth = width;
        mHeight = height;
        mRoot = new Node();
        mRoot.leftTopX = 0;
        mRoot.leftTopY = 0;
        mRoot.rightBotX = mWidth * SpriteSheet.level.SPRITE_SIZE;
        mRoot.rightBotY = mHeight * SpriteSheet.level.SPRITE_SIZE;
        Node.createNodes(mRoot,this);
        mLoaded = true;
    }
    
    
    
    
    
}
