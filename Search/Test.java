package Search;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;
//This is the main class to perform search operation.
public class Test {
	public static void main(String[] args) throws IOException {
		
		// user enters a word and gets urls[stored in uwindsor.txt] which best matches that word.
		
		// then name of website will pass to search engine 
		 
		System.out.println("Creating Trie From uwindsor.ca........");

		MySearchEngine s_engine = new MySearchEngine("uwindsor.txt");
//now it will ask to enter a word you want to search
		System.out.println("Enter a word you want to search in uwindsor.ca");

		String i = new Scanner(System.in).next();

		try {
			while (!i.equals("esc") && !i.equals(null)) {
				String[] indexArray = i.split("[[,]*|[ ]*]+");
				String[] webpages = s_engine.search(indexArray);
				try {
					if (webpages == null) {
						System.out.println("Please enter a keyword or query!");
					}

					Map<String, Integer> unsortedLinks = null;
					unsortedLinks = new HashMap<>();
//store links in unsortedlink map
					for (String url : webpages) {
						unsortedLinks.put(url, CountTheWords.getWordCount(url, i));
					}
					//to sort the links according to occurence of the word
					LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
			        unsortedLinks.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
			         System.out.println("Priority \t Your Search Result");
			        for (Map.Entry<String, Integer> entry : reverseSortedMap.entrySet()) {
			            System.out.println(entry.getValue()+"\t \t"+entry.getKey());
			        }
				}

				 catch (NullPointerException e) {

					System.out.println("sorry");
				}

				System.out.println("\nyou want to search (separated by comma - \"esc\" to end):");
				i = new Scanner(System.in).next();

			}
		} catch (NullPointerException e) {
			
		}

		

	}

}
