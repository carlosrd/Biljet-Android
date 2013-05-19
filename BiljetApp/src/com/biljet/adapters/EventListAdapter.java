package com.biljet.adapters;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.app.R;
import com.biljet.types.Event;
import com.biljet.types.Province;

public class EventListAdapter extends BaseAdapter{
		
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
		
		public EventListAdapter(Activity context, ArrayList<Event> sampleEvents) {
			this.context = context;
			events = sampleEvents;
		}

		public View getView(int position, View convertView, ViewGroup parent){
		            
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_event, null);
			
			ImageView imageEventView = (ImageView)item.findViewById(R.id.eventList_Image);
			//imageEvent.setImageResource(events.get(position).getImage());

			File imgFile = new File(events.get(position).getImagePath());
			Log.d("adapter",imgFile.getAbsolutePath());
			
			if(imgFile.exists()){
			    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			    imageEventView.setImageBitmap(myBitmap);
			} else
				imageEventView.setVisibility(View.INVISIBLE); // TODO Imagen por defecto
			
			TextView title = (TextView)item.findViewById(R.id.eventList_TxtTitle);
			title.setText(events.get(position).getTitle());
			
			TextView type = (TextView)item.findViewById(R.id.eventList_TxtType);
			type.setText(events.get(position).getCategory());
			
			TextView date = (TextView)item.findViewById(R.id.eventList_TxtDate);
			long timestamp = events.get(position).getDate();
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM, yyyy");
			String auxDate = dateFormatter.format(new Date(timestamp));
			
			date.setText(auxDate);	
			
			TextView location = (TextView)item.findViewById(R.id.eventList_TxtPlace);
			String site = events.get(position).getAddress()+", ";
			Province p = new Province();
			site += p.toString(events.get(position).getProvince());
			location.setText(site);
			
			return item;

		}

	}
