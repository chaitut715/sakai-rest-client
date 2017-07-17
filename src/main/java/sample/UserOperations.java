package sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserOperations {
	final static String ENDPOINT_SESSION = "/direct/session/new";
	final static String ENDPOINT_USER = "/direct/user";
	final static String ENDPOINT_SITE = "/direct/site";
	final static String ENDPOINT_MEMBERSHIP = "/direct/membership";
	public static  HttpContext   getSession(String url,String username, String password){
		HttpContext httpContext = new BasicHttpContext();
		try{
			List<NameValuePair> params = new ArrayList<>();

	        params.add(new BasicNameValuePair("_username", username));
	        params.add(new BasicNameValuePair("_password", password));

	        //create httpclient
	        HttpClient httpClient = HttpClientBuilder.create().build();
	       
	        //store cookies in context
	        CookieStore cookieStore = new BasicCookieStore();
	        
	        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
	        
	        //call login web service
	        String webserviceURL = url+ENDPOINT_SESSION;
	        HttpPost postRequest = new HttpPost(webserviceURL);
	        postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
	        postRequest.setEntity(new UrlEncodedFormEntity(params));
	        HttpResponse response = httpClient.execute(postRequest, httpContext);
	        
	        //get the status code
	        int status = response.getStatusLine().getStatusCode();
	        //System.out.println("session status:"+status);
	        //if status code is 201 login is successful. So reuse the httpContext for next requests.
	        //if status code is not 201, there is a problem with the request.
	        if(status!=201)
	            return null;
	        EntityUtils.consume( response.getEntity() );
		}catch(Exception e){
			return null;
		}
		
		return httpContext;
	}
	
	public static  String createUser(String url, HttpContext httpContext, String eid, String firstName, String lastName, String email, String password, String type){
		HttpClient httpClient = HttpClientBuilder.create().build();
        String webserviceURL = url+ENDPOINT_USER;
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("eid", eid));
        params.add(new BasicNameValuePair("firstName", firstName));
        params.add(new BasicNameValuePair("lastName", lastName));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("type", type));
        HttpPost request = new HttpPost(webserviceURL);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        try {
			request.setEntity(new UrlEncodedFormEntity(params));
		
			HttpResponse response;
		
			response = httpClient.execute(request, httpContext);
			int status = response.getStatusLine().getStatusCode();
	        System.out.println("session status:"+status);
			if(status!=201)
	            return null;
			HttpEntity httpEntity = response.getEntity();
	        String responseVal = EntityUtils.toString(httpEntity);
	        System.out.println("responseVal:"+responseVal);
	        EntityUtils.consume(httpEntity);
	        return  responseVal;
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} 

        
	}
	public static boolean isNotEmpty(String s){
		if(s==null || s.isEmpty() || s.trim().isEmpty()){
			return false;
		}
		return true;
	}
	public static  String updateUser(String url, HttpContext httpContext, String id, String firstName, String lastName, String email, String password, String type){
		HttpClient httpClient = HttpClientBuilder.create().build();
        String webserviceURL = url+ENDPOINT_USER+"/"+id;
       JSONObject jsonObject = new JSONObject();
        if(isNotEmpty(id)){
        	jsonObject.put("id", id);
        	jsonObject.put("eid", id);
        	//params.add(new BasicNameValuePair("id", id));
        }else{
        	return null;
        }
        if(isNotEmpty(firstName)){
        	jsonObject.put("firstName", firstName);
        }
        if(isNotEmpty(lastName)){
        
        	jsonObject.put("lastName", lastName);
        }
        if(isNotEmpty(email)){
        	jsonObject.put("email", email);
        }
        if(isNotEmpty(password)){
        	jsonObject.put("password", password);
        }
        if(isNotEmpty(type)){
        	jsonObject.put("type", type);
        }
        String json = jsonObject.toString();
       
        HttpPut request = new HttpPut(webserviceURL);
        //request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        try {
        	request.setHeader("Accept", "application/json");
        	request.setHeader("Content-type", "application/json");
        	 StringEntity se = new StringEntity(json);
        	 request.setEntity(se);
		
			HttpResponse response;
		
			response = httpClient.execute(request, httpContext);
			int status = response.getStatusLine().getStatusCode();
	        System.out.println("session status:"+status);
	        //if status code is 204 update is successful.
	        if(status!=204)
	            return null;
	        return  id;
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} 

	}

	public static String getUser(String url, HttpContext httpContext, String id) {

		HttpClient httpClient = HttpClientBuilder.create().build();
		String webserviceURL = url + ENDPOINT_USER + "/" + id;

		if (!isNotEmpty(id)) {
			return null;
		}

		HttpGet request = new HttpGet(webserviceURL);
		try {
			request.setHeader("Accept", "application/json");
        	request.setHeader("Content-type", "application/json");
			HttpResponse response = httpClient.execute(request, httpContext);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("session status:" + status);
			// if status code is 200 get is successful.
			if (status != 200)
				return null;
			HttpEntity httpEntity = response.getEntity();
	        String responseVal = EntityUtils.toString(httpEntity);
	        System.out.println("responseVal:"+responseVal);
	        EntityUtils.consume(httpEntity);
	        return  responseVal;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}
	
	public static  String deleteUser(String url, HttpContext httpContext, String id){
		HttpClient httpClient = HttpClientBuilder.create().build();
        String webserviceURL = url+ENDPOINT_USER+"/"+id;
       
        if(!isNotEmpty(id)){
        	return null;
        }
       
        HttpDelete request = new HttpDelete(webserviceURL);
        try {
        	
		
			HttpResponse response = httpClient.execute(request, httpContext);
			int status = response.getStatusLine().getStatusCode();
	        System.out.println("session status:"+status);
	        //if status code is 204 update is successful.
	        if(status!=204)
	            return null;
	        return  id;
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} 
	}
	public static  String assignCouseToUser(String url, HttpContext httpContext, String siteId, String id, String memberRole){
		HttpClient httpClient = HttpClientBuilder.create().build();
        String webserviceURL = url+ENDPOINT_MEMBERSHIP;
        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("userId", id));
        params.add(new BasicNameValuePair("locationReference", siteId));
        params.add(new BasicNameValuePair("memberRole", memberRole));
        HttpPost request = new HttpPost(webserviceURL);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        try {
			request.setEntity(new UrlEncodedFormEntity(params));
		
			HttpResponse response;
		
			response = httpClient.execute(request, httpContext);
			int status = response.getStatusLine().getStatusCode();
	        System.out.println("session status:"+status);
			if(status!=201)
	            return null;
			HttpEntity httpEntity = response.getEntity();
	        String responseVal = EntityUtils.toString(httpEntity);
	        System.out.println("responseVal:"+responseVal);
	        EntityUtils.consume(httpEntity);
	        return  responseVal;
		}catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} 

	}
	//It will only work when site name is unique otherwise it returns null
	public static String getSiteId(String url, HttpContext httpContext, String SiteName) {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		
		if (!isNotEmpty(SiteName)) {
			return null;
		}
		
		try {
			String webserviceURL = url + ENDPOINT_SITE+".json?search="+URLEncoder.encode(SiteName, "UTF-8");
			System.out.println(webserviceURL);
			HttpGet request = new HttpGet(webserviceURL);
			request.setHeader("Accept", "application/json");
        	
			HttpResponse response = httpClient.execute(request, httpContext);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("session status:" + status);
			// if status code is 200 get is successful.
			if (status != 200)
				return null;
			HttpEntity httpEntity = response.getEntity();
	        String responseVal = EntityUtils.toString(httpEntity);
	        //System.out.println("responseVal:"+responseVal);
	        EntityUtils.consume(httpEntity);
	        
	        JSONParser jsonParser = new JSONParser();
	        JSONObject sites= (JSONObject)jsonParser.parse(responseVal);
	        JSONArray site_collection = (JSONArray) sites.get("site_collection");
	        
	        //either empty or multiple sites with same name
	        if(site_collection.size()==0 ||site_collection.size()>1 ){
	        	return null;
	        }
	        JSONObject site=  (JSONObject)site_collection.get(0);
	        System.out.println("siteId:"+site.get("entityId"));
	        return (String)site.get("entityId");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static  String  getUserCerifications(){
		return null;
	}
	public static  String  getSiteCerifications(){
		return null;
	}
	
}
