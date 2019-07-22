package eu.vbrlohu.trap.iotools;

/**
 * This file is part of TrAP, Tree Averaging Program, which computes medians and means of phylogenetic trees.
 * Copyright (C) 2013-2019 Miroslav Bacak
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by mbacak on 7/22/19.
 */

import eu.vbrlohu.trap.iotools.Utils;
import eu.vbrlohu.trap.treetools.MeanAlg;
import eu.vbrlohu.trap.treetools.Tree;
import eu.vbrlohu.trap.treetools.meanalgorithms.RandomPPAMean;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static eu.vbrlohu.trap.iotools.Utils.printTree;
import static eu.vbrlohu.trap.iotools.Utils.writeLog;
import static eu.vbrlohu.trap.iotools.Utils.writeToFileNewick;





public final class TrAP {


    // Here is where you should set up parameters
    //*********************************************

    static String pathToInput = "";
    static String pathToOutput = "";
    static String pathToLog = "";

    static List<String> metadata;


    static String method = "random";   // choose either "random" or "cyclic"
    static int numOfIteration = 100;  // set number of iterations (for cyclic methods, it is number of cycles

    //*********************************************







    private static String polishTime(double timeInMilis) {
        if (timeInMilis < 60000) {
            return timeInMilis / 1000 + " sec";
        } else {
            return timeInMilis / 60000 + " min";
        }

    }






    public static void main(String[] args) throws Exception {

        System.out.println("***********");
        System.out.println("*  START  *");
        System.out.println("***********");

        List<Tree> mytrees;
        try {
            mytrees = Utils.readTreesFromFile(pathToInput);
        } catch (IOException e) {
            throw new Exception("Reading input file failed", e);
        }

        System.out.println();

        if (mytrees == null || mytrees.isEmpty())
        {
            throw new Exception("Trees not uploaded correctly");
        }

        for (Tree tree : mytrees)
        {
            printTree(tree);
        }


        double startTime = System.currentTimeMillis();
        MeanAlg meanAlg = new RandomPPAMean();
        double elapsedTime = System.currentTimeMillis() - startTime;

        Tree resultingTree = meanAlg.computeMean(mytrees, numOfIteration);

        writeToFileNewick(resultingTree, pathToOutput);

        metadata = new ArrayList<>();
        metadata.add("Number of trees in the input set: " + mytrees.size());
        metadata.add("Method: " + method);
        metadata.add("Number of iterations: " + numOfIteration);
        metadata.add("Computation time: " + polishTime(elapsedTime));

        writeLog(metadata, pathToLog);


        System.out.println();
        System.out.println("***********");
        System.out.println("* THE END *");
        System.out.println("***********");

    }// end main
}