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


import eu.vbrlohu.trap.orthants.Graph;
import eu.vbrlohu.trap.orthants.Path;
import eu.vbrlohu.trap.orthants.Vertex;

import java.util.*;

import static eu.vbrlohu.trap.iotools.Utils.nextIndexOf;


public final class Tree {

	private List<InnerEdge> innerEdges = new ArrayList<>();
	private List<LeafEdge> leafEdges = new ArrayList<>();

	// constructor
	public Tree(List<InnerEdge> innerEdges, List<LeafEdge> leafEdges) {
		setLeafEdges(leafEdges);
		setInnerEdges(innerEdges);
	}

	// constructor
	public Tree(Tree tree) {
		setLeafEdges(tree.getLeafEdges());
		setInnerEdges(tree.getInnerEdges());
	}


	// constructor from Newick, where the root has label 0 and always is the last leaf in the string
	public Tree(String newick) throws IllegalArgumentException {
		List<LeafEdge> leafEdges = new ArrayList<>();
		List<InnerEdge> innerEdges = new ArrayList<>();
		int i = newick.indexOf('(');

		while (i < newick.length()) {
			int id;
			String name;
			double length;

			switch (newick.charAt(i)) {
				case '(':
					if (i == newick.indexOf('(')) {  // if the first '(' is associated with the root and not with an inner edge
						++i;
					} else {

						List<LeafEdge> auxSplit = new ArrayList<>();
						List<LeafEdge> auxCosplit = new ArrayList<>();
						InnerEdge auxInnerEdge = new InnerEdge(auxSplit, auxCosplit, -1);
						innerEdges.add(auxInnerEdge);
						i++;
					}
					break;

				case ')':
					i = i + 2;
					int j;
					j = nextIndexOf(newick, ";,)", i);
					length = Double.parseDouble(newick.substring(i, j));

					for (int r = innerEdges.size() - 1; r > -1; r--) {
						if (innerEdges.get(r).getLength() == -1) {
							innerEdges.get(r).setLength(length);
							break;
						}
					}
					i = j;
					break;

				case ',':
					i++;
					break;

				default:

					id = Integer.parseInt(newick.substring(i, newick.indexOf(':', i)));
					System.out.println("id " + id);
					name = Integer.toString(id);
					length = Double.parseDouble(newick.substring(newick.indexOf(':', i) + 1, nextIndexOf(newick, ";,)", i)));

					LeafEdge auxLeafEdge = new LeafEdge(id, name, length);
					leafEdges.add(auxLeafEdge);
					if (id == 0) {
						for (InnerEdge innerEdge : innerEdges) {
							for (LeafEdge leafEdge : leafEdges) {
								if (!innerEdge.isIDinCosplit(leafEdge.getID())) {
									innerEdge.addLeafEdgeIntoSplit(leafEdge);
								}
							}
						}
						i = newick.length();
					} else {
						for (InnerEdge innerEdge : innerEdges) {
							if (innerEdge.getLength() == -1) {
								innerEdge.addLeafEdgeIntoCosplit(auxLeafEdge);
							}
						}
						i = nextIndexOf(newick, ";,)", i);
					}

			}  //switch

		} // while

		setInnerEdges(innerEdges);
		setLeafEdges(leafEdges);
	}


	// getters/setters
	public List<InnerEdge> getInnerEdges() {
		return Collections.unmodifiableList(innerEdges);
	}

	private void setInnerEdges(List<InnerEdge> innerEdges) {
		this.innerEdges.clear();
		for (InnerEdge innerEdge : innerEdges) {
			InnerEdge auxInnerEdge = new InnerEdge(innerEdge);
			if (auxInnerEdge.getLength() > Graph.ZERO) {
				this.innerEdges.add(auxInnerEdge);
			}
		}
	}

	public List<LeafEdge> getLeafEdges() {
		return Collections.unmodifiableList(leafEdges);
	}

	private void setLeafEdges(List<LeafEdge> leafEdges) {
		this.leafEdges.clear();
		this.leafEdges.addAll(leafEdges);
	}

	private void setLeafEdgesWithNew(List<LeafEdge> leafEdges) {
		leafEdges.clear();
		for (LeafEdge leafEdge : leafEdges) {
			addLeafEdge(new LeafEdge(leafEdge));
		}
	}


