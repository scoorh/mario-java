package Graphics;

public class Transition {
	
	public static final int FADE_IN_SPEED = 2;
	public static final int FADE_OUT_SPEED = 2;
	
	int fadeIn = 0;
    int fadeOut = 0;
	
	
	public Transition() {
		
	}
	
	
	public void reset() {
		fadeIn = 0;
		fadeOut = 0;
	}
	
	public boolean processFadeIn() {
		for(int i = 0; i < PixelData.data.length; ++i) {
			if(PixelData.data[i] == PixelData.IGNORE_PIXEL) continue;
			
			int a = (PixelData.data[i] & 0xff000000) >> 24;
			int r = ((PixelData.data[i] & 0xff0000) >> 16) - 255 + fadeIn;
			int g = ((PixelData.data[i] & 0xff00) >> 8) - 255 + fadeIn;
			int b = (PixelData.data[i] & 0xff) - 255 + fadeIn;
			
			if(r < 0) r = 0;
			if(g < 0) g = 0;
			if(b < 0) b = 0;
						
			PixelData.data[i] = a << 24 | r << 16 | g << 8 | b;
			
		}
		
		fadeIn+=FADE_IN_SPEED;
		return fadeIn >= 255;
	}
	
	public boolean processFadeOut() {
		for(int i = 0; i < PixelData.data.length; ++i) {
			if(PixelData.data[i] == PixelData.IGNORE_PIXEL) continue;
			
			int a = (PixelData.data[i] & 0xff000000) >> 24;
			int r = ((PixelData.data[i] & 0xff0000) >> 16) - fadeOut;
			int g = ((PixelData.data[i] & 0xff00) >> 8) - fadeOut;
			int b = (PixelData.data[i] & 0xff) - fadeOut;
		
			if(r < 0) r = 0;
			if(g < 0) g = 0;
			if(b < 0) b = 0;
					
			PixelData.data[i] = a << 24 | r << 16 | g << 8 | b;
		}
		
		fadeOut+=FADE_OUT_SPEED;
		return fadeOut >= 255;
	}
}
