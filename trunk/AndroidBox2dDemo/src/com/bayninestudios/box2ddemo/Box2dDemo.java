package com.bayninestudios.box2ddemo;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.CircleShape;
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
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ToggleButton;

public class Box2dDemo extends Activity
{
    private GLSurfaceView mGLView;

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
}


class ClearGLSurfaceView extends GLSurfaceView
{
    ClearRenderer mRenderer;
	
    public ClearGLSurfaceView(Context context, SensorManager sensorMgr) {
        super(context);
        mRenderer = new ClearRenderer(context, sensorMgr);
        setRenderer(mRenderer);
    }

//    public boolean onKeyUp(final int keyCode, KeyEvent msg) {
//    	Log.d("key", "key event");
//    	queueEvent(new Runnable(){
//            public void run() {
//            	if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
//            	{
//            	} else if (keyCode == KeyEvent.KEYCODE_MENU)
//            	{
//            	} 
//            }});
//            return true;
//    }

    public boolean onTouchEvent(final MotionEvent event)
    {
    	// Q: Seriously? on every touch setSize!
    	// A: Yep. Until I find a better way
    	mRenderer.setSize(this.getWidth(),this.getHeight());

    	queueEvent(new Runnable(){
            public void run()
            {
            	if (event.getAction() == MotionEvent.ACTION_UP)
            	{
            		mRenderer.touchEvent(event.getX(), event.getY());
            	}
            }});
        	// if this isn't here, multiple events happen
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
	private PhysicsWorld mWorld;
	private DrawModel mBox;
	private DrawModel mLongBox;
	private DrawModel mCircle;
	private int activeModel = 1;

	// TODO: shouldn't be here, should be in a config file or something
	private float circleX = -5f;
	private float circleY = -15f;
	private float circleR = 5f;
	private Context mContext;
	SensorEventListener mSensorEventListener;
	private List<Sensor> sensors;
	private Sensor accSensor;
	private int width;
	private int height;
	
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
    		new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
    		14);
        mWorld = new PhysicsWorld();
        mWorld.createWorld();
        mWorld.addBox(0f, -25f, 50f, 10f, 0f, false);
        mWorld.addBox(-10f, 0f, 10f, .2f, -.2f, false);
        mWorld.addBox(12f, -15f, 2f, 10f, 0f, false);
        mWorld.addBall(circleX, circleY, circleR, false);

        mSensorEventListener = new SensorEventListener()
        {	
			@Override
			public void onSensorChanged(SensorEvent event)
			{
	        	float xAxis = event.values[SensorManager.DATA_X];
	        	float yAxis = event.values[SensorManager.DATA_Y];
	        	mWorld.setGrav(-xAxis,-yAxis);
			}
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		};

		sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size() > 0)
        {
          accSensor = sensors.get(0);
        } 
		sensorMgr.registerListener(mSensorEventListener,
				accSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
		GLU.gluOrtho2D(gl, -12f, 12f, -20f, 20f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
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

        gl.glColor4f(1f, 1f, 1f, 1f);  // white
    	drawActiveBody(gl);

    	gl.glColor4f(1f, 1f, 1f, 1f);  // white
    	Vec2 vec;
        Body mBody = mWorld.getBodyList();
        do
        {
    		Shape mShape = mBody.getShapeList();
    		if (mShape != null)
    		{
		    	vec = mBody.getPosition();
		    	float rot = mBody.getAngle() * 57f;  // convert radians to degrees
        		if (ShapeType.POLYGON_SHAPE == mShape.getType())
        		{
        			Vec2[] vertexes = ((PolygonShape)mShape).getVertices();
        	    	mBox.draw(gl, vec.x, vec.y, 0f, rot, vertexes[2].x, vertexes[2].y);
        		}
        		else if (ShapeType.CIRCLE_SHAPE == mShape.getType())
        		{
        			float radius = ((CircleShape)mShape).m_radius;
			    	mCircle.draw(gl, vec.x, vec.y, 0f, rot, radius);
        		}
    		}
	        mBody = mBody.getNext();
        }
        while (mBody != null);
        mWorld.update();
    }

    // switches between which model gets dropped 
    public void switchModel()
    {
    	activeModel++;
    	if (activeModel > 2)
    	{
    		activeModel = 0;
    	}
    }

    
    // interprets the touch event. Either switches models or
    // adds a model to the world.
    public void touchEvent(float x, float y)
    {
    	// look at all those magic numbers, truly magic!
    	// calculate world X and Y from the touch co-ords
    	float worldX = ((x-(this.width/2))*12f)/(this.width/2);
    	float worldY = ((y-(this.height/2))*-20f)/(this.height/2);

    	// if the user clicks lower than the box at the bottom
    	// switch models
    	if (worldY < -15f)
    	{
    		this.switchModel();
    	}
    	else
    	{
	    	if (activeModel == 0) {
	    		mWorld.addBall(worldX, worldY, 0.98f, true);
	    	} else if (activeModel == 1) {
	        	mWorld.addBox(worldX, worldY, .98f, .98f, 0f, true);
	    	} else if (activeModel == 2) {
	        	mWorld.addBox(worldX, worldY, .2f, 2f, 0f, true);
	    	}
    	}
    }
    
    // the easiest way to communicate to the renderer what the
    // size of the screen is
    public void setSize(int x, int y)
    {
    	this.width = x;
    	this.height = y;
    }
}
