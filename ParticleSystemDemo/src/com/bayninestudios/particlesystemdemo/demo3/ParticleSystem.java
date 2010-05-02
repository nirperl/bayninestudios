package com.bayninestudios.particlesystemdemo.demo3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class ParticleSystem {
	private Particle[] mParticles;

	// probably a good idea to add these two to the constructor
	private int PARTICLECOUNT = 100;
	// gravity is 10, no real reason, but gotta start with something
	// and 10m/s sounds good
	private float GRAVITY = 10f;
	private float PARTICLESIZE = 0.04f;
	// don't let particles go below this z value
	private float FLOOR = 0.0f;
	
	// this is used to track the time of the last update so that
	// we can calculate a frame rate to find out how far a particle
	// has moved between updates
	private long lastTime;

	// for use to draw the particle
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	
	private Random gen;

	public ParticleSystem() {
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

    public void draw(GL10 gl) {
    	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
    	gl.glColor4f(1f, 1f, 1f, 1f);
		for (int i = 0; i < PARTICLECOUNT; i++) {
			gl.glPushMatrix();
			gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);
		    gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
		    gl.glPopMatrix();
    	}
	}

    private void initParticle(int i)
    {
		// loop through all the particles and create new instances of each one
		mParticles[i].x = 0f;
		mParticles[i].y = 0f;
		mParticles[i].z = 0f;
		// random x and y speed between -1.0 and 1.0
		mParticles[i].dx = (gen.nextFloat()*2f) - 1f;
		mParticles[i].dy = (gen.nextFloat()*2f) - 1f;
		// random z speed between 4.0 and 7.0
		mParticles[i].dz = (gen.nextFloat()*3) + 4f;
    }

    // update the particle system, move everything
    public void update()
    {
    	// calculate time between frames in seconds
    	long currentTime = System.currentTimeMillis();
    	float timeFrame = (currentTime - lastTime)/1000f;
    	// replace the last time with the current time.
    	lastTime = currentTime;

    	// move the particles
    	// first apply gravity to the z speed
    	// second move the particle according to its speed and time frame
    	// third re-init a particle if it has gone below the 'floor'
    	for (int i = 0; i < PARTICLECOUNT; i++) {
			// apply a gravity to the z speed, in this case 
			mParticles[i].dz = mParticles[i].dz - (GRAVITY*timeFrame);

			// move the particle according to it's speed
			mParticles[i].x = mParticles[i].x + (mParticles[i].dx*timeFrame);
			mParticles[i].y = mParticles[i].y + (mParticles[i].dy*timeFrame);
			mParticles[i].z = mParticles[i].z + (mParticles[i].dz*timeFrame);

			// if the particle hits the 'floor' respawn it
			if (mParticles[i].z < FLOOR) {
				initParticle(i);
			}
		}
    }
}