	private void addLeafEdge(LeafEdge leafEdge) {
		//TODO check whether leafEdge can be added

		leafEdges.add(leafEdge);
	}


	private void removeInnerEdge(int ind) {
		// TODO out of bounds

		innerEdges.remove(ind);
	}


	// convert a tree into a string of characters in Newick format
	public String toString() {

		StringBuilder treeInSB = new StringBuilder();
		List<String> atomsInString = new ArrayList<>();
		List<LeafEdge> activeLeaf = new ArrayList<>();
		List<InnerEdge> activeInner = new ArrayList<>();
		List<InnerEdge> atoms = new ArrayList<>();
		double lengthOfRoot = 0;

		for (InnerEdge innerEdge : innerEdges) {
			InnerEdge auxInnerEdge = new InnerEdge(innerEdge);
			activeInner.add(auxInnerEdge);
		}

		for (LeafEdge leafEdge : leafEdges) {
			if (leafEdge.getID() != 0) {
				LeafEdge auxLeaveEdge = new LeafEdge(leafEdge);
				activeLeaf.add(auxLeaveEdge);
			} else {
				lengthOfRoot = leafEdge.getLength();
			}
		}

		while (!activeInner.isEmpty()) {
			for (int i = 0; i < activeInner.size(); ++i) {
				if (activeInner.get(i).isMinimalAmong(activeInner)) {
					StringBuilder auxsb = new StringBuilder();
					for (int j = 0; j < atoms.size(); ++j) {
						if (atoms.get(j).isBelow(activeInner.get(i))) {
							if (auxsb.length() == 0) {
								auxsb.append(atomsInString.get(j));
							} else {
								auxsb.append(",").append(atomsInString.get(j));
							}
							atoms.remove(j);
							atomsInString.remove(j);
							--j;
						}
					}

					for (int r = 0; r < activeLeaf.size(); r++) {

						if (activeInner.get(i).isIDinCosplit(activeLeaf.get(r).getID())) {
							if (auxsb.length() == 0) {
								auxsb.append(String.valueOf(activeLeaf.get(r).getID())).append(":").append(String.valueOf(activeLeaf.get(r).getLength()));

							} else {

								auxsb.append(",").append(String.valueOf(activeLeaf.get(r).getID())).append(":").append(String.valueOf(activeLeaf.get(r).getLength()));
							}

							activeLeaf.remove(r);
							r--;
						}
					}

					atoms.add(activeInner.get(i));
					auxsb.insert(0, '(');
					auxsb.append("):").append(String.valueOf(activeInner.get(i).getLength()));
					String auxString = auxsb.toString();
					atomsInString.add(auxString);
					activeInner.remove(i);
					i--;
				}
			}
		}

		for (int i = 0; i < atoms.size(); i++) {
			if (treeInSB.length() == 0) {
				treeInSB.append(atomsInString.get(i));
			} else {
				treeInSB.append(",").append(atomsInString.get(i));
			}
		}

		for (LeafEdge anActiveLeaf : activeLeaf) {
			StringBuilder auxsb = new StringBuilder();
			auxsb.append(String.valueOf(anActiveLeaf.getID()));
			auxsb.append(":").append(String.valueOf(anActiveLeaf.getLength()));
			treeInSB.append(",").append(auxsb);
		}
		treeInSB.insert(0, '(');
		treeInSB.append(",0:").append(lengthOfRoot).append(");");

		return treeInSB.toString();
	}


	// finds the length of shortest leaf edge
	public OptionalDouble shortestLeafEdge() {
		return leafEdges.stream().mapToDouble(LeafEdge::getLength).min();
	}

	// finds the length of shortest inner edge
	public OptionalDouble shortestInnerEdge() {
		return innerEdges.stream().mapToDouble(InnerEdge::getLength).min();
	}

	// computes norm squared of a given set of inner edges
	public double norm2OfInnerEdges() {
		return innerEdges.stream().mapToDouble(e -> Math.pow(e.getLength(), 2)).sum();
	}


