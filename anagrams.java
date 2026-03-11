/*
CSC211 Practical 5
Java translation of anagrams.py

AI Assistance:
ChatGPT used for translating Python logic to Java.

Files required:
joyce1922_ulysses.text
latex/anagrams.tex

Output generated:
latex/theAnagrams.tex
*/

import java.io.*;
import java.util.*;

public class anagrams {

    // Create signature (sorted letters)
    public static String signature(String word) {

        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public static void main(String[] args) {



        String inputfile = "joyce1922_ulysses.text";

        HashMap<String, Integer> D = new HashMap<>();
        HashMap<String, ArrayList<String>> A = new HashMap<>();

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(inputfile), "latin1")
            );

            String line;

            // ----------- STEP 1: READ AND CLEAN WORDS -----------

            while ((line = reader.readLine()) != null) {

                String[] words = line.split("\\s+");

                for (String w : words) {

                    String W = w.replaceAll("[0-9(),.;:_.!?\\-\\[\\]]", "");
                    String clean = W;

                    if (clean.length() == 0)
                        continue;

                    // count words
                    if (D.containsKey(clean))
                        D.put(clean, D.get(clean) + 1);
                    else
                        D.put(clean, 1);
                }
            }

            reader.close();

            // ----------- STEP 2: BUILD ANAGRAM DICTIONARY -----------

            for (String w : D.keySet()) {

                String sig = signature(w);

                if (!A.containsKey(sig)) {

                    ArrayList<String> list = new ArrayList<>();
                    list.add(w);
                    A.put(sig, list);

                } else {

                    A.get(sig).add(w);
                }
            }

            // ----------- STEP 3: WRITE ANAGRAM PERMUTATIONS -----------

            PrintWriter out = new PrintWriter("anagrams");

            for (String key : A.keySet()) {

                ArrayList<String> list = A.get(key);

                if (list.size() > 1) {

                    String anagramList = String.join(" ", list);

                    out.println(anagramList + "\\\\");

                    for (int i = 0; i < list.size() - 1; i++) {

                        int space = anagramList.indexOf(" ");

                        anagramList =
                                anagramList.substring(space + 1)
                                        + " "
                                        + anagramList.substring(0, space);

                        out.println(anagramList + "\\\\");
                    }
                }
            }

            out.close();

            // ----------- STEP 4: SORT OUTPUT -----------

            List<String> sorted = new ArrayList<>();

            BufferedReader r = new BufferedReader(new FileReader("anagrams"));
            String l;

            while ((l = r.readLine()) != null) {
                sorted.add(l);
            }

            r.close();

            Collections.sort(sorted);

            PrintWriter sortedOut = new PrintWriter("anagrams.sorted");

            for (String s : sorted)
                sortedOut.println(s);

            sortedOut.close();

            // ----------- STEP 5: CREATE LATEX OUTPUT -----------

            BufferedReader sr = new BufferedReader(new FileReader("anagrams.sorted"));
            PrintWriter tex = new PrintWriter("anagrams-1.tex");

            char letter = 'X';

            while ((l = sr.readLine()) != null) {

                char initial = l.charAt(0);

                if (Character.toLowerCase(initial) != Character.toLowerCase(letter)) {

                    letter = initial;

                    tex.println("\n\\vspace{14pt}");
                    tex.println("\\noindent\\textbf{\\Large " + Character.toUpperCase(initial) + "}\\\\*[+12pt]");
                }

                tex.println(l);
            }

            sr.close();
            tex.close();

            // cleanup
            new File("anagrams").delete();
            new File("anagrams.sorted").delete();

            System.out.println("Anagram dictionary created successfully.");

        }

        catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}