package org.adaptivesampling.phylotree.iotools;

import java.io.File;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class OptParser {

    private static final String INPUT_FILE_OPT = "inputFile";
    private static final String OUPUT_FILE_OPT = "outputFile";
    private static final String ITERATIONS_OPT = "iterations";
    private static final String LOG_OPT = "logFile";
    
    private final Options options = createOptions();
    private final String[] args;
    private final CommandLine cmdLine;

    public OptParser(String[] args) {
        this.args = args;
        this.cmdLine = parseCmdLine();
    }
    
    public String getInputFileOpt() {
        return cmdLine.getOptionValue(INPUT_FILE_OPT);
    }
    
    public File getInputFile() {
        String fName = cmdLine.getOptionValue(INPUT_FILE_OPT);
        File f = new File(fName);
        if(!f.exists())
            exit(String.format("Input file %s doesn't exists, please provide existing file", fName));
        return f;
    }

    public String getOutputFileOpt() {
        return cmdLine.getOptionValue(OUPUT_FILE_OPT);
    }
    
    public String getIterationsOpt() {
        return cmdLine.getOptionValue(ITERATIONS_OPT);
    }
    
    public int getIterations() {
        String itr = cmdLine.getOptionValue(ITERATIONS_OPT);
        int i = 0;
        try {
            i = Integer.valueOf(itr).intValue();
        } catch(NumberFormatException e) {
            exit(String.format("Number of iterations %s doesn't seems to be an integer, please provide integer number", itr));
        }
        return i;
    }
    
    public String getLogFileOpt() {
        return cmdLine.getOptionValue(LOG_OPT);
    }
    
    private CommandLine parseCmdLine() {
        CommandLineParser parser = new BasicParser();
        CommandLine cl = null;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Parsing of command line arguments failed.  Reason: " + e.getMessage());
            printUsage(options);
            System.exit(1);
        }
        return cl;
    }

    private void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("TrAP", options);
    }
    
    private void exit(String msg) {
        System.err.println("Exiting, something went wrong:");
        System.err.println(msg);
        System.exit(1);
    }

    public static Options createOptions() {

        // required options
        Option inputFile = OptionBuilder.withArgName(INPUT_FILE_OPT).withLongOpt(INPUT_FILE_OPT).hasArg().isRequired(true)
                .withDescription("Input data file").create("i");
        Option outputFile = OptionBuilder.withArgName(OUPUT_FILE_OPT).withLongOpt(OUPUT_FILE_OPT).hasArg().isRequired(true)
                .withDescription("Output data file").create("o");
        Option iterations = OptionBuilder.withArgName(ITERATIONS_OPT).withLongOpt(ITERATIONS_OPT).hasArg().isRequired(true)
                .withDescription("Number of iterations").create("n");

        // optional options
        Option logFile = OptionBuilder.withArgName(LOG_OPT).withLongOpt(LOG_OPT).hasArg()
                .withDescription("Log file").create("log");

        Options options = new Options();
        options.addOption(inputFile);
        options.addOption(outputFile);
        options.addOption(iterations);
        options.addOption(logFile);
        return options;
    }

}