	// extract a subtree from a given tree below a given inner edge
	private Tree giveSubtree(InnerEdge e) {

		Tree auxTree = new Tree(this);

		// changes leaf edges of the tree
		auxTree.setLeafEdges(e.getSplit());
		LeafEdge auxLeafEdge = new LeafEdge(e.smallestIDinCosplit(), e.nameOfSmallestIDinCosplit(), e.getLength());
		auxTree.addLeafEdge(auxLeafEdge);

		// removes inner edges
		for (int i = 0; i < auxTree.getInnerEdges().size(); ++i) {
			int n = 0;
			for (LeafEdge leafEdge : auxTree.getInnerEdges().get(i).getCosplit()) {
				if (e.getCosplit().contains(leafEdge)) {
					++n;
				}
			}
			;
			if (n == auxTree.getInnerEdges().get(i).getCosplit().size()) {
				auxTree.removeInnerEdge(i);
				i--;
			} else { // we don't remove this edge and need to find out whether it contains e.getCosplit().get(0) in split or cosplit in order to know where to put auxLeafEdge
				if (auxTree.getInnerEdges().get(i).getSplit().contains(e.getCosplit().get(0))) {
					auxTree.getInnerEdges().get(i).addLeafEdgeIntoSplit(auxLeafEdge);
				} else {
					auxTree.getInnerEdges().get(i).getCosplit().add(auxLeafEdge);
				}
			}
		}

		// removes leaf edges from splits/cosplits of the remaining inner edges
		for (InnerEdge innerEdge : auxTree.getInnerEdges()) {
			for (LeafEdge leafEdge : e.getCosplit()) {
				innerEdge.removeLeafEdgeFromSplit(leafEdge);
				innerEdge.removeLeafEdgeFromCosplit(leafEdge);
			}
		}
		return auxTree;
	}

	// extracts a cosubtree of a given tree below a given inner edge
	private Tree giveCosubtree(InnerEdge e) {

		Tree auxTree = new Tree(this);

		// changes leaf edges of the tree
		auxTree.setLeafEdges(e.getCosplit());
		LeafEdge auxLeafEdge = new LeafEdge(0, "root", e.getLength());  // creates a new root
		auxTree.addLeafEdge(auxLeafEdge); // adds the new root

		// removes inner edges
		for (int i = 0; i < auxTree.getInnerEdges().size(); ++i) {
			int n = 0;
			for (LeafEdge leafEdge : auxTree.getInnerEdges().get(i).getCosplit()) {
				if (e.getSplit().contains(leafEdge)) {
					++n;
				}
			}
			;
			if (n == auxTree.getInnerEdges().get(i).getCosplit().size()) {
				auxTree.removeInnerEdge(i);
				--i;
			}
		}

		for (int i = 0; i < auxTree.getInnerEdges().size(); ++i) {
			int n = 0;
			for (LeafEdge leafEdge : auxTree.getInnerEdges().get(i).getSplit()) {
				if (e.getSplit().contains(leafEdge)) {
					++n;
				}
			}
			;
			if (n == auxTree.getInnerEdges().get(i).getSplit().size()) {
				auxTree.removeInnerEdge(i);
				--i;
			}
		}

		// removes leaf edges from splits/cosplits of the remaining inner edges
		for (InnerEdge innerEdge : auxTree.getInnerEdges()) {
			for (LeafEdge leafEdge : e.getSplit()) {
				innerEdge.removeLeafEdgeFromSplit(leafEdge);
			}
			innerEdge.addLeafEdgeIntoSplit(auxLeafEdge);
		}

		return auxTree;

	}


	// check whether inner edges of a given tree contain a given inner edge
	public boolean isInInnerEdges(InnerEdge query) {
		for (InnerEdge innerEdge : innerEdges) {
			if (innerEdge.isIdenticalWith(query)) {
				return true;
			}
		}
		return false;
	}

	// compares a pair of inner edges, checks whether they are identical, i.e. have the same split (and hence cosplit)
	private static boolean isSameEdge(InnerEdge e, InnerEdge f) {
		if (e.getSplit().size() != f.getSplit().size()) {
			return false;
		}
		int n = 0;
		for (LeafEdge leafEdge : e.getSplit()) {
			if (f.isIDinSplit(leafEdge.getID())) {
				++n;
			}
		}
		return n == e.getSplit().size();
	}


	// find indices of a common edge in a given pair of trees, if no common edge, returns -1,-1
	private static int[] findCommonEdge(Tree s, Tree t) {
		for (int i = 0; i < s.getInnerEdges().size(); i++) {
			for (int j = 0; j < t.getInnerEdges().size(); j++) {
				if (isSameEdge(s.getInnerEdges().get(i), t.getInnerEdges().get(j))) {
					return new int[]{i, j};
				}
			}
		}
		return new int[]{-1, -1};
	}


