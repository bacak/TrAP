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

import static eu.vbrlohu.trap.treetools.Tree.areCompatible;

public final class Graph {

	public static final double ZERO = 0.0001;
	private List<VertexL> verticesL = new ArrayList<>();
	private List<VertexR> verticesR = new ArrayList<>();
	private boolean[][] matrix;
	private List<VertexL> coverL = new ArrayList<>();
	private List<VertexR> coverR = new ArrayList<>();
	private int numberOfVertices = 2; // this number is used in the relabel-flow algorithm only, is equal to #verticesL + #verticesR + 2 (this '2' is for the source and sink)
	private double ratio = 0;         // this is used in Path.sequence where it forms a sequence of ratios
	private double numerator = 0;    // the numerator of the 'ratio'
	private double denominator = 0;  // the denominator of the 'ratio'

	// constructor
	public Graph(Tree s, Tree t) {

		for (int i = 0; i < s.getInnerEdges().size(); i++) {
			double ow = Math.pow(s.getInnerEdges().get(i).getLength(), 2);
			double w = ow / s.norm2OfInnerEdges();
			verticesL.add(new VertexL(i, ow, w));
		}

		for (int i = 0; i < t.getInnerEdges().size(); i++) {
			double ow = Math.pow(t.getInnerEdges().get(i).getLength(), 2);
			double w = ow / t.norm2OfInnerEdges();
			verticesR.add(new VertexR(i, ow, w));
		}

		matrix = new boolean[verticesL.size()][verticesR.size()];

		for (int i = 0; i < s.getInnerEdges().size(); i++) {
			for (int j = 0; j < t.getInnerEdges().size(); j++) {
				if (areCompatible(s.getInnerEdges().get(i), t.getInnerEdges().get(j))) {
					matrix[i][j] = false;
				} else {
					matrix[i][j] = true;
					verticesL.get(i).getArcs().add(new Arc(verticesL.get(i), verticesR.get(j)));
					verticesR.get(j).getReverseArcs().add(verticesL.get(i).getArcs().get(verticesL.get(i).getArcs().size()-1));

				}
			}
		}

		numerator = Math.sqrt(s.norm2OfInnerEdges());
		denominator = Math.sqrt(t.norm2OfInnerEdges());
		ratio = numerator / denominator;
		numberOfVertices = verticesL.size() + verticesR.size() + 2;
		coverL = new ArrayList<>();
		coverR = new ArrayList<>();

	}

	// constructor
	public Graph(List<VertexL> verticesL, List<VertexR> verticesR, boolean[][] matrix) {
		this.verticesL = new ArrayList<>();
		setVerticesL(verticesL);
		this.verticesR = new ArrayList<>();
		setVerticesR(verticesR);

		this.matrix = new boolean[verticesL.size()][verticesR.size()];
		for (int i = 0; i < this.verticesL.size(); i++) {
			System.arraycopy(matrix[i], 0, this.matrix[i], 0, verticesR.size());
		}

		for (int i = 0; i < this.verticesL.size(); i++) {
			for (int j = 0; j < this.verticesR.size(); j++) {
				if (this.matrix[i][j]) {
					this.verticesL.get(i).getArcs().add(new Arc(this.verticesL.get(i), this.verticesR.get(j)));
					this.verticesR.get(j).getReverseArcs().add(this.verticesL.get(i).getArcs().get(this.verticesL.get(i).getArcs().size() - 1));
				}
			}
		}


		// compute ratio
		double auxDouble1 = 0;
		for (VertexL aVerticesL : this.verticesL) {
			auxDouble1 += aVerticesL.getOriginalWeight();
		}
		double auxDouble2 = 0;
		for (VertexR aVerticesR : this.verticesR) {
			auxDouble2 += aVerticesR.getOriginalWeight();
		}
		auxDouble1 = Math.sqrt(auxDouble1);
		auxDouble2 = Math.sqrt(auxDouble2);
		setNumerator(auxDouble1);
		setDenominator(auxDouble2);
		setRatio(auxDouble1 / auxDouble2);

		numberOfVertices = this.verticesL.size() + this.verticesR.size() + 2;
		coverL = new ArrayList<>();
		coverR = new ArrayList<>();
	}

	// getters/setters
	public List<VertexL> getVerticesL() {
		return Collections.unmodifiableList(verticesL);
	}

	private void setVerticesL(List<VertexL> verticesL) {
		this.verticesL.clear();
		this.verticesL.addAll(verticesL);
	}

	public List<VertexR> getVerticesR() {
		return Collections.unmodifiableList(verticesR);
	}

	private void setVerticesR(List<VertexR> verticesR) {
		this.verticesR.clear();
		this.verticesR.addAll(verticesR);
	}

	public boolean[][] getMatrix() {
		return matrix;
	}

	public List<VertexL> getCoverL() {
		return Collections.unmodifiableList(coverL);
	}

	public void setCoverL(List<VertexL> verticesL) {
		coverL.clear();
		coverL.addAll(verticesL);
	}

	public List<VertexR> getCoverR() {
		return Collections.unmodifiableList(coverR);
	}

