package comp1110.ass2.common;

public class Common {
    private Bag bag;
    private Centre centre;
    private Discard discard;
    private Factory[] factories;

    public Common() {
    }

    public Bag getBag() {
        return bag;
    }

    public Centre getCentre() {
        return centre;
    }

    public Discard getDiscard() {
        return discard;
    }

    public Factory[] getFactories() {
        return factories;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public void setDiscard(Discard discard) {
        this.discard = discard;
    }

    public void setFactories(Factory[] factories) {
        this.factories = factories;
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
