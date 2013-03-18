package com.biljet.adapters;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PicAdapter {
	
	// Atributes
	
	
	int defaultItemBackground;	//use the default gallery background image
	
	private Context galleryContext;	//gallery context
	
	private Bitmap[] imageBitmaps;	//array to store bitmaps to display
	
	Bitmap placeholder;		//placeholder bitmap for empty spaces in gallery
	
	// Constructor
	public PicAdapter(Context c) {
	    //instantiate context
	    galleryContext = c;
	    //create bitmap array
	    imageBitmaps  = new Bitmap[10];
	    //decode the placeholder image
	    placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	    //more processing
	}
}
