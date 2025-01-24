/* --------------------------Section de Code Utilisateur---------------------*/
package fr.usmb.m1isc.compilation.tp1;
import java_cup.runtime.Symbol;

%%

/* -----------------Section des Declarations et Options----------------------*/
%public
%class LexicalAnalyzer
%unicode
%cup
%line   // numerotation des lignes
%column // numerotation caracteres par ligne

%{
%}

/* definitions regulieres */
nombre = [0-9]+
espace = [ \t\n\r]
commentaire = "/*" ~"*/"
lettre = [a-zA-Z]
identifiant = {lettre}[a-zA-Z0-9_]*

%%
/* ------------------------Section des Regles Lexicales----------------------*/

{espace}        { /* ignore */ }
{commentaire}   { /* ignore */ }

/* Mots-clés */
"let"           { return new Symbol(sym.LET); }
"if"            { return new Symbol(sym.IF); }
"then"          { return new Symbol(sym.THEN); }
"else"          { return new Symbol(sym.ELSE); }
"while"         { return new Symbol(sym.WHILE); }
"do"            { return new Symbol(sym.DO); }
"input"         { return new Symbol(sym.INPUT); }
"output"        { return new Symbol(sym.OUTPUT); }
"nil"           { return new Symbol(sym.NIL); }
"and"           { return new Symbol(sym.AND); }
"or"            { return new Symbol(sym.OR); }
"not"           { return new Symbol(sym.NOT); }
"mod"           { return new Symbol(sym.MOD); }

/* Opérateurs */
"="             { return new Symbol(sym.ASSIGN); }
"+"             { return new Symbol(sym.PLUS); }
"-"             { return new Symbol(sym.MINUS); }
"*"             { return new Symbol(sym.TIMES); }
"/"             { return new Symbol(sym.DIV); }
"<"             { return new Symbol(sym.LT); }
"<="            { return new Symbol(sym.LE); }
"=="            { return new Symbol(sym.EQ); }
"("             { return new Symbol(sym.LPAREN); }
")"             { return new Symbol(sym.RPAREN); }
";"             { return new Symbol(sym.SEMI); }
"."             { return new Symbol(sym.DOT); }

/* Identificateurs et nombres */
{nombre}        { return new Symbol(sym.NUMBER, Integer.parseInt(yytext())); }
{identifiant}   { return new Symbol(sym.ID, yytext()); }

/* Caracteres non reconnus */
.               { throw new Error("Caractère illégal: " + yytext()); }
