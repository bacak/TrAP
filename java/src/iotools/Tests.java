package iotools;

import geodesic.Tree;

public class Tests {

/*
	
	
	
	
	
	// example
	ArrayList<LeafEdge> leafEdges1 = new ArrayList<LeafEdge>();
	leafEdges1.add(new LeafEdge(0,1));
	leafEdges1.add(new LeafEdge(1,1));
	leafEdges1.add(new LeafEdge(2,1));
	leafEdges1.add(new LeafEdge(3,1));
	leafEdges1.add(new LeafEdge(4,1));
	leafEdges1.add(new LeafEdge(5,1));
	leafEdges1.add(new LeafEdge(6,1));
	leafEdges1.add(new LeafEdge(7,1));
	
	ArrayList<InnerEdge> innerEdges1 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		cosplit.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.83));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.73));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		split.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.47));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		split.add(leafEdges1.get(2));
		split.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		split.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.88));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		split.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		split.add(leafEdges1.get(4));
		split.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,1));
	}
	Tree tree1 = new Tree(innerEdges1,leafEdges1);

	
	
	ArrayList<LeafEdge> leafEdges2 = new ArrayList<LeafEdge>();
	leafEdges2.add(new LeafEdge(0,1));
	leafEdges2.add(new LeafEdge(1,1));
	leafEdges2.add(new LeafEdge(2,1));
	leafEdges2.add(new LeafEdge(3,.2));
	leafEdges2.add(new LeafEdge(4,1));
	leafEdges2.add(new LeafEdge(5,1));
	leafEdges2.add(new LeafEdge(6,1));
	leafEdges2.add(new LeafEdge(7,1));
	
	ArrayList<InnerEdge> innerEdges2 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		cosplit.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		cosplit.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.7));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		split.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		cosplit.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.87));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		split.add(leafEdges2.get(3));
		split.add(leafEdges2.get(4));
		cosplit.add(leafEdges2.get(5));
		split.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.42));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		split.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		split.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.5));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		split.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.15));
	}
	
	Tree tree2 = new Tree(innerEdges2,leafEdges2);
	

	
	ArrayList<LeafEdge> leafEdges3 = new ArrayList<LeafEdge>();
	leafEdges3.add(new LeafEdge(0,.01));
	leafEdges3.add(new LeafEdge(1,.1));
	leafEdges3.add(new LeafEdge(2,.2));
	leafEdges3.add(new LeafEdge(3,.3));
	leafEdges3.add(new LeafEdge(4,.4));
	leafEdges3.add(new LeafEdge(5,.5));
	leafEdges3.add(new LeafEdge(6,.6));
	leafEdges3.add(new LeafEdge(7,.7));
	
	ArrayList<InnerEdge> innerEdges3 = new ArrayList<InnerEdge>();
	
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges3.get(0));
		cosplit.add(leafEdges3.get(6));
		split.add(leafEdges3.get(1));
		cosplit.add(leafEdges3.get(2));
		cosplit.add(leafEdges3.get(3));
		cosplit.add(leafEdges3.get(4));
		cosplit.add(leafEdges3.get(5));
		cosplit.add(leafEdges3.get(7));
		innerEdges3.add(new InnerEdge(split,cosplit,.90));
		
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges3.get(0));
		split.add(leafEdges3.get(6));
		split.add(leafEdges3.get(1));
		cosplit.add(leafEdges3.get(2));
		cosplit.add(leafEdges3.get(3));
		cosplit.add(leafEdges3.get(4));
		split.add(leafEdges3.get(5));
		cosplit.add(leafEdges3.get(7));
		innerEdges3.add(new InnerEdge(split,cosplit,.91));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges3.get(0));
		cosplit.add(leafEdges3.get(6));
		split.add(leafEdges3.get(1));
		split.add(leafEdges3.get(2));
		split.add(leafEdges3.get(3));
		split.add(leafEdges3.get(4));
		cosplit.add(leafEdges3.get(5));
		split.add(leafEdges3.get(7));
		innerEdges3.add(new InnerEdge(split,cosplit,.92));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges3.get(0));
		split.add(leafEdges3.get(6));
		split.add(leafEdges3.get(1));
		split.add(leafEdges3.get(2));
		cosplit.add(leafEdges3.get(3));
		split.add(leafEdges3.get(4));
		split.add(leafEdges3.get(5));
		cosplit.add(leafEdges3.get(7));
		innerEdges3.add(new InnerEdge(split,cosplit,.93));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges3.get(0));
		split.add(leafEdges3.get(6));
		split.add(leafEdges3.get(1));
		split.add(leafEdges3.get(2));
		cosplit.add(leafEdges3.get(3));
		cosplit.add(leafEdges3.get(4));
		split.add(leafEdges3.get(5));
		cosplit.add(leafEdges3.get(7));
		innerEdges3.add(new InnerEdge(split,cosplit,.94));
	}
	Tree tree3 = new Tree(innerEdges3,leafEdges3);
	
*/




