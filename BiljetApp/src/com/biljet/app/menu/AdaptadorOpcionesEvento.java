package com.biljet.app.menu;

import java.util.ArrayList;

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
        
		private static ArrayList<OpcionEvento> opciones=new ArrayList<OpcionEvento>();
		private ListaEventos lista=new ListaEventos();

		
	public AdaptadorOpcionesEvento(Activity context)
	{
		super(context, R.layout.menu_item, opciones);
		for (int i=1;i<=lista.getTablaEventos().size();i++)
		{
			opciones.add(new OpcionEvento(lista.getTablaEventos().get(i).getNombre(),lista.getTablaEventos().get(i).getImagen()));
		}	
		this.context = context;
	}
		
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.menu_item, null);

		ImageView icono = (ImageView)item.findViewById(R.id.menuImagen_icono);
		icono.setImageResource(opciones.get(position).getIcono());
		icono.setScaleType(ImageView.ScaleType.CENTER);
			
		TextView titulo = (TextView)item.findViewById(R.id.menuTitulo_opcion);
		titulo.setText(opciones.get(position).getTitulo());
			
		return item;

		}
	}