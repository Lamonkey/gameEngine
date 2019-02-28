function update(){
    if (gameObject.onTheGround()) {
			gameObject.setYspeed(-100);
		} else {
			gameObject.setJump(true);
			gameObject.setLand(false);
		}
   // gameObject.jump();
}