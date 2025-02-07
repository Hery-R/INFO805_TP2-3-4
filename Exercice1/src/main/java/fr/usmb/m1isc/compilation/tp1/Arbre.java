package fr.usmb.m1isc.compilation.tp1;

import java.util.ArrayList;
import java.util.List;

public class Arbre {
    private final String type;
    private final List<Object> enfants;

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

    public String generateCode() {
        StringBuilder sb = new StringBuilder();

        sb.append("DATA SEGMENT\n");
        for (Object enfant : enfants) {
            if (enfant instanceof Arbre arbre) {
                if (arbre.type.equals("LET")) {
                    sb.append("\t").append(arbre.enfants.get(0)).append(" DD\n");
                }
            }
        }
        sb.append("DATA ENDS\n");

        sb.append("CODE SEGMENT\n");
        for (Object enfant : enfants) {
            if (enfant instanceof Arbre arbre) {
                switch (arbre.type) {
                    case "LET" -> {
                        sb.append("	mov eax, ").append(arbre.enfants.get(1)).append("\n");
                        sb.append("	mov ").append(arbre.enfants.get(0)).append(", eax\n");
                    }
                    case "*" -> {
                        sb.append("	mov eax, ").append(arbre.enfants.get(0)).append("\n");
                        sb.append("	push eax\n");
                        sb.append("	mov eax, ").append(arbre.enfants.get(1)).append("\n");
                        sb.append("	pop ebx\n");
                        sb.append("	mul eax, ebx\n");
                    }
                    case "/" -> {
                        sb.append("	mov eax, ").append(arbre.enfants.get(0)).append("\n");
                        sb.append("	push eax\n");
                        sb.append("	mov eax, ").append(arbre.enfants.get(1)).append("\n");
                        sb.append("	pop ebx\n");
                        sb.append("	div ebx, eax\n");
                    }
                    default -> {
                    }
                }
                            }
        }
        sb.append("CODE ENDS\n");
        return sb.toString();
    }
}