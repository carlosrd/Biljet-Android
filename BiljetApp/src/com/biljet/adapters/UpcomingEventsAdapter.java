package com.biljet.adapters;
import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.app.R;
import com.biljet.types.Event;

public class UpcomingEventsAdapter extends BaseAdapter{
		
		Activity context;
		protected ArrayList<Event> events;
		
		// GETTERS
		// *************************************************************************
		@Override
		public int getCount() {
			return events.size();
		}
		
		@Override
		public Object getItem(int position) {
			return events.get(position);
		}

		@Override
		public long getItemId(int position) {
			return events.get(position).getId();
		}
		
		// CONSTRUCTOR
		// *************************************************************************
		
		public UpcomingEventsAdapter(Activity context, ArrayList<Event> sampleEvents) {
			this.context = context;
			events = sampleEvents;
		}

		public View getView(int position, View convertView, ViewGroup parent){
		            
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_event, null);
			
			ImageView icono = (ImageView)item.findViewById(R.id.eventList_Image);
			icono.setImageResource(events.get(position).getImage());
			
			TextView lblTitulo = (TextView)item.findViewById(R.id.eventList_TxtTitle);
			lblTitulo.setText(events.get(position).getName());
			
			TextView lblSubtitulo = (TextView)item.findViewById(R.id.eventList_TxtSubtitle);
			lblSubtitulo.setText(events.get(position).getEventType());
			
			return item;

		}

	}
