package Week_11_Program;

import java.util.LinkedList;

public class GenericQueue <E> implements QueueInterface<E>{
    GenericLinkedList<E> queue;
    int maxNumElements = -1;

    public GenericQueue(int maxNumElements){
        queue = new LinkedList<>();
        this.maxNumElements = maxNumElements;
    }

    public GenericQueue(){
        queue = new LinkedList<>();
    }

    @Override
    public void enqueue(E element) throws QueueFullException {
        if (queue.size() > maxNumElements) throw new QueueFullException();
        queue.add(element);
    }

    @Override
    public E dequeue() throws QueueEmptyException {
        if (queue.isEmpty()) throw new QueueEmptyException();
        return queue.getFirst();
    }

    @Override
    public E peek() throws QueueEmptyException {
        if (queue.isEmpty()) throw new QueueEmptyException();
        return queue.peekFirst();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean isFull() {
        return (queue.size() == maxNumElements);
    }

    @Override
    public int size() {
        return queue.size();
    }
}
