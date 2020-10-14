package Search;

import java.util.HashMap;

public class MyNode<t> {
	char k;
	t value;
	HashMap<Character, MyNode<t>> children;
	
	public MyNode() {
		this.children = new HashMap<Character, MyNode<t>>();
	}
	
	public MyNode(char key) {
		this.k = key;
		this.children = new HashMap<Character, MyNode<t>>();
	}
}
