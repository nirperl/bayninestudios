package com.bayninestudios.eldania;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

public class Eldania extends Activity
{
    private GLSurfaceView mGLView;
    private PowerManager.WakeLock wl;

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
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mGLView.onPause();
        wl.release();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mGLView.onResume();
        wl.acquire();
    }
}

class ClearGLSurfaceView extends GLSurfaceView
{
    ClearRenderer mRenderer;
    Activity mActivity;

    public ClearGLSurfaceView(Context context)
    {
        super(context);
        mActivity = (Activity) context;
        mRenderer = new ClearRenderer(context, mActivity);
        setRenderer(mRenderer);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    public boolean onTouchEvent(final MotionEvent event)
    {
        // queueEvent(new Runnable(){
        // public void run()
        // {
        // }});
        return true;
    }

    public boolean onKeyUp(final int keyCode, KeyEvent msg)
    {
        queueEvent(new Runnable()
        {
            public void run()
            {
                // mRenderer.processKeyEvent(keyCode, KeyEvent.ACTION_UP);
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
                else if (keyCode == KeyEvent.KEYCODE_H)
                {
                    mActivity.finish();
                }
            }
        });
        return true;
    }

    public boolean onKeyDown(final int keyCode, KeyEvent msg)
    {
        queueEvent(new Runnable()
        {
            public void run()
            {
                Log.d("keycode", "::: " + keyCode);
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
                // + button
                else if (keyCode == KeyEvent.KEYCODE_P)
                {
                    mRenderer.zoom(true);
                }
                // - button
                else if (keyCode == KeyEvent.KEYCODE_M)
                {
                    mRenderer.zoom(false);
                }
                else if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                }
            }
        });
        return true;
    }
}

class ClearRenderer implements GLSurfaceView.Renderer
{
    private Speedo mSpeedo;
    private GameSystem gameSystem;
    private Context mContext;
    private Activity mActivity;

    private float mCameraX = 0f;
    private float zoom = 1.0f;
    private float mCameraY = -8f * zoom;
    private float mCameraZ = 12f * zoom;

    public ClearRenderer(Context context, Activity act)
    {
        mContext = context;
        mSpeedo = new Speedo();
        mActivity = act;
        gameSystem = new GameSystem(context);
    }

    public void toggleTextures()
    {
        Intent i = new Intent(mActivity, DialogActivity.class);
        mContext.startActivity(i);
//        gameSystem.toggleTextures();
    }

    public void zoom(boolean in)
    {
        if (in)
            zoom = zoom - 0.1f;
        else
            zoom = zoom + 0.1f;
    }

    public void playerAction()
    {
        gameSystem.playerAction();
    }

    public void stopCharacter(int keyCode)
    {
        gameSystem.mPlayer.moveCharacter(keyCode, true);
    }

    public void moveCharacter(int keyCode)
    {
        gameSystem.mPlayer.moveCharacter(keyCode, false);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 15.0f, 80.0f / 48.0f, 1, 100);
        GLU.gluLookAt(gl, mCameraX, mCameraY, mCameraZ, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

        gl.glEnable(GL10.GL_DEPTH_TEST);

        // alpha blending
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glAlphaFunc(GL10.GL_GREATER, 0.2f);
        gl.glEnable(GL10.GL_ALPHA_TEST);

        gameSystem.init(gl);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h)
    {
        gl.glViewport(0, 0, w, h);
    }

    public void onDrawFrame(GL10 gl)
    {
        mSpeedo.setStartTime();

        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 15.0f, 80.0f / 48.0f, 1, 100);
        GLU.gluLookAt(gl, mCameraX, mCameraY * zoom, mCameraZ * zoom, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);

        gl.glClearColor(0f, 0f, 0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gameSystem.update();
        gameSystem.draw(gl);

        // start drawing the dash board
        gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, -5f, 5f, -3f, 3f);

        gameSystem.drawDash(gl);
        mSpeedo.setEndTime();
        mSpeedo.draw(gl);

    }
}
