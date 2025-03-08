# TP Compilation : Génération d'arbres abstraits
**Auteurs :**
- RASOAMIARAMANANA Hery ny aina
- ROUSSEAU Maxime

(On a fait tout le TP, c'est dans le dossier Exercice1)

## Évaluateur d'expressions arithmétiques infixées sur les nombres entiers

### Description
Ce projet implémente un évaluateur d'expressions arithmétiques infixées sur les nombres entiers en utilisant JFlex pour l'analyse lexicale et CUP pour l'analyse syntaxique. Le projet permet de lire des expressions arithmétiques à partir de fichiers d'entrée, de les analyser, de les évaluer et de générer du code correspondant.

### Structure du projet
- `Exercice1/src/main/java/fr/usmb/m1isc/compilation/tp1/Main.java` : Point d'entrée principal du programme.
- `Exercice1/src/main/java/fr/usmb/m1isc/compilation/tp1/Arbre.java` : Classe représentant un arbre syntaxique abstrait.
- `Exercice1/src/main/jflex/AnalyseurLexical.jflex` : Analyseur lexical généré par JFlex.
- `Exercice1/src/main/cup/AnalyseurSyntaxique.cup` : Analyseur syntaxique généré par CUP.
- `test.txt`, `test1.txt`, `test2.txt` : Fichiers de test contenant des expressions arithmétiques à évaluer.

### Prérequis
- Java Development Kit (JDK)
- Gradle

### Compilation et exécution
1. Cloner le dépôt ou télécharger les fichiers du projet.
2. Ouvrir un terminal et naviguer vers le répertoire du projet.
3. ```sh
   ./gradlew build
   cd .\Exercice1\
   java -jar build/libs/Exercice1.jar test.txt
