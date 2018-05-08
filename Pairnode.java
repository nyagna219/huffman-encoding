

public class Pairnode {
	Node elem;
	Pairnode leftChild;
	Pairnode nextSibling;
	Pairnode previous;

	public Pairnode(Node x) {
		elem = x;
		leftChild = null;
		nextSibling = null;
		previous = null;
	}

	public Pairnode getleft() {
		return leftChild;
	}

	public Pairnode getnext() {
		return nextSibling;
	}

	public Pairnode getprevious() {
		return previous;
	}
	
}
