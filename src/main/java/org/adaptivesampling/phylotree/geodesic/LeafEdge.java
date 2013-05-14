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


package org.adaptivesampling.phylotree.geodesic;

public class LeafEdge extends TreeEdge {

	private int id;
	
	// constructor
	public LeafEdge(int id, double length) {
		this.setLength(length);
		this.setID(id);
	}
	
	// constructor
	public LeafEdge(LeafEdge leafEdge) {
		this.id = leafEdge.getID();
		this.setLength(leafEdge.getLength());
	}
	
	
	// getters/setters
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return this.id;
	}

}
