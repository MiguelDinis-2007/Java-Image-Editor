package Exercice3_RAQS;

public class ResizingArrayQueueOfStrings {

    private String[] queue; // Array que guarda as palavras
    private int N; // Variável que indica o endereço atual do array

    public ResizingArrayQueueOfStrings(){
        queue = new String[1];
        N = 0;
    }

    private void resize(int capacity){
        String[] copy = new String[capacity];
        for (int i = 0; i < N; i++){
            copy[i] = queue[i];
        }
        queue = copy;
    }


    public void enqueue(String item){
        if (N == queue.length){
            resize(2*queue.length);
        }
        queue[N++] = item;
    }

    public String dequeue() {
        if(N!=0) {
            String first = queue[0];
            String[] copy = new String[queue.length-1];
            N--;
            for(int i = 0; i < N; i++){
                copy[i] = queue[i+1];
            }
            queue = copy;
            return first;
        }
        return null;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void shift(){
        if(N>1) {
            String[] copy = new String[queue.length];
            for (int i = 1; i < N - 1; i++) {
                copy[i] = queue[i];
            }
            copy[0] = queue[N-1];
            copy[N - 1] = queue[0];
            queue = copy;
        }
    }
}
