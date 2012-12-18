package amigos;

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

public class ItemAmigoAdapter extends BaseAdapter {
	protected Activity activity;
	protected ArrayList<ItemAmigo> items;
	
	
	public ItemAmigoAdapter(Activity activity, ArrayList<ItemAmigo> items) {
		this.activity = activity;
		this.items = items;
	}


	//@Override
	public int getCount() {
		return items.size();
	}


	//@Override
	public Object getItem(int position) {
		return items.get(position);
	}


	//@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}


	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		
        if(convertView == null) {
        	LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	vi = inflater.inflate(R.layout.list_item_layout, null);//R.layout.list_item_layout        	
        }
            
        ItemAmigo item = items.get(position);
        
        ImageView image = (ImageView) vi.findViewById(R.id.imagen);
        int imageResource = activity.getResources().getIdentifier(item.getRutaImagen(), null, activity.getPackageName());
        image.setImageDrawable(activity.getResources().getDrawable(imageResource));
        
        TextView nombre = (TextView) vi.findViewById(R.id.nombre);
        nombre.setText(item.getNombre());
        
        TextView tipo = (TextView) vi.findViewById(R.id.ciudad);
        tipo.setText(item.getCiudad());

        return vi;
	}
}


