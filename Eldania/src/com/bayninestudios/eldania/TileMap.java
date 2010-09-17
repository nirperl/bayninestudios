package com.bayninestudios.eldania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class TileMap
{
    private TileDef[][] tiles;
    Random gen = new Random(System.currentTimeMillis());

    public TileMap()
    {
        tiles = new TileDef[100][100];
        for (int y = 0; y < 100; y++)
        {
            for (int x = 0; x < 100; x++)
            {
                tiles[x][y].look = gen.nextInt(3);
            }
        }
    }

    public TileMap(Context context, int xmlResource)
    {
        tiles = new TileDef[100][100];
        for (int y = 0; y < 100; y++)
        {
            for (int x = 0; x < 100; x++)
            {
                tiles[x][y] = new TileDef();
            }
        }
        try
        {
            XmlResourceParser xrp = context.getResources().getXml(xmlResource);
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT)
            {
                if (xrp.getEventType() == XmlResourceParser.START_TAG)
                {
                    String s = xrp.getName();
                    if (s.equals("tile"))
                    {
                        int x = xrp.getAttributeIntValue(null, "x", 0);
                        int y = xrp.getAttributeIntValue(null, "y", 0);
                        int look = xrp.getAttributeIntValue(null, "look", 0);
                        int passable = xrp.getAttributeIntValue(null, "passable", 0);
                        int interact = xrp.getAttributeIntValue(null, "interact", 0);
                        int wallw = xrp.getAttributeIntValue(null, "wallw", 0);
                        int walln = xrp.getAttributeIntValue(null, "walln", 0);
                        int walle = xrp.getAttributeIntValue(null, "walle", 0);
                        int walls = xrp.getAttributeIntValue(null, "walls", 0);
                        tiles[x][y].look = look;
                        tiles[x][y].passable = passable;
                        tiles[x][y].interact = interact;
                        tiles[x][y].wallw = wallw;
                        tiles[x][y].walln = walln;
                        tiles[x][y].walle = walle;
                        tiles[x][y].walls = walls;
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
    }

    public int getTile(int x, int y)
    {
        return tiles[x][y].look;
    }

    public TileDef getTileD(int x, int y)
    {
        return tiles[x][y];
    }

    public boolean checkPassible(int x, int y)
    {
        if (tiles[x][y].passable == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void addGameObj(GameObject obj)
    {
        int x = (int) obj.position.x;
        int y = (int) obj.position.y;
        if (getTileD(x, y).gameObj == null)
        {
            getTileD(x, y).gameObj = new ArrayList<GameObject>();
        }
        getTileD(x, y).gameObj.add(obj);
    }
}
