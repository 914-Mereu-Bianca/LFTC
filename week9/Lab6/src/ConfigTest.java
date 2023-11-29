import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ConfigTest {

    @Test
    public void test() {

        Grammar grammar = new Grammar();
        DescendentRecursiveParser descendentRecursiveParser = new DescendentRecursiveParser();
        grammar.readGrammarFromFile("src/g1.txt");

        String sequence = "b a b a";

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

        List<Configuration> result = descendentRecursiveParser.descendantRecursiveParserAlgorithm(values.toArray(new String[0]), grammar);


        String string_result = "";

        for(Configuration configuration: result) {
            string_result += String.format("%20s \n", configuration);
        }

        assertEquals(string_result, "Configuration{move = EXPAND, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 1, workingStack = [], inputStack = [S]} \n" +
                "Configuration{move = EXPAND, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 1, workingStack = [S-1], inputStack = [a, A]} \n" +
                "Configuration{move = MOMENTARY_INSUCCESS, stateOfParsing = BACK_STATE, positionCurrentSymbol = 1, workingStack = [S-1], inputStack = [a, A]} \n" +
                "Configuration{move = ANOTHER_TRY, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 1, workingStack = [S-2], inputStack = [B, a]} \n" +
                "Configuration{move = EXPAND, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 1, workingStack = [S-2, B-1], inputStack = [b, A, a]} \n" +
                "Configuration{move = ADVANCE, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 2, workingStack = [S-2, B-1, b], inputStack = [A, a]} \n" +
                "Configuration{move = EXPAND, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 2, workingStack = [S-2, B-1, b, A-1], inputStack = [a, C, b, a]} \n" +
                "Configuration{move = ADVANCE, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 3, workingStack = [S-2, B-1, b, A-1, a], inputStack = [C, b, a]} \n" +
                "Configuration{move = EXPAND, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 3, workingStack = [S-2, B-1, b, A-1, a, C-1], inputStack = [epsilon, b, a]} \n" +
                "Configuration{move = ADVANCE, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 3, workingStack = [S-2, B-1, b, A-1, a, C-1], inputStack = [b, a]} \n" +
                "Configuration{move = ADVANCE, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 4, workingStack = [S-2, B-1, b, A-1, a, C-1, b], inputStack = [a]} \n" +
                "Configuration{move = ADVANCE, stateOfParsing = NORMAL_STATE, positionCurrentSymbol = 5, workingStack = [S-2, B-1, b, A-1, a, C-1, b, a], inputStack = []} \n" +
                "Configuration{move = SUCCESS, stateOfParsing = FINAL_STATE, positionCurrentSymbol = 5, workingStack = [S-2, B-1, b, A-1, a, C-1, b, a], inputStack = []} \n");

    }

}
