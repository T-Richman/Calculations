package project_classes;
import java.util.*;

class Node{
	public String data;
	public String parent;
	public LinkedList<Node> children;
	public Node(String val) {
		data=val;
		children=new LinkedList<>();
	}
	public void set_parent(String val) {
		parent=val;
	}
}

public class PEDMAS_Calc {
	//Separates string by the given character/operator
	public static LinkedList<String[]> split_eqtn(String s, String c) {
		LinkedList<String[]> list_layer=new LinkedList<>();
		list_layer.add(s.split(c));
		return split_eqtn(list_layer,c);
	}
	//Separates each string in LinkedList element by the given character/operator
	public static LinkedList<String[]> split_eqtn(LinkedList<String[]> l, String c) {
		LinkedList<String[]> next_layer=new LinkedList<>();
		for(String[] s:l) {
			for(int i=0;i<s.length;i++) {
				next_layer.add(s[i].split(c));
			}
		}
		return next_layer;
	}
	//Converts LinkedList storing arrays back into a 1D array for filtering
	public static String[] convert_LinkedList(LinkedList<String[]> list) {
		int arr_size=0;
		for(String[] l:list) {
			for(int i=0;i<l.length;i++) {
				//System.out.println(l[i]);
				arr_size++;
			}
		}
		String[] arr=new String[arr_size];
		int n=0;
		for(String[] l:list) {
			for(int i=0;i<l.length;i++) {
				arr[n]=l[i];
				n++;
			}
		}
		return arr;
	}
	//Filters and removes any duplicates/leafs of the equation for the next array/LinkedList
	public static String[] filter_eqtn(String[] prev_arr, String[] next_arr) {
		//Compares the arrays and counts the number of copies, sets copies in next array to NULL
		int copy_cnt=0;
		int sv_flag=0;
		for(int i=0;i<prev_arr.length;i++) {
			for(int j=0;j<next_arr.length;j++) {
				sv_flag=convert_String_Double(next_arr[j]);
				if(prev_arr[i]==next_arr[j] && sv_flag==1) {
					next_arr[j]="NULL";
					copy_cnt++;
				}
			}
		}
		//Transfers next arrays non-copies or subequations to the result array
		String[] result = new String[next_arr.length-copy_cnt];
		int result_ind=0;
		for (int i=0;i<next_arr.length;i++) {
			sv_flag=convert_String_Double(next_arr[i]);
			if(next_arr[i]!="NULL") {
				result[result_ind]=next_arr[i];
				System.out.println(result[result_ind]);
				result_ind++;
			}
		}
		return result;
	}
	//Converts String to Double value(1), catches exception if cannot be converted(0)
	public static int convert_String_Double(String s) {
		int sv_flag=0;
		try {
			double val=Double.valueOf(s).doubleValue();
			sv_flag=1;
		}
		catch(NumberFormatException e) {
			sv_flag=0;
		}
		return sv_flag;
	}
	//Sorts the single-values LinkedList to properly match the equation
	public static LinkedList<Double> sort_sv(LinkedList<String> eqtn_list, LinkedList<Double> sv_list) {
		LinkedList<String> val_list = new LinkedList<>();
		for(String s:eqtn_list) {
			val_list.add(s);
		}
		val_list.removeIf(v->v.equals("+")||v.equals("-")||v.equals("*")||v.equals("/"));
		for(String s:val_list) {
			double comp=Double.valueOf(s).doubleValue();
			double temp=0;
			for(double d:sv_list) {
				if(comp==d) {
					if(sv_list.indexOf(d)<val_list.indexOf(s)) {
						temp=sv_list.get(sv_list.indexOf(d)+1);
						sv_list.set(sv_list.indexOf(d)+1, d);
						sv_list.set(sv_list.indexOf(d), temp);
					}
					else if(sv_list.indexOf(d)>val_list.indexOf(s)) {
						temp=sv_list.get(sv_list.indexOf(d)-1);
						sv_list.set(sv_list.indexOf(d)-1, d);
						sv_list.set(sv_list.indexOf(d), temp);
					}
				}
			}
		}
		return sv_list;
	}
	//Repeatedly performs operation is PEDMAS order, leaving last pair for final operation
	// 10/4/2 Performs order div-mul-sub-add, exp and par will be added someday
	public static LinkedList<Double> operate_sv(LinkedList<String> eqtn_list, LinkedList<Double> val_list, String[] sym){
		LinkedList<Double> sv_list = new LinkedList<>();
		LinkedList<String> noneq_list = new LinkedList<>();
		
		for(double d:val_list) {
			sv_list.add(d);
		}
		for(String s:eqtn_list) {
			noneq_list.add(s);
		}
		val_list.removeIf(v->v.equals("+")||v.equals("-")||v.equals("*")||v.equals("/"));
		
		int num1_ind=0;
		int num2_ind=0;
		double num1=0;
		double num2=0;
		double result=0;
		//for each operation, checks which string-value is an operator
		for(int i=(sym.length-1);i>-1;i--) {
			System.out.println("Symbol:"+sym[i]);
			for(int j=0;j<eqtn_list.size();j++) {
				//System.out.println("Eqtn Sym:"+eqtn_list.get(j)+" Sym Sym:"+sym[i]);
				//System.out.println(eqtn_list.get(j).length()==1 && eqtn_list.get(j).charAt(0)==sym[i].charAt(0));
				if(eqtn_list.get(j).length()==1 && eqtn_list.get(j).charAt(0)==sym[i].charAt(0)) {
					num1=Double.valueOf(eqtn_list.get(j-1)).doubleValue();
					num2=Double.valueOf(eqtn_list.get(j+1)).doubleValue();
					System.out.println("Number 1: "+num1+", Number 2: "+num2);
					for(int k=0;k<sv_list.size()-1;k++) {
						if(sv_list.get(k)==num1 && sv_list.get(k+1)==num2) {
							num1_ind=k;
						}
					}
					for(int k=1;k<sv_list.size();k++) {
						if(sv_list.get(k)==num2 && sv_list.get(k-1)==num1) {
							num2_ind=k;
						}
					}
					switch(sym[i]) {
					case "+":
						result=sv_list.get(num1_ind)+sv_list.get(num2_ind);
						break;
					case "-":
						result=sv_list.get(num1_ind)-sv_list.get(num2_ind);
						break;
					case "*":
						result=sv_list.get(num1_ind)*sv_list.get(num2_ind);
						break;
					case "/":
						result=sv_list.get(num1_ind)/sv_list.get(num2_ind);
						break;
					}
					System.out.println("Result: "+result);
					eqtn_list.set(j, String.valueOf(result));
					eqtn_list.remove(j+1);
					eqtn_list.remove(j-1);
					for(String s:eqtn_list) {
						System.out.print(s+" ");
					}
					System.out.println(" ");
					sv_list.set(num1_ind, result);
					sv_list.remove(num2_ind);
					val_list=sv_list;
					for(double d:val_list) {
						System.out.print(d+" ");
					}
					System.out.println(" ");
					System.out.println("-------------------------");
					j=1;
				}
			}
		}
		return sv_list;
	}
	
