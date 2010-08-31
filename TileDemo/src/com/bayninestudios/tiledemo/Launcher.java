package com.bayninestudios.tiledemo;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		String[] examples = new String[] {
			"Simple Tiles",
			"Tiles in Perspective"
		};
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;
		switch(position) {
			default:
			case 0:
				intent = new Intent(this, com.bayninestudios.tiledemo.demo1.TileDemo.class);
				startActivity(intent);
				break;
            case 1:
                intent = new Intent(this, com.bayninestudios.tiledemo.demo2.TileDemo.class);
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
