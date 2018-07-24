package com.cc.purecloud.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cc.purecloud.client.model.CustomPresenceDefinition;
import com.cc.purecloud.util.Config;
import com.cc.purecloud.util.ConfigException;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.api.PresenceApi;
import com.mypurecloud.sdk.v2.model.OrganizationPresence;
import com.mypurecloud.sdk.v2.model.OrganizationPresenceEntityListing;

public class PresenceClient extends AbstractClient {
  
  public PresenceClient(String clientId, String clientSecret) {
    super(clientId, clientSecret);
  }
  
  
  public void run() {
    this.executeOrganizationPresenceListing();
  }
  
  public void executeSystemPresenceListing(String accessToken) {
    //TBD
  }
  
  public List<CustomPresenceDefinition> executeOrganizationPresenceListing() {
    this.initClient();
    PresenceApi apiInstance = new PresenceApi();
    Integer pageSize = 25; // Integer | Page size
    Integer pageNumber = 1; // Integer | Page number
    
    List<CustomPresenceDefinition> model = new ArrayList<CustomPresenceDefinition>();
    
    try {
      OrganizationPresenceEntityListing result = //apiInstance.getPresencedefinitions(pageNumber, pageSize, "false", "ALL");
      apiInstance.getPresencedefinitions(pageNumber, pageSize, "false", "en_US");
      List<OrganizationPresence> list = result.getEntities();
      
      for (OrganizationPresence presence : list) {
        model.add(new CustomPresenceDefinition(presence.getId(), presence.getLanguageLabels().get("en_US"), presence.getSystemPresence()));
        //System.out.println("Presence: " + presence.getSystemPresence() + "." + presence.getLanguageLabels().get("en_US") + "::" + presence.getId() + " primary?: " + presence.getPrimary());
      }
      
    } catch (ApiException e) {
      System.err.println("Exception when calling PresenceApi#PresenceDefinitions");
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
    
    return model;
  }
  
  public static void main(String[] args) {
    
    String clientId = null;
    String clientSecret = null;
    
    try {
      Config config = new Config("/Users/gmongelli/work/temp/batch.properties");
      clientId = (clientId == null || clientId.equals("") ? Config.get("purecloud.clientid") : clientId);
      clientSecret = (clientSecret == null || clientSecret.equals("") ? Config.get("purecloud.clientsecret") : clientSecret);
      PresenceClient client = new PresenceClient(clientId, clientSecret);
      List<CustomPresenceDefinition> model = client.executeOrganizationPresenceListing();
      
      for (CustomPresenceDefinition presence : model) {
        System.out.println(presence.getId() + " : " + presence.getSystemPresence() + "." + presence.getDesc());
      }
      //client.run();
    } catch(IOException e) {
      e.printStackTrace();
    } catch(ConfigException e) {
      e.printStackTrace();
    }
    
  }
}
