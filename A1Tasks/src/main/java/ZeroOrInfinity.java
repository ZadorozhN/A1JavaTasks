import java.util.stream.IntStream;

public class ZeroOrInfinity {
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 1 || n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static double zeroOrInfinityFunction(int n) {
        return (1. / factorial(n)) * IntStream.range(1, n + 1).map(i -> factorial(i)).sum();
    }
}
