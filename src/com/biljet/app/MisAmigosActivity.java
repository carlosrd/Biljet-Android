package com.biljet.app;


import java.util.ArrayList;

import amigos.ItemAmigo;
import amigos.ItemAmigoAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.biljet.app.R;

public class MisAmigosActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_amigos);
        
        ListView lv = (ListView)findViewById(R.id.listView);       
        ArrayList<ItemAmigo> itemsAmigo = obtenerItems();        
        ItemAmigoAdapter adapter = new ItemAmigoAdapter(this, itemsAmigo);

        lv.setAdapter(adapter);
        
        /*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	 // @Override
        	  public void onItemClick(AdapterView parent, View view, int position, long id) {
        	    Intent intent = new Intent(ListViewSampleActivity.this, NombreNuevaActivity.class);
        	    // Aquí se le puede pasar información al intent como el id del elemento o la posición con 
        	    // los métodos putExtra
        	    startActivity(intent);
        	  }
        	});
         */
    }  
    
    private ArrayList<ItemAmigo> obtenerItems() { 
    	ArrayList<ItemAmigo> items = new ArrayList<ItemAmigo>();    	
    	items.add(new ItemAmigo(1, "Alan Turing", "Londres", "drawable/usr_alan"));     	
    	items.add(new ItemAmigo(2, "Albert Einstein", "Ulm", "drawable/usr_albert"));    	
    	items.add(new ItemAmigo(3, "Bill Gates", "Seattle", "drawable/usr_bill"));    	
    	items.add(new ItemAmigo(4, "Gordon Earl Moore", "San Francisco", "drawable/usr_gordon"));
    	items.add(new ItemAmigo(5, "Frank Gray", "Alpine", "drawable/usr_gray"));    	    	    	
    	items.add(new ItemAmigo(6, "Isaac Newton", "Londres", "drawable/usr_isaac"));    	
    	items.add(new ItemAmigo(7, "Linus B. Torvalds", "Helsinki", "drawable/usr_linus"));      	
    	items.add(new ItemAmigo(8, "Richard Stallman", "Manhattan", "drawable/usr_richard"));    	
    	items.add(new ItemAmigo(9, "Sergey Brin", "Moscú", "drawable/usr_sergey"));    	
    	items.add(new ItemAmigo(10, "Steve Jobs", "San Francisco", "drawable/usr_steve"));
    	items.add(new ItemAmigo(11, "Thomas John Watson", "Campbell", "drawable/usr_thomas"));    	
    	items.add(new ItemAmigo(12, "Tim Berners-Lee", "Londres", "drawable/usr_tim"));  
    	
    	return items;
    }    
}