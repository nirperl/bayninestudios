package com.bayninestudios.particlesystemdemo.demo7;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

public class ParticleSystemDemo extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLView = new ClearGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 1, 0, "Visit bayninestudios.com");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        	case 1:
        		Intent myIntent = new Intent(Intent.ACTION_VIEW,
        				android.net.Uri.parse("http://www.bayninestudios.com"));
        		startActivity(myIntent);
        		return true;
        }
        return false;
    }

	private GLSurfaceView mGLView;
}

class ClearGLSurfaceView extends GLSurfaceView {

	ClearRenderer mRenderer;

	public ClearGLSurfaceView(Context context) {
        super(context);
        mRenderer = new ClearRenderer();
        setRenderer(mRenderer);
    }

    public boolean onTouchEvent(final MotionEvent event)
    {
    	mRenderer.setSize(this.getWidth(),this.getHeight());
    	queueEvent(new Runnable()
    	{
            public void run()
            {
        		mRenderer.touchEvent(event.getX(), event.getY(), event.getAction());
            }
        });
    	// if this isn't here, multiple events happen on touch
    	try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return true;
    }
}

class ClearRenderer implements GLSurfaceView.Renderer
{
	private ParticleSystem[] mParticleSystems;
	private int systemCount = 0;
	private int screenX, screenY;

	public ClearRenderer()
	{
		mParticleSystems = new ParticleSystem[40];
	}

	// I haven't found a good way to initialize the size so that
	// touch events are at the right GL co-ords.  I think it might be
	// better to do the translation at the touch event
    public void setSize(int width, int height)
    {
    	this.screenX = width;
    	this.screenY = height;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        GLU.gluPerspective(gl, 15.0f, 48.0f/80.0f, 1, 100);
//        GLU.gluLookAt(gl, 0f, -17f, 5f, 0.0f, 0.0f, 1f, 0.0f, 1.0f, 0.0f);
        GLU.gluLookAt(gl, 0f, -10f, 0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h)
    {
        gl.glViewport(0, 0, w, h);
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
    	gl.glClearColor(0, 0, 0, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        for (int i=0; i < systemCount; i++)
        {
	        mParticleSystems[i].update();
	        mParticleSystems[i].draw(gl);
        }
    }

    // init a new particle system on touch event
    public void touchEvent(float x, float y, int eventCode)
    {
    	if (eventCode == MotionEvent.ACTION_UP)
    	{
    		// only allow 40 explosions.
	    	if (systemCount < 40) {
	    		mParticleSystems[systemCount] = new ParticleSystem();
	    		mParticleSystems[systemCount].x = (x - (screenX/2)) / (screenX/2);
	    		mParticleSystems[systemCount].z = (y - (screenY/2)) / (screenX/-2);
	    		systemCount++;
	    	}
    	}
    }
}