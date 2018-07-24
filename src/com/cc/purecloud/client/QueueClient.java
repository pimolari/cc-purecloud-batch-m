package com.cc.purecloud.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cc.purecloud.client.model.CustomQueue;
import com.cc.purecloud.client.model.CustomUser;
import com.cc.purecloud.util.Config;
import com.cc.purecloud.util.ConfigException;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.api.RoutingApi;
import com.mypurecloud.sdk.v2.api.UsersApi;
import com.mypurecloud.sdk.v2.model.Queue;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.User;
import com.mypurecloud.sdk.v2.model.UserEntityListing;
import com.mypurecloud.sdk.v2.model.UserSearchCriteria;
import com.mypurecloud.sdk.v2.model.UserSearchRequest;
import com.mypurecloud.sdk.v2.model.UsersSearchResponse;

public class QueueClient extends AbstractClient {
  
  public QueueClient(String clientId, String clientSecret) {
    super(clientId, clientSecret);
  }
  
  public List<CustomQueue> list() {
    
    RoutingApi apiInstance = new RoutingApi();
    Integer pageSize = 25; // Integer | Page size
    Integer pageNumber = 1; // Integer | Page number
    String sortBy = "id"; // String | Sort by
    //String name = "name_example"; // String | Name
    String name = null;
    Boolean active = true; // Boolean | Active
    try {
      QueueEntityListing result = apiInstance.getRoutingQueues(pageSize, pageNumber, sortBy, name, active);
      List<CustomQueue> model = new ArrayList<CustomQueue>();
      
      List<Queue> list = result.getEntities();
      
      if (list.size() == 0)
        System.out.println(" No queues retrieved");
      
      for (Queue queue : list) {
        model.add(new CustomQueue(queue.getId(), queue.getName()));
      }
      return model;
      //System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling RoutingApi#getQueues");
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public CustomQueue get(String id) {
    
    CustomQueue queue = new CustomQueue();
    
    return queue;
  }
  
  public static void main(String[] args) {
    
    String clientId = null;
    String clientSecret = null;
    
    try {
      Config config = new Config("/Users/gmongelli/work/temp/batch.properties");
      clientId = (clientId == null || clientId.equals("") ? Config.get("purecloud.clientid") : clientId);
      clientSecret = (clientSecret == null || clientSecret.equals("") ? Config.get("purecloud.clientsecret") : clientSecret);
      QueueClient client = new QueueClient(clientId, clientSecret);
      List<CustomQueue> model = client.list();
      
      if (model != null) {
        for (CustomQueue queue : model) {
          System.out.println(queue.getId() + " : " + queue.getName() + "");
        }
      }
      
      //client.run();
    } catch(IOException e) {
      e.printStackTrace();
    } catch(ConfigException e) {
      e.printStackTrace();
    }
  }
}
