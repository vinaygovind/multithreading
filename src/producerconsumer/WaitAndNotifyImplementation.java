/**
 * 
 */
package producerconsumer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author vgovind
 *
 */
public class WaitAndNotifyImplementation {
	
	private List<Integer> queue = new LinkedList<>();
	private int LIMIT = 10;
	private Object lock = new Object();
	
	public void produce() throws InterruptedException {
		Random random = new Random();
		Integer value;
		while(true) {
			synchronized (lock) {
				value = random.nextInt(100);
				while(queue.size() == LIMIT) {
					lock.wait();
				}
				queue.add(value);
				lock.notify();
				System.out.println("Produced the value : " + value + "; Queue Size is :" + queue.size());
			}
		}
	}
	
	public void consume() throws InterruptedException {
		Integer value;
		while(true) {
			Thread.sleep(1000);// to ensure that producer gets a chance to execute
			synchronized (lock) {
				while(queue.isEmpty()) {
					lock.wait();
				}
				value = queue.remove(queue.size()-1);
				lock.notify();
				System.out.println("Consumed the value : " + value + "; Queue Size is :" + queue.size());	
			}
		}
	}
	
	public static void main(String[] args) {
		WaitAndNotifyImplementation impl = new WaitAndNotifyImplementation();
		Thread t1 = new Thread(() ->  {
			try {
				impl.produce();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		Thread t2 = new Thread(()-> {
			try {
				impl.consume();
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
