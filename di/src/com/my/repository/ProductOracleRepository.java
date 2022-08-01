package com.my.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.my.dto.Product;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.sql.MyConnection;

@Repository(value="productRepository")
public class ProductOracleRepository implements ProductRepository {
	@Autowired  //retention 언제 어노테이션이 효과를 낼 것인가 (Runtime -> 실행 시) 
//	@Qualifier(value="dataSource")
	private DataSource ds; //직접 만드는게 아니라 외부로부터 주입(생성자,세터)받아 의존관계를 완성해야함 
	
	//필드에 Autowired 붙였을 때
	//객체가 생성되자마자 그 즉시 field에 inject된다 
	
//	public ProductOracleRepository() {}
//	public ProductOracleRepository(DataSource ds) {
//		this.ds = ds;
//	}
	@Override
	public void insert(Product product) throws AddException {

	}

	@Override
	public List<Product> selectAll() throws FindException {
//		List<Map<String,Object>> sample = new ArrayList<>();  //맵은 다용성 
		List<Product> products = new ArrayList<>();   //구체적인 클래스를 이용 
//		Map<String,Object> map1 = new HashMap<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectProductAllSQL = "SELECT * FROM product ORDER BY prod_no ASC";
		try {
			//con = MyConnection.getConnection();
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectProductAllSQL);   //바인드 변수가 없기에 세팅할 값도 없음
			rs = pstmt.executeQuery();
			//prod_no의 값이 rs에 담겨있고 이것을 검색해오려면 다음행으로 진행하는 next()가 필요하다 
			//rs.next를 할 때 마다 다음행으로 커서가 이동함, 값이 없으면 false를 반환함 
			while(rs.next()) {
				String prod_no = rs.getString("prod_no");	//VARCHAR2 -> String
				String prod_name = rs.getString("prod_name");
				int prod_price = rs.getInt("prod_price");  //NUMBER -> int
//				Map<String,Object> map1 = new HashMap<>();
//				map1.put("prod_no", prod_no);
//				map1.put("prod_name", prod_name);
//				map1.put("prod_price", prod_price);
//				sample.add(map1); //List타입의 sample변수에 map을 저장해보았음 
				Product p = new Product(prod_no, prod_name, prod_price);
				products.add(p);
			}
			if(products.size() == 0) {
				throw new FindException("상품이 없습니다");
			}
			return products;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
//		return null;
	}

	@Override
	public Product selectByProdNo(String prodNo) throws FindException {
		//모든 DB와의 연결 시 절차 1)DB연결  2)SQL송신 3)수신및활용 4)DB연결닫기 
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectProdNoSQL = "SELECT * FROM product WHERE prod_no=?";
		try {
			//con = MyConnection.getConnection();
			con = ds.getConnection();
			pstmt = con.prepareStatement(selectProdNoSQL);
			pstmt.setString(1,  prodNo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String prodName = rs.getString("prod_name");
				int prodPrice = rs.getInt("prod_price");
				String prodInfo = rs.getString("prod_info");
				java.sql.Date prodMfd = rs.getDate("prod_mfd");
				
				Product p = new Product(prodNo, prodName, prodPrice, prodInfo, prodMfd);
				return p;
			}else {
				throw new FindException("상품이 없습니다");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(rs, pstmt, con);
		}
		
	}

	@Override
	public List<Product> selectByProdNoOrProdName(String word) throws FindException {
		return null;
	}

}
