/*
 *
 *
 * 
 *
 *
 *
 */
package Graphics;


public class CollisionData {    
    public int xpos;
    public int ypos;
    public int size;
    
    
    public CollisionID leftTop;
    public CollisionID leftBottom;
    public CollisionID rightTop;
    public CollisionID rightBottom;
    
    public int leftX;
    public int rightX;
    public int topY;
    public int bottomY;
    
    
    public void reset() {
        leftTop = leftBottom = rightTop = rightBottom = CollisionID.NONE;
    }
    
    
}
