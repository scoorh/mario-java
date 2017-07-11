/*
 *
 *
 * 
 *
 *
 *
 */
package Graphics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Sprite {
    public int number = 0;
    public SpriteSheet sheet = null;
    public Rotation rotation = Rotation.NONE;
    public CollisionID collision = CollisionID.NONE;
    
    
    public Sprite(int number, SpriteSheet sheet, Rotation rotation, CollisionID collision) {
        this.number = number;
        this.sheet = sheet;
        this.rotation = rotation;
        this.collision = collision;
    }

    public Sprite() {
    }    
    
    
    public void save(ObjectOutputStream os) throws IOException {
        os.writeInt(number);
        os.writeInt(sheet.SHEET_ID.ordinal());
        os.writeInt(collision.ordinal());
        os.writeInt(rotation.ordinal());        
    }
    
    public void load(ObjectInputStream is) throws IOException {
        number = is.readInt();
        sheet = SpriteSheet.getSheet(is.readInt());
        collision = CollisionID.values()[is.readInt()];
        rotation = Rotation.values()[is.readInt()];
    }
    

    public void draw(int xoffset, int yoffset) {
        if(sheet == null) return;
        
        int sRow = sheet.getSpriteRow(number);
	int sCol = sheet.getSpriteCol(number);
	int bbRow;
	int bbCol;
	int sPixelIndex;
	int bbPixelIndex;
				
        
	for(int r = 0; r < sheet.SPRITE_SIZE; ++r) {
            for(int c = 0; c < sheet.SPRITE_SIZE; ++c) {
		sPixelIndex = (sCol+c) + (sRow+r)*sheet.SHEET_WIDTH;
		if(sheet.PIXELS[sPixelIndex] == PixelData.IGNORE_PIXEL) 	
                    continue;				
				
		bbRow = 0;
		bbCol = 0;				
					
                switch(rotation) {
                    case NONE:
                        bbRow = r + yoffset;				
                        bbCol = c + xoffset;
                        break;
                    case X:
                        bbRow = r + yoffset;
                        bbCol = sheet.SPRITE_SIZE-1-c + xoffset;
                        break;
                    case Y:
                        bbRow = sheet.SPRITE_SIZE-1-r + yoffset;
                        bbCol = c + xoffset;
                        break;
                    case XY:
                        bbRow = sheet.SPRITE_SIZE-1-r + yoffset;
                        bbCol = sheet.SPRITE_SIZE-1-c + xoffset;
                        break;
                }
				
		if(bbRow < 0 || bbRow >= PixelData.HEIGHT ||
                    bbCol < 0 || bbCol >= PixelData.WIDTH)
			continue;
				
                bbPixelIndex = bbCol + bbRow*PixelData.WIDTH;					
								
		PixelData.data[bbPixelIndex] = sheet.PIXELS[sPixelIndex];							
            }	
        }		
    }
}
