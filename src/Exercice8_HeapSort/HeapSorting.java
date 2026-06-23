public class HeapSorting {

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    public static void sort(Comparable[] a) {
        int N = a.length;
        heapify(a, N);
        sortdown(a, N);
    }

    public static void heapify(Comparable[] a, int N) {
        for (int k = parent(N - 1); k >= 0; k--) {
            sink(a, k, N);
        }
    }

    public static void sortdown(Comparable[] a, int N) {
        while (N > 1) {
            exch(a, 0, N - 1);
            N--;
            sink(a, 0, N);
        }
    }

    public static void sink(Comparable[] a, int k, int N) {
        while (left(k) < N) {
            int j = left(k);
            if (j < N - 1 && less(a[j], a[right(k)])) {
                j = right(k);
            }
            if (!less(a[k], a[j])) {
                break;
            }
            exch(a, k, j);
            k = j;
        }
    }

    public static int parent(int k) {
        return (k - 1) / 2;
    }

    public static int left(int k) {
        return 2 * k + 1;
    }

    public static int right(int k) {
        return 2 * k + 2;
    }

}

