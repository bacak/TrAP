package eu.vbrlohu.trap.treetools;

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

public final class InnerEdge
{
	private double length;

	private List<LeafEdge> split = new ArrayList<>();
	private List<LeafEdge> cosplit = new ArrayList<>();

	public InnerEdge(final List<LeafEdge> split, final List<LeafEdge> cosplit, final double length) {
		setLength(length);
		setSplit(split);
		setCosplit(cosplit);
	}

	public InnerEdge(final InnerEdge innerEdge) {
		setLength(innerEdge.getLength());
		setSplit(innerEdge.getSplit());
		setCosplit(innerEdge.getCosplit());
	}

	public List<LeafEdge> getSplit() {
		return Collections.unmodifiableList(split);
	}

	public List<LeafEdge> getCosplit()
	{
		return Collections.unmodifiableList(cosplit);
	}

	private void setSplit(final List<LeafEdge> leafEdges) {
		split.clear();
		split.addAll(leafEdges);
	}

	private void setCosplit(final List<LeafEdge> leafEdges) {
		cosplit.clear();
		cosplit.addAll(leafEdges);
	}

	public void clearSplit()
	{
		split.clear();
	}

	public void clearCosplit()
	{
		cosplit.clear();
	}

	public double getLength() {
		return length;
	}

	public void setLength(final double length) {
		this.length = length;
	}


	public void addLeafEdgeIntoSplit(final LeafEdge leafEdge)
	{
		// TODO check whether possible
		split.add(leafEdge);
	}

	public void addLeafEdgeIntoCosplit(final LeafEdge leafEdge)
	{
		// TODO check whether possible
		cosplit.add(leafEdge);
	}

	public void removeLeafEdgeFromSplit(final LeafEdge leafEdge)
	{
		split.remove(leafEdge);
	}

	public void removeLeafEdgeFromCosplit(final LeafEdge leafEdge)
	{
		cosplit.remove(leafEdge);
	}



	// test whether a leaf edge with given ID lies in split, returns true if in split, false otherwise,
	//this method is used for finding a point on a treetools, namely for reconstructing a tree,
	// and also to find out whether two inner edges are compatible
	public boolean isIDinSplit(final int index) {
		for (LeafEdge leafEdge : split) {
			if (leafEdge.getID() == index) {
				return true;
			}
		}
		return false;
	}
	// the same as above for cosplits
	public boolean isIDinCosplit(final int index) {
		for (LeafEdge leafEdge : cosplit) {
			if (leafEdge.getID() == index) {
				return true;
			}
		}
		return false;
	}


	// give smallest ID (of a leaf edge) in the cosplit, needed when creating subtrees and cosubtrees
	public int smallestIDinCosplit() {
		int id = cosplit.get(0).getID();
		for (int i=1; i< cosplit.size(); ++i) {
			if (cosplit.get(i).getID() < id) {
				id = cosplit.get(i).getID();
			}
		}
		return id;
	}

	//  returns the name of the leaf edge with smallest id among all leaves in the cosplit
	public String nameOfSmallestIDinCosplit() {
		String name = null;
		int id = cosplit.get(0).getID();
		for (int i=1; i<cosplit.size(); ++i) {
			if (cosplit.get(i).getID() < id) {
				id = cosplit.get(i).getID();
				name = cosplit.get(i).getName();
			}
		}
		return name;
	}

	// check whether a given inner edge is the same as the inner edge in place of parameter
	public boolean isIdenticalWith(final InnerEdge queryEdge) {
		if ( (split.size() != queryEdge.getSplit().size() ) || (cosplit.size() != queryEdge.getCosplit().size())  ) {
			return false;
		}
		int n=0;
		for (LeafEdge leafEdge : split) {
			if (queryEdge.isIDinSplit(leafEdge.getID())) {
				n++;
			}
			else {
				break;
			}
		}
		if (n < split.size()) {
			return false;
		}
		int m=0;
		for (LeafEdge leafEdge : cosplit) {
			if (queryEdge.isIDinCosplit(leafEdge.getID())) {
				++m;
			}
			else {
				break;
			}
		}
		return m >= cosplit.size();
	}



	// check whether a given inner edge is minimal in the set 'innerEdges', i.e. its cosplit contains no smaller cosplit of an edge from 'innerEdges', and has smallest cardinality
	public boolean isMinimalAmong(final List<InnerEdge> innerEdges) {
		int minSizeOfCosplit= innerEdges.get(0).getCosplit().size();
		for (InnerEdge innerEdge : innerEdges)
		{
			int size = innerEdge.getCosplit().size();
			if (minSizeOfCosplit > size)
			{
				minSizeOfCosplit = size;
			}
			if (innerEdge.isBelow(this))
			{
				return false;
			}
		}
		return cosplit.size() <= minSizeOfCosplit;
	}

	// check whether the cosplit is contained in the cosplit of a given edge
	public boolean isBelow(final InnerEdge queryEdge) {

		int counter =0;
		for (LeafEdge leafEdge : cosplit) {
			if (queryEdge.isIDinCosplit(leafEdge.getID())) {
				++counter;
			}
		}
		int size = cosplit.size();
		return counter == size && queryEdge.getCosplit().size() > size;
	}
}
