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
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
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
            	else if (keyCode == KeyEvent.KEYCODE_1)
            	{
            		mRenderer.toggleTextures();
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
    private Player mPlayer;
    private DrawModel mNPC;

    private float mCameraX = 0f;
    private float zoom = 1.0f;
	private float mCameraY = -8f*zoom;
	private float mCameraZ = 12f*zoom;

	private float charDX = 0.0f;
	private float charDY = 0.0f;
	private float MOVESPEED = 0.03f;
	private boolean onQuest = false;

	public ClearRenderer(Context context)
	{
		mContext = context;
		mLandscape = new Landscape(context);
		mSpeedo = new Speedo();
		mPlayer = new Player(context);
		mNPC = new DrawModel(context, R.xml.player);
	}

	public void toggleTextures()
	{
		if (mLandscape.useTextures)
			mLandscape.useTextures = false;
		else mLandscape.useTextures = true;
	}

	public void toggleQuest()
	{
		if (onQuest)
			onQuest = false;
		else onQuest = true;
	}
	public void playerAction()
	{
		float REACH = 0.4f;
		int newX = (int)mPlayer.x;
		int newY = (int)mPlayer.y;
		if (mPlayer.facing == 0f)
			newY = (int)(mPlayer.y - REACH);
		else if (mPlayer.facing == 180f)
			newY = (int)(mPlayer.y + REACH);
		else if (mPlayer.facing == -90f)
			newX = (int)(mPlayer.x - REACH);
		else if (mPlayer.facing == 90f)
			newX = (int)(mPlayer.x + REACH);

		if ((newY == 12) && (newX == 50))
		{
			toggleQuest();
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

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
        		GL10.GL_REPLACE);

        mLandscape.loadTextures(gl, mContext);
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
        gl.glTranslatef(-mPlayer.x, -mPlayer.y, -mPlayer.z);
        mLandscape.draw(gl, mPlayer.x, mPlayer.y);

        // draw NPC
        if (onQuest)
        	gl.glColor4f(1f, .3f, .3f, 1f);
        else gl.glColor4f(1f, 1f, 0f, 1f);
        mNPC.draw(gl, 50.5f, 12.5f, 0f);

        gl.glPopMatrix();

        mPlayer.setFacing(charDX, charDY);

        // draw player
        mPlayer.draw(gl);

        float newCharX = mPlayer.x + charDX;
        float newCharY = mPlayer.y + charDY;

        if (mLandscape.checkPassable(newCharX, mPlayer.y))
        {
        	mPlayer.x = newCharX;
        }
        if (mLandscape.checkPassable(mPlayer.x, newCharY))
        {
        	mPlayer.y = newCharY;
        }

        mSpeedo.setEndTime();
        mSpeedo.draw(gl);
    }
}
