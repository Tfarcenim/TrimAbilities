package tfar.trimabilities;

@FunctionalInterface
public interface BooleanFunction<V> {
    boolean apply(V value);
}
