package tfar.trimabilities;

public enum TrimTier {
    A(-1,2),B(-2, 1),C(-3, 0);
    public final int passive;
    public final int active;
    TrimTier(int passive,int active) {
        this.passive = passive;
        this.active = active;
    }
}
