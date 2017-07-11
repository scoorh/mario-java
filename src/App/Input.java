/*
 *
 *
 * 
 *
 *
 *
 */
package App;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Input implements KeyListener, MouseListener, MouseMotionListener {

    static long mFrameStamp = 0;
    static boolean[] mKeyState = new boolean[256];
    static long[] mKeyStamp = new long[256];
    static boolean[] mMouseState = new boolean[3];
    static long[] mMouseStamp = new long[3];
    static int mMouseX;
    static int mMouseY;
    
    public static void update() {
        ++mFrameStamp;
    }
    
    public static int getMouseX() { return mMouseX; }
    public static int getMouseY() { return mMouseY; }
    public static boolean isKeyPressed(int key, boolean ignoreStamp) {
        if(mKeyState[key]) {
            boolean pressed = true;
            if(ignoreStamp == false) {
                if(mKeyStamp[key] == mFrameStamp || mKeyStamp[key] == mFrameStamp-1)
                    pressed = false;
            }
            
            mKeyStamp[key] = mFrameStamp;
            return pressed;
        }
        return false;
    }
    public static boolean isMousePressed(int button, boolean ignoreStamp) {
        if(mMouseState[button]) {
           boolean pressed = true;
            if(ignoreStamp == false) 
		if(mMouseStamp[button] == mFrameStamp || mMouseStamp[button] == mFrameStamp-1)
			pressed = false;
			
            mMouseStamp[button] = mFrameStamp;
            return pressed;
        }		
        return false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mKeyState[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mKeyState[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mMouseState[e.getButton()-1] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mMouseState[e.getButton()-1] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mMouseX = e.getX();
        mMouseY = e.getY();
    }
    
    
    
}
