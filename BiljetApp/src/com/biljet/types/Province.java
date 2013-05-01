package com.biljet.types;

/**
 * 
 * 	+ 1: returnA Coru�a                     
	+ 2: return�lava                        
	+ 3: returnAlbacete                     
	+ 4: returnAlicante                     
	+ 5: returnAlmer�a                      
	+ 6: returnAsturias                     
	+ 7: return�vila                        
	+ 8: returnBadajoz                      
	+ 9: returnBaleares                     
	+ 10: return Barcelona                    
	+ 11: return Burgos                       
	+ 12: return C�ceres                      
	+ 13: return C�diz                        
	+ 14: return Cantabria                    
	+ 15: return Castell�n                    
	+ 16: return Ceuta                        
	+ 17: return Ciudad Real                  
	+ 18: return C�rdoba                      
	+ 19: return Cuenca                       
	+ 20: return Girona                       
	+ 21: return Granada                      
	+ 22: return Guadalajara                  
	+ 23: return Guip�zcoa                    
	+ 24: return Huelva                       
	+ 25: return Huesca                       
	+ 26: return Ja�n                         
	+ 27: return La rioja                     
	+ 28: return Las palmas                   
	+ 29: return Le�n                         
	+ 30: return Lleida                       
	+ 31: return Lugo                         
	+ 32: return Madrid                       
	+ 33: return M�laga                       
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
			case 1: return "A Coru�a";
			case 2: return "�lava";                        
			case 3: return "Albacete";                     
			case 4: return "Alicante";                     
			case 5: return "Almer�a";                      
			case 6: return "Asturias";                     
			case 7: return "�vila";                        
			case 8: return "Badajoz";                      
			case 9: return "Baleares";                     
			case 10: return "Barcelona";                    
			case 11: return "Burgos";                       
			case 12: return "C�ceres";                      
			case 13: return "C�diz";                        
			case 14: return "Cantabria";                    
			case 15: return "Castell�n";                    
			case 16: return "Ceuta";                        
			case 17: return "Ciudad Real";                  
			case 18: return "C�rdoba";                      
			case 19: return "Cuenca";                       
			case 20: return "Girona";                       
			case 21: return "Granada";                      
			case 22: return "Guadalajara";                  
			case 23: return "Guip�zcoa";                    
			case 24: return "Huelva";                       
			case 25: return "Huesca";                       
			case 26: return "Ja�n";                         
			case 27: return "La Rioja";                     
			case 28: return "Las Palmas de G.C.";                   
			case 29: return "Le�n";                         
			case 30: return "Lleida";                       
			case 31: return "Lugo";                         
			case 32: return "Madrid";                       
			case 33: return "M�laga";                       
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
		
		provinces[0] = "A Coru�a";
		provinces[1] = "�lava";                        
		provinces[2] = "Albacete";                     
		provinces[3] = "Alicante";                     
		provinces[4] = "Almer�a";                      
		provinces[5] = "Asturias";                     
		provinces[6] = "�vila";                        
		provinces[7] = "Badajoz";                      
		provinces[8] = "Baleares";                     
		provinces[9] = "Barcelona";                    
		provinces[10] = "Burgos";                       
		provinces[11] = "C�ceres";                      
		provinces[12] = "C�diz";                        
		provinces[13] = "Cantabria";                    
		provinces[14] = "Castell�n";                    
		provinces[15] = "Ceuta";                        
		provinces[16] = "Ciudad Real";                  
		provinces[17] = "C�rdoba";                      
		provinces[18] = "Cuenca";                       
		provinces[19] = "Girona";                       
		provinces[20] = "Granada";                      
		provinces[21] = "Guadalajara";                  
		provinces[22] = "Guip�zcoa";                    
		provinces[23] = "Huelva";                       
		provinces[24] = "Huesca";                       
		provinces[25] = "Ja�n";                         
		provinces[26] = "La Rioja";                     
		provinces[27] = "Las Palmas de G.C.";                   
		provinces[28] = "Le�n";                         
		provinces[29] = "Lleida";                       
		provinces[30] = "Lugo";                         
		provinces[31] = "Madrid";                       
		provinces[32] = "M�laga";                       
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
