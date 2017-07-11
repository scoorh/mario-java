/*
 *
 *
 * 
 *
 *
 *
 */
package Level;

import App.Input;
import App.Sound;
import Graphics.CollisionData;
import Graphics.CollisionID;
import Graphics.PixelData;
import Graphics.Rotation;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Player  {
    public enum STATE {
        IDLE,
        JUMPING,
        FALLING,
        MOVING,
        SWIMMING,
        DIED,
        RESETING
    };
    public enum MOVE_DIR {
        NONE,
        LEFT,
        RIGHT
    };
    
    Level mLevel;
    Enemy mEnemy;
    
    public int x;
    public int y;
    CollisionData collision;
    Sprite sprite;
    
    int spawnx;
    int spawny;
    int endLine;
       
    STATE state;
    MOVE_DIR moveDir;
    
    long animTimer = 0;
    long currMaxJump = 0;
    long currJump = 0;
    
    static final int moveSpeed = 4;    
    static final int maxJump = 180;
    static final int jumpMod = 60;

    public Player(Level level, Enemy enemy) {
        mLevel = level;
        mEnemy = enemy;
         
        collision= new CollisionData();
        sprite = new Sprite(0, SpriteSheet.character, Rotation.NONE, CollisionID.NONE);
        
        state = STATE.IDLE;
        moveDir = MOVE_DIR.NONE;
    }    
        
    public void setSpawn(int sx, int sy) {
        x =sx;
        y =sy;
        spawnx = sx;
        spawny =sy;
    }

    public void draw() {    
        if(state == STATE.RESETING) {
            if(animTimer >= 90) {
                animTimer = 0;
                state = STATE.IDLE;
                x = spawnx;
                y = spawny;
                Sound.MainTheme();
            }
            else return;
        }
        
        int drawX, drawY;
        if(x < PixelData.HALF_WIDTH-24)
            drawX = x;
        else if(x > mLevel.mWidth*32 - PixelData.HALF_WIDTH-24) 
            drawX =  PixelData.WIDTH  - (mLevel.mWidth*32-x);
        else drawX = PixelData.HALF_WIDTH-24;
              
        if(y < PixelData.HALF_HEIGHT-16)
            drawY = y;
        else drawY = PixelData.HALF_HEIGHT-16;
              
        sprite.draw(drawX, drawY);
    }  
    
        
    public void update() {   
        if(state != STATE.DIED && state != STATE.RESETING)
            enemyCollisionCheck();
        else if(state == STATE.DIED || state == STATE.RESETING) {
            animation();
            return;
        }
        
        levelCollisionCheck();
                    
        if(state == STATE.FALLING) {
            falling();
        }
        else if(state == STATE.JUMPING) {
            jumping();
        }
        
        move(); 
        if(moveDir != MOVE_DIR.NONE && state == STATE.IDLE)
            state = STATE.MOVING;
        else if(state == STATE.MOVING && moveDir == MOVE_DIR.NONE) {
            state = STATE.IDLE;
        }        
        
        animation();
    }
    
    void enemyCollisionCheck() {
        for(int i = 0; i < mEnemy.mData.length; ++i) {
            Enemy.Data d = mEnemy.mData[i];                     
            boolean dead = false;
            
            if(x >= d.x && x <= d.x+40) {
                if((y >= d.y && y <= d.y+40) || (y+40 >= d.y && y+40 <= d.y+40))
                    dead = true;
            }
            else if(x+40 >= d.x && x+40 <= d.x+40) {
                 if((y >= d.y && y <= d.y+40) || (y+40 >= d.y && y+40 <= d.y+40))
                    dead = true;
            }
            
            if(dead) {
                state = STATE.DIED;
                animTimer = 0;
                Sound.PlayerDied();
                return;
            }            
        }
    }
      
    void levelCollisionCheck() {
        collision.reset();
        collision.xpos = x;
        collision.ypos = y;
        collision.size = 48;
        mLevel.collisionCheck(collision);
        
        int px = -1, py = -1;
        if(collision.rightTop == CollisionID.PICKABLE) {
            px = collision.rightX;
            py = collision.topY;          
        }
        if(collision.leftTop == CollisionID.PICKABLE) {
            px = collision.leftX;
            py = collision.topY;
        }
        if(collision.leftBottom == CollisionID.PICKABLE) {
            px = collision.leftX;
            py = collision.bottomY;
        }
        if(collision.rightBottom == CollisionID.PICKABLE) {
            px = collision.rightX;
            py = collision.bottomY;
        }
        
        if(px != -1 && py != -1) {
            Entity e = mLevel.pickEntity(px, py, true);
            if(e.id == EntityID.COIN) {
                Sound.Coin();
            }        
        }
        
        
        if(collision.leftBottom == CollisionID.TERRAIN || collision.rightBottom == CollisionID.TERRAIN) {
            if(state == STATE.FALLING) {
                if(y < collision.bottomY - 25) {
                    y = collision.bottomY-sprite.sheet.SPRITE_SIZE;
                    state = STATE.IDLE;
                }
            }            
        }       
        else if(collision.leftBottom == CollisionID.NONE && collision.rightBottom == CollisionID.NONE) {
            if(state != STATE.JUMPING)
                state = STATE.FALLING;
        }
        else if(collision.leftTop == CollisionID.WATER && collision.rightTop == CollisionID.WATER) {
            state = STATE.SWIMMING;
            y -= 2;
        }
    }
    
    void falling() {
        y += moveSpeed;
        sprite.number = 1;
    }
    
    void jumping() {
        currJump += moveSpeed;
        y -= moveSpeed;
        if(currJump >= currMaxJump) {
            y += currJump - currMaxJump;
            currJump = 0;
            currMaxJump = 0;
            state = STATE.FALLING;            
        }
    }
    
    void swimming() {
        
    }
    
    void animation() {
        ++animTimer;
        
        if(state == STATE.IDLE)
            sprite.number = 0;
        else if(state == STATE.FALLING)
            sprite.number = 1;
        else if(state == STATE.JUMPING)
            sprite.number = 2;
        else if(state == STATE.MOVING) {
            
            if(animTimer >= 15) {
                animTimer = 0;
                sprite.number = 
                        sprite.number==3 ? 0 : 3;
            }
            
        }
        else if(state == STATE.SWIMMING) {
            if(animTimer >= 15) {
                animTimer = 0;
                ++sprite.number;
                if(sprite.number < 4 || sprite.number > 6) 
                    sprite.number = 4;                
            }
        }
        else if(state == STATE.DIED) {
            sprite.number = 7;
            if(animTimer >= 40 && animTimer < 50) {
                y -= moveSpeed;
            }
            if(animTimer >= 80 && animTimer < 120) {
                y += moveSpeed;
            }
            if(animTimer >= 120) {
                animTimer = 0;
                state = STATE.RESETING;
            }           
        }
    }
    
    void move() {
        moveDir = MOVE_DIR.NONE;
        
        if((Input.isKeyPressed(KeyEvent.VK_SPACE, false) || Input.isKeyPressed(KeyEvent.VK_UP, false))
                && state != STATE.FALLING) {
            if(state != STATE.JUMPING) {
                state = STATE.JUMPING;
                Sound.Jump();
            }
            
            currMaxJump += jumpMod;
            if(currMaxJump >= maxJump)
                currMaxJump = maxJump;
        }
        if(Input.isKeyPressed(KeyEvent.VK_LEFT, true))
            moveDir = MOVE_DIR.LEFT;
        if(Input.isKeyPressed(KeyEvent.VK_RIGHT, true))
            moveDir = MOVE_DIR.RIGHT;        
        
        if(moveDir == MOVE_DIR.LEFT) {
            sprite.rotation = Rotation.X;
            x -= moveSpeed;
        }
        else if(moveDir == MOVE_DIR.RIGHT) {
            sprite.rotation = Rotation.NONE;
            x += moveSpeed;
        }
    }
            
            
    
    void save(ObjectOutputStream os) throws IOException {
        
    }
    
    void load(ObjectInputStream is) throws IOException  {
        
    }
}
