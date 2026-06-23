package Exercice6_OrderedList;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class OrderedList<T extends Comparable<T>> implements Iterable<T> {

    private class Node {
        public T item;
        public Node next;
    }

    private Node first;
    private Node last;
    private int size;

    public OrderedList() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    public void add(T item) {
        Node newNode = new Node();
        newNode.item = item;
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
        size++;
    }

    public boolean contains(T item) {
        Node current = first;
        while (current != null) {
            if (current.item.compareTo(item) == 0) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void sort() {
        if (first == null || first.next == null) {
            return; // Lista vazia ou com 1 elemento já está ordenada
        }

        Node sortedHead = first;
        Node sortedTail = first;
        Node current = first.next;
        sortedHead.next = null;

        while (current != null) {
            Node nextNode = current.next;
            if (current.item.compareTo(sortedTail.item) >= 0) {
                sortedTail.next = current;
                sortedTail = current;
                sortedTail.next = null;
            }
            else if (current.item.compareTo(sortedHead.item) < 0) {
                current.next = sortedHead;
                sortedHead = current;
            }
            else {
                Node search = sortedHead;
                while (search.next != null && search.next.item.compareTo(current.item) <= 0) {
                    search = search.next;
                }
                current.next = search.next;
                search.next = current;
            }
            current = nextNode;
        }
        first = sortedHead;
        last = sortedTail;
    }

    @SuppressWarnings("unchecked")
    public void shuffle() {
        if (size <= 1) return;
        Object[] items = new Object[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            items[i++] = current.item;
            current = current.next;
        }
        Random rand = new Random();
        for (i = 0; i < size; i++) {
            int r = i + rand.nextInt(size - i); // Índice aleatório entre i e size-1
            Object temp = items[i];
            items[i] = items[r];
            items[r] = temp;
        }
        current = first;
        i = 0;
        while (current != null) {
            current.item = (T) items[i++];
            current = current.next;
        }
    }
    public boolean isSorted() {
        if (first == null || first.next == null) return true;

        Node current = first;
        while (current.next != null) {
            if (current.item.compareTo(current.next.item) > 0) {
                return false;
            }
            current = current.next;
        }
        return true;
    }
    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<T> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T item = current.item;
            current = current.next;
            return item;
        }
    }
}