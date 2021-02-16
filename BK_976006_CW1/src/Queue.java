
/**
 *
 * A class that implements a queue.  It is your job to complete this class.  Your queue
 * will use a linked list constructed by QueueElements.  However, your queue must be general and allow
 * setting of any type of Object.  Also you cannot use ArrayLists or arrays (you will get zero).
 *
 * @author Bartosz Kubica
 *
 */
import java.util.NoSuchElementException;

public class Queue<T> {

    private QueueElement head;
    private QueueElement tail;
    private int n;

    /**
     * Constructs an empty Queue.
     */
    public Queue() {
        head = null;
        tail = null;
        n = 0;
    }

    /**
     * Returns true if the queue is empty
     *
     * @return
     */
    public boolean isEmpty() {
        return ((head == null) && (tail == null));
    }

    public int size() {
        return n;
    }

    /**
     * Returns the element at the head of the queue
     *
     * @return
     */
    public T peek() {
        if (isEmpty() || head == null) {
            throw new NoSuchElementException();
        } else {
            return (T) head.getElement();
        }
    }

    /**
     * Removes the front element of the queue
     */
    public void dequeue() throws NoSuchElementException {
        //Dequeue code is neede here
        if (head == null) {
            throw new NoSuchElementException();
        } else {
            QueueElement prev = head;
            if (prev.getNext() == null) {
                head = null;
                tail = null;
            } else {
                QueueElement temp = prev.getNext();
                head = temp;
                prev.setNext(null);
            }
            
        }
        n--;
    }

    /**
     * Puts an element on the back of the queue.
     *
     * @param element
     */
    public void enqueue(T element) {
        //Enqueue code is needed here
        QueueElement oldTail = tail;
        QueueElement e = new QueueElement(element, null);

        if (isEmpty()) {
            head = e;
            tail = e;
        } else {
            oldTail.setNext(e);
            tail = e;
        }
        n++;
    }

    /**
     * Method to print the full contents of the queue in order from head to tail.
     */
    public void print() {
        //Code to print the code is needed here
        QueueElement temp = head;

        if (temp == null) {
            System.out.println("The queue is empty.");
        } else {
            for (int i = 0; i < n; i++) {
                System.out.println(temp.getElement());
                temp = temp.getNext();
            }
            

        }

    }
}
