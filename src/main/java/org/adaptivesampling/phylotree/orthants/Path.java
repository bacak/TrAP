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

package orthants;

import geodesic.*;
import java.util.*;

public class Path {

    private ArrayList<Graph> sequence = new ArrayList<Graph>();
    private Tree origin,head;

	// constructor, creates a path with one element in its Path.sequence
	public Path(Tree s, Tree t) {
		this.sequence.clear();
		this.sequence.add(new Graph(s,t));
		this.origin =s;
		this.head =t;
		
	}
  
	// getters/setters
	public ArrayList<Graph> getSequence() {
		return this.sequence;
	}
	
	public Tree getOrigin() {
		return this.origin;
	}
	
	public Tree getHead() {
		return this.head;
	}
	
	// method replacing a graph in the sequence by its two subgraphs obtained by the solution to the extension problem
	private boolean replace(int i, Graph graph1, Graph graph2) {
		if ((this.sequence.get(i) == graph1) || (this.sequence.get(i) == graph2)) {
			return false;
		}
		this.sequence.set(i,graph1);
		this.sequence.add(i+1,graph2);
		return true;
	}
	
	// check whether there is a nontrivial a solution to the extension problem, if so, modifies the sequence of orthants
	public boolean extensionProblem(int index) {
		
		if (this.getSequence().get(index).getVerticesL().size()<2 || this.getSequence().get(index).getVerticesR().size()<2) {
			//System.out.println("extension problem called for a small number of vertices ");
			//System.out.println("we have "+this.getSequence().get(index).getVerticesL().size() + " verticesL, and " + this.getSequence().get(index).getVerticesR().size() + " verticesR");
			//System.out.println("ratio of this graph is: "+this.getSequence().get(index).getRatio());
			return false;
		}

		if (this.sequence.get(index).maxFlow() == false) {
			//System.out.println("could not find nontrivial min cut ");
			return false;
		}
		
		
		
		
		ArrayList<VertexL> c1 = new ArrayList<VertexL>();
		ArrayList<VertexL> c2 = new ArrayList<VertexL>();
		ArrayList<VertexR> d1 = new ArrayList<VertexR>();
		ArrayList<VertexR> d2 = new ArrayList<VertexR>();
		
		//System.out.println("we have "+this.getSequence().get(index).getVerticesL().size() + " verticesL, and " + this.getSequence().get(index).getVerticesR().size() + " verticesR");
		
		// set c1,c2,d1,d2
		
		for (int i=0; i<this.sequence.get(index).getCoverL().size(); i++) {
			c1.add(new VertexL(this.sequence.get(index).getCoverL().get(i)));
		}
		for (int i=0; i<this.sequence.get(index).getCoverR().size(); i++) {
			d2.add(new VertexR(this.sequence.get(index).getCoverR().get(i)));
		}
		
		for (int i = 0; i < this.sequence.get(index).getVerticesL().size(); i++) {
			int aux=0;
			for (int j=0; j< c1.size(); j++) {
				if (c1.get(j).getID() == this.sequence.get(index).getVerticesL().get(i).getID()) {
					aux++;
				}
			}
			if (aux ==0) {
				c2.add(new VertexL(this.sequence.get(index).getVerticesL().get(i)));
			}
			
		}
		
		for (int i = 0; i < this.sequence.get(index).getVerticesR().size(); i++) {
			int aux=0;
			for (int j=0; j< d2.size(); j++) {
				if (d2.get(j).getID() == this.sequence.get(index).getVerticesR().get(i).getID()) {
					aux++;
				}
			}
			if (aux ==0) {
				d1.add(new VertexR(this.sequence.get(index).getVerticesR().get(i)));
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
				auxMatrix1[i][j] = this.sequence.get(index).getMatrix()[this.sequence.get(index).indexLofID(c1.get(i).getID())][this.sequence.get(index).indexRofID(d1.get(j).getID())];
			}
		}
		
		for (int i = 0; i < c2.size(); i++) {
			for (int j = 0; j < d2.size(); j++) {
				auxMatrix2[i][j] = this.sequence.get(index).getMatrix()[this.sequence.get(index).indexLofID(c2.get(i).getID())][this.sequence.get(index).indexRofID(d2.get(j).getID())];
			}
		}
		
		// creates two subgraphs
		
		Graph auxGraph1 = new Graph(c1,d1,auxMatrix1);
		Graph auxGraph2 = new Graph(c2,d2,auxMatrix2);
		
		
		// check whether the ratio sequence is still nondecreasing if we added new graphs
		if (index > 0) {
			if (this.getSequence().get(index-1).getRatio()  > auxGraph1.getRatio() ) {
				//System.out.println("sequence cannot be extended");
				//System.exit(0);
				return false;
			}
		}
		if (index < this.getSequence().size() -1 ) {
			if (this.getSequence().get(index+1).getRatio() < auxGraph2.getRatio() ) {
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
		
				double auxWeightC1 =0, auxWeightC2=0, auxWeightD1=0, auxWeightD2=0;
				
				for (int i = 0; i < auxGraph1.getVerticesL().size(); i++) {
					auxWeightC1 += auxGraph1.getVerticesL().get(i).getOriginalWeight();
				}
				for (int i = 0; i < auxGraph1.getVerticesL().size(); i++) {
					auxGraph1.getVerticesL().get(i).setWeight(auxGraph1.getVerticesL().get(i).getOriginalWeight()/auxWeightC1);
					auxGraph1.getVerticesL().get(i).setResidualCapacity(auxGraph1.getVerticesL().get(i).getWeight());
				}
				
				for (int i = 0; i < auxGraph2.getVerticesL().size(); i++) {
					auxWeightC2 += auxGraph2.getVerticesL().get(i).getOriginalWeight();
				}
				for (int i = 0; i < auxGraph2.getVerticesL().size(); i++) {
					auxGraph2.getVerticesL().get(i).setWeight(auxGraph2.getVerticesL().get(i).getOriginalWeight()/auxWeightC2);
					auxGraph2.getVerticesL().get(i).setResidualCapacity(auxGraph2.getVerticesL().get(i).getWeight());
				}
				
				for (int i = 0; i < auxGraph1.getVerticesR().size(); i++) {
					auxWeightD1 += auxGraph1.getVerticesR().get(i).getOriginalWeight();
				}
				for (int i = 0; i < auxGraph1.getVerticesR().size(); i++) {
					auxGraph1.getVerticesR().get(i).setWeight(auxGraph1.getVerticesR().get(i).getOriginalWeight()/auxWeightD1);
					auxGraph1.getVerticesR().get(i).setResidualCapacity(auxGraph1.getVerticesR().get(i).getWeight());
				}

				for (int i = 0; i < auxGraph2.getVerticesR().size(); i++) {
					auxWeightD2 += auxGraph2.getVerticesR().get(i).getOriginalWeight();
				}
				for (int i = 0; i < auxGraph2.getVerticesR().size(); i++) {
					auxGraph2.getVerticesR().get(i).setWeight(auxGraph2.getVerticesR().get(i).getOriginalWeight()/auxWeightD2);
					auxGraph2.getVerticesR().get(i).setResidualCapacity(auxGraph2.getVerticesR().get(i).getWeight());
				}
		
		this.replace(index,auxGraph1,auxGraph2);
		//System.out.println("size of sequence after extending: " + this.getSequence().size());
		
		
		/*for (int u=0; u<this.getSequence().size(); u++) {
			System.out.println(this.getSequence().get(u).getVerticesL().size() + " L:R " + this.getSequence().get(u).getVerticesR().size());
			System.out.println( " ratio:  " + this.getSequence().get(u).getRatio());
		}
		*/
		
		return true;
	}
}
