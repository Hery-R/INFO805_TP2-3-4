package fr.usmb.m1isc.compilation.tp1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public void affichageStructure(String prefix, boolean isLeft) {
        String value = type;
        Arbre fg = null;
        Arbre fd = null;
        if (!enfants.isEmpty()) {
            if (enfants.get(0) instanceof Arbre arbre) {
                fg = arbre;
            }
            if (enfants.size() > 1 && enfants.get(1) instanceof Arbre) {
                fd = (Arbre) enfants.get(1);
            }
        }
        System.out.println(prefix + (isLeft ? "|-- " : "|-- ") + value);

        if (fg != null) {
            fg.affichageStructure(prefix + (isLeft ? "|   " : "    "), true);
        }
        if (fd != null) {
            fd.affichageStructure(prefix + (isLeft ? "|   " : "    "), false);
        }
    }

    private void collectVariables(Set<String> vars) {
        for (Object enfant : enfants) {
            if (enfant instanceof Arbre arbre) {
                if (arbre.type.equals("LET")) {
                    vars.add(arbre.enfants.get(0).toString());
                }
                arbre.collectVariables(vars);
            }
        }
    }

    public String printData(boolean isArbre) {
        StringBuilder sb = new StringBuilder();
        if (!isArbre) {
            Set<String> variables = new HashSet<>();
            collectVariables(variables);
            
            sb.append("DATA SEGMENT\n");
            for (String var : variables) {
                sb.append("\t").append(var).append(" DD\n");
            }
            sb.append("DATA ENDS");
        }
        return sb.toString();
    }

    

    private static int labelCounter = 0;
    
    private void generateCodeForNode(Arbre arbre, StringBuilder sb) {
        switch (arbre.type) {
            case "LET" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tmov ").append(arbre.enfants.get(0)).append(", eax\n");
            }
            case "INPUT" -> {
                sb.append("\tin eax\n");
            }
            case "OUTPUT" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tout eax\n");
            }
            case "WHILE" -> {
                int currentLabel = ++labelCounter;
                sb.append("debut_while_").append(currentLabel).append(":\n");
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tjz sortie_while_").append(currentLabel).append("\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tjmp debut_while_").append(currentLabel).append("\n");
                sb.append("sortie_while_").append(currentLabel).append(":\n");
            }
            case "<" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tpush eax\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tpop ebx\n");
                sb.append("\tsub eax, ebx\n");
                int currentLabel = labelCounter + 1;
                sb.append("\tjle faux_gt_").append(currentLabel).append("\n");
                sb.append("\tmov eax, 1\n");
                sb.append("\tjmp sortie_gt_").append(currentLabel).append("\n");
                sb.append("faux_gt_").append(currentLabel).append(":\n");
                sb.append("\tmov eax, 0\n");
                sb.append("sortie_gt_").append(currentLabel).append(":\n");
            }
            case "+" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tpush eax\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tpop ebx\n");
                sb.append("\tadd eax, ebx\n");
            }
            case "-" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tpush eax\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tpop ebx\n");
                sb.append("\tsub eax, ebx\n");
            }
            case "*" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tpush eax\n");
                sb.append("\tmov eax,\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tpop ebx\n");
                sb.append("\tmul eax, ebx\n");
            }
            case "/" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tpush eax\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tpop ebx\n");
                sb.append("\tdiv ebx, eax\n");
                sb.append("\tmov eax, ebx\n");
            }
            case "MOD" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                sb.append("\tpush eax\n");
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
                sb.append("\tpop ebx\n");
                sb.append("\tmov ecx, eax\n");
                sb.append("\tdiv ecx, ebx\n");
                sb.append("\tmul ecx, ebx\n");
                sb.append("\tsub eax, ecx\n");
            }
            case ";" -> {
                generateCodeForNode((Arbre) arbre.enfants.get(0), sb);
                generateCodeForNode((Arbre) arbre.enfants.get(1), sb);
            }
            default -> {
                if (arbre.type.matches("-?\\d+")) {
                    sb.append("\tmov eax, ").append(arbre.type).append("\n");
                } else {
                    sb.append("\tmov eax, ").append(arbre.type).append("\n");
                }
            }
        }
    }

    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        labelCounter = 0;
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