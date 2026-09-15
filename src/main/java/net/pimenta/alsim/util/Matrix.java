package net.pimenta.alsim.util;

public class Matrix {
    private final double[][] data;
    private final int rows;
    private final int cols;

    public Matrix(int r, int c){
        this.data = new double[r][c];
        this.rows = r;
        this.cols = c;
    }

    public int rows(){
        return this.rows;
    }

    public int cols(){
        return this.cols;
    }

    public double get(int row, int col){
        return this.data[row][col];
    }

    public void set(int i, int j, double x){
        this.data[i][j] = x;
    }

    public void add(int i, int j, double x){
        this.data[i][j] += x;
    }

    public void add(Matrix other){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                this.data[i][j] += other.data[i][j];
            }
        }
    }

    public void scale(int i, int j, double x){
        this.data[i][j] *= x;
    }

    public void scale(double x){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                this.data[i][j] *= x;
            }
        }
    }

    public Matrix mul(Matrix other) throws IllegalArgumentException{
        if(this.cols != other.rows){
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication");
        }
        Matrix result = new Matrix(this.rows,this.cols);

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                double sum = 0.0;
                for(int k = 0; k < this.cols; k++){
                    sum += this.data[i][k] * this.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }

        return result;
    }

    public Vector apply(Vector other) throws IllegalArgumentException{
        if(this.cols != other.size()){
            throw new IllegalArgumentException("Matrix columns must match of vector");
        }
        Vector result = new Vector(this.rows);

        for(int i = 0; i < this.rows; i++){
            double sum = 0.0;
            for(int j = 0; j < this.cols; j++){
                sum += this.data[i][j] * other.get(j);
            }
            result.set(i,sum);
        }

        return  result;
    }

    public void swapRows(int a, int b){
        double[] temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }
    public void scaleRow(int r, double k){
        for(int i = 0; i < cols; i++){
            data[r][i] *= k;
        }
    }
    public void addRows(int a, int b, double k){
        for(int i = 0; i < cols; i++){
            data[a][i] += k*data[b][i];
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                sb.append(String.format("%10.4f",data[i][j]));
            }
            if(i < rows - 1) sb.append("\n");
        }

        return sb.toString();
    }
}
