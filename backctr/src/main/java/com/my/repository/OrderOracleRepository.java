package com.my.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.dto.OrderInfo;
import com.my.dto.OrderLine;
import com.my.dto.Product;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.sql.MyConnection;

@Repository
public class OrderOracleRepository implements OrderRepository {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	public void insert(OrderInfo info) throws AddException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			insertInfo(session, info);
			insertLines(session, info.getLines());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(session != null){
				session.close();
			}
		}
	}
	private void insertInfo(SqlSession session, OrderInfo info) throws SQLException{
		session.insert("com.my.mapper.OrderMapper.insertInfo", info);
	}
	private void insertLines(SqlSession session, List<OrderLine> lines) throws SQLException{
		for (OrderLine line: lines) {
			session.insert("com.my.mapper.OrderMapper.insertLine", line);
		}
	}
// 	@Override
//	public List<OrderInfo> selectById(String orderId) throws FindException {
//		//최근주문내역부터 나타나게 하기 주문번호순 내림차순 -> 상품번호 기준으로 오름차순  select구문  
// 		//여기서는 SQL구문 사용 set 어쩌구로 
// 		Connection con = null;
// 		PreparedStatement pstmt = null;
// 		PreparedStatement pstmtTwo = null;
// 		ResultSet rs = null;
// 		ResultSet rsTwo = null;
// 		System.out.println("1");
// 		String selectOrderInfoLine = "SELECT * FROM order_line l JOIN order_info i ON (i.order_no = l.order_no) ORDER BY i.order_no DESC, l.order_prod_no ASC";
// 		
// 		System.out.println("2");
// 		try {
//			con = MyConnection.getConnection();
//			pstmt = con.prepareStatement(selectOrderInfoLine);
//			rs = pstmt.executeQuery();
//			while(rs.next()) {
//				int orderNo = rs.getInt("order_no");
//				String orderIds = orderId;
//				System.out.println("3");
//				Date orderDate = rs.getDate("order_dt");
//				int orderQuantity = rs.getInt("order_quantity");
//				
//				String orderProNo = rs.getString("order_prod_no");
//				Product orderP = new Product(orderProNo, orderIds, orderNo);
//				
////				List<OrderLine> orderLine = (List<OrderLine>)new OrderLine(orderNo, orderP, orderQuantity);
////				List<OrderInfo> odfInfo = (List)new OrderInfo(orderNo, orderIds, orderDate, (List<OrderLine>)new OrderLine(orderNo, orderP, orderQuantity));
//				OrderInfo odfInfo = new OrderInfo(orderNo, orderIds, orderDate, (List<OrderLine>)new OrderLine(orderNo, orderP, orderQuantity));
////				System.out.println(orderNo + orderIds + orderDate + orderLine);
//				List<OrderInfo> add;
////					add.add(odfInfo);
//				System.out.println("4");
//				return add;
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			MyConnection.close(rs, pstmt, con);
//		}
// 		
//	}
	@Override
	public List<OrderInfo> selectById(String orderId) throws FindException {
 		SqlSession session = null;
 		try {
 			session = sqlSessionFactory.openSession();
 			List<OrderInfo> infos = session.selectList("com.my.mapper.OrderMapper.selectById", orderId);
 		
			if(infos.size() == 0) {
				throw new FindException("주문내역이 없습니다");
			}
			return infos;
		}catch(Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			session.close();
		}
	}
	
		
 		// 주문 내역은 꺼내 오는 것이기 때문에 DB와 연결을 해서 꺼내와야함
 		// insert는 넣어주는 값이기 때문에 DB와 연결 할 필요가 X
		// repository에선 웹과 관련된 일 절대 하지X
 		// 로그인 된 orderId를 가지고 와서 orderInfo(주문자가 주문한 내용만 반환)를 List에 넣어서 
 		// 반환된 내용 서블릿이 받아와서 서블릿이 json문자열로 만들어서 응답
 		// 최근 주문번호부터 내림차순, 같은 주문번호라면 상품 번호로 오름차순하여 정렬
 		// select 구문 만들어져야하고 
// 		
// 		Connection con = null;
// 		PreparedStatement pstmt = null;
// 		ResultSet rs = null;
// 		String viewOrderInfoSQL = 
// 				"SELECT * FROM ORDER_LINE, ORDER_INFO, PRODUCT "
// 				+ "WHERE ORDER_LINE.ORDER_NO = ORDER_INFO.ORDER_NO";
//// 				+ "ORDER BY ORDER_DT DESC, PROD_NAME";
//
// 		// 		String viewOrderInfoSQL = 
//// 				"SELECT I.ORDER_NO, I.ORDER_ID, I.ORDER_DT, L.ORDER_NO,P.PROD_NO,P.PROD_NAME,P.PROD_PRICE,L.ORDER_QUANTITY "
//// 				+ "FROM ORDER_LINE L JOIN ORDER_INFO I ON (L.ORDER_NO = I.ORDER_NO)"
//// 				+ "JOIN PRODUCT P ON (P.PROD_NO = L.ORDER_PROD_NO)";
////				+ "ORDER BY 3 DESC, 5";
// 		
//		try {
//			con = MyConnection.getConnection();
//			pstmt = con.prepareStatement(viewOrderInfoSQL);
//			rs = pstmt.executeQuery(); // rs라는 변수에 주문 내역을 할당
//			// 여기까지 DB에서 주문 내역을 꺼내 오는 것
//			
//			// 값이 한줄한줄 들어가는 것
//			List<OrderInfo> orderInfo = new ArrayList<>();
//			while(rs.next()) {
//				int orderNo = rs.getInt("order_no"); 
//				String order_id = rs.getString("order_id"); 
//				Date orderDt = rs.getDate("order_dt");
//				String orderProdNo = rs.getString("order_prod_no");
//				int orderQuantity = rs.getInt("order_quantity");
//				String prodName = rs.getString("prod_name");
//				int prodPrice = rs.getInt("prod_price");
//				
//				Product orderP = new Product(orderProdNo, prodName, prodPrice); //생성자 선언
//				
//				List<OrderLine> lines = new ArrayList<>();
//				OrderLine orderLine = new OrderLine(orderNo, orderP, orderQuantity);
////				lines.add(orderLine);
//				
//				OrderInfo info = new OrderInfo(orderNo, orderId, orderDt, lines);
//				orderInfo.add(info);
//				
////				(List)new OrderLine(orderNo, orderP, orderQuantity); 
//				
////				OrderLine l = new OrderLine(orderNo, orderP, orderQuantity);
//				
////				info =  (List) new OrderInfo(orderNo, orderId, orderDt, orderLine);
//			}
//			if(orderInfo.size() == 0) {
//				throw new FindException("주문 내역이 없습니다");
//			}
//			return orderInfo;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new FindException(e.getMessage());
//		}finally {
//			MyConnection.close(rs,  pstmt, con);
//		}
//		
// 
// 	} 

}
