package object;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class Squares extends GameObject{
	//double [][] direction;
	
	public Squares(int x, int y){
		this.cliable = false;
		this.speed = 10;
		this.gravity = 10;
		this.collusion = 0;
		//this.parent = p;
		//x = 0;
		this.height = x;
		this.y = 1080 - height;
		this.xspeed = 10;
		this.yspeed = 0;
	    this.width = y;
		this.x = 50;	
	}

	
	
}
