package edu.yeditepe.wkkafka.Weighted_Fair_Kafka;

import java.util.ArrayList;
import java.util.HashMap;

public class PollDistributor {
	private static final PollDistributor INSTANCE = new PollDistributor();

	private PollDistributor() {
	}

	public static PollDistributor instance() {
		return INSTANCE;
	}

	public HashMap<Integer, Integer> distribution(ArrayList<Integer> priorities, int pollRecords) {
		int sum = 0;
		int maxPriority = 0;
		HashMap<Integer, Integer> distributedPollRecords = new HashMap<Integer, Integer>();
		for (Integer a : priorities) {
			sum += (1 << a);
			if (a > maxPriority) {
				maxPriority = a;
			}
		}
		System.out.println(sum + "andd" + maxPriority);
		if (pollRecords < sum) {
			throw new IllegalArgumentException("Invalid poll records it must be at least " + sum);
		}
		int left = pollRecords;
		int a = 0;
		int maxValues = 0;
		int amount=0;
		for (int i : priorities) {
			System.out.println((pollRecords * (1 << i)) / sum);
			amount= (pollRecords * (1 << i)) / sum;
			distributedPollRecords.put(a, amount);
			left -= amount;
			a++;
			if (i == maxPriority) {
				maxValues++;
			}
		}
		int index=distributedPollRecords.size();
		int circular=1;
		System.out.println("indexx "+index);
		if (left > 0) {
			while (left != 0) {
				
				distributedPollRecords.put(index-circular, distributedPollRecords.get(index-circular) + 1);
				if(index-circular==index-maxValues) {
					circular=1;
				}
				else {
					circular++;
				}
				
				left--;
			}
		}
		return distributedPollRecords;
	}

}
