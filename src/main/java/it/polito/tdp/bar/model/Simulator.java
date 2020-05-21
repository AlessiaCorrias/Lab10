package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;
import java.util.concurrent.ThreadLocalRandom;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {

	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue = new PriorityQueue<>();

	// PARAMETRI DI SIMULAZIONE
	private int N10 = 2; // numero tavoli da 10
	private int N8 = 4; // numero tavoli da 8
	private int N6 = 4; // numero tavoli da 6
	private int N4 = 5; // numero tavoli da 4

	private Duration T_IN = Duration.of(10, ChronoUnit.MINUTES); // intervallo tra i clienti

	private final LocalTime oraApertura = LocalTime.of(7, 00);
	private final LocalTime oraChiusura = LocalTime.of(21, 00);

	// MODELLO DEL MONDO
	private int n10; // numero tavoli da 10 disponibili. compreso tra 0 e N10
	private int n8;
	private int n6;
	private int n4;

	// VALORI DA CALCOLARE
	private int clienti;
	private int insoddisfatti;
	private int soddisfatti;

	// METODI PER IMPOSTARE I PARAMETRI
	public void setN10(int N) {
		this.N10 = N;
	}

	public void setN8(int N) {
		this.N8 = N;
	}

	public void setN6(int N) {
		this.N6 = N;
	}

	public void set4(int N) {
		this.N4 = N;
	}

	public void setClientFrequency(Duration d) {
		this.T_IN = d;
	}

	// METODI PER RESTITUIRE I RISULTATI
	public int getClienti() {
		return clienti;
	}

	public int getInsoddisfatti() {
		return insoddisfatti;
	}

	public int getSoddisfatti() {
		return soddisfatti;
	}

	// SIMULAZIONE VERA E PROPRIA

	public void run() {
		// preparazione iniziale (mondo + coda eventi)
		this.n10 = this.N10;
		this.n8 = this.N8;
		this.n6 = this.N6;
		this.n4 = this.N4;

		this.clienti = this.insoddisfatti = this.soddisfatti = 0;

		this.queue.clear();
		LocalTime oraArrivoCliente = this.oraApertura;
		do {
			Event e = new Event(oraArrivoCliente, EventType.ARRIVO_GRUPPO_CLIENTI);
			this.queue.add(e);

			oraArrivoCliente = oraArrivoCliente.plus(this.T_IN);
		} while (oraArrivoCliente.isBefore(this.oraChiusura));

		// esecuzione del ciclo di simulazione
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
//				System.out.println(e);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {

		case ARRIVO_GRUPPO_CLIENTI:

			int num_persone = (int) (Math.random() * 10) + 1;
			Duration durata = calcolaDurata();
			float tolleranza = (float) Math.random();
			// come gestisco probabilità ??? divido in due intervalli (cut a 0.5), ma non ha
			// senso

			switch (num_persone) {

			case 1: // bancone
				if (tolleranza > 0.5) {
					// cliente soddisfatto
					// 1. aggiorno modello del mondo
					// dovrei occupare tavolo ma è solo 1, non ha senso
					// 2. aggiorno risultati
					this.clienti++;
					this.soddisfatti++;
					// 3. genero nuovi eventi
					// dovrei generare evento tavolo liberato
				} else {
					// cliente insoddisfatto
					this.clienti++;
					this.insoddisfatti++;
				}break;

			case 2: // tavolo da 4 o bancone
				// cerco tavolo
				if (n4 > 0) {
					// lo accomodo

					this.n4--;

					this.clienti += 2;
					this.soddisfatti += 2;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO4_LIBERATO);
					this.queue.add(nuovo);

				} else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 2;
					this.soddisfatti += 2;

				} else {
					// insoddisfatto

					this.clienti += 2;
					this.insoddisfatti += 2;
				}break;

			case 3: // tavolo da 4 o da 6 o bancone

				// cerco tavolo
				if (n4 > 0) {
					// lo accomodo

					this.n4--;

					this.clienti += 3;
					this.soddisfatti += 3;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO4_LIBERATO);
					this.queue.add(nuovo);

				} else if (n6 > 0) {

					// lo accomodo

					this.n6--;

					this.clienti += 3;
					this.soddisfatti += 3;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO6_LIBERATO);
					this.queue.add(nuovo);

				} else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 3;
					this.soddisfatti += 3;

				} else {
					// insoddisfatto

					this.clienti += 3;
					this.insoddisfatti += 3;
				}break;

			case 4: // tavolo da 4 o 6 o 8 o bancone
				
				// cerco tavolo
				if (n4 > 0) {
					// lo accomodo

					this.n4--;

					this.clienti += 4;
					this.soddisfatti += 4;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO4_LIBERATO);
					this.queue.add(nuovo);
					
				} else if (n6 > 0) {
					
					// lo accomodo

					this.n6--;

					this.clienti += 4;
					this.soddisfatti += 4;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO6_LIBERATO);
					this.queue.add(nuovo);

				} else if (n8 > 0) {
					// lo accomodo

					this.n8--;

					this.clienti += 4;
					this.soddisfatti += 4;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO8_LIBERATO);
					this.queue.add(nuovo);
				}
				
				else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 4;
					this.soddisfatti += 4;

				} else {
					// insoddisfatto

					this.clienti+=4;
					this.insoddisfatti+=4;
				}break;

			case 5: // tavolo da 6 o 8 o 10 o bancone
				// cerco tavolo
				if (n6 > 0) {
					// lo accomodo

					this.n6--;

					this.clienti += 5;
					this.soddisfatti += 5;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO6_LIBERATO);
					this.queue.add(nuovo);
					
				} else if (n8 > 0) {
					
					// lo accomodo

					this.n8--;

					this.clienti += 5;
					this.soddisfatti += 5;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO8_LIBERATO);
					this.queue.add(nuovo);

				} else if (n10 > 0) {
					// lo accomodo

					this.n10--;

					this.clienti += 5;
					this.soddisfatti += 5;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO10_LIBERATO);
					this.queue.add(nuovo);
				}
				
				else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 5;
					this.soddisfatti += 5;

				} else {
					// insoddisfatto

					this.clienti+=5;
					this.insoddisfatti+=5;
				}break;

			case 6: // tavolo da 6 o 8 o 10 o bancone
				// cerco tavolo
				if (n6 > 0) {
					// lo accomodo

					this.n6--;

					this.clienti += 6;
					this.soddisfatti += 6;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO6_LIBERATO);
					this.queue.add(nuovo);
					
				} else if (n8 > 0) {
					
					// lo accomodo

					this.n8--;

					this.clienti += 6;
					this.soddisfatti += 6;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO8_LIBERATO);
					this.queue.add(nuovo);

				} else if (n10 > 0) {
					// lo accomodo

					this.n10--;

					this.clienti += 6;
					this.soddisfatti += 6;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO10_LIBERATO);
					this.queue.add(nuovo);
				}
				
				else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 6;
					this.soddisfatti += 6;

				} else {
					// insoddisfatto

					this.clienti+=6;
					this.insoddisfatti+=6;
				}break;

			case 7: // tavolo da 8 o 10 o bancone
				// cerco tavolo
				if (n8 > 0) {
					
					// lo accomodo

					this.n8--;

					this.clienti += 7;
					this.soddisfatti += 7;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO8_LIBERATO);
					this.queue.add(nuovo);

				} else if (n10 > 0) {
					// lo accomodo

					this.n10--;

					this.clienti += 7;
					this.soddisfatti += 7;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO10_LIBERATO);
					this.queue.add(nuovo);
				}
				
				else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 7;
					this.soddisfatti += 7;

				} else {
					// insoddisfatto

					this.clienti+=7;
					this.insoddisfatti+=7;
				}break;

			case 8: // tavolo da 8 o 10 o bancone
				// cerco tavolo
				if (n8 > 0) {
					
					// lo accomodo

					this.n8--;

					this.clienti += 8;
					this.soddisfatti += 8;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO8_LIBERATO);
					this.queue.add(nuovo);

				} else if (n10 > 0) {
					// lo accomodo

					this.n10--;

					this.clienti += 8;
					this.soddisfatti += 8;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO10_LIBERATO);
					this.queue.add(nuovo);
				}
				
				else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 8;
					this.soddisfatti += 8;

				} else {
					// insoddisfatto

					this.clienti+=8;
					this.insoddisfatti+=8;
				}break;

			case 9: // tavolo da 10 o bancone
				
				// cerco tavolo
				if (n10 > 0) {
					// lo accomodo

					this.n10--;

					this.clienti += 9;
					this.soddisfatti += 9;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO10_LIBERATO);
					this.queue.add(nuovo);
				}
				
				else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 9;
					this.soddisfatti += 9;

				} else {
					// insoddisfatto

					this.clienti+=9;
					this.insoddisfatti+=9;
				}break;

			case 10: // tavolo da 10 o bancone
				// cerco tavolo
				if (n10 > 0) {
					// lo accomodo

					this.n10--;

					this.clienti += 10;
					this.soddisfatti += 10;

					Event nuovo = new Event(e.getTime().plus(durata), EventType.TAVOLO10_LIBERATO);
					this.queue.add(nuovo);
				}
				
				else if (tolleranza > 0.5) {
					// bancone

					this.clienti += 10;
					this.soddisfatti += 10;

				} else {
					// insoddisfatto

					this.clienti+=10;
					this.insoddisfatti+=10;
				}break;


			}

			break;

		case TAVOLO4_LIBERATO:
			n4++;
			break;
			
		case TAVOLO6_LIBERATO:
			n6++;
			break;
			
		case TAVOLO8_LIBERATO:
			n8++;
			break;
			
		case TAVOLO10_LIBERATO:
			n10++;
			break;
		}

	}

	private Duration calcolaDurata() {
		Duration d;

		int num = ThreadLocalRandom.current().nextInt(60, 120 + 1);

		d = Duration.of(num, ChronoUnit.MINUTES);

		return d;
	}

}
