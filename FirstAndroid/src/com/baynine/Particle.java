package com.baynine;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Particle {

	public static int LAVA = 1;
	public static int WATER = 2;

	private int particleType;
	private int systemType;
	
	// location
	public float x;
	public float y;
	public float z;
	// speed
	private float dx;
	private float dy;
	private float dz;
	// acceleration
	private float ax;
	private float ay;
	private float az;
	// gravity
	private float gravity = -10.0f;
	// color
	private float cr;
	private float cg;
	private float cb;

	boolean hitGround;
	public boolean alive = true;
	float timeToLive;
	Random gen;
	private float TIMEONGROUND = 0.2f;
	private float FLOOR = 0.001f;

	public Particle(Random gen, int systemType, int particleType)
	{
		this.gen = gen;
		this.particleType = particleType;
		this.systemType = systemType;
		resetParticle();
	}

	private void resetParticle()
	{
		this.alive = true;
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.01f;  // just above the floor

		// particle color
		if (particleType == LAVA) {
			this.cr = 1.0f;
			this.cb = 0.0f;
			int cg = gen.nextInt(100);
			this.cg = cg / 100f;
		} else if (particleType == WATER){
			int cb = gen.nextInt(50)+50;
			this.cb = cb / 100f;
			this.cr = this.cb * .7f;
			this.cg = this.cb * .7f;
		} else {
			this.cb = 1.0f;
			this.cr = 1.0f;
			this.cg = 1.0f;
		}

		if (systemType == ParticleSystem.FOUNTAIN) {
			int dx = gen.nextInt(400) - 200;
			int dy = gen.nextInt(400) - 200;
			int dz = gen.nextInt(50) + 50;

			this.dx = ((dx * 1.0f) / 100.0f)* .20f;
			this.dy = ((dy * 1.0f) / 100.0f)* .20f;
			this.dz = (dz * 1.0f) / 100.0f * 7.0f;

			hitGround = false;
			timeToLive = 0.0f;
		} else if (systemType == ParticleSystem.PYRO) {
			this.dx = (((gen.nextInt(200)-100) * 5.0f) / 100.0f);
			this.dy = (((gen.nextInt(200)-100) * 5.0f) / 100.0f);
			this.dz = (((gen.nextInt(200)-100) * 5.0f) / 100.0f);
//			this.ax = ((gen.nextInt(100) * 10.0f) / 100.0f);
//			this.ay = ((gen.nextInt(100) * 10.0f) / 100.0f);
//			this.az = ((gen.nextInt(100) * 10.0f) / 100.0f);
			// gravity acceleration
			hitGround = false;
			timeToLive = (gen.nextInt(5)+10) / 10f;
			timeToLive = 2.0f;
		}

	}

	public void update(float timeMod)
	{
		if (!hitGround)
		{
			// apply gravity
			if (systemType == ParticleSystem.FOUNTAIN) {
				dz = dz + (gravity * timeMod);
			} else if (systemType == ParticleSystem.PYRO) {
				float airResistance = 6.0f;
				dx = dx * (1-(airResistance*timeMod));
				dy = dy * (1-(airResistance*timeMod));
				dz = dz * (1-(airResistance*timeMod));
			} 

			x = x + (dx * timeMod);
			y = y + (dy * timeMod);
			if (systemType == ParticleSystem.FOUNTAIN) {
				z = z + (dz * timeMod);
			} else if (systemType == ParticleSystem.PYRO) {
				z = z + (dz * timeMod);
				z = z + (-1.0f * timeMod);
			} 
		}
		if (systemType == ParticleSystem.FOUNTAIN) {
			if (z <= FLOOR) {
				z = FLOOR;
				if (hitGround == false) {
					hitGround = true;
					timeToLive = TIMEONGROUND;
				} else {
					if (timeToLive < 0.0f) {
						resetParticle();
					} else {
						timeToLive = timeToLive - timeMod;
					}
				}
			}
		} else if (systemType == ParticleSystem.PYRO) {
			if (timeToLive < 0.0f) {
				resetParticle();
//				this.alive = false;
//				timeToLive = timeToLive - timeMod;
//				if (timeToLive < -3.0f) {
//					resetParticle();
//				}
			} else {
				timeToLive = timeToLive - timeMod;
			}
		}
	}

	// TODO don't like this, no GL code should be here
	public void drawColor(GL10 gl) {
        gl.glColor4f(cr, cg, cb, 1f);
	}
}
