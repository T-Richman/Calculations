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
				//System.out.println(result[result_ind]);
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
	public static LinkedList<Double> sort_sv(LinkedList<String> eqtn_list, LinkedList<Double> sv_old_list) {
		LinkedList<String> val_list = new LinkedList<>();
		LinkedList<Double> sv_list = new LinkedList<>();
		for(String s:eqtn_list) {
			val_list.add(s);
		}
		for(double d:sv_old_list) {
			sv_list.add(d);
		}
		val_list.removeIf(v->v.equals("+")||v.equals("-")||v.equals("*")||v.equals("/"));
		for(int i=0;i<val_list.size();i++) {
			double comp=Double.valueOf(val_list.get(i)).doubleValue();
			double comp_next=0;
			double comp_prev=0;
			if(i<val_list.size()-1) {
				comp_next=Double.valueOf(val_list.get(i+1)).doubleValue();
			}
			if(i>0){
				comp_prev=Double.valueOf(val_list.get(i-1)).doubleValue();
			}
			double temp=0;
			//System.out.println(comp);
			for(int j=0;j<sv_list.size();j++) {
				//System.out.println("Same Value: "+(sv_list.get(j)==comp));
				if(sv_list.get(j)==comp && j==i) {
					//System.out.println("Wrong Right: "+(i<val_list.size()-1 && sv_list.get(j+1)!=comp_next));
					if(i<val_list.size()-1 && sv_list.get(j+1)!=comp_next) {
						int temp_ind=sv_list.subList(j+1, sv_list.size()).indexOf(comp_next);
						int ind_next=sv_list.size()-sv_list.subList(j+1, sv_list.size()).size()+temp_ind;
						temp=sv_list.get(j+1);
						sv_list.set(j+1, sv_list.get(ind_next));
						sv_list.set(ind_next, temp);
						break;
					}
					else {
						//System.out.println("Wrong Left: "+(i>0 && sv_list.get(j-1)!=comp_prev));
						if(i>0 && sv_list.get(j-1)!=comp_prev) {
							int ind_prev=sv_list.subList(0, j).lastIndexOf(comp_prev)-1;
							temp=sv_list.get(j-1);
							sv_list.set(j-1, sv_list.get(ind_prev));
							sv_list.set(ind_prev, temp);
							break;
						}
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
		
		double num1=0;
		double num2=0;
		double result=0;
		//for each operation, checks which string-value is an operator
		for(int i=(sym.length-1);i>-1;i--) {
			System.out.println("Operation: "+sym[i]);
			int num1_ind=0;
			int num2_ind=0;
			for(int j=1;j<eqtn_list.size()-1;j++) {
				if(eqtn_list.get(j).length()==1 && eqtn_list.get(j).charAt(0)==sym[i].charAt(0)) {
					num1=Double.valueOf(eqtn_list.get(j-1)).doubleValue();
					num2=Double.valueOf(eqtn_list.get(j+1)).doubleValue();
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
					System.out.println("Number 1: "+num1+", Number 2: "+num2);
					System.out.println("Result: "+result);
					eqtn_list.set(j, String.valueOf(result));
					eqtn_list.remove(j+1);
					eqtn_list.remove(j-1);
					sv_list.set(num1_ind, result);
					sv_list.remove(num2_ind);
					val_list=sv_list;
					j=1;
				}
			}
		}
		return sv_list;
	}
	
	public static void main(String[] args) {
		//Gets string from user input
		//Ex 1: 9+2*11-5*6-10/2-100+24/12*2/8
		//Ex 2: 2+4+55*6*5/3/6*5-1-1-8
		Scanner s1 = new Scanner(System.in);
		System.out.println("Enter your equation: ");
		String eqtn = s1.nextLine();
		s1.close();
		
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
		
		LinkedList<String[]> main_layer=split_eqtn(eqtn,"\\+");
		String[] plus_arr=convert_LinkedList(main_layer);
		for(int i=0;i<plus_arr.length;i++) {
			try {
				sv_list.add(Double.valueOf(plus_arr[i]).doubleValue());
			}
			catch(NumberFormatException e) {
				//System.out.println("Cannot add the following as a single-value: "+plus_arr[i]);
			}
		}
		
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
				//System.out.println("Cannot add the following as a single-value: "+minus_arr[i]);
			}
		}

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
				//System.out.println("Cannot add the following as a single-value: "+multiply_arr[i]);
			}
		}
		
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
				//System.out.println("Cannot add the following as a single-value: "+divide_arr[i]);
			}
		}
		
		sv_list=sort_sv(eqtn_list, sv_list);
		/*
		for(String s:eqtn_list) {
			System.out.print(s+" ");
		}
		System.out.println(" ");
		for(double d:sv_list) {
			System.out.print(d+" ");
		}
		System.out.println(" ");
		*/
		
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
