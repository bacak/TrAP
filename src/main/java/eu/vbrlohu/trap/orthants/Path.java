package eu.vbrlohu.trap.orthants;


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


import eu.vbrlohu.trap.treetools.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class Path {

	private List<Graph> sequence = new ArrayList<>();
	private Tree origin, head;

	// constructor, creates a path with one element in its Path.sequence
	public Path(Tree s, Tree t) {
		sequence.add(new Graph(s, t));
		origin = s;
		head = t;

	}

	// getters/setters
	public List<Graph> getSequence() {
		return Collections.unmodifiableList(sequence);
	}

	public Tree getOrigin() {
		return origin;
	}

	public Tree getHead() {
		return head;
	}

	// method replacing a graph in the sequence by its two subgraphs obtained by the solution to the extension problem
	private boolean replace(final int i, final Graph graph1, final Graph graph2) {
		if ((sequence.get(i) == graph1) || (sequence.get(i) == graph2)) {
			return false;
		}
		sequence.set(i, graph1);
		sequence.add(i+1, graph2);
		return true;
	}

	// check whether there is a nontrivial a solution to the extension problem, if so, modifies the sequence of orthants
	public boolean extensionProblem(int index) {

		Graph graph = sequence.get(index);

		if (graph.getVerticesL().size() < 2 || graph.getVerticesR().size() < 2) {
			//System.out.println("extension problem called for a small number of vertices ");
			//System.out.println("we have "+this.getSequence().get(index).getVerticesL().size() + " verticesL, and " + this.getSequence().get(index).getVerticesR().size() + " verticesR");
			//System.out.println("ratio of this graph is: "+this.getSequence().get(index).getRatio());
			return false;
		}

		if (!sequence.get(index).maxFlow()) {
			//System.out.println("could not find nontrivial min cut ");
			return false;
		}

		List<VertexL> c1 = new ArrayList<>();
		List<VertexL> c2 = new ArrayList<>();
		List<VertexR> d1 = new ArrayList<>();
		List<VertexR> d2 = new ArrayList<>();

		//System.out.println("we have "+this.getSequence().get(index).getVerticesL().size() + " verticesL, and " + this.getSequence().get(index).getVerticesR().size() + " verticesR");

		// set c1,c2,d1,d2

		for (VertexL vertexL : graph.getCoverL())
		{
			c1.add(new VertexL(vertexL));
		}

		for (VertexR vertexR : graph.getCoverR()) {
			d2.add(new VertexR(vertexR));
		}

		for (VertexL vertexL : graph.getVerticesL()) {
			int aux = 0;
			for (VertexL aC1 : c1) {
				if (aC1.getID() == vertexL.getID()) {
					++aux;
				}
			}
			if (aux == 0) {
				c2.add(new VertexL(vertexL));
			}

		}

		for (VertexR vertexR : graph.getVerticesR()) {
			int aux = 0;
			for (VertexR aD2 : d2) {
				if (aD2.getID() == vertexR.getID()) {
					++aux;
				}
			}
			if (aux == 0) {
				d1.add(new VertexR(vertexR));
			}
		}
        /*
		System.out.println("sizes of c1, c2, d1, d2:");
		System.out.println(c1.size());
		System.out.println(c2.size());
		System.out.println(d1.size());
		System.out.println(d2.size());
		*/


		// computes the incidence matrix for the graph c1,d1, and then for the graph c2,d2

		boolean[][] auxMatrix1 = new boolean[c1.size()][d1.size()];
		boolean[][] auxMatrix2 = new boolean[c2.size()][d2.size()];

		for (int i = 0; i < c1.size(); i++) {
			for (int j = 0; j < d1.size(); j++) {
				auxMatrix1[i][j] = graph.getMatrix()[graph.indexLofID(c1.get(i).getID())][graph.indexRofID(d1.get(j).getID())];
			}
		}

		for (int i = 0; i < c2.size(); i++) {
			for (int j = 0; j < d2.size(); j++) {
				auxMatrix2[i][j] = graph.getMatrix()[graph.indexLofID(c2.get(i).getID())][graph.indexRofID(d2.get(j).getID())];
			}
		}

		// creates two subgraphs

		Graph auxGraph1 = new Graph(c1, d1, auxMatrix1);
		Graph auxGraph2 = new Graph(c2, d2, auxMatrix2);


		// check whether the ratio sequence is still nondecreasing if we added new graphs
		if (index > 0) {
			if (sequence.get(index-1).getRatio() > auxGraph1.getRatio()) {
				//System.out.println("sequence cannot be extended");
				//System.exit(0);
				return false;
			}
		}
		if (index < sequence.size()-1) {
			if (getSequence().get(index+1).getRatio() < auxGraph2.getRatio()) {
				//System.out.println("sequence cannot be extended");
				//System.exit(0);
				return false;
			}
		}
		if (auxGraph1.getRatio() > auxGraph2.getRatio()) {
			//System.out.println("sequence cannot be extended");
			//System.exit(0);
			return false;
		}

		// if not, in the sequence of orthants (e.i. Path) replaces the current graph by these two subgraphs

		// change weights of all vertices from c1,c2,d1,d2

		double auxWeightC1 = 0, auxWeightC2 = 0, auxWeightD1 = 0, auxWeightD2 = 0;

		for (Vertex vertexL : auxGraph1.getVerticesL()) {
			auxWeightC1 += vertexL.getOriginalWeight();
		}
		for (Vertex vertexL : auxGraph1.getVerticesL()) {
			vertexL.setWeight(vertexL.getOriginalWeight() / auxWeightC1);
			vertexL.setResidualCapacity(vertexL.getWeight());
		}

		for (Vertex vertexL : auxGraph2.getVerticesL()) {
			auxWeightC2 += vertexL.getOriginalWeight();
		}
		for (Vertex vertexL : auxGraph2.getVerticesL()) {
			vertexL.setWeight(vertexL.getOriginalWeight() / auxWeightC2);
			vertexL.setResidualCapacity(vertexL.getWeight());
		}

		for (Vertex vertexR : auxGraph1.getVerticesR()) {
			auxWeightD1 += vertexR.getOriginalWeight();
		}
		for (Vertex vertexR : auxGraph1.getVerticesR()) {
			vertexR.setWeight(vertexR.getOriginalWeight() / auxWeightD1);
			vertexR.setResidualCapacity(vertexR.getWeight());
		}

		for (Vertex vertexR : auxGraph2.getVerticesR()) {
			auxWeightD2 += vertexR.getOriginalWeight();
		}
		for (Vertex vertexR : auxGraph2.getVerticesR()) {
			vertexR.setWeight(vertexR.getOriginalWeight() / auxWeightD2);
			vertexR.setResidualCapacity(vertexR.getWeight());
		}

		replace(index, auxGraph1, auxGraph2);
		//System.out.println("size of sequence after extending: " + this.getSequence().size());

		return true;
	}
}