package eventos;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.app.Evento;
import com.biljet.app.ListaEventos;
import com.biljet.app.R;
import com.biljet.app.menu.HeaderActividades;

public class EventoJessieJActivity extends HeaderActividades {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_jessie_j);
        
        ListaEventos lista = new ListaEventos();
        Evento e = lista.getTablaEventos().get(2);
        
        //cabecera(true, android.R.drawable.ic_menu_revert, ProximosEventosActivity.class, e.getNombre(), false, android.R.drawable.ic_menu_search, ProximosEventosActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,e.getNombre(), android.R.drawable.ic_menu_search,false);
		setBotonVolver();
        creaEvento(e.getNombre(), e.getId(), e.getTipo(), e.getLugar(), e.getFecha(), e.getPrecio(), e.getImagen());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_evento_jessie_j, menu);
        return true;
    }
}