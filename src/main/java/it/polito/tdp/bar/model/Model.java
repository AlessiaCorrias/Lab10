package it.polito.tdp.bar.model;

public class Model {
	
	Simulator sim;
	
	
	
	public Model() {
		sim = new Simulator ();
	}



	public String doSimulazione() {
		
		String s = "";
		
		
		
		sim.run();
		
		s+=String.format("Sono arrivati %d clienti, %d sono stati soddisfatti e %d non sono stati soddisfatti", 
				sim.getClienti(), sim.getSoddisfatti(), sim.getInsoddisfatti());
		
		return s;
	}

}
