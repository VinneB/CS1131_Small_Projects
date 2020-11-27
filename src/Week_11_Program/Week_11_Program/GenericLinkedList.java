package Week_11_Program;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

public class GenericLinkedList <E> implements List<E> {
    private Node head = null;
    private Node tail = null;

    public void addFirst(E element){
        if (size() == 0) {
            add(element);
            return;
        }
        Node node = new Node(element);
        node.next = head;
        head = node;
    }

    public void addLast(E element){
        if (size() == 0) {
            add(element);
            return;
        }
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

    public E poll(){
        return pollFirst();
    }

    public E pollFirst(){
        return removeFirst();
    }

    public E pollLast(){
        return removeLast();
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
        if (index == 0) { addFirst(element); return; }
        if (index == (size())) { addLast(element); return; }
        if (size() == 0) { add(element); return; }
        if (size() < index) { throw new IndexOutOfBoundsException(); }

        Node node = new Node(element);
        Node prevNode = retrieveNodeAtIndex(index - 1);
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
        if (size() < index) { throw new IndexOutOfBoundsException(); }
        if (size() == index) { return addAll(c); }

        Node prevNode = retrieveNodeAtIndex(index - 1);
        Node endNode = prevNode.next;

        for (E element : c){
            prevNode.next = new Node(element);
            prevNode = prevNode.next;
        }

        if (endNode == null){
            tail = prevNode;
        } else {
            prevNode.next = endNode;
        }


        return true;
    }

    private Node retrieveNodeAtIndex(int index){
        if (index == (size() - 1)) { return tail; }
        Node node = head;
        for (int i = 0; i < index; i++){
            node = node.next;
        }
        return node;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;

    }

    @Override
    public boolean contains(Object o) {
        for (E element : this) {
            if (element.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> collectionIterator = c.iterator();
        Iterator<E> thisGenericIterator = iterator();

        for (Object element : c){
            boolean satisfiesCriteria = false;
            for (E criteria : this){
                if (element.equals(criteria)){ satisfiesCriteria = true; break; }
            }
            if (!satisfiesCriteria) { return false; }
        }
        return true;
    }

    @Override
    public E get(int index) {
        if (size() <= index) { throw new IndexOutOfBoundsException(); }
        if (index == 0) { return getFirst(); }
        if (index == (size() - 1)) { return getLast(); }
        
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
    public int lastIndexOf(Object o) {
        Node node = head;
        int index = -1;
        for (int i = 1; i < size(); i++){
            node = node.next;
            if (node.data.equals(o)){
                index = i;
            }
        }
        return index;
    }

    @Override
    public GenericIterator iterator() {
        return new GenericIterator();
    }

    @Override
    public ListIterator<E> listIterator() { return new GenericListIterator(); }

    @Override
    public ListIterator<E> listIterator(int index) { return new GenericListIterator(index); }

    @Override
    public boolean remove(Object o) {
        if (o.equals(head.data)) {
            head = head.next;
            return true;
        }
        else {
            Node node = head;
            for (int i = 0; i < size(); i++) {
                if (node.next.data.equals(o)) {
                    node.next = node.next.next;
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

    @Override
    public E remove(int index) {
        if (index >= size()){ throw new IndexOutOfBoundsException(); }
        E returnValue;
        if (index == 0){
            returnValue = head.data;
            head = head.next;
        } else {
            Node prevNode = retrieveNodeAtIndex(index - 1);
            returnValue = prevNode.next.data;
            prevNode.next = prevNode.next.next;
        }
        return returnValue;
    }

    public E removeFirst(){
        E returnValue = head.data;
        head = head.next;
        return returnValue;
    }

    public E removeLast(){
        Node node = retrieveNodeAtIndex(size() - 2);
        E returnValue = node.next.data;
        tail = node;
        node.next = null;
        return returnValue;
    }

    public boolean removeFirstOccurrence(E o){
        return remove(o);
    }

    public boolean removeLastOccurrence(E o){
        int lastOccurrenceIndex = -1;
        int currentIndex = 0;
        for (Node node = head; node.next != null; node = node.next){
            if (o.equals(node.data)) {
                lastOccurrenceIndex = currentIndex;
            }
            currentIndex++;
        }
        if (lastOccurrenceIndex == -1) { return false; }
        else {
            remove(lastOccurrenceIndex);
            return true;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object element : c){
            while (contains(element)){
                remove(element);
            }
        }
        return true;
    }

    public Node removeNode(Node rmvNode){
        Node node = head;
        for (int i = 0; i < size(); i++){
            if (node == rmvNode){
                remove(i);
                return node;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        for (E element: this){
            boolean satisfiesCriteria = false;
            for (Object criteria : c){
                if (element.equals(criteria)){
                    satisfiesCriteria = true;
                    break;
                }
            }
            if (!satisfiesCriteria){ remove(element); }
        }
        return true;
    }

    @Override
    public E set(int index, E element) {
        Node node = retrieveNodeAtIndex(index);
        E returnData = node.data;
        node.data = element;
        return returnData;
    }

    @Override
    public int size() {
        Node node = head;
        int counter = 0;
        while (node != null){
            node = node.next;
            counter += 1;
        }
        return counter;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (0 > fromIndex || size() <= fromIndex || size() < toIndex || 0 >= toIndex) {
            throw new IndexOutOfBoundsException(); }
        if (fromIndex >= toIndex) { throw new IllegalArgumentException(); }
        GenericLinkedList<E> subList = clone();
        subList.tail = retrieveNodeAtIndex(toIndex - 1);
        subList.head = retrieveNodeAtIndex(fromIndex);
        subList.tail.next = null;
        return subList;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        if (size() >= a.length) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size());
        }
        int index = 0;
        for (E element : this){
            a[index] = (T) element;
            index += 1;
        }
        return a;
    }

    public Object[] toArray() {
        Object[] array = new Object[size()];
        int index = 0;
        for (E element : this){
            array[index] = element;
            index++;
        }
        return array;
    }

    public ArrayList<E> toArrayList() {
        ArrayList<E> returnList = new ArrayList<>(size());
        returnList.addAll(this);
        return returnList;
    }

    @Override
    public GenericLinkedList<E> clone(){
        GenericLinkedList<E> cloneList = new GenericLinkedList<>();
        cloneList.addAll(this);
        return cloneList;
    }

    @Override
    public String toString() {
        if (size() == 0) { return "[]"; }
        StringBuilder string = new StringBuilder();
        string.append("[");
        for (E element : this){
            string.append(element.toString()).append(", ");
        }
        string.replace((string.length() - 2), (string.length()), "]");
        return string.toString();
    }

    private class Node{
        E data;
        Node next;

        Node(E data){
            this.data = data;
        }
    }

    /**
     * Simple One-Way Iterator.
     */
    private class GenericIterator implements java.util.Iterator<E> {
            Node currentNode = head;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            E returnData = currentNode.data;
            currentNode = currentNode.next;
            return returnData;
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {
            removeNode(currentNode);
        }
    }

    /**
     * Proxy Iterator Class for ArrayList.listIterator(). Warning: This class is hyper inefficient since clear() and
     * addAll() are called everytime a value is altered.
     */
    private class GenericListIterator implements ListIterator<E>{
        ArrayList<E> list = toArrayList();
        ListIterator<E> arrayListIterator = list.listIterator();
        int index = 0;

        GenericListIterator(){

        }

        GenericListIterator(int index){
            if (index > size()){ throw new IndexOutOfBoundsException(); }
            for (int i = 0; i < index; i++){ arrayListIterator.next(); }
        }

        /**
         * Returns {@code true} if this list iterator has more elements when
         * traversing the list in the forward direction. (In other words,
         * returns {@code true} if {@link #next} would return an element rather
         * than throwing an exception.)
         *
         * @return {@code true} if the list iterator has more elements when
         * traversing the list in the forward direction
         */
        @Override
        public boolean hasNext() {
            return arrayListIterator.hasNext();
        }

        /**
         * Returns the next element in the list and advances the cursor position.
         * This method may be called repeatedly to iterate through the list,
         * or intermixed with calls to {@link #previous} to go back and forth.
         * (Note that alternating calls to {@code next} and {@code previous}
         * will return the same element repeatedly.)
         *
         * @return the next element in the list
         * @throws NoSuchElementException if the iteration has no next element
         */
        @Override
        public E next() {
            return arrayListIterator.next();
        }

        /**
         * Returns {@code true} if this list iterator has more elements when
         * traversing the list in the reverse direction.  (In other words,
         * returns {@code true} if {@link #previous} would return an element
         * rather than throwing an exception.)
         *
         * @return {@code true} if the list iterator has more elements when
         * traversing the list in the reverse direction
         */
        @Override
        public boolean hasPrevious() {
            return arrayListIterator.hasPrevious();
        }

        /**
         * Returns the previous element in the list and moves the cursor
         * position backwards.  This method may be called repeatedly to
         * iterate through the list backwards, or intermixed with calls to
         * {@link #next} to go back and forth.  (Note that alternating calls
         * to {@code next} and {@code previous} will return the same
         * element repeatedly.)
         *
         * @return the previous element in the list
         * @throws NoSuchElementException if the iteration has no previous
         *                                element
         */
        @Override
        public E previous() {
            return arrayListIterator.previous();
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to {@link #next}. (Returns list size if the list
         * iterator is at the end of the list.)
         *
         * @return the index of the element that would be returned by a
         * subsequent call to {@code next}, or list size if the list
         * iterator is at the end of the list
         */
        @Override
        public int nextIndex() {
            return arrayListIterator.nextIndex();
        }

        /**
         * Returns the index of the element that would be returned by a
         * subsequent call to {@link #previous}. (Returns -1 if the list
         * iterator is at the beginning of the list.)
         *
         * @return the index of the element that would be returned by a
         * subsequent call to {@code previous}, or -1 if the list
         * iterator is at the beginning of the list
         */
        @Override
        public int previousIndex() {
            return arrayListIterator.previousIndex();
        }

        /**
         * Removes from the list the last element that was returned by {@link
         * #next} or {@link #previous} (optional operation).  This call can
         * only be made once per call to {@code next} or {@code previous}.
         * It can be made only if {@link #add} has not been
         * called after the last call to {@code next} or {@code previous}.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this list iterator
         * @throws IllegalStateException         if neither {@code next} nor
         *                                       {@code previous} have been called, or {@code remove} or
         *                                       {@code add} have been called after the last call to
         *                                       {@code next} or {@code previous}
         */
        @Override
        public void remove() {
            arrayListIterator.remove();
            updateMain();
        }

        /**
         * Replaces the last element returned by {@link #next} or
         * {@link #previous} with the specified element (optional operation).
         * This call can be made only if neither {@link #remove} nor {@link
         * #add} have been called after the last call to {@code next} or
         * {@code previous}.
         *
         * @param e the element with which to replace the last element returned by
         *          {@code next} or {@code previous}
         * @throws UnsupportedOperationException if the {@code set} operation
         *                                       is not supported by this list iterator
         * @throws ClassCastException            if the class of the specified element
         *                                       prevents it from being added to this list
         * @throws IllegalArgumentException      if some aspect of the specified
         *                                       element prevents it from being added to this list
         * @throws IllegalStateException         if neither {@code next} nor
         *                                       {@code previous} have been called, or {@code remove} or
         *                                       {@code add} have been called after the last call to
         *                                       {@code next} or {@code previous}
         */
        @Override
        public void set(E e) {
            arrayListIterator.set(e);
            updateMain();
        }

        /**
         * Inserts the specified element into the list (optional operation).
         * The element is inserted immediately before the element that
         * would be returned by {@link #next}, if any, and after the element
         * that would be returned by {@link #previous}, if any.  (If the
         * list contains no elements, the new element becomes the sole element
         * on the list.)  The new element is inserted before the implicit
         * cursor: a subsequent call to {@code next} would be unaffected, and a
         * subsequent call to {@code previous} would return the new element.
         * (This call increases by one the value that would be returned by a
         * call to {@code nextIndex} or {@code previousIndex}.)
         *
         * @param e the element to insert
         * @throws UnsupportedOperationException if the {@code add} method is
         *                                       not supported by this list iterator
         * @throws ClassCastException            if the class of the specified element
         *                                       prevents it from being added to this list
         * @throws IllegalArgumentException      if some aspect of this element
         *                                       prevents it from being added to this list
         */
        @Override
        public void add(E e) {
            arrayListIterator.add(e);
            updateMain();
        }

        /**
         * Simple private function which makes LinkedList equal to ArrayList list.
         */
        private void updateMain(){
            clear();
            addAll(list);
        }

    }

}
