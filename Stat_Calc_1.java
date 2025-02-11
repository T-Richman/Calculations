package calculations;
import java.util.*;

public class Stat_Calc_1 {

	static double mean_calc(LinkedList<Double> val) {
		double total=0.0;
		for(double v:val) {
			total+=v;
		}
		double mu = total/val.size();
		return mu;
	}
	
	static double stan_dev_calc(LinkedList<Double> val, double mean) {
		double total=0.0;
		for(double v:val) {
			total+=(v-mean)*(v-mean);
		}
		double sigma = Math.sqrt(total/(val.size()-1)); 
		return sigma;
	}
	
	static double corr_coeff_calc(LinkedList<Double> x_val, double x_bar, 
			LinkedList<Double> y_val, double y_bar) {
		double total_1 = 0.0;
		double total_2 = 0.0;
		double total_3 = 0.0;
		
		for(double x:x_val) {
			total_2+=(x-x_bar)*(x-x_bar);
		}
		System.out.println(total_2);
		for(double y:x_val) {
			total_3+=(y-y_bar)*(y-y_bar);
		}
		System.out.println(total_3);
		while(x_val.isEmpty()==false&&y_val.isEmpty()==false) {
			total_1+=(x_val.getFirst()-x_bar)*(y_val.getFirst()-y_bar);
			x_val.removeFirst();
			y_val.removeFirst();
		}
		
		double r = total_1/(Math.sqrt(total_2*total_3));
		
		return r;
	}
	
	static double slope_calc(double r, double stan_dev_x, double stan_dev_y) {
		double m=0.0;
		m = (r*stan_dev_y)/stan_dev_x;
		return m;
	}
	
	static double y_int_calc(double m, double x_bar, double y_bar) {
		double y=0.0;
		y = y_bar-(m*x_bar);
		return y;
	}
	
	public static void main(String[] args) {
		Scanner scan_1 = new Scanner(System.in);
		int input1=-1;
		while(input1!=0) {
			scan_1.reset();
			System.out.println("Enter the sample size (n): ");
			int n=scan_1.nextInt();
			scan_1.reset();
			
			LinkedList<Double> x_val = new LinkedList<Double>();
			LinkedList<Double> y_val = new LinkedList<Double>();
			System.out.println("Enter the x-values: ");
			for(int i=0;i<n;i++) {
				x_val.add(scan_1.nextDouble());
			}
			scan_1.reset();
			
			System.out.println("Enter the y-values: ");
			for(int i=0;i<n;i++) {
				y_val.add(scan_1.nextDouble());
			}
			scan_1.reset();
			
			double x_bar = mean_calc(x_val);
			double y_bar = mean_calc(y_val);
			double stan_dev_x = stan_dev_calc(x_val, x_bar);
			double stan_dev_y = stan_dev_calc(y_val, y_bar);
			double corr_coeff = corr_coeff_calc(x_val, x_bar, y_val, y_bar);
			double slope = slope_calc(corr_coeff, stan_dev_x, stan_dev_y);
			double y_int = y_int_calc(slope, x_bar, y_bar);
			
			System.out.println("Mean x-values: "+x_bar);
			System.out.println("Mean y-values: "+y_bar);
			System.out.println("Standard Deviation x-values: "+stan_dev_x);
			System.out.println("Standard Deviation y-values: "+stan_dev_y);
			System.out.println("Correlation Coeeficient: "+corr_coeff);
			System.out.println("Slope: "+slope);
			System.out.println("y-Intercept: "+y_int);
			
			System.out.println("");
			System.out.println("Enter another sample?");
			System.out.println("0) No");
			System.out.println("1) Yes");
			input1=scan_1.nextInt();
		}
	}

}
