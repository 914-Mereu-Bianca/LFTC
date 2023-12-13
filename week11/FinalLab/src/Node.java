public class Node {
    private final int index;
    private final String info;
    private final int parent;
    private final int leftSibling;

    public Node(int index, String info, int parent, int leftSibling) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.leftSibling = leftSibling;
    }

    public int getIndex() {
        return index;
    }

    public String getInfo() {
        return info;
    }

    public int getParent() {
        return parent;
    }

    public int getLeftSibling() {
        return leftSibling;
    }
}