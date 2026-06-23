package Exercice4_FGLL;
import java.util.Iterator;
import java.util.NoSuchElementException;

class Queue<T> implements Iterable<T> {

    private Node first;
    private Node last;
    private int size;

    private class Node { // Class privada Nó
        public T item;
        public Node next;
    }

    public Queue() { // constructor
        first = null;
        last = null;
        size = 0;
    }

    public void enqueue(T item) { // add item to the queue
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;

        if ( isEmpty() ){
            first = last;
        }else{
            oldLast.next = last;
        }
        size++;
    }

    public T dequeue() { // remove and return the least recently added item
        T item = first.item;
        first = first.next;
        if ( isEmpty() ){
            last = null;
        }
        size--;
        return item;
    }

    public boolean isEmpty() { // is the queue empty?
        return first == null;
    }

    public int size() { // number of items in the queue
        return size;
    }

    public void shift() { // move the last element to the start of the queue
        if ( size <= 1 ){
            return;
        }

        Node penultimo = first;
        while ( penultimo.next != last){
            penultimo = penultimo.next;
        }

        Node oldLast = last;
        penultimo.next = null;
        last = penultimo;
        oldLast.next = first;
        first = oldLast;
    }

    public Iterator<T> iterator() { // return the iterator for this queue
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<T> { // Iterator

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public T next() {
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            T item = current.item;
            current = current.next;
            return item;
        }
    }
}
