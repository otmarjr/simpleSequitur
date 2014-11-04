/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author otmarpereira
 */
public class sequiturTest {
    
    public sequiturTest() {
    }

    @Test
    public void testSequiturExampleChapter5LosThesis() {
        System.out.println("runSequitur");
        
        String input = "ABCBCDABCBCBCD";
        String expected = "(A(BC)+D)+";
        
        System.out.println(sequitur.getSequiturRules(input));
        
        String result = sequitur.getGrammarBasedRegex(input);
        
        
        System.out.print(result);
        assertEquals(expected, result);
    }
    
    @Test 
    public void testMerge(){
        List<String> input;
        input = new LinkedList<>();
        input.add("A(BC)+");
        input.add("D");
        input.add("A(BC)+");
        input.add("BC");
        input.add("D");
        
        List<String> expected;
        expected = new LinkedList<>();
        expected.add("A(BC)+");
        expected.add("D");
        expected.add("A(BC)+");
        expected.add("D");
        
        List<String> result = rule.mergeRepeatedItemsWithPlusOperator(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSimplestRegex(){
        List<String> input;
        input = new LinkedList<>();
        input.add("a");
        input.add("a");
        
        List<String> expected;
        expected = new LinkedList<>();
        expected.add("(");
        expected.add("a");
        expected.add(")+");
        
        List<String> result = rule.convertRepeatedSubstringsToRegex(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testLosThesisChapter5ExampleOuterLoopEquals1Regex(){
        List<String> input;
        input = new LinkedList<>();
        //ABCBCDABCBCBCD
        input.add("a");
        input.add("b");
        input.add("c");
        input.add("b");
        input.add("c");
        input.add("d");
        
        List<String> expected;
        expected = new LinkedList<>();
        expected.add("a");
        expected.add("(");
        expected.add("b");
        expected.add("c");
        expected.add(")+");
        expected.add("d");
        
        List<String> result = rule.convertRepeatedSubstringsToRegex(input);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testMutantLosThesisChapter5ExampleOuterLoopEquals1Regex(){
        List<String> input;
        input = new LinkedList<>();
        //ABCBCDABCBCBCD
        input.add("a");
        input.add("b");
        input.add("c");
        input.add("e");
        input.add("b");
        input.add("c");
        input.add("d");
        
        List<String> expected;
        expected = new LinkedList<>(input);
        
        List<String> result;
        result = rule.convertRepeatedSubstringsToRegex(input);
        
        assertEquals(expected, result);
    }
    
    
    @Test
    public void testMutantLosThesisChapter5ExampleOuterLoopEquals2Regex(){
        List<String> input;
        input = new LinkedList<>();
        //ABCBCDABCBCBCD
        input.add("a");
        input.add("b");
        input.add("c");
        input.add("d");
        input.add("a");
        input.add("b");
        input.add("c");
        input.add("d");
        
        List<String> expected;
        expected = new LinkedList<>();
        expected.add("(");
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");
        expected.add(")+");
        
        List<String> result;
        result = rule.convertRepeatedSubstringsToRegex(input);
        
        assertEquals(expected, result);
    }
    
     @Test
    public void testNonRegex(){
        List<String> input;
        input = new LinkedList<>();
        input.add("a");
        input.add("b");
        
        List<String> expected;
        expected = new LinkedList<>();
        expected.add("a");
        expected.add("b");
        
        List<String> result = rule.convertRepeatedSubstringsToRegex(input);
        
        assertEquals(expected, result);
    }
    
}
