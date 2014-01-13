package krishagni.catissueplus.throttling;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter 
{
	 private long startTime;
	 private long timeInterval;
	 private int maxLimit;
	 private AtomicInteger currentCount;

	public Counter(long timeInterval, int maxLimit) {
		super();
		this.timeInterval = timeInterval;
		this.maxLimit = maxLimit;
		startTime = System.currentTimeMillis();
		currentCount = new AtomicInteger(0);
	}


	public boolean increment()
	    {
	        long currentTime = System.currentTimeMillis();
	        if ((startTime + timeInterval) < currentTime) {
	            synchronized (this) {
	                if ((startTime + timeInterval) < currentTime) {
	                    startTime = currentTime;
	                    currentCount = new AtomicInteger(0);
	                }
	            }
	        }

	        return !(currentCount.incrementAndGet() > maxLimit);
	    }
}
