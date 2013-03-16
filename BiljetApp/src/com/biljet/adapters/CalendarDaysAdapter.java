package com.biljet.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.biljet.app.R;

public class CalendarDaysAdapter extends ArrayAdapter<String> {
	
	Activity context;
	
	private static String[] dayLabels = new String[]{"Lun","Mar","Mie","Jue","Vie","Sab","Dom"};

	public CalendarDaysAdapter(Activity context) {
		super(context, R.layout.griditem_calendarday, dayLabels);
		this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.griditem_calendarday, null);
		
		TextView title = (TextView)item.findViewById(R.id.calendarView_LabelDays);
		title.setText(dayLabels[position]);
		
		return item;

	}
}
