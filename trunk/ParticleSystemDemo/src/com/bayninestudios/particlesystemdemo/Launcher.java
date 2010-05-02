package com.bayninestudios.particlesystemdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Launcher extends ListActivity {

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		String[] examples = new String[] {
			"Simple Falling Particle System",
			"Fountain"
		};
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
		switch(position) {
			default:
			case 0: // Loading the Engine
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo2.ParticleSystemDemo.class);
				startActivity(intent);
				break;
			case 1: // Textures and Backgrounds
				intent = new Intent(this, com.bayninestudios.particlesystemdemo.demo3.ParticleSystemDemo3.class);
				startActivity(intent);
				break;
		}
	}
	
}
