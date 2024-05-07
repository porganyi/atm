public interface Checkable {

    default void check(boolean condition, String errorMsg) {
        if (condition) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