	// find indices of redundant edges in a given tree, if no redundant edge, returns -1,-1  ... only for testing purposes
	public static int[] findRedundantEdge(Tree s) {
		for (int i = 0; i < s.getInnerEdges().size(); i++) {
			for (int j = 0; j < s.getInnerEdges().size(); j++) {
				if (isSameEdge(s.getInnerEdges().get(i), s.getInnerEdges().get(j)) && i != j) {
					return new int[]{i, j};
				}
			}
		}
		return new int[]{-1, -1};
	}

	// check whether a tree has mutually compatible inner edges  ... only for testing purposes
	public boolean hasCompatibleEdges() {
		return areCompatible(getInnerEdges(), getInnerEdges());
	}

	// check whether a tree has correct splits/cosplits wrt leaf edges ... only for testing purposes
	public boolean selfTest() {
		int counter2 = 0;
		for (LeafEdge leafEdge : leafEdges) {
			int counter1 = 0;
			for (InnerEdge innerEdge : innerEdges) {
				int counter = 0;
				if (innerEdge.getSplit().contains(leafEdge)) {
					++counter;
				}
				if (innerEdge.getCosplit().contains(leafEdge)) {
					++counter;
				}
				if (counter != 1) {
					return false;
				} else {
					++counter1;
				}
			}
			if (counter1 != innerEdges.size()) {
				return false;
			} else {
				++counter2;
			}
		}
		return counter2 == leafEdges.size();
	}

	// checks whether two inner edges are compatible ... TODO test for two edges from one tree, which must be always compatible
	public static boolean areCompatible(InnerEdge e, InnerEdge f) {

		int n = 0;
		for (LeafEdge leafEdge : e.getCosplit()) {
			if (f.isIDinSplit(leafEdge.getID())) {
				++n;
			}
		}
		if (n == 0) {
			return true;
		}

		int m = 0;
		for (LeafEdge leafEdge : f.getCosplit()) {
			if (e.isIDinSplit(leafEdge.getID())) {
				++m;
			}
		}
		if (m == 0) {
			return true;
		}

		int k = 0;
		for (LeafEdge leafEdge : e.getCosplit()) {
			if (f.isIDinCosplit(leafEdge.getID())) {
				++k;
			}
		}
		return k == 0;

	}

	// checks whether two sets of edges are compatible
	public static boolean areCompatible(List<InnerEdge> E, List<InnerEdge> F) {
		for (InnerEdge aE : E) {
			for (InnerEdge aF : F) {
				if (!areCompatible(aE, aF)) {
					return false;
				}
			}
		}
		return true;
	}


	// gives length of the leaf edge with ID index, if there is no leaf edge with this ID, gives -1
	private double getLengthOfID(int index) {
		for (LeafEdge leafEdge : leafEdges) {
			if (leafEdge.getID() == index) {
				return leafEdge.getLength();
			}
		}
		return -1;
	}

	private LeafEdge getLeafEdgeOfID(int index) {
		for (LeafEdge leafEdge : leafEdges) {
			if (leafEdge.getID() == index) {
				return leafEdge;
			}
		}
		return null;
	}

	// computes the contribution to the distance (squared) due to inner edges
	private static double innerEdgesDist2(Tree s, Tree t) {

		Tree copyOfs = new Tree(s);
		Tree copyOft = new Tree(t);

		double distance2 = 0;
		int i = findCommonEdge(copyOfs, copyOft)[0];
		int j = findCommonEdge(copyOfs, copyOft)[1];

		if (i == -1) {

			// checks whether the tree have a common edge, if not (i.e. if i == -1) it calls distOfDistjoint2

			//System.out.println("call for disjoint case of inner edges distance");

			//copyOfs.print();
			//copyOft.print();


			return distOfDisjoint2(copyOfs, copyOft);
		} else {
			// else it computes the contribution from the common edge itself, and recursively calls this method for subtrees

			//System.out.println("call recursively inner edges distance");

			distance2 = Math.pow(copyOfs.getInnerEdges().get(i).getLength() - copyOft.getInnerEdges().get(j).getLength(), 2);


			distance2 += innerEdgesDist2(copyOfs.giveSubtree(copyOfs.getInnerEdges().get(i)), copyOft.giveSubtree(copyOft.getInnerEdges().get(j)));

			/*copyOfs.print();
            copyOfs.giveSubtree(copyOfs.getInnerEdges().get(i)).print();
			copyOft.print();
			copyOft.giveSubtree(copyOft.getInnerEdges().get(j)).print();
			*/

			distance2 += innerEdgesDist2(copyOfs.giveCosubtree(copyOfs.getInnerEdges().get(i)), copyOft.giveCosubtree(copyOft.getInnerEdges().get(j)));

		}
		return distance2;
	}


