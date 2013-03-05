package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.adapters.ActivitiesHeader;

public class NewEventActivity extends ActivitiesHeader {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        
        //cabecera(true, android.R.drawable.ic_menu_revert, ProximosEventosActivity.class, "Nuevo Evento", false, R.drawable.amigo, MisEventosActivity.class);
        createHeaderView(R.drawable.header_back_button,"Nuevo Evento", R.drawable.amigo,false);
		setBackButton();
		setRightButtonAction(MyEventsActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event, menu);
        return true;
    }
}
