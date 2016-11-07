

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of queue for easy cpp translation
 * 
 * @author Maciej Gladki (maciej.szymon.gladki@cern.ch)
 *
 */
public class SimpleFifoQueue {

	public List<Data> queue;
	private int tailIndex;

	private int capacity;

	public SimpleFifoQueue(int capacity) {
		this.capacity = capacity;
		this.queue = new ArrayList<Data>(capacity);
		this.tailIndex = 0;
	}

	/**
	 * Take head but not remove
	 * 
	 * @return
	 */
	public Data peek() {
		return queue.get(0);
	}

	/**
	 * Take head and remove
	 * 
	 * @return
	 */
	public Data poll() {
		Data result = queue.remove(0);
		// queue.remove(tailIndex);
		tailIndex--;
		return result;
	}

	public boolean add(Data data) {
		if (tailIndex >= capacity)
			return false;

		queue.add(tailIndex, data);
		tailIndex++;
		return true;
	}

	public int size() {
		return tailIndex;
	}

	public String toString() {
		return queue.toString();
	}

	public boolean isEmpty() {
		if (size() == 0)
			return true;
		else
			return false;
	}

	public Data get(int index) {
		return queue.get(index);
	}

	public void clear() {
		tailIndex = 0;
		queue.clear();
	}

	public int[] getProgress() {
		
		if(queue.size() == 0){
			return new int[]{0};
		}

		int[] progressArray = new int[queue.size()];
		for(int i=0; i<queue.size(); i++){
			progressArray[i] = queue.get(i).getProgress();
		}
		return progressArray;
	}

}
