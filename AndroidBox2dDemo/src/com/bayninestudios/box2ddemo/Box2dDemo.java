package com.bayninestudios.box2ddemo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.collision.Shape;
import org.jbox2d.collision.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class Box2dDemo extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGLView = new ClearGLSurfaceView(this, (SensorManager) getSystemService(SENSOR_SERVICE));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

class ClearGLSurfaceView extends GLSurfaceView
{
    public ClearGLSurfaceView(Context context, SensorManager sensorMgr) {
        super(context);
        mRenderer = new ClearRenderer(context, sensorMgr);
        setRenderer(mRenderer);
    }

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run()
            {
            	if (event.getAction() == MotionEvent.ACTION_UP)
            	{
            		if (event.getY() > 700f) {
            			mRenderer.switchModel();
            		}
            		else
            		{
            			mRenderer.addBall(event.getX(), event.getY());
            		}
            	}
            }});
	    	try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            return true;
        }

        ClearRenderer mRenderer;
}

class ClearRenderer implements GLSurfaceView.Renderer
{
	private PhysicsWorld mWorld;
	private DrawModel mBox;
	private DrawModel mLongBox;
	private DrawModel mCircle;
	private int activeModel = 1;

	// TODO: shouldn't be here, should be in a config file or something
	private float circleX = 0f;
	private float circleY = -15f;
	private float circleR = 5f;
	private Context mContext;
	SensorListener mySensorListener;
	SensorEventListener mSensorEventListener;

	public ClearRenderer(Context newContext, SensorManager sensorMgr)
	{
		mContext = newContext;
    	mBox = new DrawModel(new float[]{
    			  -1,-1,0,
				  1,-1,0,
				  1,1,0,
				  -1,1,0
				  },
				  new float[]{
	  			  0f,1f,
				  1f,1f,
				  1f,0f,
				  0f,0f
    			  },
				  new short[]{0,1,2,3,0},
    			  5);
    	mLongBox = new DrawModel(new float[]{
    			-.2f,-2f,0,
				.2f,-2f,0,
				.2f,2f,0,
				-.2f,2f,0
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
  			  .5f,.866f,0,
  			  0f,1f,0
    		},
    		new float[]{
      		  0.5f,0.5f,
			  0.5f,0.0f,
			  .25f,.067f,
			  .067f,.25f,
			  0.0f,0.5f,
			  .067f,.75f,
			  .25f,.933f,
			  0.5f,1.0f,
			  .75f,.933f,
			  .933f,.75f,
			  1.0f,0.5f,
			  .933f,.25f,
			  .75f,.067f,
			  .5f,.0f
      		},
      		// one vertex short of a circle to make the pac man shape
    		new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
    		14);
        mWorld = new PhysicsWorld();
        mWorld.createWorld();
        mWorld.createGround();
        mWorld.createGroundCircle(circleX, circleY, circleR);

        mSensorEventListener = new SensorEventListener()
        {	
			@Override
			public void onSensorChanged(SensorEvent event)
			{
	        	float xAxis = event.values[SensorManager.DATA_X];
	        	float yAxis = event.values[SensorManager.DATA_Y];
	        	mWorld.setGrav(xAxis,yAxis);
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		};

		// TODO: use SensorEventListener instead of SensorListener
        mySensorListener = new SensorListener() {
	    	public void onSensorChanged(int sensor, float[] values)
	    	{
	        	if (sensor == SensorManager.SENSOR_ACCELEROMETER)
	        	{
		        	float xAxis = values[SensorManager.DATA_X];
		        	float yAxis = values[SensorManager.DATA_Y];
		        	mWorld.setGrav(xAxis,yAxis);
	        	}
	    	}
	    	public void onAccuracyChanged(int sensor, int accuracy) { }
	    };
		sensorMgr.registerListener(mySensorListener,
				SensorManager.SENSOR_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_GAME);
//		sensorMgr.registerListener(mSensorEventListener,
//				SensorManager.SENSOR_ACCELEROMETER,
//				SensorManager.SENSOR_DELAY_UI);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
		GLU.gluOrtho2D(gl, -12f, 12f, -20f, 20f);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);
        mBox.loadTexture(gl, mContext, R.drawable.box);
        mCircle.loadTexture(gl, mContext, R.drawable.soccerball);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h)
    {
        gl.glViewport(0, 0, w, h);
    }

    public void drawActiveBody(GL10 gl)
    {
    	float x = 10f;
    	float y = 17f;

    	switch (activeModel) {
    		case 0: mCircle.draw(gl, x, y, 0f); break;
    		case 1: mBox.draw(gl, x, y, 0f); break;
    		case 2: mLongBox.draw(gl, x, y, 0f); break;
    	}
    }

    public void onDrawFrame(GL10 gl)
    {
    	gl.glClearColor(0, 0, .5f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
            GL10.GL_REPLACE);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // TODO: blech, hard coding the drawing of the ground objects
        gl.glColor4f(1f, 1f, 1f, 1f);  // white
    	mCircle.draw(gl, 0f, circleY, 0f, 180f, circleR);
    	mBox.draw(gl, 0, -25f, 0f, 0f, 50f, 10f);

    	drawActiveBody(gl);

    	gl.glColor4f(1f, 1f, 1f, 1f);  // white
    	Vec2 vec;
        Body mBody = mWorld.getBodyList();
        do
        {
        	// only draw non-static bodies for now
        	if (!mBody.isStatic())
        	{
        		Shape mShape = mBody.getShapeList();
		    	vec = mBody.getPosition();
		    	float rot = mBody.getAngle() * 57f;  // convert radians to degrees
        		if (ShapeType.POLYGON_SHAPE == mShape.getType())
        		{
        			Object userData = mShape.getUserData();
        			// only checking if the userData is set, if it is, it's a long box
        			// if not, it's a normal box.  should step into the shape and get
        			// the x y instead
        			if (userData == null) {
        				mBox.draw(gl, vec.x, vec.y, 0f, rot, 0.98f);
        			} else {
        				mLongBox.draw(gl, vec.x, vec.y, 0f, rot, 0.98f);
        			}
        		}
        		else if (ShapeType.CIRCLE_SHAPE == mShape.getType())
        		{
			    	mCircle.draw(gl, vec.x, vec.y, 0f, rot, 0.98f);
        		}
        	}
	        mBody = mBody.getNext();
        }
        while (mBody != null);
        mWorld.update();
    }

    public void switchModel()
    {
    	activeModel++;
    	if (activeModel > 2)
    	{
    		activeModel = 0;
    	}
    }

    public void addBall(float x, float y)
    {
    	if (activeModel == 0) {
    		mWorld.addBall((x/20f) - 12f, (y - 400)/-20f);
    	} else if (activeModel == 1) {
        	mWorld.addBox((x/20f) - 12f, (y - 400)/-20f);
    	} else if (activeModel == 2) {
        	mWorld.addLongBox((x/20f) - 12f, (y - 400)/-20f);
    	}
    }
}
