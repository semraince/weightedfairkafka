package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;

import java.util.Properties;

public abstract class KafkaTopic implements Comparable<KafkaTopic>{
	int priority;
	String topicName;
	Properties properties;
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority=priority;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName=topicName;
	}
	public void setProperties(Properties properties) {
		this.properties=properties;
	}
	public Properties getProperties() {
		return properties;
	}
	public int compareTo(KafkaTopic o) {
		// TODO Auto-generated method stub
		int comparePriority = ((KafkaTopic) o).getPriority();
		return this.priority-comparePriority;
	}
	

}
