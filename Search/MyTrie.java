package Search;

import java.util.HashMap;

public class MyTrie<t> {
	private MyNode<t> root;
	public int size;
	
	public MyTrie() {
		this.root = new MyNode<t>();
		this.size = 0;
	}
	//insert key and values in trie
	public void insert(String s, t value) {
		HashMap<Character, MyNode<t>> children = this.root.children;
		MyNode<t> node = null;
		
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			
			if (children.containsKey(c)) {
				node = children.get(c);
			} else {
				node = new MyNode<t>(c);
				children.put(c,node);
			}
			
			if (i==s.length()-1) // is the end of the word
				node.value = value;
			
			children = node.children;
		}
		this.size += 1;
	}
	
	// when a word is searched and if the characters of the word are present it returns the url which has the word else returns null 
	 
	public t search_word(String s) {
		HashMap<Character, MyNode<t>> children = this.root.children;
		MyNode<t> node = null;
		t ans = null;
		
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			
			if (children.containsKey(c)) {
				node = children.get(c);
			} else {
				return null;
			}
			if (i == s.length()-1) {
				ans = node.value;
			}
			children = node.children;
		}
		return ans; // page index gets returned if exists else null
	}

}
