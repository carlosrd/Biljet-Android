package com.biljet.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.app.R;
import com.biljet.types.MenuOption;

public class MenuOptionsAdapter extends ArrayAdapter<MenuOption> {
		
		Activity context;
		
		private static MenuOption[] options =
				new MenuOption[]{
				new MenuOption("Prox Eventos",R.drawable.prox_eventos),	// android.R.drawable.ic_menu_today
				new MenuOption("Mis Eventos",R.drawable.mis_eventos), // android.R.drawable.ic_menu_agenda
				new MenuOption("Amigos", R.drawable.amigos),
				new MenuOption("Calendario",R.drawable.calendario)//, // android.R.drawable.ic_menu_my_calendar
				/*new MenuOption("Sección 5",android.R.drawable.ic_menu_crop)*/};
		

		public MenuOptionsAdapter(Activity context) {
			super(context, R.layout.menuitem_option, options);
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.menuitem_option, null);
			
			ImageView icon = (ImageView)item.findViewById(R.id.menuItem_Icon);
			icon.setImageResource(options[position].getIcon());
			icon.setScaleType(ImageView.ScaleType.CENTER);
			
			TextView title = (TextView)item.findViewById(R.id.menuItem_Title);
			title.setText(options[position].getTitle());
			
			return item;

		}
	}