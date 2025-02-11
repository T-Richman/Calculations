package calculations;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
//import java.io.InputStreamReader;


class Node{
	  public String data;
	  public boolean is_Full = false;
	  public boolean has_Match = true;
	  public Node parent;
	  public Node left;
	  public Node right;
	  
	  public Node(String e){
	    data = e;
	  } 
	}

class List<E extends Comparable<E>>{
  private Node root = null;
  
  public Node insert(Node node, Node node_add, Node node_Update){
	  Node curr = node;
	  if(curr.left!=null&&node_Update.data==curr.left.data) {
		  //System.out.println("Left node updated as full: "+curr.left.data);
		  curr.left.is_Full=node_Update.is_Full;
		  
	  }
	  else if(curr.right!=null&&node_Update.data==curr.right.data) {
		  //System.out.println("Right node updated as full: "+curr.right.data);
		  curr.right.is_Full=node_Update.is_Full;
	  }
	  //System.out.println("Current node: "+curr.data);
	  
	  //if left is empty add to left
	  if(curr.left==null) {
		  //System.out.println("Left is empty, adding to left");
		  //System.out.println("");
		  node_add.left=curr.left;
		  curr.left=node_add;
		  curr.left.parent=curr;
		  return curr;
	  }
	  //if left is full, check if its NULL itself, is full, or has space/empty children
	  else {
		  //if left is NULL, check if right is empty
		  if(curr.left.data=="NULL"){
			  //System.out.println("Left is NULL, checking right");
			  //if right is empty, add to right
			  if(curr.right==null) {
				  //System.out.println("Right is empty, adding to right");
				  //System.out.println("");
				  node_add.right=curr.right;
				  curr.right=node_add;
				  curr.right.parent=curr;
				  return curr;
			  }
			  //if right is NULL, this is full, so go to node's parent
			  else if(curr.right.data=="NULL") {
				  //System.out.println("Right is NULL, recalculating back to parent");
				  //System.out.println("");
				  curr.is_Full=true;
				  curr.parent=insert(curr.parent,node_add,curr);
				  return curr;
			  }
		  }
		  //if left is not NULL and not empty, check if its full(non-empty children)
		  else if(curr.left.is_Full==true) {
			  //System.out.println("Left is full(non-empty children), checking right");
			  //if left's is full, left is an end, so check right
			  if(curr.right==null) {
				  //System.out.println("Right is empty, adding to right");
				  //System.out.println("");
				  node_add.left=curr.right;
				  curr.right=node_add;
				  curr.right.parent=curr;
				  return curr;
			  }
			  //if right is NULL, this is full, so go to node's parent
			  else if(curr.right.data=="NULL") {
				  //System.out.println("Right is NULL, recalculating back to parent");
				  //System.out.println("");
				  curr.is_Full=true;
				  curr.parent=insert(curr.parent,node_add,curr);
				  return curr;
			  }
			  else if(curr.right.is_Full==true) {
				  //System.out.println("Right is full, recalculating back to parent");
				  //System.out.println("");
				  curr.is_Full=true;
				  curr.parent=insert(curr.parent,node_add,curr);
				  return curr;
			  }
			  //if right is not NULL or full(empty children), move to right's children
			  else {
				  //System.out.println("Right is not empty or ended, looking through right");
				  curr.right=insert(curr.right, node_add,curr);
			  }
		  }
		  //if left is not NULL or full (empty children), move to left's children
		  else {
			  //System.out.println("Left is not empty or ended, looking through left");
			  curr.left=insert(curr.left, node_add,curr);
		  }
		  return curr;
	  }
  }
  
  public void add_Node(String e){
	  Node node_to_add = new Node(e);
		if(root==null){
	    	node_to_add.left=root;
	    	node_to_add.right=root;
	    	root=node_to_add;
	    }
	    else {
	    	//System.out.println("Attempting to add "+node_to_add.data+" starting on the left...");
	    	Node found_node = insert(root,node_to_add,node_to_add);
	    	root=found_node;
	    }
  }
  