	// computes inner distance between a given pair of trees which have no common edge
	private static double distOfDisjoint2(Tree s, Tree t) {
		double distOfDisjoint2 = 0;

//        if (!Tests.areComparable(s,t)) {
//            System.out.println("Trees not comparable!");
//            System.out.println("s.leafs.size= " + s.getLeafEdges().size());
//            System.out.println("t.leafs.size= " + s.getLeafEdges().size());
//            for (int i=0; i< s.getLeafEdges().size(); i++) {
//                System.out.println("s. leaf. id: " + s.getLeafEdges().get(i).getID());
//            }
//            for (int i=0; i< t.getLeafEdges().size(); i++) {
//                System.out.println("t. leaf. id: " + t.getLeafEdges().get(i).getID());
//            }
//            System.exit(0);
//        }

		// check whether both trees have inner edges
		if (s.getInnerEdges().isEmpty()) {
			distOfDisjoint2 += t.getInnerEdges().stream().mapToDouble(e -> Math.pow(e.getLength(), 2)).sum();

		} else {
			if (t.getInnerEdges().isEmpty()) {
				distOfDisjoint2 += s.getInnerEdges().stream().mapToDouble(e -> Math.pow(e.getLength(), 2)).sum();
			} else {
				Path path = new Path(s, t);

				//System.out.println("new path created of size: " + path.getSequence().size());

				int n;
				do {
					n = 0;
					for (int i = 0; i < path.getSequence().size(); i++) {

						//System.out.println("starting extesion problem");
						boolean myBool = path.extensionProblem(i);
						//System.out.println("extension problem: " + myBool);
						if (myBool) {
							//i++; // should not matter
							++n;
						}

					}

				} while (n > 0);

				/*System.out.println("sequence of ratios of size: " + path.getSequence().size());
                for (int i = 0; i < path.getSequence().size(); i++) {
					System.out.println(i + ": " + path.getSequence().get(i).getRatio() );
				}*/


				distOfDisjoint2 += path.getSequence().stream().mapToDouble(g -> Math.pow(g.getNumerator() + g.getDenominator(), 2)).sum();

                /*for (Graph graph : path.getSequence()) {

                    // double aux1 =0, aux2 =0;

                    distOfDisjoint2 += Math.pow(graph.getNumerator() + graph.getDenominator(), 2);

					*//*for (int j = 0; j < path.getSequence().get(i).getVerticesL().size(); j++) {
                        aux1 += path.getSequence().get(i).getVerticesL().get(j).getOriginalWeight();
						//aux1 += Math.pow(s.innerEdges.get(path.getSequence().get(i).getVerticesL().get(j).getID()).getLength(),2);
					}

					for (int j = 0; j < path.getSequence().get(i).getVerticesR().size(); j++) {
						aux2 += path.getSequence().get(i).getVerticesR().get(j).getOriginalWeight();
						//aux2 += Math.pow(t.innerEdges.get(path.getSequence().get(i).getVerticesR().get(j).getID()).getLength(),2);
					}

					distOfDisjoint2 += aux1 + 2 * Math.sqrt(aux1*aux2) + aux2;*//*
                }
*/
			}
		}
		return distOfDisjoint2;
	}


	// computes the total distance between a given pair of trees
	public static double totalDist(Tree s, Tree t) {
		double innerDist2, leafDist2 = 0;

		// contribution of leaf edges squared
		for (LeafEdge leafEdge : s.leafEdges) {
			int auxID = leafEdge.getID();
			leafDist2 += Math.pow(s.getLengthOfID(auxID) - t.getLengthOfID(auxID), 2);
		}

		// contribution of inner edges squared
		innerDist2 = innerEdgesDist2(s, t);

		//System.out.println("inner edges contribution computed");

		return Math.sqrt(innerDist2 + leafDist2);

	}

