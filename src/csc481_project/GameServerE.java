package csc481_project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import java.util.concurrent.ConcurrentHashMap;

import object.GameObject;
import object.Rectangles;
import object.Squares;
import processing.core.PApplet;
import scriptManager.ScriptManager;

/**
 * @author jianllin This is the gameServer class
 * Event handler used some codes of from textbook 945
 */
public class GameServerE extends PApplet {
	//list of dead object
	static ArrayList<GameObject> deadObject = new ArrayList<GameObject>();
	// status of replay
	static boolean replay = false;
	// the priority of event h is high priority
	private static final long H_PRIORITY = 1;
	// the priority of event m is mid priority
	private static final long M_PRIORITY = 5;
	// this is the priorityQueue to store the event
	static volatile PriorityQueue<Event> events = new PriorityQueue<Event>(5, new EventComparator());
	// monitor for synchronization
	final static Object monitor = new Object();
	// the virtual timeline
	static TimeLine timeLine = new TimeLine();
	public static int refreshLimit = 1;
	/**
	 * Gravity acceleration is 9
	 */
	public static final int GRAVITY = 9;
	/**
	 * platformA
	 */
	static Rectangles platformA;
	/**
	 * platformB
	 */
	static Rectangles platformB;
	/**
	 * object rectangles
	 */
	static Rectangles rectangles;
	static Rectangles rectangles2;
	static Rectangles rectangles3;
	// Rectangles rectangles2 = new Rectangles(this);

	/**
	 * object square 2
	 */
	static Squares moveableA;
	/**
	 * object square 3
	 */
	static Squares movableB;

	/**
	 * object square 1
	 */
	static Squares playerSquare;

	/**
	 * message for clients
	 */
	private static volatile List<GameObject> gameObjects = Collections.synchronizedList(new ArrayList<GameObject>());

	private static ArrayList<String> clients = new ArrayList<String>();

	/**
	 * Game Object
	 * 
	 */

	/**
	 * GameObject for client
	 */
	static Map<Integer, Thread> servers = new ConcurrentHashMap<>();
	/**
	 * the id of connection
	 */
	static volatile int i = 0;

	/**
	 * dispatch events has the samller or same timestamp to handler
	 */
	public void dispatchEvents() {
		Event pEvent;
		pEvent = events.peek();
		if(timeLine.m_timeCycles == 0) {
			pEvent = new Event(Event.type.TYPE_SPAWN, 0 + timeLine.m_timeCycles,
					null, null);
		}
		// for event has timestamp smaller or equal to the virtual time
		while (pEvent != null && pEvent.timestamp <= timeLine.m_timeCycles) {

			events.poll();
			// dispatch the event to event handler
			eventHandler(pEvent);
			pEvent = events.peek();

			// eventHandler(pEvent);

		}
	}

	/**
	 * handling event
	 * 
	 * @param event
	 */
	public void eventHandler(Event event) {

		switch (event.getType()) {
		case TYPE_COLLISION:
			
			event.Collision();
			break;
		case TYPE_DEATH:
			ScriptManager.loadScript("src/Events/death.js");
			ScriptManager.bindArgument("gameObject", event.goA);
			ScriptManager.executeScript();
			
			break;
		case TYPE_SPAWN:
			gameObjects.get(5).spawn();
			gameObjects.get(6).spawn();
			gameObjects.get(7).spawn();
			break;
		case TYPE_JUMP:
			//event.jump();
			
			ScriptManager.loadScript("src/Events/jump.js");
			ScriptManager.bindArgument("gameObject", event.goA);
			ScriptManager.executeScript();
	
			break;
		case TYPE_LEFT:
			ScriptManager.loadScript("src/Events/left.js");
			ScriptManager.bindArgument("gameObject", event.goA);
			ScriptManager.executeScript();
			break;
		case TYPE_RIGHT:
			ScriptManager.loadScript("src/Events/right.js");
			ScriptManager.bindArgument("gameObject", event.goA);
			ScriptManager.executeScript();
			break;
		case TYPE_REPLAY:

			replay = true;
			timeLine.setPaused(true);

			break;
		case TYPE_PAUSE:
			if (timeLine.isPaused())
				timeLine.setPaused(false);
			else
				timeLine.setPaused(true);
			break;
		case TYPE_RECORDING:
			event.recording();
			break;
		case TYPE_STOP_RECORDING:
			event.stop_recording();
			break;
		default:
			break;

		}
	}

	
	

