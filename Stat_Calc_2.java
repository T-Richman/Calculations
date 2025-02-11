package calculations;
import java.util.Scanner;
import java.util.LinkedList;

public class Stat_Calc_2 {
  public static double mean_value_calc(LinkedList<Double> x_Arr, LinkedList<Double> p_Arr, int n){
    double bar_value = 0;
    for (int i=0;i<x_Arr.size();i++) {
      bar_value += x_Arr.get(i)*p_Arr.get(i);
    }
    return bar_value;
  }
  
  public static double  variance_calc(LinkedList<Double> x_Arr, LinkedList<Double> p_Arr, double mu){
    double s2 = 0;
    for (int i=0;i<x_Arr.size();i++) {
      s2 += ((x_Arr.get(i)-mu)*(x_Arr.get(i)-mu))*p_Arr.get(i);
    }
    //sigma = sum/(n-1);
    return s2;
  }

  public static double stan_dev_calc(double sigma){
    return Math.sqrt(sigma);
  }

  public static double bin_dist_calc(double n, double k, double p){
    //recursion needed for cumulatives
    //System.out.println()
    return bin_coeff_calc(n, k)*Math.pow(p, k)*Math.pow(1-p,n-k);
  }
  
  public static double bin_coeff_calc(double n, double k){
    return fact(n)/(fact(k)*fact(n-k));
  }

  public static double fact(double  n){
    if(n==1){
      return 1;
    }
    else{
      return n*fact(n-1);
    }
  }
  
  public static void main(String[] args) {
    Scanner input1 = new Scanner(System.in);
    System.out.println("Calculations for a sample");

    int choice1=10;
    while(choice1!=0){
      System.out.println("0. Quit");
      System.out.println("1. Find discrete random variables properties");
      System.out.println("2. Find binomial distribution properties");
      choice1=input1.nextInt();

      switch(choice1){
        case 1:
          System.out.println("Enter the sample size: ");
          int n = input1.nextInt();

          LinkedList<Double> x_Arr = new LinkedList<Double>();
          System.out.println("Enter the discrete random variables: ");
          for (int i = 0; i < n; i++) {
            x_Arr.add(input1.nextDouble());
          }

          LinkedList<Double> p_Arr = new LinkedList<Double>();
          System.out.println("Enter each variable's probability: ");
          for (int i = 0; i < n; i++) {
            p_Arr.add(input1.nextDouble());
          }

          System.out.println("Complete. Processing...");

          double mu = mean_value_calc(x_Arr, p_Arr, n);
          System.out.println("Mean/Expected Value of x: " + mu);
          double s2 = variance_calc(x_Arr, p_Arr, mu);
          System.out.println("Variance Value of x: " + s2);
          double sigma = stan_dev_calc(s2);
          System.out.println("Standard Deviation Value of x: " + sigma);
          break;
        case 2:
          System.out.println("Enter n: ");
          double n1 = input1.nextDouble();
          System.out.println("Enter p: ");
          double p1 = input1.nextDouble();
          System.out.println("Enter k: ");
          double k1 = input1.nextDouble();

          double bin_dist = bin_dist_calc(n1, k1, p1);
          System.out.println("Exact Binomial Distribution: " + bin_dist);
          System.out.println("Mean: "+(n1*p1));
          System.out.println("Standard Deviation: "+Math.sqrt(n1*p1*(1-p1)));
          break;
        default:
          System.out.println("Quitting program...");
          break;
      }
    }
  }
}