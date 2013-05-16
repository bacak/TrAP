/** This file is part of TrAP, Tree Averaging Program, which computes medians and means of phylogenetic trees.
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

public class VertexL extends Vertex {

	private ArrayList<Arc> arcs = new ArrayList<Arc>();  // list of arcs from VerticesL to VerticesR
	
	// constructor
	public VertexL(int id, double originalWeight, double weight) {
		this.setID(id);
		this.setOriginalWeight(originalWeight);
		this.setWeight(weight);
		this.setFlow(0);
		this.setExcess(0);
		this.setResidualCapacity(weight);
		this.setHeight(0);
		this.arcs = new ArrayList<Arc>();
	}
	
	// constructor
	public VertexL(VertexL vertexL) {
		this.setID(vertexL.getID());
		this.setOriginalWeight(vertexL.getOriginalWeight());
		this.setWeight(vertexL.getWeight());
		this.setFlow(0);
		this.setExcess(0);
		this.setResidualCapacity(vertexL.getWeight());
		this.setHeight(0);
		this.arcs = new ArrayList<Arc>();
	}
	
	// getters/setters
	public ArrayList<Arc> getArcs() {
		return this.arcs;
	}
	
	
	public void reversePush() {
		//System.out.println("reverse pushing  from : " + this.getID() + " to the source " );
		if (this.getFlow() >= this.getExcess()) {
			this.setFlow(this.getFlow()-this.getExcess());
			this.setExcess(0);
		}
		else {
			//this.setReverseFlow(this.getReverseResidualCapacity());
			this.setExcess(Math.max(0,this.getExcess()-this.getFlow()));
			this.setFlow(0);
		}
	}
	
}
