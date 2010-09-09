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
        Vector3 playerPos = player.position;
        Vector3 enemyFacing = new Vector3(playerPos.x, playerPos.y, playerPos.z);
        enemyFacing.subtract(enemy.position);
        enemyFacing.normalize();
        enemy.setFacing(enemyFacing);
        enemyFacing.multiply(-1f);
        player.setFacing(enemyFacing);
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
            player.actionTimer += timeDif;
            if (player.actionTimer > player.ACTIONINTERVAL)
                player.actionTimer = player.ACTIONINTERVAL;
            enemy.actionTimer += timeDif;
            if (enemy.actionTimer > enemy.ACTIONINTERVAL)
                enemyAttack();
        }
        lastUpdate = curTime;
    }

    public void enemyAttack()
    {
        player.curHealth -= 3;
        if (player.curHealth < 0)
            player.curHealth = 0;
        enemy.actionTimer = 0;
    }

    public void attack()
    {
        if (player.actionTimer == player.ACTIONINTERVAL)
        {
            enemy.curHealth -= 10;
            player.actionTimer = 0;
        }
    }
    
    public Vector3 getTarget()
    {
        return enemy.position;
    }
}