  public String find_encode(Node temp, String message, char val, Node node_Update){
	  if(temp!=null) {
		  if(temp.left!=null&&node_Update.data==temp.left.data) {
			  //System.out.println("Left node updated as no-matches: "+temp.left.data);
			  temp.left.has_Match=node_Update.has_Match;
			  
			  char[] tempArr = new char[message.length()];
			  for(int i=0; i<tempArr.length; i++) {
				  tempArr[i]=message.charAt(i);
			  }
			  tempArr[tempArr.length-1]=' ';
			  message="";
			  for(int i=0; i<tempArr.length-1; i++) {
				  message+=tempArr[i];
			  }
			  
			  //System.out.println("Message: "+message);
		  }
		  else if(temp.right!=null&&node_Update.data==temp.right.data) {
			  //System.out.println("Right node updated as no-matches: "+temp.right.data);
			  temp.right.has_Match=node_Update.has_Match;
			  
			  char[] tempArr = new char[message.length()];
			  for(int i=0; i<tempArr.length; i++) {
				  tempArr[i]=message.charAt(i);
			  }
			  tempArr[tempArr.length-1]=' ';
			  message="";
			  for(int i=0; i<tempArr.length-1; i++) {
				  message+=tempArr[i];
			  }
			  
			  //System.out.println("Message: "+message);
		  }
	  }

	  //if encoding space, just add space 
	  if(val==' ') {
		  message+=" ";
		  return message;
	  }
	  else {
		  //if left is same as val, add "." and return
		  if(temp.left.data.toString().charAt(0)==val) {
			  //System.out.println("Left is same, encoding as . ");
			  //System.out.println("");
			  message+=".";
			  return message;
		  }
		  else {
			  //if left is NULL, check if right is empty
			  if(temp.left.data=="NULL"){
				  //System.out.println("Left is NULL, checking right");
				  //if right is same as val, add "_" and return
				  if(temp.right.data.toString().charAt(0)==val) {
					  //System.out.println("Right is same, encoding as _ ");
					  //System.out.println("");
					  message+="_";
					  return message;
				  }
				  //if right is NULL, there are no matching values, so go to node's parent
				  else if(temp.right.data=="NULL") {
					  //System.out.println("Right is NULL, recalculating back to parent");
					  //System.out.println("");
					  temp.has_Match=false;
					  message=find_encode(temp.parent,message,val,temp);
					  return message;
				  }
			  }
			  //if left has no matching values, check right
			  else if(temp.left.has_Match==false) {
				  //System.out.println("Left has no matching values, checking right");
				  //if right is same as val, add "_" and return
				  if(temp.right.data.toString().charAt(0)==val) {
					  //System.out.println("Right is same, encoding as _ ");
					  //System.out.println("");
					  message+="_";
					  return message;
				  }
				  //if right is NULL, there are no matching values, so go to node's parent
				  else if(temp.right.data=="NULL") {
					  //System.out.println("Right is NULL, recalculating back to parent");
					  //System.out.println("");
					  temp.has_Match=false;
					  message=find_encode(temp.parent,message,val,temp);
					  return message;
				  }
				  else if(temp.right.has_Match==false) {
					  //System.out.println("Right has no matching values, recalculating back to parent");
					  //System.out.println("");
					  temp.has_Match=false;
					  message=find_encode(temp.parent,message,val,temp);
					  return message;
				  }
				  //if right is not NULL or full(empty children), move to right's children
				  else {
					  //System.out.println("Right continues, checking right");
					  message+="_";
					  message=find_encode(temp.right,message,val,temp);
					  return message;
				  }
			  }
			  //if left is not NULL or full (empty children), move to left's children
			  else {
				  //System.out.println("Left continues, checking left");
				  message+=".";
				  message=find_encode(temp.left,message,val,temp);
				  return message;
			  }
		  }
		  return message;
	  }
  }
  
