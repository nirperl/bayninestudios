package com.bayninestudios.particlesystemdemo.demo7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class ParticleSystem {
	private Particle[] mParticles;

	public float x, y, z;

	// probably a good idea to add these two to the constructor
	private int PARTICLECOUNT = 50;
	private int activeParticles = 0;

	// gravity is 10, no real reason, but gotta start with something
	// and 10m/s sounds good
	private float GRAVITY = .2f;
	private float AIR_RESISTANCE = 15.0f;
	private float PARTICLESIZE = 0.02f;
	
	// this is used to track the time of the last update so that
	// we can calculate a frame rate to find out how far a particle
	// has moved between updates
	private long lastTime;

	// for use to draw the particle
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	
	private Random gen;

	public ParticleSystem()
	{
		mParticles = new Particle[PARTICLECOUNT];

		// setup the random number generator
		gen = new Random(System.currentTimeMillis());
		// loop through all the particles and create new instances of each one
		for (int i=0; i < PARTICLECOUNT; i++) {
			mParticles[i] = new Particle();
			initParticle(i);
		}

		// a simple triangle, kinda like this ^
	    float[] coords = {
	    		-PARTICLESIZE,0.0f,0.0f,
	    		PARTICLESIZE,0.0f,0.0f,
	    		0.0f,0.0f,PARTICLESIZE};
	    short[] icoords = {0,1,2};

	    mVertexBuffer = makeFloatBuffer(coords);
	    mIndexBuffer = makeShortBuffer(icoords);
	    
	    lastTime = System.currentTimeMillis();
	}

	// used to make native order float buffers
	private FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

	// used to make native order short buffers
    private ShortBuffer makeShortBuffer(short[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    public void draw(GL10 gl)
    {
    	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		for (int i = 0; i < PARTICLECOUNT; i++)
		{
			if (mParticles[i].timeToLive > 0f)
			{
				gl.glPushMatrix();
				gl.glColor4f(mParticles[i].red, mParticles[i].green, mParticles[i].blue, 1.0f);
				gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);
			    gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
			    gl.glPopMatrix();
			}
    	}
		gl.glPopMatrix();
	}

    private void initParticle(int i)
	{
	    // loop through all the particles and create new instances of each one
	    mParticles[i].x = 0f;
	    mParticles[i].y = 0f;
	    mParticles[i].z = 0f;
	
	    float DOUBLESPEED = 20f;
	    mParticles[i].dx = (gen.nextFloat()*DOUBLESPEED) - (DOUBLESPEED/2f);
	    mParticles[i].dy = (gen.nextFloat()*DOUBLESPEED) - (DOUBLESPEED/2f);
	    mParticles[i].dz = (gen.nextFloat()*DOUBLESPEED) - (DOUBLESPEED/2f);
	
	    // completely random color
	    mParticles[i].red = (gen.nextFloat()*.5f)+.5f;
	    mParticles[i].green = (gen.nextFloat()*.5f)+.5f;
	    mParticles[i].blue = (gen.nextFloat()*.5f)+.5f;
		
	    // set time to live
	    mParticles[i].timeToLive = (gen.nextFloat()*1.5f) + 0f;
	}

	// update the particle system, move everything
    public void update()
    {
    	// calculate time between frames in seconds
    	long currentTime = System.currentTimeMillis();
    	float timeFrame = (currentTime - lastTime)/1000f;
    	// replace the last time with the current time.
    	lastTime = currentTime;
    	// dead particle count, kill the system if all dead
    	int deadCount = 0;

    	// move the particles
    	for (int i = 0; i < PARTICLECOUNT; i++)
    	{
			// first apply a gravity to the z speed, in this case 

			// apply air resistance
			mParticles[i].dx = mParticles[i].dx - (mParticles[i].dx*AIR_RESISTANCE*timeFrame);
			mParticles[i].dy = mParticles[i].dy - (mParticles[i].dy*AIR_RESISTANCE*timeFrame);
			mParticles[i].dz = mParticles[i].dz - (mParticles[i].dz*AIR_RESISTANCE*timeFrame);
			
			// second move the particle according to it's speed
			mParticles[i].x = mParticles[i].x + (mParticles[i].dx*timeFrame);
			mParticles[i].y = mParticles[i].y + (mParticles[i].dy*timeFrame);
			mParticles[i].z = mParticles[i].z + (mParticles[i].dz*timeFrame);

			// add a gravity effect
			mParticles[i].z = mParticles[i].z - (GRAVITY*timeFrame);

			// fourth decrement the time to live for the particle,
			// if it gets below zero, add to the dead count
			mParticles[i].timeToLive = mParticles[i].timeToLive - timeFrame;
			if (mParticles[i].timeToLive < 0f)
			{
				deadCount++;
			}
		}
    	if (deadCount == PARTICLECOUNT)
    	{
//    		systemAlive = false;
    	}
    }
}
