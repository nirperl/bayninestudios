package com.bayninestudios.particlesystemdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Launcher extends ListActivity {

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		String[] examples = new String[] {
			"Simple Falling Particle System",
			"Fountain 1 - Basic",
			"Fountain 2 - Time to Live, Color, Sideways",
			"Fountain 3 - Same as 3 but less time to live",
			"Fountain 4 - Slowly add particles and bounce",
			"Pyro 1 - Slower gravity and air resistance",
			"Pyro 2 - Touch for pyro!"
		};
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
		switch(position) {
			default:
			case 0:
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo2.ParticleSystemDemo.class);
				startActivity(intent);
				break;
			case 1:
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo3.ParticleSystemDemo3.class);
				startActivity(intent);
				break;
			case 2:
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo4a.ParticleSystemDemo.class);
				startActivity(intent);
				break;
			case 3:
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo4b.ParticleSystemDemo.class);
				startActivity(intent);
				break;
			case 4:
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo5.ParticleSystemDemo.class);
				startActivity(intent);
				break;
			case 5:
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo6.ParticleSystemDemo.class);
				startActivity(intent);
				break;
			case 6:
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo7.ParticleSystemDemo.class);
				startActivity(intent);
				break;
		}
	}
	
    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 1, 0, "Visit bayninestudios.com");
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        	case 1:
        		Intent myIntent = new Intent(Intent.ACTION_VIEW,
        				android.net.Uri.parse("http://www.bayninestudios.com"));
        		startActivity(myIntent);
        		return true;
        }
        return false;
    }
}
