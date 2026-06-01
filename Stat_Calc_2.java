package calculators;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class SC2Page implements ActionListener{
	private JFrame mainFrame = new JFrame();
	private JPanel inPanel = new JPanel();
	private JPanel outPanel = new JPanel();
	private JLabel meanLabel = new JLabel();
	private JLabel varianceLabel = new JLabel();
	private JLabel standevLabel = new JLabel();
	private JLabel binDistLabel = new JLabel();
	private JLabel bdMeanLabel = new JLabel();
	private JLabel bdStandevLabel = new JLabel();
	private JTextField randVar = new JTextField();
	private JTextField probVar = new JTextField();
	private JTextField npkVal = new JTextField();
	private JButton discRandButton = new JButton("Discrete Random Variables");
	private JButton binDistButton = new JButton("Binomial Distribution");
	private JButton submitButton = new JButton("Submit");
	private int dist_in;
	private int dist_out;
	private int text_height;
	private int max_char;
	private int option;
	
	public SC2Page() {
		dist_in = 40;
		dist_out = 20;
		text_height = 20;
		max_char = 250;
		option = 0;
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(640, 320);
		mainFrame.setVisible(true);
		mainFrame.setLayout(null);
		
		inPanel.setBounds(0,0,mainFrame.getWidth()/2,mainFrame.getHeight());
		inPanel.setLayout(null);
		outPanel.setBounds(inPanel.getWidth(),0,mainFrame.getWidth()/2,mainFrame.getHeight());
		outPanel.setLayout(null);
		
		discRandButton.setBounds(20, 20, 100, 40);
		discRandButton.setFocusable(false);
		discRandButton.addActionListener(this);
		
		binDistButton.setBounds(discRandButton.getX()+discRandButton.getWidth()+dist_in, 20, 100, 40);
		binDistButton.setFocusable(false);
		binDistButton.addActionListener(this);
		
		randVar.setBounds(20, discRandButton.getY()+discRandButton.getHeight()+dist_in, max_char, text_height);
		randVar.setToolTipText("Enter discrete randon variables");
		probVar.setBounds(20, randVar.getY()+dist_in, max_char, text_height);
		probVar.setToolTipText("Enter each variable's probability");
		npkVal.setBounds(20, discRandButton.getY()+discRandButton.getHeight()+dist_in, max_char, text_height);
		npkVal.setToolTipText("Enter n, p, and k");
		
		submitButton.setBounds(20, discRandButton.getY()+(dist_in*5), 100, 40);
		submitButton.setText("Submit");
		submitButton.setFocusable(false);
		submitButton.addActionListener(this);
		
	}
	
	public void setup() {
		
		inPanel.add(discRandButton);
		inPanel.add(binDistButton);
		inPanel.add(submitButton);
		inPanel.add(randVar);
		inPanel.add(probVar);
		inPanel.add(npkVal);
		outPanel.add(meanLabel);
		outPanel.add(varianceLabel);
		outPanel.add(standevLabel);
		outPanel.add(binDistLabel);
		outPanel.add(bdMeanLabel);
		outPanel.add(bdStandevLabel);
		
		randVar.setVisible(false);
		probVar.setVisible(false);
		meanLabel.setVisible(false);
		varianceLabel.setVisible(false);
		standevLabel.setVisible(false);
		npkVal.setVisible(false);
		binDistLabel.setVisible(false);
		bdMeanLabel.setVisible(false);
		bdStandevLabel.setVisible(false);
		
		mainFrame.add(inPanel);
		mainFrame.add(outPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==discRandButton) {
			randVar.setVisible(true);
			probVar.setVisible(true);
			meanLabel.setVisible(true);
			varianceLabel.setVisible(true);
			standevLabel.setVisible(true);
			option = 1;
			npkVal.setVisible(false);
			binDistLabel.setVisible(false);
			bdMeanLabel.setVisible(false);
			bdStandevLabel.setVisible(false);
		}
		if(e.getSource()==binDistButton) {
			npkVal.setVisible(true);
			binDistLabel.setVisible(true);
			bdMeanLabel.setVisible(true);
			bdStandevLabel.setVisible(true);
			option = 2;
			randVar.setVisible(false);
			probVar.setVisible(false);
			meanLabel.setVisible(false);
			varianceLabel.setVisible(false);
			standevLabel.setVisible(false);
		}
		if(e.getSource()==submitButton) {
			switch(option) {
				case 1:
					String[] var = randVar.getText().split(",");
					LinkedList<Double> var_val = new LinkedList<Double>();
					for(int i=0;i<var.length;i++) {
						var_val.add(Double.valueOf(var[i]));
					}
					String[] prob = probVar.getText().split(",");
					LinkedList<Double> prob_val = new LinkedList<Double>();
					for(int i=0;i<prob.length;i++) {
						prob_val.add(Double.valueOf(prob[i]));
					}
					
					int size=0;
					if (var_val.size()==prob_val.size()) {
						size=var_val.size();
						double sum=0;
						for(double i:prob_val) {
							sum+=i;
						}
						if (sum==1) {
							double mean = Stat_Calc_2.mean_value_calc(var_val,prob_val,size);
							double variance = Stat_Calc_2.variance_calc(var_val,prob_val,size);
							double standev = Stat_Calc_2.stan_dev_calc(variance);
							draw_disc_rand(mean, variance, standev);
						}
						else {
							JOptionPane.showMessageDialog(null, "The probabilities must lead to a total sum of 1", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "The number of variables and probabilities must match", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
					}
					break;
				case 2:
					String[] npk_temp = npkVal.getText().split(",");
					double[] npk = new double[npk_temp.length];
					for(int i=0;i<npk_temp.length;i++) {
						npk[i]=Double.valueOf(npk_temp[i]);
					}
					
					if(npk.length==3) {
						if(npk[2]<npk[0]) {
							if(npk[1]<1 && npk[1]>0) {
								double binDist = Stat_Calc_2.bin_dist_calc(npk[0], npk[1], npk[2]);
								double bd_mean = (npk[0] * npk[1]);
								double bd_standev = Math.sqrt(npk[0]*npk[1]*(1-npk[1]));
								draw_bin_dist(binDist, bd_mean, bd_standev);
							}
							else
							{
								JOptionPane.showMessageDialog(null, "The probability (p) cannot be above 1 or below 0", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "The successes (k) must not exceed the trials (n)", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please only enter the following separated by commas: n, p, k", "Invalid Input", JOptionPane.PLAIN_MESSAGE);
					}
					
					break;
				default: 
					break;
			}
		}
	}
	
	public void draw_disc_rand(double x, double y, double z) {
		meanLabel.setText("Mean/Expected Value: "+x);
		meanLabel.setBounds(20, 20, max_char, text_height);
		varianceLabel.setText("Variance Value: "+y);
		varianceLabel.setBounds(20, meanLabel.getY()+dist_out, max_char, text_height);
		standevLabel.setText("Standard Deviation Value: "+x);
		standevLabel.setBounds(20, varianceLabel.getY()+dist_out, max_char, text_height);
		
	}
	public void draw_bin_dist(double x, double y, double z) {
		binDistLabel.setText("Exact Binomial Distribution: "+x);
		binDistLabel.setBounds(20, 20, max_char, text_height);
		bdMeanLabel.setText("Mean Value: "+y);
		bdMeanLabel.setBounds(20, binDistLabel.getY()+dist_out, max_char, text_height);
		bdStandevLabel.setText("Standard Deviation Value: "+z);
		bdStandevLabel.setBounds(20, bdMeanLabel.getY()+dist_out, max_char, text_height);
		
	}
}

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

  public static double bin_dist_calc(double n, double p, double k){
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
    SC2Page mp = new SC2Page();
    mp.setup();
  }
  
}