	//double aux =totalDist(tree2,tree1);
	
	//System.out.println(aux);
	
	//Tree hello = new Tree(tree1);
	//hello.print();
	//treeOnGeodesic(tree2,hello,0.5);
	//hello.print();
	
	
	//Tree result1= new Tree(treeOnGeodesic(tree1,tree2,0.5));
	//result1.print();
	//Tree result2= new Tree(treeOnGeodesic(result1,tree1,0.1));
	//result2.print();
	//Tree result3= new Tree(treeOnGeodesic(result1,result2,0.4));
	//result3.print();
	
	//ArrayList<Tree> listOfTrees =new ArrayList<Tree>();
	//listOfTrees.add(new Tree(tree1));
	//listOfTrees.add(new Tree(tree2));
	//listOfTrees.add(new Tree(tree3));
	
	
	
	//average(listOfTrees).print();
	
	/*
	ArrayList<LeafEdge> leafEdges1 = new ArrayList<LeafEdge>();
	leafEdges1.add(new LeafEdge(0,1));
	leafEdges1.add(new LeafEdge(1,1));
	leafEdges1.add(new LeafEdge(2,1));
	leafEdges1.add(new LeafEdge(3,1));
	leafEdges1.add(new LeafEdge(4,1));
	leafEdges1.add(new LeafEdge(5,1));
	leafEdges1.add(new LeafEdge(6,1));
	leafEdges1.add(new LeafEdge(7,1));
	
	ArrayList<InnerEdge> innerEdges1 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		cosplit.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.83));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.73));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		split.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.47));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		split.add(leafEdges1.get(2));
		split.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		cosplit.add(leafEdges1.get(5));
		split.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,.88));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		split.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		split.add(leafEdges1.get(4));
		split.add(leafEdges1.get(5));
		cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,1));
	}
	Tree tree1 = new Tree(innerEdges1,leafEdges1);

	
	
	ArrayList<LeafEdge> leafEdges2 = new ArrayList<LeafEdge>();
	leafEdges2.add(new LeafEdge(0,1));
	leafEdges2.add(new LeafEdge(1,1));
	leafEdges2.add(new LeafEdge(2,1));
	leafEdges2.add(new LeafEdge(3,.2));
	//leafEdges2.add(new LeafEdge(3,1));
	leafEdges2.add(new LeafEdge(4,1));
	leafEdges2.add(new LeafEdge(5,1));
	leafEdges2.add(new LeafEdge(6,1));
	leafEdges2.add(new LeafEdge(7,1));
	
	ArrayList<InnerEdge> innerEdges2 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		cosplit.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		cosplit.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.7));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		split.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		cosplit.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.87));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		split.add(leafEdges2.get(3));
		split.add(leafEdges2.get(4));
		cosplit.add(leafEdges2.get(5));
		split.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.42));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		split.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		split.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.5));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		split.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,.15));
	}
	
	Tree tree2 = new Tree(innerEdges2,leafEdges2);
	
	
	*/
	
	
	
	
	
	
	/*
	
	ArrayList<LeafEdge> leafEdges1 = new ArrayList<LeafEdge>();
	leafEdges1.add(new LeafEdge(0,1));
	leafEdges1.add(new LeafEdge(1,1));
	leafEdges1.add(new LeafEdge(2,1));
	leafEdges1.add(new LeafEdge(3,1));
	leafEdges1.add(new LeafEdge(4,1));
	leafEdges1.add(new LeafEdge(5,1));
	leafEdges1.add(new LeafEdge(6,1));
	//leafEdges1.add(new LeafEdge(7,1));
	
	ArrayList<InnerEdge> innerEdges1 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		cosplit.add(leafEdges1.get(6));
		cosplit.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		split.add(leafEdges1.get(5));
		//cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,1));
	}
	
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		cosplit.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		split.add(leafEdges1.get(5));
		//cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,1));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		split.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		split.add(leafEdges1.get(2));
		cosplit.add(leafEdges1.get(3));
		cosplit.add(leafEdges1.get(4));
		split.add(leafEdges1.get(5));
		//split.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,1));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		cosplit.add(leafEdges1.get(6));
		split.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		split.add(leafEdges1.get(3));
		split.add(leafEdges1.get(4));
		split.add(leafEdges1.get(5));
		//cosplit.add(leafEdges1.get(7));
		innerEdges1.add(new InnerEdge(split,cosplit,1));
	}
	Tree tree1 = new Tree(innerEdges1,leafEdges1);

	
	
	ArrayList<LeafEdge> leafEdges2 = new ArrayList<LeafEdge>();
	leafEdges2.add(new LeafEdge(0,1));
	leafEdges2.add(new LeafEdge(1,1));
	leafEdges2.add(new LeafEdge(2,1));
	leafEdges2.add(new LeafEdge(3,1));
	leafEdges2.add(new LeafEdge(4,1));
	leafEdges2.add(new LeafEdge(5,1));
	leafEdges2.add(new LeafEdge(6,1));
	//leafEdges2.add(new LeafEdge(7,1));
	
	ArrayList<InnerEdge> innerEdges2 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		cosplit.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		cosplit.add(leafEdges2.get(5));
		//cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,1));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		cosplit.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		split.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		//cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,1));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		split.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		split.add(leafEdges2.get(3));
		cosplit.add(leafEdges2.get(4));
		cosplit.add(leafEdges2.get(5));
		//split.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,1));
	}
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(6));
		split.add(leafEdges2.get(1));
		cosplit.add(leafEdges2.get(2));
		split.add(leafEdges2.get(3));
		split.add(leafEdges2.get(4));
		split.add(leafEdges2.get(5));
		//cosplit.add(leafEdges2.get(7));
		innerEdges2.add(new InnerEdge(split,cosplit,1));
	}
	
	
	Tree tree2 = new Tree(innerEdges2,leafEdges2);
	
	*/
	
	
	
	
	
	
	
	
	
