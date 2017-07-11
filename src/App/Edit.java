/*
 *
 *
 * 
 *
 *
 *
 */
package App;

import Graphics.CollisionData;
import Graphics.CollisionID;
import Graphics.PixelData;
import Graphics.Rotation;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import Level.Coin;
import Level.Entity;
import Level.EntityID;
import Level.Level;
import Level.Node;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/*
    do edycji mapy, olać D:


*/


public class Edit extends State {
     
    Level mLevel;
    int x = 1950;
    int y = 400;
    
    boolean pickTiles = false;
    boolean pickEntities=  false;
    Sprite brush = new Sprite();
    
    Sprite p = new Sprite();
    
    CollisionData cd=  new CollisionData();
    
    
    public Edit(App app) {
        super(app);
        
        mLevel = new Level();
        
   //    mLevel.createNew(128, 32);
      mLevel.load("res/Level/Level2");
       mLevel.setStartPoint(0, 800);
       mLevel.setEndLine(4096);
        
        x = mLevel.startPointX;
        y = mLevel.startPointY;
         
        p.sheet = SpriteSheet.getSheet(0);
        p.number = 143;
 
    }

    @Override
    void onDraw() {
        mLevel.draw();
 //      p.draw(32,50*32);
 //       p.draw(PixelData.WIDTH/2-16, PixelData.HEIGHT/2-16);
    }

    
    static int c=0;
    @Override              
    boolean onUpdate() {
        if(Input.isKeyPressed(KeyEvent.VK_S, false))
            mLevel.save("res/Level/Level2");
        if(Input.isKeyPressed(KeyEvent.VK_L, false))
            mLevel.load("res/Level/Level2");
        
        
         if(Input.isKeyPressed(KeyEvent.VK_UP, true)) {
			y -= 5;
		}
		if(Input.isKeyPressed(KeyEvent.VK_DOWN, true)) {
			y += 5;
		}
		if(Input.isKeyPressed(KeyEvent.VK_LEFT, true)) {
			x -= 5;
		}
		if(Input.isKeyPressed(KeyEvent.VK_RIGHT, true)) {
			x += 5;
		}
                
                
             //  y+=5;
        cd.xpos = x;
        cd.ypos = y;
        cd.size = 32;        
        mLevel.collisionCheck(cd);  
        
       // System.out.println(cd.leftTop + " " + cd.rightTop + " " + cd.leftBottom + " " + cd.rightBottom);
       // System.out.println(x+" "+y);
                
        
        
        mLevel.update(x-PixelData.WIDTH/2+16,y-PixelData.HEIGHT/2+16);
        
        
        
        
        
        editops();
        
        if(Input.isMousePressed(0, false)) {
			if(pickTiles) {
				Sprite t = mLevel.pickTile(Input.getMouseX(), Input.getMouseY());
				if(t != null) {
					t.number = brush.number;
					t.sheet = brush.sheet;
					t.collision = brush.collision;
					t.rotation = brush.rotation;
				}
                                
                                
				t = null;
			}
			
		}
        
        return false;
    }
    
    void editops() {
        if(Input.isKeyPressed(KeyEvent.VK_E, false)) {
                        JTextField x = new JTextField();
                        JTextField y = new JTextField();                        
			Object[] msg = {
                                "X: ", x,
                                "Y: ", y
			};			
			if(JOptionPane.showConfirmDialog(null, msg, "EditTile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
					== JOptionPane.CANCEL_OPTION)
				return;				
						
                            Coin e = (Coin)Entity.createFromID(0);
                            e.id = EntityID.COIN;
                            e.animSize = 6;
                            e.animTime = 5;
                            e.currAnim = 0;                        
                            e.sprite = new Sprite();
                            e.sprite.number = 60;
                            e.sprite.collision = CollisionID.PICKABLE;
                            e.sprite.sheet = SpriteSheet.level;
                            e.x = Integer.parseInt(x.getText());
                            e.y = Integer.parseInt(y.getText());
                           mLevel.addEntity(e);
                        
                        
        }
        if(Input.isKeyPressed(KeyEvent.VK_T, false)) {
			JTextField number = new JTextField();
			JTextField sheet = new JTextField();
			JTextField collision = new JTextField();
			JTextField rotation = new JTextField();
			Object[] msg = {
				"Tile number: ", number,
				"Sprite sheet id: ", sheet,
				"Collision: ", collision,
				"Rotation: ", rotation
			};			
			if(JOptionPane.showConfirmDialog(null, msg, "EditTile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
					== JOptionPane.CANCEL_OPTION)
				return;				
			brush.number = Short.parseShort(number.getText());
			if(sheet.getText().length() != 0)
				brush.sheet = SpriteSheet.getSheet(Integer.parseInt(sheet.getText()));	
			if(collision.getText().length() != 0)
				brush.collision = CollisionID.values()[Integer.parseInt(collision.getText())];
			if(rotation.getText().length() != 0)
				brush.rotation = Rotation.values()[Integer.parseInt(rotation.getText())];
			
			pickTiles = true;
			
		}
    }

    @Override
    void onPause() {
    }

    @Override
    void onResume() {
    }
    
}
