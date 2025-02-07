package fr.usmb.m1isc.compilation.tp1;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception  {
         LexicalAnalyzer yy;
         if (args.length > 0)
                yy = new LexicalAnalyzer(new FileReader(args[0])) ;
            else
                yy = new LexicalAnalyzer(new InputStreamReader(System.in)) ;
        @SuppressWarnings("deprecation")
        parser p = new parser (yy);

        @SuppressWarnings("unchecked")
        List<Arbre> arbres = (List<Arbre>) p.parse().value;

        for (Arbre arbre : arbres) {
            System.out.println(arbre.generateCode());
        }
    }

}