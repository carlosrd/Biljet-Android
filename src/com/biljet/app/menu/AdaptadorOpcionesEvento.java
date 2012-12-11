package com.biljet.app.menu;

import com.biljet.app.Evento;
import com.biljet.app.ListaEventos;
import com.biljet.app.R;
import com.biljet.app.R.drawable;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorOpcionesEvento extends ArrayAdapter<OpcionEvento> {
		
		Activity context;
        
		private static OpcionEvento[] opciones =
				new OpcionEvento[]{
				new OpcionEvento("Concierto ACDC", drawable.acdc_boton),
				new OpcionEvento("Concierto Jessie J", drawable.jessie_j_boton),
				new OpcionEvento("Evento 3", drawable.amigos),
				new OpcionEvento("Evento 4",android.R.drawable.ic_menu_set_as),
				new OpcionEvento("Evento 5",android.R.drawable.ic_menu_crop)};
		

		public AdaptadorOpcionesEvento(Activity context) {
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