package com.biljet.types;

/**
 * 
 * 	+ 1: returnA Coruña                     
	+ 2: returnÁlava                        
	+ 3: returnAlbacete                     
	+ 4: returnAlicante                     
	+ 5: returnAlmería                      
	+ 6: returnAsturias                     
	+ 7: returnÁvila                        
	+ 8: returnBadajoz                      
	+ 9: returnBaleares                     
	+ 10: return Barcelona                    
	+ 11: return Burgos                       
	+ 12: return Cáceres                      
	+ 13: return Cádiz                        
	+ 14: return Cantabria                    
	+ 15: return Castellón                    
	+ 16: return Ceuta                        
	+ 17: return Ciudad Real                  
	+ 18: return Córdoba                      
	+ 19: return Cuenca                       
	+ 20: return Girona                       
	+ 21: return Granada                      
	+ 22: return Guadalajara                  
	+ 23: return Guipúzcoa                    
	+ 24: return Huelva                       
	+ 25: return Huesca                       
	+ 26: return Jaén                         
	+ 27: return La rioja                     
	+ 28: return Las palmas                   
	+ 29: return León                         
	+ 30: return Lleida                       
	+ 31: return Lugo                         
	+ 32: return Madrid                       
	+ 33: return Málaga                       
	+ 34: return Melilla                      
	+ 35: return Murcia                       
	+ 36: return Navarra                      
	+ 37: return Ourense                      
	+ 38: return Palencia                     
	+ 39: return Pontevedra                   
	+ 40: return Salamanca                    
	+ 41: return Santa cruz de tenerife       
	+ 42: return Segovia                      
	+ 43: return Sevilla                      
	+ 44: return Soria                        
	+ 45: return Tarragona                    
	+ 46: return Teruel                       
	+ 47: return Toledo                       
	+ 48: return Valencia                     
	+ 49: return Valladolid                   
	+ 50: return Vizcaya                      
	+ 51: return Zamora                       
	+ 52: return Zaragoza             
	+ 99: return Extranjero                   
 */

public class Province {
	
	public Province() {};
	
	public String toString(int p){
		switch(p){
			case 1: return "A Coruña";
			case 2: return "Álava";                        
			case 3: return "Albacete";                     
			case 4: return "Alicante";                     
			case 5: return "Almería";                      
			case 6: return "Asturias";                     
			case 7: return "Ávila";                        
			case 8: return "Badajoz";                      
			case 9: return "Baleares";                     
			case 10: return "Barcelona";                    
			case 11: return "Burgos";                       
			case 12: return "Cáceres";                      
			case 13: return "Cádiz";                        
			case 14: return "Cantabria";                    
			case 15: return "Castellón";                    
			case 16: return "Ceuta";                        
			case 17: return "Ciudad Real";                  
			case 18: return "Córdoba";                      
			case 19: return "Cuenca";                       
			case 20: return "Girona";                       
			case 21: return "Granada";                      
			case 22: return "Guadalajara";                  
			case 23: return "Guipúzcoa";                    
			case 24: return "Huelva";                       
			case 25: return "Huesca";                       
			case 26: return "Jaén";                         
			case 27: return "La Rioja";                     
			case 28: return "Las Palmas de G.C.";                   
			case 29: return "León";                         
			case 30: return "Lleida";                       
			case 31: return "Lugo";                         
			case 32: return "Madrid";                       
			case 33: return "Málaga";                       
			case 34: return "Melilla";                      
			case 35: return "Murcia";                       
			case 36: return "Navarra";                      
			case 37: return "Ourense";                     
			case 38: return "Palencia";                     
			case 39: return "Pontevedra";                   
			case 40: return "Salamanca";                    
			case 41: return "Sta Cruz de Tenerife";       
			case 42: return "Segovia";                      
			case 43: return "Sevilla";                      
			case 44: return "Soria";                        
			case 45: return "Tarragona";                    
			case 46: return "Teruel";                       
			case 47: return "Toledo";                       
			case 48: return "Valencia";                     
			case 49: return "Valladolid";                   
			case 50: return "Vizcaya";                      
			case 51: return "Zamora";                       
			case 52: return "Zaragoza";             
			case 99: return "Extranjero";                   
			
			default: return "";
		}
	}
	
	public String[] toArrayString(){
		
		String[] provinces = new String[52]; 
		
		provinces[0] = "A Coruña";
		provinces[1] = "Álava";                        
		provinces[2] = "Albacete";                     
		provinces[3] = "Alicante";                     
		provinces[4] = "Almería";                      
		provinces[5] = "Asturias";                     
		provinces[6] = "Ávila";                        
		provinces[7] = "Badajoz";                      
		provinces[8] = "Baleares";                     
		provinces[9] = "Barcelona";                    
		provinces[10] = "Burgos";                       
		provinces[11] = "Cáceres";                      
		provinces[12] = "Cádiz";                        
		provinces[13] = "Cantabria";                    
		provinces[14] = "Castellón";                    
		provinces[15] = "Ceuta";                        
		provinces[16] = "Ciudad Real";                  
		provinces[17] = "Córdoba";                      
		provinces[18] = "Cuenca";                       
		provinces[19] = "Girona";                       
		provinces[20] = "Granada";                      
		provinces[21] = "Guadalajara";                  
		provinces[22] = "Guipúzcoa";                    
		provinces[23] = "Huelva";                       
		provinces[24] = "Huesca";                       
		provinces[25] = "Jaén";                         
		provinces[26] = "La Rioja";                     
		provinces[27] = "Las Palmas de G.C.";                   
		provinces[28] = "León";                         
		provinces[29] = "Lleida";                       
		provinces[30] = "Lugo";                         
		provinces[31] = "Madrid";                       
		provinces[32] = "Málaga";                       
		provinces[33] = "Melilla";                      
		provinces[34] = "Murcia";                       
		provinces[35] = "Navarra";                      
		provinces[36] = "Ourense";                     
		provinces[37] = "Palencia";                     
		provinces[38] = "Pontevedra";                   
		provinces[39] = "Salamanca";                    
		provinces[40] = "Sta Cruz de Tenerife";       
		provinces[41] = "Segovia";                      
		provinces[42] = "Sevilla";                      
		provinces[43] = "Soria";                        
		provinces[44] = "Tarragona";                    
		provinces[45] = "Teruel";                       
		provinces[46] = "Toledo";                       
		provinces[47] = "Valencia";                     
		provinces[48] = "Valladolid";                   
		provinces[49] = "Vizcaya";                      
		provinces[50] = "Zamora";                       
		provinces[51] = "Zaragoza";             
    
		return provinces;
	
		
	}
	
	
}