	/**
	 * detect collision between two object
	 * 
	 * @param goA a square
	 * @param goB a rectangle
	 */
	public void collsion(GameObject goA, GameObject goB) {
		// System.out.println(goA.landed);
		if (refreshLimit == 1) {

			goA.inTheAir();
			refreshLimit++;
		} else if (refreshLimit == 2) {
			refreshLimit++;
		} else if (refreshLimit == 3) {
			refreshLimit = 1;
		}

		if (goB.convert().intersects(goA.convert())) {
			Event.type type = Event.type.TYPE_COLLISION;
			Event collision = new Event(type, timeLine.m_timeCycles + H_PRIORITY, goA, goB);

			events.add(collision);

		}

	}

	@SuppressWarnings("resource")
	public void draw() {

//create main thread
		Thread mainThread = new Thread(new welcomeThread());
		mainThread.start();
		// init the timeline

		timeLine.init();
		long beginTime = 0;
		long endTime = 0;
		long deltaTime = 0;
		int frameRate = 10;
		int phy = 20;
		double expectTime = timeLine.singleStep(frameRate);
		double physicalF = timeLine.singleStep(phy);
		
		
		while (true) {
			//increase the loop iteration
			timeLine.increamnetLoop();
			deltaTime = endTime - beginTime;
			timeLine.update(deltaTime);
			// raise all the events should be processed at current frame
			dispatchEvents();
			beginTime = System.currentTimeMillis();
			// limit the frame rate accroding the the tic size
			if (expectTime <= timeLine.m_timeCycles) {
				// update object in gameObject
				expectTime = timeLine.singleStep(frameRate);
				synchronized (monitor) {
					for (int i = 0; i < gameObjects.size(); ) {
						if(gameObjects.get(i).checkLife()) {
							gameObjects.get(i).move();
							i++;
						}
						else {
							gameObjects.remove(i);
						}
						

					}
				}
			}
			endTime = System.currentTimeMillis();

			deltaTime = endTime - beginTime;
			timeLine.update(deltaTime);
			beginTime = System.currentTimeMillis();
			// limit the physical effect accroding to the tic size
			if (physicalF <= timeLine.m_timeCycles) {
				physicalF = timeLine.singleStep(phy);
				synchronized (monitor) {
					collsion(gameObjects.get(5), gameObjects.get(7));
					collsion(gameObjects.get(5), gameObjects.get(6));
					collsion(gameObjects.get(6), gameObjects.get(7));

					for (int i = 5; i < 8; i++) {
						// collision between player object
						for (int k = 0; k < 5; k++)

							collsion(gameObjects.get(i), gameObjects.get(k));

					}
				}
			}

			endTime = System.currentTimeMillis();

		}
	}

	/**
	 * main to run the server
	 * 
	 * @param args system argument
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void main(String args[]) {
		// save message for clients
		clients.add(0, "Hello client1");
		clients.add(1, "hello client2");
		clients.add(2, "Hello client3");

		rectangles = new Rectangles(50, 200, 0, 0);
		rectangles.setCliable(false);
		rectangles2 = new Rectangles(50, 201, 0, 0);
		rectangles2.setCliable(false);
		rectangles3 = new Rectangles(200, 50, 0, 0);
		rectangles3.setCliable(true);

		platformA = new Rectangles(330, 60, 0, 1);
		platformA.setCliable(true);
		platformB = new Rectangles(330, 60, 1, 0);
		platformB.setCliable(true);

		platformA.setRangex(1920);
		platformB.setRangey(500);
		platformA.setlocation(400, 400);
		platformB.setlocation(1200, 680);
		rectangles2.setlocation(400, 1080 - 200);
		rectangles3.setlocation(400, 700);

		gameObjects.add(platformA);
		gameObjects.add(platformB);

		gameObjects.add(rectangles);

		GameObject player1 = new Squares(50, 50);
		GameObject player2 = new Squares(50, 50);
		GameObject player3 = new Squares(50, 50);
		player1.setlocation(200, 0);
		player2.setlocation(250, 0);
		player3.setlocation(0, 0);
		gameObjects.add(rectangles2);
		gameObjects.add(rectangles3);
		gameObjects.add(player1);
		gameObjects.add(player2);
		gameObjects.add(player3);

		// runing the draw loop
		PApplet.main("csc481_project.GameServerE");

	}
	

	/**
	 * create multiThreadServer
	 * 
	 * @param csocket
	 * @throws IOException
	 */

