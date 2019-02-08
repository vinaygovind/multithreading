/**
 * 
 */
package producerconsumer;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author vgovind
 *
 */
public class ReEntrantLockImplementation {
	
	private List<Integer> queue = new LinkedList<>();
	private int LIMIT = 10;
	private Lock lock = new ReentrantLock();
	private Condition isEmpty = lock.newCondition();
	private Condition isFull = lock.newCondition();
	
	public void produce() throws InterruptedException {
		Random random = new Random();
		Integer value;
		while(true) {
			lock.lock();
			value = random.nextInt(100);
			while(queue.size() == LIMIT) {
				isFull.await();
			}
			queue.add(value);
			isEmpty.signal();
			System.out.println("Produced the value : " + value + "; Queue Size is :" + queue.size());
			lock.unlock();
		}	
	}
	
	public void consume() throws InterruptedException {
		Integer value;
		Thread.sleep(1000);
		while(true) {
			lock.lock();
			while(queue.isEmpty()) {
				isEmpty.await();
			}
			value = queue.remove(queue.size() - 1);
			isFull.signal();
			System.out.println("Consumed the value : " + value + "; Queue Size is :" + queue.size());	
			lock.unlock();	
		}	
	}
	
	public static void main(String[] args) {
		ReEntrantLockImplementation impl = new ReEntrantLockImplementation();
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
