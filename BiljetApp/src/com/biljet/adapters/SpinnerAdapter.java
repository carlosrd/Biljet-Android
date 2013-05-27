package com.biljet.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.biljet.app.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
	
	Activity context;

	private String[] options;
	
	public SpinnerAdapter(Activity context, String[] options) {
		super(context, R.layout.spinner_biljet_view, options);
		this.context = context;
		this.options = options;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.spinner_biljet_view, null);
		
		TextView title = (TextView)item.findViewById(R.id.spinner_TextView);
		title.setText(options[position]);
		
		return item;

	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.spinner_dropdown_biljet_view, null);
		
		CheckedTextView title = (CheckedTextView)item.findViewById(R.id.spinner_CheckedTextView);
		title.setText(options[position]);
		
		return item;
		
	}


	
}
