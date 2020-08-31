package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	ArtsmiaDAO dao ;
	Graph<Integer, DefaultWeightedEdge> grafo;
	List <Integer> vertici ;
	List<Adiacenza> archi;
	
	public Model () {
		this.dao = new ArtsmiaDAO();
		this.grafo = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertici = new ArrayList<Integer>();
		this.archi = new ArrayList<Adiacenza>();
		
	}
	
	public void creaGrafo (String ruolo) {
		
		this.vertici = dao.getArtisti(ruolo);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		this.archi = dao.getAdiacenza();
		
		for (Adiacenza a : archi) {
			if (a.getA1() != null && a.getA2() != null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		
	}
	
	public List <String> getRuoli() {
		return dao.getRuoli();
	}
	
	public int nVertici () {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getConnessi () {
		List <Adiacenza> result = new ArrayList<Adiacenza>();
		
		for (Adiacenza a : archi) {
			result.add(a);
		}
		
		return result;
	}
}
