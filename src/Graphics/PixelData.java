/*
 *
 *
 * 
 *
 *
 *
 */
package Graphics;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class PixelData {
    public static final int CLEAR_PIXEL = 0x87ceeb;
    public static final int IGNORE_PIXEL = 0xffff00ff;
    
    public static int[] data = null;
    public static int WIDTH;
    public static int HEIGHT;
    public static int PIXEL_COUNT;
    
    public static int HALF_WIDTH;
    public static int HALF_HEIGHT;
    
    static BufferedImage mImage = null;
    
    public PixelData() {
        WIDTH = 32*30;
        HEIGHT = 32*15;
        HALF_WIDTH = WIDTH/2;
        HALF_HEIGHT = HEIGHT/2;
        PIXEL_COUNT = WIDTH * HEIGHT;    

        mImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        data = ((DataBufferInt)mImage.getRaster().getDataBuffer()).getData();
    }
    
    public static void clear() {
        for(int i = 0; i < PIXEL_COUNT; ++i) {
            data[i] = CLEAR_PIXEL;
        }
    }
    
    public static void present(Canvas canvas) {
        BufferStrategy bs = canvas.getBufferStrategy();
	if(bs == null){
            canvas.createBufferStrategy(3);
            return;
	}
		
	Graphics g = bs.getDrawGraphics();
	g.drawImage(mImage, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
	g.dispose();		
	bs.show();
    }
    
}
