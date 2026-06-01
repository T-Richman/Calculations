package calculators;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

class SC1Page implements ActionListener{
	private JFrame mainFrame = new JFrame();
	private JPanel inPanel = new JPanel();
	private JPanel outPanel = new JPanel();
	private JLabel meanXLabel = new JLabel();
	private JLabel meanYLabel = new JLabel();
	private JLabel standevXLabel = new JLabel();
	private JLabel standevYLabel = new JLabel();
	private JLabel corrCoeffLabel = new JLabel();
	private JLabel slopeLabel = new JLabel();
	private JLabel yIntLabel = new JLabel();
	private JTextField valuesX = new JTextField();
	private JTextField valuesY = new JTextField();
	private JButton submitButton = new JButton("Submit");
	private int dist_in;
	private int dist_out;
	private int text_height;
	private int max_char;
	
	public SC1Page() {
		dist_in = 40;
		dist_out = 20;
		text_height = 20;
		max_char = 250;
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(640, 320);
		mainFrame.setVisible(true);
		mainFrame.setLayout(null);
		
		inPanel.setBounds(0,0,mainFrame.getWidth()/2,mainFrame.getHeight());
		inPanel.setLayout(null);
		outPanel.setBounds(inPanel.getWidth(),0,mainFrame.getWidth()/2,mainFrame.getHeight());
		outPanel.setLayout(null);
		
		valuesX.setBounds(20, 20, max_char, text_height);
		valuesX.setToolTipText("Enter x-values");
		valuesY.setBounds(20, valuesX.getY()+dist_in, max_char, text_height);
		valuesY.setToolTipText("Enter y-values");
		
		submitButton.setBounds(20, valuesY.getY()+dist_in, 100, 40);
		submitButton.setText("Submit");
		submitButton.setFocusable(false);
		submitButton.addActionListener(this);
		
	}
	
	public void setup() {
		
		inPanel.add(valuesX);
		inPanel.add(valuesY);
		inPanel.add(submitButton);
		
		outPanel.add(meanXLabel);
		outPanel.add(meanYLabel);
		outPanel.add(standevXLabel);
		outPanel.add(standevYLabel);
		outPanel.add(corrCoeffLabel);
		outPanel.add(slopeLabel);
		outPanel.add(yIntLabel);
		mainFrame.add(inPanel);
		mainFrame.add(outPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==submitButton) {
			String[] x = valuesX.getText().split(",");
			LinkedList<Double> x_val = new LinkedList<Double>();
			for(int i=0;i<x.length;i++) {
				x_val.add(Double.valueOf(x[i]));
			}
			String[] y = valuesY.getText().split(",");
			LinkedList<Double> y_val = new LinkedList<Double>();
			for(int i=0;i<y.length;i++) {
				y_val.add(Double.valueOf(y[i]));
			}
			
			double x_bar = Stat_Calc_1.mean_calc(x_val);
			double y_bar = Stat_Calc_1.mean_calc(y_val);
			draw_mean(x_bar,y_bar);
			double standev_x = Stat_Calc_1.stan_dev_calc(x_val,x_bar);
			double standev_y = Stat_Calc_1.stan_dev_calc(y_val,y_bar);
			draw_standev(standev_x,standev_y);
			double corr_coeff = Stat_Calc_1.corr_coeff_calc(x_val, x_bar, y_val, y_bar);
			double slope = Stat_Calc_1.slope_calc(corr_coeff, standev_x, standev_y);
			double y_int = Stat_Calc_1.y_int_calc(slope, x_bar, y_bar);
			draw_misc(corr_coeff,slope,y_int);
			
		}
	}
	
	public void draw_mean(double x, double y) {
		meanXLabel.setText("Mean X-Values: "+x);
		meanXLabel.setBounds(20, 20, max_char, text_height);
		meanYLabel.setText("Mean Y-Values: "+y);
		meanYLabel.setBounds(20, meanXLabel.getY()+dist_out, max_char, text_height);
	}
	
	public void draw_standev(double x, double y) {
		standevXLabel.setText("Standard Deviation X-Values: "+x);
		standevXLabel.setBounds(20, meanYLabel.getY()+dist_out, max_char, text_height);
		standevYLabel.setText("Standard Deviation Y-Values: "+y);
		standevYLabel.setBounds(20, standevXLabel.getY()+dist_out, max_char, text_height);
	}
	
	public void draw_misc(double x, double y, double z) {
		corrCoeffLabel.setText("Correlation Coefficient: "+x);
		corrCoeffLabel.setBounds(20, standevYLabel.getY()+dist_out, max_char, text_height);
		slopeLabel.setText("Slope: "+y);
		slopeLabel.setBounds(20, corrCoeffLabel.getY()+dist_out, max_char, text_height);
		yIntLabel.setText("Y-Intercept: "+z);
		yIntLabel.setBounds(20, slopeLabel.getY()+dist_out, max_char, text_height);
	}
	
}

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
		SC1Page mp = new SC1Page();
		mp.setup();
	}
}
