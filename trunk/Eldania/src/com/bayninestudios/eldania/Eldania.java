package com.bayninestudios.eldania;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;


public class Eldania extends Activity
{
    private GLSurfaceView mGLView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLView = new ClearGLSurfaceView(this);
        setContentView(mGLView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
	
    public ClearGLSurfaceView(Context context)
    {
        super(context);
        mRenderer = new ClearRenderer(context);
        setRenderer(mRenderer);
//        setFocusable(true);
//        setFocusableInTouchMode(true);
//        requestFocus();
    }

    public boolean onTouchEvent(final MotionEvent event)
    {
//        queueEvent(new Runnable(){
//            public void run()
//            {
//            }});
            return true;
        }

    public boolean onKeyUp(final int keyCode, KeyEvent msg)
    {
    	queueEvent(new Runnable(){
            public void run() {
            	if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            	{
            		mRenderer.stopCharacter(keyCode);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
            	{
            		mRenderer.stopCharacter(keyCode);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
            	{
            		mRenderer.stopCharacter(keyCode);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            	{
            		mRenderer.stopCharacter(keyCode);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_BACK)
            	{
            	} 
            }});
            return true;
    }

    public boolean onKeyDown(final int keyCode, KeyEvent msg)
    {
    	queueEvent(new Runnable(){
            public void run() {
            	Log.d("keycode","::: "+keyCode);
            	if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            	{
            		mRenderer.moveCharacter(KeyEvent.KEYCODE_DPAD_LEFT);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
            	{
            		mRenderer.moveCharacter(keyCode);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            	{
            		mRenderer.moveCharacter(keyCode);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
            	{
            		mRenderer.moveCharacter(keyCode);
            	}
            	else if (keyCode == KeyEvent.KEYCODE_2)
            	{
            		mRenderer.playerAction();
            	} 
            	else if (keyCode == KeyEvent.KEYCODE_BACK)
            	{
            	} 
            }});
            return true;
    }
}

class ClearRenderer implements GLSurfaceView.Renderer
{
	private Context mContext;
    private Landscape mLandscape;
    private Speedo mSpeedo;
    private DrawModel mPlayer;

    private float mCameraX = 0f;
	private float mCameraY = -8f;
	private float mCameraZ = 12f;

	private float charX = 50.5f;
	private float charY = 10.5f;
	private float charZ = 1f;
	private float charDX = 0.0f;
	private float charDY = 0.0f;
	private float MOVESPEED = 0.03f;

	public ClearRenderer(Context context)
	{
		mContext = context;
		mLandscape = new Landscape(context);
		mSpeedo = new Speedo();
		mPlayer = new DrawModel(context, R.xml.player);
	}

	public void playerAction()
	{
		int newY = (int)(charY + 0.5f);
		if ((newY == 12) && (((int)charX) == 50))
		{
			Log.d("quest","You get a new Quest!");
		}
		else
		{
			Log.d("action","checking: "+charX+","+charY);
		}
	}

	public void stopCharacter(int keyCode)
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			charDX = charDX + MOVESPEED;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			charDX = charDX - MOVESPEED;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			charDY = charDY - MOVESPEED;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			charDY = charDY + MOVESPEED;
		}
	}

	public void moveCharacter(int keyCode)
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			charDX = charDX - MOVESPEED;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			charDX = charDX + MOVESPEED;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			charDY = charDY + MOVESPEED;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			charDY = charDY - MOVESPEED;
		}
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
    	gl.glLoadIdentity();
        GLU.gluPerspective(gl, 15.0f, 80.0f/48.0f, 1, 100);
        GLU.gluLookAt(gl, mCameraX, mCameraY, mCameraZ, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h)
    {
        gl.glViewport(0, 0, w, h);
    }

    public void onDrawFrame(GL10 gl)
    {
    	mSpeedo.setStartTime();

    	// not needed until I move the camera around
//    	gl.glLoadIdentity();
//        GLU.gluPerspective(gl, 15.0f, 80.0f/48.0f, 1, 100);
//        GLU.gluLookAt(gl, mCameraX, mCameraY, mCameraZ, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);

        gl.glClearColor(0f, 0f, 0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glPushMatrix();
        gl.glTranslatef(-charX, -charY, 0);
        mLandscape.draw(gl, charX, charY);
        gl.glColor4f(0f, 1f, 1f, 1f);
        mPlayer.draw(gl, 50.5f, 12.5f, 0f);
        gl.glPopMatrix();
        gl.glColor4f(0f, 1f, 1f, 1f);
        mPlayer.draw(gl);

        float newCharX = charX + charDX;
        float newCharY = charY + charDY;

        if (mLandscape.checkPassable(newCharX, charY))
        {
        	charX = newCharX;
        }
        if (mLandscape.checkPassable(charX, newCharY))
        {
        	charY = newCharY;
        }

        mSpeedo.setEndTime();
        mSpeedo.draw(gl);
    }
}
