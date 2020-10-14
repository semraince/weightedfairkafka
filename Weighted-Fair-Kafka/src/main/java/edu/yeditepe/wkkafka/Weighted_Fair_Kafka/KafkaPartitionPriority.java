package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;

import java.util.Properties;

public class KafkaPartitionPriority extends KafkaTopic{
	private int partition;
	public KafkaPartitionPriority(Properties properties,String topicName,int partition,int priority) {
		this.topicName=topicName;
		this.priority=priority;
		this.partition=partition;
		this.properties=properties;
	}
	public int getPartition() {
		return partition;
	}
	public void setPartition(int partition) {
		this.partition=partition;
	}

}
