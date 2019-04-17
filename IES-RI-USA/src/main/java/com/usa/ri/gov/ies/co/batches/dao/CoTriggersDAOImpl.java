package com.usa.ri.gov.ies.co.batches.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.usa.ri.gov.ies.co.batches.entity.CoTriggersEntity;

@Repository("coTriggersDAO")
public class CoTriggersDAOImpl implements CoTriggersDAO {

	private final static String SELECT_PENDING_TRIGGERS
		="SELECT * FROM CO_TRIGGERS WHERE TRG_STATUS=? AND ORA_HASH(TRG_ID,?)=?";
	
	@Autowired
	private DataSource ds;
	
	@Override
	public List<CoTriggersEntity> findByTriggerStatus(String status, Integer totalBuckets, Integer currInstance) {

		List<CoTriggersEntity> entities=new ArrayList();
		try {
			Connection con=ds.getConnection();
		PreparedStatement ps=	con.prepareStatement(SELECT_PENDING_TRIGGERS);
			
		//set query param
		ps.setString(1, status);
		ps.setInt(2, totalBuckets);
		ps.setInt(3, currInstance);
		
		ResultSet rs=ps.executeQuery();
		
		while(rs.next()) {
			CoTriggersEntity entity=new  CoTriggersEntity();
			entity.setTriggerId(rs.getInt(1));
			entity.setCaseNum(rs.getInt(2));
			entity.setCreatedDate(rs.getDate(3));
			entity.setTriggerStatus(rs.getString(4));
			entity.setUpdatedDate(rs.getDate(5));
			entities.add(entity);
		}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return entities;
	}

	
}
