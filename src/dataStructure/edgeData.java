package dataStructure;

import java.io.Serializable;
import java.util.Comparator;

public class edgeData implements edge_data, Serializable, Comparator<edgeData> {
    int sour;
    int targ ;
    Double weight;
    String inf;
    int color;
    

    public edgeData(int src, int dest, double w){
        this.sour = src;
        this.targ = dest;
        this.weight = w;
        this.inf=null;
        this.color=0;
    }
    public edgeData(int so ,int tar,Double w,String in,int co){
        this.sour=so;
        this.targ=tar;
        this.weight=w;
        this.inf=in;
        this.color=co;
    }

    @Override
    public int getSrc() {
        return this.sour;
    }

    @Override
    public int getDest() {
        return this.targ;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.inf;
    }

    @Override
    public void setInfo(String s) {
    this.inf=s;
    }

    @Override
    public int getTag() {
        return this.color;
    }

    @Override
    public void setTag(int t) {
        if(t<3 && t>-1){this.color=t;}
        else{this.color=0;}
    }

    @Override
    public int compare(edgeData ed, edgeData ed2) {
            if (ed.getWeight() < ed2.getWeight()) return -1;
            if (ed.getWeight() > ed2.getWeight()) return 1;
            return 0;
        }
    public boolean equals(Object ob){
        if(!(ob instanceof edgeData))return false;
        edgeData ed = (edgeData) ob;
        if(ed.targ == this.targ && ed.sour == this.sour && ed.inf.equals(this.inf) && ed.weight==this.weight)return true;

        return false;
    }


}

