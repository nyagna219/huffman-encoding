import java.util.Vector;

public class Node implements Comparable<Node> {
	private Vector<Node> children=new Vector<Node>();
	private int key;
	private int freq;
	private boolean isLeaf = false;

	public Node() {
	}

	public Node(int freq, int i) {
		this.key = i;
		this.freq = freq;
	}

	public Node getChild(int i) {
		return children.get(i);
	}

	public void addChild(Node node) {
		children.add(node);
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int getFreq() {
		return freq;
	}

	public void setToLeaf() {
		isLeaf = true;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public int compareTo(Node tmp) {
		// TODO Auto-generated method stub
		if (this.freq < tmp.freq)
			return -1;
		else if (this.freq > tmp.freq)
			return 1;
		else if(this.freq==tmp.freq){
			if(this.key==-1) return 1;
			if(tmp.key==-1) return -1;
			return this.key-tmp.key;
		}
		return 0;
	}
	public boolean isChildEmpty(){
		return children.isEmpty();
	}

	@Override
	public String toString() {
		return "Node [key=" + key + ", freq=" + freq + "]";
	}
	
}
