package com.bayninestudios.box2ddemo;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import android.util.Log;

public class PhysicsWorld
{
    final private int MAXBALLS = 50;
    final private float FRAMERATE = 30f;

    private float timeStep = (1f / FRAMERATE);
    private int iterations = 5;

    private Body[] bodies;
    private int count = 0;

    private AABB worldAABB;
    private World world;
    
    public void createWorld()
    {
        // Step 1: Create Physics World Boundaries
        worldAABB = new AABB();
        worldAABB.lowerBound.set(new Vec2(-100f, -100f));
        worldAABB.upperBound.set(new Vec2(100f, 100f));

        // Step 2: Create Physics World with Gravity
        Vec2 gravity = new Vec2(0f, -10f);
        boolean doSleep = true;
        world = new World(worldAABB, gravity, doSleep);
        bodies = new Body[MAXBALLS];
    }

    public void setGrav(float x, float y)
    {
    	world.setGravity(new Vec2(x,y));
    }

    public void createGround()
    {
        BodyDef groundBodyDef;
        PolygonDef groundShapeDef;
        groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vec2(0.0f, -25.0f));
        Body groundBody = world.createBody(groundBodyDef);
        groundShapeDef = new PolygonDef();
        groundShapeDef.setAsBox(50f, 10f);
        groundBody.createShape(groundShapeDef);
    }

    public void createGroundCircle(float x, float y, float r)
    {
        BodyDef groundBodyDef2;
        CircleDef groundShapeDef2;
        groundBodyDef2 = new BodyDef();
        groundBodyDef2.position.set(new Vec2(x,y));
        Body groundBody2 = world.createBody(groundBodyDef2);
        groundShapeDef2 = new CircleDef();
        groundShapeDef2.radius = r;
        groundBody2.createShape(groundShapeDef2);
    }

    /*
     * Add ball should take the box2d x and y co-ords, translated in the view
     */
    public void addBall(float x, float y)
    {
    	if (count < (MAXBALLS-1))
    	{
	        // Create Dynamic Body
	        BodyDef bodyDef = new BodyDef();
	        bodyDef.position.set(x, y);
	        bodies[count] = world.createBody(bodyDef);        	

	        // Create Shape with Properties
	        CircleDef circle = new CircleDef();
	        circle.radius = 1.0f;
	        circle.density = 1.0f;

	        // Assign shape to Body
	        bodies[count].createShape(circle);
	        bodies[count].setMassFromShapes();

	        // Increase Counter
	        count++;
    	}
    }

    public void addBox(float x, float y)
    {
    	if (count < (MAXBALLS-1))
    	{
	        // Create Dynamic Body
	        BodyDef bodyDef = new BodyDef();
	        bodyDef.position.set(x, y);
	        bodies[count] = world.createBody(bodyDef);        	

	        PolygonDef poly = new PolygonDef();
	        poly.setAsBox(1f, 1f);
	        poly.density = 1.0f;

	        // Assign shape to Body
	        bodies[count].createShape(poly);
	        bodies[count].setMassFromShapes();
	        bodies[count].setUserData(new String("0"));

	        // Increase Counter
	        count++;
    	}
    }

    public void addLongBox(float x, float y)
    {
    	if (count < (MAXBALLS-1))
    	{
	        // Create Dynamic Body
	        BodyDef bodyDef = new BodyDef();
	        bodyDef.position.set(x, y);
	        bodies[count] = world.createBody(bodyDef);

	        PolygonDef poly = new PolygonDef();
	        poly.setAsBox(.2f, 2f);
	        poly.density = 1.0f;
	        // using a String of 1 to denote a long box, bit of a hack
	        // in theory you should step into the shape object and grab the x and y
	        poly.userData = new String("1");

	        // Assign shape to Body
	        bodies[count].createShape(poly);
	        bodies[count].setMassFromShapes();

	        // Increase Counter
	        count++;
    	}
    }

    public void update()
    {
        world.step(timeStep, iterations);
    }

    public Vec2 getBodyLoc(int bodyNum)
    {
    	return bodies[bodyNum].getPosition();
    }

    public int getCount()
    {
    	return count;
    }

    public Body getBodyList()
    {
    	return world.getBodyList();
    }
}
