# TP Compilation : Génération d'arbres abstraits
**Auteurs :**
- RASOAMIARAMANANA Hery ny aina
- ROUSSEAU Maxime

## Objectif
L'objectif du TP est d'utiliser les outils JFlex et CUP pour générer des arbres abstraits correspondant à un sous ensemble du langage λ-ada.

## Exercice 1 : Expressions Arithmétiques
Utiliser JFlex et CUP pour générer l'arbre abstrait correspondant à l'analyse d'expressions arithmétiques sur les nombres entiers.

### Exemple de fichier source
```ada
12 + 5;             /* ceci est un commentaire */
10 / 2 - 3;  99;    /* le point-virgule sépare les expressions à évaluer */
/* l'évaluation donne toujours un nombre entier */
((30 * 1) + 4) mod 2; /* opérateurs binaires */
3 * -4;             /* attention à l'opérateur unaire */

let prixHt = 200;   /* une variable prend valeur lors de sa déclaration */
let prixTtc =  prixHt * 119 / 100;
prixTtc + 100.
```

### Exemple de génération d'arbre
L'expression :
```ada
let prixTtc =  prixHt * 119 / 100;
prixTtc + 100
```
pourra donner, par exemple, l'arbre suivant sous forme d'expression préfixée parenthésée :
```
(; (LET prixTtc (/ (* prixHt 119) 100)) (+ prixTtc 100))
```

## Exercice 2 : Extension de la Grammaire
Compléter la grammaire précédente en y ajoutant les opérateurs booléens, ceux de comparaison, la boucle et la conditionnelle, pour obtenir un sous-ensemble plus complet du langage λ-ada.

### Grammaire abstraite
```
expression → expression ';' expression  
expression → LET IDENT '=' expression
expression → IF expression THEN expression ELSE expression
expression → WHILE expression DO expression
expression → '-' expression
expression → expression '+' expression
expression → expression '-' expression
expression → expression '*' expression
expression → expression '/' expression
expression → expression MOD expression
expression → expression '<' expression
expression → expression '<=' expression
expression → expression '=' expression
expression → expression AND expression
expression → expression OR expression
expression → NOT expression 
expression → OUTPUT expression 
expression → INPUT | NIL | IDENT | ENTIER
```

### Exemple de programme : Calcul de PGCD
```ada
let a = input;
let b = input;
while (0 < b)
do (let aux=(a mod b); let a=b; let b=aux );
output a .
```
