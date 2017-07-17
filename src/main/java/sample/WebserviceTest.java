package sample;

import java.util.ArrayList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.protocol.HttpContext;

public class WebserviceTest {
	final static String URL = "http://localhost:8080";
	final static String ADMIN_USERNAME = "admin";
	final static String ADMIN_PASSWORD = "admin";
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpContext httpContext = UserOperations.getSession(URL, ADMIN_USERNAME, ADMIN_PASSWORD);
		if(httpContext!=null){
			//createUser(httpContext);
			//updateUser(httpContext);
			//deleteUser(httpContext);
			//getUser(httpContext);
			//assignCouseToUser(httpContext);
			getSiteId(httpContext);
		}
	}
	
	public static  String updateUser(HttpContext httpContext){
		String eid ="test1";
		String firstName="zzztestFupdated"; 
		String lastName="testLupdated"; 
		String email="test1updated@yahoo.com"; 
		//String type="student"; 
		String password="yyyyy";
		UserOperations.updateUser(URL, httpContext, eid, firstName, lastName, email, password, null);
		return null;
	}
	public static  String createUser(HttpContext httpContext){
		String eid ="test1";
		String firstName="testF"; 
		String lastName="testL"; 
		String email="test1@yahoo.com"; 
		String type="student"; 
		String password="xxxxxx";
		return UserOperations.createUser(URL, httpContext, eid, firstName, lastName, email, password, type);
	}
	
	public static  String getUser(HttpContext httpContext){
		String eid ="student1";
		return UserOperations.getUser(URL, httpContext, eid);
	}
	public static  String deleteUser(HttpContext httpContext){
		String eid ="test1";
		return UserOperations.deleteUser(URL, httpContext, eid);
	}
	public static  String assignCouseToUser(HttpContext httpContext){
		String eid= "student1";
		String siteId = "/site/"+"066cad45-a9c7-4181-85a1-0136a0f5c963";
		String memberRole = "Student";
		return UserOperations.assignCouseToUser(URL, httpContext, siteId, eid, memberRole);
	}
	
	public static  String getSiteId(HttpContext httpContext){
		
		String siteName = "2017 ABC I ORG";
		return UserOperations.getSiteId(URL, httpContext, siteName);
	}
	public static  String  getUserCerifications(HttpContext httpContext){
		return null;
	}
	public static  String  getSiteCerifications(HttpContext httpContext){
		return null;
	}
}
