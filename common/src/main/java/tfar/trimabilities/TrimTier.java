package tfar.trimabilities;

public enum TrimTier {
    A(-1),B(-2),C(-3);
    public final int passive;

    TrimTier(int passive) {
        this.passive = passive;
    }
}
