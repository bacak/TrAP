/** This file is part of MeMe, program for computing medians and means of phylogenetic trees.
    Copyright (C) 2013 Miroslav Bacak, Vojtech Juranek

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */


package org.adaptivesampling.phylotree.orthants;

import java.util.ArrayList;

import org.adaptivesampling.phylotree.geodesic.Tree;

public class Graph {
	
    public static final double ZERO = 0.0001;
	private ArrayList<VertexL> verticesL = new ArrayList<VertexL>();
	private ArrayList<VertexR> verticesR = new ArrayList<VertexR>();
	private boolean[][] matrix;
	private ArrayList<VertexL> coverL = new ArrayList<VertexL>();
	private ArrayList<VertexR> coverR = new ArrayList<VertexR>();
	private int numberOfVertices =2; // this number is used in the relabel-flow algorithm only, is equal to #verticesL + #verticesR + 2 (this '2' is for the source and sink)
	private double ratio =0;  // this is used in Path.sequence where it forms a sequence of ratios
	private double numerator = 0;    // the numerator of the 'ratio'
	private double denominator = 0;  // the denominator of the 'ratio'
	
	// constructor
	public Graph(Tree s, Tree t) {
	
		for (int i = 0; i < s.getInnerEdges().size(); i++) {
			double ow =  s.getInnerEdges().get(i).getLength() * s.getInnerEdges().get(i).getLength();
			double w = ow/s.norm2OfInnerEdges();
			this.verticesL.add(new VertexL(i,ow,w));
		}
		
		for (int i = 0; i < t.getInnerEdges().size(); i++) {
			double ow = t.getInnerEdges().get(i).getLength() * t.getInnerEdges().get(i).getLength();
			double w = ow/t.norm2OfInnerEdges();
			this.verticesR.add(new VertexR(i,ow,w));
		}
		
		this.matrix =  new boolean[verticesL.size()][verticesR.size()];
		
		for (int i = 0; i < s.getInnerEdges().size(); i++) {
			for (int j= 0; j < t.getInnerEdges().size(); j++) {
				if (Tree.areCompatible(s.getInnerEdges().get(i),t.getInnerEdges().get(j))) {
					this.matrix[i][j] = false;
				}
				else {
					this.matrix[i][j] = true;
					this.verticesL.get(i).getArcs().add(new Arc(this.verticesL.get(i),this.verticesR.get(j)));
					this.verticesR.get(j).getReverseArcs().add(this.verticesL.get(i).getArcs().get(this.verticesL.get(i).getArcs().size()-1));
					
				}
			}
		}
		
		this.numerator = Math.sqrt(s.norm2OfInnerEdges() );
		this.denominator = Math.sqrt(t.norm2OfInnerEdges());
		this.ratio = this.numerator/this.denominator;
		this.numberOfVertices = this.verticesL.size() + this.verticesR.size() + 2;
		this.coverL = new ArrayList<VertexL>();
		this.coverR = new ArrayList<VertexR>();
		
	}
	
