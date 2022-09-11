package it.polito.tdp.PremierLeague.model;

public class DifferenzaPunti implements Comparable<DifferenzaPunti>{

	private Team team;
	private int puntiDiDifferenza;
	
	public DifferenzaPunti(Team team, int puntiDiDifferenza) {
		super();
		this.team = team;
		this.puntiDiDifferenza = puntiDiDifferenza;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getPuntiDiDifferenza() {
		return puntiDiDifferenza;
	}

	public void setPuntiDiDifferenza(int puntiDiDifferenza) {
		this.puntiDiDifferenza = puntiDiDifferenza;
	}

	@Override
	public int compareTo(DifferenzaPunti other) {
		return this.getPuntiDiDifferenza() - other.getPuntiDiDifferenza();
	}
	
}
