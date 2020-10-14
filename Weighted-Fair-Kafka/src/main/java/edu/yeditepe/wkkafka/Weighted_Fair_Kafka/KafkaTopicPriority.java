package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;

import java.util.Properties;

public class KafkaTopicPriority extends KafkaTopic{
	public KafkaTopicPriority(Properties properties,String topicName,int priority) {
		this.topicName=topicName;
		this.priority=priority;
		this.properties=properties;
		
	}
}
