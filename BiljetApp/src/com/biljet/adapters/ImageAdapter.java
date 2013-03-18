package com.biljet.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.biljet.app.R;

public class ImageAdapter extends BaseAdapter{
	
	int galleryItemBackground;
	private Context context;
	
	private ArrayList<Integer> imagesID = new ArrayList<Integer>();
	
	public ImageAdapter(Context c){
		context = c;
		TypedArray attr = context.obtainStyledAttributes(R.styleable.GalleryEventsFollow);
		galleryItemBackground = attr.getResourceId(
				R.styleable.GalleryEventsFollow_android_galleryItemBackground, 0);
		attr.recycle();
	}
	
	@Override
	public int getCount() {
		return imagesID.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		
		imageView.setImageResource(imagesID.get(position));
		imageView.setLayoutParams(new Gallery.LayoutParams(150,100));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setBackgroundResource(galleryItemBackground);
		
		return imageView;
	}
	
	public void putImagesID(Integer im){
		imagesID.add(im);
	}
	
}// ImageAdapter
