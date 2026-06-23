package Exercice2_UnionFind;

public class WeightedQUPPathCompressionUF{

    private int[] id;
    private int[] sz;

    public WeightedQUPPathCompressionUF(int N){ // constructor (N is the number of points)
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

    private void sizeArray(int n){

    }

    public void union (int p , int q){ // connect p and q
        int rp = root(p);
        int rq = root(q);
        if(rp== rq) return;
        if (sz[rp] < sz[rq]) { id[rp] = rq; sz[rq] += sz[rp]; }
        else { id[rq] = rp; sz[rp] += sz[rq]; }
    }
}
