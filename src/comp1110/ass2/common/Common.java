package comp1110.ass2.common;

/**
 * Author: Xinjie Wang, Jiaan Guo, Xiang Lu
 */
public class Common {

    private Bag bag;
    private Centre centre;
    private Discard discard;
    private Factory[] factories;

    public Common() {
        init();
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Discard getDiscard() {
        return discard;
    }

    public void setDiscard(Discard discard) {
        this.discard = discard;
    }

    public Factory[] getFactories() {
        return factories;
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
        for (int i = 0; i < 5; i++) {
            factories[i] = new Factory(i);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('F');
        for (Factory factory : this.factories) {
            stringBuilder.append(factory.toString());
        }
        stringBuilder.append('C');
        stringBuilder.append(this.centre);
        stringBuilder.append('B');
        stringBuilder.append(this.bag);
        stringBuilder.append('D');
        stringBuilder.append(this.discard);
        return stringBuilder.toString();
    }
}
