package com.games.dots.utilities;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskScheduler {
	private PriorityBlockingQueue<TaskElement> m_queue = new PriorityBlockingQueue<>(); 
	
	public TaskScheduler(){
		
	}	
	
	public String putTask(Date time, Runnable task){
		String id = (UUID.randomUUID()).toString(); 
		TaskElement e = new TaskElement(id, time, task);
		m_queue.put(e);
		return id;
	}
	
	public void removeTask(String id){
		//check only by id
		TaskElement e = new TaskElement(id, null, null);
		m_queue.remove(e);
	}
	
	private class TaskElement implements Comparable<TaskElement>{
		public String id;
		public Date time;		
		public Runnable task;
		
		public TaskElement(String id, Date time, Runnable task) {
			this.id = id;
			this.time = time;
			this.task = task;
		}
		@Override
		public int compareTo(TaskElement o) {			
			return this.time.compareTo(o.time);
		}
		
		@Override
		public boolean equals(Object obj){
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TaskElement other = (TaskElement) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id)){
				return false;
			}			
			
			return true;
		}
	}
}
