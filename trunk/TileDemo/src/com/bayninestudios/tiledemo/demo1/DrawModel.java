package com.bayninestudios.tiledemo.demo1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class DrawModel
{
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mTexBuffer;
    private ShortBuffer mIndexBuffer;
    private boolean hasTexture = false;
    private int vertexCount = 3;
    private int[] mTexture = new int[1];

    public DrawModel(Context context, int xmlFile)
    {
        this(context, xmlFile, 0f, 0f, 0f);
    }

    // TODO: need to refactor these parameters, context and xmlfile can be moved
    // to xrp type
    public DrawModel(Context context, int xmlFile, float tx, float ty, float tz)
    {
        float[] coords = null;
        short[] icoords = null;
        float[] ncoords = null;
        float[] tcoords = null;
        int vertexIndex = 0;
        int texIndex = 0;
        int faceIndex = 0;
        int normalIndex = 0;
        try
        {
            XmlResourceParser xrp = context.getResources().getXml(xmlFile);
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT)
            {
                if (xrp.getEventType() == XmlResourceParser.START_TAG)
                {
                    String s = xrp.getName();
                    if (s.equals("faces"))
                    {
                        int i = xrp.getAttributeIntValue(null, "count", 0);
                        icoords = new short[i * 3];
                        vertexCount = i * 3;
                    }
                    if (s.equals("geometry"))
                    {
                        int i = xrp.getAttributeIntValue(null, "vertexcount", 0);
                        Log.d("coords", Integer.toString(i));
                        coords = new float[i * 3];
                        ncoords = new float[i * 3];
                        tcoords = new float[i * 2];
                    }
                    if (s.equals("position"))
                    {
                        float x = xrp.getAttributeFloatValue(null, "x", 0);
                        float y = xrp.getAttributeFloatValue(null, "y", 0);
                        float z = xrp.getAttributeFloatValue(null, "z", 0);
                        if (coords != null)
                        {
                            coords[vertexIndex++] = x + tx;
                            coords[vertexIndex++] = y + ty;
                            coords[vertexIndex++] = z + tz;
                        }
                    }
                    if (s.equals("normal"))
                    {
                        float x = xrp.getAttributeFloatValue(null, "x", 0);
                        float y = xrp.getAttributeFloatValue(null, "y", 0);
                        float z = xrp.getAttributeFloatValue(null, "z", 0);
                        if (ncoords != null)
                        {
                            // z and y get switched for sketchup meshes
                            ncoords[normalIndex++] = x;
                            ncoords[normalIndex++] = y;
                            ncoords[normalIndex++] = z;
                        }
                    }
                    if (s.equals("texcoord"))
                    {
                        float u = xrp.getAttributeFloatValue(null, "u", 0);
                        float v = xrp.getAttributeFloatValue(null, "v", 0);
                        if (ncoords != null)
                        {
                            tcoords[texIndex++] = u;
                            tcoords[texIndex++] = v;
                        }
                    }
                    if (s.equals("face"))
                    {
                        short v1 = (short) xrp.getAttributeIntValue(null, "v1", 0);
                        short v2 = (short) xrp.getAttributeIntValue(null, "v2", 0);
                        short v3 = (short) xrp.getAttributeIntValue(null, "v3", 0);
                        if (icoords != null)
                        {
                            icoords[faceIndex++] = v1;
                            icoords[faceIndex++] = v2;
                            icoords[faceIndex++] = v3;
                        }
                    }
                }
                else if (xrp.getEventType() == XmlResourceParser.END_TAG)
                {
                    ;
                }
                else if (xrp.getEventType() == XmlResourceParser.TEXT)
                {
                    ;
                }
                xrp.next();
            }
            xrp.close();
        }
        catch (XmlPullParserException xppe)
        {
            Log.e("TAG", "Failure of .getEventType or .next, probably bad file format");
            xppe.toString();
        }
        catch (IOException ioe)
        {
            Log.e("TAG", "Unable to read resource file");
            ioe.printStackTrace();
        }
        mVertexBuffer = makeFloatBuffer(coords);
        mNormalBuffer = makeFloatBuffer(ncoords);
        mTexBuffer = makeFloatBuffer(tcoords);
        mIndexBuffer = makeShortBuffer(icoords);
    }

    private FloatBuffer makeFloatBuffer(float[] arr)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    private ShortBuffer makeShortBuffer(short[] arr)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    public void loadTexture(GL10 gl, Context mContext, int mTex)
    {
        hasTexture = true;
        gl.glGenTextures(1, mTexture, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), mTex);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
    }

    public void draw(GL10 gl)
    {
        if (hasTexture)
        {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
        }
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, vertexCount, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    public void draw(GL10 gl, float x, float y, float z, float rot, float scale)
    {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(rot, 0f, 0f, 1f);
        gl.glScalef(scale, scale, scale);
        this.draw(gl);
        gl.glPopMatrix();
    }

    public void draw(GL10 gl, float x, float y, float z, float rot)
    {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(rot, 0f, 0f, 1f);
        this.draw(gl);
        gl.glPopMatrix();
    }

    public void draw(GL10 gl, float x, float y, float z)
    {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        this.draw(gl);
        gl.glPopMatrix();
    }
}
