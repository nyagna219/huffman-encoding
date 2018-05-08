

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class PairHeap  {
	private Pairnode root;
	private long totalTime;
	private Map<Integer, String> ctable = new HashMap<>();
	public PairHeap() {
		root = null;
	}

	
	public Pairnode insert(Node x) {
		Pairnode n = new Pairnode(x);
		if (root == null)
			root =  n;
		else
			root = companlin(root,  n);
		return  n;
	}

	public boolean isempty() {
		return root == null;
	}
	private Pairnode companlin(Pairnode p1, Pairnode p2) {
		if (p2 == null)
			return p1;

		if (p2.elem.compareTo(p1.elem) < 0) {
			p2.previous = p1.previous;
			p1.previous = p2;
			p1.nextSibling = p2.leftChild;
			if (p1.nextSibling != null)
				p1.nextSibling.previous = p1;
			p2.leftChild = p1;
			return p2;
		} else {
			p2.previous = p1;
			p1.nextSibling = p2.nextSibling;
			if (p1.nextSibling != null)
				p1.nextSibling.previous = p1;
			p2.nextSibling = p1.leftChild;
			if (p2.nextSibling != null)
				p2.nextSibling.previous = p2;
			p1.leftChild = p2;
			return p1;
		}
	}

	private Pairnode combinesib(Pairnode p1) {
		if (p1.nextSibling == null)
			return p1;
		int n = 0;
		Vector<Pairnode> tarray = new Vector<Pairnode>(5);
		for (; p1 != null; n++) {
			tarray.add(n, p1);
			p1.previous.nextSibling = null;
			p1 = p1.nextSibling;
		}
		tarray.add(n, null);
		int i = 0;
		for (; i + 1 < n; i += 2)
			tarray.set(i, companlin(tarray.get(i), tarray.get(i + 1)));
		int j = i - 2;
		if (j == n - 3)
			tarray.set(j, companlin(tarray.get(j), tarray.get(j + 2)));
		for (; j >= 2; j -= 2)
			tarray.set(j - 2, companlin(tarray.get(j - 2), tarray.get(j)));
		return tarray.get(0);
	}

	public Node delmin() {
		if (isempty())
			return null;
		Node x = root.elem;
		if (root.leftChild == null)
			root = null;
		else
			root = combinesib(root.leftChild);
		return x;
	}

	public void encode() {
		while (root.getleft()!=null) {
			Node n1 = delmin();
			Node n2 = delmin();
			Node n3 = new Node(n1.getFreq() + n2.getFreq(), -1);
			n3.addChild(n1);
			n3.addChild(n2);
			insert(n3);
		}

	}

	public void encode(Node n, String s, final Vector<String> pre) {
		if (n.isChildEmpty()) {
			ctable.put(n.getKey(), s);
		} else {
			int count = 0;
			for (String i : pre) {
				encode(n.getChild(count++), s + i, pre);
			}
		}
	}

	public Map<Integer, String> getCodeTable() {
		return ctable;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public Node getStartNode() {
		return root.elem;
	}

	public void setCodeTable(Map<Integer, String> codetable) {
		this.ctable=codetable;
	}

}
