package com.biljet.adaptadores;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.app.R;
import com.biljet.tipos.Evento;

public class AdaptadorProximosEventos extends BaseAdapter{
		
		Activity context;
		protected ArrayList<Evento> eventos;
		
		// GETTERS
		// *************************************************************************
		@Override
		public int getCount() {
			return eventos.size();
		}
		
		@Override
		public Object getItem(int position) {
			return eventos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return eventos.get(position).getId();
		}
		
		// CONSTRUCTOR
		// *************************************************************************
		
		public AdaptadorProximosEventos(Activity context, ArrayList<Evento> itemsEvento) {
			this.context = context;
			eventos = itemsEvento;
		}

		public View getView(int position, View convertView, ViewGroup parent){
		            
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_evento, null);
			
			ImageView icono = (ImageView)item.findViewById(R.id.listEventoImagen);
			icono.setImageResource(eventos.get(position).getImagen());
			
			TextView lblTitulo = (TextView)item.findViewById(R.id.listEventoTxtTitulo);
			lblTitulo.setText(eventos.get(position).getNombre());
			
			TextView lblSubtitulo = (TextView)item.findViewById(R.id.listEventoTxtSubtitulo);
			lblSubtitulo.setText(eventos.get(position).getTipo());
			
			return item;

		}

	}