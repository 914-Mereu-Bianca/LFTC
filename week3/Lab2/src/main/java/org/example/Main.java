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
            System.out.println("d -> " + symbolTable.addIdentifier("d"));
            System.out.println("a -> " + symbolTable.addIdentifier("a"));
            System.out.println("bc -> " + symbolTable.addIdentifier("bc"));
            System.out.println("banana -> " + symbolTable.addIdentifier("banana"));

            System.out.println("2 -> " + symbolTable.addIntConstant(2));
            System.out.println("3 -> " + symbolTable.addIntConstant(3));
            p2 = symbolTable.addIntConstant(100);
            System.out.println("100 -> " + p2);
            System.out.println("20 -> " + symbolTable.addIntConstant(20));
            System.out.println("1701 -> " + symbolTable.addIntConstant(1701));
            System.out.println("900 -> " + symbolTable.addIntConstant(900));
            System.out.println("96 -> " + symbolTable.addIntConstant(96));

            System.out.println("string1 -> " + symbolTable.addStringConstant("string1"));
            System.out.println("string2 -> " + symbolTable.addStringConstant("string2"));
            System.out.println("mmmmm -> " + symbolTable.addStringConstant("mmmm"));
            System.out.println("hello -> " + symbolTable.addStringConstant("hello"));
            p3 = symbolTable.addStringConstant("world");
            System.out.println("world -> " + p3);

            System.out.println("abc -> " + symbolTable.addIdentifier("abc"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println(symbolTable);

        // check pos of a string constant
        if(symbolTable.getPositionStringConstant("world").equals(p3)) {
            System.out.println("world has position " + p3);
        } else {
            System.out.println("world does not have position " + p3);
        }

        // check pos of an int constant
        if(symbolTable.getPositionIntConstant(96).equals(new ImmutablePair<>(2, 2))) {
            System.out.println("96 has the position (2, 2)");
        } else {
            System.out.println("96 does not have the position (2, 2)");
        }

        // check pos of an identifier
        if(symbolTable.getPositionIdentifier("abc").equals(p1)) {
            System.out.println("abc has the position " + p1);
        } else {
            System.out.println("abc does not have the position " + p1);
        }

        if(!Objects.equals(symbolTable.getPositionIdentifier("banana"), new ImmutablePair<>(-1, -1))) {
            System.out.println("banana is in the table at position " + symbolTable.getPositionIdentifier("banana"));
        } else {
            System.out.println("banana is not in the table at position " + symbolTable.getPositionIdentifier("banana"));
        }

        if(symbolTable.getPositionIntConstant(22).equals(p2)) {
            System.out.println("22 has the position " + p2);
        }
        else {
            System.out.println("22 does not have the position " + p2);
        }

        if(!Objects.equals(symbolTable.getPositionIdentifier("word"), new ImmutablePair<>(-1, -1))) {
            System.out.println("word is in the table at position " + symbolTable.getPositionIdentifier("ba"));
        }
        else {
            System.out.println("word does not have a position ");
        }

    }
}