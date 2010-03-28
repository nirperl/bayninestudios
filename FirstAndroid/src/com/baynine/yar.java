package com.baynine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class yar extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new ClearGLSurfaceView(this);
        setContentView(mGLView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
        mRenderer = new ClearRenderer(context);
        setRenderer(mRenderer);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    public boolean onKeyUp(final int keyCode, KeyEvent msg) {
    	Log.d("key", "key event");
    	queueEvent(new Runnable(){
            public void run() {
            	if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
            	{
            		mRenderer.toggleParticle();
            	} else if (keyCode == KeyEvent.KEYCODE_MENU)
            	{
            		mRenderer.nextBlend();
            	} 
            }});
            return true;
    }

//    public boolean onKeyEvent(final KeyEvent keyEvent) {
//    	Log.d("key", "key event");
//    	queueEvent(new Runnable(){
//            public void run() {
//            	mRenderer.toggleParticle();
//            	if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER)
//            	{
//            		mRenderer.toggleParticle();
//            	}
//            }});
//            return true;
//        }

    public boolean onTrackBallEvent(final MotionEvent event) {

    	queueEvent(new Runnable(){
            public void run() {
            	if (event.getAction() == MotionEvent.ACTION_UP)
            	{
            		mRenderer.toggleParticle();
            	}
            }});
            return true;
    }

    public boolean onTouchEvent(final MotionEvent event) {

        queueEvent(new Runnable(){
        	boolean doNotMove = false;
            public void run() {
            	if ((event.getAction() == MotionEvent.ACTION_DOWN) ||
            			(event.getAction() == MotionEvent.ACTION_MOVE)) { 
	                mRenderer.setMoveTo(event.getX() / getWidth(),
	                        event.getY() / getHeight());
            	} else if (event.getAction() == MotionEvent.ACTION_UP) {
	                mRenderer.resetMoveTo();
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

class ClearRenderer implements GLSurfaceView.Renderer {

    private Player mPlayer;
    private Context mContext;
    private ParticleSystem mParticleFountain;
    private ParticleSystem mParticlePyro;
    private Speedo mSpeedo;
    private Model mTree;
    private Model mTest;
    private Model mPlant;
    private Pixie mPixie;
    private float mX = 0.0f;
    private float mY = 0.0f;
    private float landX = 0.0f;
    private float landY = 0.0f;
	private Landscape mLandscape;
	private float mCameraY = 10f;
	private float mCameraZ = 15f;
	private boolean particleOn = true;
	private int clickX = 0;
	private int clickY = 0;
	private int blendFactor = GL10.GL_ONE_MINUS_SRC_ALPHA;


	public ClearRenderer(Context context) {
		mContext = context;
		mPlayer = new Player(R.drawable.character2);
//		mPlayer = new Model(context, R.xml.pyramid, );
		mLandscape = new Landscape(context);
		mParticleFountain = new ParticleSystem(-1.5f, 1f, 0.0f, ParticleSystem.FOUNTAIN, Particle.WATER, 30);
		mParticlePyro = new ParticleSystem(1.0f, 0f, 3.0f, ParticleSystem.PYRO, Particle.LAVA, 20);
		mSpeedo = new Speedo();
		mTree = new Model(context, R.xml.palm3, 1);
		mTest = new Model(context, R.xml.post3, 2);
		mPlant = new Model(context, R.xml.plant, 2);
		mPixie = new Pixie(context, R.xml.pixie, 2);
	}

	public void setMoveTo(float x, float y) {
		float speed = .15f;
		mX = (x - 0.5f) * -1.0f * speed;
		mY = (y - 0.5f) * speed;
	}

	public void resetMoveTo() {
		mX = 0.0f;
		mY = 0.0f;
	}

	public void toggleParticle() {
		Log.d("particle","particle");
		if (particleOn) {
			particleOn = false;
		} else {
			particleOn = true;
		}
	}

	public void nextBlend() {
		int newBlend = GL10.GL_ZERO;
		switch (blendFactor)
		{
		case GL10.GL_ZERO: 					newBlend = GL10.GL_ONE;break;
		case GL10.GL_ONE: 					newBlend = GL10.GL_SRC_COLOR;break;
		case GL10.GL_SRC_COLOR: 			newBlend = GL10.GL_ONE_MINUS_SRC_COLOR;break;
		case GL10.GL_ONE_MINUS_SRC_COLOR: 	newBlend = GL10.GL_SRC_ALPHA;break;
		case GL10.GL_SRC_ALPHA: 			newBlend = GL10.GL_ONE_MINUS_SRC_ALPHA;break;
		case GL10.GL_ONE_MINUS_SRC_ALPHA: 	newBlend = GL10.GL_DST_ALPHA;break;
		case GL10.GL_DST_ALPHA: 			newBlend = GL10.GL_ONE_MINUS_DST_ALPHA;break;
		case GL10.GL_ONE_MINUS_DST_ALPHA: 	newBlend = GL10.GL_ZERO;break;
		default: break;
		}
		blendFactor = newBlend;

	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Do nothing special.
        GLU.gluPerspective(gl, 15.0f, 80.0f/48.0f, 1, 100);

// ISO
        GLU.gluLookAt(gl, 0f, -mCameraY, mCameraZ, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);
// Straight on
//        GLU.gluLookAt(gl, 0, 0, 20, 0f, 0f, 0f, 0f, 1.0f, 0f);

        // gotta do NICEST and DEPTH_TEST to make the textures look right
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);

//        gl.glShadeModel(GL10.GL_FLAT);
        gl.glShadeModel(GL10.GL_SMOOTH);

//        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_DEPTH_TEST);


        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);

        // alpha blending
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, blendFactor);

        gl.glAlphaFunc(GL10.GL_GREATER, 0.5f);
        gl.glEnable(GL10.GL_ALPHA_TEST);

        mPlayer.init(gl, mContext);
        mLandscape.init(gl, mContext, R.drawable.grass_sm);
        mSpeedo.recordRate(1);
        mTest.loadTexture(gl, mContext, R.drawable.post3);
        mTree.loadTexture(gl, mContext, R.drawable.palm3);
        mPlant.loadTexture(gl, mContext, R.drawable.grass4);
        mPixie.loadTexture(gl, mContext, R.drawable.pixie);
        //mParticleSystem.loadTexture(gl, mContext);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
    }

    public void onDrawFrame(GL10 gl) {

    	long startTime = System.currentTimeMillis();
//        gl.glEnable(GL10.GL_DITHER);

        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
//                GL10.GL_MODULATE);
              GL10.GL_REPLACE);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        landX = landX + mX;
		landY = landY + mY;
		if (landY > 0.8f) {
			landY = 0.8f;
		}

        gl.glTranslatef(landX, landY, 0.0f);
//    	Log.d("Position", Float.toString(landX)+ ","+Float.toString(landY));
		mLandscape.draw(gl);
    	mLandscape.drawForeground(gl);
        mTree.draw(gl,1f,3f,2f,30f,.10f);
//        mTree.draw(gl, -1.5f, -2.0f, 0f);
//        mTree.draw(gl,3.5f,-2.0f,0f,-20f, 1.2f);
//        mTree.draw(gl,-3.5f,2f,0f,110f,1.3f);
//        mTest.draw(gl, 0f, 0f, 0f, 45f+180f);
//	    	if (landX > -1) {
	    		mParticleFountain.draw(gl);
	    		mParticlePyro.draw(gl);
//	    	}

	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, blendFactor);
        gl.glEnable(GL10.GL_BLEND);

        gl.glEnable(GL10.GL_DEPTH_TEST);
        mPlant.draw(gl, -0.5f, 0.4f, 0f, 20f, .5f);
        mPlant.draw(gl, -0.25f, -.2f, 0f, 10f, 1.f);
        mPlant.draw(gl, 0.5f, 2.5f, 0f, -15f, .75f);
        mPlant.draw(gl, -1.5f, 1.5f, 0f, 5f, .5f);
        mPixie.animate();
        mPixie.draw(gl);
        gl.glDisable(GL10.GL_BLEND);

	    gl.glTranslatef(-landX, -landY, 0.0f);
        mPlayer.draw(gl);
        long endTime = System.currentTimeMillis();
        mSpeedo.recordRate(endTime - startTime);
        mSpeedo.draw(gl);
    }

}