	public void setCoverR(List<VertexR> verticesR) {
		coverR.clear();
		coverR.addAll(verticesR);
	}

	private int getNumberOfVertices() {
		return numberOfVertices;
	}

	private void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getRatio() {
		return ratio;
	}

	private void setNumerator(double numerator) {
		this.numerator = numerator;
	}

	public double getNumerator() {
		return numerator;
	}

	private void setDenominator(double denominator) {
		this.denominator = denominator;
	}

	public double getDenominator() {
		return denominator;
	}

	public void print() {
		System.out.println("");
		System.out.println("");
		System.out.println("Bipartite graph: ");
		System.out.println("");
		System.out.println("Number of verticesL: " + verticesL.size());
		for (VertexL vertexL : verticesL) {
			System.out.println("vertexL of ID " + vertexL.getID() + " has weight: " + vertexL.getWeight() + " and excess: " + vertexL.getExcess());
			System.out.println("    " + " height: " + vertexL.getHeight());
			System.out.println("    " + " flow: " + vertexL.getFlow());
			System.out.println("    " + " residual capacity: " + vertexL.getResidualCapacity());
			System.out.println("    " + " number of arcs: " + vertexL.getArcs().size());
			for (int j = 0; j < vertexL.getArcs().size(); j++) {
				System.out.println("    " + "arcs: " + j + ":");
				System.out.println("             " + " origin: " + vertexL.getArcs().get(j).getOrigin().getID());
				System.out.println("             " + " head: " + vertexL.getArcs().get(j).getHead().getID());
				System.out.println("             " + " flow: " + vertexL.getArcs().get(j).getFlow());

			}
		}
		System.out.println("");
		System.out.println("Number of verticesR: " + verticesR.size());
		for (VertexR vertexR : verticesR) {
			System.out.println("vertexR of ID " + vertexR.getID() + " has weight: " + vertexR.getWeight() + " and excess: " + vertexR.getExcess());
			System.out.println("    " + " height: " + vertexR.getHeight());
			System.out.println("    " + " flow: " + vertexR.getFlow());

			System.out.println("    " + " residual capacity: " + vertexR.getResidualCapacity());
			System.out.println("    " + " number of reverse arcs: " + vertexR.getReverseArcs().size());
			for (int j = 0; j < vertexR.getReverseArcs().size(); j++) {
				System.out.println("    " + " reverse arcs: " + j + ":");
				System.out.println("             " + " origin: " + vertexR.getReverseArcs().get(j).getOrigin().getID());
				System.out.println("             " + " head: " + vertexR.getReverseArcs().get(j).getHead().getID());
				System.out.println("             " + " flow: " + vertexR.getReverseArcs().get(j).getFlow());

			}
		}
		System.out.println("");
		System.out.println("Number of coverL: " + coverL.size());
		System.out.println("");
		System.out.println("Number of coverR: " + coverR.size());
		System.out.println("");
		System.out.println("Numerator: " + numerator);
		System.out.println("Denominator: " + denominator);
		System.out.println("Ratio: " + ratio);
		System.out.println(" ");
	}

	private VertexL vertexLofID(int index) {
		for (VertexL vertexL : verticesL) {
			if (vertexL.getID() == index) {
				return vertexL;
			}
		}
		return null;
	}

	private VertexR vertexRofID(int index) {
		for (VertexR vertexR : verticesR) {
			if (vertexR.getID() == index) {
				return vertexR;
			}
		}
		return null;
	}

	public int indexLofID(int index) {

		int size = verticesL.size();

		if (index<0 || index >= size)
		{
			throw new IndexOutOfBoundsException("vertexL with index" + index + "does not exist");
		}
		for (int i = 0; i < size; ++i) {
			if (verticesL.get(i).getID() == index) {
				return i;
			}
		}
		return -1;
	}


	public int indexRofID(int index) {

		int size = verticesR.size();

		if (index<0 || index >= size)
		{
			throw new IndexOutOfBoundsException("vertexR with index" + index + "does not exist");
		}
		for (int i = 0; i < size; i++) {
			if (verticesR.get(i).getID() == index) {
				return i;
			}
		}
		return -1;
	}

	private boolean isReverseEligible(VertexL vertexL) {
		//System.out.println("vertexL of ID: " + vertexL.getID() + " is eligible: ");
		//System.out.println("true ");
		//System.out.println("false ");
		return vertexL.getHeight() > getNumberOfVertices() && vertexL.getExcess() > Graph.ZERO && vertexL.getFlow() > Graph.ZERO;
	}

	private boolean isEligible(VertexR vertexR) {
		return (vertexR.getHeight() > 0 && vertexR.getExcess() > Graph.ZERO && vertexR.getResidualCapacity() > Graph.ZERO);
	}

	// process a given VertexL, that is, pushes flow out of this vertex to decrease its excess
	private void processVertex(VertexL vertexL) {
		//System.out.println("processing vertexL of ID: " + vertexL.getID());
		if (isReverseEligible(vertexL)) {
			vertexL.reversePush();
		}
		for (Arc arc : vertexL.getArcs()) {
			if (arc.isEligible()) {
				arc.push();
				break;
			}
		}
		if (vertexL.getExcess() > Graph.ZERO) {
			vertexL.increaseHeight();
		}
	}

