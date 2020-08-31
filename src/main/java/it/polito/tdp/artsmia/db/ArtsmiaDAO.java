package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mariadb.jdbc.internal.com.read.dao.Results;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <String> getRuoli () {
		String sql = "SELECT DISTINCT role " + 
				"FROM authorship " + 
				"ORDER BY role ASC ";
		
		List <String> result = new ArrayList<String>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(res.getString("role"));
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Integer> getArtisti (String ruolo) {
		String sql = "SELECT DISTINCT artist_id " + 
				"FROM authorship " + 
				"WHERE role = ? ";
		
		List <Integer> result = new ArrayList<Integer>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(res.getInt("artist_id"));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List <Adiacenza> getAdiacenza (String ruolo) {
		String sql = "SELECT DISTINCT a1.artist_id AS artista1, a2.artist_id AS artista2, COUNT(DISTINCT(e1.exhibition_id)) AS peso " + 
				"FROM authorship a1, authorship a2, objects o1, objects o2, exhibition_objects e1, exhibition_objects e2 " + 
				"WHERE a1.artist_id > a2.artist_id " + 
				"AND a1.role = ? " + 
				"AND a2.role = ? " +
				"AND a1.object_id = o1.object_id " + 
				"AND a2.object_id = o2.object_id " + 
				"AND o1.object_id <> o2.object_id " + 
				"AND o1.object_id = e1.object_id " + 
				"AND o2.object_id = e2.object_id " + 
				"AND e1.exhibition_id = e2.exhibition_id " + 
				"GROUP BY artista1, artista2 " +
				"ORDER BY peso DESC ";
		
		List <Adiacenza> result = new ArrayList<Adiacenza>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				if (res.getInt("artista1") !=0 && res.getInt("artista2") !=0) {
					result.add(new Adiacenza(res.getInt("artista1"), res.getInt("artista2"), res.getInt("peso")));
				}
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
