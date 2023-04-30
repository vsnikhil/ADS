// Starter code for SP8

// Change to your netid
/**
 * @author Akshat Maheshwari (AXM210024)
 * @author Sai Nikhil Voruganti (SXV210118)
 */

package sxv210118;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    // add method: resize pq if needed
    public boolean add(T x) {

        if (size == pq.length) {
            resize();
        }

        move(size, x);
        percolateUp(size);
        size++;

        return true;
    }

    public boolean offer(T x) {
        return add(x);
    }

    // throw exception if pq is empty
    public T remove() throws NoSuchElementException {
        T result = poll();
        if (result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    // return null if pq is empty
    public T poll() {
        if (isEmpty()) {
            return null;
        }

        Comparable min = min();
        move(0, pq[size-1]);
        size--;
        percolateDown(0);

        return (T) min;
    }

    public T min() {
        return peek();
    }

    // return null if pq is empty
    public T peek() {
        if (isEmpty()) {
            return null;
        }

        return (T) pq[0];
    }

    int parent(int i) {
        return (i - 1) / 2;
    }

    int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * pq[index] may violate heap order with parent
     */
    void percolateUp(int index) {
        Comparable x = pq[index];
        while (index>0 && compare(x, pq[parent(index)])<0) {
            move(index, pq[parent(index)]);
            index = parent(index);
        }
        move(index, x);
    }

    /**
     * pq[index] may violate heap order with children
     */
    void percolateDown(int index) {
        Comparable x = pq[index];
        int small = leftChild(index);

        while (small <= size-1) {
            if ((small < size-1) && compare(pq[small], pq[small+1])>0) {
                small = small+1;
            }
            if (compare(x, pq[small]) < 0) {
                break;
            }
            move(index, pq[small]);
            index = small;
            small = leftChild(index);
        }
        move(index, x);
    }

    /**
     * use this whenever an element moved/stored in heap. Will be overridden by IndexedHeap
     */
    void move(int dest, Comparable x) {
        pq[dest] = x;
    }

    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /**
     * Create a heap.  Precondition: none.
     */
    void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) {
            percolateDown(i);
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    // Resize array to double the current size
    void resize() {
        int newSize = size * 2;
        Comparable[] newPQ = new Comparable[newSize];
        System.arraycopy(pq, 0, newPQ, 0, pq.length);
        pq = newPQ;
    }

    public interface Index {
        public void putIndex(int index);

        public int getIndex();
    }

    // IndexedHeap is useful to implement algorithms, such as Prim's MST, that requires
    // decreaseKey() operation. You can implement this now or later when you implement MST algorithms
    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {
        /**
         * Build a priority queue with a given array
         */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /**
         * restore heap order property after the priority of x has decreased
         */
        void decreaseKey(T x) {
            int xInd = x.getIndex();
            percolateUp(xInd);
        }

        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
            T val = (T) x;
            val.putIndex(i);
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {0, 9, 7, 5, 3, 1, 8, 6, 4, 2, 1, 12};
        BinaryHeap<Integer> h = new BinaryHeap(arr.length);

        System.out.print("Before:");
        for (Integer x : arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();
//        h.offer(10);
//        System.out.println("size is: "+h.size+" "+h.pq.length);

        for (int i = 0; i < arr.length; i++) {
            arr[i] = h.poll();
        }

        System.out.print("After :");
        for (Integer x : arr) {
            System.out.print(" " + x);
        }
        System.out.println();
    }
}
