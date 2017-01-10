package com.zipeiyi.game.gateServer.domain;

import com.zipeiyi.game.common.message.MessageReq;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {
	private Queue<MessageReq> requestQueue;
	private boolean running = false;

	public MessageQueue(ConcurrentLinkedQueue<MessageReq> concurrentLinkedQueue) {
		this.requestQueue = concurrentLinkedQueue;
	}

	public Queue<MessageReq> getRequestQueue() {
		return this.requestQueue;
	}

	public void setRequestQueue(Queue<MessageReq> requestQueue) {
		this.requestQueue = requestQueue;
	}

	public void clear() {
		this.requestQueue.clear();
		this.requestQueue = null;
	}

	public int size() {
		return this.requestQueue != null ? this.requestQueue.size() : 0;
	}

	public boolean add(MessageReq request) {
		return this.requestQueue.add(request);
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isRunning() {
		return this.running;
	}
}