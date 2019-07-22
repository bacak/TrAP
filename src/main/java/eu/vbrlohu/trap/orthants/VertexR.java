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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class VertexR extends Vertex {

	private List<Arc> reverseArcs;  // list of arcs from VerticesL to VerticesR

	// constructor
	public VertexR(int id, double originalWeight, double weight) {
		setID(id);
		setOriginalWeight(originalWeight);
		setWeight(weight);
		setResidualCapacity(weight);
		setHeight(0);
		setFlow(0);
		setExcess(0);
		reverseArcs = new ArrayList<>();
	}


	// constructor
	public VertexR(VertexR vertexR) {
		setID(vertexR.getID());
		setOriginalWeight(vertexR.getOriginalWeight());
		setWeight(vertexR.getWeight());
		setResidualCapacity(vertexR.getWeight());
		setHeight(0);
		setFlow(0);
		setExcess(0);
		reverseArcs = new ArrayList<>();
	}


	// getter/setters
	public List<Arc> getReverseArcs() {
		return Collections.unmodifiableList(reverseArcs);
	}


	public void push() {
		//System.out.println("pushing  from : " + this.getID() + " to the sink " );

		if (getResidualCapacity() >= getExcess()) {
			setFlow(getFlow() + getExcess());
			setResidualCapacity(getResidualCapacity() - getExcess());
			setExcess(0);
		} else {
			setFlow(getFlow() + getResidualCapacity());
			setExcess(Math.max(0, getExcess() - getResidualCapacity()));
			setResidualCapacity(0);
		}
	}
}