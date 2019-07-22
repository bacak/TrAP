package eu.vbrlohu.trap.treetools.meanalgorithms;

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


import eu.vbrlohu.trap.treetools.MeanAlg;
import eu.vbrlohu.trap.treetools.Tree;

import java.util.List;

import static eu.vbrlohu.trap.treetools.Tree.convexCombination;

/**
 * Computes mean of a given set of trees, with uniform distribution. Based on the Proximal point algorithm. Cyclic order of the trees.
 */
public class CyclicPPAMean implements MeanAlg {
    @Override
    public Tree computeMean(List<Tree> trees, int numberOfIterations) {

        Tree solution = new Tree(trees.get(0));

        for (int i = 1; i <= numberOfIterations; ++i) {
            double iAsDouble = (double) i;
            double coef = 2 / (iAsDouble + 2);
            for (Tree tree : trees) {
                solution = convexCombination(solution, tree, coef);
            }
        }
        return solution;

    }
}