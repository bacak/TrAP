package eu.vbrlohu.trap.iotools;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OptParserTest {
    
    private final String INPUT_FILE =  "testInputFile";
    private final String OUTPUT_FILE =  "testOutputFile";
    private final int ITERATIONS = 10;
    
    private final String[] args = {"-i",INPUT_FILE,"-o",OUTPUT_FILE,"-n",String.valueOf(ITERATIONS)};
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
    public void testIterationOptions() {
        assertEquals(String.valueOf(ITERATIONS),op.getIterationsOpt());
    }
    
    @Test
    public void testIteration() {
        assertEquals(ITERATIONS,op.getIterations());
    }
}
