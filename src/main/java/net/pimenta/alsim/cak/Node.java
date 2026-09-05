package net.pimenta.alsim.cak;

public class Node {
    private int id;
    public Node(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Node other)) return false;
        return id == other.id;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }
}
