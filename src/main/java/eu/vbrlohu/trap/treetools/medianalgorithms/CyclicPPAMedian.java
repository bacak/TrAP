package eu.vbrlohu.trap.treetools.medianalgorithms;


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


import eu.vbrlohu.trap.treetools.MedianAlg;
import eu.vbrlohu.trap.treetools.Tree;

import java.util.List;

import static eu.vbrlohu.trap.treetools.Tree.convexCombination;
import static eu.vbrlohu.trap.treetools.Tree.totalDist;

/**
 * Computes median of a given set of trees, uniform distribution (all weights are 1/N). Based on the Proximal point algorithm. Cyclic order of the trees.
 */
public class CyclicPPAMedian implements MedianAlg {

    @Override
    public Tree computeMedian(List<Tree> trees, int numberOfIterations) {
        Tree solution = new Tree(trees.get(0));

        for (int i = 1; i <= numberOfIterations; ++i) {
            double iAsDouble = (double) i;
            for (Tree setOfTree : trees) {
                double auxDist = totalDist(solution, setOfTree);
                double coef = 0;
                if (auxDist <= 0) {
                    coef = 1;
                } else {
                    coef = Math.min(1, 1 / (iAsDouble * auxDist));
                }
                //coef = Math.min(1,1/(i*auxDist));
                solution = convexCombination(solution, setOfTree, coef);
            }
        }
        return solution;
    }

}