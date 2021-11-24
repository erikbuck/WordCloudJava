import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// This class counts and records occurences of words from an input stream. The words may
// be filtered to remove common noise words like "the". The getTopWords() function returns a
// list of the most common words that have not been filtered.
public class WordCounter {
    HashMap<String, Integer> wordCounts = new HashMap<String, Integer>();

    // This function records each word and its number of occurrences in the stream.
    void countWordsFromInputStream(InputStream is) {
        Scanner wordScanner = new Scanner(is);
        while(wordScanner.hasNext()) {
            String word = wordScanner.next().toLowerCase();
            if(wordCounts.containsKey(word)) {
                Integer count = wordCounts.get(word);
                count += 1;
                wordCounts.put(word, wordCounts.get(word) + 1);
            } else {
                wordCounts.put(word, 1);
            }
        }
        wordScanner.close();
    }

    // Remove each word in is from the collection of words. Use this to remove noise words
    // like "the".
    void filterWordsFromInputStream(InputStream is) {
        Scanner wordScanner = new Scanner(is);
        while(wordScanner.hasNext()) {
            String word = wordScanner.next().toLowerCase();
            if(wordCounts.containsKey(word)) {
                wordCounts.remove(word);
            }  
        }     
        wordScanner.close();
    }

    // This comparator is used to compare entries based on each entry's value which is the
    // number of occurrences of the entry's key.
    static Comparator<HashMap.Entry<String, Integer>> byValue = 
        new Comparator<HashMap.Entry<String, Integer>>() {
		@Override
		public int compare(HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2) {
			return o2.getValue().compareTo(o1.getValue());
		}
	};

    // Return a List of entries for the words that occur the most. Use numberOfWords to specify 
    // the maximum number of words you want. Each entry has a key that is the word, and a value 
    // that is the number of occurrences.
    List<HashMap.Entry<String, Integer>> getTopWords(int numberOfWords) {
        List<HashMap.Entry<String, Integer>> sorted = 
                wordCounts.entrySet().stream()  // Fill a stream with the unordered entries
                .sorted(byValue)                // Fill a new stream with entries sorted
                .limit(numberOfWords)           // Limit stream to first numberOfWords entries
                .collect(Collectors.toList());  // Collect the entries in the stream into a List

        // The following is for debug only:
        // System.out.println("Sorted my occurrences words:");
        // for (HashMap.Entry<String, Integer> entry : sorted) {
        //     System.out.println(entry.getKey() + ":" + entry.getValue());
        // }

        return sorted;
    }

    // Output information about current word counts to s
    void printWordCountsToOutputStream(OutputStream s) {
        PrintStream ps = new PrintStream(s);
        for (HashMap.Entry<String, Integer> entry : wordCounts.entrySet()) {
            ps.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