	/*
	ArrayList<LeafEdge> leafEdges1 = new ArrayList<LeafEdge>();
	leafEdges1.add(new LeafEdge(0,1));
	leafEdges1.add(new LeafEdge(1,1));
	leafEdges1.add(new LeafEdge(2,1));
	leafEdges1.add(new LeafEdge(3,1));
	
	
	ArrayList<InnerEdge> innerEdges1 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges1.get(0));
		cosplit.add(leafEdges1.get(1));
		cosplit.add(leafEdges1.get(2));
		split.add(leafEdges1.get(3));
		
		innerEdges1.add(new InnerEdge(split,cosplit,10));
	}
	Tree tree1 = new Tree(innerEdges1,leafEdges1);

	
	
	ArrayList<LeafEdge> leafEdges2 = new ArrayList<LeafEdge>();
	leafEdges2.add(new LeafEdge(0,1));
	leafEdges2.add(new LeafEdge(1,1));
	leafEdges2.add(new LeafEdge(2,1));
	leafEdges2.add(new LeafEdge(3,1));
	
	
	ArrayList<InnerEdge> innerEdges2 = new ArrayList<InnerEdge>();
	{
		ArrayList<LeafEdge> split = new ArrayList<LeafEdge>();
		ArrayList<LeafEdge> cosplit = new ArrayList<LeafEdge>();
		split.add(leafEdges2.get(0));
		cosplit.add(leafEdges2.get(1));
		split.add(leafEdges2.get(2));
		cosplit.add(leafEdges2.get(3));
		
		innerEdges2.add(new InnerEdge(split,cosplit,20));
	}
	
	Tree tree2 = new Tree(innerEdges2,leafEdges2);
	*/
	
	
	
	
	
	
	//tree1.print();
			//middleTree2.print();
			//System.out.println("midpoint-average dist:" + mt);
			//System.out.println("t1-av: " + error + "    av-t2: " +error1);
			//System.out.println("sum: " + (error+error1));
			//System.out.println(orig);
			
			//System.out.println(dist2);
			//System.out.println(dist4);
			//System.out.println(dist5);
			//System.out.println(dist6);
			//System.out.println(dist8);
			//System.out.println(dista);
			//double total = dist2+dist4+dist5+dist6+dist8+dista;
			//System.out.println("total distance: " + total);
			//System.out.println("orig distance: " + dist);
			//System.out.println("orig distance: " +orig);
			//tree1.print();
			//tree2.print();
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			//tree1.print();
			
