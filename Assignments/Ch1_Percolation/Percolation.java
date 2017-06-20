import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private int openCount;
    
    //0 as not open, 1 as open
    private int[][] ids;
    private WeightedQuickUnionUF uf;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;

        //last 2 as virtual nodes
        uf = new WeightedQuickUnionUF(n * n + 2); 
        ids = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ids[i][j] = 0;
            }
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (ids[row - 1][col - 1] == 0) {
            ids[row - 1][col - 1] = 1;
            openCount++;
        } else {
            return;
        }
        if (row - 1 == 0) {
            uf.union(n * n, posInUF(row, col));
        }
        if (row - 1 == n - 1) {
            uf.union(n * n + 1, posInUF(row, col));
        }
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                uf.union(posInUF(row - 1, col), posInUF(row, col));
            }
        }
        if (row < n) {
            if (isOpen(row + 1, col)) {
                uf.union(posInUF(row + 1, col), posInUF(row, col));
            }
        }
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                uf.union(posInUF(row, col - 1), posInUF(row, col));
            }
        }
        if (col < n) {
            if (isOpen(row, col + 1)) {
                uf.union(posInUF(row, col + 1), posInUF(row, col));
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return ids[row - 1][col - 1] == 1;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return isOpen(row, col) && uf.connected(n * n, posInUF(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(n * n, n * n + 1);
    }

    // calculate the position in uf
    private int posInUF(int row, int col) {
        return (row - 1) * this.n + col - 1;
    }
    
    // whether row, col is out of bounds
    private boolean isOutOfBounds(int row, int col) {
        return row < 1 || row > n || col < 1 || col > n;
    }
}