	static class updateThread implements Runnable {

		ObjectOutputStream oos;
		ObjectInputStream ois;

		public updateThread(ObjectOutputStream output, ObjectInputStream input) {
			this.oos = output;
			this.ois = input;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			int id = -1;

			while (true) {

				try {

					try {
						id = (int) ois.readObject();

					} catch (ClassNotFoundException e) {

						i--;
						System.out.println("not right class");
					}

					oos.reset();
					oos.writeObject(clients.get(id));

					try {
						ArrayList<int[]> rendering = new ArrayList<int[]>();

						for (GameObject go : gameObjects) {
							rendering.add(go.getRenderInformation());

						}

						oos.reset();
						oos.writeObject(rendering);

						String keyInput = null;
						keyInput = (String) ois.readObject();
						if (keyInput != null) {

							if (keyInput.equals("jump")) {
								events.add(new Event(Event.type.TYPE_JUMP, M_PRIORITY + timeLine.m_timeCycles,
										gameObjects.get(5 + id), null));

							} else if (keyInput.equals("pause")) {

								events.add(new Event(Event.type.TYPE_PAUSE, 0 + timeLine.m_timeCycles, null, null));

							} else if (keyInput.equals("keyLeft")) {
								events.add(new Event(Event.type.TYPE_LEFT, M_PRIORITY + timeLine.m_timeCycles,
										gameObjects.get(5 + id), null));
							} else if (keyInput.equals("keyRight")) {
								events.add(new Event(Event.type.TYPE_RIGHT, M_PRIORITY + timeLine.m_timeCycles,
										gameObjects.get(5 + id), null));
							} else if (keyInput.equals("exist")) {

							} else if (keyInput.equals("replay")) {
								events.add(new Event(Event.type.TYPE_REPLAY, H_PRIORITY + timeLine.m_timeCycles, null,
										null));
							}
							else if(keyInput.equals("recording")) {
								events.add(new Event(Event.type.TYPE_RECORDING, M_PRIORITY + timeLine.m_timeCycles, null,
										null));
							}
							else if(keyInput.equals("recordingStoped")) {
								events.add(new Event(Event.type.TYPE_STOP_RECORDING, M_PRIORITY + timeLine.m_timeCycles, null,
										null));
							}

						}

					} catch (IOException | ClassNotFoundException e) {
						i--;
						Thread.interrupted();

					}

				} catch (IOException e) {
					i--;
					Thread.interrupted();

				}
			}
		}

	}

	static class welcomeThread implements Runnable {
		ServerSocket server = null;
		ObjectOutputStream oos;
		ObjectInputStream ois;

		@SuppressWarnings("resource")
		@Override
		public void run() {

			try {
				server = new ServerSocket(9877);
			} catch (IOException e) {

				i--;
				System.out.println("rest");
			}

			while (true) {
				System.out.println("Game Server Listening");

				Socket csocket = null;
				try {
					csocket = server.accept();
				} catch (IOException e1) {

					i--;
					e1.printStackTrace();
				}
				System.out.println("Connected to " + i + " " + "clients");
				try {
					oos = new ObjectOutputStream(csocket.getOutputStream());
					ois = new ObjectInputStream(csocket.getInputStream());
				} catch (IOException e) {

				}

				servers.put(i, new Thread(new updateThread(oos, ois)));
				servers.get(i++).start();

			}
		}

	}

}
