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

    public String printData(boolean isArbre) {
        StringBuilder sb = new StringBuilder();
        if (!isArbre) {
            sb.append("DATA SEGMENT\n");
        }
        for (Object enfant : enfants) {
            if (enfant instanceof Arbre arbre) {
                if (arbre.type.equals("LET")) {
                    sb.append("\t").append(arbre.enfants.get(0)).append(" DD\n");
                } else {
                    sb.append(arbre.printData(true));
                }
            }
        }
        if (!isArbre) {
            sb.append("DATA ENDS\n");
        }
        return sb.toString();
    }

    

    private void generateCodeForNode(Arbre arbre, StringBuilder sb) {
        switch (arbre.type) {
            case "LET" -> {
                sb.append("	mov eax, ");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\n	mov ").append(arbre.enfants.get(0)).append(", eax\n");

            }
            case "*" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\n	push eax\n");
                sb.append("        mov eax, ");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\n	pop ebx\n");
                sb.append("	mul eax, ebx");
            }
            case "/" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\n	push eax\n");
                sb.append("        mov eax, ");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\n	pop ebx\n");
                sb.append("	div ebx, eax\n");
                sb.append("	mov eax, ebx");
            }

            case ";" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
            }

            default -> {
                sb.append(arbre.type);
            }
        }
    }

    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("CODE SEGMENT\n");
        for (Object enfant : enfants) {
            if (enfant instanceof Arbre arbre) {
                generateCodeForNode(arbre, sb);
            }
        }
        sb.append("CODE ENDS\n");
        return sb.toString();
    }
}