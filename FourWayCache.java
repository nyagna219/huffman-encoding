import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

class FourWayCache {

	private Vector<Node> heap;
	private Map<Integer, String> codetable;

	public FourWayCache(int capacity) {
		heap = new Vector<Node>(capacity + 4);
		heap.add(new Node(Integer.MAX_VALUE,-1));
		heap.add(new Node(Integer.MAX_VALUE,-1));
		heap.add(new Node(Integer.MAX_VALUE,-1));
	}

	public boolean isEmpty() {
		return heap.size()==3;
	}

	public void clear() {
		heap.clear();
	}

	private int parent(int i) {
		return (i - 4) / 4+3;
	}

	private int kthChild(int i, int k) {
		return 4 * (i-3) + k+3;
	}

	public void insert(Node x) {

		heap.add(x);
		heapifyUp(heap.size() - 1);
	}

	public Node delete(int ind) {
		if (isEmpty())
			throw new NoSuchElementException("Underflow Exception");
		Node keyItem = heap.get(ind);
		heap.set(ind, heap.get(heap.size() - 1));
		heap.remove(heap.size() - 1);
		if (heap.size() > 4) {
			heapifyDown(ind);
		}
		return keyItem;
	}

	private void heapifyUp(int childInd) {
		Node tmp = heap.get(childInd);
		while (childInd > 3 && heap.get(parent(childInd)).compareTo(tmp) > 0) {
			heap.set(childInd, heap.get(parent(childInd)));
			childInd = parent(childInd);
		}
		heap.set(childInd, tmp);
	}

	private void heapifyDown(int ind) {
		int child;
		Node tmp = heap.get(ind);
		while (kthChild(ind, 1) < heap.size()) {
			child = minChild(ind);
			if (heap.get(child).compareTo(tmp) < 0)
				heap.set(ind, heap.get(child));
			else
				break;
			ind = child;
		}
		heap.set(ind, tmp);
	}

	private int minChild(int ind) {
		int bestChild = kthChild(ind, 1);
		int k = 2;
		int pos = kthChild(ind, k);
		while ((k <= 4) && (pos < heap.size())) {
			if (heap.get(pos).compareTo(heap.get(bestChild)) < 0)
				bestChild = pos;
			pos = kthChild(ind, ++k);
		}
		return bestChild;
	}

	public int getSize() {
		return heap.size();
	}

	public int getChild(int nodeIndex, int childNum) {
		return nodeIndex * 4 + childNum;
	}
	public Node getStartNode(){
		return heap.get(3);
	}

	public void setCodeTable(Map<Integer, String> codetable) {
		this.codetable=codetable;
	}
	public Map<Integer, String> getCodetable() {
		return codetable;
	}
}
