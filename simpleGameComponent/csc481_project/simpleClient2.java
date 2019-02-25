package csc481_project;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


import object.GameObject;


import processing.core.PApplet;

/**
 * refer to the sample on
 * https://www.journaldev.com/741/java-socket-programming-server-client This
 * class implements java socket client
 * 
 *
 */
public class simpleClient2 extends PApplet {
	//different player has different id TODO
	static int id = 1;
	static GameObject playerSquare;
	static Socket socket = null;
	static ObjectInputStream ois = null;
	static ArrayList<float[]> renderingInformInform;
	static ArrayList<ArrayList<int[]>> log = new ArrayList<ArrayList<int[]>>();
	static boolean replay = false;
	static boolean record = false;
	static ObjectOutputStream oos = null;
	static String keypress = null;
	static int replayIndex = 0;
	private boolean normalSpeed = true;
	private boolean slowMotion = false;
	private boolean doubleSpeed = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		

		background(255);

		
		try {
			//read id, this is used for identify player
			oos.reset();
			oos.writeObject(id);
			
			//this message represent the server has recived the message
			 ois.readObject();
			 
			
			// float[] rendering = playerSquare.getRenderInformation();
			@SuppressWarnings("unchecked")
			ArrayList<int[]> list = ((ArrayList<int[]>) ois.readObject());
			if (record) {
				log.add(list);
			}
			background(255);
			//this is for replay
			if (replay) {
				ArrayList<int[]> replayObject;
				if (replayIndex < log.size()) {
					replayObject = log.get(replayIndex);
					replayIndex++;
					for (int i = 0; i < replayObject.size(); i++) {
						int[] temp = replayObject.get(i);
						if (i == 0) {
							fill(160, 20, 20);
						} else if (i == 1) {
							fill(20, 160, 20);
						} else if (i == 2) {
							fill(20, 20, 160);
						} else if (i == 3) {
							fill(20, 150, 150);
						} else if (i == 4) {
							fill(150, 20, 150);
						} else if (i == 5) {
							fill(150, 150, 150);
						} else if (i == 6) {
							fill(150, 0, 150);
						}

						else
							fill(150, 150, 20);
												int a = temp[0];
						int b = temp[1];
						int c = temp[2];
						int d = temp[3];
						rect(a, b, c, d);
						
					}

				} else {
					// replay end
					replay = false;
					System.out.println("replay end");
					replayIndex = 0;
					normalSpeed = true;
					slowMotion = false;
					doubleSpeed = false;
				}

			}

			else {
				//this is for normal game redering
				for (int i = 0; i < list.size(); i++) {
					int[] temp = list.get(i);
					if (i == 0) {
						fill(160, 20, 20);
					} else if (i == 1) {
						fill(20, 160, 20);
					} else if (i == 2) {
						fill(20, 20, 160);
					} else if (i == 3) {
						fill(20, 150, 150);
					} else if (i == 4) {
						fill(150, 20, 150);
					} else if (i == 5) {
						fill(150, 150, 150);
					} else if (i == 6) {
						fill(150, 0, 150);
					}

					else
						fill(150, 150, 20);
					int a = temp[0];
					int b = temp[1];
					int c = temp[2];
					int d = temp[3];
					rect(a, b, c, d);
				}
			}
			
			// update key
			oos.reset();
			oos.writeObject(keypress);
			keypress = null;
			// }
		} catch (ClassNotFoundException | IOException e) {
			
			e.printStackTrace();
		}

//this is to adjust the replay speed
		if (normalSpeed) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		} else if (slowMotion) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		} else if (doubleSpeed) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

	

	public void settings() {
		//set the screen size
		size(1920, 1080);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see processing.core.PApplet#keyPressed()
	 */
	public void keyPressed() {
		keypress = null;
		if (key == ENTER) {
			keypress = "pause";
		}
		if (key == ' ') {
			keypress = "jump";
		}
		if (keyCode == LEFT) {
			keypress = "keyLeft";
			
		} else if (keyCode == RIGHT) {
			keypress = "keyRight";
		} else if (keyCode == KeyEvent.VK_R) {
			
			record = true;
			log =  new ArrayList<ArrayList<int[]>>();
			keypress="recording";
			//System.out.println("recording");
		} else if (keyCode == KeyEvent.VK_1) {
			keypress = "replay";
			replay = true;
			slowMotion = true;
			normalSpeed = false;
			doubleSpeed = false;
			System.out.println("replaySpeed1/2");
		} else if (keyCode == KeyEvent.VK_2) {
			
			keypress = "replay";
			replay = true;
			normalSpeed = true;
			doubleSpeed = false;
			slowMotion = false;
			System.out.println("replaying");
		} else if (keyCode == KeyEvent.VK_3) {
			keypress = "replay";
			replay = true;
			doubleSpeed = true;
			slowMotion = false;
			normalSpeed = false;
			System.out.println("replaySpeedDouble");
		} else if (keyCode == KeyEvent.VK_S) {
			record = false;
			keypress="recordingStoped";
			//System.out.println("recording stoped");
			
		}
	}

	// the processing part to render the
	@SuppressWarnings({ "resource", "unchecked" })
	public static void main(String[] args) {
		InetAddress host = null;
		try {
			host = InetAddress.getLocalHost();
		} catch (UnknownHostException e9) {
			
			e9.printStackTrace();
		}


		try {
			// ("connecting..");
			socket = new Socket(host.getHostName(), 9877);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e8) {
			
			e8.printStackTrace();
		}
		// the processing part to render the TODO
		PApplet.main("csc481_project.simpleClient2");

	}
}