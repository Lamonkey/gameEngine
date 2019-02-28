package csc481_project;

import java.io.Serializable;
import java.util.Comparator;

import object.GameObject;

public class Event implements Serializable {
	/**
	 * to make event transmitable bewteen client and server
	 */
	private static final long serialVersionUID = 1L;
	// type of the event
	type t;
	// when should the event be handled
	long timestamp;
	// gameobject a and b only used when there are collision
	GameObject goA;
	GameObject goB;

	/**
	 * this enum is type of the event type, decide how the event will be handled
	 * 
	 * @author jianl
	 *
	 */
	public static enum type {
		TYPE_COLLISION, TYPE_DEATH, 
		TYPE_SPAWN, TYPE_JUMP, 
		TYPE_PAUSE, TYPE_LEFT, TYPE_RIGHT, TYPE_REPLAY,
		TYPE_RECORDING, TYPE_STOP_RECORDING,

	}

	/**
	 * event constructor
	 * 
	 * @param t
	 * @param timestamp
	 * @param goA
	 * @param goB
	 */
	public Event(type t, long timestamp, GameObject goA, GameObject goB) {
		this.t = t;
		this.timestamp = timestamp;
		this.goA = goA;
		this.goB = goB;
	}

	/**
	 * method to handled collison type of event
	 */
	public void Collision() {

		if (goB.isCliable()) {
			goA.land(goB);
		} else {
			if (goB.getShape()[1] == 201) {
				goA.spawn();
			} else if (goA.onTheGround() && goB.getShape()[1] == 200) {
				goA.revertXSpeed();
				goA.x += goA.getXspeed() * 10;

			} else if (goB.getShape()[1] == 200) {
				goA.revertYSpeed();
			} else {

				goA.revertXSpeed();
				goB.revertXSpeed();
				goA.x += (goA.getXspeed() / goA.getXspeed()) * goB.width;
				// goB.x += goA.width;
			}

		}

	}



	/**
	 * jump event handler
	 */
	public void jump() {
		goA.jump();
	}


	
	/**
	 * user input handler
	 */
	public void replay() {

	}

	/**
	 * recording handler
	 */
	public void recording() {
		System.out.println("recording");
	}
	/**
	 * stop recording handler
	 */
	public void stop_recording() {
		System.out.println("stop recording");
				
	}

	/**
	 * get event type
	 * 
	 * @return event type
	 */
	public type getType() {

		return this.t;
	}

}

/**
 * comparator for priority queues
 * 
 * @author jianl
 *
 */
class EventComparator implements Comparator<Event> {

	@Override
	public int compare(Event e1, Event e2) {
		return Long.compare(e1.timestamp, e2.timestamp);

	}

}
