import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DescendentRecursiveParser {

    public DescendentRecursiveParser() {
    }

    /**
     * This function implements the logic of EXPAND move: creates the next configuration of
     the algorithm, removing the nonterminal from the top of the input stack and processing the
     production indicated by the count parameter – adds the nonterminal to the working stack to
     know which one is used and its corresponding production to the input stack.
     * @param configuration : the current configuration (which will be changed)
     * @param count : an integer number representing the number of the production to be
    processed for the current nonterminal
     * @param grammar : the grammar of the language (the one from the fifth laboratory)
     * @return : the new (modified) configuration
     */
    public Configuration expand(Configuration configuration, int count, Grammar grammar) {
        configuration.setMove(Move.EXPAND);

        String nonTerminal = configuration.getInputStack().pop();
        String production = grammar.getProductionForNonTerminal(nonTerminal).toArray()[count].toString();

        String[] values = production.split(" ");
        List<String> list = Arrays.asList(values);
        Collections.reverse(list);
        values = list.toArray(values);

        Stack<String> workingStack = configuration.getWorkingStack();
        workingStack.push(nonTerminal + "-" + (count + 1));
        configuration.setWorkingStack(workingStack);

        Stack<String> inputStack = configuration.getInputStack();
        for(String value: values) {
            inputStack.push(value);
        }
        configuration.setInputStack(inputStack);

        return configuration;
    }

    /**
     * This function implements the logic of ADVANCE move: creates the next configuration of
     the algorithm, removing the terminal from the top of the input stack and adding it to the
     working stack. In the case the removed terminal is epsilon, it will not be added to the working
     stack (because it will never be equal to any element from the sequence).
     * @param configuration : the current configuration (which will be changed)
     * @return : the new (modified) configuration
     */
    public Configuration advance(Configuration configuration) {
        configuration.setMove(Move.ADVANCE);

        String terminal = configuration.getInputStack().pop();

        if(!terminal.equals("epsilon")) {
            Stack<String> workingStack = configuration.getWorkingStack();
            workingStack.push(terminal);
            configuration.setWorkingStack(workingStack);

            configuration.setPositionCurrentSymbol(configuration.getPositionCurrentSymbol() + 1);
        }

        return configuration;
    }

    /**
     * This function implements the logic of MOMENTARY INSUCCESS move: creates the next
     configuration of the algorithm, changing the state of parsing to BACK_STATE.
     * @param configuration : the current configuration (which will be changed)
     * @return : the new (modified) configuration
     */
    public Configuration momentaryInsuccess(Configuration configuration) {
        configuration.setMove(Move.MOMENTARY_INSUCCESS);
        configuration.setStateOfParsing(State.BACK_STATE);
        return configuration;
    }

    /**
     * This function implements the logic of BACK move: creates the next configuration of the
     algorithm, removing the terminal from the top of the working stack and adding it to the input
     stack (it decreases the position of the current symbol table because we go back with the process
     of searching for a position).
     * @param configuration : the current configuration (which will be changed)
     * @return : the new (modified) configuration
     */
    public Configuration back(Configuration configuration) {
        configuration.setMove(Move.BACK);

        configuration.setPositionCurrentSymbol(configuration.getPositionCurrentSymbol() - 1);

        String terminal = configuration.getWorkingStack().pop();
        Stack<String> inputStack = configuration.getInputStack();
        inputStack.push(terminal);
        configuration.setInputStack(inputStack);

        return configuration;
    }

    /**
     * This function implements the logic of ANOTHER TRY move: creates the next configuration
     of the algorithm, trying another production for the nonterminal from the top of the working
     stack if it exists or adding the nonterminal back to the input stack otherwise. In case we return
     to the start symbol, it is an error.
     * @param configuration : the current configuration (which will be changed)
     * @param grammar : the grammar of the language (the one from the fifth laboratory)
     * @return : the new (modified) configuration
     */
    public Configuration anotherTry(Configuration configuration, Grammar grammar) {
        configuration.setMove(Move.ANOTHER_TRY);

        String topWorkingStack = configuration.getWorkingStack().pop();
        String[] values = topWorkingStack.split("-");
        String nonTerminal = values[0];
        int count = Integer.parseInt(values[1]) - 1;

        int maximumNumberOfProductions = grammar.getProductionForNonTerminal(nonTerminal).size();
        if((count + 1) < maximumNumberOfProductions) {
            configuration.setStateOfParsing(State.NORMAL_STATE);

            Stack<String> workingStack = configuration.getWorkingStack();
            workingStack.push(nonTerminal + "-" + (count + 2));
            configuration.setWorkingStack(workingStack);

            Stack<String> inputStack = configuration.getInputStack();
            anotherTry_helper(configuration, grammar, nonTerminal, count, inputStack);

            String production = grammar.getProductionForNonTerminal(nonTerminal).toArray()[count + 1].toString();

            values = production.split(" ");
            List<String> list = Arrays.asList(values);
            Collections.reverse(list);
            values = list.toArray(values);

            inputStack = configuration.getInputStack();
            for(String value: values) {
                inputStack.push(value);
            }
            configuration.setInputStack(inputStack);
        } else if(configuration.getPositionCurrentSymbol() == 1 && nonTerminal.equals(grammar.getStartSymbol())) {
            configuration.setStateOfParsing(State.ERROR_STATE);
        } else {
            Stack<String> inputStack = configuration.getInputStack();

            anotherTry_helper(configuration, grammar, nonTerminal, count, inputStack);

            inputStack.push(nonTerminal);
            configuration.setInputStack(inputStack);
        }

        return configuration;
    }

    private void anotherTry_helper(Configuration configuration, Grammar grammar, String nonTerminal, int count, Stack<String> inputStack) {
        if(!grammar.getProductionForNonTerminal(nonTerminal).toArray()[count].equals("epsilon")) {
            int sizeOfPreviousProduction = (int) Arrays.stream(grammar.getProductionForNonTerminal(nonTerminal)
                            .toArray()[count]
                            .toString()
                            .split(" "))
                    .count();
            while(sizeOfPreviousProduction > 0) {
                inputStack.pop();
                sizeOfPreviousProduction--;
            }
            configuration.setInputStack(inputStack);
        }
    }

    /**
     * This function implements the logic of SUCCESS move: creates the next configuration of
     the algorithm, changing the state of parsing to FINAL_STATE.
     * @param configuration : the current configuration (which will be changed)
     * @return : the new (modified) configuration
     */
    public Configuration success(Configuration configuration) {
        configuration.setMove(Move.SUCCESS);

        configuration.setStateOfParsing(State.FINAL_STATE);
        return configuration;
    }

    /**
     * This function implements the parser algorithm, calling the above described functions
     when they meet their conditions. This function uses the following
     methods:
     -> private boolean verifyAdvance(String[] sequence, Grammar grammar,
     Configuration configuration) – contains all conditions needed to access advance move;
     -> public boolean isIdentifier(String identifier) – verifies if the given string is an
     identifier;
     -> public boolean isConstant(String constant) – verifies if the given string is a
     constant;
     -> private void constructWorkingAndInputStacks(List<Configuration>
     configurations, Configuration configuration)
     * @param sequence : the sequence given by user
     * @param grammar : the grammar of the language (the one from the fifth laboratory)
     */
    public void descendantRecursiveParserAlgorithm(String[] sequence, Grammar grammar) {
        List<Configuration> configurations = new ArrayList<>();
        try {
            Stack<String> inputStack = new Stack<>();
            inputStack.push(grammar.getStartSymbol());
            Configuration configuration = new Configuration(Move.EXPAND, State.NORMAL_STATE, 1, new Stack<>(), inputStack);

            constructWorkingAndInputStacks(configurations, configuration);

            while (!configuration.getStateOfParsing().equals(State.ERROR_STATE) &&
                    !configuration.getStateOfParsing().equals(State.FINAL_STATE)) {

                if (configuration.getStateOfParsing().equals(State.NORMAL_STATE)) {
                    if (configuration.getPositionCurrentSymbol() == (sequence.length + 1) && configuration.getInputStack().isEmpty()) {
                        configuration = this.success(configuration);
                    } else {
                        if (grammar.getNonTerminals().contains(configuration.getInputStack().peek())) {
                            configuration = this.expand(configuration, 0, grammar);
                        } else {
                            if (verifyAdvance(sequence, grammar, configuration)) {
                                configuration = this.advance(configuration);
                            } else {
                                configuration = this.momentaryInsuccess(configuration);
                            }
                        }
                    }

                    constructWorkingAndInputStacks(configurations, configuration);
                } else {
                    if (configuration.getStateOfParsing().equals(State.BACK_STATE)) {
                        if (grammar.getTerminals().contains(configuration.getWorkingStack().peek())) {
                            configuration = this.back(configuration);
                        } else {
                            configuration = this.anotherTry(configuration, grammar);
                        }

                        constructWorkingAndInputStacks(configurations, configuration);
                    }
                }
            }

            writeConfigurationsToFile("src/configurations.txt", configurations);

            if (configuration.getStateOfParsing().equals(State.ERROR_STATE)) {
                System.out.println("Error");
            } else {
                System.out.println("Sequence accepted");

                List<String> finalNonTerminals = selectFinalNonTerminals(configurations.get(configurations.size() - 1), grammar);
                writeTableToFile("src/table.txt", constructTree(finalNonTerminals, grammar));
            }
        } catch(Exception e) {
            writeConfigurationsToFile("src/configurations.txt", configurations);

            e.printStackTrace();
        }
    }

    public List<String> selectFinalNonTerminals(Configuration successConfiguration, Grammar grammar) {
        Stack<String> workingStack = successConfiguration.getWorkingStack();

        List<String> workingList = new ArrayList<>();
        while(!workingStack.isEmpty()) {
            String value = workingStack.pop();

            if(!value.equals("constant") && !value.equals("identifier") && !grammar.getTerminals().contains(value)) {
                workingList.add(value);
            }
        }

        Collections.reverse(workingList);

        return workingList;
    }
    /**
     * This function extracts all the non-terminals from a given working list and adds each of
     them to the tree along with their generated productions. For each parsed symbol, a new Node will be
     generated containing the following fields:
     - index: will keep increasing by 1 as each node is added to the tree
     - info: the String representation of the current symbol
     - leftSibling: for each generated production, the first element will not have a left slibing so the
     field will be marked as -1, as for the following elements, their left slibing will be the previous index value
     - parent: this field contains the index of the non-terminal which generated the current
     production. In order to remember the index of each parent, a new Dictionary is created which has as a
     key the non-terminal String representation, and as the corresponding value, a queue which contains the
     indexes of the non-terminals whose productions have not been parsed yet. Once the production is
     parsed, their parent index will be eliminated from the queue, as we will not need it anymore for the
     following iterations.
     * @param workingList: a configuration’s final working list
     * @param grammar : the grammar of the language
     */
    public List<Node> constructTree(List<String> workingList, Grammar grammar) {
        int index = 0;
        List<Node> tree = new ArrayList<>();

        String[] values = workingList.get(index).split("-");
        String nonTerminal = values[index];

        Node startNode = new Node(0, nonTerminal, -1, -1);
        tree.add(startNode);

        Map<String, Queue<Integer>> parentIndexes = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(index);
        parentIndexes.put(nonTerminal, queue);

        for (String element : workingList) {
            values = element.split("-");
            nonTerminal = values[0];
            int count = Integer.parseInt(values[1]) - 1;

            String productionForNonTerminal = grammar.getProductionForNonTerminal(nonTerminal).toArray()[count].toString();
            String[] productionElements = productionForNonTerminal.split(" ");

            index++;
            Node node = new Node(index, productionElements[0], parentIndexes.get(nonTerminal).peek(), -1);
            tree.add(node);

            if (grammar.getNonTerminals().contains(productionElements[0])) {
                if (!parentIndexes.containsKey(productionElements[0])) {
                    queue = new LinkedList<>();
                } else {
                    queue = parentIndexes.get(productionElements[0]);
                }
                queue.add(index);
                parentIndexes.put(productionElements[0], queue);
            }

            for (int j = 1; j < productionElements.length; ++j) {
                index++;
                node = new Node(index, productionElements[j], parentIndexes.get(nonTerminal).peek(), index - 1);
                tree.add(node);

                if (grammar.getNonTerminals().contains(productionElements[j])) {
                    if (!parentIndexes.containsKey(productionElements[j])) {
                        queue = new LinkedList<>();
                    } else {
                        queue = parentIndexes.get(productionElements[j]);
                    }
                    queue.add(index);
                    parentIndexes.put(productionElements[j], queue);
                }
            }

            parentIndexes.get(nonTerminal).remove();
        }

        return tree;
    }

    public void writeTableToFile(String path, List<Node> tree) {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(String.format("%40s %40s %40s %40s \r\n", "Index", "Info", "Parent", "Left Sibling"));
            for(Node node: tree) {
                writer.write(String.format("%40d %40s %40d %40d \r\n", node.getIndex(), node.getInfo(),
                        node.getParent(), node.getLeftSibling()));
            }
            writer.close();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public void writeConfigurationsToFile(String path, List<Configuration> configurations) {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(String.format("%20s \r\n", "Configuration"));
            for(Configuration configuration: configurations) {
                writer.write(String.format("%20s \r\n", configuration));
            }
            writer.close();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    private boolean verifyAdvance(String[] sequence, Grammar grammar, Configuration configuration) {
        if(sequence.length > (configuration.getPositionCurrentSymbol() - 1)) {
            if((isIdentifier(sequence[configuration.getPositionCurrentSymbol() - 1]) && configuration.getInputStack().peek().equals("identifier")) ||
                    (isConstant(sequence[configuration.getPositionCurrentSymbol() - 1]) && configuration.getInputStack().peek().equals("constant")) ||
                    (grammar.getTerminals().contains(configuration.getInputStack().peek()) &&
                            sequence[configuration.getPositionCurrentSymbol() - 1].equals(configuration.getInputStack().peek()))) {

                return true;
            }
        }

        return configuration.getInputStack().peek().equals("epsilon");
    }

    public boolean isIdentifier(String identifier) {
        return identifier.charAt(0) == '_' && identifier.length() >= 2 && Character.isLetter(identifier.charAt(1));
    }

    public boolean isConstant(String constant) {
        if(constant.charAt(0) == '"' || constant.charAt(0) == '\'') {
            return true;
        } else {
            if(Character.isDigit(constant.charAt(0))) {
                return true;
            }

            if(constant.charAt(0) == '-' || constant.charAt(0) == '+') {
                return constant.length() >= 2 && Character.isDigit(constant.charAt(1));
            }
        }

        return false;
    }

    private void constructWorkingAndInputStacks(List<Configuration> configurations, Configuration configuration) {
        Stack<String> wS = new Stack<>();
        for(String e: configuration.getWorkingStack()) {
            wS.push(e);
        }

        Stack<String> iS = new Stack<>();
        for(String e: configuration.getInputStack()) {
            iS.push(e);
        }

        configurations.add(new Configuration(configuration.getMove(), configuration.getStateOfParsing(),
                configuration.getPositionCurrentSymbol(), wS, iS));
    }
}