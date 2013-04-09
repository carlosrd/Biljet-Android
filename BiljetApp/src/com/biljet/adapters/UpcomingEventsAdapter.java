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
			
			ImageView imageEvent = (ImageView)item.findViewById(R.id.eventList_Image);
			imageEvent.setImageResource(events.get(position).getImage());
			
			TextView title = (TextView)item.findViewById(R.id.eventList_TxtTitle);
			title.setText(events.get(position).getName());
			
			TextView type = (TextView)item.findViewById(R.id.eventList_TxtType);
			type.setText(events.get(position).getEventType());
			
			TextView date = (TextView)item.findViewById(R.id.eventList_TxtDate);
			date.setText(events.get(position).getDate().toString());	
			
			TextView location = (TextView)item.findViewById(R.id.eventList_TxtPlace);
			location.setText(events.get(position).getSite());
			
			return item;

		}

	}
