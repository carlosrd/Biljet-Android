package com.biljet.adapters;

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
import com.biljet.types.Friend;

public class FriendsAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Friend> items;
		
	public FriendsAdapter(Activity activity, ArrayList<Friend> items) {
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
		
		View vi = convertView;
	
	    if (convertView == null) {
	       LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	       vi = inflater.inflate(R.layout.listitem_friend, null);//R.layout.list_item_layout
	       }
	            
	    Friend item = items.get(position);
	        
	    ImageView image = (ImageView) vi.findViewById(R.id.friendList_Avatar);	    
	    image.setImageResource(items.get(position).getImagePath());
	    
	    TextView friendName = (TextView) vi.findViewById(R.id.friendList_TxtName);
	    friendName.setText(item.getName());
	        
	    TextView friendCity = (TextView) vi.findViewById(R.id.friendList_TxtCity);
	    friendCity.setText(item.getCity());
	
	    return vi;
	}
}
