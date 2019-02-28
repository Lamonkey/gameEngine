package object;

import java.awt.Rectangle;





public abstract class GameObject {
	boolean isAlive = true;
	boolean cliable;
	int speed;// speed of rectangle
	int gravity = 10;
	int collusion;
	public float x;// horizontal location of rectangle
	float y;// vertical location of rectangle
	int xspeed;// xspeed of rectangle
    int yspeed;// yspeed of rectangle
	public float width; // width of rectangle
	public float height; // length of rectangle
	// PApplet parent; // the parent PApplet that will render the rectangles
	int color; // the color of rectangles
	public boolean landed = false;// detect if object is landed
	boolean jump = false;
	private boolean onTheGround;
	private int rangx = 1920;
	private int rangy = 1080;
	private float or_x = 1400; 
	private float or_y = 500;
	public float getX() {
		return this.x;
	}
	
	public void addToX(int x) {
		this.x += x;
	}
	public void AddToY(int y) {
		this.y += y;
	}
	public boolean checkLife() {
		return isAlive;
	}
	public void kill() {
		isAlive = false;
	}
	public void spawn() {
		isAlive = true;
		this.x = or_x;
		this.y = or_y;
	}
	public boolean isInAir() {
		return landed;
	}
	public int getRangex() {
		return rangx;
	}

	/**
	 * modify the cliable property
	 * 
	 * @param togle
	 */
	public void setCliable(boolean togle) {
		this.cliable = togle;
	}

	public void setRangex(int rangex) {
		this.rangx = rangex;
	}

	public int getRangey() {
		return rangy;
	}

	public void setRangey(int rangey) {
		this.rangy = rangey;
	}

	/**
	 * get xspeed
	 * 
	 * @return xspeed
	 */
	public double getXspeed() {
		return xspeed;
	}

	/**
	 * get yspeed
	 * 
	 * @return yspeed
	 */
	public double getYspeed() {
		return yspeed;
	}

	/**
	 * get the location of gameObject
	 * 
	 * @return location[x,y]
	 */
	public int[] getLocation() {
		int[] location = new int[2];
		location[0] = (int) x;
		location[1] = (int) y;
		return location;
	}

	/**
	 * get the shape of the GameObject
	 * 
	 * @return the dimension of gameobject w,l
	 */
	public int[] getShape() {
		int[] list = new int[2];
		list[0] = (int) width;
		list[1] = (int) height;
		return list;
	}

//	public boolean landed(GameObject goB) {
//		// int[] locationOfA = goA.getLocation();
//		//int[] locationOfB = goB.getLocation();
//		//int goA_bottom = this.getLocation()[1] + this.height/2;
//		int goA_top = this.getLocation()[1];
//		int goB_bottom = goB.getLocation()[1] + goB.getShape()[1];
//     	int goB_top = goB.getLocation()[1]-goB.getShape()[1]/2;
//     	int pointA = this.getLocation()[1];
//		int pointB = goB.getLocation()[1];
//	//	if (goB_top > goA_top && goB_bottom > goA_top) {
//		if(pointA < pointB ) {
//		//if(goB.getLocation()[1] < this.y) {
//		
//			landed = true;
//			
//
//		} else {
//			landed = false;
//
//		}
//		return landed;
//	}

	public void inTheAir() {
		landed = false;
	}

	/**
	 * revert the Yspeed
	 */
	public void revertYSpeed() {
		// this.y += height/2;
		yspeed *= -1;
	}

	/**
	 * revert the xSpeed
	 */
	public void revertXSpeed() {
		xspeed *= -1;

	}

	/**
	 * detect if the object is on the ground
	 * 
	 * @return yes on the ground, no not on the ground
	 */
	public boolean onTheGround() {
		if (y >= (1080 - this.height)) {
			this.onTheGround = true;
		} else {
			this.onTheGround = false;
		}
		return onTheGround;

	}

	/**
	 * update yspeed
	 * 
	 * @param d speed
	 */
	public void changeYSpeed(int d) {
		this.yspeed = d;
	}

	/**
	 * update xspeed
	 * 
	 * @param d speed
	 */
	public void changeXSpeed(int d) {
		this.xspeed += d;
	}

	/**
	 * get location of the object
	 * 
	 * @return location
	 */
	public float getY() {
		return this.y;
	}

	// set the color of rectangles
	/**
	 * set the object
	 * 
	 * @param color color of the object
	 */
	public void setColor(int color) {
		this.color = color;
	}

	// Draw rectangle
	/**
	 * display the object
	 */
	public void display() {
		// parent.fill(color);
		// parent.noStroke();
		// parent.rect(x, y, width, height);
	}

	public int[] getRenderInformation() {
		int[] list = new int[4];
		list[0] = (int) x;
		list[1] = (int) y;
		list[2] = (int) width;
		list[3] = (int) height;
		return list;
	}

	/**
	 * Let the object jump
	 */
	public void jump() {
		// jump = true;

		// yspeed = -100;

		// y += height;

		if (this.onTheGround()) {
			yspeed = -100;
		} else {
			jump = true;
			landed = false;
		}
	}
	public void setYspeed(int num) {
		yspeed = num;
	}
	public void setJump(boolean flag) {
		jump = true;
	}
	public void setLand(boolean flag) {
		landed = false;
	}
	// Move stripe
	/**
	 * move the object
	 */
	public void move() {
	
		// y += speed;

		// space to jump;

		// Boundary collision
		if (x < 0) {
			xspeed = (Math.abs(xspeed));
		} else if (x > rangx - width) {
			xspeed = -1 * Math.abs(xspeed);
		}
		if (y >= rangy - height) {
			yspeed = 0;
			y = rangy - height;

		} else if (y <= 0) {
			yspeed *=-1;
		}

		// y = 1080 - l;
		if (!onTheGround() && !landed) {
			yspeed += gravity;
		} else if (onTheGround() || landed) {
			yspeed = 0;
		}
		if (onTheGround()) {
			resetXSpeed();

		}

		// if(y > parent.width + speed) x = -20;
	}

	// convert to java.awt.rectangle
	/**
	 * convert object to Rectangle
	 * 
	 * @return Rectangle
	 */
	public Rectangle convert() {
		Rectangle r = new Rectangle();
		// r.setLocation((int)this.x, (int)this.y);
		r.setBounds((int) (x), (int) (y), (int) width, (int) height);

		return r;
	}

	/**
	 * get the number times of collision of square object.
	 * 
	 * @return collision
	 */
	public int getCollusion() {
		return this.collusion;
	}

	/**
	 * set the location of square object
	 * 
	 * @param x location
	 * @param y location
	 */
	public void setlocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * add a collusion.
	 *
	 */
	public void addCollusion() {
		this.collusion += 1;
	}

	

	

	/**
	 * detect landing return true if landed, false not landed
	 */
	public void land(GameObject goB) {
		// this.y = goB.getLocation()[1] - 5;
		landed = true;
		// yspeed = 0;
		resetXSpeed();

		this.changeXSpeed((int) goB.getXspeed());
		if (jump) {
			yspeed = -100;
			this.y -= 50;
			// this.changeYSpeed( -50);

			// jump();
			jump = false;
			// landed = false;

		} else {
			yspeed = 0;
			this.y = (int) (goB.getY() - height);
		}

	}

	public void resetXSpeed() {
		if (xspeed == 0) {
			xspeed = 10;
		} else {
			this.xspeed = (int) (this.speed * ((int) xspeed / (int) Math.abs(xspeed)));
		}

	}

	public boolean isCliable() {
		// TODO Auto-generated method stub
		return cliable;

	}

}