	// finds the tree between a given pair of DISJOINT trees with 'fraction' being the parameter of convex combination
	private static Tree convexCombinationOfDisjointTrees(Tree s, Tree t, double fraction) {
		if (fraction == 1) {
			return new Tree(t);
		}

		Path path = new Path(s, t);
		int n;
		// form Path.sequence
		do {
			n = 0;
			for (int i = 0; i < path.getSequence().size(); ++i) {

				//System.out.println("path sequence has size: " +path.getSequence().size());
				//System.out.println("its " +i+ "th element has "+path.getSequence().get(i).getVerticesL().size() + " verticesL, and " + path.getSequence().get(i).getVerticesR().size() + " verticesR");
				//System.out.println("ratio of this graph is: "+path.getSequence().get(i).getRatio());
				boolean myBool = path.extensionProblem(i);
				//System.out.println("extension problem returns: " + myBool);
				if (myBool) {
					++i;
					++n;
				}

			}
		} while (n > 0);

		/*for (int f=0; f<path.getSequence().size(); f++) {
            System.out.println("ratio sequence: " + path.getSequence().get(f).getRatio() + " of size: " + path.getSequence().size());
		}*/


		double auxFraction = fraction / (1 - fraction);
		int segmentNumber = 0;

		// identify in which segment of the treetools the graph lies
		for (int i = 0; i < path.getSequence().size(); ++i) {
			if (auxFraction > path.getSequence().get(i).getRatio()) {
				segmentNumber = i + 1;
			} else {
				break;
			}
		}

		List<InnerEdge> auxInnerEdges = new ArrayList<>();
		List<LeafEdge> auxLeafEdges = new ArrayList<>();

		auxLeafEdges.addAll(s.getLeafEdges());

		if (segmentNumber == 0) { // in this case the tree lies in the same orthant as 's'
			//System.out.println("number of inner edges of the original tree: " + s.getInnerEdges().size());

			for (Graph graph : path.getSequence()) {

				for (Vertex vertexL : graph.getVerticesL()) {
					InnerEdge auxInnerEdge = new InnerEdge(path.getOrigin().getInnerEdges().get(vertexL.getID()));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength((1 - fraction - (fraction * graph.getDenominator()) / graph.getNumerator()) * auxInnerEdge.getLength());

					//System.out.println("index in sequence/sequence size: " + r + " / " + path.getSequence().size());
					//System.out.println("auxInnerEdge length: " + auxInnerEdge.getLength());

					if (auxInnerEdge.getLength() <= Graph.ZERO) {
						//System.out.println("!0! LENGTH: " + auxInnerEdge.getLength());
					} else {
						auxInnerEdges.add(auxInnerEdge);
						//System.out.println("new inner edge added, length: " + auxInnerEdge.getLength());
						//System.out.println("current number of inner edges: " + auxInnerEdges.size());
					}
				}
			}

			return new Tree(auxInnerEdges, auxLeafEdges);
		}

		if (segmentNumber == path.getSequence().size()) {  // in this case the tree lies in the same orthant as 't'

			for (Graph graph : path.getSequence()) {
				for (Vertex vertexR : graph.getVerticesR()) {

					InnerEdge auxInnerEdge = new InnerEdge(path.getHead().getInnerEdges().get(vertexR.getID()));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength((fraction - (1 - fraction) * graph.getRatio()) * auxInnerEdge.getLength());

					if (auxInnerEdge.getLength() <= Graph.ZERO) {
						//System.out.println("!1! LENGTH: " + auxInnerEdge.getLength());
					} else {
						auxInnerEdges.add(auxInnerEdge);
					}
				}
			}

		} else {  // this is a general case in which we collect inner edges from both trees s and t

			// collect inner edges coming from the tree t
			for (int i = 0; i < segmentNumber; ++i) {
				for (Vertex vertexR : path.getSequence().get(i).getVerticesR()) {
					int auxIndex = vertexR.getID();
					InnerEdge auxInnerEdge = new InnerEdge(path.getHead().getInnerEdges().get(auxIndex));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength((fraction - (1 - fraction) * path.getSequence().get(i).getRatio()) * auxInnerEdge.getLength());

					if (auxInnerEdge.getLength() <= Graph.ZERO) {
						//System.out.println("!2! LENGTH: " + auxInnerEdge.getLength());
					} else {
						auxInnerEdges.add(auxInnerEdge);
					}
				}
			}

			// collect inner edges coming from the tree s
			for (int i = segmentNumber; i < path.getSequence().size(); ++i) {
				for (Vertex vertexL : path.getSequence().get(i).getVerticesL()) {
					int auxIndex = vertexL.getID();
					InnerEdge auxInnerEdge = new InnerEdge(path.getOrigin().getInnerEdges().get(auxIndex));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength((1 - fraction - (fraction * path.getSequence().get(i).getDenominator()) / path.getSequence().get(i).getNumerator()) * auxInnerEdge.getLength());

					if (auxInnerEdge.getLength() > Graph.ZERO) {
						auxInnerEdges.add(auxInnerEdge);
					} else {
						//System.out.println("!3! LENGTH: " + auxInnerEdge.getLength());
					}

				}
			}
		}

		return new Tree(auxInnerEdges, auxLeafEdges);
	}

