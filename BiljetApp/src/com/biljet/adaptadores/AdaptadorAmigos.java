package com.biljet.adaptadores;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.app.R;
import com.biljet.tipos.Amigo;

public class AdaptadorAmigos extends BaseAdapter {
	
	protected Activity activity;
	protected ArrayList<Amigo> items;
	
	public AdaptadorAmigos(Activity activity, ArrayList<Amigo> items) {
		this.activity = activity;
		this.items = items;
	}
	
	// GETTERS
	// *************************************************************************
	@Override
	public int getCount() {
		return items.size();
	}
	
	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	// VISTA
	// *************************************************************************
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View vi=convertView;
	
	    if (convertView == null) {
	       LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	       vi = inflater.inflate(R.layout.listitem_amigo, null);//R.layout.list_item_layout
	       }
	            
	    Amigo item = items.get(position);
	        
	    ImageView image = (ImageView) vi.findViewById(R.id.listAmigosAvatar);	    
	    image.setImageResource(items.get(position).getRutaImagen());
	    
	    TextView nombre = (TextView) vi.findViewById(R.id.listAmigosTxtNombre);
	    nombre.setText(item.getNombre());
	        
	    TextView ciudad = (TextView) vi.findViewById(R.id.listAmigosTxtCiudad);
	    ciudad.setText(item.getCiudad());
	
	    return vi;
	}

}









