package project_classes;
import java.util.*;

class Node{
	public String data;
	public Node parent;
	public LinkedList<Node> children;
	public Node(String val) {
		data=val;
		children=new LinkedList<>();
	}
}

class Tree{
	private Node root;
	public int lvl;
	
	public Tree() {
		root = new Node("NULL");
		lvl = 0;
	}
	//Adds value or part of equation as a node
	public void add_Node(String s, Node node) {
		Node n=new Node(s);
		Node current=node;
		int data_type=check_operator(n);
		
		//if root is empty, then simply add to root
		if(lvl==0) {
			System.out.println("New child node, adding child "+n.data);
			n.parent=current;
			current.children.add(n);
		}
		else {
			//if the entry is a duplicate, check if this entry being added is a leaf
			if(check_leaf(current,s)==true) {
				System.out.println("This node has duplicate data on this level, checking tree...");
				//if the entry is a leaf it is skipped
				double val=0;
				int single_val_flag=0;
				try {
					val=Double.valueOf(n.data).doubleValue();
					single_val_flag=1;
				}
				catch(NumberFormatException e) {
					System.out.println("Cannot convert from String to double...");
				}
				
				if(single_val_flag==1) {
					System.out.println("This node being left as a leaf");
					System.out.println(" ");
				}
				//if the entry is NOT a leaf the process moves to the duplicate child
				else {
					for(Node c:current.children) {
						System.out.println(c.data);
						if(c.data==n.data) {
							System.out.println("Node is not leaf/broken down, moving to next layer...");
							add_Node(s,c);
							break;
						}
					}
				}
			}
			//if the entry is not a duplicate, find its parent from the current and its children
			else {
				System.out.println("This node is not a duplicate on this level, continuing adding...");
				System.out.println("Node Data:"+n.data+", Length:"+n.data.length());
				System.out.println("Current/Parent Data:"+current.data+", Length:"+current.data.length());
				int cnt=check_data(n,current);
				//if the entry and child have same parts
				//n.data.length()
				if(cnt==1 && data_type==2) {
					System.out.println("Node data is contained in current's data, adding as a child node");
					System.out.println(" ");
					n.parent=current;
					current.children.add(n);
				}
				else {
					if(data_type==1) {
						System.out.println("Node has single-number data, checking with current's children");
					}
					else {
						System.out.println("Node data is NOT contained in current's data, checking with current's children");
					}
					if(current.children.isEmpty()==false) {
						for(Node c:current.children) {
							System.out.println("Checking the current's child "+c.data);
							cnt=check_data(n,c);
							//if the entry and child have same parts
							if(cnt==1) {
								System.out.println("Node is contained in current's child, moving to the child node "+c.data);
								add_Node(s,c);
								break;
							}
						}
						if(cnt==0) {
							System.out.println("Current's children do not contain node, so adding single-number node as a child node");
							System.out.println(" ");
							n.parent=current;
							current.children.add(n);
						}
					}
					else {
						System.out.println("Current has no children, so adding single-number node as a child node");
						System.out.println(" ");
						n.parent=current;
						current.children.add(n);
					}
				}
			}
		}
		
	}
	//Max-Children First Search: Search algorithm for the node with most children
	public Node mcfs(Node current) {
		//System.out.println(current.data);
		LinkedList<Node> child_list=current.children;
		
		if(child_list.isEmpty()==true) {
			System.out.println("Can add nodes to "+current.data+" since this node has no children");
			return current;
		}
		else {
			//Manually added grand-children for testing
			
			for(Node n:child_list) {
				Node temp = new Node("-null-");
				for(int i=0;i<(int)(Math.random()*4);i++) {
					n.children.add(temp);
				}
			}
			/*
			for(int i=0;i<child_list.size();i++) {
				Node temp = new Node("-null-");
				for(int j=0;j<(int)(Math.random()*4);j++) {
					child_list.get(i).children.add(temp);
				}
			}
			*/
			Node temp_2=new Node("-null-");
			child_list.getLast().children.add(temp_2);
			
			//children are sorted for easier choice by depth
			child_list=sort_list(child_list);
			System.out.println("Current's children size: "+child_list.size());
			
			for(int i=0;i<child_list.size();i++) {
				System.out.println("Sorted Node "+i+" Size: "+child_list.get(i).children.size());
			}
			
			//If last node, which supposed to have most children, is a similar node (not unique)
			//perform search on the first node with same child amount as last node
			//since that node will be the leftmost node with the same amount of children
			if(child_list.size()==1) {
				System.out.println("Only one child, moving on...");
				System.out.println(" ");
				return mcfs(child_list.getFirst());
			}
			else {
				System.out.println("Current's last index of the max children amount: "+child_list.lastIndexOf(child_list.getLast()));
				System.out.println(" ");
				if(child_list.getLast().children.size()==child_list.get(child_list.lastIndexOf(child_list.getLast())-1).children.size()) {
					//Creates placeholder node and checks for the leftmost similar node
					Node next_node = new Node(" ");
					for(Node c:child_list) {
						if(c.children.size()==child_list.getLast().children.size()) {
							next_node=c;
							break;
						}
					}
					return mcfs(next_node);
				}
				//Returns the last node, since it should have the most children
				else {
					return mcfs(child_list.getLast());
				}
			}
		}
	}
	
