function update(){
    if (gameObject.onTheGround()) {
			gameObject.yspeed = -100;
		} else {
			gameObject.jump = true;
			gameObject.landed = false;
		}
   // gameObject.jump();
}