	// the same as above, for a VertexR
	private void processVertex(VertexR vertexR) {
		//System.out.println("processing vertexR of ID: " + vertexR.getID());

		if (this.isEligible(vertexR)) {
			vertexR.push();
		}

		for (Arc reverseArc : vertexR.getReverseArcs()) {
			if (reverseArc.isReverseEligible()) {
				reverseArc.reversePush();
				if (vertexR.getExcess() <= Graph.ZERO) {
					break;
				}
			}
		}
		if (vertexR.getExcess() > Graph.ZERO) {
			vertexR.increaseHeight();
		}
	}

	// finds a vertex with maximal height among all vertices with positive excess  .. each selection takes O(n) where n = #vertices ... TODO use a double linked list to get a constant time operation
	private int selectVertex() {
		Vertex auxVertex = new Vertex();
		int auxHeight1 = -1;
		for (Vertex vertexL : verticesL) {
			if (vertexL.getExcess() > Graph.ZERO) {
				if (vertexL.getHeight() > auxHeight1) {
					auxVertex = vertexL;
					auxHeight1 = auxVertex.getHeight();
				}
			}
		}

		int auxHeight2 = auxHeight1;
		for (Vertex vertexR : verticesR) {
			if (vertexR.getExcess() > Graph.ZERO) {
				if (vertexR.getHeight() > auxHeight2) {
					auxVertex = vertexR;
					auxHeight2 = auxVertex.getHeight();
				}
			}
		}
		// we need ID, but return either ID (if selected vertex in VerticesR) or -ID (if selected vertex is in VerticesR)
		int auxInt = auxVertex.getID() + 1;  // just a technicality to avoid ambiguity with 0
		if (auxHeight1 < auxHeight2) {
			return auxInt;
		} else {
			return (-auxInt);
		}
	}

	// check whether there is a vertex with excess > 0
	private boolean existsActiveVertex() {
		for (Vertex vertexL : verticesL) {
			if (vertexL.getExcess() > Graph.ZERO) {
				return true;
			}
		}

		for (Vertex vertexR : verticesR)
		{
			if (vertexR.getExcess() > Graph.ZERO)
			{
				return true;
			}
		}

		return false;
	}

	// solves the min-cut max-flow problem and sets nontrivial coverL and coverR .. uses Goldberg's push-relabel algorithm
	public boolean maxFlow() {

		// initialization

		for (VertexR vertexR : verticesR) {
			vertexR.setHeight(0);
			vertexR.setFlow(0);
			vertexR.setExcess(0);
			vertexR.setResidualCapacity(vertexR.getWeight());
		}
		for (VertexL vertexL : verticesL) {
			vertexL.setHeight(0);
			vertexL.setExcess(vertexL.getWeight());
			vertexL.setFlow(vertexL.getWeight());
			vertexL.setResidualCapacity(0);
		}

		// initialization ends here



		// find a vertex with excess > 0 and process it, repeat until there is no such vertex

		int counter = 0;
		while (existsActiveVertex()) {

			int auxInt;
			int vertexID = selectVertex();

			if (selectVertex() < 0) {

				auxInt = -vertexID - 1;
				//System.out.println("heightL: " + this.vertexLofID(auxInt).getHeight());
				//System.out.println("excessL: " + this.vertexLofID(auxInt).getExcess());
				processVertex(vertexLofID(auxInt));
			} else {

				auxInt = vertexID - 1;

				//System.out.println("heightR: " + this.vertexRofID(auxInt).getHeight());
				//System.out.println("excessR: " + this.vertexRofID(auxInt).getExcess());

				processVertex(vertexRofID(auxInt));

			}
			++counter;
			//System.out.println("an active vertex exists, counter: " + counter);

		}

		// extract the minimal s-t cut
		int thresholdHeight = 1;
		boolean auxCond = true;
		coverL.clear();
		coverR.clear();

		// find a threshold value of height, which later determines min cut
		while (auxCond) {
			blockForThreshold:
			{
				for (Vertex vertexL : verticesL) {
					if (vertexL.getHeight() == thresholdHeight) {
						thresholdHeight++;
						break blockForThreshold;
					}
				}
				for (Vertex vertexR : verticesR) {
					if (vertexR.getHeight() == thresholdHeight) {
						thresholdHeight++;
						break blockForThreshold;
					}
				}
				auxCond = false;
			}
		}

		for (VertexL vertexL : verticesL) {
			if (vertexL.getHeight() < thresholdHeight) {
				coverL.add(new VertexL(vertexL));
			}
		}

		for (VertexR vertexR : verticesR) {
			if (vertexR.getHeight() > thresholdHeight) {
				coverR.add(new VertexR(vertexR));
			}
		}

		return !(coverL.size() == verticesL.size() || coverL.isEmpty() || coverR.isEmpty() || coverR.size() == verticesR.size());
	}
}