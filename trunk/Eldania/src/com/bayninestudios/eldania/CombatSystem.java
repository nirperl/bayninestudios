package com.bayninestudios.eldania;

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
}
