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



package eu.vbrlohu.trap.geodesic;

import java.util.*;

public class InnerEdge extends TreeEdge {

	private ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
	private ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
	
	public InnerEdge(ArrayList<LeafEdge> split, ArrayList<LeafEdge> cosplit, double length) {
		super(length);
		this.setSplit(split);
		this.setCosplit(cosplit);
	}
	
	public InnerEdge(InnerEdge innerEdge) {
		super(innerEdge.getLength());
		this.setSplit(innerEdge.getSplit());
		this.setCosplit(innerEdge.getCosplit());
	}
	
	public ArrayList<LeafEdge> getSplit() {
		return this.split;
	}
	
	public ArrayList<LeafEdge> getCosplit() {
		return this.cosplit;
		
	}
	
	public void setSplit(ArrayList<LeafEdge> split) {
		this.split.clear();
		for (int i=0; i< split.size(); i++) {
			this.split.add(split.get(i));
		}
	}
	
	public void setCosplit(ArrayList<LeafEdge> cosplit) {
		this.cosplit.clear();
		for (int i=0; i< cosplit.size(); i++) {
			this.cosplit.add(cosplit.get(i));
		}
	}
	
	// test whether a leaf edge with given ID lies in split, returns true if in split, false otherwise, 
	//this method is used for finding a point on a geodesic, namely for reconstructing a tree,
	// and also to find out whether two inner edges are compatible
	public boolean isIDinSplit(int index) {
		for (int i = 0; i< this.getSplit().size(); i++) {
			if (this.getSplit().get(i).getID() == index) {
				return true;
			}
		}
		return false;
	}
	// the same as above for cosplits
	public boolean isIDinCosplit(int index) {
		for (int i = 0; i< this.getCosplit().size(); i++) {
			if (this.getCosplit().get(i).getID() == index) {
				return true;
			}
		}
		return false;
	}
	
	
	// give smallest ID (of a leaf edge) in the cosplit, needed when creating subtrees and cosubtrees
	public int smallestIDinCosplit() {
		int id =this.getCosplit().get(0).getID();
		for (int i=1; i< this.getCosplit().size(); i++) {
			if (this.getCosplit().get(i).getID() < id) {
				id = this.getCosplit().get(i).getID();
			}
		}
		return id;
	}
	
	// check whether a given inner edge is the same as the inner edge in place of parameter
	public boolean isIdenticalWith(InnerEdge innerEdge) {
		if ( (this.getSplit().size() != innerEdge.getSplit().size() ) || (this.getCosplit().size() != innerEdge.getCosplit().size())  ) {
			return false;
		}
		int n=0;
		for (int i=0; i < this.getSplit().size(); i++) {
			if (innerEdge.isIDinSplit(this.getSplit().get(i).getID())) {
				n++;
			}
			else {
				break;
			}
		}
		if (n < this.getSplit().size()) {
			return false;
		}
		int m=0;
		for (int i=0; i < this.getCosplit().size(); i++) {
			if (innerEdge.isIDinCosplit(this.getCosplit().get(i).getID())) {
				m++;
			}
			else {
				break;
			}
		}
		if (m < this.getCosplit().size()) {
			return false;
		}
		return true;
	}
	
	
	
	// check whether a given inner edge is minimal in the set 'innerEdges', i.e. its cosplit contains no smaller cosplit of an edge from 'innerEdges', and has smallest cardinality
	public boolean isMinimalAmong(ArrayList<InnerEdge> innerEdges) {
		int minSizeOfCosplit= innerEdges.get(0).getCosplit().size();
		for (int i=0; i<innerEdges.size(); i++) {
			if (minSizeOfCosplit> innerEdges.get(i).getCosplit().size()) {
				minSizeOfCosplit=innerEdges.get(i).getCosplit().size();
			}
			if (innerEdges.get(i).isBelow(this)) {
				return false;
			}
		}
		if (this.getCosplit().size()>minSizeOfCosplit) {
			return false;
		}
		return true;
	}	

	// check whether the cosplit is contained in the cosplit of a given edge
	public boolean isBelow(InnerEdge innerEdge) {
		int counter =0;
		for (int j=0; j<this.getCosplit().size(); j++) {
			if (innerEdge.isIDinCosplit(this.getCosplit().get(j).getID())) {
				counter++;
			}
		}
		if (counter==this.getCosplit().size()&& innerEdge.getCosplit().size()>this.getCosplit().size()) {
			return true;
		} else {
			return false;
		}
	}
}
