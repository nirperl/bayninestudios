package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class CombatSystem
{
    public Player player;
    public boolean combatActive = false;
    public long lastUpdate;
    public Enemy enemy;

    public CombatSystem(Player player)
    {
        this.player = player;
        enemy = null;
    }

    public void addEnemy(Enemy newEnemy)
    {
        enemy = newEnemy;
        enemy.inCombat = true;
        player.inCombat = true;
        combatActive = true;
        player.dx = 0f;
        player.dy = 0f;
        Vector3 enemyPos = enemy.position;
        Vector3 enemyFacing = new Vector3(enemyPos.x, enemyPos.y, enemyPos.z);
        enemyFacing.subtract(player.position);
        enemy.setFacing(enemyFacing);
    }

    public void update()
    {
        long curTime = System.currentTimeMillis();
        long timeDif = curTime - lastUpdate;
        float frameRate = timeDif / 1000f;

        if (combatActive)
        {
            if (enemy.curHealth < 1)
            {
                enemy.dead = true;
                this.combatActive = false;
                player.inCombat = false;
            }
            player.actionTimer -= timeDif;
            if (player.actionTimer < 0)
                player.actionTimer = 0;
            enemy.actionTimer -= timeDif;
            if (enemy.actionTimer < 0)
                enemyAttack();
        }
        lastUpdate = curTime;
    }

    public void enemyAttack()
    {
        player.curHealth -= 3;
        if (player.curHealth < 0)
            player.curHealth = 0;
        enemy.actionTimer = enemy.ACTIONINTERVAL;
    }

    public void attack()
    {
        if (player.actionTimer == 0)
        {
            enemy.curHealth -= 10;
            player.actionTimer = player.ACTIONINTERVAL;
        }
    }
    
    public Vector3 getTarget()
    {
        return enemy.position;
    }
}
