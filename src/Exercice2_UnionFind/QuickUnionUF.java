package Exercice2_UnionFind;

public class QuickUnionUF{

    private int[] id;

    public QuickUnionUF(int N){ // constructor (N is the number of points)
        id = new int[N];
        for(int i = 0; i<N; i++){
            id[i]=i;
        }
    }

    public int root(int i){ // root of p
        while (i != id[i])
            i = id[i];
        return i;
    }

    public boolean connected(int p , int q){ // are p and q connected?
        return root(p)==root(q);
    }

    public void union(int p , int q){ // connect p and q
        int rq = root(q);
        int rp = root(p);
        id[rp]=rq;
    }
}