	// replaces leaf edges in order to set their lengths
	private void updateLeafEdges(Tree s, Tree t, double fraction) {
		// set leaf edges
		List<LeafEdge> auxLeafEdges = new ArrayList<>();
		auxLeafEdges.addAll(getLeafEdges());

		setLeafEdgesWithNew(auxLeafEdges);

		// set the lengths of leaf edges
		for (LeafEdge leafEdge : leafEdges) {
			leafEdge.setLength((1 - fraction) * s.getLengthOfID(leafEdge.getID()) + fraction * t.getLengthOfID(leafEdge.getID()));
		}

		// set splits, and cosplits
		for (InnerEdge innerEdge : innerEdges) {
			List<LeafEdge> auxSplit = new ArrayList<>();
			List<LeafEdge> auxCosplit = new ArrayList<>();
			auxSplit.addAll(innerEdge.getSplit());
			auxCosplit.addAll(innerEdge.getCosplit());

			innerEdge.clearSplit();
			innerEdge.clearCosplit();
			for (LeafEdge anAuxSplit : auxSplit) {
				innerEdge.addLeafEdgeIntoSplit(getLeafEdgeOfID(anAuxSplit.getID()));
			}
			for (LeafEdge anAuxCosplit : auxCosplit) {
				innerEdge.addLeafEdgeIntoCosplit(getLeafEdgeOfID(anAuxCosplit.getID()));
			}
		}
	}

