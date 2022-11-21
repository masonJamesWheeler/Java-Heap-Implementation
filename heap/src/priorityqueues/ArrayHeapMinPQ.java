package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Collections;


/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;

    private int size;
    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        PriorityNode<T> beginOfList = new PriorityNode<>(null, 0);
        items.add(0, null);
    }
    private void swap(int a, int b) {
        Collections.swap(items, a, b);
    }
    @Override
    public void add(T item, double priority) {
        if (this.contains(item)) {
            throw new IllegalArgumentException();
        } else {
            PriorityNode<T> toAdd = new PriorityNode<>(item, priority);
            items.add(toAdd);
            if (items.size() > 1) {
                organizeDown(items.size(), priority);
            }
        }
    }
    @Override
    public boolean contains(T item) {
        for (int i = 1; i < items.size(); i++) {
            PriorityNode curr = items.get(i);
            if (curr.getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T peekMin() {
        if (items.size() == 1) {
            throw new NoSuchElementException();
        } else {
            return items.get(1).getItem();
        }
    }

    @Override
    public T removeMin() {
        if (items.size() == 1) {
            throw new NoSuchElementException();
        } else {
            PriorityNode<T> toReturn = new PriorityNode<>(items.get(1).getItem(), 69);
            items.remove(1);
            return toReturn.getItem();
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        int changed = 0;
        for (int i = 1; i < items.size(); i++) {
            PriorityNode<T> curr = items.get(i);
            if (java.util.Objects.equals(item, curr.getItem())) {
                PriorityNode<T> toInsert = new PriorityNode<>(item, priority);
                items.remove(i);
                this.add(toInsert.getItem(), priority);
                changed = 1;
            }
        }
        if (changed == 0) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int size() {
        return items.size()-1;
    }
    private void organizeDown(int i, double priority) {
        while (i > 1) {
            double priorityIterator = items.get(i-1).getPriority();
            if (priority < priorityIterator) {
                swap(i, i - 1);
            }
            i--;
        }
    }
    private void organizeUp(int i, double priority) {
        if (i > 1 && i < items.size()-1) {
            double priorityIterator = items.get(i+1).getPriority();
            if (priority > priorityIterator) {
                swap(i, i +1);
            }
            organizeUp(i+1, priority);
        }
    }
}
