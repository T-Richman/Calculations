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

/*
Adding a node as a child to the tree's current node being checked:

if lvl is 0(+)
	"add node to current"
else (lvl is not 0)
	if node is leaf
		if node is single value
			"skip adding the node"
		else (node is equation)
			for each child in current:
			if node is same as child (subequation) of current
				"add node to currents child"
				break
	else (node is not leaf)
		if node is contained in current and node is equation
			"add node to current"
		else (node is not contained in current or node is single-value)
			if current has children
				for each child in current:
				if node is contained in currents child
					if currents child has/is duplicate of node
					else (currents child does not have/is not duplicate of node)
						"add node to currents child"
				if node is not same to any child of current
					"add node to current"
			else (current has no children)
				"add node to current"
*/

/*
adding based on node being an equation or single-value
"contained"=node data is a subequation of currents data

equation:
if node contained in current
	if current has children
		for each child of current:
		if node contained in currents child
			"add node to currents child"
		else (node not contained in currents child)
			"add node to current"
	else (current does not have children)
		"add node to current"
else (node not contained in current, root="NULL")
	if current has children
		for each child of current:
		if node contained in currents child
			"add node to currents child"
		else (node not contained in currents child)
			"add node to current"
	else (current has no children)
		"skip adding node"

single-value:
if node contained in current
	if current has children
		for each child of current:
		if node contained in currents child
			if currents child is a copy of node
				"set current as filled with node"
				"add node to currents parent"
			else (currents child is not a copy of node)
				"add node to currents child"
		else (node not contained in currents child)
			"add node to current"
	else (current does not have children)
		"add node to current"
else (node is not contained in current)
*/

/*
* int d_cnt=check_count(n,c,lvl);
	if(d_cnt<=1) {
		System.out.println("No self-operation (ex 2*2), checking for existing child values");
		for(Node cc:c.children) {
			if(cc.data==n.data) {
				System.out.println("The current's grandchild is same as the node, leaving this branch");
				break;
			}
		}
	}
* 
*/

class Tree{
	private Node root;
	private int sv_flag;
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
		LinkedList<Node> search_queue = new LinkedList<>();
		//if root is empty, then simply add to root
		if(lvl==0) {
			System.out.println("New child node, adding child "+n.data);
			n.parent=current;
			current.children.add(n);
		}
		else {
			//if the entry is a duplicate, check if this entry being added is a leaf
			if(check_leaf(current,s)==true) {
				//tries to convert data to double, works if single-value, catches if equation
				System.out.println("This node has duplicate data on this level, checking tree...");
				sv_flag=convert_data(n);
				
				//if the entry is a leaf (converted to double successfully) it is skipped
				if(sv_flag==1) {
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
				//checks if node is child of current(1) or not a child of current(0)
				int cnt=check_data(n,current);
				//checks if node is a single-value(1) or an equation(2)
				//if node is a child of current and is an equation
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
							//if the entry and current's child have same parts
							if(cnt==1) {
								
								//CHECK IF CHILD HAS EXISITNG SAME DATA
								//2*11 --> 2,11 VS 10/2 --> 10,2
								//ALSO CHECK FOR SAME VALUES
								//2*2 --> 2,2
								double n_val=0;
								double c_val=0;
								try {
									n_val=Double.valueOf(n.data).doubleValue();
									c_val=Double.valueOf(c.data).doubleValue();
								}
								catch(NumberFormatException e) {
									System.out.println("Cannot convert data of child or node from String to double...");
								}
								System.out.println(n_val);
								System.out.println(c_val);
								
								if(c_val>0 && n_val>0 && c_val==n_val){
									System.out.println("Current has duplicate of node, already filled, moving to next branch");
									break;
								}
								else {
									System.out.println("Node is contained in child "+c.data+", moving to the child node");
									//search_queue.add(c);
									add_Node(s,c);
									break;
								}
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
	
	public int check_count(Node n, Node c, int lvl) {
		int cnt=0;
		String sym="";
		switch(lvl) {
		case 0:
			sym="\\+";
			break;
		case 1:
			sym="-";
			break;
		case 2:
			sym="\\*";
			break;
		case 3:
			sym="/";
			break;
		}
		String[] x=c.data.split(sym);
		for(int i=0;i<x.length;i++) {
			if(x[i]==n.data) {
				cnt++;
			}
		}
		return cnt;
	}
	//Checks if current node contains node as a child
	public int check_data(Node n, Node c) {
		int cnt=0;
		if(c.data.contains(n.data)==true) {
			cnt=1;
		}
		return cnt;
	}
	//Checks if node is a leaf by checking node's children for same data
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
	//Checks if node data is an equation by checking for operators in data
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
	//Converts node data to double if its single value
	public int convert_data(Node n) {
		int single_val_flag=0;
		try {
			double val=Double.valueOf(n.data).doubleValue();
			single_val_flag=1;
		}
		catch(NumberFormatException e) {
			System.out.println("Cannot convert from String to double...");
		}
		return single_val_flag;
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
		System.out.println("===========================================================");
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
		System.out.println("===========================================================");
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
		System.out.println("===========================================================");
		System.out.println("===========================================================");
		//tree1.print_children();
		
		System.out.println("Divide Layer:");
		LinkedList<String[]> divide_layer=split_eqtn(multiply_layer,"/");
		for(String[] l:divide_layer) {
			for(int i=0;i<l.length;i++) {
				System.out.println(l[i]);
				tree1.add_Node(l[i], tree1.get_root());
			}
		}
		tree1.lvl++;
		System.out.println("===========================================================");
		System.out.println("===========================================================");
		System.out.println("===========================================================");
		
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
