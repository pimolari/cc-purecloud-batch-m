package com.cc.purecloud.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cc.purecloud.client.model.CustomUser;
import com.cc.purecloud.util.Config;
import com.cc.purecloud.util.ConfigException;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.api.UsersApi;
import com.mypurecloud.sdk.v2.model.User;
import com.mypurecloud.sdk.v2.model.UserEntityListing;
import com.mypurecloud.sdk.v2.model.UserSearchCriteria;
import com.mypurecloud.sdk.v2.model.UserSearchRequest;
import com.mypurecloud.sdk.v2.model.UsersSearchResponse;

public class UserClient extends AbstractClient {
  
  public UserClient(String clientId, String clientSecret) {
    super(clientId, clientSecret);
  }
  
  
  public CustomUser get(String id) {
    return new CustomUser();
  }
  
  public List<CustomUser> list() {
    
    UsersApi apiInstance = new UsersApi();
    Integer pageSize = 50; // Integer | Page size
    Integer pageNumber = 1; // Integer | Page number
    //List<String> expand = Arrays.asList("presence", "station");
    
    try {
      UserEntityListing list = apiInstance.getUsers(pageSize, pageNumber, null, "ASC", null, "active");
      List<CustomUser> model = new ArrayList<CustomUser>();
      for (User user : list.getEntities()) {
        model.add(new CustomUser(user.getId(), user.getName()));
      }
      
      return model;
    } catch (ApiException e) {
      System.err.println("Exception when calling RoutingApi#getQueues");
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public List<CustomUser> search() {
    UsersApi apiInstance = new UsersApi();
    Integer pageSize = 25; // Integer | Page size
    Integer pageNumber = 1; // Integer | Page number
    //List<String> expand = Arrays.asList("presence", "station");
    
    try {
      UserSearchRequest body = new UserSearchRequest(); // UserSearchRequest | Search request options
      UserSearchCriteria criteria = new UserSearchCriteria();
      
      criteria.setFields(Arrays.asList("name"));
      criteria.setType(UserSearchCriteria.TypeEnum.CONTAINS);
      criteria.setValue("Pe");
      
      List<UserSearchCriteria> query = Arrays.asList(criteria);
      body.setQuery(query);
      
      UsersSearchResponse result = apiInstance.postUsersSearch(body);
      
      List<CustomUser> model = new ArrayList<CustomUser>();
      for (User user : result.getResults()) {
        model.add(new CustomUser(user.getId(), user.getName()));
      }
      
      return model;
      
    } catch (ApiException e) {
      System.err.println("Exception when calling RoutingApi#getQueues");
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  
  public static void main(String[] args) {
    
    String clientId = null;
    String clientSecret = null;
    
    try {
      Config config = new Config("/Users/gmongelli/work/temp/batch.properties");
      clientId = (clientId == null || clientId.equals("") ? Config.get("purecloud.clientid") : clientId);
      clientSecret = (clientSecret == null || clientSecret.equals("") ? Config.get("purecloud.clientsecret") : clientSecret);
      UserClient client = new UserClient(clientId, clientSecret);
      List<CustomUser> model = client.list();
      
      for (CustomUser user : model) {
        System.out.println(user.getId() + " : " + user.getName() + "");
      }
      //client.run();
    } catch(IOException e) {
      e.printStackTrace();
    } catch(ConfigException e) {
      e.printStackTrace();
    }
  }
}
