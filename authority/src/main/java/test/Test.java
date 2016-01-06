package test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) throws SQLException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
		DataSource dataSource = (DataSource) ctx.getBean("dataSource");
		Connection con = dataSource.getConnection();
		System.out.println(con);
	}
}
