package di;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.my.dto.Product;
import com.my.exception.FindException;
import com.my.repository.ProductRepository;
import com.my.service.CustomerService;
import com.my.service.ProductService;

public class Test {

	public static void main(String[] args) {
		Product p1, p2; 
//		p1 = new Product(); << 이런방식으로 하는게 아니고
		//1. 스프링 컨테이너 구동 
		
		String configurationPath = "configuration3.xml";
		ClassPathXmlApplicationContext ctx;
		ctx = new ClassPathXmlApplicationContext(configurationPath);
		//2. Product 객체찾아 사용하기 
		//Product.class -> Product클래스 타입으로 다운캐스팅 가능한가? 
		p1 = ctx.getBean("p", Product.class); //첫번째 인자 : 객체 이름 / 두번째 인자 : 다운캐스팅가능한 자료형
		System.out.println(p1.hashCode());
		System.out.println(p1);
		
		p2 = ctx.getBean("p", Product.class); 
		System.out.println(p2.hashCode());
		System.out.println(p1 == p2);   //p1과 p2는 같은것을 참조하고 있음 
		
		CustomerService service = ctx.getBean("customerService", CustomerService.class);
		System.out.println(service.hashCode());
		//component-scan을 쓰는 경우 bean의 name이 아니라 클래스이름으로 해야함 
		//Annotation에 value로 name을 지정할수 있으나 지정하지 않으면 class이름이 annotation이름이 됨 
		
		//서비스에 주입된 리포지토리 얻기 
		ProductService pService = ctx.getBean("productService", ProductService.class);
		ProductRepository r1 = pService.getRepository();
		
		//리포지토리 찾기 
		ProductRepository r2 = ctx.getBean("productRepository", ProductRepository.class); 
		//형변환을 Oracle리파지토리로도 가능하나 하지 않는 이유는 ProductOracle리파지토리는 변경 가능성이 있기 때문 
		//소스코드를 변경하지 않고 설정파일을 통해 리파지토리를 바꿀 수 있음 -> 소스코드 변경은 바람직하지 않음 
		//ex) ProductOracleRepository -> ProductMySQLRepository 
		
		System.out.println(r1 == r2);
		
		try {
			Product p = r2.selectByProdNo("C0001");
			System.out.println(p);
		} catch (FindException e) {
			e.printStackTrace();
		}

	}

} 
