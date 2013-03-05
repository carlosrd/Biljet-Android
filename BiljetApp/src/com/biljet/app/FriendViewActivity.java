package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.adapters.ActivitiesHeader;

public class FriendViewActivity extends ActivitiesHeader {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_view);
		
		Bundle bundleDatos = getIntent().getExtras();
		
		createHeaderView(R.drawable.header_back_button,"Amigo: "+bundleDatos.getString("Name"), R.drawable.perfil,false);
		setBackButton();	

		ImageView imagenAmigo = (ImageView)findViewById(R.id.friendView_Avatar);	
		
		imagenAmigo.setImageResource(bundleDatos.getInt("Avatar"));	
		imagenAmigo.setScaleType(ImageView.ScaleType.CENTER);
	
		TextView txtNombre = (TextView)findViewById(R.id.friendView_TxtName);
		txtNombre.setText(bundleDatos.getString("Name"));
			
		TextView txtLugar = (TextView)findViewById(R.id.friendView_TxtCity);
		txtLugar.setText(bundleDatos.getString("City"));	
		
		TextView txtBiografia = (TextView)findViewById(R.id.friendView_TxtBio);
		txtBiografia.setText(bundleDatos.getString("Bio"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_view, menu);
		return true;
	}
}

