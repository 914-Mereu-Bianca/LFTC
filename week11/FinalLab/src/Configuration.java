import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Configuration {
    private Move move;
    private State stateOfParsing;
    private Integer positionCurrentSymbol;
    private Stack<String> workingStack;
    private Stack<String> inputStack;

    public Configuration(Move move, State stateOfParsing, Integer positionCurrentSymbol, Stack<String> workingStack, Stack<String> inputStack) {
        this.move = move;
        this.stateOfParsing = stateOfParsing;
        this.positionCurrentSymbol = positionCurrentSymbol;
        this.workingStack = workingStack;
        this.inputStack = inputStack;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public State getStateOfParsing() {
        return stateOfParsing;
    }

    public void setStateOfParsing(State stateOfParsing) {
        this.stateOfParsing = stateOfParsing;
    }

    public Integer getPositionCurrentSymbol() {
        return positionCurrentSymbol;
    }

    public void setPositionCurrentSymbol(Integer positionCurrentSymbol) {
        this.positionCurrentSymbol = positionCurrentSymbol;
    }

    public Stack<String> getWorkingStack() {
        return workingStack;
    }

    public void setWorkingStack(Stack<String> workingStack) {
        this.workingStack = workingStack;
    }

    public Stack<String> getInputStack() {
        return inputStack;
    }

    public void setInputStack(Stack<String> inputStack) {
        this.inputStack = inputStack;
    }

    public String printInputStack() {
        String[] values = this.getInputStack().toArray(new String[0]);
        List<String> list = Arrays.asList(values);
        Collections.reverse(list);
        values = list.toArray(values);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        int i = 0;
        for(String value: values) {
            stringBuilder.append(value);
            if(i < (Arrays.stream(values).count() - 1)) {
                stringBuilder.append(", ");
            }
            i++;
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "move = " + move +
                ", stateOfParsing = " + stateOfParsing +
                ", positionCurrentSymbol = " + positionCurrentSymbol +
                ", workingStack = " + workingStack +
                ", inputStack = " + printInputStack() +
                '}';
    }
}