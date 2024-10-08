package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T> {
    HANLinkedList<T> list;

    @Override
    public void push(T value) {
        list.addFirst(value);
    }

    @Override
    public T pop() {
        T tos = list.getFirst();
        list.removeFirst();
        return tos;
    }

    @Override
    public T peek() {
        return list.getFirst();
    }
}
