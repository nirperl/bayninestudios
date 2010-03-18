package com.bayninestudios;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.collision.Shape;
import org.jbox2d.collision.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.MotionEvent;

public class Box2dDemo extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
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

    private GLSurfaceView mGLView;
}

class ClearGLSurfaceView extends GLSurfaceView {
    public ClearGLSurfaceView(Context context) {
        super(context);
        mRenderer = new ClearRenderer();
        setRenderer(mRenderer);
    }

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run()
            {
            	mRenderer.addBall(event.getX(), event.getY());
            }});
            return true;
        }

        ClearRenderer mRenderer;
}

class ClearRenderer implements GLSurfaceView.Renderer
{
	private PhysicsWorld mWorld;
	private DrawModel mBox;
	private DrawModel mCircle;

	// TODO: shouldn't be here, should be in a config file or something
	private float circleX = 60f;
	private float circleY = 600f;
	private float circleR = 50f;

	public ClearRenderer()
	{
    	mBox = new DrawModel(new float[]{
    			  -1,-1,0,
				  1,-1,0,
				  1,1,0,
				  -1,1,0
				  },
				  new short[]{0,1,2,3,0},
    			  5);
    	mCircle = new DrawModel(new float[]{
    		  0,0,0,
  			  0,1,0,
  			  -.5f,.866f,0,
  			  -.866f,.5f,0,
  			  -1,0,0,
  			  -.866f,-.5f,0,
  			  -.5f,-.866f,0,
  			  0,-1,0,
  			  .5f,-.866f,0,
  			  .866f,-.5f,0,
  			  1,0,0,
  			  .866f,.5f,0,
  			  .5f,.866f,0
    		},
    		new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1},
    		13);
        mWorld = new PhysicsWorld();
        mWorld.createWorld();
        mWorld.createGround();
        mWorld.createGroundCircle(circleX, circleY, circleR);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
		GLU.gluOrtho2D(gl, -12f, 12f, -20f, 20f);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h)
    {
        gl.glViewport(0, 0, w, h);
    }

    public void onDrawFrame(GL10 gl)
    {
    	gl.glClearColor(0, 0, .5f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    	Vec2 vec;
        Body mBody = mWorld.getBodyList();
        do
        {
        	// only draw non-static bodies for now
        	if (!mBody.isStatic())
        	{
        		Shape mShape = mBody.getShapeList();
		    	vec = mBody.getPosition();
		    	float rot = mBody.getAngle() * 57f;
        		if (ShapeType.POLYGON_SHAPE == mShape.getType())
        		{
			    	mBox.draw(gl, vec.x, vec.y, 0f, rot, 0.97f);
        		}
        		else if (ShapeType.CIRCLE_SHAPE == mShape.getType())
        		{
			    	mCircle.draw(gl, vec.x, vec.y, 0f, rot, 0.97f);        			
        		}
        	}
	        mBody = mBody.getNext();
        }
        while (mBody != null);
        mWorld.update();
    }

    public void addBall(float x, float y)
    {
    	mWorld.addBall(mWorld.translateScreenX(x)-24f, (y/-10f)+40f);
    	mWorld.addBox(mWorld.translateScreenX(x)-24f, (y/-10f)+40f);
    }
}
