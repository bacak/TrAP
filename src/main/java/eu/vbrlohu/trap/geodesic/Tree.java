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

package eu.vbrlohu.trap.geodesic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;


import eu.vbrlohu.trap.iotools.Tests;
import eu.vbrlohu.trap.iotools.TrAP;
import eu.vbrlohu.trap.orthants.Graph;
import eu.vbrlohu.trap.orthants.Path;

public class Tree {

	private ArrayList<InnerEdge> innerEdges = new ArrayList<InnerEdge>();
	private ArrayList<LeafEdge> leafEdges = new ArrayList<LeafEdge>();
	
	// constructor
	public Tree(ArrayList<InnerEdge> innerEdges, ArrayList<LeafEdge> leafEdges) {
		this.setLeafEdges(leafEdges);
		this.setInnerEdges(innerEdges);
	}
	
	// constructor
	public Tree(Tree tree) {
		this.setLeafEdges(tree.getLeafEdges());
		this.setInnerEdges(tree.getInnerEdges());
	}
	
	/*// constructor
	public Tree(ArrayList<String> stringTree) {
		ArrayList<LeafEdge> leafEdges = new ArrayList<LeafEdge>();
		ArrayList<InnerEdge> innerEdges = new ArrayList<InnerEdge>();
		for (int i=0; i<stringTree.size(); i++) {
			int id = Integer.parseInt(stringTree.get(i).substring(0, stringTree.get(i).indexOf(' ')));
			double length =0;
			
			if (stringTree.get(i).indexOf(' ') != stringTree.get(i).lastIndexOf(' ')) {   // inner edges
				int indexOfSecondSpace = stringTree.get(i).indexOf(' ', stringTree.get(i).indexOf(' ')+1);
				length = Double.parseDouble(stringTree.get(i).substring(stringTree.get(i).indexOf(' ')+1, indexOfSecondSpace));
				ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
				ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
				int indexOfPrevSpace = indexOfSecondSpace;
				int indexOfSpace = indexOfSecondSpace;
				boolean auxCond = true;
				while (auxCond) {
					
					if (indexOfSpace != stringTree.get(i).lastIndexOf(' ')) {
						indexOfPrevSpace = indexOfSpace;
						indexOfSpace = stringTree.get(i).indexOf(' ', indexOfSpace+1);
						int idToSplit = Integer.parseInt(stringTree.get(i).substring(indexOfPrevSpace + 1, indexOfSpace));
						for (int r=0; r< leafEdges.size(); r++) {
							if (idToSplit == leafEdges.get(r).getID()) {
								split.add(leafEdges.get(r));
								break;
							}
						}
					}
					else {
						int idToSplit = Integer.parseInt(stringTree.get(i).substring(indexOfSpace + 1, stringTree.get(i).length()));
						for (int r=0; r< leafEdges.size(); r++) {
							if (idToSplit == leafEdges.get(r).getID()) {
								split.add(leafEdges.get(r));
								break;
							}
						}
						auxCond = false;
					}	
				}
				
				InnerEdge innerEdge = new InnerEdge(split, cosplit, length);
				// set cosplit
				for (int q=0; q<leafEdges.size(); q++) {
					int auxID =0;
					auxID = leafEdges.get(q).getID();
					if (!innerEdge.isIDinSplit(auxID)) {
						innerEdge.getCosplit().add(leafEdges.get(q));
					}
				}
				if (innerEdge.getLength()>Graph.ZERO) {
  					innerEdges.add(innerEdge);
  				}
			}
			else {   // leaf edges
				
				length = Double.parseDouble(stringTree.get(i).substring(stringTree.get(i).indexOf(' ')+1, stringTree.get(i).length() ));
				leafEdges.add(new LeafEdge(id,length));
			}
		}
		this.setInnerEdges(innerEdges);
		this.setLeafEdges(leafEdges);
	}*/
	
	
	// constructor from Newick, where the root has label 0 and always is the last leaf in the string
			public Tree(String newick) {
				ArrayList<LeafEdge> leafEdges = new ArrayList<LeafEdge>();
				ArrayList<InnerEdge> innerEdges = new ArrayList<InnerEdge>();
				int i=newick.indexOf('('); 
				
				while (i<newick.length()) {
					int id=0;
					double length =0;
					
					switch(newick.charAt(i)) {
					case '(': 
						if ( i==newick.indexOf('(') ) {  // if the first '(' is associated with the root and not with an inner edge
							i++;
						} else {
							
							ArrayList<LeafEdge> auxSplit = new ArrayList<LeafEdge>();
							ArrayList<LeafEdge> auxCosplit = new ArrayList<LeafEdge>();
							InnerEdge auxInnerEdge = new InnerEdge(auxSplit,auxCosplit,-1);
							innerEdges.add(auxInnerEdge);
							i++;
						}
						
						break;
					
					case ')':
						i=i+2;
						int j=0;
						j=TrAP.nextIndexOf(newick,";,)",i);
						length = Double.parseDouble(newick.substring(i,j));
						
						for (int r=innerEdges.size()-1; r>-1; r--) {
							if (innerEdges.get(r).getLength()==-1) {
								innerEdges.get(r).setLength(length);
								
								break;
							}
						}
					    i=j;
						break;
					
					case ',':
						i++;
						break;
					
					default:
						
						id = Integer.parseInt(newick.substring(i,newick.indexOf(':',i)));
					    length = Double.parseDouble(newick.substring(newick.indexOf(':',i)+1, TrAP.nextIndexOf(newick,";,)",i)  ));
						
						LeafEdge auxLeafEdge = new LeafEdge(id,length);
						leafEdges.add(auxLeafEdge);
						if (id==0) {
							for (int r=0; r<innerEdges.size(); r++) {
								for (int q=0; q< leafEdges.size(); q++) {
									
									if (!innerEdges.get(r).isIDinCosplit(leafEdges.get(q).getID())) {
										
										innerEdges.get(r).getSplit().add(leafEdges.get(q));
									}
									
								}
							}
							i=newick.length();
						} else {
							for (int r=0; r<innerEdges.size(); r++) {
								if (innerEdges.get(r).getLength()==-1) {
									innerEdges.get(r).getCosplit().add(auxLeafEdge);
								}
							}
							i=TrAP.nextIndexOf(newick,";,)",i);
						}
						
						
						
					}  //switch
					
				} // while
				
				this.setInnerEdges(innerEdges);
				this.setLeafEdges(leafEdges);
					
			}
		
	
	// constructor from Newick which adds the root (root will have no. 0 and length 1)
		/*public Tree(String newick) {
			ArrayList<LeafEdge> leafEdges = new ArrayList<LeafEdge>();
			ArrayList<InnerEdge> innerEdges = new ArrayList<InnerEdge>();
			int i=newick.indexOf('('); 
			
			while (i<newick.length()) {
				int id=0;
				double length =0;
				
				switch(newick.charAt(i)) {
				case '(': 
					if ( i==newick.indexOf('(') ) {  // if the first '(' is associated with the root and not with an inner edge
						i++;
					} else {
						
						ArrayList<LeafEdge> auxSplit = new ArrayList<LeafEdge>();
						ArrayList<LeafEdge> auxCosplit = new ArrayList<LeafEdge>();
						InnerEdge auxInnerEdge = new InnerEdge(auxSplit,auxCosplit,-1);
						innerEdges.add(auxInnerEdge);
						i++;
					}
					
					break;
				
				case ')':
					if (newick.charAt(i+1)==';') {
						
						LeafEdge root = new LeafEdge(0,1);
						leafEdges.add(root);
						
						
						for (int r=0; r<innerEdges.size(); r++) {
							for (int q=0; q< leafEdges.size(); q++) {
								
								if (!innerEdges.get(r).isIDinCosplit(leafEdges.get(q).getID())) {
									
									innerEdges.get(r).getSplit().add(leafEdges.get(q));
								}
								
							}
						}
						
						i=newick.length();
					} else {
						i=i+2;
						int j=0;
						j=Frechet.nextIndexOf(newick,";,)",i);
						length = Double.parseDouble(newick.substring(i,j));
						
						for (int r=innerEdges.size()-1; r>-1; r--) {
							if (innerEdges.get(r).getLength()==-1) {
								innerEdges.get(r).setLength(length);
								
								break;
							}
						}
					    i=j;
					    
					}
					break;
				
				case ',':
					i++;
					break;
				
				default:
					
					id = Integer.parseInt(newick.substring(i,newick.indexOf(':',i)));
				    length = Double.parseDouble(newick.substring(newick.indexOf(':',i)+1, Frechet.nextIndexOf(newick,";,)",i)  ));
					
					LeafEdge auxLeafEdge = new LeafEdge(id,length);
					leafEdges.add(auxLeafEdge);
					for (int r=0; r<innerEdges.size(); r++) {
						if (innerEdges.get(r).getLength()==-1) {
							innerEdges.get(r).getCosplit().add(auxLeafEdge);
						}
					}
					i=Frechet.nextIndexOf(newick,";,)",i);
					
				}  //switch
				
			} // while
			
			this.setInnerEdges(innerEdges);
			this.setLeafEdges(leafEdges);
				
		}
	*/
	
	
	// constructor from a single file containing a tree in our format (good for large trees)
	public Tree(File file) {
		
		ArrayList<LeafEdge> leafEdges = new ArrayList<LeafEdge>();
	    ArrayList<InnerEdge> innerEdges = new ArrayList<InnerEdge>();
	    
	    BufferedReader br = null;
	    
	    try {
	      
	      br = new BufferedReader(new FileReader(file));
	      
	      String line;
	      
	      while ( (line = br.readLine()) != null) {
	    	  
	    	  int id =0;
			  double length =0;
			  int counter =0;
			  String number;
	    	  StringTokenizer st = new StringTokenizer(line);
	    	  ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
			  ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
			  InnerEdge innerEdge = new InnerEdge(split, cosplit, length);
	    	  while (st.hasMoreTokens()) {
	    		  number = st.nextToken();
	    		  if (counter ==0) {
	    			  id = Integer.parseInt(number);
	    			  counter++;
	    			  
	    			  continue;
	    		  }
                  if (counter ==1) {
                	  
	    			  length = Double.parseDouble(number);
	    			  //System.out.println("length: " + length);
	    			  counter++;
	    			  
	    			  continue;
	    		  }
                  
                  counter++;
                  int idToSplit = Integer.parseInt(number);
                  for (int r=0; r< leafEdges.size(); r++) {
						if (idToSplit == leafEdges.get(r).getID()) {
							innerEdge.getSplit().add(leafEdges.get(r));
							break;
						}
				  }
	    	  }  
	    	  //System.out.println("counter: " + counter);
	    	  if (counter == 2) {
 				   leafEdges.add(new LeafEdge(id,length));
 				   
 			  }
 			  else {
 				  // set cosplit
  				  for (int q=0; q<leafEdges.size(); q++) {
  					  int auxID =0;
  					  auxID = leafEdges.get(q).getID();
  				      if (!innerEdge.isIDinSplit(auxID)) {
  					  innerEdge.getCosplit().add(leafEdges.get(q));
  					  }
  				  }
  				  // set length
  				//System.out.println("length again: " + length);
  				  innerEdge.setLength(length);
  				  if (innerEdge.getLength()>Graph.ZERO) {
  					  innerEdges.add(innerEdge);
  				  }
  			      
 			  }
	      }
       
	      br.close();
	     
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
	    this.setLeafEdges(leafEdges);
	    /*System.out.println("inner edges size: " + innerEdges.size());
	    for (int w=0; w< innerEdges.size(); w++) {
	    	System.out.println("inner edge length: " + innerEdges.get(w).getLength());
	    }*/
		this.setInnerEdges(innerEdges);
		
		
	}
	
	// getters/setters
	public ArrayList<InnerEdge> getInnerEdges() {
		return this.innerEdges;
	}
	
	public void setInnerEdges(ArrayList<InnerEdge> innerEdges) {
		this.innerEdges.clear();
		for (int i=0; i<innerEdges.size(); i++) {
			InnerEdge auxInnerEdge = new InnerEdge(innerEdges.get(i));
			if (auxInnerEdge.getLength()>Graph.ZERO) {
				this.innerEdges.add(auxInnerEdge);
			}
		}
	}
	
	public ArrayList<LeafEdge> getLeafEdges() {
		return this.leafEdges;
	}
	
	public void setLeafEdges(ArrayList<LeafEdge> leafEdges) {
		this.leafEdges.clear();
		for (int i=0; i< leafEdges.size(); i++) {
			this.leafEdges.add(leafEdges.get(i));
		}
	}
	
	public void setLeafEdgesWithNew(ArrayList<LeafEdge> leafEdges) {
		this.getLeafEdges().clear();
		for (int i=0; i< leafEdges.size(); i++) {
			this.getLeafEdges().add(new LeafEdge(leafEdges.get(i)));
		}
	}
	

	
	// print a tree
	public void print() {
		System.out.println("Leaf Edges:");
		for (int i=0; i< this.getLeafEdges().size(); i++) {
			System.out.println(this.getLeafEdges().get(i).getID() + "  " + this.getLeafEdges().get(i).getLength());
		}
		System.out.println();
		System.out.println("Inner Edges: " + this.getInnerEdges().size());
		System.out.println();
		
		for (int i=0; i< this.getInnerEdges().size(); i++) {
			System.out.print( "length:  " + this.getInnerEdges().get(i).getLength());
			System.out.println();
			System.out.print( "split:  ");
			for (int j=0; j<this.getInnerEdges().get(i).getSplit().size(); j++) {
				System.out.print( this.getInnerEdges().get(i).getSplit().get(j).getID() + " ");
			}
			System.out.println();
			System.out.print( "cosplit:  ");
			for (int j=0; j<this.getInnerEdges().get(i).getCosplit().size(); j++) {
				System.out.print( this.getInnerEdges().get(i).getCosplit().get(j).getID() + " ");
			}
			System.out.println();
			System.out.println();
		}
	}
	
	
	
	// convert a tree into a string of characters in Newick format
	public String convertToString() {
		
		StringBuffer treeInSB = new StringBuffer();
		ArrayList<String> atomsInString = new ArrayList<String>();
		ArrayList<LeafEdge> activeLeaf = new ArrayList<LeafEdge>();
		ArrayList<InnerEdge> activeInner = new ArrayList<InnerEdge>();
		ArrayList<InnerEdge> atoms = new ArrayList<InnerEdge>();
		double lengthOfRoot = 0;
		
		for (int i=0; i<this.getInnerEdges().size(); i++) {
			InnerEdge auxInnerEdge = new InnerEdge(this.getInnerEdges().get(i));
			activeInner.add(auxInnerEdge);
		}
		
		for (int i=0; i<this.getLeafEdges().size(); i++) {
			if (this.getLeafEdges().get(i).getID()!=0) {
				LeafEdge auxLeaveEdge = new LeafEdge(this.getLeafEdges().get(i));
				activeLeaf.add(auxLeaveEdge);
			} else {
				lengthOfRoot = this.getLeafEdges().get(i).getLength();
			}
		}
		
		while (!activeInner.isEmpty()) {
			for (int i=0; i<activeInner.size(); i++) {
				if (activeInner.get(i).isMinimalAmong(activeInner)) {
					StringBuffer auxsb = new StringBuffer();
					for (int j=0; j<atoms.size(); j++) {
						if (atoms.get(j).isBelow(activeInner.get(i))) {
							if (auxsb.length()==0) {
								auxsb.append(atomsInString.get(j));
							} else {
								auxsb.append("," + atomsInString.get(j));
							}
							atoms.remove(j);
							
							atomsInString.remove(j);
							
							j--;
						}
					}

					for (int r=0; r<activeLeaf.size(); r++) {
						
						if (activeInner.get(i).isIDinCosplit(activeLeaf.get(r).getID())) {
							if (auxsb.length()==0) {
								auxsb.append(String.valueOf(activeLeaf.get(r).getID()) + ":" + String.valueOf(activeLeaf.get(r).getLength()));

							} else {
								
								auxsb.append("," + String.valueOf(activeLeaf.get(r).getID()) + ":" + String.valueOf(activeLeaf.get(r).getLength()));
							}

							activeLeaf.remove(r);
							r--;
						}
					}
					
					atoms.add(activeInner.get(i));
					auxsb.insert(0,'(');
					auxsb.append("):" + String.valueOf(activeInner.get(i).getLength()));
					String auxString = auxsb.toString();
					atomsInString.add(auxString);
					activeInner.remove(i);
					i--;
				}
			}
		}
		
		for (int i=0; i<atoms.size();i++) {
			if (treeInSB.length()==0) {
				treeInSB.append(atomsInString.get(i));
			} else {
				treeInSB.append("," + atomsInString.get(i));
			}
		}
		
		for (int i=0; i<activeLeaf.size(); i++) {
			StringBuffer auxsb = new StringBuffer();
			auxsb.append(String.valueOf(activeLeaf.get(i).getID()));
			auxsb.append(":" + String.valueOf(activeLeaf.get(i).getLength()));
			treeInSB.append("," + auxsb);
		}
		treeInSB.insert(0, '(');
		treeInSB.append(",0:" + lengthOfRoot +");");
		
		
		String treeInString = treeInSB.toString();
		return treeInString;
	}
	
	// write a tree to a file in the Newick format
	public void writeToFileNewick(File file) {
		
		String treeInString = this.convertToString();
		 try{
			    // Create file 
			    FileWriter fstream = new FileWriter(file);
			    BufferedWriter out = new BufferedWriter(fstream);
			    
			    out.write(treeInString);
			    
			    //Close the output stream
			    out.close();
			    }catch (Exception e){ //Catch exception if any
			      System.err.println("Error: " + e.getMessage());
			    }
	}	
	
	
	
	
	
	// write a tree to a file
	public void writeToFile(File file) {
		 try{
			    // Create file 
			    FileWriter fstream = new FileWriter(file);
			    BufferedWriter out = new BufferedWriter(fstream);
			    
			    for (int l=0; l< this.getLeafEdges().size(); l++) {
			    	out.write(this.getLeafEdges().get(l).getID() + " " + this.getLeafEdges().get(l).getLength() + '\n');
			    }
			    int innerID = this.getLeafEdges().size();
			    for (int i=0; i< this.getInnerEdges().size(); i++) {
			    	out.write(innerID + " " + this.getInnerEdges().get(i).getLength());
			    	for (int s=0; s<this.getInnerEdges().get(i).getSplit().size(); s++) {
			    		out.write(" " + this.getInnerEdges().get(i).getSplit().get(s).getID());
			    	}
			    	out.write('\n');
			    	innerID++;
			    }
			    
			    
			    
			    //Close the output stream
			    out.close();
			    }catch (Exception e){//Catch exception if any
			      System.err.println("Error: " + e.getMessage());
			    }
	}	
	
	// finds the length of shortest leaf edge
	public double shortestLeafEdge() {
		double auxLength=this.getLeafEdges().get(0).getLength();
		for (int i=0; i<this.getLeafEdges().size(); i++) {
			if (this.getLeafEdges().get(i).getLength()<auxLength) {
				auxLength = this.getLeafEdges().get(i).getLength();
			}
		}
		return auxLength;
	}
	
	// finds the length of shortest inner edge
	public double shortestInnerEdge() {
		double auxLength=this.getInnerEdges().get(0).getLength();
		for (int i=0; i<this.getInnerEdges().size(); i++) {
			if (this.getInnerEdges().get(i).getLength()<auxLength) {
				auxLength = this.getInnerEdges().get(i).getLength();
			}
		}
		return auxLength;
	}
	
	// computes norm squared of a given set of inner edges
	public double norm2OfInnerEdges() {
		double norm2 = 0;
		for (int i = 0 ; i < this.getInnerEdges().size(); i++) {
			norm2 += (this.getInnerEdges().get(i).getLength() * this.getInnerEdges().get(i).getLength() );
		}
		return norm2;
	}
	
	
	// extract a subtree from a given tree below a given inner edge
	private Tree giveSubtree(InnerEdge e) {
		
		Tree auxTree = new Tree(this);
		
		// changes leaf edges of the tree
		auxTree.setLeafEdges(e.getSplit());
		LeafEdge auxLeafEdge = new LeafEdge(e.smallestIDinCosplit(),e.getLength());
		auxTree.getLeafEdges().add(auxLeafEdge);
		
		// removes inner edges
		for (int i=0; i < auxTree.innerEdges.size(); i++) {
			int n = 0;
			for (int j=0; j < auxTree.innerEdges.get(i).getCosplit().size(); j++ ) {
				if ( e.getCosplit().contains( auxTree.innerEdges.get(i).getCosplit().get(j)) ) {
					n++;
				}
			};
			if (n == auxTree.innerEdges.get(i).getCosplit().size()) {
				auxTree.innerEdges.remove(i);
				i--;
			}
			else { // we don't remove this edge and need to find out whether it contains e.getCosplit().get(0) in split or cosplit in order to know where to put auxLeafEdge
				if (auxTree.getInnerEdges().get(i).getSplit().contains(e.getCosplit().get(0))) {
					auxTree.getInnerEdges().get(i).getSplit().add(auxLeafEdge);
				}
				else {
					auxTree.getInnerEdges().get(i).getCosplit().add(auxLeafEdge);
				}
			}
		}
		
		// removes leaf edges from splits/cosplits of the remaining inner edges
		for (int i=0; i < auxTree.innerEdges.size(); i++) {
			for (int j=0; j < e.getCosplit().size(); j++) {
				auxTree.innerEdges.get(i).getSplit().remove(e.getCosplit().get(j));
				auxTree.innerEdges.get(i).getCosplit().remove(e.getCosplit().get(j));
			}
		}
		return auxTree;
	}
	
	// extracts a cosubtree of a given tree below a given inner edge
	private Tree giveCosubtree(InnerEdge e) {
		
		Tree auxTree = new Tree(this);
		
		// changes leaf edges of the tree
		auxTree.setLeafEdges(e.getCosplit());
		LeafEdge auxLeafEdge = new LeafEdge(0,e.getLength());  // creates a new root
		auxTree.leafEdges.add(auxLeafEdge); // adds the new root
		
		// removes inner edges
		for (int i=0; i < auxTree.innerEdges.size(); i++) {
			int n = 0;
			for (int j=0; j < auxTree.innerEdges.get(i).getCosplit().size(); j++ ) {
				if ( e.getSplit().contains( auxTree.innerEdges.get(i).getCosplit().get(j)) ) {
					n++;
				}
			};
			if (n == auxTree.innerEdges.get(i).getCosplit().size()) {
				auxTree.innerEdges.remove(i);
				i--;
			}
		}
		
		for (int i=0; i < auxTree.innerEdges.size(); i++) {
			int n = 0;
			for (int j=0; j < auxTree.innerEdges.get(i).getSplit().size(); j++ ) {
				if ( e.getSplit().contains( auxTree.innerEdges.get(i).getSplit().get(j)) ) {
					n++;
				}
			};
			if (n == auxTree.innerEdges.get(i).getSplit().size()) {
				auxTree.innerEdges.remove(i);
				i--;
			}
		}
		
		// removes leaf edges from splits/cosplits of the remaining inner edges
		for (int i=0; i < auxTree.innerEdges.size(); i++) {
			for (int j=0; j < e.getSplit().size(); j++) {
				auxTree.innerEdges.get(i).getSplit().remove(e.getSplit().get(j));
			}
			auxTree.innerEdges.get(i).getSplit().add(auxLeafEdge);
		}
		
		return auxTree;
	}
	
	
	// check whether inner edges of a given tree contain a given inner edge
	public boolean isInInnerEdges(InnerEdge innerEdge) {
		for (int i=0; i < this.getInnerEdges().size(); i++) {
			if (this.getInnerEdges().get(i).isIdenticalWith(innerEdge)) {
				return true;
			}
		}
		return false;
	}
	
	// compares a pair of inner edges, checks whether they are identical, i.e. have the same split (and hence cosplit)
	public static boolean isSameEdge(InnerEdge e, InnerEdge f) {
		if (e.getSplit().size() != f.getSplit().size()) {
			return false;
		}
		int n=0;
		for (int i=0; i < e.getSplit().size(); i++) {
			if (f.isIDinSplit(e.getSplit().get(i).getID())) {
				n++;
			}
		};
		if (n==e.getSplit().size()) {
			return true;
		}
		return false;
	}
	
	
	// find indices of a common edge in a given pair of trees, if no common edge, returns -1,-1
	public static int[] findCommonEdge(Tree s, Tree t) {
		for (int i=0; i<s.getInnerEdges().size(); i++) {
			for (int j=0; j<t.getInnerEdges().size(); j++) {
				if (Tree.isSameEdge(s.getInnerEdges().get(i), t.getInnerEdges().get(j))) {
					int[] tmp = {i,j};
					return tmp;
				}
			}
		}
		int[] tmp = {-1,-1};
		return tmp;
	}
	
	
	
	// find indices of redundant edges in a given tree, if no redundant edge, returns -1,-1  ... only for testing purposes
	public static int[] findRedundantEdge(Tree s) {
		for (int i=0; i<s.getInnerEdges().size(); i++) {
			for (int j=0; j<s.getInnerEdges().size(); j++) {
				if (Tree.isSameEdge(s.getInnerEdges().get(i), s.getInnerEdges().get(j)) && i!=j) {
					int[] tmp = {i,j};
					return tmp;
				}
			}
		}
		int[] tmp = {-1,-1};
		return tmp;
	}

	// check whether a tree has mutually compatible inner edges  ... only for testing purposes
	public boolean hasCompatibleEdges() {
		if (Tree.areCompatible(this.getInnerEdges(),this.getInnerEdges())) {
			return true;
		}
		return false;
	}
	
	// check whether a tree has correct splits/cosplits wrt leaf edges ... only for testing purposes
	public boolean selfTest(){
		int counter2 =0;
		for (int l=0; l<this.getLeafEdges().size(); l++) {
			int counter1 =0;
			for (int i=0; i<this.getInnerEdges().size(); i++) {
				int counter =0;
				if (this.getInnerEdges().get(i).getSplit().contains(this.getLeafEdges().get(l))) {
					counter++;
				}
				if (this.getInnerEdges().get(i).getCosplit().contains(this.getLeafEdges().get(l))) {
					counter++;
				}
				if (counter !=1) {
					return false;
				}
				else {
					counter1++;
				}
			}
			if (counter1 != this.getInnerEdges().size()) {
				return false;
			}
			else {
				counter2++;
			}
		}
		if (counter2 !=this.getLeafEdges().size()) {
			return false;
		}
		return true;
	}
	
	// checks whether two inner edges are compatible ... TODO test for two edges from one tree, which must be always compatible
	public static boolean areCompatible(InnerEdge e, InnerEdge f) {
		
		int n=0;
		for (int i=0; i<e.getCosplit().size(); i++) {
		    if (f.isIDinSplit(e.getCosplit().get(i).getID())) {
		    	n++;
		    }
		}
		if (n==0) {
			return true;
		}
	
		int m=0;
		for (int i=0; i<f.getCosplit().size(); i++) {
		    if (e.isIDinSplit(f.getCosplit().get(i).getID())) {
		    	m++;
		    }
		}
		if (m==0) {
			return true;
		}
	
		int k=0;
		for (int i=0; i<e.getCosplit().size(); i++) {
		    if (f.isIDinCosplit(e.getCosplit().get(i).getID())) {
		    	k++;
		    }
		}
		if (k==0) {
			return true;
		}
		
		return false;
	}
	
	// checks whether two sets of edges are compatible
	public static boolean areCompatible(ArrayList<InnerEdge> E, ArrayList<InnerEdge> F) {
		for (int i =0; i < E.size(); i++) {
			for (int j =0; j< F.size(); j++) {
				if (!areCompatible(E.get(i),F.get(j))) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	
	// gives length of the leaf edge with ID index, if there is no leaf edge with this ID, gives -1
	public double getLengthOfID(int index) {
		for (int i=0 ; i< this.getLeafEdges().size(); i++) {
			if (this.getLeafEdges().get(i).getID() == index) {
				return this.getLeafEdges().get(i).getLength();
			}
		}
		return -1;
	}
	public LeafEdge getLeafEdgeOfID(int index) {
		for (int i=0; i< this.getLeafEdges().size(); i++) {
			if (this.getLeafEdges().get(i).getID() == index) {
				return this.getLeafEdges().get(i);
			}
		}
		return null;
	}
	
	// computes the contribution to the distance (squared) due to inner edges 
	public static double innerEdgesDist2(Tree s, Tree t) {
	
		Tree copyOfs = new Tree(s);
		Tree copyOft = new Tree(t);
		
	    double distance2 =0;
	    int i = Tree.findCommonEdge(copyOfs,copyOft)[0];
	    int j = Tree.findCommonEdge(copyOfs,copyOft)[1];
	    
	    if (i==-1) {  
	    	
	    	// checks whether the tree have a common edge, if not (i.e. if i == -1) it calls distOfDistjoint2
	    	
	    	//System.out.println("call for disjoint case of inner edges distance");
	    	
	    	//copyOfs.print();
	    	//copyOft.print();
	    	
	    	
	    	return Tree.distOfDisjoint2(copyOfs,copyOft);
	    }
	    else {
	    	// else it computes the contribution from the common edge itself, and recursively calls this method for subtrees
	    	
	    	//System.out.println("call recursively inner edges distance");
	    	
			distance2 = Math.pow(copyOfs.getInnerEdges().get(i).getLength()-copyOft.getInnerEdges().get(j).getLength(),2);
			
			
			
			distance2 += innerEdgesDist2(copyOfs.giveSubtree(copyOfs.getInnerEdges().get(i)),copyOft.giveSubtree(copyOft.getInnerEdges().get(j)) );
			
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
	public static double distOfDisjoint2(Tree s, Tree t) {
		double distOfDisjoint2 =0;
	
		if (!Tests.areComparable(s,t)) {
			System.out.println("Trees not comparable!");
			System.out.println("s.leafs.size= " + s.getLeafEdges().size());
			System.out.println("t.leafs.size= " + s.getLeafEdges().size());
			for (int i=0; i< s.getLeafEdges().size(); i++) {
				System.out.println("s. leaf. id: " + s.getLeafEdges().get(i).getID());
			}
			for (int i=0; i< t.getLeafEdges().size(); i++) {
				System.out.println("t. leaf. id: " + t.getLeafEdges().get(i).getID());
			}
			System.exit(0);
		}
		
		// check whether both trees have inner edges
		if (s.getInnerEdges().isEmpty()) {
			for (int i=0; i < t.getInnerEdges().size(); i++) {
				distOfDisjoint2 += t.getInnerEdges().get(i).getLength() * t.getInnerEdges().get(i).getLength(); 
			}
		}
		else {
			if (t.getInnerEdges().isEmpty()) {
				for (int i=0; i < s.getInnerEdges().size(); i++) {
					distOfDisjoint2 += s.getInnerEdges().get(i).getLength() * s.getInnerEdges().get(i).getLength(); 
				}
			}
			else {
				Path path = new Path(s,t);
				
				//System.out.println("new path created of size: " + path.getSequence().size());
				
				int n = 0;
				do {
					n = 0;
					
					for (int i = 0; i < path.getSequence().size(); i++) {
						
						
						//System.out.println("starting extesion problem");
						boolean myBool = path.extensionProblem(i);
						//System.out.println("extension problem: " + myBool);
						if (myBool) {
							
							//i++; // should not matter
							n++;
						}
						
					}
					
				} while (n > 0);
				
				/*System.out.println("sequence of ratios of size: " + path.getSequence().size());
				for (int i = 0; i < path.getSequence().size(); i++) {
					System.out.println(i + ": " + path.getSequence().get(i).getRatio() );
				}*/
				
				
				
				for (int i = 0; i < path.getSequence().size(); i++) {
					
					// double aux1 =0, aux2 =0;
					
					distOfDisjoint2 += Math.pow(path.getSequence().get(i).getNumerator() + path.getSequence().get(i).getDenominator(),2);
					
					/*for (int j = 0; j < path.getSequence().get(i).getVerticesL().size(); j++) {
						aux1 += path.getSequence().get(i).getVerticesL().get(j).getOriginalWeight();
						//aux1 += Math.pow(s.innerEdges.get(path.getSequence().get(i).getVerticesL().get(j).getID()).getLength(),2);
					}
					
					for (int j = 0; j < path.getSequence().get(i).getVerticesR().size(); j++) {
						aux2 += path.getSequence().get(i).getVerticesR().get(j).getOriginalWeight();
						//aux2 += Math.pow(t.innerEdges.get(path.getSequence().get(i).getVerticesR().get(j).getID()).getLength(),2);
					}
				
					distOfDisjoint2 += aux1 + 2 * Math.sqrt(aux1*aux2) + aux2;*/
				}
				
			}
		}		
		
		
		return distOfDisjoint2;
	}
	
	
	
	// computes the total distance between a given pair of trees
	public static double totalDist(Tree s, Tree t) {
		double innerDist2 =0, leafDist2=0;
		
		// contribution of leaf edges squared
		for (int i=0; i < s.leafEdges.size(); i++) {
			int auxID = s.leafEdges.get(i).getID();
			leafDist2 += Math.pow(s.getLengthOfID(auxID)-t.getLengthOfID(auxID),2);
		}
		
		// contribution of inner edges squared
		innerDist2 = innerEdgesDist2(s,t);
				
		//System.out.println("inner edges contribution computed");
		
		
	    return Math.sqrt(innerDist2 + leafDist2);
		
	}
	
	// finds the tree between a given pair of DISJOINT trees with 'fraction' being the parameter of convex combination 
	private static Tree treeOnGeodesicForDisjoint(Tree s, Tree t, double fraction) {
		
		
		
		if (fraction == 1) {
			return new Tree(t);
		}
		
		Path path = new Path(s,t);
		int n = 0;
		// form Path.sequence
		do {
			n = 0;
			for (int i = 0; i < path.getSequence().size(); i++) {
				
				//System.out.println("path sequence has size: " +path.getSequence().size());
				//System.out.println("its " +i+ "th element has "+path.getSequence().get(i).getVerticesL().size() + " verticesL, and " + path.getSequence().get(i).getVerticesR().size() + " verticesR");
				//System.out.println("ratio of this graph is: "+path.getSequence().get(i).getRatio());
				boolean myBool = path.extensionProblem(i);
				//System.out.println("extension problem returns: " + myBool);
				if (myBool) {
				
					i++;
					n++;
				}
			
			}
		} while (n > 0);
		
		/*for (int f=0; f<path.getSequence().size(); f++) {
			System.out.println("ratio sequence: " + path.getSequence().get(f).getRatio() + " of size: " + path.getSequence().size());
		}*/
		
		
		
		double auxFraction = fraction / (1-fraction);
		int segmentNumber =0;

		// identify in which segment of the geodesic the graph lies
		for (int i = 0; i < path.getSequence().size(); i++) {
			if (auxFraction > path.getSequence().get(i).getRatio()  ) {
				segmentNumber = (i+1);
			}
			else {
				break;
			}
		}
		
		//System.out.println("segment number: " + segmentNumber);

		
		ArrayList<InnerEdge> auxInnerEdges = new ArrayList<InnerEdge>();
		ArrayList<LeafEdge> auxLeafEdges = new ArrayList<LeafEdge>();
				
		for (int y=0; y < s.getLeafEdges().size(); y++) {
			auxLeafEdges.add(s.getLeafEdges().get(y));
		}
			
		if (segmentNumber == 0) { // in this case the tree lies in the same orthant as 's'
			//System.out.println("number of inner edges of the original tree: " + s.getInnerEdges().size());
            
			for (int r=0; r < path.getSequence().size(); r++) {
				
				for (int q=0; q < path.getSequence().get(r).getVerticesL().size(); q++) {
					
					
					InnerEdge auxInnerEdge = new InnerEdge(path.getOrigin().getInnerEdges().get(path.getSequence().get(r).getVerticesL().get(q).getID()));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength( (1-fraction -(fraction * path.getSequence().get(r).getDenominator())/ path.getSequence().get(r).getNumerator()) * auxInnerEdge.getLength() );
					
					//System.out.println("index in sequence/sequence size: " + r + " / " + path.getSequence().size()); 
					//System.out.println("auxInnerEdge length: " + auxInnerEdge.getLength());
					
					if (auxInnerEdge.getLength()<=Graph.ZERO) {
						//System.out.println("!0! LENGTH: " + auxInnerEdge.getLength());
					}
					else {
						auxInnerEdges.add( auxInnerEdge );
						//System.out.println("new inner edge added, length: " + auxInnerEdge.getLength());
						//System.out.println("current number of inner edges: " + auxInnerEdges.size());
					}
				}
			}
				
			return new Tree(auxInnerEdges,auxLeafEdges);
		}
		
		if (segmentNumber == path.getSequence().size()) {  // in this case the tree lies in the same orthant as 't'
			
			for (int r=0; r < path.getSequence().size(); r++) {
				for (int q=0; q < path.getSequence().get(r).getVerticesR().size(); q++) {
					
					InnerEdge auxInnerEdge = new InnerEdge(path.getHead().getInnerEdges().get(path.getSequence().get(r).getVerticesR().get(q).getID()));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength( (fraction -(1-fraction) * path.getSequence().get(r).getRatio())  * auxInnerEdge.getLength() );
					
					if (auxInnerEdge.getLength()<=Graph.ZERO) {
						//System.out.println("!1! LENGTH: " + auxInnerEdge.getLength());
					}
					else {
						auxInnerEdges.add( auxInnerEdge );
					}
				}
			}

		}
		else {  // this is a general case in which we collect inner edges from both trees s and t
			
			// collect inner edges coming from the tree t
			for (int i=0 ; i < segmentNumber; i++) {
				
				int auxSize = path.getSequence().get(i).getVerticesR().size();
				for (int m=0; m< auxSize; m++) {
					int auxIndex = path.getSequence().get(i).getVerticesR().get(m).getID();
					InnerEdge auxInnerEdge = new InnerEdge(path.getHead().getInnerEdges().get(auxIndex));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength( (fraction -(1-fraction) * path.getSequence().get(i).getRatio())  * auxInnerEdge.getLength() );
					
					
					if (auxInnerEdge.getLength()<=Graph.ZERO) {
						//System.out.println("!2! LENGTH: " + auxInnerEdge.getLength());
					}
					
					else {
						auxInnerEdges.add( auxInnerEdge );
					}
				}
			}
			
			// collect inner edges coming from the tree s
			for (int i= segmentNumber; i< path.getSequence().size(); i++) {
				int auxSize = path.getSequence().get(i).getVerticesL().size();
				for (int m=0; m< auxSize; m++) {
					int auxIndex = path.getSequence().get(i).getVerticesL().get(m).getID();
					InnerEdge auxInnerEdge = new InnerEdge(path.getOrigin().getInnerEdges().get(auxIndex));
					// set the length of auxInnerEdge
					auxInnerEdge.setLength( (1-fraction -(fraction * path.getSequence().get(i).getDenominator())/ path.getSequence().get(i).getNumerator()) * auxInnerEdge.getLength() );
					
					if (auxInnerEdge.getLength()<=Graph.ZERO) {
						//System.out.println("!3! LENGTH: " + auxInnerEdge.getLength());
					}
					else {
						auxInnerEdges.add( auxInnerEdge );
					}
					
				}
			}						
		}
		
		return new Tree(auxInnerEdges,auxLeafEdges);
	}
	
	// replaces leaf edges in order to set their lengths
	private void updateLeafEdges(Tree s, Tree t, double fraction) {
		// set leaf edges
	    ArrayList<LeafEdge> auxLeafEdges = new ArrayList<LeafEdge>();
	    for (int q=0; q< this.getLeafEdges().size(); q++) {
	    	auxLeafEdges.add(this.getLeafEdges().get(q));
	    }
	    
	    
	    this.setLeafEdgesWithNew(auxLeafEdges);
	    
	    
	    // set the lengths of leaf edges
	    for (int q=0 ; q< this.getLeafEdges().size(); q++) {
	    	this.getLeafEdges().get(q).setLength( (1-fraction) * s.getLengthOfID(this.getLeafEdges().get(q).getID()) + fraction * t.getLengthOfID(this.getLeafEdges().get(q).getID()) );
 		}	
	    
	    // set splits, and cosplits
	    for (int r=0; r<this.getInnerEdges().size(); r++) {
	    	ArrayList<LeafEdge> auxSplit = new ArrayList<LeafEdge>();
	    	ArrayList<LeafEdge> auxCosplit = new ArrayList<LeafEdge>();
	    	for (int i=0; i<this.getInnerEdges().get(r).getSplit().size(); i++) {
	    		auxSplit.add(this.getInnerEdges().get(r).getSplit().get(i));
	    	}
	    	for (int i=0; i<this.getInnerEdges().get(r).getCosplit().size(); i++) {
	    		auxCosplit.add(this.getInnerEdges().get(r).getCosplit().get(i));
	    	}
	    	
	    	this.getInnerEdges().get(r).getSplit().clear();
	    	this.getInnerEdges().get(r).getCosplit().clear();
	    	for (int p=0; p < auxSplit.size(); p++) {
	    		this.getInnerEdges().get(r).getSplit().add(this.getLeafEdgeOfID(auxSplit.get(p).getID()));
	    	}
	    	for (int p=0; p < auxCosplit.size(); p++) {
	    		this.getInnerEdges().get(r).getCosplit().add(this.getLeafEdgeOfID(auxCosplit.get(p).getID()));
	    	}
	    }
	}
	
	// finds the tree between a given pair of trees with 'fraction' being the parameter of convex combination 
	public static Tree treeOnGeodesic(Tree s, Tree t, double fraction) {
		
	
		Tree copyOfs = new Tree(s);
		Tree copyOft = new Tree(t);
		
		Tree auxTree;
		
		if (copyOfs.getInnerEdges().isEmpty()) {
			
			auxTree = new Tree(t);
			for (int i=0; i < auxTree.getInnerEdges().size(); i++) {
				auxTree.getInnerEdges().get(i).setLength( fraction * copyOft.getInnerEdges().get(i).getLength() );
			}
			auxTree.updateLeafEdges(copyOfs,copyOft,fraction);
			
			return auxTree;
		}
		
		if (copyOft.getInnerEdges().isEmpty()) {
			
			auxTree = new Tree(copyOfs);
			for (int i=0; i < auxTree.getInnerEdges().size(); i++) {
				auxTree.getInnerEdges().get(i).setLength( (1-fraction) * copyOfs.getInnerEdges().get(i).getLength() );
			}
			auxTree.updateLeafEdges(copyOfs,copyOft,fraction);
			
			return auxTree;
		}
		
		
	    int i = Tree.findCommonEdge(copyOfs,copyOft)[0];
	    int j = Tree.findCommonEdge(copyOfs,copyOft)[1];
	    
	    if (i==-1) {  // checks whether the tree have a common edge, if not (i.e. if i == -1) it calls treeOnGeodesicForDisjoint 
	    	
	    	auxTree = new Tree(Tree.treeOnGeodesicForDisjoint(copyOfs,copyOft,fraction));
	    }
	    else { // else it computes the combination of the common edge itself, and recursively calls this method for subtrees
	    	
	    	// collect inner edges now, set their lengths, splits, and cosplits, and lengths of edges in splits and cosplits
	    	ArrayList<InnerEdge> auxInnerEdges = new ArrayList<InnerEdge>();
	    	InnerEdge auxInnerEdge = new InnerEdge(copyOfs.getInnerEdges().get(i));  // = t.getInnerEdges().get(j) ... not true: they have distinct leaf edges in split/cosplit
	    	// set new length of this edge itself
	    	auxInnerEdge.setLength( (1-fraction) * copyOfs.getInnerEdges().get(i).getLength() + fraction * copyOft.getInnerEdges().get(j).getLength() );
			
	    	
			auxInnerEdges.add(auxInnerEdge);
			
			Tree auxTree1 = Tree.treeOnGeodesic(copyOfs.giveSubtree(copyOfs.innerEdges.get(i)), copyOft.giveSubtree(copyOft.innerEdges.get(j)), fraction);
			Tree auxTree2 = Tree.treeOnGeodesic(copyOfs.giveCosubtree(copyOfs.innerEdges.get(i)), copyOft.giveCosubtree(copyOft.innerEdges.get(j)), fraction);
		
			for (int l=0; l < auxTree1.getInnerEdges().size(); l++) {
				InnerEdge auxInnerEdge2 = new InnerEdge(auxTree1.getInnerEdges().get(l));
				
				int auxInt = copyOfs.getInnerEdges().get(i).smallestIDinCosplit(); // auxiliary leaf edge
				
				
				// we need to find out whether this auxiliary leaf edge lies in split or cosplit of the current inner edge
				if (auxInnerEdge2.isIDinSplit(auxInt)) {
					
					
					for (int m=0; m < auxTree2.getLeafEdges().size(); m++) {
						if ( ! auxInnerEdge2.isIDinSplit(auxTree2.getLeafEdges().get(m).getID())) {
							auxInnerEdge2.getSplit().add( auxTree2.getLeafEdges().get(m) );
						}

					}
				}
				else {
					
					for (int m=0; m < auxTree2.getLeafEdges().size(); m++) {
						if ( (! auxInnerEdge2.isIDinCosplit(auxTree2.getLeafEdges().get(m).getID()) ) && auxTree2.getLeafEdges().get(m).getID() !=0) {
							auxInnerEdge2.getCosplit().add( auxTree2.getLeafEdges().get(m) );
						}
					}
				}
				
			
				
				int auxCounter=0;
				for (int z=0; z < auxInnerEdges.size(); z++) {
					if (auxInnerEdges.get(z).isIdenticalWith(auxInnerEdge2)) {
						auxCounter++;
					}
				}
				if (auxCounter == 0) {
					auxInnerEdges.add(auxInnerEdge2);
				}
				
				
			}
			for (int l=0; l < auxTree2.getInnerEdges().size(); l++) {
				
                InnerEdge auxInnerEdge3 = new InnerEdge(auxTree2.getInnerEdges().get(l));
                
				for (int m=0; m < auxTree1.getLeafEdges().size(); m++) {
					if ( (! auxInnerEdge3.isIDinSplit(auxTree1.getLeafEdges().get(m).getID()))  &&  (! auxInnerEdge3.isIDinCosplit(auxTree1.getLeafEdges().get(m).getID()))) {
						
						auxInnerEdge3.getSplit().add( auxTree1.getLeafEdges().get(m) );
					}
					
				}
				
				auxInnerEdges.add(auxInnerEdge3);
			}

			auxTree = new Tree(auxInnerEdges,copyOfs.getLeafEdges());
			
		}
	    
	    auxTree.updateLeafEdges(copyOfs, copyOft, fraction);
	    return auxTree;
	}
	
	// computes the Frechet mean of a given set of trees, uniform distribution (all weights are 1/N). Based on the Law of large numbers.
	public static Tree meanViaLLN(ArrayList<Tree> setOfTrees, int numberOfIterations) {
		//ArrayList<Tree> solution = new ArrayList<Tree>();
		//solution.add(new Tree(setOfTrees.get(0)));
		Tree solution = new Tree(setOfTrees.get(0));
		Random generator = new Random();
		int randomIndex =0;
		double coef =0;
		for (int i=1; i <= numberOfIterations; i++) {
			
			randomIndex = generator.nextInt(setOfTrees.size());
			
			double iAsDouble = (double) i;
			coef = 1/(iAsDouble+1);
			
			solution = treeOnGeodesic(solution , setOfTrees.get(randomIndex),coef) ;
			
			//solution.add( treeOnGeodesic(solution.get(i-1) , setOfTrees.get(randomIndex),coef)) ;
			
			/*
			System.out.println("random index: " + randomIndex);
			System.out.println("coef: " + coef);*/
		}
		 
		return solution;
		//return solution.get(solution.size()-1);
	}
	
	// computes the Frechet mean of a given set of trees, with given distribution. Based on the Law of large numbers.
		public static Tree meanViaLLN(ArrayList<Tree> setOfTrees, double[] probabilities, int numberOfIterations) {
			
			
			// creating array of partial sums of probabilities
			double auxSum=0;
			double[] partialSumsOfProb = new double[probabilities.length];
			for (int i=0; i<probabilities.length-1; i++) {
				auxSum = auxSum + probabilities[i];
				partialSumsOfProb[i]= auxSum;
			}			
			partialSumsOfProb[probabilities.length-1]=1;  // set the last element manually because of possible rounding errors 			
						
						
			Tree solution = new Tree(setOfTrees.get(0));
			Random generator = new Random();
			
			double coef =0;
			for (int i=1; i <= numberOfIterations; i++) {
				
				// choosing randomly a tree
				int randomIndex=0;
				double randomNumber =0;
				randomNumber = generator.nextDouble();
				while (randomNumber > partialSumsOfProb[randomIndex]) {
					randomIndex++;
				}
				
				double iAsDouble = (double) i;
				coef = 1/(iAsDouble+1);
				
				solution = treeOnGeodesic(solution , setOfTrees.get(randomIndex),coef) ;
				
			}
			 
			return solution;
		}
		
		
		
		
	
		// computes the Frechet mean of a given set of trees, uniform distribution (all weights are 1/N). Based on the Proximal point algorithm. Random order of the trees.
				public static Tree meanViaPPARandom(ArrayList<Tree> setOfTrees, int numberOfIterations) {
					Tree solution = new Tree(setOfTrees.get(0));
					Random generator = new Random();
					int randomIndex =0;
					double coef =0;
					for (int i=1; i <= numberOfIterations; i++) {
						
						randomIndex = generator.nextInt(setOfTrees.size());
						
						double iAsDouble = (double) i;
						coef = 2/(iAsDouble+2);
						
						solution = treeOnGeodesic(solution , setOfTrees.get(randomIndex),coef) ;
					}
					 
					return solution;
				}
		
		// computes the Frechet mean of a given set of trees, with uniform distribution. Based on the Proximal point algorithm. Cyclic order of the trees.
				public static Tree meanViaPPACyclic(ArrayList<Tree> setOfTrees, int numberOfIterations) {
					Tree solution = new Tree(setOfTrees.get(0));
					double coef =0;
					for (int i=1; i <= numberOfIterations; i++) {
						double iAsDouble = (double) i;
						coef = 2/(iAsDouble+2);
						for (int j=0; j< setOfTrees.size(); j++) {
							solution = treeOnGeodesic(solution , setOfTrees.get(j),coef) ;	
						}	
					}
					return solution;
				}
	
				
			
				// computes median of a given set of trees, uniform distribution (all weights are 1/N). Based on the Proximal point algorithm. Random order of the trees.
						public static Tree medianRandom(ArrayList<Tree> setOfTrees, int numberOfIterations) {
							Tree solution = new Tree(setOfTrees.get(0));
							Random generator = new Random();
							int randomIndex =0;
							double coef =0;
							for (int i=1; i <= numberOfIterations; i++) {
								
								randomIndex = generator.nextInt(setOfTrees.size());
								
								double iAsDouble = (double) i;
								double auxDist=0;
								auxDist= totalDist(solution,setOfTrees.get(randomIndex));
								if (auxDist<=0) {
									coef=1;
									} else {
										coef = Math.min(1,1/(iAsDouble*auxDist));
									}
								solution = treeOnGeodesic(solution , setOfTrees.get(randomIndex),coef) ;
							}
							return solution;
						}
				
			  // computes median of a given set of trees, with uniform distribution. Based on the Proximal point algorithm. Cyclic order of the trees.
						public static Tree medianCyclic(ArrayList<Tree> setOfTrees, int numberOfIterations) {
							Tree solution = new Tree(setOfTrees.get(0));
							double coef =0;
							double auxDist=0;
							for (int i=1; i <= numberOfIterations; i++) {
								double iAsDouble = (double) i;
								for (int j=0; j< setOfTrees.size(); j++) {
									
									auxDist= totalDist(solution,setOfTrees.get(j));
									if (auxDist<=0) {
										coef=1;
										} else {
											coef = Math.min(1,1/(iAsDouble*auxDist));
										}
								    coef = Math.min(1,1/(i*auxDist));
								    solution = treeOnGeodesic(solution,setOfTrees.get(j),coef);	
								}	
							}
							return solution;
						}
			
}
