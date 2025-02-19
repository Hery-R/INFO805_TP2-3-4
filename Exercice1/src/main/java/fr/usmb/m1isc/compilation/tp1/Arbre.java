package fr.usmb.m1isc.compilation.tp1;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

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
        
        // Set pour garder trace des variables déjà déclarées
        Set<String> declaredVars = new HashSet<>();
        
        for (Object enfant : enfants) {
            if (enfant instanceof Arbre arbre) {
                if (arbre.type.equals("LET")) {
                    String varName = arbre.enfants.get(0).toString();
                    if (!declaredVars.contains(varName)) {
                        sb.append("\t").append(varName).append(" DD\n");
                        declaredVars.add(varName);
                    }
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

    

    private static int labelCounter = 0;
    
    private void generateCodeForNode(Arbre arbre, StringBuilder sb) {
        switch (arbre.type) {
            case "LET" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\n\tmov ").append(arbre.enfants.get(0)).append(", eax\n");
            }
            case "INPUT" -> {
                sb.append("\tin eax");
            }
            case "OUTPUT" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\n\tout eax");
            }
            case "WHILE" -> {
                int currentLabel = ++labelCounter;
                sb.append("\ndebut_while_").append(currentLabel).append(":\n");
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\n\tjz sortie_while_").append(currentLabel).append("\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\n\tjmp debut_while_").append(currentLabel).append("\n");
                sb.append("sortie_while_").append(currentLabel).append(":");
            }
            case "<" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\n\tpush eax");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\n\tpop ebx");
                sb.append("\n\tsub eax, ebx");
                int currentLabel = labelCounter + 1;
                sb.append("\n\tjle faux_gt_").append(currentLabel);
                sb.append("\n\tmov eax,1");
                sb.append("\n\tjmp sortie_gt_").append(currentLabel);
                sb.append("\nfaux_gt_").append(currentLabel).append(":");
                sb.append("\n\tmov eax,0");
                sb.append("\nsortie_gt_").append(currentLabel).append(":");
            }
            case "MOD" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\n\tpush eax");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\n\tpop ebx");
                sb.append("\n\tmov ecx,eax");
                sb.append("\n\tdiv ecx,ebx");
                sb.append("\n\tmul ecx,ebx");
                sb.append("\n\tsub eax,ecx");
            }
            case ";" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
            }
            default -> {
                if (arbre.type.matches("-?\\d+")) {
                    sb.append("\n\tmov eax, ").append(arbre.type);
                } else {
                    sb.append("\n\tmov eax, ").append(arbre.type);
                }
            }
        }
    }

    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        labelCounter = 0;  // Réinitialiser le compteur au début
        sb.append("CODE SEGMENT\n");
        for (Object enfant : enfants) {
            if (enfant instanceof Arbre arbre) {
                generateCodeForNode(arbre, sb);
            }
        }
        sb.append("\nCODE ENDS\n");
        return sb.toString();
    }
}