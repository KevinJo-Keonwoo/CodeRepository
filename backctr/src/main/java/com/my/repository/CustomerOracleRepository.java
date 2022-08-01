package com.my.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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
	private SqlSessionFactory sessionFactory;
	
//	@Autowired
//	@Qualifier(value="dataSource")
//	private DataSource ds;
	//sqlsessionfactory가 이미 datasource와 연결되어있기에 쓸 필요 없음 
	
	@Override
	public void insert(Customer customer) throws AddException {
		//더이상 DB와의 연결을 통해 사용하는 것이 아닌
		//세션팩토리의 세션메서드로 사용
		SqlSession session = null;
		try {
			session = sessionFactory.openSession();
			session.insert("com.my.mapper.CustomerMapper.insert",customer); 
			//인자값 mapper의 namespace, 전달할 parameter객체
		}catch(Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}finally {
			if(session !=null) {
				session.close();
			}
		}
	}
		
//		Connection con = null;
//		PreparedStatement pstmt = null; ///executeUpdate()
//		
//		String insertSQL = "INSERT INTO customer(id, pwd, name, address, status, buildingno) VALUES (?, ?, ?, ?, 1, ?)";
//		
//		try {
//			con = ds.getConnection();
//			pstmt = con.prepareStatement(insertSQL);
//			System.out.println(customer.getId());
//			pstmt.setString(1,customer.getId());  //위의 request.getParamaet("id")의 값이 대입 
//			pstmt.setString(2,customer.getPwd());
//			pstmt.setString(3,customer.getName());
//			pstmt.setString(4,customer.getAddress());
//			pstmt.setString(5,customer.getBuildingno());
//			pstmt.executeUpdate(); 
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new AddException(e.getMessage());
//		} finally {
//			MyConnection.close(pstmt, con);
//		}

	@Override
	public Customer selectById(String id) throws FindException {
		SqlSession session = null;
		try {
			session = sessionFactory.openSession(); //connection과 같은 의미 
			Customer c = session.selectOne("com.my.mapper.CustomerMapper.selectById", id); 
			//Customermapper의 namespace.id
			//여기의 id값이 customerMapper의 #{id}로 간다 
			//CustomerMapper에서 수행된 SQL결과문의 값이 자동 입력되어있는 객체가 반환됨 
			if(c == null) { //sql구문이 없다
				throw new FindException("고객이 없습니다");
			}
			return c;
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) { 
				session.close(); //DB와의 연결을 닫는것이 아니라 연결을 다시 Connectionpool에게 돌려준다는 의미 
				//connection.close와는 다른 의미 여기서의 close는 반드시필요하지는 않음. 
			}
		}
		
		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		String selectIdDupChkSQL = "SELECT * FROM customer WHERE id = ?";
//		try {
////			con = MyConnection.getConnection();
//			con = ds.getConnection();
//			pstmt = con.prepareStatement(selectIdDupChkSQL);
//			pstmt.setString(1, id);
//			rs = pstmt.executeQuery();
//			if(rs.next()) {
//				return new Customer(
//						rs.getString("id"),
//						rs.getString("pwd"),
//						rs.getString("name"),
//						rs.getString("address"),
//						rs.getInt("status"),
//						rs.getString("buildingno")
//						);
//			}
//			throw new FindException("고객이 없습니다");
//		}catch (SQLException e) {
//			throw new FindException(e.getMessage());
//		}finally {
//			MyConnection.close(rs, pstmt, con);
//		}
	}
}