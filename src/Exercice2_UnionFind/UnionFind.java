import Exercice2_UnionFind.QuickFindUF;

void main() {
    IO.println(getUnionOperationsTime(20));
}

static double getUnionOperationsTime(int N){
    QuickFindUF QFUF = new QuickFindUF(N);
    double start = System.currentTimeMillis();
    // operações union
    for(int i = 0 ; i<N; i++){
        int p = (int) (N*Math.random());
        int q = (int) (N*Math.random());
        QFUF.union(p,q);
    }
    double end = System.currentTimeMillis();
    return end-start;
}