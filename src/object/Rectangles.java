package object;



@SuppressWarnings("serial")
public class Rectangles extends GameObject{

    /**
     * create a rectangles
     * @param p the processing object
     */
	public Rectangles(int x, int y,int speedx, int speedy){
		//this.parent = p;
		//x = 0;
		//this.cliable = true;
		this.height = y;
		this.y = 1080 - height ;
		this.gravity=0;
		this.width = x;
		this.x = (1920-width)/2;
		this.xspeed=speedx;
		this.yspeed=speedy;
	}
	@Override
	public void revertXSpeed() {
		
	}
	public void move() {
		x+= xspeed;
		y+= yspeed;
		if (x <= 900 || x >= this.getRangex() - width) {
		xspeed *= -1;
	}
	if (y <= 0 || y >= this.getRangey() - height ) {
		yspeed *=-1;
			
//	}
	}
//	public void move(int rangx,int rangy) {
//		// TODO Auto-generated method stub
//		//super.move();
//		x += xspeed;
//		y += yspeed;
//		// y += speed;
//
//		// space to jump;
//
//		// Boundary collision
//		if (x <= 900 || x >= rangx) {
//			xspeed *= -1;
//		}
//		if (y <= 0 || y >= rangy ) {
//			yspeed *=-1;
//		
//			//landed = true;
//		}
		
		
		
	
	
	
}
}
