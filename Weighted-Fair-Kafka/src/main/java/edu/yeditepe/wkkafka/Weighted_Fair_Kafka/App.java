package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.kafka.common.metrics.stats.Sum;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
	static JFrame frame;
	static JLabel lbl;
	static ImageIcon icon;
	static JTextField time1,time2,time3;
	static JTextField soilMoisture1,soilMoisture2,soilMoisture3;
	static JTextField hum1,hum2,hum3;
	static JTextField airHumidity1,airHumidity2,airHumidity3;
	static JTextField sigma1,sigma2,sigma3;
	static ImageIcon timeImage;
	static ImageIcon humidityImage;
	static ImageIcon soilMoistureImage;
	static ImageIcon temperatureImage;
	static ImageIcon totalImage;
	static JLabel timeLabel;
	
    public static void main( String[] args )
    {
    	makeWindow();
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		final Logger logger = LoggerFactory.getLogger(App.class.getName());
		final Runnable consumerRunnable = new Consumer();
        Thread thread = new Thread(consumerRunnable);
       thread.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                
                ((Consumer) consumerRunnable).shutDown();
                try {
                	((Consumer) consumerRunnable).latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Application has exited");                

            }
        });
        
    }
    public static void makeWindow() {
		frame = new JFrame();
		//frame.setLayout(new FlowLayout());
		frame.setLayout(null);
		
		frame.setSize(1100, 1000);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.black);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panelImage=new JPanel();
		frame.add(panelImage);
		lbl = new JLabel();
		//frame.add(lbl);
		panelImage.add(lbl);
		panelImage.setBounds(10, 150, 600, 500);
		panelImage.setBackground(Color.black);
		timeImage=new ImageIcon(App.class.getResource("/clock.png"));
		soilMoistureImage=new ImageIcon(App.class.getResource("/moisture.png"));
		humidityImage=new ImageIcon(App.class.getResource("/water.png"));
		temperatureImage=new ImageIcon(App.class.getResource("/temperature.png"));
		totalImage=new ImageIcon(App.class.getResource("/sigma.png"));
		
		
		
		
		JLabel l1,l2,l3,l4,l5;  
	    
		l1=new JLabel(timeImage);
	    
		l2=new JLabel(soilMoistureImage);
	    //l3=new JLabel("Humidity");
		l3=new JLabel(temperatureImage);
	   
		l4=new JLabel(humidityImage);
		l5=new JLabel(totalImage);
		l1.setBounds(640, 30, 100, 70);
		
	    l2.setBounds(780,30,100,70);
		
	    l3.setBounds(640, 130, 100, 70);
	    l4.setBounds(750, 130, 150, 70);
	    l5.setBounds(850,80,150,70);
	    time1=new JTextField("Timestamp",JTextField.CENTER);
		time1.setBounds(650,100, 100,30);   
		time1.setEditable(false);  
		time1.setBackground(Color.black);
		time1.setForeground(Color.white);
		frame.add(time1);
		soilMoisture1=new JTextField("Soil Mosture",JTextField.CENTER);
		soilMoisture1.setBounds(780,100, 100,30);   
		soilMoisture1.setEditable(false);  
		soilMoisture1.setBackground(Color.black);
		soilMoisture1.setForeground(Color.white);
		frame.add(soilMoisture1);
		hum1=new JTextField("Temperature",JTextField.CENTER);
		hum1.setBounds(650,200, 100,30);   
		hum1.setEditable(false);  
		hum1.setBackground(Color.black);
		hum1.setForeground(Color.white);
		frame.add(hum1);
		airHumidity1=new JTextField("Humidity",JTextField.CENTER);
		airHumidity1.setBounds(785,200, 100,30);   
		airHumidity1.setEditable(false);  
		airHumidity1.setBackground(Color.black);
		airHumidity1.setForeground(Color.white);
		frame.add(airHumidity1);
		sigma1=new JTextField("Total",JTextField.CENTER);
		sigma1.setBounds(950,100, 100,30);   
		sigma1.setEditable(false);  
		sigma1.setBackground(Color.black);
		sigma1.setForeground(Color.white);
		frame.add(sigma1);
		frame.add(l1);
		frame.add(l2);
		frame.add(l3);
		frame.add(l4);
		frame.add(l5);
		//End1
		JLabel l21,l22,l23,l24,l25;  
		l21=new JLabel(timeImage);  
	    l22=new JLabel(soilMoistureImage); 
	    l23=new JLabel(temperatureImage);
	    l24=new JLabel(humidityImage);
	    l25=new JLabel(totalImage);
	    /*l21=new JLabel("TimeStamp");  
	    l22=new JLabel("Soil Mosture"); 
	    l23=new JLabel("Humidity");
	    l24=new JLabel("Humidty Rate");*/
	    l21.setFont(l1.getFont().deriveFont(16.0f));
	    l22.setFont(l2.getFont().deriveFont(16.0f));
	    l23.setFont(l2.getFont().deriveFont(16.0f));
	    l24.setFont(l2.getFont().deriveFont(16.0f));
	    l21.setBounds(640, 300, 100, 70);
	    l22.setBounds(780,300,100,70);
	    l23.setBounds(640, 400, 100, 70);
	    l24.setBounds(750, 400, 150, 70);
	    l25.setBounds(850,350,150,70);
	    time2=new JTextField("Timestamp");
		time2.setBounds(650,370, 100,30);   
		time2.setEditable(false);  
		time2.setBackground(Color.black);
		time2.setForeground(Color.white);
		frame.add(time2);
		soilMoisture2=new JTextField("Soil Mosture");
		soilMoisture2.setBounds(780,370, 100,30);   
		soilMoisture2.setEditable(false);  
		soilMoisture2.setBackground(Color.black);
		soilMoisture2.setForeground(Color.white);
		frame.add(soilMoisture2);
		hum2=new JTextField("Temperature");
		hum2.setBounds(650,470, 100,30);   
		hum2.setEditable(false);  
		hum2.setBackground(Color.black);
		hum2.setForeground(Color.white);
		frame.add(hum2);
		airHumidity2=new JTextField("Humidity");
		airHumidity2.setBounds(785,470, 100,30);   
		airHumidity2.setEditable(false); 
		airHumidity2.setBackground(Color.black);
		airHumidity2.setForeground(Color.white);
		frame.add(airHumidity2);
		sigma2=new JTextField("Total",JTextField.CENTER);
		sigma2.setBounds(950,370, 100,30);   
		sigma2.setEditable(false);  
		sigma2.setBackground(Color.black);
		sigma2.setForeground(Color.white);
		frame.add(sigma2);
		frame.add(l21);
		frame.add(l22);
		frame.add(l23);
		frame.add(l24);
		frame.add(l25);
		//END2
		JLabel l31,l32,l33,l34,l35;  
		l31=new JLabel(timeImage);  
	    l32=new JLabel(soilMoistureImage); 
	    l33=new JLabel(temperatureImage);
	    l34=new JLabel(humidityImage);
	    l35=new JLabel(totalImage);
	    l31.setBounds(640, 570, 100, 70);
	    l32.setBounds(780,570,100,70);
	    l33.setBounds(640, 670, 100, 70);
	    l34.setBounds(750, 670, 150, 70);
	    l35.setBounds(850,620,150,70);
	    time3=new JTextField("Timestamp");
		time3.setBounds(650,640, 100,30);   
		time3.setEditable(false); 
		time3.setBackground(Color.black);
		time3.setForeground(Color.white);
		frame.add(time3);
		soilMoisture3=new JTextField("Soil Mosture");
		soilMoisture3.setBounds(780,640, 100,30);   
		soilMoisture3.setEditable(false);  
		soilMoisture3.setBackground(Color.black);
		soilMoisture3.setForeground(Color.white);
		frame.add(soilMoisture3);
		hum3=new JTextField("Temperature");
		hum3.setBounds(650,740, 100,30);   
		hum3.setEditable(false); 
		hum3.setBackground(Color.black);
		hum3.setForeground(Color.white);
		frame.add(hum3);
		airHumidity3=new JTextField("Humidity");
		airHumidity3.setBounds(785,740, 100,30);   
		airHumidity3.setEditable(false);  
		airHumidity3.setBackground(Color.black);
		airHumidity3.setForeground(Color.white);
		frame.add(airHumidity3);
		sigma3=new JTextField("Total",JTextField.CENTER);
		sigma3.setBounds(950,640, 100,30);   
		sigma3.setEditable(false);  
		sigma3.setBackground(Color.black);
		sigma3.setForeground(Color.white);
		frame.add(sigma3);
		frame.add(l31);
		frame.add(l32);
		frame.add(l33);
		frame.add(l34);
		frame.add(l35);
		frame.setVisible(true);
	}
    public static void PushImage(Image img2) {
		//Pencere olu?turulmamy? ise hazyrlanyr
		if (frame == null)
			makeWindow();
		icon = new ImageIcon(img2);
		lbl.setIcon(icon);
		frame.revalidate();
	}
    
    
}
