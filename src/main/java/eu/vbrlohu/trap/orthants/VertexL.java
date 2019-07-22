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

public final class VertexL extends Vertex {

	private List<Arc> arcs;  // list of arcs from VerticesL to VerticesR

	// constructor
	public VertexL(int id, double originalWeight, double weight) {
		setID(id);
		setOriginalWeight(originalWeight);
		setWeight(weight);
		setFlow(0);
		setExcess(0);
		setResidualCapacity(weight);
		setHeight(0);
		arcs = new ArrayList<>();
	}

	// constructor
	public VertexL(VertexL vertexL) {
		setID(vertexL.getID());
		setOriginalWeight(vertexL.getOriginalWeight());
		setWeight(vertexL.getWeight());
		setFlow(0);
		setExcess(0);
		setResidualCapacity(vertexL.getWeight());
		setHeight(0);
		arcs = new ArrayList<>();
	}


	public List<Arc> getArcs() {
		return Collections.unmodifiableList(arcs);
	}


	public void reversePush() {
		//System.out.println("reverse pushing  from : " + this.getID() + " to the source " );
		if (getFlow() >= getExcess()) {
			setFlow(getFlow() - getExcess());
			setExcess(0);
		} else {
			//this.setReverseFlow(this.getReverseResidualCapacity());
			setExcess(Math.max(0, getExcess() - getFlow()));
			setFlow(0);
		}
	}

}