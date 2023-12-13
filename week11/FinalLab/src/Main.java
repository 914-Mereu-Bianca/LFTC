import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void showMenu() {
        System.out.println("1. Print set of nonTerminals.");
        System.out.println("2. Print set of terminals.");
        System.out.println("3. Print set of productions.");
        System.out.println("4. Print set of productions for a given nonTerminal.");
        System.out.println("5. Print start symbol.");
        System.out.println("6. Verify if CFG.");
        System.out.println("7. Parse sequence.");
        System.out.println("8. Write to file out1.txt");
        System.out.println("0. Exit.\n");
    }

    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        String source = "";
        BufferedReader reader_source = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("source = ");
        try {
            source = reader_source.readLine();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
        if (Objects.equals(source, "g1")) {
            grammar.readGrammarFromFile("src/g1.txt");
        }
        else if (Objects.equals(source, "g2")) {
            grammar.readGrammarFromFile("src/g2.txt");
        }
        else{
            System.out.println("This option is not available. Try again.\n");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean condition = true;
        String choice = "";

        while (condition) {
            showMenu();

            System.out.print("choice = ");
            try {
                choice = reader.readLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            switch (choice) {
                case "1":
                    System.out.println(grammar.getNonTerminals().size() + "\n" + grammar.getNonTerminals() + "\n");
                    break;
                case "2":
                    System.out.println(grammar.getTerminals().size() + "\n" + grammar.getTerminals() + "\n");
                    break;
                case "3":
                    System.out.println(grammar.getProductions().size() + grammar.printProductions() + "\n");
                    break;
                case "4":
                    String nonTerminal = "";

                    try {
                        nonTerminal = reader.readLine();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    System.out.println(grammar.getProductionForNonTerminal(nonTerminal).size()
                            + grammar.printProductionsForNonTerminal(nonTerminal) + "\n");
                    break;
                case "5":
                    System.out.println(grammar.getStartSymbol() + "\n");
                    break;
                case "6":
                    boolean isCFG = grammar.isCFG();
                    if (isCFG) {
                        System.out.println("The grammar is CFG.\n");
                    } else {
                        System.out.println("The grammar is not CFG.\n");
                    }
                    break;
                case "7":
                    String sequence = "";
                    System.out.print("sequence = ");
                    try {
                        sequence = reader.readLine();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    String[] split = sequence.split(" ");
                    List<String> values = new ArrayList<>();
                    for(int i = 0; i < split.length; ++i) {
                        if(split[i].charAt(0) != '"') {
                            values.add(split[i]);
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(split[i]).append(" ");

                            if(!split[i].endsWith("\"")) {
                                i++;
                                while(i < split.length) {
                                    if (split[i].endsWith("\"")) {
                                        stringBuilder.append(split[i]);
                                        break;
                                    } else {
                                        stringBuilder.append(split[i]).append(" ");
                                    }
                                    i++;
                                }
                            }

                            values.add(stringBuilder.toString());
                        }
                    }

                    DescendentRecursiveParser descendentRecursiveParser = new DescendentRecursiveParser();

                    boolean ok = true;
                    for(String value: values) {
                        if (!descendentRecursiveParser.isIdentifier(value) &&
                                !descendentRecursiveParser.isConstant(value) && !grammar.getTerminals().contains(value)) {
                            ok = false;
                            System.out.println(value);
                            break;
                        }
                    }

                    if(ok) {
                        System.out.println();
                        descendentRecursiveParser.descendantRecursiveParserAlgorithm(values.toArray(new String[0]), grammar);
                        System.out.println();
                    } else {
                        System.out.println("The sequence is not correct!\n");
                    }

                    break;
                case "8":
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("src/out1.txt"));
                        writer.write(grammar.getNonTerminals().size() + "\n" + grammar.getNonTerminals() + "\n");
                        writer.write(grammar.getTerminals().size() + "\n" + grammar.getTerminals() + "\n");
                        String sequence_text = "";
                        writer.write(grammar.getProductions().size() + grammar.printProductions() + "\n");
                        BufferedReader reader_sequence = new BufferedReader(new FileReader("src/seq.txt"));
                        try {
                            sequence_text = reader_sequence.readLine();
                        } catch(IOException ioException) {
                            ioException.printStackTrace();
                        }
                        String[] split1 = sequence_text.split(" ");
                        List<String> values1 = new ArrayList<>();
                        for(int i = 0; i < split1.length; ++i) {
                            if(split1[i].charAt(0) != '"') {
                                values1.add(split1[i]);
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(split1[i]).append(" ");

                                if(!split1[i].endsWith("\"")) {
                                    i++;
                                    while(i < split1.length) {
                                        if (split1[i].endsWith("\"")) {
                                            stringBuilder.append(split1[i]);
                                            break;
                                        } else {
                                            stringBuilder.append(split1[i]).append(" ");
                                        }
                                        i++;
                                    }
                                }

                                values1.add(stringBuilder.toString());
                            }
                        }

                        DescendentRecursiveParser descendentRecursiveParser1 = new DescendentRecursiveParser();

                        boolean ok1 = true;
                        for(String value: values1) {
                            if (!descendentRecursiveParser1.isIdentifier(value) &&
                                    !descendentRecursiveParser1.isConstant(value) && !grammar.getTerminals().contains(value)) {
                                ok1 = false;
                                System.out.println(value);
                                break;
                            }
                        }

                        if(ok1) {
                            System.out.println();
                            descendentRecursiveParser1.descendantRecursiveParserAlgorithm(values1.toArray(new String[0]), grammar);
                            String table = "";
                            BufferedReader reader_sequence1 = new BufferedReader(new FileReader("src/table.txt"));
                            try {
                                table = new String(Files.readAllBytes(Paths.get("src/table.txt")), StandardCharsets.UTF_8);
                            } catch(IOException ioException) {
                                ioException.printStackTrace();
                            }
                            writer.write(table);
                            System.out.println();
                        } else {
                            System.out.println("The sequence is not correct!\n");
                        }
                        writer.close();
                    }
                    catch(IOException exception){
                        exception.printStackTrace();
                    }


                    break;
                case "0":
                    condition = false;
                    break;
                default:
                    System.out.println("This option is not available. Try again.\n");
                    break;
            }
        }
    }
}