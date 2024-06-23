package org.example.Utilities;

public class Trio<T, U, Z>{

    private T first;
    private U second;
    private Z third;

    public Trio(T first, U second, Z third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst(){
        return this.first;
    }
    public U getSecond(){
        return this.second;
    }
    public Z getThird(){
        return this.third;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Trio){
            Trio<T, U, Z> other = (Trio<T, U, Z>) obj;
            return this.first.equals(other.first) && this.second.equals(other.second) && this.third.equals(other.third);
        }
        return false;
    }
    
}
