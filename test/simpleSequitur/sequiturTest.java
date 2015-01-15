package simpleSequitur;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import simpleSequitur.sequitur;
import simpleSequitur.rule;
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
        
        String result = sequitur.getGrammarBasedRegex(input,null);
        
        
        System.out.print(result);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSequiturExampleChapter5LosThesisWithDelimiter() {
        System.out.println("runSequitur");
        
        String input = "A,B,C,B,C,D,A,B,C,B,C,B,C,D";
        String expected = "(A,(B,C)+,D)+";
        
        String result = sequitur.getGrammarBasedRegex(input,",");
        
        
        System.out.print(result);
        assertEquals(expected, result);
    }
    
    @Test
    public void testWithB6411513ClassInput() {
        System.out.println("B6411513 - DatagramSocket class test");
        
        String input = ">java.net.NetworkInterface.Enumeration java.net.NetworkInterface.getNetworkInterfaces() >java.net.NetworkInterface.boolean java.net.NetworkInterface.isUp() >java.net.NetworkInterface.boolean java.net.NetworkInterface.isVirtual() >java.net.NetworkInterface.Enumeration java.net.NetworkInterface.getInetAddresses() >java.net.NetworkInterface.String java.net.NetworkInterface.getName() >new java.net.DatagramSocket.java.net.DatagramSocket(int, InetAddress) >java.net.DatagramSocket.void java.net.DatagramSocket.connect(InetAddress, int) >java.net.DatagramSocket.void java.net.DatagramSocket.disconnect() >java.net.DatagramSocket.InetAddress java.net.DatagramSocket.getLocalAddress() >java.net.DatagramSocket.int java.net.DatagramSocket.getLocalPort() >new java.net.DatagramPacket.java.net.DatagramPacket(byte[], int, InetAddress, int) >java.net.DatagramSocket.void java.net.DatagramSocket.setSoTimeout(int) >java.net.DatagramSocket.void java.net.DatagramSocket.send(DatagramPacket) >java.net.DatagramSocket.void java.net.DatagramSocket.receive(DatagramPacket) >java.net.DatagramSocket.void java.net.DatagramSocket.close() >new java.net.DatagramSocket.java.net.DatagramSocket(int, InetAddress) >java.net.DatagramSocket.void java.net.DatagramSocket.connect(InetAddress, int) >java.net.DatagramSocket.void java.net.DatagramSocket.disconnect() >java.net.DatagramSocket.InetAddress java.net.DatagramSocket.getLocalAddress() >java.net.DatagramSocket.int java.net.DatagramSocket.getLocalPort() >new java.net.DatagramPacket.java.net.DatagramPacket(byte[], int, InetAddress, int) >java.net.DatagramSocket.void java.net.DatagramSocket.setSoTimeout(int) >java.net.DatagramSocket.void java.net.DatagramSocket.send(DatagramPacket) >java.net.DatagramSocket.void java.net.DatagramSocket.receive(DatagramPacket) >java.net.DatagramSocket.void java.net.DatagramSocket.close()";
        
        String result = sequitur.getGrammarBasedRegex(input," >");
        
        
        System.out.print(result);
        assertNotNull(result);
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
        expected.add("(a)+");
        
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
        expected.add("(b");
        expected.add("c)+");
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
        expected.add("(a");
        expected.add("b");
        expected.add("c");
        expected.add("d)+");
        
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
