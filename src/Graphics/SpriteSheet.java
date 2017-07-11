/*
 *
 *
 * 
 *
 *
 *
 */
package Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


public class SpriteSheet {
    public final SpriteSheetID SHEET_ID;
    public final int SHEET_WIDTH;
    public final int SHEET_HEIGHT;
    public final int SPRITE_SIZE;
    public final int SPRITE_COUNT;
    public final int[] PIXELS;  
       
    
    public static final SpriteSheet level = new SpriteSheet("/Texture/level.png",32,384,384,SpriteSheetID.LEVEL_SPRITES);
    public static final SpriteSheet character = new SpriteSheet("/Texture/character.png",48,384,384,SpriteSheetID.CHARACTER_SPRITES);
    
    
    
    
    public static SpriteSheet getSheet(int id) {
        SpriteSheetID sid = SpriteSheetID.values()[id];
        switch(sid) {
            case LEVEL_SPRITES: return level;    
            case CHARACTER_SPRITES: return character;
            default:   return null;
        }
    }
            
    
    public SpriteSheet(String filename, int spriteSize, int sheetWidth, int sheetHeight, SpriteSheetID sheetID) {
        SHEET_ID = sheetID;
        SHEET_WIDTH = sheetWidth;
        SHEET_HEIGHT = sheetHeight;
        SPRITE_SIZE = spriteSize;
        SPRITE_COUNT = SHEET_WIDTH / SPRITE_SIZE * SHEET_HEIGHT / SPRITE_SIZE;
        
        PIXELS = new int[SHEET_WIDTH * SHEET_HEIGHT];
        try {
            BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(filename));
            image.getRGB(0,0,SHEET_WIDTH,SHEET_HEIGHT,PIXELS,0,SHEET_WIDTH);
        }
        catch(IOException e) {}
    }
    
    public int getSpriteRow(int number) {
        return (number / (SHEET_WIDTH/SPRITE_SIZE)) * SPRITE_SIZE;
    }
    
    public int getSpriteCol(int number) {
        return (number % (SHEET_HEIGHT/SPRITE_SIZE)) * SPRITE_SIZE;
    }
}
