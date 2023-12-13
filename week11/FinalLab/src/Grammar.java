import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {
    // Set of terminal symbols
    private final Set<String> terminals;
    // Set of non-terminal symbols
    private final Set<String> nonTerminals;
    // Map to store production rules for each non-terminal
    private final Map<String, Set<String>> productions;
    // Start symbol of the grammar
    private String startSymbol;
    // Flag to indicate if the grammar is a CFG (Context-Free Grammar)
    private boolean isCFG;

    // Constructor initializes sets, map, start symbol, and assumes it's a CFG
    public Grammar() {
        this.terminals = new HashSet<>();
        this.nonTerminals = new HashSet<>();
        this.productions = new HashMap<>();
        this.startSymbol = "";
        this.isCFG = true;
    }

    // Method to check if the grammar is a CFG
    public boolean isCFG() {
        return isCFG;
    }

    // Getter methods for terminals, non-terminals, start symbol, and productions
    public Set<String> getTerminals() {
        return terminals;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public Map<String, Set<String>> getProductions() {
        return productions;
    }

    // Utility method to convert a line read from file to a number
    private int getNumber(BufferedReader reader, int number) throws IOException {
        String value;
        value = reader.readLine();
        for (int i = 0; i < value.length(); ++i) {
            number = number * 10 + (value.charAt(i) - '0');
        }
        return number;
    }

    // Method to read grammar details from a file
    public void readGrammarFromFile(String filePath) {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filePath));

            int numberOfTerminals = 0, numberOfNonTerminals = 0, numberOfProductions = 0;

            // Read the number of non-terminals and populate the set
            numberOfNonTerminals = getNumber(reader, numberOfNonTerminals);
            for(int i = 0; i < numberOfNonTerminals; ++i) {
                this.nonTerminals.add(reader.readLine());
            }

            // Read the number of terminals and populate the set
            numberOfTerminals = getNumber(reader, numberOfTerminals);
            for(int i = 0; i < numberOfTerminals; ++i) {
                this.terminals.add(reader.readLine());
            }

            // Read the number of productions and populate the map
            numberOfProductions = getNumber(reader, numberOfProductions);
            String[] values;
            for (int i = 0; i < numberOfProductions; ++i) {
                values = reader.readLine().split(" -> ");
                String key = values[0], value = values[1];

                // Check if the key (non-terminal) is valid
                if(!this.nonTerminals.contains(key)) {
                    this.isCFG = false; // It's not a CFG
                }

                // Add production rule to the map
                if(!this.productions.containsKey(key)) {
                    this.productions.put(key, new HashSet<>());
                }
                this.productions.get(key).add(value);
            }

            // Read the start symbol of the grammar
            this.startSymbol = reader.readLine();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // Method to retrieve production rules for a non-terminal
    public Set<String> getProductionForNonTerminal(String nonTerminal) {
        return this.productions.get(nonTerminal);
    }

    // Method to print all productions in the grammar
    public String printProductions() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");

        for(Map.Entry<String, Set<String>> entry: this.productions.entrySet()) {
            stringBuilder.append(entry.getKey()).append(" -> ");

            int i = 0;
            for(String value: entry.getValue()) {
                stringBuilder.append(value);
                if(i < (entry.getValue().size() - 1)) {
                    stringBuilder.append(" | ");
                    i++;
                }
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    // Method to print productions for a specific non-terminal
    public String printProductionsForNonTerminal(String nonTerminal) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");

        int i = 0;
        Set<String> values = this.getProductionForNonTerminal(nonTerminal);
        for(String value: values) {
            stringBuilder.append(value);
            if(i < (values.size() - 1)) {
                stringBuilder.append(" | ");
                i++;
            }
        }

        return stringBuilder.toString();
    }

    // Override toString method to represent the Grammar object as a string
    @Override
    public String toString() {
        return "Grammar{" +
                "terminals = " + terminals +
                ", nonTerminals = " + nonTerminals +
                ", productions = " + printProductions() +
                ", startSymbol = '" + startSymbol + '\'' +
                ", isCFG = " + isCFG +
                '}';
    }
}