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

public class Vertex {

	private int id = 0;
	private double weight =0;  // determines capacity of arcs in the push-relabel algorithm
	private double originalWeight =0; // squared length of a given edge, unlike 'weight' never changes
	private int height =0;  // height is needed for the push-relabel algorithm 'maxFlow' for computing min vertex cover
	private double excess =0;
	private double flow =0; // for a vertex from VerticesL it is the flow from the source to this point, and for a vertex from Vertices it is the flow from this point to the target
	private double residualCapacity =0;   // for a vertex from VerticesL it is a residual capacity from the source to this vertex, and for a vertex from VerticesR it is a residual capacity from this vertex to the target 
	
	// getter/setters
	public int getID() {
		return this.id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public double getOriginalWeight() {
		return this.originalWeight;
	}
	
	public void setOriginalWeight(double originalWeight) {
		this.originalWeight = originalWeight;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void setFlow(double flow) {
		this.flow = flow;
	}
	
	public double getFlow() {
		return this.flow;
	}
	
	/*public void setReverseFlow(double reverseFlow) {
		this.reverseFlow = reverseFlow;
	}
	
	public double getReverseFlow() {
		return this.reverseFlow;
	}*/
	
	public void setResidualCapacity(double residualCapacity) {
		this.residualCapacity = residualCapacity;
	}
	
	public double getResidualCapacity() {
		return this.residualCapacity;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public double getExcess() {
		return this.excess;
	}
	
	public void setExcess(double excess) {
		this.excess = excess;
	}
	
	public void increaseHeight() {
		this.height ++;
	}
	
}
