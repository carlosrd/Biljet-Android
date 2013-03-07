package com.biljet.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.types.Date;
import com.biljet.types.Event;

public class NewEventActivity extends ActivitiesHeader {
	
	private Calendar dateTime = Calendar.getInstance();    
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a"); 
    private static final int DIALOG_DATE = 1;
    private static final int DIALOG_TIME = 2;
	protected static final int RESULT_LOAD_IMAGE = 1;	
    private Button buttonDatePicker;
    private Button buttonTimePicker;    
    private EditText editTextDate;
    private EditText editTextTime;
    int postImage = 0;
    
    final int [] arrayIMG = {R.drawable.logo_evento,R.drawable.android1,R.drawable.android2,R.drawable.android3};	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        
        //cabecera(true, android.R.drawable.ic_menu_revert, ProximosEventosActivity.class, "Nuevo Evento", false, R.drawable.amigo, MisEventosActivity.class);
        createHeaderView(R.drawable.header_back_button,"Nuevo Evento", R.drawable.amigo,false);
		setBackButton();
		setRightButtonAction(MyEventsActivity.class);
		

		buttonDatePicker = (Button) findViewById(R.id.newEvent_ButtonSetDate);			
		editTextDate = (EditText) findViewById(R.id.newEvent_EditDate);			
		editTextDate.setText(dateFormatter.format(dateTime.getTime()));	
		
		buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });
 
		buttonTimePicker = (Button) findViewById(R.id.newEvent_ButtonSetTime);
		editTextTime = (EditText) findViewById(R.id.newEvent_EditTime);
		
		editTextTime.setText(timeFormatter.format(dateTime.getTime()));
		buttonTimePicker.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });	
		
		
		
		Button buttonChangeImage = (Button) findViewById(R.id.newEvent_ButtonChangeImage);
		buttonChangeImage.setOnClickListener(new View.OnClickListener() {             
            public void onClick(View arg0) {
                 
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });
        
		addListenerChangeImage();
    }

	 public void addListenerChangeImage() {				
			
			final ImageView image = (ImageView) findViewById(R.id.newEvent_Image);
	 
			Button button = (Button) findViewById(R.id.newEvent_ButtonChangeImage);
			button.setOnClickListener(new OnClickListener() {
				
				public void onClick(View arg0) {
					postImage=(postImage+1)%(arrayIMG.length);
					image.setImageResource(arrayIMG[postImage]);
				}
			});
	}
	 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event, menu);
        return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_DATE:
            return new DatePickerDialog(this, new OnDateSetListener() {
 
                
                public void onDateSet(DatePicker view, int year,
                        int monthOfYear, int dayOfMonth) {
                    dateTime.set(year, monthOfYear, dayOfMonth);
                    editTextDate.setText(dateFormatter
                            .format(dateTime.getTime()));
                }
            }, dateTime.get(Calendar.YEAR),
               dateTime.get(Calendar.MONTH),
               dateTime.get(Calendar.DAY_OF_MONTH));
 
        case DIALOG_TIME:
            return new TimePickerDialog(this, new OnTimeSetListener() {	 

                public void onTimeSet(TimePicker view, int hourOfDay,
                        int minute) {
                    dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    dateTime.set(Calendar.MINUTE, minute);
                    editTextTime.setText(timeFormatter
                            .format(dateTime.getTime()));
 
                }
            }, dateTime.get(Calendar.HOUR_OF_DAY),
               dateTime.get(Calendar.MINUTE), false);
        }
        return null;
    }
    

    /** Called when the user clicks the createNewEvent button */
    @SuppressLint("NewApi")
	public void createNewEvent(View view) {
    	       
    	Intent intent = new Intent(NewEventActivity.this, EventViewActivity.class);
		
		Bundle dataBundle = new Bundle();	
		
		 EditText editTxtName = (EditText) findViewById(R.id.newEvent_EditName);
		 EditText editTxtType = (EditText) findViewById(R.id.newEvent_EditEventType);
		 EditText editTxtSite = (EditText) findViewById(R.id.newEvent_EditSite);
		 EditText editTxtDate = (EditText) findViewById(R.id.newEvent_EditDate);
		 EditText editTxtTime = (EditText) findViewById(R.id.newEvent_EditTime);
		 EditText editTxtPrice = (EditText) findViewById(R.id.newEvent_EditPrice);
		 EditText editTxtConfirmedPeople = (EditText) findViewById(R.id.newEvent_EditConfirmedPeople);
		 EditText editTxtCapacity = (EditText) findViewById(R.id.newEvent_EditCapacity);
		 EditText editTxtInfo = (EditText) findViewById(R.id.newEvent_EditInfo);
		 
		 		
		if(!editTxtName.getText().toString().isEmpty()	&&
		   !editTxtType.getText().toString().isEmpty()	&&
		   !editTxtSite.getText().toString().isEmpty()	&&			   
		   !editTxtPrice.getText().toString().isEmpty()	&&
		   !editTxtConfirmedPeople.getText().toString().isEmpty()	&&
		   !editTxtCapacity.getText().toString().isEmpty()	&&
		   !editTxtInfo.getText().toString().isEmpty()				   
		   																	){	
			
			
			   dataBundle.putInt("IMAGE-URL", arrayIMG[postImage]);
			   dataBundle.putString("NANEM",editTxtName.getText().toString());
			   dataBundle.putString("EVENT_TYPE", editTxtType.getText().toString());	
			   dataBundle.putString("SITE", editTxtSite.getText().toString());				   
			   /*
			   dataBundle.putString("FECHA", editTxtDate.getText().toString());
			   dataBundle.putString("HORA", editTxtTime.getText().toString());
			   */
			   dataBundle.putInt("PRICE", Integer.parseInt(editTxtPrice.getText().toString()));	
			   dataBundle.putInt("CONFIRMED_PEOPLE", Integer.parseInt(editTxtConfirmedPeople.getText().toString()));
			   dataBundle.putInt("CAPACITY", Integer.parseInt(editTxtCapacity.getText().toString()));
			   dataBundle.putString("INFO", editTxtInfo.getText().toString());
			   
			   intent.putExtras(dataBundle);				
			   startActivity(intent);
			   finish();
		
		}
		else{
			alertRequiredField();
		}
			
    }
    
    /** Called when the user clicks the cancelNewEvent button */
    public void alertCancelNewEvent(View view) {	 
    	
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
			// set title
			alertDialogBuilder.setTitle("Alerta!!!");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Estas seguro que quieres cancelar el evento "+((EditText) findViewById(R.id.newEvent_EditName)).getText().toString()+"?")
				.setCancelable(false)
				.setPositiveButton("Si",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						NewEventActivity.this.finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show alert
				alertDialog.show();
		}	    
   
    
   public void alertRequiredField(){
	   AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Es obligatorio rellenar todos los campos");
        dlgAlert.setTitle("Atención!!!");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
   }
}
