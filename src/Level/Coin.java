/*
 *
 *
 * 
 *
 *
 *
 */
package Level;

import java.io.IOException;
import java.io.ObjectInputStream;


public class Coin extends Entity {
    
    public Coin() {
        super();
    }

    @Override
    void load(ObjectInputStream is) throws IOException {
        super.load(is);
        id = EntityID.COIN;
    }
    
}
