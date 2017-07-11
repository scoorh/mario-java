/*
 *
 *
 * 
 *
 *
 *
 */
package Level;

import Graphics.Sprite;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public abstract class Entity {
    public static int ENTITY_SIZE = 32;
    
    public int x, y;
    public EntityID id;
    public Sprite sprite = null;
    public int animTime = 0;
    public int currAnim = 0;   
    public int animSize = 0;
    
    long timer = 0;
    
    
    public Entity() {
        sprite = new Sprite();
    }
    
    void update() {
        ++timer;
        if(timer == animTime) {
            timer = 0;
            ++currAnim;
            ++sprite.number;
            if(currAnim == animSize) {
                sprite.number -= animSize;
                currAnim = 0;
            }
        }
    }    
    
    void draw(int xoffset, int yoffset) {
        sprite.draw(xoffset, yoffset);
    }    
      
    void save(ObjectOutputStream os) throws IOException {
        os.writeInt(id.ordinal());
        System.out.println(id.ordinal());
        os.writeInt(x);
        os.writeInt(y);
        sprite.save(os);
        os.writeInt(animTime);
        os.writeInt(currAnim);
        os.writeInt(animSize);
    }
    
    void load(ObjectInputStream is) throws IOException {
        x = is.readInt();
        y = is.readInt();
        sprite.load(is);
        animTime = is.readInt();
        currAnim = is.readInt();
        animSize = is.readInt();
    }
    
    public static Entity createFromID(int id) {
        switch(id) {
            case 0: return new Coin();
            default: return null;            
        }
    }
}