	public static LinkedList<Node> sort_list(LinkedList<Node> list){
		for(Node a:list) {
			for(int i=list.indexOf(a)+1;i<list.size();i++) {
				if(a.children.size()>list.get(i).children.size()) {
					Node temp=list.get(i);
					list.set(i, a);
					list.set(list.indexOf(a), temp);
				}
			}
		}
		return list;
	}
	
	//FIX THIS, 100 is being read as 10 for each char scanned
	
	public int check_data(Node n, Node c) {
		int cnt=0;
		if(c.data.contains(n.data)==true) {
			cnt=1;
		}
		/*
		for(int i=0;i<n.data.length();i++) {
			for(int j=0;j<c.data.length();j++) {
				if(c.data.charAt(j)==n.data.charAt(i)) {
					System.out.println(c.data.charAt(j)+","+n.data.charAt(i));
					cnt++;
					c.data.replace(c.data.charAt(j), '_');
					j=c.data.length()-1;
				}
			}
		}
		System.out.println("Current checked: "+c.data);
		*/
		return cnt;
	}
	
	public boolean check_leaf(Node n,String s) {
		boolean check=false;
		for(Node c:n.children) {
			if (c.data==s) {
				check=true;
				break;
			}
		}
		return check;
	}
	
	public int check_operator(Node n) {
		//System.out.println(n.data);
		int type=1;
		for(int i=0;i<n.data.length();i++) {
			if(n.data.charAt(i)=='+' || n.data.charAt(i)=='-' || n.data.charAt(i)=='*' || n.data.charAt(i)=='/') {
				System.out.println("Node is an equation, not a single number");
				type=2;
				break;
			}
			else {
				type=1;
			}
		}
		return type;
	}
	
	public Node get_root() {
		return root;
	}
	
	public void print_children(){
		System.out.println(root.children.get(0).data);
		System.out.println(root.children.get(1).data);
		System.out.println(root.children.get(2).data);
		System.out.println(" ");
		System.out.println(root.children.get(1).children.get(0).data);
		System.out.println(root.children.get(1).children.get(1).data);
		System.out.println(root.children.get(1).children.get(2).data);
		System.out.println(root.children.get(1).children.get(3).data);
		System.out.println(root.children.get(2).children.get(0).data);
		System.out.println(" ");
		System.out.println(root.children.get(1).children.get(0).children.get(0).data);
		System.out.println("============");
	}
}

public class PEDMAS_Calc {

	
	
	public static LinkedList<String[]> split_eqtn(String s, String c) {
		LinkedList<String[]> list_layer=new LinkedList<>();
		list_layer.add(s.split(c));
		return split_eqtn(list_layer,c);
	}
	
