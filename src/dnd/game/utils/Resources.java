package dnd.game.utils;

public class Resources {
    private int maxResources;
    private int currentResources;
    private final int cost;

    public Resources(int maxResources, int cost) {
        this.maxResources = maxResources;
        currentResources = maxResources;
        this.cost = cost;
    }

    private boolean canCast() {
        return currentResources >= cost;
    }

    public void consume() {
        if (!canCast())
            throw new IllegalStateException("Cannot cast ability - not enough resources");
        currentResources -= cost;
    }

    public void regenerate(int amount) {
        currentResources = Math.min(maxResources, currentResources + amount);
    }

    public void increaseMaxResources(int amount) {
        maxResources += amount;
    }

    public int getCurrentResources() {
        return currentResources;
    }

    public int getMaxResources() {
        return maxResources;
    }
}
