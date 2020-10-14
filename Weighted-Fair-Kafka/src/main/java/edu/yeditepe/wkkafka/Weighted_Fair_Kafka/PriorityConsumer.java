package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class PriorityConsumer<K, V> {
	int priority;
	//Slider slider;
	KafkaConsumer<K, V> consumer;
	public PriorityConsumer(int priority,KafkaConsumer<K, V> consumer/*,Slider slider*/) {
		this.priority=priority;
		this.consumer=consumer;
		//this.slider=slider;
	}
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public KafkaConsumer<K, V> getConsumer() {
		return consumer;
	}
	public void setConsumer(KafkaConsumer<K, V> consumer) {
		this.consumer = consumer;
	}
	

}
