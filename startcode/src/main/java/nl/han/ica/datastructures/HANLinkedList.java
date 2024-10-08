package nl.han.ica.datastructures;

public class HANLinkedList<T> implements IHANLinkedList<T> {

    ListNode<T> head;
    int size = 0;

    @Override
    public void addFirst(T value) {
        ListNode<T> listNode;
        if (size > 0) {
            listNode = new ListNode<>(value, head.next);
        } else {
            listNode = new ListNode<>(value, null);
        }
        head.next = listNode;
        size++;
    }

    @Override
    public void clear() {
        head.next = null;
        size = 0;
    }

    @Override
    public void insert(int index, T value) {
        if (index > size+1) {
            throw new IndexOutOfBoundsException();
        }
        ListNode<T> listNode = head;
        for (int i = 0; i < index-1; i++) {
            listNode = listNode.next;
        }
        listNode.next = new ListNode<>(value, listNode.next);
    }

    @Override
    public void delete(int pos) {
        if (pos > size) {
            throw new IndexOutOfBoundsException();
        }
        ListNode<T> listNode = head;
        for (int i = 0; i < pos-1; i++) {
            listNode = listNode.next;
        }
        listNode.next = listNode.next.next;
    }

    @Override
    public T get(int pos) {
        if (pos > size) {
            throw new IndexOutOfBoundsException();
        }
        ListNode<T> listNode = head;
        for (int i = 0; i < pos; i++) {
            listNode = listNode.next;
        }
        return listNode.element;
    }

    @Override
    public void removeFirst() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        } else {
            head.next = head.next.next;
        }
        size--;
    }

    @Override
    public T getFirst() {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return head.next.element;
    }

    @Override
    public int getSize() {
        return size;
    }
}
