package Search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
public class MySearchEngine {
	/* 
	 * The splitting is used to split the words excluding regular expressions or any other symbols in it
	 */
	private final String splitstrings = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[’]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
	/*
	 * I created a file consisting of all the stop words
	 */
	private final String stopwordsFile = "stopwords.txt";
	/*
	 * A Trie is initialized using the ArrayList collection 
	 */
	private MyTrie<ArrayList<Integer>> trie;
	/*
	 * A web pages array is used to store all the Urls of the web pages in a web site 
	 */
	private String [] webPagesArray;
	
	public MySearchEngine(String websiteName) {
		// The Trie Data Structure is used for mapping words to references (i.e.) mapping words to the urls (w,L) format
		this.trie = new MyTrie<ArrayList<Integer>>();
		/*
		 *A Hash set is used to store the text information in  file and it does not contain any duplicates    
		 */
		HashSet<String> stop_words = savepages(stopwordsFile);
		
		HashSet<String> temp = savepages(websiteName);
		// Convert the HashSet to String array and assign them to the web pages array
		 this.webPagesArray = temp.toArray(new String[0]);
		
		temp = null;
		String txt;
		String word;
		String[] words;
		/*
		 *Now I have created Iterator in java which is used to get the string one by one  from the pages array that is to get each url one by one
		 */
		Iterator<String> iterator = null;
		 // The Page Index is used to keep track of the index of the urls in a webpages array
		for (int index = 0; index < this.webPagesArray.length; ++index) {
			try {
				txt = webCrawl(this.webPagesArray[index]);
				txt = txt.toLowerCase();
				words = txt.split(splitstrings);
				/*
				 * the words are split.
				 * the words array is then converted to the list and stored as hashset and assigned to temp 
				 */
				temp = new HashSet<String>(Arrays.asList(words));
				 // removeall function will remove words from the array.
				temp.removeAll(stop_words); // remove all stop words from the page
				
				iterator = temp.iterator();
				while(iterator.hasNext()) {
					word = (String) iterator.next();
					/*
					 *When we get an index call the search function in the trie if the trie is empty insert into the trie
					 *  else search
					 */
					ArrayList<Integer> ar = this.trie.search_word(word);
					if (ar == null) {
						// insert a new word referencing the current page
						this.trie.insert(word, new ArrayList<Integer>(Arrays.asList(index)));
					} else {
						ar.add(index);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		System.out.println("The trie has " + this.trie.size + " entries.");
	}
	
	/*
	 * savepages is used to parse and read through a file and put the lines in a hashset so that there will not be any duplicate entries
	 */
	
	/*
	 *The process here is to read the web site file and store all the string or text in the hash and 
	 *if no such file exists it displays a message to the user
	 */
	private HashSet<String> savepages(String filename) {
		HashSet<String> hash = new HashSet<String>();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				hash.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("no such file");
			
			
		}
		catch (IOException e){
			System.out.println("sorrry");
		}
		return hash;
	}
	
	private String webCrawl(String url) throws Exception {	
		Document doc = Jsoup.connect(url).get();
		String text = doc.body().text();
		return text;
	}
	//the index is passed and is searched and the ranking is done here that is the first occurrence in all the urls 
	public String[] search (String[] indexTerm) {
	
		int[] votes = new int[this.webPagesArray.length];
		ArrayList<Integer> tmp = null;
		for (int i = 0; i < indexTerm.length; ++i) {
			tmp = this.trie.search_word(indexTerm[i].toLowerCase());
			if (tmp != null) {
				for (int k = 0; k < tmp.size(); k++) {
					votes[tmp.get(k)]++;
				}
			} else {
				System.out.println("The word <" + indexTerm[i] + "> is not in any file!" );
				
				return null;
			}
		}
		/*answers stores the indexes of the webPages*/ 
		ArrayList<String> webPages = new ArrayList<String>();
		for (int p = 0; p < votes.length; ++p) {
			if (votes[p] == indexTerm.length) {
				webPages.add(this.webPagesArray[p]);
			}
		}
		return webPages.toArray(new String[0]);
	}

}
