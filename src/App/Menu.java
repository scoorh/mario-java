/*
 *
 *
 * 
 *
 *
 *
 */
package App;

import Graphics.Sprite;
import Graphics.SpriteSheet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Menu extends State {

    boolean gramy = false;
    
    JPanel panel;  //dodawaj buttony itp

    JButton start;
    
    public Menu(App app) {
        super(app);      
        panel = mApp.getPanel();
         
        start = new JButton("Start");
        start.setBounds(mApp.getWidth()/2-50,mApp.getHeight()/2-25,100,50);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gramy = true;
            }            
        });
        panel.add(start);          
        panel.add(mApp);    //canvas do panelu na koncu zeby buttony byly na gorze
    }
   
    
    /*
        Tu mozna narysowac jakies tlo za pomoca Sprite
    np : 
       
       	 Sprite sprite = new Sprite();
         sprite.number = 0;
         sprite.sheet = SpriteSheet.level;
         sprite.draw(x,y);
    */
    
    @Override
    void onDraw() {
    	
    	 
         
         
    }

    /*
        Tu mozna updejtowac rysowane tlo z onDraw
        zwroc true zeby przejsc do stanu Game
    */
    @Override
    boolean onUpdate() {  
        return gramy;
    }

    /*
        onPause bedzie odpalone przy zmianie z Menu na Game
    */
    @Override
    void onPause() {
        start.setVisible(false);   //schowaj wszystko zeby byl sam canvas
    }

    /*
        onResume bedzie odpalone przy zmianie z Game na Menu
    */
    @Override
    void onResume() {
        gramy = false;
        start.setVisible(true);  //pokaz
    }
    
}
