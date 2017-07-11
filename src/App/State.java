/*
 *
 *
 * 
 *
 *
 *
 */
package App;


public abstract class State {    
    
    protected App mApp;
    
    public State(App app) {
        mApp = app;
    }
    
    abstract void onDraw();
    abstract boolean onUpdate();
    abstract void onPause();
    abstract void onResume();
}
