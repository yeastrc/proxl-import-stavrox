package org.yeastrc.proxl.xml.stavrox.reader;

import java.util.Collection;

public class StavroxCrosslinkerEnd {

    @Override
    public String toString() {
        return "StavroxCrosslinkerEnd{" +
                "residues=" + residues +
                ", bindsNTerminus=" + bindsNTerminus +
                ", bindsCTerminus=" + bindsCTerminus +
                '}';
    }

    public StavroxCrosslinkerEnd(Collection<String> residues, boolean bindsNTerminus, boolean bindsCTerminus) {
        this.residues = residues;
        this.bindsNTerminus = bindsNTerminus;
        this.bindsCTerminus = bindsCTerminus;
    }

    public Collection<String> getResidues() {
        return residues;
    }

    public boolean isBindsNTerminus() {
        return bindsNTerminus;
    }

    public boolean isBindsCTerminus() {
        return bindsCTerminus;
    }

    private Collection<String> residues;
    private boolean bindsNTerminus;
    private boolean bindsCTerminus;

}
