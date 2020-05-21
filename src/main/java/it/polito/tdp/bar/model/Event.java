package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.LocalTime;



public class Event implements Comparable<Event>{

	public enum EventType {
		ARRIVO_GRUPPO_CLIENTI, TAVOLO4_LIBERATO, TAVOLO6_LIBERATO, TAVOLO8_LIBERATO, TAVOLO10_LIBERATO
	}

	private LocalTime time;
	private EventType type;
	
	
	
	public Event(LocalTime time, EventType type) {
		super();
		this.time = time;
		this.type = type;
		
	}


	public LocalTime getTime() {
		return time;
	}


	public void setTime(LocalTime time) {
		this.time = time;
	}


	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + "]";
	}


	public EventType getType() {
		return type;
	}


	public void setType(EventType type) {
		this.type = type;
	}
	
	
	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.time);
	}

}