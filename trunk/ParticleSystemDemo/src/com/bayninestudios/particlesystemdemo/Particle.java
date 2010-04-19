package com.bayninestudios.particlesystemdemo;

import java.util.Random;

public class Particle {

	// location
	public float x;
	public float y;
	public float z;
	
	public Particle(Random gen) {
		this.x = gen.nextFloat();
		this.y = gen.nextFloat();
		this.z = gen.nextFloat();
	}
}
