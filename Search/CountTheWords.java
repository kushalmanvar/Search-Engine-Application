package Search;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CountTheWords {

	public static int  getWordCount(String url, String findWord) throws IOException{
	
		Map<String, Word_search> count_map = new HashMap<String, Word_search>();

		// connect to uwindsor and get the HTML
		Document doc = Jsoup.connect(url).get();

		// Get the actual text from the page, excluding the HTML
		String txt = doc.body().text();

		// Create BufferedReader so the words can be counted
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(txt.getBytes(StandardCharsets.UTF_8))));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] words = line.split("[^A-ZÃ…Ã„Ã–a-zÃ¥Ã¤Ã¶]+");
			for (String word : words) {
				if ("".equals(word)) {
					continue;
				}
				Word_search wordObj = count_map.get(word);
				if (word.equalsIgnoreCase(findWord)) {
					if (wordObj == null) {
						wordObj = new Word_search();
						wordObj.word = word;
						wordObj.count = 0;
						count_map.put(word, wordObj);
					}
					wordObj.count++;
				}
			}
		}
		reader.close();

		SortedSet<Word_search> sortedWords = new TreeSet<Word_search>(count_map.values());
		int i = 0;
		int max_words_to_display = 1000;

		String[] wordsToIgnore = {"the", "and", "a" };

		for (Word_search word : sortedWords) {
			if (i >= max_words_to_display) { 
				break;
			}

			if (Arrays.asList(wordsToIgnore).contains(word.word)) {
				i++;
				max_words_to_display++;
			} else {

				i++;
				return word.count;
			}

		}
		return 0;

	}
	//class for searching a word
	public static class Word_search implements Comparable<Word_search> {
		String word;
		int count;

		@Override
		public int hashCode() {
			return word.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return word.equals(((Word_search) obj).word);
		}

		@Override
		public int compareTo(Word_search b) {
			return b.count - count;
		}
	}
}