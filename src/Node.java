/**
 * Node Class provided by instructor. Small changes included such as implementing comparable.
 *
 */
public class Node implements Comparable < Node > {

    private int row,
    col,
    f,
    g,
    h,
    type;
    private Node parent;

    public Node(int r, int c, int t) {
        row = r;
        col = c;
        this.setType(t);
        parent = null;
        //type 0 is traverseable, 1 is not
    }

    //mutator methods to set values
    public void setF() {
        f = g + h;
    }
    public void setG(int value) {
        g = value;
    }
    public void setH(int value) {
        h = value;
    }
    public void setParent(Node n) {
        parent = n;
    }

    public void setType(int type) {
        this.type = type;
    }

    //accessor methods to get values
    public int getF() {
        return f;
    }
    public int getG() {
        return g;
    }
    public int getH() {
        return h;
    }
    public Node getParent() {
        return parent;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public int getType() {
        return type;
    }

    public boolean equals(Object in ) {
        //typecast to Node
        Node n = (Node) in ;

        return row == n.getRow() && col == n.getCol();
    }
    public String toString() {
        return "Node: " + row + "_" + col;
    }

    @Override
    public int compareTo(Node n) {
        // TODO Auto-generated method stub
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this.getF() == n.getF()) return EQUAL;
        else if (this.getF() < n.getF()) return BEFORE;
        else return AFTER;
    }

}