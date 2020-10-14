package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class Consumer implements Runnable {

	
	// private static String face_cascade_name = "haarcascade_frontalface_alt.xml";
	public static BufferedImage image1;
	public static byte[] image2;
	Date now=new Date();
	// public static BlockingQueue<byte[]> q = new
	// ArrayBlockingQueue<byte[]>(0XFFFF);
	String topic1;
	String topic2 = "denden12";
	String topic3 = "denden13";
	KafkaPriorityConsumer2<String, byte[]> kafkaPriorityConsumer;
	Object o = new Object();
	int a = 0;
	int b = 0, d = 0;
	int c = 0;
	int e=0;
	long exconsumed2=0;
	long exconsumed1=0;
	boolean first = true;

	private static Logger log;
	private static final long START = System.currentTimeMillis();
	private ExecutorService service;

	// static CascadeClassifier faceDetector = new
	// CascadeClassifier(Consumer.class.getResource("haarcascade_frontalface_alt.xml").getPath());
	SleepTime image1SleepTime;
	SleepTime image2SleepTime;
	long timeTakenMs=0;
	// private static final long START = System.currentTimeMillis();
	// static CascadeClassifier faceDetector = new
	// CascadeClassifier(Consumer.class.getResource("haarcascade_frontalface_alt.xml").getPath());
	CascadeClassifier faceDetector = new CascadeClassifier(
			Consumer.class.getResource("lbpcascade_frontalface_improved.xml").getPath());
	CountDownLatch latch;

	// CascadeClassifier cascadeMouthClassifier = new
	// CascadeClassifier("OpenCV/haarcascades/haarcascade_mcs_mouth.xml");
	// haarcascade_m
	public Consumer() {

		// TODO Auto-generated constructor stub

		topic1 = "denden13";
		log = LoggerFactory.getLogger(Consumer.class);
		service = Executors.newFixedThreadPool(1);

		String group_id = "stream-app9";
		String bootstrapServers = "192.168.1.34:9092";// "192.168.1.45:9092";////
														// "192.168.4.1:9092";////;//;//"192.168.1.41:9092";//"localhost:9092";
		// Integer max_poll_records = 1;

		// properties
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, group_id);
		//properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "100000");

		// properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, max_poll_records);
		// properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// Demo demo=new Demo();
		/*
		 * Runnable demo = new Demo(); Thread thread = new Thread(demo); thread.start();
		 */
		// demo.start();

		// properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"100000");
		// properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,"2097152");
		 //properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,"100000");

		// TopicPartition topicPartition = new TopicPartition(topic, 0);
		// kafkaConsumer.assign(Arrays.asList(topicPartition));
		// create consumer
		//kafkaConsumer = new KafkaConsumer<String, byte[]>(properties);
		//kafkaConsumer.subscribe(Arrays.asList(topic1));
		// create consumer
		ArrayList<KafkaTopic> topicList = new ArrayList<KafkaTopic>();
		properties.put("SliderSize", "6");
		properties.put("MinPollCount", "4");
		topicList.add(new KafkaPartitionPriority(properties, topic1, 0, 1));
		topicList.add(new KafkaPartitionPriority(properties, topic1, 1, 0));
		topicList.add(new KafkaPartitionPriority(properties, topic1, 2, 0));
		topicList.add(new KafkaPartitionPriority(properties, topic1, 3, 0));
		//topicList.add(new KafkaPartitionPriority(properties, topic1, 2,0));

		int pollSize = 500;
		kafkaPriorityConsumer = new KafkaPriorityConsumer2<String, byte[]>(topicList, pollSize);
		image1SleepTime = new SleepTime();
		image2SleepTime = new SleepTime();
		this.latch = new CountDownLatch(1);

		// t1.schedule(new Demo(), 0,33);

	}

	public void run() {
		int k=0;
		long end = 1000 * 180;
		/*String name1 = "thwf1cam.txt";//"wfsc2camrp1.txt";
		File file1 = new File(name1);
		try {
			file1.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PrintWriter pw1 = null;
		FileWriter fileW1 = null;
		try {
			fileW1 = new FileWriter(name1);
			pw1 = new PrintWriter(fileW1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String name2 = "thwf1camspec.txt";//"wfsc2camspectrp1.txt";
		File file2 = new File(name2);
		try {
			file2.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PrintWriter pw2 = null;
		FileWriter fileW2 = null;
		try {
			fileW2 = new FileWriter(name2);
			pw2 = new PrintWriter(fileW2);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		long i = 1000;
		// TODO Auto-generated method stub
		// while (true) {
		long start = 0;
		// while (System.currentTimeMillis() < start + end) {
		try {
			while (true) {

				ConsumerRecords<String, byte[]> consumerRecords = /* kafkaConsumer.poll(100); */ kafkaPriorityConsumer
						.poll(0);
				if (first) {
					if (!consumerRecords.isEmpty()) {
						start = System.currentTimeMillis();
						first = false;
					}
				}
				for (ConsumerRecord<String, byte[]> cR : consumerRecords) {

					/*} else if (cR.topic().equals(topic1) && cR.partition() == 1) {*/
						
						

						//image2 = cR.value();
						// image1 = cR.value();
						
						
						
						/*long consumedim2 = System.currentTimeMillis();
						if(consumedim2-exconsumed2<33) {
							try {
								Thread.sleep(33-(consumedim2-exconsumed2));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//System.out.println(consumedim2-exconsumed2);
						
						image2 = cR.value();
						String[] s = cR.key().split(";");
						
						pw2.printf("camera2 id %d produced %d ms consumed %d ms size %d latency %d\n",
								Integer.parseInt(s[1]), Long.parseLong(s[0]), consumedim2, image2.length,
								consumedim2 - Long.parseLong(s[0]));
						exconsumed2=consumedim2;
						
						b++;
					}*/
					
					 if(cR.topic().equals(topic1)&&cR.partition()==0) { //synchronized (o) {
					 image1 = detectAndDisplay(cR.value(),image1SleepTime);
					  if(image1SleepTime.time<30) { try { Thread.sleep(30-image1SleepTime.time); }
					  catch (InterruptedException e) { // TODO Auto-generated catch block
					  e.printStackTrace(); } }
					  App.PushImage(image1);
					  
					  a++;
					 
					 } /*else if(cR.topic().equals(topic1)&&cR.partition()==1){
					 * 
					 * image2 = detectAndDisplay(cR.value(),image2SleepTime);
					 * if(image2SleepTime.time<30) { try { Thread.sleep(30-image2SleepTime.time); }
					 * catch (InterruptedException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); } }
					 * 
					 * b++; }
					 */

					else if (cR.topic().equals(topic1) && cR.partition() == 1) {
						
						
						String[] s = cR.key().split(";");
						ByteBuffer cd = ByteBuffer.wrap(cR.value());
						App.time1.setText(String.valueOf(cd.getLong()));
						App.soilMoisture1.setText(String.valueOf(cd.getFloat()));
						App.hum1.setText(String.valueOf(cd.getFloat()));
						App.airHumidity1.setText(String.valueOf(cd.getFloat()));
						App.sigma1.setText(String.valueOf(b));
						
						b++;

					} else if (cR.topic().equals(topic1) && cR.partition() == 2) {
						
						
						ByteBuffer cd = ByteBuffer.wrap(cR.value());
						String[] s = cR.key().split(";");
						App.time2.setText(String.valueOf(cd.getLong()));
						App.soilMoisture2.setText(String.valueOf(cd.getFloat()));
						App.hum2.setText(String.valueOf(cd.getFloat()));
						App.airHumidity2.setText(String.valueOf(cd.getFloat()));
						App.sigma2.setText(String.valueOf(c));
						
						c++;

					}
					 else if (cR.topic().equals(topic1) && cR.partition() == 3) {
							
							
							ByteBuffer cd = ByteBuffer.wrap(cR.value());
							String[] s = cR.key().split(";");
							App.time3.setText(String.valueOf(cd.getLong()));
							App.soilMoisture3.setText(String.valueOf(cd.getFloat()));
							App.hum3.setText(String.valueOf(cd.getFloat()));
							App.airHumidity3.setText(String.valueOf(cd.getFloat()));
							App.sigma3.setText(String.valueOf(d));
							
							d++;

						}
					
					
					/*timeTakenMs = System.currentTimeMillis() - start;
					if (timeTakenMs >= i) {
						log.info("After {} ms {} sec completed from a {} from b {} from c {} from d {}", timeTakenMs,
							(timeTakenMs / 1000), a, b, c, d);
						pw1.printf(
								"After %d ms %d sec completedfrom camera1 %d from camera2 %d from scaler1 %d from scaler2 %d\n",
								timeTakenMs, (timeTakenMs / 1000), a, b, c, d);
						i = i + 1000;
					}*/

					/*log.info("After {} ms {} sec completed from a {} from b {} from c {} from d {}", timeTakenMs,
							(timeTakenMs / 1000), a, b, c, d);
					if (timeTakenMs >= i) {
						pw1.printf(
								"After %d ms %d sec completedfrom camera1 %d from camera2 %d from scaler1 %d from scaler2 %d\n",
								timeTakenMs, (timeTakenMs / 1000), a, b, c, d);
						i = i + 1000;
					}*/

				}
				timeTakenMs = System.currentTimeMillis() - start;
				if (timeTakenMs >= i&&!first) {
					log.info("After {} ms {} sec completed from a {} from b {} from c {} from d {}", timeTakenMs,
						(timeTakenMs / 1000), a, b, c, d);
					/*pw1.printf(
							"After %d ms %d sec completedfrom camera1 %d from camera2 %d from scaler1 %d from scaler2 %d\n",
							timeTakenMs, (timeTakenMs / 1000), a, b, c, d);*/
					i = i + 1000;
				}
			}

		} catch (WakeupException e) {
			System.out.println("Wakeup Exception catched");
		} finally {
			kafkaPriorityConsumer.close();
			// tell our main code we're done with the consumer
			/*try {
				pw1.printf(
						"After %d ms %d sec completedfrom camera1 %d from camera2 %d from scaler1 %d from scaler2 %d\n",
						timeTakenMs, (timeTakenMs / 1000), a, b, c, d);
				pw1.close();
				fileW1.close();
				pw2.close();
				fileW2.close();

			} catch (IOException e) { // TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			latch.countDown();
		}

	}

	public static BufferedImage getImage1() {
		return image1;
	}

	public static byte[] getImage2() {
		return image2;
	}

	public void shutDown() {
		// TODO Auto-generated method stub
		kafkaPriorityConsumer.wakeup();

	}

	

	public byte[] processImage(Mat matrix) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (matrix.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
		byte[] buffer = new byte[bufferSize];
		matrix.get(0, 0, buffer); // get all the pixels
		BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BufferedImage detectAndDisplay(byte[] a, SleepTime time) {
		// System.out.println("enter");
		long start = System.currentTimeMillis();
		// long start=System.currentTimeMillis();
		// System.out.println("Coming array size "+a.length);

		Mat image = Imgcodecs.imdecode(new MatOfByte(a), Imgcodecs.IMREAD_UNCHANGED);

		// long end=System.currentTimeMillis();
		// System.out.println("diff "+(end-start));

		MatOfRect faceDetections = new MatOfRect();

		faceDetector.detectMultiScale(image, faceDetections);
		// long end2=System.currentTimeMillis();

		// System.out.println("diff2: "+(end2-end));

		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
					new Scalar(0, 255, 0));
		}
		// System.out.println("diff3: "+(System.currentTimeMillis()-end2));
		time.time = System.currentTimeMillis() - start;
		//return image;
		return ConvertMat2Image(image);
	}

	private static BufferedImage ConvertMat2Image(Mat kameraVerisi) {

		MatOfByte byteMatVerisi = new MatOfByte();

		Imgcodecs.imencode(".jpg", kameraVerisi, byteMatVerisi);

		byte[] byteArray = byteMatVerisi.toArray();
		// System.out.println("byte array size is"+byteArray.length);
		//return byteArray;
		BufferedImage goruntu = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			goruntu = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return goruntu;
	}

}

class SleepTime {
	long time;

}