	// constructor
	public Graph(ArrayList<VertexL> verticesL, ArrayList<VertexR> verticesR, boolean[][] matrix) {
		this.verticesL = new ArrayList<VertexL>();
        this.setVerticesL(verticesL);
		this.verticesR = new ArrayList<VertexR>();
		this.setVerticesR(verticesR);
		
		this.matrix = new boolean[verticesL.size()][verticesR.size()];
		for (int i=0; i < this.verticesL.size(); i++) {
			for (int j=0 ; j < this.verticesR.size(); j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}	
		
		for (int i=0; i < this.verticesL.size(); i++) {
			for (int j=0 ; j < this.verticesR.size(); j++) {
				if (this.matrix[i][j]) {
					this.verticesL.get(i).getArcs().add(new Arc(this.verticesL.get(i),this.verticesR.get(j)));
					this.verticesR.get(j).getReverseArcs().add(this.verticesL.get(i).getArcs().get(this.verticesL.get(i).getArcs().size()-1));
				}
			}
		}
		
		
		// compute ratio
		double auxDouble1 =0;
		for (int i=0; i < this.verticesL.size(); i++) {
			auxDouble1 += this.verticesL.get(i).getOriginalWeight();
		}
		double auxDouble2 =0;
		for (int i=0; i < this.verticesR.size(); i++) {
			auxDouble2 += this.verticesR.get(i).getOriginalWeight();
		}
		auxDouble1 = Math.sqrt(auxDouble1);
		auxDouble2 = Math.sqrt(auxDouble2);
		this.setNumerator(auxDouble1);
		this.setDenominator(auxDouble2);
		this.setRatio(auxDouble1/auxDouble2);
		
		this.numberOfVertices = this.verticesL.size() + this.verticesR.size() + 2;
		this.coverL = new ArrayList<VertexL>();
		this.coverR = new ArrayList<VertexR>();
	}

	// getters/setters
	public ArrayList<VertexL> getVerticesL() {
		return this.verticesL;
	}
	public void setVerticesL(ArrayList<VertexL> verticesL) {
		this.verticesL.clear();
		for (int i=0; i < verticesL.size(); i++) {
			this.verticesL.add(new VertexL(verticesL.get(i)));
		}
	}
	public ArrayList<VertexR> getVerticesR() {
		return this.verticesR;
	}
	public void setVerticesR(ArrayList<VertexR> verticesR) {
		this.verticesR.clear();
		for (int i=0; i < verticesR.size(); i++) {
			this.verticesR.add(new VertexR(verticesR.get(i)));
		}
	}
	
	public boolean[][] getMatrix() {
		return this.matrix;
	}
	public ArrayList<VertexL> getCoverL() {
		return this.coverL;
	}
	public void setCoverL(ArrayList<VertexL> coverL) {
		this.coverL.clear();
		for (int i=0; i<coverL.size(); i++) {
			this.coverL.add(coverL.get(i));
		}
	}
	public ArrayList<VertexR> getCoverR() {
		return this.coverR;
	}
	public void setCoverR(ArrayList<VertexR> coverR) {
		this.coverR.clear();
		for (int i=0; i<coverR.size(); i++) {
			this.coverR.add(coverR.get(i));
		}
	}
	
	public int getNumberOfVertices() {
		return this.numberOfVertices;
	}
	
	public void setRatio(double ratio) {
		this.ratio =ratio;
	}
	
	public double getRatio() {
		return this.ratio;
	}
	
	public void setNumerator(double numerator) {
		this.numerator = numerator;
	}
	
	public double getNumerator() {
		return this.numerator;
	}
	
	public void setDenominator(double denominator) {
		this.denominator = denominator;
	}
	
	public double getDenominator() {
		return this.denominator;
	}
	
	public void print() {
		System.out.println("");
		System.out.println("");
		System.out.println("Bipartite graph: ");
		System.out.println("");
		System.out.println("Number of verticesL: " + this.getVerticesL().size());
		for (int i=0; i< this.getVerticesL().size(); i++) {
			System.out.println("vertexL of ID " + this.getVerticesL().get(i).getID() + " has weight: " + this.getVerticesL().get(i).getWeight() + " and excess: " + this.getVerticesL().get(i).getExcess());
			System.out.println("    " + " height: " + this.getVerticesL().get(i).getHeight()); 
			System.out.println("    " + " flow: " + this.getVerticesL().get(i).getFlow());
		    System.out.println("    " + " residual capacity: " + this.getVerticesL().get(i).getResidualCapacity()); 
		    System.out.println("    " + " number of arcs: " + this.getVerticesL().get(i).getArcs().size()); 
			for (int j=0; j<this.getVerticesL().get(i).getArcs().size(); j++) {
				System.out.println("    " + "arcs: " + j + ":");
				System.out.println("             " + " origin: " + this.getVerticesL().get(i).getArcs().get(j).getOrigin().getID());
				System.out.println("             " + " head: " + this.getVerticesL().get(i).getArcs().get(j).getHead().getID());
				System.out.println("             " + " flow: " + this.getVerticesL().get(i).getArcs().get(j).getFlow());
				
			}
		}
		System.out.println("");
		System.out.println("Number of verticesR: " + this.getVerticesR().size());
		for (int i=0; i< this.getVerticesR().size(); i++) {
			System.out.println("vertexR of ID " + this.getVerticesR().get(i).getID() + " has weight: " + this.getVerticesR().get(i).getWeight() + " and excess: " + this.getVerticesR().get(i).getExcess());
			System.out.println("    " + " height: " + this.getVerticesR().get(i).getHeight());
			System.out.println("    " + " flow: " + this.getVerticesR().get(i).getFlow()); 
			
			System.out.println("    " + " residual capacity: " + this.getVerticesR().get(i).getResidualCapacity()); 
			System.out.println("    " + " number of reverse arcs: " + this.getVerticesR().get(i).getReverseArcs().size()); 
			for (int j=0; j<this.getVerticesR().get(i).getReverseArcs().size(); j++) {
				System.out.println("    " + " reverse arcs: " + j + ":");
				System.out.println("             " + " origin: " + this.getVerticesR().get(i).getReverseArcs().get(j).getOrigin().getID());
				System.out.println("             " + " head: " + this.getVerticesR().get(i).getReverseArcs().get(j).getHead().getID());
				System.out.println("             " + " flow: " + this.getVerticesR().get(i).getReverseArcs().get(j).getFlow());
				
			}
		}	
		System.out.println("");
		System.out.println("Number of coverL: " + this.getCoverL().size());
		System.out.println("");
		System.out.println("Number of coverR: " + this.getCoverR().size());
		System.out.println("");
		System.out.println("Numerator: " + this.getNumerator());
		System.out.println("Denominator: " + this.getDenominator());
		System.out.println("Ratio: " + this.getRatio());
		System.out.println(" ");
	}
	
	public VertexL vertexLofID(int index) {
		for (int i=0;i<this.getVerticesL().size(); i++) {
			if (this.getVerticesL().get(i).getID() == index) {
				return this.getVerticesL().get(i);
			}
		}
		return null;
	}
	
	public VertexR vertexRofID(int index) {
		for (int i=0;i<this.getVerticesR().size(); i++) {
			if (this.getVerticesR().get(i).getID() == index) {
				
				
				return this.getVerticesR().get(i);
				
			}
		}
		return null;
	}
	
	public int indexLofID(int index) {
		for (int i=0;i<this.getVerticesL().size(); i++) {
			if (this.getVerticesL().get(i).getID() == index) {
				return i;
			}
		}
		return -1;
	}
	

	public int indexRofID(int index) {
		for (int i=0;i<this.getVerticesR().size(); i++) {
			if (this.getVerticesR().get(i).getID() == index) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean isReverseEligible(VertexL vertexL) {
		//System.out.println("vertexL of ID: " + vertexL.getID() + " is eligible: ");
		if (vertexL.getHeight()> this.getNumberOfVertices() && vertexL.getExcess()> Graph.ZERO && vertexL.getFlow() > Graph.ZERO) {
			//System.out.println("true ");
			return true;
		}
		else {
			//System.out.println("false ");
			return false;
		}
	}
	
	public boolean isEligible(VertexR vertexR) {
		//System.out.println("vertexR of ID: " + vertexR.getID() + " is eligible: ");
		if (vertexR.getHeight()> 0 && vertexR.getExcess()> Graph.ZERO && vertexR.getResidualCapacity() > Graph.ZERO) {
			//System.out.println("true ");
			return true;
		}
		else {
			//System.out.println("false ");
			
			return false;
		}
	}	

	// process a given VertexL, that is, pushes flow out of this vertex to decrease its excess
	private void processVertex(VertexL vertexL) {
		//System.out.println("processing vertexL of ID: " + vertexL.getID());
		
		if (this.isReverseEligible(vertexL)) {
        	vertexL.reversePush();
        }
		for (int i= 0; i < vertexL.getArcs().size(); i++) { 
			if (vertexL.getArcs().get(i).isEligible()) {
				vertexL.getArcs().get(i).push();
				break;
			}
		}
		if  (vertexL.getExcess()> Graph.ZERO) {
	      	vertexL.increaseHeight();
		}			
       
		
	}
	
	// the same as above, for a VertexR
	private void processVertex(VertexR vertexR) {
		//System.out.println("processing vertexR of ID: " + vertexR.getID());
		
		if (this.isEligible(vertexR)) {
			
			vertexR.push();
		}
		
		for (int i=0; i < vertexR.getReverseArcs().size(); i++) {
			
			if (vertexR.getReverseArcs().get(i).isReverseEligible()) {
				vertexR.getReverseArcs().get(i).reversePush();
				if (vertexR.getExcess()> Graph.ZERO) {
					continue;
				}
				else {
					break;
				}
			}	
		} 
		if  (vertexR.getExcess()> Graph.ZERO) {
			vertexR.increaseHeight();
		}
	}
	
	// finds a vertex with maximal height among all vertices with positive excess  .. each selection takes O(n) where n = #vertices ... TODO use a double linked list to get a constant time operation 
	public int selectVertex() {
		Vertex auxVertex = new Vertex();
		int auxHeight1 =-1; 
		for (int i=0; i< this.getVerticesL().size(); i++) {
			if (this.getVerticesL().get(i).getExcess()> Graph.ZERO) {
				if (this.getVerticesL().get(i).getHeight() > auxHeight1) {
					auxVertex = this.getVerticesL().get(i);
					auxHeight1 = auxVertex.getHeight();
				}
			}
		}
		
		int auxHeight2 = auxHeight1;
		for (int i=0; i< this.getVerticesR().size(); i++) {
			if (this.getVerticesR().get(i).getExcess()> Graph.ZERO) {
				if (this.getVerticesR().get(i).getHeight() > auxHeight2) {
					auxVertex = this.getVerticesR().get(i);
					auxHeight2 = auxVertex.getHeight();
				}
			}
		}
		// we need ID, but return either ID (if selected vertex in VerticesR) or -ID (if selected vertex is in VerticesR)
		int auxInt = auxVertex.getID()+1;  // just a technicality to avoid ambiguity with 0
		if (auxHeight1 < auxHeight2) {
			return auxInt;
		}
		else {
			return (-auxInt);
		}
	}
	
	// check whether there is a vertex with excess > 0
	public boolean existsActiveVertex() {
		
		for (int i=0; i< this.getVerticesL().size(); i++) {
			if (this.getVerticesL().get(i).getExcess()> Graph.ZERO) {
			    //System.out.println("excess of an active vertexL " + this.getVerticesL().get(i).getID() + " is " + this.getVerticesL().get(i).getExcess());
				return true;
			}
		}
		
		for (int i=0; i< this.getVerticesR().size(); i++) {
			if (this.getVerticesR().get(i).getExcess()> Graph.ZERO) {
				//System.out.println("excess of an active vertexR " + this.getVerticesR().get(i).getID() + " is " + this.getVerticesR().get(i).getExcess());
				
				return true;
			}
		}
	
		return false;
	}
	
	// solves the min-cut max-flow problem and sets nontrivial coverL and coverR .. uses Goldberg's push-relabel algorithm
	public boolean maxFlow() {
		
		
		// initialization

		for (int i =0; i< this.verticesR.size(); i++) {
			this.verticesR.get(i).setHeight(0);
			this.verticesR.get(i).setFlow(0);
			this.verticesR.get(i).setExcess(0);
			this.verticesR.get(i).setResidualCapacity(this.verticesR.get(i).getWeight());
		}
		for (int i =0; i< this.verticesL.size(); i++) {
			this.verticesL.get(i).setHeight(0);
			this.verticesL.get(i).setExcess(this.verticesL.get(i).getWeight());
			this.verticesL.get(i).setFlow(this.verticesL.get(i).getWeight());
			this.verticesL.get(i).setResidualCapacity(0);
		}
		
		// initialization ends here
		
		//this.print();
		
		
		// find a vertex with excess > 0 and process it, repeat until there is no such vertex
		
		int counter =0;
		while (this.existsActiveVertex()) {
			
			int auxInt = 0;
			int vertexID = this.selectVertex();
			//System.out.println(vertexID);
			if (this.selectVertex() < 0) {
				
				auxInt = -vertexID -1;
				//System.out.println("heightL: " + this.vertexLofID(auxInt).getHeight());
				//System.out.println("excessL: " + this.vertexLofID(auxInt).getExcess());
				this.processVertex(this.vertexLofID(auxInt));
				//this.print();
				

			}
			else {
				
				auxInt = vertexID -1;
				
				//System.out.println("heightR: " + this.vertexRofID(auxInt).getHeight());
				//System.out.println("excessR: " + this.vertexRofID(auxInt).getExcess());
				
				this.processVertex(this.vertexRofID(auxInt));
				
				//this.print();
			}
			counter++;
			//System.out.println("an active vertex exists, counter: " + counter);
			
		}
		
		
		
		// extract the minimal s-t cut
		int thresholdHeight = 1 ;
		boolean auxCond = true;
		this.coverL.clear();
		this.coverR.clear();
		
		// find a threshold value of height, which later determines min cut
		while (auxCond) {
			blockForThreshold: {
	          for (int i=0; i < this.getVerticesL().size(); i++) {
					if (this.getVerticesL().get(i).getHeight() == thresholdHeight) {
						thresholdHeight++;
						break blockForThreshold;
					}		
			    }
	            for (int i=0; i < this.getVerticesR().size(); i++) {
					if (this.getVerticesR().get(i).getHeight() == thresholdHeight) {
						thresholdHeight++;
						break blockForThreshold;
					}		
			    }
	            auxCond = false;
		    }
		}
		
		for (int i=0; i < this.getVerticesL().size(); i++) {
			if (this.getVerticesL().get(i).getHeight() < thresholdHeight) {
				this.coverL.add(new VertexL(this.verticesL.get(i)));	
			}
		}
	
		for (int i=0; i < this.getVerticesR().size(); i++) {
			if (this.getVerticesR().get(i).getHeight() > thresholdHeight) {
				this.coverR.add(new VertexR(this.verticesR.get(i)));	
			}
		}
		
		//this.print();
		
		
		if (this.coverL.size() == this.verticesL.size() || this.coverL.isEmpty() || this.coverR.isEmpty() || this.coverR.size() == this.verticesR.size()) {
			//System.out.println("size of coverL "+this.getCoverL().size() + " and size of coverR "+this.getCoverR().size());
			//System.out.println("max flow returns false");
			
			return false;
            
		}
		else {
			//System.out.println("max flow returns true");
			
			return true;
		}
	}
}	
