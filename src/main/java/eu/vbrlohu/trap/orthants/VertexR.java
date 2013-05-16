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

package eu.vbrlohu.trap.orthants;

import java.util.ArrayList;

public class VertexR extends Vertex {
	
	private ArrayList<Arc> reverseArcs = new ArrayList<Arc>();  // list of arcs from VerticesL to VerticesR
	
	
	// constructor
	public VertexR(int id, double originalWeight, double weight) {
		this.setID(id);
		this.setOriginalWeight(originalWeight);
		this.setWeight(weight);
		this.setResidualCapacity(weight);
		this.setHeight(0);
		this.setFlow(0);
		this.setExcess(0);
		this.reverseArcs = new ArrayList<Arc>();
	}
	
	
	
	// constructor
	public VertexR(VertexR vertexR) {
		this.setID(vertexR.getID());
		this.setOriginalWeight(vertexR.getOriginalWeight());
		this.setWeight(vertexR.getWeight());
		this.setResidualCapacity(vertexR.getWeight());
		this.setHeight(0);
		this.setFlow(0);
		this.setExcess(0);
		this.reverseArcs = new ArrayList<Arc>();
	}
	
	// getters/setters
	public ArrayList<Arc> getReverseArcs() {
		return this.reverseArcs;
	}
	

    public void push() {
    	//System.out.println("pushing  from : " + this.getID() + " to the sink " );
    	
    		if (this.getResidualCapacity() >= this.getExcess()) {
    			
    			this.setFlow(this.getFlow()+this.getExcess());
    			this.setResidualCapacity(this.getResidualCapacity()-this.getExcess());
    			this.setExcess(0);
    			
    		}
    		else {
    			
    			this.setFlow(this.getFlow()+this.getResidualCapacity());
    			this.setExcess(Math.max(0,this.getExcess()-this.getResidualCapacity()));
    			this.setResidualCapacity(0);
    		}
    }
}
