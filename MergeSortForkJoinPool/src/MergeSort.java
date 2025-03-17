import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MergeSort {
    private static final int THRESHOLD = 10;
    private static final int RANGE = 100;

    public static void main(String[] args) {
        // losowanie wartosci do tabeli
        Random random = new Random();
        int[] array = new int[100000];
        int[] count = new int[RANGE];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(RANGE);
            count[array[i]]++;
        }

        //do wypisania do sprawdzenia ile czego jest
        for (int i = 0; i < count.length; i++) {
            System.out.println(i + ": " + count[i]);
        }


        // glowne nasze zadanie
        ForkJoinPool pool = new ForkJoinPool();
        MergeSortTask task = new MergeSortTask(array);
        int[] sortedArray = pool.invoke(task);

        System.out.println("Podstawowa tablica: " + Arrays.toString(array));
        System.out.println("Posortowana tablica: " + Arrays.toString(sortedArray));
    }

    static class MergeSortTask extends RecursiveTask<int[]> {
        private final int[] array;

        public MergeSortTask(int[] array) {
            this.array = array;
        }

        @Override
        protected int[] compute() {
            if (array.length <= THRESHOLD) {
                return sequentialSort(array);
            }

            int pivot = array.length / 2;
            MergeSortTask leftTask = new MergeSortTask(Arrays.copyOfRange(array, 0, pivot));
            MergeSortTask rightTask = new MergeSortTask(Arrays.copyOfRange(array, pivot, array.length));

            leftTask.fork();
            int[] rightResult = rightTask.compute();
            int[] leftResult = leftTask.join();

            return merge(leftResult, rightResult);
        }

        private int[] sequentialSort(int[] array) {
            Arrays.sort(array);
            return array;
        }

        private int[] merge(int[] left, int[] right) {
            int[] result = new int[left.length + right.length];
            int i = 0, j = 0, k = 0;

            while (i < left.length && j < right.length) {
                if (left[i] <= right[j]) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }

            while (i < left.length) {
                result[k++] = left[i++];
            }

            while (j < right.length) {
                result[k++] = right[j++];
            }

            return result;
        }
    }
}
