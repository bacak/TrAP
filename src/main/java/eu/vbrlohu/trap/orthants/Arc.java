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

public class Arc {
	
	
	private VertexL origin;
	private VertexR head;
	private double flow =0;
	
	
	// constructor
	public Arc(VertexL origin, VertexR head) {
		this.origin = origin;
		this.head = head;
		this.flow =0;
	}
	
	
	
	// getters/setters
	public double getFlow() {
		return this.flow;
	}
	
	public void setFlow(double flow) {
		this.flow = flow;
	}
	
	public VertexR getHead() {
		return this.head;
	}
	
	public VertexL getOrigin() {
		return this.origin;
	}

	// tests whether a given arc is eligible
	public boolean isEligible() {
		if (this.getOrigin().getHeight() > this.getHead().getHeight() && this.getOrigin().getExcess() > Graph.ZERO) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// tests whether the reverse arc of a given arc is eligible
	public boolean isReverseEligible() {
		if (this.getHead().getHeight() > this.getOrigin().getHeight() && this.getFlow() > Graph.ZERO && this.getHead().getExcess() > Graph.ZERO) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void push() {
		//System.out.println("pushing through the arc from : " + this.getOrigin().getID() + " to " + this.getHead().getID());
		this.getHead().setExcess(this.getHead().getExcess() + this.getOrigin().getExcess());
		this.setFlow(this.getFlow()+this.getOrigin().getExcess());
		this.getOrigin().setExcess(0);
		
	}
	
	public void reversePush() {
		//System.out.println("reverse pushing through the arc from : " + this.getHead().getID() + " to " + this.getOrigin().getID());
		if (this.getFlow() >= this.getHead().getExcess()) {
			this.getOrigin().setExcess(this.getOrigin().getExcess()+this.getHead().getExcess());
			this.setFlow(this.getFlow()-this.getHead().getExcess());
			this.getHead().setExcess(0);
		}
		else {
			this.getHead().setExcess(this.getHead().getExcess() - this.getFlow());
			this.getOrigin().setExcess(this.getOrigin().getExcess() + this.getFlow());
			this.setFlow(0);
		}
	}



}
