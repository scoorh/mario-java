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
import Graphics.PixelData;
import Graphics.Rotation;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Node {    
    static final int MIN_NODE_SIZE = SpriteSheet.level.SPRITE_SIZE * 10;
    static final int NODE_TILE_SIZE = 32;
    
    Level mLevel = null;
    
    public Node[] children = null;
    public Node parent = null;    
    public boolean leaf = false;
    public int leftTopX = 0, leftTopY = 0;
    public int rightBotX = 0, rightBotY = 0;
    
    public Sprite[][] tiles = null;
    public Entity[] entities = null;
    public boolean visible = false;
    
    
    public Node() {
        
    }

    public static void createNodes(Node node, Level level) {
       if(node == null) return;
   
       node.mLevel = level;
       
       if(node.rightBotX - node.leftTopX <= MIN_NODE_SIZE) {           
           node.leaf = true;
           
           int tilesY = (node.rightBotX - node.leftTopX) / NODE_TILE_SIZE;
           int tilesX = (node.rightBotY - node.leftTopY) / NODE_TILE_SIZE;
           node.tiles = new Sprite[tilesY][tilesX];           
           
           for(int i = 0; i < tilesY; ++i) {
               for(int j = 0; j < tilesX; ++j) {                   
                   node.tiles[i][j] = new Sprite(SpriteSheet.level.SPRITE_COUNT-1, SpriteSheet.level, Rotation.NONE, CollisionID.NONE);                   
               }               
           } 
       }
       else {
           node.leaf = false;
           node.children = new Node[4];
           
           int halfX = node.leftTopX + (node.rightBotX-node.leftTopX) / 2;
           int halfY = node.leftTopY + (node.rightBotY-node.leftTopY) / 2;
           
           
           if(node.rightBotY - node.leftTopY <= MIN_NODE_SIZE) {               
               for(int i = 0; i < 2; ++i) {
                   node.children[i] = new Node();
                   node.children[i].parent = node;
               }
               
               node.children[0].leftTopX = node.leftTopX; 
               node.children[0].leftTopY = node.leftTopY;
               node.children[0].rightBotX = halfX;
               node.children[0].rightBotY = node.rightBotY;
               
               node.children[1].leftTopX = halfX; 
               node.children[1].leftTopY = node.leftTopY;
               node.children[1].rightBotX = node.rightBotX; 
               node.children[1].rightBotY = node.rightBotY;       
               
               for(int i = 0; i < 4; ++i) 
                   createNodes(node.children[i], level);
               
           }
           else { 
                  
               for(int i = 0; i < 4; ++i) {
                   node.children[i] = new Node();
                   node.children[i].parent = node;
               }
               
                node.children[0].leftTopX = node.leftTopX; 
                node.children[0].leftTopY = node.leftTopY;
		node.children[0].rightBotX = halfX; 
                node.children[0].rightBotY = halfY;
						
		node.children[1].leftTopX = halfX; 
                node.children[1].leftTopY = node.leftTopY;
		node.children[1].rightBotX = node.rightBotX; 
                node.children[1].rightBotY = halfY;
		
		node.children[2].leftTopX = node.leftTopX; 
                node.children[2].leftTopY = halfY;
		node.children[2].rightBotX = halfX;
                node.children[2].rightBotY = node.rightBotY;
						
		node.children[3].leftTopX = halfX; 
                node.children[3].leftTopY = halfY;
		node.children[3].rightBotX = node.rightBotX;
                node.children[3].rightBotY = node.rightBotY;
               
               for(int i = 0; i < 4; ++i) 
                   createNodes(node.children[i],level);
           }
       }
    }
    

    public static void drawNodes(Node node) {
        if(node == null || node.visible == false) return;
        
        if(node.leaf) {
            for(int i = 0; i < node.tiles.length; ++i) {
                for(int j = 0; j < node.tiles[0].length; ++j) {                              
                    int x = i * SpriteSheet.level.SPRITE_SIZE + node.leftTopX - node.mLevel.mXOffset;
                    int y = j * SpriteSheet.level.SPRITE_SIZE + node.leftTopY - node.mLevel.mYOffset;
  
                    node.tiles[i][j].draw(x, y);                
                }
            }             
            
            
            if(node.entities != null) {
                for(int i = 0; i < node.entities.length; ++i) {
                    if(node.entities[i] != null) 
                        node.entities[i].draw(node.entities[i].x-node.mLevel.mXOffset, node.entities[i].y-node.mLevel.mYOffset);
                }
            }
        }
        else {
            for(int i = 0; i < 4; ++i)
                drawNodes(node.children[i]);
        }
    }
    
    public static void updateNodes(Node node) {
        if(node == null) return;
        
        node.visible = true;
        if(node.rightBotX < node.mLevel.mXOffset || node.rightBotY < node.mLevel.mYOffset)
            node.visible = false;
        if(node.leftTopX > node.mLevel.mXOffset + PixelData.WIDTH || node.leftTopY > node.mLevel.mYOffset + PixelData.HEIGHT)
            node.visible = false;
        
        if(node.visible && node.leaf == false) {
            for(int i = 0; i < 4; ++i)
                updateNodes(node.children[i]);
        }
        else if(node.visible && node.leaf == true) {
            if(node.entities != null) {
                for(int i = 0; i < node.entities.length; ++i) {
                    if(node.entities[i] != null)
                        node.entities[i].update();
                }
            }
        }
    }
    
    
    public static void collisionCheck(Node node, CollisionData data) {
        if(node == null) return;
                
        if((data.xpos >= node.leftTopX && data.xpos <= node.rightBotX) ||
                (data.xpos+data.size >= node.leftTopX && data.xpos+data.size <= node.rightBotX)) {
            
            if((data.ypos >= node.leftTopY && data.ypos <= node.rightBotY) ||
                    (data.ypos+data.size >= node.leftTopY && data.ypos+data.size <= node.rightBotY)){
            
            
                if(node.leaf) {                           
                    for(int i = 0; i < node.tiles.length; ++i) {
                        for(int j = 0; j < node.tiles[0].length; ++j) {
                            if(node.tiles[i][j].collision == CollisionID.NONE)
                                  continue;

                            int tileTY = j * NODE_TILE_SIZE + node.leftTopY;
                            int tileLX = i * NODE_TILE_SIZE + node.leftTopX;
                            int tileRX = tileLX+NODE_TILE_SIZE-1;
                            int tileBY = tileTY+NODE_TILE_SIZE-1;


                            if(tileLX <= data.xpos && tileRX >= data.xpos) {
                                if(tileTY <= data.ypos && tileBY >= data.ypos) {
                                    data.leftTop = node.tiles[i][j].collision;
                                    data.leftX = tileRX;
                                    data.topY = tileBY;
                                }
                                if(tileTY <= data.ypos+data.size && tileBY >= data.ypos+data.size) {
                                    data.leftBottom = node.tiles[i][j].collision;
                                    data.leftX = tileLX;
                                    data.bottomY = tileTY;
                                }
                            }
                            if(tileLX <= data.xpos+data.size && tileRX >= data.xpos+data.size) {
                                if(tileTY <= data.ypos && tileBY >= data.ypos) {
                                    data.rightTop = node.tiles[i][j].collision;
                                    data.rightX = tileLX;
                                    data.topY = tileBY;
                                }
                                if(tileTY <= data.ypos+data.size && tileBY >= data.ypos+data.size) {
                                    data.rightBottom = node.tiles[i][j].collision;
                                    data.rightX = tileRX;
                                    data.bottomY = tileTY;
                                }
                            }

                        }
                    } //end tiles check
                    
                    
                    if(node.entities != null) {
                        for(int i = 0; i < node.entities.length; ++i) {
                            if(node.entities[i] != null && node.entities[i].sprite.collision != CollisionID.NONE) {
                                CollisionID cid = node.entities[i].sprite.collision;
                                int LX = node.entities[i].x;
                                int RX = LX+Entity.ENTITY_SIZE;
                                int TY = node.entities[i].y;
                                int BY = TY+Entity.ENTITY_SIZE;
                                
                                if(LX <= data.xpos &&  RX >= data.xpos) {
                                    if(TY <= data.ypos && BY >= data.ypos) {
                                        data.leftTop = cid;
                                        data.leftX = RX;
                                        data.topY = BY;
                                    }
                                    if(TY <= data.ypos+data.size && BY >= data.ypos+data.size) {
                                        data.leftBottom = cid;
                                        data.leftX = RX;
                                        data.bottomY = TY;
                                    }
                                }
                                if(LX <= data.xpos+data.size && RX >= data.xpos+data.size) {
                                    if(TY <= data.ypos && BY >= data.ypos) {
                                        data.rightTop = cid;
                                        data.rightX = LX;
                                        data.topY = BY;
                                    }
                                    if(TY <= data.ypos+data.size && BY >= data.ypos+data.size) {
                                        data.rightBottom = cid;
                                        data.rightX = LX;
                                        data.bottomY = TY;
                                    }
                                }
                            }
                        }
                    }
                    
                }
                else {
                    for(int i = 0; i < 4; ++i) {
                        collisionCheck(node.children[i],data);
                    }
                }
            }       
        }
    }
    
    
    public static Entity addEntity(Node node, Entity entity) {
        if(node == null || node.visible == false) return null;
        
        if(entity.x >= node.leftTopX && entity.x <= node.rightBotX &&
                entity.y >= node.leftTopY && entity.y <= node.rightBotY) {
            
            if(node.leaf) {
                if(node.entities == null) {
                    node.entities = new Entity[1];
                    node.entities[0] = entity;
                }
                else {
                    boolean added = false;
                    for(int i = 0; i < node.entities.length; ++i) {
                        if(node.entities[i] == null) {
                            node.entities[i] = entity;
                            added = true;
                            break;
                        }
                    }
                    
                    if(added == false) {
                        Entity[] tmp = node.entities;
                        node.entities = new Entity[tmp.length+1];
                        for(int i = 0; i < tmp.length; ++i) {
                            node.entities[i] = tmp[i];
                        }
                        node.entities[tmp.length] = entity;
                    }                    
                }     
                return entity;
            }
            else {
                for(int i = 0; i < 4; ++i) {
                    Entity e = addEntity(node.children[i],entity);
                    if(e != null)
                        return e;
                }
            }
        }
        
        return null;
    }
    
    public static Entity pickEntity(Node node, int x, int y, boolean destroy) {
        if(node == null || node.visible == false) return null;
        
        int realX = x;
        int realY = y;
  //      int realX = x + node.mLevel.mXOffset;
  //      int realY = y + node.mLevel.mYOffset;
        if(realX >= node.leftTopX && realX <= node.rightBotX &&
                realY >= node.leftTopY && realY <= node.rightBotY) {

            if(node.leaf) {
                if(node.entities == null) return null;
                              
                for(int i = 0; i < node.entities.length; ++i) {
                    if(node.entities[i] != null) {                        
                        if(realX >= node.entities[i].x && realX <= node.entities[i].x+Entity.ENTITY_SIZE &&
                                realY >= node.entities[i].y && realY <= node.entities[i].y+Entity.ENTITY_SIZE) {
                                
                            if(destroy)
                            {                                
                                Entity e = node.entities[i];
                                node.entities[i] = null;                                
                                return e;                                
                            }
                            return node.entities[i];
                        }
                    }
                }
           
            }
            else {
                for(int i = 0; i < 4; ++i) {
                    Entity entity = pickEntity(node.children[i],x,y,destroy);
                    if(entity != null)
                        return entity;
                }
            }
        }
        
        return null;
    }
    
    public static Sprite pickTile(Node node, int x, int y) {       
        if(node == null || node.visible == false) return null;
        
        
        int realX = x + node.mLevel.mXOffset;
        int realY = y + node.mLevel.mYOffset;
        if(realX >= node.leftTopX && realX <= node.rightBotX &&
                realY >= node.leftTopY && realY <= node.rightBotY) {
            
            if(node.leaf) {
                for(int i = 0; i < node.tiles.length; ++i) {
                    for(int j = 0; j < node.tiles[0].length; ++j) {
                        int tileY = j * SpriteSheet.level.SPRITE_SIZE + node.leftTopY;
                        int tileX = i * SpriteSheet.level.SPRITE_SIZE + node.leftTopX;
                        
                        if(realX >= tileX && realX <= tileX + SpriteSheet.level.SPRITE_SIZE &&
                                realY >= tileY && realY <= tileY + SpriteSheet.level.SPRITE_SIZE) 
                            return node.tiles[i][j];
                    }
                }
            }
            else {
                for(int i = 0; i < 4; ++i) {
                    Sprite tile = pickTile(node.children[i],x,y);
                    if(tile != null)
                        return tile;
                }
            }
        }
        
        return null;
    }
    
    
    public static void saveNodes(Node node, ObjectOutputStream os) throws IOException {
        if(node != null && node.leaf == false) {
            for(int i = 0; i < 4; ++i) 
                saveNodes(node.children[i], os);
        }
        else if(node != null) {
            for(int i = 0; i < node.tiles.length; ++i) {
                for(int j = 0; j < node.tiles[0].length; ++j) {
                    node.tiles[i][j].save(os);
                }
            }
            
            if(node.entities != null) {
                int nls = 0;
                for(int i = 0; i < node.entities.length; ++i) {
                    if(node.entities[i] == null)
                        ++nls;
                }
                
                os.writeInt(node.entities.length-nls);
                 
                for(int i = 0; i < node.entities.length; ++i) {
                    if(node.entities[i] != null)
                        node.entities[i].save(os);
                }
            }                
            else
                os.writeInt(0);
        }
    }
    
    public static void loadNodes(Node node, ObjectInputStream is) throws IOException {
        if(node != null && node.leaf == false) {
            for(int i = 0; i < 4; ++i)
                loadNodes(node.children[i], is);
        }
        else if(node != null) {
            for(int i =0; i < node.tiles.length; ++i) {
                for(int j = 0; j < node.tiles[0].length; ++j) 
                    node.tiles[i][j].load(is);                    
            }        
                
            int ecount = is.readInt();
            if(ecount != 0) {
                node.entities = new Entity[ecount];
            
                int id;
                for(int i = 0; i < ecount; ++i) {
                    id = is.readInt();
                    
                    node.entities[i] = Entity.createFromID(id);                    
                    node.entities[i].load(is);
                }
            }
        }
    }
    
}
