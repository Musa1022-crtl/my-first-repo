import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class tryHeapsort {

    // ===================== TIMING =====================
    public static long startTime() {
        return System.nanoTime();
    }

    public static long endTime(long start) {
        return System.nanoTime() - start;
    }

    // ===================== SWAP =====================
    public static void swap(String[] arr, int i, int j) {
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // ===================== HEAPIFY DOWN =====================
    public static void heapifyDown(String[] arr, int size, int i) {
        int largest = i;

        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < size && arr[left].compareTo(arr[largest]) > 0) {
                largest = left;
            }

            if (right < size && arr[right].compareTo(arr[largest]) > 0) {
                largest = right;
            }

            if (largest != i) {
                swap(arr, i, largest);
                i = largest;
            } else {
                break;
            }
        }
    }

    // ===================== HEAPIFY UP =====================
    public static void heapifyUp(String[] arr, int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;

            if (arr[i].compareTo(arr[parent]) > 0) {
                swap(arr, i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    // ===================== BOTTOM-UP BUILD =====================
    public static void buildHeapBottomUp(String[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyDown(arr, n, i);
        }
    }

    // ===================== TOP-DOWN BUILD =====================
    public static void buildHeapTopDown(String[] arr) {
        for (int i = 1; i < arr.length; i++) {
            heapifyUp(arr, i);
        }
    }

    // ===================== HEAPSORT =====================
    public static void heapSort(String[] arr) {
        int n = arr.length;

        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            heapifyDown(arr, i, 0);
        }
    }

    // ===================== FILE LOADING =====================
    public static String[] loadWordsFromFile(String filename) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // Split into words
        String text = content.toString().toLowerCase();

        // Remove punctuation (basic cleaning)
        text = text.replaceAll("[^a-zA-Z ]", " ");

        return text.split("\\s+");
    }

    // ===================== DISPLAY =====================
    public static void printResults(String label, String[] arr, long time) {
        System.out.println(label + " (First 20 words):");

        for (int i = 0; i < Math.min(20, arr.length); i++) {
            System.out.print(arr[i] + " ");
        }

        System.out.println("\nTime: " + time + " ns");
        System.out.println("--------------------------------------");
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {

        // ===== SMALL TEST =====
        String[] testWords = {
                "banana", "apple", "grape", "orange", "pear",
                "kiwi", "mango", "lemon", "peach", "plum"
        };

        System.out.println("===== SMALL TEST =====");

        String[] bottomTest = Arrays.copyOf(testWords, testWords.length);
        String[] topTest = Arrays.copyOf(testWords, testWords.length);

        buildHeapBottomUp(bottomTest);
        heapSort(bottomTest);

        buildHeapTopDown(topTest);
        heapSort(topTest);

        System.out.println("Bottom-Up Sorted: " + Arrays.toString(bottomTest));
        System.out.println("Top-Down Sorted: " + Arrays.toString(topTest));

        // ===== LARGE DATA (ULYSSES) =====
        System.out.println("\n===== LARGE DATA TEST =====");

        String[] words = loadWordsFromFile("ulysses_cleaned.txt");

        String[] bottomUpArr = Arrays.copyOf(words, words.length);
        String[] topDownArr = Arrays.copyOf(words, words.length);

        // ----- Bottom-Up Timing -----
        long start1 = startTime();

        buildHeapBottomUp(bottomUpArr);
        heapSort(bottomUpArr);

        long time1 = endTime(start1);

        // ----- Top-Down Timing -----
        long start2 = startTime();

        buildHeapTopDown(topDownArr);
        heapSort(topDownArr);

        long time2 = endTime(start2);

        // ----- Display -----
        printResults("Bottom-Up", bottomUpArr, time1);
        printResults("Top-Down", topDownArr, time2);
    }
}