package eu.vbrlohu.trap.iotools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptParserTest {
    
    private final String INPUT_FILE =  "testInputFile";
    private final String OUTPUT_FILE =  "testOutputFile";
    private final String QUANTITY =  "median";
    private final String METHOD =  "cyclic";
    private final int ITERATIONS = 10;
    
    private final String[] args = {"-i",INPUT_FILE,"-o",OUTPUT_FILE,"-q",QUANTITY,"-m",METHOD,"-n",String.valueOf(ITERATIONS)};
    private final OptParser op = new OptParser(args);
    
    @Test
    public void testInputFileOptions() {
        assertEquals(INPUT_FILE,op.getInputFileOpt());
    }
    
    @Test
    public void testOutputFileOptions() {
        assertEquals(OUTPUT_FILE,op.getOutputFileOpt());
    }
    
    @Test
    public void testQuantity() {
        assertEquals(Quantity.median,op.getQuantity());
    }
    
    @Test
    public void testQuantityOptions() {
        assertEquals(QUANTITY,op.getQuantityOpt());
    }
    
    @Test
    public void testMethod() {
        assertEquals(Method.cyclic,op.getMethod());
    }
    
    @Test
    public void testMethodOpt() {
        assertEquals(METHOD,op.getMethodOpt());
    }
    
    @Test
    public void testIterationOptions() {
        assertEquals(String.valueOf(ITERATIONS),op.getIterationsOpt());
    }
    
    @Test
    public void testIteration() {
        assertEquals(ITERATIONS,op.getIterations());
    }
}
