package producerconsumer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueImplementation {
	
	// Thread safe data structure
	private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
	
	private static void produce() throws InterruptedException {
		Random random = new Random();
		Integer value;
		while(true) {
			value = random.nextInt(100);
			queue.put(value);
			System.out.println("Produced the value : " + value + "; Queue Size is :" + queue.size());
		}
	}
	
	private static void consume() throws InterruptedException {
		Random random = new Random();
		Integer value;
		while(true) {
			Thread.sleep(1000);// to ensure that producer gets a chance to execute
			if (random.nextInt(10) == 1) {
				value = queue.take();
				System.out.println("Consumed the value : " + value + "; Queue Size is :" + queue.size());
			}
		}
	}
	
	public static void main(String[] args) {
		Thread t1 = new Thread(() ->  {
			try {
				produce();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		Thread t2 = new Thread(()-> {
			try {
				consume();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
