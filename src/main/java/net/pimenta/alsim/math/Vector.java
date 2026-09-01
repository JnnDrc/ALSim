package net.pimenta.alsim.math;

public class Vector {
    private final double[]    data;
    private final int         size;

    public Vector(int size){
        data = new double[size];
        this.size = size;
    }

    public Vector(double... xs){
        this.size = xs.length;
        data = xs;
    }

    public double get(int i){
        return data[i];
    }

    public void set(int i, double x){
        data[i] = x;
    }

    public int size(){
        return  size;
    }

    public void add(int i, double x){
        this.data[i] += x;
    }
    public void sub(int i, double x){
        this.data[i] -= x;
    }

    public void add(Vector other){
        if (other.size != this.size) return;
        for(int i = 0; i < this.size; i++) this.data[i] += other.data[i];
    }
    public void sub(Vector other){
        if (other.size != this.size) return;
        for(int i = 0; i < this.size; i++) this.data[i] += other.data[i];
    }

    public void scale(double x){
        for(int i = 0; i < this.size; i++) this.data[i] *= x;
    }

    public void scale(int i, double x){
        this.data[i] *= x;
    }

    public void swap(int a, int b){
        double temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }
    public void add(int a, int b, double k){
        data[a] += k*data[b];
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < size; i++){
            sb.append(String.format("%10.4f",data[i]));
            if(i < size - 1) sb.append("\n");
        }

        return sb.toString();
    }

}
