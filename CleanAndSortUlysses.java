import java.io.*;
import java.util.*;

public class CleanAndSortUlysses {

    public static void main(String[] args) {
        // Input file location
        String inputFile = "C:\\Users\\CLC USER\\Desktop\\16Practical\\ulysses_raw.txt";
        // Output file location (safe inside project folder)
        String outputFile = "C:\\Users\\CLC USER\\IdeaProjects\\sort\\ulysses_cleaned.txt";

        List<String> words = new ArrayList<>();

        // Read and clean words
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] rawWords = line.split("\\s+");
                for (String word : rawWords) {
                    // Remove everything except letters, make lowercase
                    word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        if (words.isEmpty()) {
            System.out.println("⚠️ No valid words found after cleaning.");
            return;
        }

        System.out.println("Total words after cleaning: " + words.size());

        Collections.sort(words);

        // Optional: print first 50 words
        System.out.println("First 50 cleaned words:");
        for (int i = 0; i < Math.min(50, words.size()); i++) {
            System.out.print(words.get(i) + " ");
        }
        System.out.println();

        // Write sorted words to output
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (String word : words) {
                bw.write(word);
                bw.newLine();
            }
            System.out.println("Done! Check: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}