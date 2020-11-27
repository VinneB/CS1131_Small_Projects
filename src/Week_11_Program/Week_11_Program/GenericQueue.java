package Week_11_Program;

public class GenericQueue <T> implements QueueInterface<T>{
    GenericLinkedList<T> queue = new GenericLinkedList<>();
    int maxNumOfElements;

    public GenericQueue(int maxNumOfElements){
        this.maxNumOfElements = maxNumOfElements;
    }

    public GenericQueue(){

    }

    /**
     * Adds one element to the rear of this queue.
     *
     * @param element the element to be added to the rear of the queue
     */
    @Override
    public void enqueue(T element) throws QueueFullException {
        if (queue.size() >= maxNumOfElements) { throw new QueueFullException(); }
        queue.addLast(element);
    }

    /**
     * Removes and returns the element at the front of this queue.
     *
     * @return the element at the front of the queue
     */
    @Override
    public T dequeue() throws QueueEmptyException {
        if (queue.isEmpty()) { throw new QueueEmptyException(); }
        return queue.pollFirst();
    }

    /**
     * Returns without removing the element at the front of this queue.
     *
     * @return the first element in the queue
     */
    @Override
    public T peek() throws QueueEmptyException {
        if (queue.isEmpty()) { throw new QueueEmptyException(); }
        return queue.getFirst();
    }

    /**
     * Returns true if this queue contains no elements.
     *
     * @return true if this queue is empty
     */
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns true if this queue contains the maximum number of elements.
     *
     * @return true if this queue is full
     */
    @Override
    public boolean isFull() {
        return (queue.size() >= maxNumOfElements);
    }

    /**
     * Returns the number of elements in this queue.
     *
     * @return the integer representation of the size of the queue
     */
    @Override
    public int size() {
        return queue.size();
    }
}
