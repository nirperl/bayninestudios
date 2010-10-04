package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class Alphabet
{
    private DrawModel character;

    public Alphabet(Context context)
    {
        character = new DrawModel(context, R.xml.tile);
    }
    
    public void loadTexture(GL10 gl, Context context, int tex)
    {
        int columnCount = 16;
        int rowCount = 16;

        character.loadTexture(gl, context, tex);
        float[] tcoords2 = new float[columnCount*rowCount*4*2];
        int position = 0;
        float xinc = 1.0f/columnCount;
        float yinc = 1.0f/rowCount;
        float[] baseCoords = {0.0f, yinc, 0.0f, 0.0f, xinc, 0.0f, xinc, yinc};
        for (int j = 0; j < rowCount; j++)
        {
            for (int k = 0; k < columnCount; k++)
            {
                tcoords2[position++] = baseCoords[0]+(k*xinc);
                tcoords2[position++] = baseCoords[1]+(j*yinc);

                tcoords2[position++] = baseCoords[2]+(k*xinc);
                tcoords2[position++] = baseCoords[3]+(j*yinc);

                tcoords2[position++] = baseCoords[4]+(k*xinc);
                tcoords2[position++] = baseCoords[5]+(j*yinc);

                tcoords2[position++] = baseCoords[6]+(k*xinc);
                tcoords2[position++] = baseCoords[7]+(j*yinc);
            }
        }
        character.setTexBuffer(Util.makeFloatBuffer(tcoords2));
    }
    
    public void draw(GL10 gl, float x, float y, int chr, Vector3 scale)
    {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0f);
        gl.glScalef(scale.x, scale.y, scale.z);
        character.texturePosition(chr);
        character.draw(gl);
        gl.glPopMatrix();
    }

    public void draw(GL10 gl, float x, float y, String chr)
    {
        float textScale = 0.30f;
        float spacing = 0.25f;

        for (int loop = 0; loop < chr.length(); loop++)
        {
            int chtr = chr.charAt(loop);
            if (chtr == 10)
            {
                y -= .30f;
                x = -4f;
            }
            else
            {
                gl.glPushMatrix();
                gl.glTranslatef(x+=spacing, y, 0f);
                gl.glScalef(textScale, textScale, 1f);
                character.texturePosition(chtr);
                character.draw(gl);
                gl.glPopMatrix();
            }
        }
    }
}
