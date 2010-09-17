package com.bayninestudios.eldania;

public class GameObject
{
    public Vector3 position;
    public int type;
    
    public GameObject(int type, Vector3 pos)
    {
        this.type = type;
        position = pos;
    }
}
