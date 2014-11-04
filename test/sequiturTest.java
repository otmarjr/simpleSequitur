/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Event;
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
    public void testRunSequitur() {
        System.out.println("runSequitur");
        String expected = "";
        String result = sequitur.getSequiturRules("ABCBCDABCBCBCD");
        System.out.print(result);
        assertEquals(expected, result);
    }
    
    @Test
    public void test
    
}
