package com.biljet.app.menu;

import com.biljet.app.R;
import com.biljet.app.R.drawable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorOpcionesMenu extends ArrayAdapter<OpcionMenu> {
		
		Activity context;
		
		private static OpcionMenu[] opciones =
				new OpcionMenu[]{
				new OpcionMenu("Prox Eventos",android.R.drawable.ic_menu_today),
				new OpcionMenu("Mis Eventos",android.R.drawable.ic_menu_my_calendar),
				new OpcionMenu("Amigos", drawable.amigos),
				new OpcionMenu("Sección 4",android.R.drawable.ic_menu_set_as),
				new OpcionMenu("Sección 5",android.R.drawable.ic_menu_crop)};
		

		public AdaptadorOpcionesMenu(Activity context) {
			super(context, R.layout.menu_item, opciones);
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.menu_item, null);
			
			ImageView icono = (ImageView)item.findViewById(R.id.menuImagen_icono);
			icono.setImageResource(opciones[position].getIcono());
			icono.setScaleType(ImageView.ScaleType.CENTER);
			
			TextView titulo = (TextView)item.findViewById(R.id.menuTitulo_opcion);
			titulo.setText(opciones[position].getTitulo());
			
			return item;

		}
	}