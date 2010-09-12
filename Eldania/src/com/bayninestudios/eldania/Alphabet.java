package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Alphabet
{
    private DrawModel character;

    public Alphabet(Context context)
    {
        character = new DrawModel(context, R.xml.tile);
    }
    
    public void loadTexture(GL10 gl, Context context, int tex)
    {
        character.loadTexture(gl, context, tex);
        float[] tcoords2 = new float[5*6*4*2];
        int position = 0;
        float[] baseCoords = {0.0f, .16666f, 0.0f, 0.0f, 0.20f, 0.0f, 0.20f, 0.16666f};
        for (int j = 0; j < 6; j++)
        {
            for (int k = 0; k < 5; k++)
            {
                tcoords2[position++] = baseCoords[0]+(k*.20f);
                tcoords2[position++] = baseCoords[1]+(j*.166666f);

                tcoords2[position++] = baseCoords[2]+(k*.2f);
                tcoords2[position++] = baseCoords[3]+(j*.166666f);

                tcoords2[position++] = baseCoords[4]+(k*.2f);
                tcoords2[position++] = baseCoords[5]+(j*.166666f);

                tcoords2[position++] = baseCoords[6]+(k*.2f);
                tcoords2[position++] = baseCoords[7]+(j*.166666f);
            }
        }
        character.setTexBuffer(Util.makeFloatBuffer(tcoords2));
    }
    
    public void draw(GL10 gl, float x, float y, int chr)
    {
        character.tileDraw(gl, x, y, 0f, chr*8);
    }
}
