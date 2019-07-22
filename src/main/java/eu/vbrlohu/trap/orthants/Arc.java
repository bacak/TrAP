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

public final class Arc {


	private VertexL origin;
	private VertexR head;
	private double flow = 0;


	// constructor
	public Arc(VertexL origin, VertexR head) {
		this.origin = origin;
		this.head = head;
		this.flow = 0;
	}


	// getters/setters
	public double getFlow() {
		return flow;
	}

	private void setFlow(double flow) {
		this.flow = flow;
	}

	public VertexR getHead() {
		return head;
	}

	public VertexL getOrigin() {
		return origin;
	}

	// tests whether a given arc is eligible
	public boolean isEligible() {
		return (origin.getHeight() > head.getHeight() && origin.getExcess() > Graph.ZERO);
	}

	// tests whether the reverse arc of a given arc is eligible
	public boolean isReverseEligible() {
		return (head.getHeight() > origin.getHeight() && flow > Graph.ZERO && head.getExcess() > Graph.ZERO);
	}

	public void push() {
		//System.out.println("pushing through the arc from : " + this.getOrigin().getID() + " to " + this.getHead().getID());
		head.setExcess(head.getExcess() + origin.getExcess());
		setFlow(flow + origin.getExcess());
		origin.setExcess(0);

	}

	public void reversePush() {
		//System.out.println("reverse pushing through the arc from : " + this.getHead().getID() + " to " + this.getOrigin().getID());
		if (flow >= head.getExcess()) {
			origin.setExcess(origin.getExcess() + head.getExcess());
			setFlow(flow - head.getExcess());
			head.setExcess(0);
		} else {
			head.setExcess(head.getExcess() - flow);
			origin.setExcess(origin.getExcess() + flow);
			setFlow(0);
		}
	}
}