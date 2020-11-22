package Week_11_Program;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GenericLinkedList <E> implements List<E> {
    Node head = null;
    Node tail = null;
    
    public void addFirst(E element){
        if (size() == 0) { add(element); }
        Node node = new Node(element);
        node.next = head;
        head = node;
    }

    public void addLast(E element){
        if (size() == 0) { add(element); }
        Node node = new Node(element);
        tail.next = node;
        tail = node;
    }

    public E getFirst(){
        if (isEmpty()) { return null; }
        return head.data;
    }

    public E getLast(){
        if (isEmpty()) { return null; }
        return tail.data;
    }

    @Override
    public boolean add(E element) {
        Node node = new Node(element);
        if (size() == 0){
            head = node;
            tail = node;
        } else{
            addLast(element);
        }
        return true;

    }

    @Override
    public void add(int index, E element) {
        if (index == 0) { addFirst(element); }
        if (index == (size() - 1)) { addLast(element); }
        if (size() == 0) { add(element); }
        if (size() <= index) { throw new IndexOutOfBoundsException(); }

        Node node = new Node(element);
        Node prevNode = head;
        for (int i = 0; i < (index - 1); i++){
            prevNode = node.next;
        }
        node.next = prevNode.next;
        prevNode.next = node;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c){
            addLast(element);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Iterator<E> iterator = (Iterator<E>) c.iterator();
        Node node = new Node(iterator.next());
        Node nextNode = null;
        boolean startedEmpty = isEmpty();

        if (startedEmpty) { head = node; }
        else {
            Node prevNode = head;
            for (int i = 0; i < (index - 1); i++){
                prevNode = prevNode.next;
            }
            nextNode = prevNode.next;
            prevNode.next = node;
        }
        while (iterator.hasNext()){
            node.next = new Node(iterator.next());
            node = node.next;
        }

        node.next = nextNode;
        if (startedEmpty) { tail = node; }
        return true;

    }

    @Override
    public void clear() {
        head = null;
        tail = null;

    }

    @Override
    public boolean contains(Object o) {
        Iterator<E> iterator = iterator();
        while (iterator.hasNext()){
            E element = iterator.next();
            if (element.equals(o)) { return true; }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<E> collectionIterator = (Iterator<E>) c.iterator();
        Iterator<E> thisIterator = iterator();

        while (collectionIterator.hasNext()){
            E collectionElement = collectionIterator.next();
            boolean elementFound = false;
            while (thisIterator.hasNext()){
                if (collectionElement.equals(thisIterator.next())){
                    elementFound = true;
                    break;
                }
            }
            if (!elementFound) { return false; }
        }
        return true;
    }

    @Override
    public E get(int index) {
        if (size() <= index) { throw new IndexOutOfBoundsException(); }
        if (index == 0) { getFirst(); }
        if (index == (size() - 1)) { getLast(); }
        
        Node node = head;
        for (int i = 0; i < index; i++){
            node = node.next;
        }
        return node.data;
    }

    @Override
    public int indexOf(Object o) {
        int counter = 0;
        for (E element: this){
            if (element.equals(o)) { return counter; }
            counter += 1;
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return (head == null);
    }

    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public E remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public E set(int index, E element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    private class Node{
        E data;
        Node next;

        Node(E data){
            this.data = data;
        }
    }

}
