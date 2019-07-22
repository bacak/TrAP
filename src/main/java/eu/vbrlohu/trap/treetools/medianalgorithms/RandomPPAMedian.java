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
import java.util.Random;

import static eu.vbrlohu.trap.treetools.Tree.convexCombination;
import static eu.vbrlohu.trap.treetools.Tree.totalDist;

/**
 * Computes median of a given set of trees, uniform distribution (all weights are 1/N). Based on the Proximal point algorithm. Random order of the trees.
 */
public class RandomPPAMedian implements MedianAlg {

    @Override
    public Tree computeMedian(List<Tree> trees, int numberOfIterations) {
        Tree solution = new Tree(trees.get(0));
        Random generator = new Random();

        for (int i = 1; i <= numberOfIterations; ++i) {

            int randomIndex = generator.nextInt(trees.size());
            double iAsDouble = (double) i;
            double coef = 0;
            double auxDist = totalDist(solution, trees.get(randomIndex));
            if (auxDist <= 0) {
                coef = 1;
            } else {
                coef = Math.min(1, 1 / (iAsDouble * auxDist));
            }
            solution = convexCombination(solution, trees.get(randomIndex), coef);
        }
        return solution;
    }
}