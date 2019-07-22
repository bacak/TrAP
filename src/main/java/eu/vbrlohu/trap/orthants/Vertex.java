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


public class Vertex {

	int id = 0;
	double weight =0;  // determines capacity of arcs in the push-relabel algorithm
	double originalWeight =0; // squared length of a given edge, unlike 'weight' never changes
	int height =0;  // height is needed for the push-relabel algorithm 'maxFlow' for computing min vertex cover
	double excess =0;
	double flow =0; // for a vertex from VerticesL it is the flow from the source to this point, and for a vertex from Vertices it is the flow from this point to the target
	double residualCapacity =0;   // for a vertex from VerticesL it is a residual capacity from the source to this vertex, and for a vertex from VerticesR it is a residual capacity from this vertex to the target

	// getter/setters
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public double getOriginalWeight() {
		return originalWeight;
	}

	public void setOriginalWeight(double originalWeight) {
		this.originalWeight = originalWeight;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public void setFlow(double flow) {
		this.flow = flow;
	}

	public double getFlow() {
		return flow;
	}



	public void setResidualCapacity(double residualCapacity) {
		this.residualCapacity = residualCapacity;
	}

	public double getResidualCapacity() {
		return residualCapacity;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getExcess() {
		return excess;
	}

	public void setExcess(double excess) {
		this.excess = excess;
	}

	public void increaseHeight() {
		height ++;
	}

}