package com.biljet.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.types.Friend;

public class FriendViewActivity extends ActivitiesHeader {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_view);
		
//		Bundle bundleDatos = getIntent().getExtras();
//		
//		createHeaderView(R.drawable.header_back_button,"Amigo: "+bundleDatos.getString("Name"), R.drawable.perfil,false);
//		setBackButton();	
		Friend fr = getIntent().getParcelableExtra("friend");
		
		createHeaderView(R.drawable.header_back_button,"Amigo: "+fr.getName()/*bundleDatos.getString("Name")*/, R.drawable.perfil,false);
		setBackButton();
		
		
		ImageView imagenAmigo = (ImageView)findViewById(R.id.friendView_Avatar);	
//		
//		imagenAmigo.setImageResource(bundleDatos.getInt("Avatar"));	
//		imagenAmigo.setScaleType(ImageView.ScaleType.CENTER);
//	
//		TextView txtNombre = (TextView)findViewById(R.id.friendView_TxtName);
//		txtNombre.setText(bundleDatos.getString("Name"));
//			
//		TextView txtLugar = (TextView)findViewById(R.id.friendView_TxtCity);
//		txtLugar.setText(bundleDatos.getString("City"));	
//		
//		TextView txtBiografia = (TextView)findViewById(R.id.friendView_TxtBio);
//		txtBiografia.setText(bundleDatos.getString("Bio"));
		
		imagenAmigo.setImageResource(fr.getImagePath());		
		imagenAmigo.setScaleType(ImageView.ScaleType.CENTER);
	
		TextView txtName = (TextView)findViewById(R.id.friendView_TxtName);
		txtName.setText(fr.getName());
			
		TextView txtSite = (TextView)findViewById(R.id.friendView_TxtCity);
		txtSite.setText(fr.getCity());	
		
		TextView txtBio = (TextView)findViewById(R.id.friendView_TxtBio);
		txtBio.setText(fr.getBio());	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_view, menu);
		return true;
	}
	
	/**
	 * Actions related to the menu options displayed when you press ��� or Config button on the device
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(FriendViewActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}

}