			//middleTree2.print();
			//tree2.print();
			/*
			Tree s = new Tree(tree1);
			Tree t = new Tree(middleTree2);
			
			double diff=0;
			int p=0;
			int q=0;
			do {
				p=Tree.findCommonEdge(s,t)[0];
				q=Tree.findCommonEdge(s,t)[1];
				System.out.println("p=" + p + ",   q=" +q);
				if (p>=0) {
					diff += Math.pow(s.getInnerEdges().get(p).getLength()-t.getInnerEdges().get(q).getLength(),2);
					s.getInnerEdges().remove(p);
					t.getInnerEdges().remove(q);
					System.out.println("difference squared: " + diff);
				}
				else {
					break;
				}
			} while (p>=0);
			
			System.out.println("total difference: " + Math.sqrt(diff));
			*/
			/*
			Random generator = new Random();
			int randomIndex =0;
			double coef =0;
			int n = Integer.parseInt(args[args.length-1]);
			double res =0;
			for (int i=1; i <= n; i++) {
				
				randomIndex = generator.nextInt(2);
				
				double iAsDouble = (double) i;
				coef = 1/(iAsDouble+1);
				
				
				
				res = (1-coef)* res + coef * randomIndex ;
				System.out.println("iteration: " + i  + " coef: " + coef   + " random index: " + randomIndex);
				
				System.out.println("res: " + Math.abs(0.5-res)*100);
				System.out.println("");
			}*/
			
			
			
			
			/*
			System.out.println(" ");
			System.out.println("number of leaf/inner edges: " + middleTree2.getLeafEdges().size() + " / " + middleTree2.getInnerEdges().size());
			System.out.println("number of leaf/inner edges: " + middleTree4.getLeafEdges().size() + " / " + middleTree4.getInnerEdges().size());
			System.out.println("number of leaf/inner edges: " + middleTree6.getLeafEdges().size() + " / " + middleTree6.getInnerEdges().size());
			System.out.println("number of leaf/inner edges: " + middleTree8.getLeafEdges().size() + " / " + middleTree8.getInnerEdges().size());
			System.out.println("number of leaf/inner edges of tree1: " + tree1.getLeafEdges().size() + " / " + tree1.getInnerEdges().size());
			System.out.println("number of leaf/inner edges of tree2: " + tree2.getLeafEdges().size() + " / " + tree2.getInnerEdges().size());
			
		*/
			//double err = Tree.totalDist(averageTree, middleTree);
			//System.out.println(err);
			//double errrev = Tree.totalDist(middleTree,averageTree);
			//System.out.println(errrev);
			//double dist = Tree.totalDist(tree1, tree2);
			
			//double distrev = Tree.totalDist(tree2, tree1);
			//System.out.println(dist2);
			//System.out.println(distrev);
			
		
			
			//System.out.println(dist2);
			
			
			/*
			Tree mytree1 = new Tree(middleTree8);
			Tree mytree2 = new Tree(tree2);
			int counter2 =0;
			for (int q=0; q < mytree1.getInnerEdges().size(); q++) {
				int counter =0;
				for (int p=0; p < mytree2.getInnerEdges().size(); p++) {
					if (mytree1.getInnerEdges().get(q).isIdenticalWith(mytree2.getInnerEdges().get(p))) {
						counter++;
						//System.out.println("not too bad");
						//System.exit(0);
					}
				}
				if (counter ==1) {
					counter2++;
				}
				else {
					System.out.println("counter: " + counter);
				}
			}
			if (counter2 == mytree1.getInnerEdges().size()) {
				System.out.println("bad");
				System.out.println("counter: " + counter2);
			}
			else {
				System.out.println("not too bad, too");
				System.out.println("counter: " + counter2);
			}
			*/
			
			//int[] index = Tree.findCommonEdge(middleTree2,middleTree8);
			
			
			//System.out.println("index.length: " + index.length);
			
			/*if (index.length>0) {
				System.out.println("tree has common edges: " + index[0] + " and " + index[1]);
			}
			else {
				System.out.println("!!!");
			}	
			*/
			
			
	// check whether two trees have the same leaves
	public static boolean areComparable(Tree s, Tree t) {
		if (s.getLeafEdges().size() != t.getLeafEdges().size()) {
			return false;
		}
		int counter1 =0;
		for (int i=0; i< s.getLeafEdges().size(); i++) {
			int counter2 =0;
			for (int j=0; j< t.getLeafEdges().size(); j++) {
				if (s.getLeafEdges().get(i).getID() == t.getLeafEdges().get(j).getID()) {
					counter2++;
				}
			}
			if (counter2>0) {
				counter1++;
			}
		}
		if (counter1<s.getLeafEdges().size()) {
			return false;
		}
		return true;
	}
	
	// check whether two trees have the same combinatorial structure
	public static boolean sameOrthant(Tree s, Tree t) {
		if (!areComparable(s,t)) {
			System.out.println("different leaves");
			return false;
		}
		if (s.getInnerEdges().size()!=t.getInnerEdges().size()) {
			System.out.println("different # inner edges");
			return false;
		}
		for (int i=0; i<s.getInnerEdges().size(); i++) {
			int counter=0;
			for (int j=0; j<t.getInnerEdges().size(); j++) {
				if (Tree.isSameEdge(s.getInnerEdges().get(i),t.getInnerEdges().get(j))) {
					counter++;
				}
			}
			if (counter!=1) {
				System.out.println("same # inner edges, but different splits");
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
}