	public static LinkedList<String[]> split_eqtn(LinkedList<String[]> l, String c) {
		LinkedList<String[]> next_layer=new LinkedList<>();
		for(String[] s:l) {
			for(int i=0;i<s.length;i++) {
				next_layer.add(s[i].split(c));
			}
		}
		/*
		for(String[] s:next_layer) {
			for(int i=0;i<s.length;i++) {
				System.out.println(s[i]);
			}
			System.out.println("===========");
		}
		System.out.println(" ");
		*/
		return next_layer;
	}
	
	
	public static void main(String[] args) {
		//Gets string and finds any parentheses pairs
		String eqtn = "9+2*11-5*6-10/2-100+24/12*2/8";
		System.out.println(eqtn);
		Tree tree1 = new Tree();
		
		System.out.println("Plus Layer:");
		LinkedList<String[]> plus_layer=split_eqtn(eqtn,"\\+");
		for(String[] l:plus_layer) {
			for(int i=0;i<l.length;i++) {
				System.out.println(l[i]);
				tree1.add_Node(l[i], tree1.get_root());
			}
		}
		tree1.lvl++;
		System.out.println("===========================================================");
		
		System.out.println("Minus Layer:");
		LinkedList<String[]> minus_layer=split_eqtn(plus_layer,"-");
		for(String[] l:minus_layer) {
			for(int i=0;i<l.length;i++) {
				System.out.println(l[i]);
				tree1.add_Node(l[i], tree1.get_root());
			}
		}
		tree1.lvl++;
		System.out.println("===========================================================");
		
		System.out.println("Multiply Layer:");
		LinkedList<String[]> multiply_layer=split_eqtn(minus_layer,"\\*");
		for(String[] l:multiply_layer) {
			for(int i=0;i<l.length;i++) {
				System.out.println(l[i]);
				tree1.add_Node(l[i], tree1.get_root());
			}
		}
		tree1.lvl++;
		System.out.println("===========================================================");
		//tree1.print_children();
		
		System.out.println("Divide Layer:");
		LinkedList<String[]> divide_layer=split_eqtn(multiply_layer,"/");
		
		System.out.println("Final Layer:");
		LinkedList<String[]> final_layer=split_eqtn(divide_layer,"/");
		
		
		/*
		//First split for addition (+) layer
		//Double-dash is because + or * character are reserved in regex system
		String layer1[]=eqtn.split("\\+");
		LinkedList<String[]> plus_layer=new LinkedList<>();
		plus_layer.add(layer1);
		for(String[] s:plus_layer) {
			for(int i=0;i<s.length;i++) {
				System.out.println(s[i]);
			}
			System.out.println("======");
		}
		
		System.out.println("x");
		/*
		for(int i=0;i<layer1.length;i++) {
			layer1[i]=layer1[i].split("-");
		}
		for(int i=0;i<layer1.length;i++) {
			System.out.println(layer1[i]);
		}
		/*
		for(int a=0;a<layer1.length;a++) {
			System.out.println(layer1[a]);
			tree1.add_Node(layer1[a],tree1.get_root());
		}
		System.out.println(" ");
		
		//Second split for subtraction (-) layer
		LinkedList<String[]> layer2 = new LinkedList<>();
		for(int a=0;a<layer1.length;a++) {
			layer2.add(layer1[a].split("-"));
		}
		for(String[] element1:layer2) {
			for(int a=0;a<element1.length;a++) {
				System.out.println(element1[a]);
				tree1.add_Node(element1[a],tree1.get_root());
			}
			System.out.println("-------------------");
		}
		System.out.println(" ");
		
		//Third split for multiplication (*) layer
		LinkedList<String[]> layer3 = new LinkedList<>();
		for(String[] element2:layer2) {
			for(int a=0;a<element2.length;a++) {
				layer3.add(element2[a].split("\\*"));
			}
		}
		for(String[] element3:layer3) {
			for(int a=0;a<element3.length;a++) {
				System.out.println(element3[a]);
			}
			System.out.println("-------------------");
		}
		System.out.println(" ");
		
		//Fourth and final split for division (/) layer
		LinkedList<String[]> layer4 = new LinkedList<>();
		for(String[] element4:layer3) {
			for(int a=0;a<element4.length;a++) {
				layer4.add(element4[a].split("/"));
			}
		}
		for(String[] element5:layer4) {
			for(int a=0;a<element5.length;a++) {
				System.out.println(element5[a]);
			}
			System.out.println("-------------------");
		}
		System.out.println(" ");
		
		/*
		Tree tree1 = new Tree();
		for(int i=0;i<eqtn.length();i++) {
			char c=eqtn.charAt(i);
			String val="";
			//Checks if current character is an operator
			System.out.println(c!='+'&&c!='-'&&c!='*'&&c!='/');
			
			if(c!='+'&&c!='-'&&c!='*'&&c!='/') {
				//Checks if next character is an operator
				
				tree1.add_Node(val);
			}
			
		}
		*/
	}

}
