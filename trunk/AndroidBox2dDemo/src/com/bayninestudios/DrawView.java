package com.bayninestudios;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View
{
	private PhysicsWorld mWorld;
	// TODO: shouldn't be here, should be in a config file or something
	private float circleX = 250f;
	private float circleY = 800f;
	private float circleR = 150f;

	public DrawView(Context context) {
	    super(context);
	    setFocusable(true); //not yet necessary, but you never know what the future brings
        mWorld = new PhysicsWorld();
        mWorld.createWorld();
        mWorld.createGround();
        mWorld.createGroundCircle(circleX, circleY, circleR);
	}

    @Override protected void onDraw(Canvas canvas)
    {
        Paint mPaint = new Paint();
        mPaint.setColor(0xFFFF7777);

        canvas.drawRect(0f, 700f, 600f, 805f, mPaint);
        canvas.drawCircle(circleX, circleY, circleR, mPaint);
        mPaint.setColor(0xFFCCCCFF);
    	Vec2 vec;
    	int count = mWorld.getCount();
    	for (int loop = 0; loop < count; loop++)
    	{
	    	vec = mWorld.getBodyLoc(loop);
	        canvas.drawCircle(vec.x*10, 400 - (vec.y*10), 20f, mPaint);
    	}
        mWorld.update();
        invalidate();
    }	    

    @Override public boolean onTouchEvent(final MotionEvent event)
    {
    	mWorld.addBall(mWorld.translateScreenX(event.getX()), mWorld.translateScreenY(this.getHeight(),event.getY()));
		return true;
    }
}