	public static void main(String[] args) {
		//Gets string and finds any parentheses pairs
		String eqtn = "9+2*11-5*6-10/2-100+24/12*2/8";
		System.out.println(eqtn);
		LinkedList<Double> sv_list = new LinkedList<>();
		LinkedList<String> eqtn_list = new LinkedList<>();
		String[] sym = {"+", "-", "*", "/"};
		String val="";
		for(int i=0;i<eqtn.length();i++) {
			boolean check_1=eqtn.charAt(i)=='+';
			boolean check_2=eqtn.charAt(i)=='-';
			boolean check_3=eqtn.charAt(i)=='*';
			boolean check_4=eqtn.charAt(i)=='/';
			if((check_1==false) && (check_2==false) && (check_3==false) && (check_4==false)) {
				val+=eqtn.charAt(i);
			}
			else {
				eqtn_list.add(val);
				eqtn_list.add(String.valueOf(eqtn.charAt(i)));
				val="";
			}
		}
		eqtn_list.add(val);
		
		System.out.println("Plus Layer Parts:");
		LinkedList<String[]> main_layer=split_eqtn(eqtn,"\\+");
		String[] plus_arr=convert_LinkedList(main_layer);
		for(int i=0;i<plus_arr.length;i++) {
			try {
				sv_list.add(Double.valueOf(plus_arr[i]).doubleValue());
			}
			catch(NumberFormatException e) {
				System.out.println("Cannot add the following as a single-value: "+plus_arr[i]);
			}
		}
		System.out.println("===========================================================");
		
		System.out.println("Minus Layer Parts:");
		main_layer=split_eqtn(main_layer,"-");
		String[] minus_arr=convert_LinkedList(main_layer);
		minus_arr=filter_eqtn(plus_arr,minus_arr);
		main_layer.removeAll(main_layer);
		main_layer.add(minus_arr);
		for(int i=0;i<minus_arr.length;i++) {
			try {
				sv_list.add(Double.valueOf(minus_arr[i]).doubleValue());
			}
			catch(NumberFormatException e) {
				System.out.println("Cannot add the following as a single-value: "+minus_arr[i]);
			}
		}
		System.out.println("===========================================================");
		
		System.out.println("Multiply Layer Parts:");
		main_layer=split_eqtn(main_layer,"\\*");
		String[] multiply_arr=convert_LinkedList(main_layer);
		LinkedList<Node> nodelist_2 = new LinkedList<>();
		multiply_arr=filter_eqtn(minus_arr,multiply_arr);
		main_layer.removeAll(main_layer);
		main_layer.add(multiply_arr);
		for(int i=0;i<multiply_arr.length;i++) {
			try {
				sv_list.add(Double.valueOf(multiply_arr[i]).doubleValue());
			}
			catch(NumberFormatException e) {
				System.out.println("Cannot add the following as a single-value: "+multiply_arr[i]);
			}
		}
		System.out.println("===========================================================");
		
		System.out.println("Divide Layer Parts:");
		main_layer=split_eqtn(main_layer,"/");
		String[] divide_arr=convert_LinkedList(main_layer);
		LinkedList<Node> nodelist_3 = new LinkedList<>();
		divide_arr=filter_eqtn(multiply_arr,divide_arr);
		main_layer.removeAll(main_layer);
		main_layer.add(divide_arr);
		for(int i=0;i<divide_arr.length;i++) {
			try {
				sv_list.add(Double.valueOf(divide_arr[i]).doubleValue());
			}
			catch(NumberFormatException e) {
				System.out.println("Cannot add the following as a single-value: "+divide_arr[i]);
			}
		}
		System.out.println("===========================================================");
		
		sv_list=sort_sv(eqtn_list, sv_list);
		
		for(String s:eqtn_list) {
			System.out.print(s+" ");
		}
		System.out.println(" ");
		for(double d:sv_list) {
			System.out.print(d+" ");
		}
		System.out.println(" ");
		System.out.println("===========================================================");
		
		sv_list=operate_sv(eqtn_list,sv_list,sym);
		double result=0;
		if(eqtn_list.contains("+")) {
			result=sv_list.getFirst()+sv_list.getLast();
		}
		else if(eqtn_list.contains("-")) {
			result=sv_list.getFirst()-sv_list.getLast();
		}
		else if(eqtn_list.contains("*")) {
			result=sv_list.getFirst()*sv_list.getLast();
		}
		else if(eqtn_list.contains("/")) {
			result=sv_list.getFirst()/sv_list.getLast();
		}
		System.out.println("Answer:"+result);
	}
}