  public char find_decode(String morse){
	  Node temp = root;
	  //LinkedList<Character> symbol = new LinkedList<Character>();
	  for(int i=0;i<morse.length();i++) {
		  if(morse.charAt(i)=='.') {
			  temp=temp.left;
		  }
		  else if (morse.charAt(i)=='_'){
			  temp=temp.right;
		  }
	  }
	  return temp.data.toString().charAt(0);
  }
  
  public String encode(String s1) {
		LinkedList<Character> letters = new LinkedList<Character>();
		for(int i=0;i<s1.length();i++) {
			letters.add(s1.charAt(i));
		}
		String message = "";
		while(letters.isEmpty()==false) {
			Node temp = root;
			message=find_encode(temp,message,letters.get(0),temp);
			message+=" ";
			letters.remove();
		}
		return message;
	}
  
  public String decode(String s1) {
		LinkedList<String> morse = new LinkedList<String>();
		String temp1="";
		for(int i=0;i<s1.length();i++) {
			if(s1.charAt(i)!=' ') {
				temp1+=s1.charAt(i);
			}
			else {
				morse.add(temp1);
				temp1="";
			}
		}
		morse.add(temp1);
		/*
		for(String s:morse) {
			System.out.println(s);
		}
		*/
		String message = "";
		while(morse.isEmpty()==false) {
			message+=find_decode(morse.getFirst());
			morse.remove();
		}
		return message;
	}
  /*
  public void show() {
		Node temp = root;
		
		//In case the file reading does not work
		//Below is manual inputs as proof of insertion working
		
		System.out.println(temp.data);//*
		
		System.out.println(temp.left.data);//e
		System.out.println(temp.left.left.data);//i
		System.out.println(temp.left.left.left.data);//s
		System.out.println(temp.left.left.left.left.data);//h
		System.out.println(temp.left.left.left.left.left.data);//h_NULL_L
		System.out.println(temp.left.left.left.left.right.data);//h_NULL_R
		System.out.println(temp.left.left.left.right.data);//v
		System.out.println(temp.left.left.left.right.left.data);//v_NULL_L
		System.out.println(temp.left.left.left.right.right.data);//v_NULL_R
		System.out.println(temp.left.left.right.data);//u
		System.out.println(temp.left.left.right.left.data);//f
		System.out.println(temp.left.left.right.left.left.data);//f_NULL_L
		System.out.println(temp.left.left.right.left.right.data);//f_NULL_R
		System.out.println(temp.left.left.right.right.data);//u_NULL_R
		System.out.println(temp.left.right.data);//a
		System.out.println(temp.left.right.left.data);//r
		System.out.println(temp.left.right.left.left.data);//l
		System.out.println(temp.left.right.left.left.left.data);//l_NULL_L
		System.out.println(temp.left.right.left.left.right.data);//l_NULL_R
		System.out.println(temp.left.right.left.right.data);//r_NULL_R
		System.out.println(temp.left.right.right.data);//w
		System.out.println(temp.left.right.right.left.data);//p
		System.out.println(temp.left.right.right.left.left.data);//p_NULL_L
		System.out.println(temp.left.right.right.left.right.data);//p_NULL_R
		System.out.println(temp.left.right.right.right.data);//j
		System.out.println(temp.left.right.right.right.left.data);//j_NULL_L
		System.out.println(temp.left.right.right.right.right.data);//j_NULL_R
		
		System.out.println(temp.right.data);//t
		System.out.println(temp.right.left.data);//n
		System.out.println(temp.right.left.left.data);//d
		System.out.println(temp.right.left.left.left.data);//b
		System.out.println(temp.right.left.left.left.left.data);//b_NULL_L
		System.out.println(temp.right.left.left.left.right.data);//b_NULL_R
		System.out.println(temp.right.left.left.right.data);//x
		System.out.println(temp.right.left.left.right.left.data);//x_NULL_L
		System.out.println(temp.right.left.left.right.right.data);//x_NULL_R
		System.out.println(temp.right.left.right.data);//k
		System.out.println(temp.right.left.right.left.data);//c
		System.out.println(temp.right.left.right.left.left.data);//c_NULL_L
		System.out.println(temp.right.left.right.left.right.data);//c_NULL_R
		System.out.println(temp.right.left.right.right.data);//y
		System.out.println(temp.right.left.right.right.left.data);//y_NULL_L
		System.out.println(temp.right.left.right.right.right.data);//y_NULL_R
		System.out.println(temp.right.right.data);//m
		System.out.println(temp.right.right.left.data);//g
		System.out.println(temp.right.right.left.left.data);//z
		System.out.println(temp.right.right.left.left.left.data);//z_NULL_L
		System.out.println(temp.right.right.left.left.right.data);//z_NULL_R
		System.out.println(temp.right.right.left.right.data);//q
		System.out.println(temp.right.right.left.right.left.data);//q_NULL_L
		System.out.println(temp.right.right.left.right.right.data);//q_NULL_R
		System.out.println(temp.right.right.right.data);//o
		System.out.println(temp.right.right.right.left.data);//o_NULL_L
		System.out.println(temp.right.right.right.right.data);//o_NULL_R
		
	}
	*/
}
public class Morse_Code_Codec {
	public static void main(String[] args) {
		try {
			List<String> tree1 = new List<String>();
		    System.out.println("Starting list insertion...");
		    //
		    FileReader file1 = new FileReader("C:\\Users\\Richards\\Documents\\MTRE\\ELEC\\Data Structures - CS3305\\morse.txt");
		    BufferedReader buffer1 = new BufferedReader(file1);
		    Scanner scan1 = new Scanner(buffer1);
		    
			LinkedList<String> list2 = new LinkedList<String>();
			
			while(scan1.hasNextLine()) {
				list2.add(scan1.nextLine());
			}
			while(list2.isEmpty()==false) {
				if(list2.getFirst().toString().length()>1) {
					tree1.add_Node("NULL");
				}
				else {
					tree1.add_Node(list2.getFirst());
				}
				list2.remove();
			}
			
			Scanner input1 = new Scanner(System.in);
			int input = 0;
			while(input!=3) {
				input=0;
				System.out.println("1.) Encode a string message");
				System.out.println("2.) Decode a morse code message");
				System.out.println("3.) Exit");
				input = input1.nextInt();
				switch(input) {
					case 1:
						Scanner temp = new Scanner(System.in);
						String message = "";
						System.out.println("Enter the message: ");
						message = temp.nextLine();
						System.out.println("Encoded Message: "+tree1.encode(message));
						//temp.close();
						break;
					case 2:
						Scanner temp2 = new Scanner(System.in);
						String message2 = "";
						System.out.println("Enter the morse code: ");
						message2 = temp2.nextLine();
						System.out.println("Decoded Message: "+tree1.decode(message2));
						//temp2.close();
						break;
					case 3:	
						System.out.println("Exiting program...");
						break;
					default:
						break;
				}
			}
			input1.close();
			scan1.close();
			//In case the file reading does not work
			//Below is manual inputs as proof of insertion algorithm working
			/*
			list1.add("*");
			list1.add("e");
			list1.add("i");
			list1.add("s");
			list1.add("h");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("v");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("u");
			list1.add("f");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("a");
			list1.add("r");
			list1.add("l");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("w");
			list1.add("p");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("j");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("t");
			list1.add("n");
			list1.add("d");
			list1.add("b");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("x");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("k");
			list1.add("c");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("y");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("m");
			list1.add("g");
			list1.add("z");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("q");
			list1.add("NULL");
			list1.add("NULL");
			list1.add("o");
			list1.add("NULL");
			list1.add("NULL");
			
			while(list1.isEmpty()==false) {
				tree1.add_Node(list1.getFirst());
				list1.remove();
			}
			
			tree1.show();
			*/
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
