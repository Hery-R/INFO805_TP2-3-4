package fr.usmb.m1isc.compilation.tp1;

import java.util.ArrayList;
import java.util.List;

public class Arbre {
    private String type;
    private List<Object> enfants;

    public Arbre(String type) {
        this.type = type;
        this.enfants = new ArrayList<>();
    }

    public void ajouterEnfant(Object enfant) {
        enfants.add(enfant);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(type);
        for (Object enfant : enfants) {
            sb.append(" ").append(enfant.toString());
        }
        sb.append(")");
        return sb.toString();
    }
}
