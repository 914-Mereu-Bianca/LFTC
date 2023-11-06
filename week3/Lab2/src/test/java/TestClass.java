import org.apache.commons.lang3.tuple.Pair;
import org.example.SymbolTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestClass {
    @Test
    public void testSymbolTable() {
        SymbolTable symbolTable = new SymbolTable(11);

        Pair<Integer, Integer> p1;

        try {
            p1 = symbolTable.addIdentifier("abc");
            assertEquals(p1, symbolTable.getPositionIdentifier("abc"));
            symbolTable.addIdentifier("bc");
            symbolTable.addIdentifier("de");
            symbolTable.addIdentifier("abc");
        }
        catch (Exception e) {
            assertEquals("Key abc is already in the table!", e.getMessage());
        }


//        assertEquals();

        try {
            p1 = symbolTable.addIntConstant(23);
            assertEquals(p1, symbolTable.getPositionIntConstant(23));
            symbolTable.addIntConstant(23342);
            symbolTable.addIntConstant(233);
            symbolTable.addIntConstant(23);
        }
        catch (Exception e) {
            assertEquals("Key 23 is already in the table!", e.getMessage());
        }

        try {
            p1 = symbolTable.addStringConstant("vvv");
            assertEquals(p1, symbolTable.getPositionStringConstant("vvv"));
            symbolTable.addStringConstant("rt");
            symbolTable.addStringConstant("bb");
            symbolTable.addStringConstant("vvv");
        }
        catch (Exception e) {
            assertEquals("Key vvv is already in the table!", e.getMessage());
        }


    }
}
