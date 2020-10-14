package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;


import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import org.apache.kafka.common.TopicPartition;

public class KafkaPriorityConsumer2<K, V> {
	private ArrayList<PriorityConsumer<K, V>> consumers;
	HashMap<Integer, Integer> pollRecords;
	public KafkaPriorityConsumer2(ArrayList<KafkaTopic> topicList,int pollSize) {
		consumers=new ArrayList<PriorityConsumer<K,V>>();
		Collections.sort(topicList);
		PollDistributor pollDistributor=PollDistributor.instance();
		ArrayList<Integer> priorities =new ArrayList<Integer>();
		for(KafkaTopic topic:topicList) {
			priorities.add(topic.getPriority());
		}
		pollRecords=pollDistributor.distribution(priorities, pollSize);
		for(int k=0;k<topicList.size();k++) {
			topicList.get(k).getProperties().put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,pollRecords.get(k));
			int slidersize=Integer.parseInt(topicList.get(k).getProperties().getProperty("SliderSize"));
			int minPollCount=Integer.parseInt(topicList.get(k).getProperties().getProperty("MinPollCount"));
			this.consumers.add(new PriorityConsumer<K, V>(topicList.get(k).getPriority(), 
					new KafkaConsumer<K, V>(topicList.get(k).getProperties())));
			if(topicList.get(k) instanceof KafkaTopicPriority) {
				consumers.get(k).getConsumer().subscribe(Arrays.asList(topicList.get(k).getTopicName()));
				
			}
			else if(topicList.get(k) instanceof KafkaPartitionPriority) {
				TopicPartition topicPartition = new TopicPartition(topicList.get(k).getTopicName(), 
						((KafkaPartitionPriority)topicList.get(k)).getPartition());
				consumers.get(k).getConsumer().assign(Arrays.asList(topicPartition));
				
			}
			
		}
		
	}
	public void commitSync() {
		for (PriorityConsumer<K, V> priorityConsumer : consumers) {
			priorityConsumer.getConsumer().commitSync();
		}
	}
	public void close() {
		for (PriorityConsumer<K, V> priorityConsumer : consumers) {
			priorityConsumer.getConsumer().close();
		}
	}
	public void wakeup() {
		for (PriorityConsumer<K, V> priorityConsumer : consumers) {
			priorityConsumer.getConsumer().wakeup();
		}
	}
	public ConsumerRecords<K, V> poll(long millis){
		Map<TopicPartition, List<ConsumerRecord<K,V>>> consumerRecordsList=new HashMap<TopicPartition, List<ConsumerRecord<K,V>>>();
		//List<Integer> pollAmount=new ArrayList<Integer>();
		
		long start=System.currentTimeMillis();
		do {
			//int isChange=0;
			for(int k=consumers.size()-1;k>=0;k--) {
				
				ConsumerRecords<K, V> consumerRecords=consumers.get(k).getConsumer().poll(Duration.ofMillis(0));
				//pollAmount.set(k, pollAmount.get(k)+consumerRecords.count());
				//int remainder=pollRecords.get(k)-consumerRecords.count();
				//System.out.println("------");
				for(TopicPartition topicPartition:consumerRecords.partitions()) {
					consumerRecordsList.put(topicPartition, consumerRecords.records(topicPartition));
				}
			}
		}while(consumerRecordsList.isEmpty()&&System.currentTimeMillis()<(start+millis));
		return new ConsumerRecords<K,V>(consumerRecordsList);
			
	}
	

}
