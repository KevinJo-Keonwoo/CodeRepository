package com.my.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.dto.Product;
import com.my.exception.AddException;
import com.my.exception.FindException;

@Repository(value="productRepository")
public class ProductOracleRepository implements ProductRepository {
//	@Autowired  //retention 언제 어노테이션이 효과를 낼 것인가 (Runtime -> 실행 시) 
//	@Qualifier(value="dataSource")
//	private DataSource ds; //직접 만드는게 아니라 외부로부터 주입(생성자,세터)받아 의존관계를 완성해야함 
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	private Logger logger = Logger.getLogger(this.getClass());

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
		List<Product> products = new ArrayList<>();   //구체적인 클래스를 이용 
		SqlSession session = null; 
		try {
			session = sqlSessionFactory.openSession();
			products = session.selectList("com.my.mapper.ProductMapper.selectAll"); //namespace.id
			//selectList -> 여러행 검색시 사용되는 메서드 
			if(products.size() == 0) {
				throw new FindException("상품이 없습니다");
			}
			return products;
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}
	@Override
	public Product selectByProdNo(String prodNo) throws FindException {
		//모든 DB와의 연결 시 절차 1)DB연결  2)SQL송신 3)수신및활용 4)DB연결닫기 
		SqlSession session = null; 
		Product p = new Product();
//		String selectProdNoSQL = "SELECT * FROM product WHERE prod_no=?";
		try {
			System.out.println("SYSOUT : prodNo in productoraclerepository selectByProdNo:" + prodNo);
			logger.debug("debug prodNo:" + prodNo);
			logger.info("info prodNo:" + prodNo);
			logger.warn("warn prodNo:" + prodNo);
			logger.error("error prodNo:" + prodNo);
			session = sqlSessionFactory.openSession();
			p = session.selectOne("com.my.mapper.ProductMapper.selectByProdNo", prodNo);
			if(p == null) {
				throw new FindException("상품이 없습니다");
			}
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	@Override
	public List<Product> selectByProdNoOrProdName(String word) throws FindException {
		List<Product> products = new ArrayList<>();   
		SqlSession session = null; 
		try {
			session = sqlSessionFactory.openSession();
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("word", word);
			hashMap.put("order", "prod_name DESC");
			products = session.selectList("com.my.mapper.ProductMapper.selectByProdNoOrProdName", 
																//"%" +word + "%");
																//word); 
																hashMap);
			return products;
			//LIKE 사용하기 위해 word에 %를 붙여서 보냄 
		}catch(Exception e){
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
			
	}

}
