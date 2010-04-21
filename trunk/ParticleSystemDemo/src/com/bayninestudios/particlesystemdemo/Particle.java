package com.bayninestudios.particlesystemdemo;

public class Particle {

	// location
	public float x;
	public float y;
	public float z;
	
	public Particle() {	
	}

	// the constructor which also assigns location
	public Particle(float newx, float newy, float newz) {
		super();
		this.x = newx;
		this.y = newy;
		this.z = newz;
	}
}
