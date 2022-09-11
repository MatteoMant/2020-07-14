package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	private List<Match> partite;
	private Map<Integer, Team> idMapTeam;
	private Map<Team, Integer> classifica;
	
	public Model() {
		dao = new PremierLeagueDAO();
		partite = new LinkedList<Match>();
		classifica = new HashMap<>();
		idMapTeam = new HashMap<>();
	}
	
	public void creaGrafo() {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, this.dao.listAllTeams(idMapTeam));
		
		partite = this.dao.listAllMatches();
		
		// creo una classifica in cui inizialmente tutte le squadre hanno 0 punti
		for (Team t : this.grafo.vertexSet()) {
			classifica.put(t, 0);
		}
		
		// in base al punteggio di ogni partita aggiorno la classifica
		for (Match m : partite) {
			Team locale = idMapTeam.get(m.getTeamHomeID());
			Team ospite = idMapTeam.get(m.getTeamAwayID());
			int risultato = m.getResultOfTeamHome();
			if (risultato == 0) {
				classifica.put(locale, classifica.get(locale)+1);
				classifica.put(ospite, classifica.get(ospite)+1);
			} else if (risultato == 1) {
				classifica.put(locale, classifica.get(locale)+3);
			} else if (risultato == -1) {
				classifica.put(ospite, classifica.get(ospite)+3);
			}	
		}
		
		// Visualizzazione della classifica
		System.out.println("Classifica PremierLeague:\n");
		for (Team t : classifica.keySet()) {
			System.out.println(t + ": " + classifica.get(t) + " punti\n");
		}
		
		// Aggiunta degli archi
		for (Team t1 : this.grafo.vertexSet()) {
			for (Team t2 : this.grafo.vertexSet()) {
				if (classifica.get(t1) > classifica.get(t2)) {
					Graphs.addEdge(this.grafo, t1, t2, (classifica.get(t1) - classifica.get(t2)));
				} else if (classifica.get(t1) < classifica.get(t2)) {
					Graphs.addEdge(this.grafo, t2, t1, (classifica.get(t2) - classifica.get(t1)));
				} else if (classifica.get(t1) == classifica.get(t2)) {
					// non faccio niente
				}
			}
		}
	}
	
	public List<DifferenzaPunti> calcolaSquadreMigliori(Team team){
		List<DifferenzaPunti> result = new LinkedList<>();
		int puntiClassifica = classifica.get(team);
		
		for (Team t : classifica.keySet()) {
			if (classifica.get(t) > puntiClassifica) {
				int differenza = classifica.get(t) - puntiClassifica;
				result.add(new DifferenzaPunti(t, differenza));
			}
		}
		return result;
	}
	
	public List<DifferenzaPunti> calcolaSquadrePeggiori(Team team){
		List<DifferenzaPunti> result = new LinkedList<>();
		int puntiClassifica = classifica.get(team);
		
		for (Team t : classifica.keySet()) {
			if (classifica.get(t) < puntiClassifica) {
				int differenza = puntiClassifica - classifica.get(t);
				result.add(new DifferenzaPunti(t, differenza));
			}
		}
		return result;
	}
	
	public Set<Team> getAllTeams(){
		return this.grafo.vertexSet();
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
}