	// finds the tree between a given pair of trees with 'fraction' being the parameter of convex combination
	public static Tree convexCombination(Tree s, Tree t, double fraction) {

		Tree copyOfs = new Tree(s);
		Tree copyOft = new Tree(t);

		Tree auxTree;

		if (copyOfs.getInnerEdges().isEmpty()) {

			auxTree = new Tree(t);
			for (int i = 0; i < auxTree.getInnerEdges().size(); ++i) {
				auxTree.getInnerEdges().get(i).setLength(fraction * copyOft.getInnerEdges().get(i).getLength());
			}
			auxTree.updateLeafEdges(copyOfs, copyOft, fraction);

			return auxTree;
		}

		if (copyOft.getInnerEdges().isEmpty()) {

			auxTree = new Tree(copyOfs);
			for (int i = 0; i < auxTree.getInnerEdges().size(); ++i) {
				auxTree.getInnerEdges().get(i).setLength((1 - fraction) * copyOfs.getInnerEdges().get(i).getLength());
			}
			auxTree.updateLeafEdges(copyOfs, copyOft, fraction);

			return auxTree;
		}


		int i = Tree.findCommonEdge(copyOfs, copyOft)[0];
		int j = Tree.findCommonEdge(copyOfs, copyOft)[1];

		if (i == -1) {  // checks whether the tree have a common edge, if not (i.e. if i == -1) it calls treeOnGeodesicForDisjoint

			auxTree = new Tree(Tree.convexCombinationOfDisjointTrees(copyOfs, copyOft, fraction));
		} else { // else it computes the combination of the common edge itself, and recursively calls this method for subtrees

			// collect inner edges now, set their lengths, splits, and cosplits, and lengths of edges in splits and cosplits
			List<InnerEdge> auxInnerEdges = new ArrayList<>();
			InnerEdge auxInnerEdge = new InnerEdge(copyOfs.getInnerEdges().get(i));  // = t.getInnerEdges().get(j) ... not true: they have distinct leaf edges in split/cosplit
			// set new length of this edge itself
			auxInnerEdge.setLength((1 - fraction) * copyOfs.getInnerEdges().get(i).getLength() + fraction * copyOft.getInnerEdges().get(j).getLength());


			auxInnerEdges.add(auxInnerEdge);

			Tree auxTree1 = convexCombination(copyOfs.giveSubtree(copyOfs.getInnerEdges().get(i)), copyOft.giveSubtree(copyOft.getInnerEdges().get(j)), fraction);
			Tree auxTree2 = convexCombination(copyOfs.giveCosubtree(copyOfs.getInnerEdges().get(i)), copyOft.giveCosubtree(copyOft.getInnerEdges().get(j)), fraction);

			for (InnerEdge innerEdge : auxTree1.getInnerEdges()) {
				InnerEdge auxInnerEdge2 = new InnerEdge(innerEdge);

				int auxInt = copyOfs.getInnerEdges().get(i).smallestIDinCosplit(); // auxiliary leaf edge

				// we need to find out whether this auxiliary leaf edge lies in split or cosplit of the current inner edge
				if (auxInnerEdge2.isIDinSplit(auxInt)) {

					for (LeafEdge leafEdge : auxTree2.getLeafEdges()) {
						if (!auxInnerEdge2.isIDinSplit(leafEdge.getID())) {
							auxInnerEdge2.addLeafEdgeIntoSplit(leafEdge);
						}
					}
				} else {
					for (LeafEdge leafEdge : auxTree2.getLeafEdges()) {
						if ((!auxInnerEdge2.isIDinCosplit(leafEdge.getID())) && leafEdge.getID() != 0) {
							auxInnerEdge2.addLeafEdgeIntoCosplit(leafEdge);
						}
					}
				}

				int auxCounter = 0;
				for (InnerEdge auxInnerEdge1 : auxInnerEdges) {
					if (auxInnerEdge1.isIdenticalWith(auxInnerEdge2)) {
						++auxCounter;
					}
				}
				if (auxCounter == 0) {
					auxInnerEdges.add(auxInnerEdge2);
				}
			}
			for (InnerEdge innerEdge : auxTree2.getInnerEdges()) {

				InnerEdge auxInnerEdge3 = new InnerEdge(innerEdge);

				for (LeafEdge leafEdge : auxTree1.getLeafEdges()) {
					if ((!auxInnerEdge3.isIDinSplit(leafEdge.getID())) && (!auxInnerEdge3.isIDinCosplit(leafEdge.getID()))) {
						auxInnerEdge3.addLeafEdgeIntoSplit(leafEdge);
					}
				}

				auxInnerEdges.add(auxInnerEdge3);
			}

			auxTree = new Tree(auxInnerEdges, copyOfs.getLeafEdges());
		}

		auxTree.updateLeafEdges(copyOfs, copyOft, fraction);
		return auxTree;
	}


	// computes mean of a given set of trees, with given distribution. Based on the Law of large numbers.
	public static Tree meanViaLLN(List<Tree> setOfTrees, double[] probabilities, int numberOfIterations) {


		// creating array of partial sums of probabilities
		double auxSum = 0;
		double[] partialSumsOfProb = new double[probabilities.length];
		for (int i = 0; i < probabilities.length - 1; ++i) {
			auxSum = auxSum + probabilities[i];
			partialSumsOfProb[i] = auxSum;
		}
		partialSumsOfProb[probabilities.length - 1] = 1;  // set the last element manually because of possible rounding errors


		Tree solution = new Tree(setOfTrees.get(0));
		Random generator = new Random();

		double coef;
		for (int i = 1; i <= numberOfIterations; ++i) {

			// choosing randomly a tree
			int randomIndex = 0;
			double randomNumber;
			randomNumber = generator.nextDouble();
			while (randomNumber > partialSumsOfProb[randomIndex]) {
				randomIndex++;
			}

			double iAsDouble = (double) i;
			coef = 1 / (iAsDouble + 1);

			solution = convexCombination(solution, setOfTrees.get(randomIndex), coef);

		}

		return solution;
	}


}