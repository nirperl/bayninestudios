package com.baynine;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class ParticleSystem {

	public static int FOUNTAIN = 1;
	public static int PYRO = 2;
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	private int mParticleCount = 20;
	private Particle[] mParticles;
	private float xpos;
	private float ypos;
	private float zpos;
	private long oldTime;

	public ParticleSystem(float x, float y, float z, int systemType, int particleType, int particleCount) {
		this.xpos = x;
		this.ypos = y;
		this.zpos = z;
		oldTime = System.currentTimeMillis();
		Random gen = new Random(oldTime);
		// generate particles
		mParticleCount = particleCount;
		mParticles = new Particle[mParticleCount];
		for (int i = 0; i < mParticleCount; i++) {
			mParticles[i] = new Particle(gen, systemType, particleType);
		}

		// particle render
		float size = 0.04f;
		float px = size * 1.0f;
		float py = size * 1.50f;

	    float[] coords = {
	    		-px,0.0f,0.0f,
	    		px,0.0f,0.0f,
	    		0.0f,0.0f,py
	    };
	    short[] icoords = {
	    		0,1,2
	    };

	    mVertexBuffer = FloatBuffer.wrap(coords);
	    mIndexBuffer = ShortBuffer.wrap(icoords);
	}

	
	public void draw(GL10 gl) {
//        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_TEXTURE_2D);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glPushMatrix();
        gl.glTranslatef(xpos, ypos, zpos);
        long newTime = System.currentTimeMillis();
        float timeMod = (newTime - oldTime) / 1000.0f;
        oldTime = newTime;
	    for (int i = 0; i < mParticleCount; i++) {
    		mParticles[i].update(timeMod);
	    	if (mParticles[i].alive == true) {
		    	mParticles[i].drawColor(gl);
				gl.glPushMatrix();
				gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);
			    gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
			    gl.glPopMatrix();
	    	}
	    }
	    gl.glPopMatrix();
//        gl.glDisable(GL10.GL_BLEND);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
	}
}
