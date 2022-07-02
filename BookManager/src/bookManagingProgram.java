import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class bookManagingProgram {
	private static int password = 1234;		//관리자 비밀번호

	
	public static void libraryApp() {			//로그인 화면
		Scanner in = new Scanner(System.in);
		 
		System.out.println("*************** 로그인 화면 ***************");
		try {
			while(true) {
				System.out.print("비밀번호를 4자리 입력해주세요.(0 : 종료) -> ");	//비밀번호 입력
				int enterNum = in.nextInt();
				System.out.println();
				
				if(enterNum==0) {			//0이면 프로그램 종료
					System.out.println("************* 프로그램 종료 *************");
					break;
				}else if(password==enterNum){		//비밀번호가 맞을때 프로그램 실행
					System.out.println("어서오세요, 관리자님!");
					menu();
					break;
				}else {			//비밀번호가 틀렸을때
				System.out.println("비밀번호가 틀립니다. 다시 입력해주세요.(0 : 종료)");
				}
			}
			
		}catch(InputMismatchException e) {			//숫자 이외를 입력했을때
			System.out.println("숫자를 입력하세요.");
			System.out.println();
			libraryApp();
		}
	}
	
	public static void menu() {			//로그인 후 메뉴화면
		System.out.println();	
		System.out.println("***************************** 메뉴 ********************************");
		System.out.println("1. 모든 책 조회  2. 책 검색  3. 책 등록  4. 책 삭제  5. 책 수정  6. 종료");
		System.out.print("원하시는 메뉴 번호를 선택하세요. > ");		//메뉴 선택
		Scanner in = new Scanner(System.in);
		int choice = in.nextInt();
		System.out.println();
		switch(choice) {
		case 1:				//모든 책 조회
			showBooks();
			break;
		case 2:				//책 검색
			searchBook();
			break;
		case 3:				//책 등록
			bookRegi();
			break;
		case 4:				//책 삭제
			delBook();
			break;
		case 5:				//책 수정
			revBook();
			break;
		case 6:				//종료
			System.out.println("************* 프로그램 종료 *************");
			break;
		default:				//메뉴 번호 이외를 입력했을때
			System.out.println("존재하지 않는 메뉴입니다. 다시 선택해주세요.");
			menu();
			break;
		}
	}
	
	private static void showBooks() {				//모든 책 조회
		 try{
		  		Connection conn = null;
		  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
		  		Statement stmt = null;
		  		ResultSet rsTable = null;

		  		try{
		  		  Class.forName("com.mysql.cj.jdbc.Driver");
		        conn=DriverManager.getConnection(url,"root","783481");
		        if( conn == null ) {
		          System.out.println("Not connected");
		        }else{
		          System.out.println("****************************** 모든 책 조회 ******************************\n");
		          stmt = conn.createStatement();
		    			rsTable = stmt.executeQuery("select bookName '책이름', publisher '출판사', price '가격' from books");
		    			System.out.println(" 책 제목 \t\t출판사 \t\t가격");
		    			System.out.println("----------------------------------------------");
		    			for(int i = 0; rsTable != null && rsTable.next(); i++){
		    			  System.out.println(rsTable.getString(1)+"\t\t"+rsTable.getString(2)+"\t\t"+rsTable.getString(3));
		    			}
		    		}
	  		}catch(Exception e){
	  			System.out.println("error:"+e.toString());
	  		}finally{
	  		  if( stmt != null ) stmt.close();
	  		  if( rsTable != null ) rsTable.close();
	  		  if( conn != null ) conn.close();
	  		  menu();
	  		}
	    }catch(Exception ex){
	      System.out.println("error : " + ex.toString());
	    }
	}
	
	private static void searchBook() {				//책 검색
		System.out.println("************************************ 책 검색 ************************************");
		System.out.println("1. 책 제목 검색  2. 출판사 검색  3. 메뉴로 돌아가기");
		System.out.print("원하시는 옵션 번호를 선택하세요. > ");
		Scanner in = new Scanner(System.in);
		int choice = in.nextInt();
		
		switch(choice) {
			case 1:				//책 제목으로 검색
				searchTitle();
				break;
			case 2:				//책 출판사로 검색
				searchPub();
				break;
			case 3:				//메뉴로 돌아가기
				menu();
				break;
			default:				//1~3 이외의 숫자 입력 시
				System.out.println("존재하지 않는 옵션입니다. 다시 입력해주세요.");
				searchBook();
				break;
		}
	}
	

	private static void searchTitle() {		//제목으로 책 검색
		try{
	  		Connection conn = null;
	  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
	  		PreparedStatement pstmt = null;
	  		ResultSet rsTable = null;

	  		try{
	  		  Class.forName("com.mysql.cj.jdbc.Driver");
	        conn=DriverManager.getConnection(url,"root","783481");
	        if( conn == null ) {
	          System.out.println("Not connected");
	        }else{
	        	Scanner in = new Scanner(System.in);		//제목 입력
	        	System.out.print("제목을 입력하세요 > ");
	        	String title = in.next();					  
	            
	      		pstmt = conn.prepareStatement("select bookName, publisher, price from books where bookName = ?");
	            pstmt.setString(1, title);
	      		rsTable = pstmt.executeQuery();
	      		try {
		      		System.out.println();
		      		System.out.println("책 제목 \t\t출판사 \t\t 가격");
	    			System.out.println("----------------------------------------------");
	    			rsTable.next();
	    			System.out.println(rsTable.getString(1)+"\t\t"+rsTable.getString(2)+"\t\t"+rsTable.getInt(3));
	      		}catch(SQLException se) {				//책 제목을 찾을 수 없을 때
	      			System.out.println("입력하신 책을 찾을 수 없습니다. 다시 시도해주세요.");
	      			System.out.println();
	      			searchBook();
	      		}
	      		}
	  		}catch(Exception e) {
	  			System.out.println("error:"+e.toString());
	  		}finally{
	  		  if( pstmt != null ) pstmt.close();
	  		  if( rsTable != null ) rsTable.close();
	  		  if( conn != null ) conn.close();
	  		  menu();
	  		}
	    }catch(Exception exx){
	      System.out.println("error : " + exx.toString());
	    }
	}

	private static void searchPub() {		//출판사로 책 검색
		try{
	  		Connection conn = null;
	  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
	  		PreparedStatement pstmt = null;
	  		ResultSet rsTable = null;

	  		try{
	  		  Class.forName("com.mysql.cj.jdbc.Driver");
	        conn=DriverManager.getConnection(url,"root","783481");
	        if( conn == null ) {
	          System.out.println("Not connected");
	        }else{
	        	Scanner in = new Scanner(System.in);		//제목 입력
	        	System.out.print("책의 출판사를 입력하세요 > ");
	        	String pub = in.next();				

	      		pstmt = conn.prepareStatement("select bookName, publisher, price from books where publisher = ?");
	            pstmt.setString(1, pub);
	      		rsTable = pstmt.executeQuery();
	      		try {
		      		System.out.println();
		      		System.out.println("책 제목 \t\t출판사 \t\t 가격");
	    			System.out.println("----------------------------------------------");
	    			rsTable.next();
	    			System.out.println(rsTable.getString(1)+"\t\t"+rsTable.getString(2)+"\t\t"+rsTable.getInt(3));
	      		}catch(SQLException se) {				//책 출판사를 찾을 수 없을 떄
	      			System.out.println("입력하신 책을 찾을 수 없습니다. 다시 시도해주세요.");
	      			System.out.println();
	      			searchBook();
	      		}
	      		}
	        }catch(Exception e) {
	  			System.out.println("error:"+e.toString());
	  		}finally{
	  		  if( pstmt != null ) pstmt.close();
	  		  if( rsTable != null ) rsTable.close();
	  		  if( conn != null ) conn.close();
	  		  menu();
	  		}
	    }catch(Exception exx){
	      System.out.println("error : " + exx.toString());
	    }
	}

	private static void bookRegi() {				//책 등록
		
		try{
	  		Connection conn = null;
	  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
	  		PreparedStatement pstmt = null;

	  		try{
	  		  Class.forName("com.mysql.cj.jdbc.Driver");
	  		  conn=DriverManager.getConnection(url,"root","783481");
	        if( conn == null ) {
	          System.out.println("Not connected");
	        }else{
	        	System.out.println("*************************** 책 등록 *****************************");
	        	
	        	Scanner in = new Scanner(System.in);
	        	System.out.print("책 제목을 입력하세요. > ");
	        	String name = in.next();
	        	System.out.print("책 출판사를 입력하세요. > ");
	        	String pub = in.next();
	        	System.out.print("책 가격을 입력하세요. > ");
	        	int pri = in.nextInt();
	        	
	           pstmt = conn.prepareStatement("insert into books(bookName, publisher, price) values(?, ?, ?) ");
	           pstmt.setString(1, name);
	           pstmt.setString(2, pub);
	           pstmt.setInt(3, pri);
	           pstmt.executeUpdate();	
	           System.out.println("'"+name+"' 이(가) 추가되었습니다.");
	        	}
	  		}catch(Exception e){
	  			System.out.println("error:"+e.toString());
	  		}finally{
	  		  if( pstmt != null ) pstmt.close();
	  		  if( conn != null ) conn.close();
	  		  menu();
	  		}
	    }catch(Exception ex){
	      System.out.println("error : " + ex.toString());
	    }
	}
	
	
	private static void delBook() {					//책 삭제
		
		try{
	  		Connection conn = null;
	  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
	  		PreparedStatement pstmt = null;
	  		ResultSet rs = null;

	  		try{
	  		  Class.forName("com.mysql.cj.jdbc.Driver");
	        conn=DriverManager.getConnection(url,"root","783481");
	        if( conn == null ) {
	          System.out.println("Not connected");
	        }else{
	        	System.out.println("*************************** 책 삭제 ****************************");
	    		System.out.print("책 제목을 입력하세요. > ");
	    		Scanner in = new Scanner(System.in);
	    		String title = in.next();
	        	
	        	try {					//책 조회 후 있는지 확인한 후 삭제
	        		pstmt = conn.prepareStatement("select count(bookName) from books where bookName = ?");
	        		pstmt.setString(1, title);
	        		rs = pstmt.executeQuery();
	        		rs.next();
	        		if(rs.getInt(1)!=0) {
	        			pstmt.close();
		        		pstmt = conn.prepareStatement("delete from books where bookName = ?");
		        		pstmt.setString(1, title);
			        	pstmt.executeUpdate();
			    		System.out.println("'"+title+"'이(가) 목록에서 제거되었습니다.");
	        		}else {				//책을 목록에서 찾을 수 없을 시
	        			System.out.println("'"+title+"'을(를) 목록에서 찾을 수 없습니다. 다시 입력하세요.");
	        			System.out.println();
	        			delBook();
	        		}
	        		
	        	}catch(SQLException e) {
	        		System.out.println("error : "+e.toString());
	        	}
	        	
	    		}
  		}catch(Exception e){
  			System.out.println("error:"+e.toString());
  		}finally{
  		  if( pstmt != null ) pstmt.close();
  		  if( conn != null ) conn.close();
  		  menu();
  		}
    }catch(Exception ex){
      System.out.println("error : " + ex.toString());
    	}
	}

	
	private static void revBook() {				//책 수정
		System.out.println("************************** 책 수정 **************************");
		System.out.println("1. 책 제목 수정  2. 책 출판사 수정  3. 책 가격 수정  4. 메뉴로 돌아가기");
		Scanner in = new Scanner(System.in);
		System.out.print("원하시는 옵션을 선택하세요. > ");
		int choice = in.nextInt();
		System.out.print("변경을 원하시는 책의 제목을 입력하세요. > ");		//?????4번과 다른번호 입력시 출력되면 안되는데 됨
		String title = in.next();
		
		switch(choice) {
			case 1:				//책 제목 수정
				revTitle(title);
				break;
			case 2:				//책 출판사 수정
				revPub(title);
				break;
			case 3:				//책 가격 수정
				revPri(title);
				break;
			case 4:				//메뉴로 돌아가기
				menu();
				break;
			default:				//보기 이외의 번호를 입력 시
				System.out.println("존재하지 않는 옵션입니다. 다시 선택해주세요.");
				revBook();
				break;
		}
	}

	private static void revTitle(String title) {		//제목 수정
		try{
	  		Connection conn = null;
	  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
	  		PreparedStatement pstmt = null;

	  		try{
	  		  Class.forName("com.mysql.cj.jdbc.Driver");
	        conn=DriverManager.getConnection(url,"root","783481");
	        if( conn == null ) {
	          System.out.println("Not connected");
	        }else{
	        	Scanner in = new Scanner(System.in);
	        	System.out.print("바꿀 제목을 적어주세요. > ");
	        	String cTitle = in.next();
	        	pstmt = conn.prepareStatement("update books set bookName = ? where bookName = ?");
	        	pstmt.setString(1, cTitle);
	        	pstmt.setString(2, title);
	        	pstmt.executeUpdate();
	    		System.out.println("'"+title+"' 의 제목이 '"+cTitle+"'로 변경되었습니다.");
	    		}
  		}catch(Exception e){
  			System.out.println("error:"+e.toString());
  		}finally{
  		  if( pstmt != null ) pstmt.close();
  		  if( conn != null ) conn.close();
  		  menu();
  		}
    }catch(Exception ex){
      System.out.println("error : " + ex.toString());
    	}
	}

	private static void revPub(String title) {		//출판사 수정
		try{
	  		Connection conn = null;
	  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
	  		PreparedStatement pstmt = null;

	  		try{
	  		  Class.forName("com.mysql.cj.jdbc.Driver");
	        conn=DriverManager.getConnection(url,"root","783481");
	        if( conn == null ) {
	          System.out.println("Not connected");
	        }else{
	        	Scanner in = new Scanner(System.in);
	        	System.out.print("바꿀 출판사의 이름을 적어주세요. > ");
	        	String cPub = in.next();
	        	pstmt = conn.prepareStatement("update books set publisher = ? where bookName = ?");
	        	pstmt.setString(1, cPub);
	        	pstmt.setString(2, title);
	        	pstmt.executeUpdate();
	    		System.out.println("'"+title+"' 의 출판사가 '"+cPub+"'로 변경되었습니다.");
	    		}
  		}catch(Exception e){
  			System.out.println("error:"+e.toString());
  		}finally{
  		  if( pstmt != null ) pstmt.close();
  		  if( conn != null ) conn.close();
  		  menu();
  		}
    }catch(Exception ex){
      System.out.println("error : " + ex.toString());
    	}
	}

	private static void revPri(String title) {		//가격 수정
		try{
	  		Connection conn = null;
	  		String url= "jdbc:mysql://127.0.0.1:3306/libarary";
	  		PreparedStatement pstmt = null;

	  		try{
	  		  Class.forName("com.mysql.cj.jdbc.Driver");
	        conn=DriverManager.getConnection(url,"root","783481");
	        if( conn == null ) {
	          System.out.println("Not connected");
	        }else{
	        	Scanner in = new Scanner(System.in);
	        	System.out.print("바꿀 가격을 적어주세요. > ");
	        	int cPri = in.nextInt();
	        	pstmt = conn.prepareStatement("update books set price = ? where bookName = ?");
	        	pstmt.setInt(1, cPri);
	        	pstmt.setString(2, title);
	        	pstmt.executeUpdate();
	    		System.out.println("'"+title+"' 의 가격이 '"+cPri+"'로 변경되었습니다.");
	    		}
  		}catch(Exception e){
  			System.out.println("error:"+e.toString());
  		}finally{
  		  if( pstmt != null ) pstmt.close();
  		  if( conn != null ) conn.close();
  		  menu();
  		}
    }catch(Exception ex){
      System.out.println("error : " + ex.toString());
    	}
	}

	public static void main(String[] args) {		//Main
		libraryApp();
	}
}
