package com.my.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.my.dto.Customer;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.sql.MyConnection;

@Repository(value="customerRepository")
public class CustomerOracleRepository implements CustomerRepository {
	@Autowired
//	@Qualifier(value="dataSource2")
	private DataSource ds;
	
	//재사용성 떨어져서 빼버림 
//	@Override
//	public Customer selectByIdAndPwd(String id, String pwd) throws FindException {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try {
//			con = MyConnection.getConnection();
//			String selectIdNPwdSQL = "SELECT * FROM customer WHERE id=? AND pwd=?";
//			pstmt = con.prepareStatement(selectIdNPwdSQL);
//			pstmt.setString(1,  id);  //위의 request.getParamaet("id")의 값이 대입 
//			pstmt.setString(2,  pwd);
//			rs = pstmt.executeQuery(); //결과로 받아줘야함 
//			if(rs.next()) {  //행이 1개또는 o개반환이기에 굳이 while문 안써도 됨  
//				return new Customer(rs.getString("id"),
//						rs.getString("pwd"),
//						rs.getString("name"),
//						rs.getString("address"),
//						rs.getInt("status"),
//						rs.getString("buildingno")
//						);
//			}
//			throw new FindException("고객이 없습니다");
//		} catch(Exception e) {
//			throw new FindException(e.getMessage());
//		} finally {
//			MyConnection.close(rs, pstmt, con);
//		}
//	}

	@Override
	public void insert(Customer customer) throws AddException {
		//DB연결
		Connection con = null;
		//SQL송신
		PreparedStatement pstmt = null; ///executeUpdate()
		
		String insertSQL = "INSERT INTO customer(id, pwd, name, address, status, buildingno) VALUES (?, ?, ?, ?, 1, ?)";
		
		try {
//			con = MyConnection.getConnection();
			con = ds.getConnection();
//		String selectIdNPwdSQL = " * FROM customer WHERE id=? AND pwd=?";
			pstmt = con.prepareStatement(insertSQL);
			System.out.println(customer.getId());
			pstmt.setString(1,customer.getId());  //위의 request.getParamaet("id")의 값이 대입 
			pstmt.setString(2,customer.getPwd());
			pstmt.setString(3,customer.getName());
			pstmt.setString(4,customer.getAddress());
			pstmt.setString(5,customer.getBuildingno());
			pstmt.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			MyConnection.close(pstmt, con);
		}
	}

	@Override
	public Customer selectById(String id) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String selectIdDupChkSQL = "SELECT * FROM customer WHERE id = ?";
		try {
//			con = MyConnection.getConnection();
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectIdDupChkSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return new Customer(
						rs.getString("id"),
						rs.getString("pwd"),
						rs.getString("name"),
						rs.getString("address"),
						rs.getInt("status"),
						rs.getString("buildingno")
						);
			}
			throw new FindException("고객이 없습니다");
		}catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(rs, pstmt, con);
		}
	}
}