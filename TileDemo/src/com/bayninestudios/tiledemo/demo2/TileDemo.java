package com.bayninestudios.tiledemo.demo2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bayninestudios.tiledemo.demo2.ClearGLSurfaceView;
import com.bayninestudios.tiledemo.demo2.ClearRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;

public class TileDemo extends Activity
{
    private GLSurfaceView mGLView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLView = new ClearGLSurfaceView(this);
        setContentView(mGLView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume()
    {
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
}

class ClearRenderer implements GLSurfaceView.Renderer
{
    private Landscape landscape;
    private float playerX;
    private float playerY;

    private float mCameraX = 0f;
    private float mCameraY = -8f;
    private float mCameraZ = 12f;

    public ClearRenderer(Context context)
    {
        playerX = 50f;
        playerY = 50f;
        landscape = new Landscape(context);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        gl.glLoadIdentity();
        // pretty much hard coded to the Nexus One aspect ratio
        GLU.gluPerspective(gl, 15.0f, 80.0f / 48.0f, 1, 100);
        GLU.gluLookAt(gl, mCameraX, mCameraY, mCameraZ, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h)
    {
        gl.glViewport(0, 0, w, h);
    }

    public void onDrawFrame(GL10 gl)
    {
        gl.glClearColor(0f, 0f, 0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glPushMatrix();
        // move the viewport to where the player is standing
        gl.glTranslatef(-playerX, -playerY, 0);
        // draw the landscape at the player position
        landscape.draw(gl, playerX, playerY);
        gl.glPopMatrix();
    }
}
