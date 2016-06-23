import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 * TODO: Rename to BPlusTree
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K,T> root;
	public static final int D = 2;
	public Stack<IndexNode<K,T>> parent;
	
	
	/**
	 * TODO Search the value for a specific key
	 * 
	 * @param key
	 * @return value
	 */
	public T search(K key) {
		if (this.root == null) {
			return null;
		}
		LeafNode<K,T> targetNode;

		if (root.isLeafNode) {
			targetNode = (LeafNode<K,T>)this.root;

		} else { 
			// root ! isLeafNode
			this.parent = new Stack<IndexNode<K,T>>();
			this.parent.add((IndexNode<K,T>)this.root);
			targetNode = findNode(this.parent, key);
		}
		for (int i = 0; i < targetNode.keys.size(); i++) {
			if (targetNode.keys.get(i).equals(key)) {					
				return targetNode.values.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Find the LeafNode matching the key: target
	 * parent is global to store the most recent tree traversal path,
	 * excludes leaf
	 * 
	 * @param parent
	 * @param target
	 * @return
	 */
	public LeafNode<K,T> findNode(Stack<IndexNode<K,T>> parent, K target) {
		IndexNode<K,T> working = parent.peek();
		for (int i = 0; i < working.keys.size(); i++ ) {
			if (working.keys.get(i).compareTo(target) > 0) {
				if (working.children.get(i).isLeafNode) {
					this.parent = parent;
					return (LeafNode<K,T>) working.children.get(i);
				}
				parent.add((IndexNode<K, T>) working.children.get(i));
				return findNode(parent, target);
			}
		}
		Node<K,T> last = working.children.get(working.children.size()-1);
		if (last.isLeafNode) {
			return (LeafNode<K, T>) last;
		} else {
			parent.add((IndexNode<K, T>) last);
			return findNode(parent, target);
		}
	}
	
	/**
	 * TODO Insert a key/value pair into the BPlusTree
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(K key, T value) {
		if (this.root == null) {
			this.root = new LeafNode<K,T>(key,value);	
		} else {			
			// find the node to insert
			this.parent = new Stack<IndexNode<K,T>>();
			LeafNode<K,T> targetNode;
			if (root.isLeafNode) {
				targetNode = (LeafNode<K,T>)this.root;

			} else { 
				// root ! isLeafNode
				this.parent.add((IndexNode<K,T>)this.root);
				targetNode = findNode(this.parent, key);
			}
			
			// inserting
			targetNode.insertSorted(key, value);
			
			// adjust b+tree
			Entry<K, Node<K,T>> entry;
			
			Node<K,T> nodeIterator = targetNode;
			// '_' -> not allocated in B+tree
			K key_; 
			Node<K,T> node_;
			
			while (nodeIterator.isOverflowed()) {
				entry = nodeIterator.isLeafNode 
						? this.splitLeafNode((LeafNode<K,T>)nodeIterator) 
						: this.splitIndexNode((IndexNode<K,T>)nodeIterator);
				node_ =  entry.getValue();
				key_ = entry.getKey();
	
				if (this.parent.size() == 0) {	
					// this node was the root
					Node<K,T> previous = this.root;
					Node<K,T> new_ = new IndexNode<K,T>(key_, previous, node_);
					this.root = new_;
					break;
				} else {		
					IndexNode<K,T> workingIndex = this.parent.pop();
					int insertIndex = findIndexInParent(workingIndex, key_);
					workingIndex.insertSorted(entry, insertIndex);
					nodeIterator = workingIndex;				
				}
			}
				
		}
	}
	
	/**
	 * return the index in parent of the node generated by splitting 
	 * 
	 * @param parent
	 * @param key
	 * @return
	 */
	int findIndexInParent(IndexNode<K,T> parent, K key) {
		for (int i = 0; i < parent.keys.size(); i++) {
			if (parent.keys.get(i).compareTo(key) > 0) {
				return i;
			}
		}
		return parent.keys.size();
	}
	
	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<splitingKey, RightNode>
	 * 
	 * @param leaf, any other relevant data
	 * @return the key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf/*, ...*/) {
		ArrayList<K> keys_ = new ArrayList<>();
		ArrayList<T> values_ = new ArrayList<>();
		// build new one
		for (int i = D; i< leaf.keys.size(); i++) {
			keys_.add(leaf.keys.get(i));
			values_.add(leaf.values.get(i));
		}
		LeafNode<K,T> splitted = new LeafNode<K,T>(keys_,values_);
		
		// trim original one
		keys_.clear();
		values_.clear();
		for (int i = 0; i < D; i++) {
			keys_.add(leaf.keys.get(i));
			values_.add(leaf.values.get(i));
		}
		leaf.keys = keys_;
		leaf.values = values_;
		
		// adjust the chaining
		if (leaf.nextLeaf != null) {
			splitted.nextLeaf = leaf.nextLeaf;
			leaf.nextLeaf.previousLeaf = splitted;
		}
		leaf.nextLeaf = splitted;
		splitted.previousLeaf = leaf;
		
		return new AbstractMap.SimpleEntry<K, Node<K,T>>(splitted.keys.get(0), splitted);
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index, any other relevant data
	 * @return new key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index/*, ...*/) {
		ArrayList<K> keys_ = new ArrayList<>();
		ArrayList<Node<K, T>> children_ = new ArrayList<>();
		// build new one
		for (int i = D + 1; i < index.children.size(); i++) {
			if (i < index.keys.size())
				keys_.add(index.keys.get(i));
			children_.add(index.children.get(i));
		}
		Node<K,T> splitted = new IndexNode<K,T>(keys_, children_);
		
		// trim original one
		keys_.clear();
		children_.clear();
		K keyToParent = index.keys.get(D);
		for (int i=0; i < D + 1; i++) {
			if (i < D)
				keys_.add(index.keys.get(i));
			children_.add(index.children.get(i));
		}
		index.keys = keys_;
		index.children = children_;
		
		return new AbstractMap.SimpleEntry<K, Node<K,T>>(keyToParent, splitted);
	
	}

	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {
		// find the node
		if (this.root != null) {

			LeafNode<K,T> targetNode;
			this.parent = new Stack<IndexNode<K,T>>();
			
			if (root.isLeafNode) {
				targetNode = (LeafNode<K,T>)this.root;

			} else {
				// root ! isLeafNode
				this.parent.add((IndexNode<K,T>)this.root);
				targetNode = findNode(this.parent, key);
			}
			

			// if target not exists: done
			if (targetNode.keys.contains(key)) {
				// else delete that one and the value
				System.out.println(targetNode.keys+" "+key);
				
				int toDelete = targetNode.keys.indexOf(key);
				targetNode.keys.remove(toDelete);
				targetNode.values.remove(toDelete);
				
				// nodeIterator -> leaf
				Node<K,T> nodeIterator = targetNode;

				// while(underflow) handle it
				while (nodeIterator.isUnderflowed()) {
					if (parent.isEmpty()) {
						// this underflowed node is root, don't care
						this.root = nodeIterator;
						break;
					}
					
					Node<K,T> left_ = null;
					Node<K,T> right_ = null;
					IndexNode<K,T> parent_ = parent.peek();;
					int toAdjust = -2;
					if (nodeIterator.isLeafNode) {
						// process leaf
						LeafNode<K,T> leaf_ = (LeafNode<K,T>) nodeIterator;
						if (leaf_.previousLeaf != null && parent_.children.contains(leaf_.previousLeaf)) {
							left_ = leaf_.previousLeaf;
							right_ = leaf_;
						} else if (leaf_.nextLeaf != null && parent_.children.contains(leaf_.nextLeaf)) {
							left_ = leaf_;
							right_ = leaf_.nextLeaf;
						}
						if (left_ != null && right_ != null) {
							toAdjust = this.handleLeafNodeUnderflow((LeafNode<K,T>)left_, (LeafNode<K,T>)right_, parent_);

						}
					} else {
						// process index
						IndexNode<K,T> index_ = (IndexNode<K,T>) nodeIterator;

						left_ = findPreviousIndexNode(index_, parent_);

						right_ = left_ == null ? null : index_;

						if (right_ == null) {
							right_ = findNextIndexNode(index_, parent_);
							left_ = right_ == null ? null : index_;
						}
						if (left_ != null && right_ != null) {
							toAdjust = this.handleIndexNodeUnderflow((IndexNode<K,T>)left_, (IndexNode<K,T>)right_, parent.peek());

						}
						
					}
					
					if (toAdjust > -1) {
						// merged
						parent_.keys.remove(toAdjust);
						parent_.children.remove(toAdjust + 1);
						if (parent.peek().keys.size() == 0){
							this.root = left_;
							break;
						}
					} else if (toAdjust == -1) {
						// redistribution
						// nothing to be done here
					}
					nodeIterator = parent.pop();
					
				}
				
			}
		}
	}
	
	/**
	 * return the previous sibling index node of processing node, if not exists return null
	 * 
	 * @param index
	 * @param parent
	 * @return
	 */
	IndexNode<K,T> findPreviousIndexNode(IndexNode<K,T> index, IndexNode<K,T> parent) {
		return this.findSiblingIndexNode(true, index, parent);
	}
	
	/**
	 * return the next sibling index node of processing node, if not exists return null
	 * 
	 * @param index
	 * @param parent
	 * @return
	 */
	IndexNode<K,T> findNextIndexNode(IndexNode<K,T> index, IndexNode<K,T> parent) {
		return this.findSiblingIndexNode(false, index, parent);
	}
	
	/**
	 * return the sibling index node of the processing one, boolean back specifies the direction
	 * 
	 * @param back
	 * @param index
	 * @param parent
	 * @return
	 */
	IndexNode<K,T> findSiblingIndexNode(boolean back, IndexNode<K,T> index, IndexNode<K,T> parent) {
		for (int i = 0; i < parent.children.size(); i++) {
			if (parent.children.get(i).equals(index)) { 
				if (!back) {
					if (i != parent.children.size()-1)
						return (IndexNode<K, T>) parent.children.get(i+1);					
				} else {
					if (i != 0)
						return (IndexNode<K, T>) parent.children.get(i-1);
				}

			}
		}
		return null;
	}
	
	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
			IndexNode<K,T> parent) {		
		// see if redistribution is possible
		int left_size = left.keys.size();
		int right_size =  right.keys.size();
		if (left_size + right_size >= 2*D) {
		// in reality, one is 1, the other is 3 or 4
		// end up with 2,2 or 2,3
			if (left_size > right_size) {
				// right underflow
				for (int i = 0; i < (left_size - right_size) / 2; i++) {
					right.keys.add(0,left.keys.get(left.keys.size()-1));
					right.values.add(0,left.values.get(left.values.size()-1));
					left.keys.remove(left.keys.size()-1);
					left.values.remove(left.values.size()-1);
					
				}
			} else {
				// left underflow
				for (int i = 0; i < (right_size - left_size) / 2; i++) {
					left.keys.add(right.keys.get(0));
					left.values.add(right.values.get(0));
					right.keys.remove(0);
					right.values.remove(0);
				}
			}
			parent.keys.set(parent.children.indexOf(left),right.keys.get(0)); // adjust parent children
			
			assert left.keys.size() - right.keys.size() <= 1;
			assert left.keys.size() == left.values.size();
			assert right.keys.size() == right.values.size();
			// redistribution: no need to adjust chaining	
			
			return -1;
			
		} else {
			// one is 1 and the other is 2 
			// end up with 3
			// merge to left
			for (int i = 0; i < right.keys.size(); i++) {
				left.keys.add(right.keys.get(i));
				left.values.add(right.values.get(i));
			}
			
			// merge: adjust chaining
			if (right.nextLeaf != null) {
				right.nextLeaf.previousLeaf = left;
				left.nextLeaf = right.nextLeaf;
			} else {
				left.nextLeaf = null;
			}
			
			for (int i = 0; i < parent.keys.size(); i++){
				if (parent.keys.get(i).compareTo(left.keys.get(0)) > 0) {
					return i;
				}
			}
			return parent.keys.size();
		}
	}

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
			IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
		// see if redistribution is possible
		int left_size = leftIndex.keys.size();
		int right_size =  rightIndex.keys.size();
		if (left_size + right_size >= 2*D) {
			// one is 1, the other is 3,4
			// end up with 2,2 or 2,3
			// collect all
			Queue<K> key_queue = new LinkedList<K>();
			Queue<Node<K,T>> node_queue = new LinkedList<Node<K,T>>();

			for (int i = 0; i < left_size + 1; i++) {
				if (i < left_size)
					key_queue.add(leftIndex.keys.get(i));

				node_queue.add(leftIndex.children.get(i));
			}
			
			key_queue.add(parent.keys.get(parent.children.indexOf(leftIndex)));
			
			for (int i = 0; i < right_size + 1; i++) {
				if (i < right_size)
					key_queue.add(rightIndex.keys.get(i));

				node_queue.add(rightIndex.children.get(i));
			}

			leftIndex.children.clear();
			leftIndex.keys.clear();
			rightIndex.children.clear();
			rightIndex.keys.clear();
			for (int i = 0; i < (left_size + right_size) / 2 + 1; i++) {
				if (i < (left_size + right_size) / 2){
					leftIndex.keys.add(key_queue.poll());

				}

				leftIndex.children.add(node_queue.poll());
			}

			parent.keys.set(parent.children.indexOf(leftIndex),key_queue.poll());

			while(! key_queue.isEmpty()) {
				rightIndex.keys.add(key_queue.poll());
			}
			while(! node_queue.isEmpty()) {
				rightIndex.children.add(node_queue.poll());
			}

			assert node_queue.size() == 0;
			assert key_queue.size() == 0;

			assert leftIndex.keys.size() - rightIndex.keys.size() <= 1;
			assert leftIndex.keys.size() + 1 == leftIndex.children.size();
			assert rightIndex.keys.size() + 1 == rightIndex.children.size();
			
			// redistribution
			return -1; 

		} else {
			// one is 1, the other is 2
			// end up with 4, pull down one from parent
			// merge to left
			// add pull down node and delete it from parent

			int indexToPullDown; 
			
			indexToPullDown = findIndexInParent(parent, leftIndex.keys.get(0));
			System.out.println(leftIndex.keys.get(0));

			leftIndex.keys.add(parent.keys.get(indexToPullDown));
			for (int i = 0; i < rightIndex.children.size(); i++) {
				if (i < rightIndex.keys.size()){
					leftIndex.keys.add(rightIndex.keys.get(i));
				}
				leftIndex.children.add(rightIndex.children.get(i));
			}
						
			return indexToPullDown;
		}
	}

}