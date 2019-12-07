package com.csc301.profilemicroservice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.csc301.profilemicroservice.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class ProfileController {
  public static final String KEY_USER_NAME = "userName";
  public static final String KEY_USER_FULLNAME = "fullName";
  public static final String KEY_USER_PASSWORD = "password";

  @Autowired
  private final ProfileDriverImpl profileDriver;

  @Autowired
  private final PlaylistDriverImpl playlistDriver;

  OkHttpClient client = new OkHttpClient();

  public ProfileController(ProfileDriverImpl profileDriver, PlaylistDriverImpl playlistDriver) {
    this.profileDriver = profileDriver;
    this.playlistDriver = playlistDriver;
  }

  @RequestMapping(value = "/profile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addProfile(@RequestParam Map<String, String> params,
			HttpServletRequest request) {

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("path", String.format("POST %s", Utils.getUrl(request)));
        Utils.setResponseStatus(response,  profileDriver.createUserProfile(params.get("userName"), params.get("fullName"), params.get("password")).getdbQueryExecResult(), null);
        return response;
	}

  @RequestMapping(value = "/followFriend/{userName}/{friendUserName}", method = RequestMethod.PUT)
  public @ResponseBody Map<String, Object> followFriend(@PathVariable("userName") String userName,
      @PathVariable("friendUserName") String friendUserName, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));
    Utils.setResponseStatus(response, profileDriver.followFriend(userName, friendUserName).getdbQueryExecResult(), null);
    return response;
  }

  @RequestMapping(value = "/getAllFriendFavouriteSongTitles/{userName}", method = RequestMethod.GET)
  public @ResponseBody Map<String, Object> getAllFriendFavouriteSongTitles(
      @PathVariable("userName") String userName, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));

    DbQueryStatus songList = profileDriver.getAllSongFriendsLike(userName);
    
    if(songList.getdbQueryExecResult().equals(DbQueryExecResult.QUERY_ERROR_NOT_FOUND)) {
      
      Utils.setResponseStatus(response, songList.getdbQueryExecResult(), null);
      return response;
    }
    
    @SuppressWarnings("unchecked")
    Map<String, ArrayList<String>> data = (Map<String, ArrayList<String>>) songList.getData();
    System.out.println(data);
    
    for(String name: data.keySet()) {
      ArrayList<String> change = new ArrayList<String>();
      for(String songId: data.get(name)) {
        
        Request toSend = new Request.Builder()
            .url("http://localhost:3001/getSongTitleById/"+songId)
                .build();
        try (Response sentResp = this.client.newCall(toSend).execute()){
          String body = sentResp.body().string();
          JSONObject a = new JSONObject(body);
          change.add((String) a.get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        } 
      }
      data.replace(name, change);
    }
    Utils.setResponseStatus(response, songList.getdbQueryExecResult(), data);

    return response;
  }


  @RequestMapping(value = "/unfollowFriend/{userName}/{friendUserName}", method = RequestMethod.PUT)
  public @ResponseBody Map<String, Object> unfollowFriend(@PathVariable("userName") String userName,
      @PathVariable("friendUserName") String friendUserName, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));
    Utils.setResponseStatus(response, profileDriver.unfollowFriend(userName, friendUserName).getdbQueryExecResult(), null);
    return response;
  }

  @RequestMapping(value = "/likeSong/{userName}/{songId}", method = RequestMethod.PUT)
  public @ResponseBody Map<String, Object> likeSong(@PathVariable("userName") String userName,
      @PathVariable("songId") String songId, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));
    
    Request check = new Request.Builder()
        .url("http://localhost:3001/getSongById/"+songId)
            .build();
    try (Response sentResp = this.client.newCall(check).execute()){
      String body = sentResp.body().string();
      JSONObject a = new JSONObject(body);
      if(!(a.get("status").equals("OK"))) {
        Utils.setResponseStatus(response, DbQueryExecResult.QUERY_ERROR_NOT_FOUND, null);
        return response;
      }
    } catch (IOException e) {
        e.printStackTrace();
    } 
    
    
    
    DbQueryStatus stat = playlistDriver.likeSong(userName, songId);
    Utils.setResponseStatus(response, stat.getdbQueryExecResult(), null);
    
    if (stat.getMessage().equals("increment")) {
      Request toSend = new Request.Builder()
          .url("http://localhost:3001/updateSongFavouritesCount/"+songId+"?shouldDecrement=false")
              .put(new FormBody.Builder().build())
              .build();
      try (Response sentResp = this.client.newCall(toSend).execute()){
        String body = sentResp.body().string();
        JSONObject a = new JSONObject(body);
        if(!(a.get("status").equals("OK"))) {
          Utils.setResponseStatus(response, DbQueryExecResult.QUERY_ERROR_NOT_FOUND, null);
          return response;
        }
      } catch (IOException e) {
          e.printStackTrace();
      } 
    }
    
    return response;
  }

  @RequestMapping(value = "/unlikeSong/{userName}/{songId}", method = RequestMethod.PUT)
  public @ResponseBody Map<String, Object> unlikeSong(@PathVariable("userName") String userName,
      @PathVariable("songId") String songId, HttpServletRequest request) {
    
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));
    System.out.println("1");
    Request check = new Request.Builder()
        .url("http://localhost:3001/getSongById/"+songId)
            .build();
    try (Response sentResp = this.client.newCall(check).execute()){
      String body = sentResp.body().string();
      JSONObject a = new JSONObject(body);
      if(!(a.get("status").equals("OK"))) {
        Utils.setResponseStatus(response, DbQueryExecResult.QUERY_ERROR_NOT_FOUND, null);
        return response;
      }
      
      
    } catch (IOException e) {
        e.printStackTrace();
    } 
    DbQueryStatus stat = playlistDriver.unlikeSong(userName, songId);
    Utils.setResponseStatus(response, stat.getdbQueryExecResult(), null);
    
    if (stat.getMessage().equals("decrement")) {
      System.out.println("2");

      Request toSend = new Request.Builder()
          .url("http://localhost:3001/updateSongFavouritesCount/"+songId+"?shouldDecrement=true")
              .put(new FormBody.Builder().build())
              .build();
      try (Response sentResp = this.client.newCall(toSend).execute()){
        String body = sentResp.body().string();
        JSONObject a = new JSONObject(body);
        System.out.println(body);
        if(!(a.get("status").equals("OK"))) {
          Utils.setResponseStatus(response, DbQueryExecResult.QUERY_ERROR_NOT_FOUND, null);
          return response;
        }
      } catch (IOException e) {
          e.printStackTrace();
      } 
    }
    
    return response;
  }

  @RequestMapping(value = "/deleteAllSongsFromDb/{songId}", method = RequestMethod.PUT)
  public @ResponseBody Map<String, Object> deleteAllSongsFromDb(
      @PathVariable("songId") String songId, HttpServletRequest request) {

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("path", String.format("PUT %s", Utils.getUrl(request)));
    
//    Request check = new Request.Builder()
//        .url("http://localhost:3001/getSongById/"+songId)
//            .build();
//    try (Response sentResp = this.client.newCall(check).execute()){
//      String body = sentResp.body().string();
//      JSONObject a = new JSONObject(body);
//      if(!(a.get("status").equals("OK"))) {
//        Utils.setResponseStatus(response, DbQueryExecResult.QUERY_ERROR_NOT_FOUND, null);
//        return response;
//      }
//    } catch (IOException e) {
//        e.printStackTrace();
//    } 
    
    Utils.setResponseStatus(response, playlistDriver.deleteSongFromDb(songId).getdbQueryExecResult(), null);

    return response;
  }
}
