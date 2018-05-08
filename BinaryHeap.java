import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

class BinaryHeap {

	private int d;
	private Vector<Node> heap;
	private Map<Integer, String> codetable;

	public BinaryHeap(int capacity) {
		d = 2;
		heap = new Vector<Node>(capacity + 1);
	}

	public boolean isEmpty() {
		return heap.isEmpty();
	}

	public void clear() {
		heap.clear();
	}

	private int parent(int i) {
		return (i - 1) / d;
	}

	private int kthChild(int i, int k) {
		return d * i + k;
	}

	public void insert(Node x) {

		heap.add(x);
		heapifyUp(heap.size() - 1);
	}

	public Node delete(int ind) {
		Node keyItem = heap.get(ind);
		heap.set(ind, heap.get(heap.size() - 1));
		heap.remove(heap.size() - 1);
		if (heap.size() > 1) {
			heapifyDown(ind);
		}
		return keyItem;
	}

	private void heapifyUp(int childInd) {
		Node tmp = heap.get(childInd);
		while (childInd > 0 && heap.get(parent(childInd)).compareTo(tmp) > 0) {
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
		while ((k <= d) && (pos < heap.size())) {
			if (heap.get(pos).compareTo(heap.get(bestChild)) < 0)
				bestChild = pos;
			pos = kthChild(ind, k++);
		}
		return bestChild;
	}

	public int getSize() {
		return heap.size();
	}

	public int getChild(int nodeIndex, int childNum) {
		return nodeIndex * 2 + childNum;
	}
	public Node getStartNode(){
		return heap.get(0);
	}
	
	

	public int getfrequency(int i) {
	
			return heap.get(i).getFreq();
	}

	public Node getNode(int i) {
		// TODO Auto-generated method stub
		
		return heap.get(i);
	}

	public void setNode(int i, int freq) {
		// TODO Auto-generated method stub
		heap.get(i).setFreq(freq);
		
	}

	public void setCodeTable(Map<Integer, String> codetable) {
		this.codetable=codetable;
	}

	public Map<Integer, String> getCodetable() {
		return codetable;
	}
	
}
