package org.example;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(47);
        Pair<Integer, Integer> p1 = new ImmutablePair<>(-1,-1);
        Pair<Integer, Integer> p2 = new ImmutablePair<>(-1,-1);
        Pair<Integer, Integer> p3 = new ImmutablePair<>(-1,-1);

        try {
            p1 = symbolTable.addIdentifier("abc");
            System.out.println("abc -> " + p1);
            System.out.println("bc -> " + symbolTable.addIdentifier("bc"));
            System.out.println("banana -> " + symbolTable.addIdentifier("banana"));

            System.out.println("2 -> " + symbolTable.addIntConstant(2));
            System.out.println("3 -> " + symbolTable.addIntConstant(3));
            p2 = symbolTable.addIntConstant(100);
            System.out.println("100 -> " + p2);
            System.out.println("1701 -> " + symbolTable.addIntConstant(1701));
            System.out.println("900 -> " + symbolTable.addIntConstant(900));

            System.out.println("mmmmm -> " + symbolTable.addStringConstant("mmmm"));
            System.out.println("hello -> " + symbolTable.addStringConstant("hello"));
            p3 = symbolTable.addStringConstant("world");
            System.out.println("world -> " + p3);

            System.out.println("abc -> " + symbolTable.addIdentifier("abc"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(symbolTable);

    }
}