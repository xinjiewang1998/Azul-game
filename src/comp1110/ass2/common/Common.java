package comp1110.ass2.common;

public class Common {
    private Bag bag;
    private Centre centre;
    private Discard discard;
    private Factory[] factories;

    public Common() {
    }

    /**
     * Initialize the Common area.
     */
    public void init() {
        this.bag = new Bag();
        this.centre = new Centre();
        this.discard = new Discard();
        this.factories = new Factory[5];
        for (int i = 0;i<5;i++){
            factories[i] = new Factory();
        }
    }

    /**
     * Cleanup the common area after the game.
     */
    public void cleanUp() {
        // FIXME
    }

    @Override
    public String toString() {
        //FIXME
        return "";
    }
}
