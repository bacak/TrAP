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

import eu.vbrlohu.trap.treetools.InnerEdge;
import eu.vbrlohu.trap.treetools.LeafEdge;
import eu.vbrlohu.trap.treetools.Tree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public final class Utils
{

    // print a tree
    public static void printTree(final Tree tree) {
        System.out.println("Leaf Edges:");
        for (LeafEdge leafEdge : tree.getLeafEdges()) {
            System.out.println(leafEdge.getID() + "  " + leafEdge.getLength());
        }
        System.out.println();
        System.out.println("Inner Edges: " + tree.getInnerEdges().size());
        System.out.println();

        for (InnerEdge innerEdge : tree.getInnerEdges()) {
            System.out.print("length:  " + innerEdge.getLength());
            System.out.println();
            System.out.print("split:  ");
            for (LeafEdge leafEdge : innerEdge.getSplit()) {
                System.out.print(leafEdge.getID() + " ");
            }
            System.out.println();
            System.out.print("cosplit:  ");
            for (LeafEdge leafEdge : innerEdge.getCosplit()) {
                System.out.print(leafEdge.getID() + " ");
            }
            System.out.println();
            System.out.println();
        }
    }


    // write a tree to a file in the Newick format
    public static void writeToFileNewick(final Tree tree, final String pathToOutput) throws IOException
    {
        File file = new File(pathToOutput);

        String treeInString = tree.toString();

        try (Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {

            output.write(treeInString);

        } catch (IOException e) {
            throw new IOException("Writing tree " + tree + "in Newick format into file " +file+" failed.", e);
        }
    }


    // write a tree to a file
    public static void writeToFile(final Tree tree, final String pathToOutput) throws IOException
    {

        File file = new File(pathToOutput);

        try (Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {

            for (LeafEdge leafEdge : tree.getLeafEdges()) {
                output.write(leafEdge.getID() + " " + leafEdge.getLength() + '\n');
            }
            int innerID = tree.getLeafEdges().size();
            for (InnerEdge innerEdge : tree.getInnerEdges()) {
                output.write(innerID + " " + innerEdge.getLength());
                for (LeafEdge leafEdge : innerEdge.getSplit()) {
                    output.write(" " + leafEdge.getID());
                }
                output.write('\n');
                ++innerID;
            }

        } catch (IOException e) {
            throw new IOException("Writing tree " + tree + "in Newick format into file " +file+" failed.", e);
        }
    }

    public static void writeLog(final List<String> metadata, final String pathToLog) throws IOException {

        File logfile = new File(pathToLog);

        try (Writer output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile), "UTF-8"))) {

            for (String metadatum : metadata)
            {
                output.write(metadatum);
                output.write("\n");
            }

        } catch (IOException e) {
            throw new IOException("Writing into log file " + pathToLog + " failed.", e);
        }

    }

    // read file with trees, each in the Newick format
    public static List<Tree> readTreesFromFile(final String pathToInput) throws IOException {

        final List<Tree> trees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathToInput), "UTF-8"))) {
            String line;

            int lineCount=0;
            while ((line = br.readLine()) != null) {
                ++lineCount;
                try
                {trees.add(new Tree(line));
                } catch (IllegalArgumentException ex)
                {
                    throw new IllegalArgumentException("Reading line " + lineCount + "of the file " + pathToInput + "failed.", ex);
                }
            }

        } catch (IOException e) {
            throw new IOException("Cannot read input file", e);
        }

        return trees;
    }


    // finds the smallest index in a string 'string' after i, which contains a character from a string 'characters', if
    // does not contains any, returns string.length
    public static int nextIndexOf(String string, String characters, int index) {
        int retInd = string.length();
        int auxIndex = -1;

        for (int i = 0; i < characters.length(); i++) {
            auxIndex = string.substring(index + 1).indexOf(characters.charAt(i));
            if ((auxIndex > -1) && (auxIndex + index + 1 < retInd)) {
                retInd = auxIndex + index + 1;
            }
        }
        return retInd;
    }
}
