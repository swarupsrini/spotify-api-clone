package com.csc301.profilemicroservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Values;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.stereotype.Repository;
import org.neo4j.driver.v1.Transaction;

@Repository
public class ProfileDriverImpl implements ProfileDriver {

  Driver driver = ProfileMicroserviceApplication.driver;

  public static void InitProfileDb() {
    String queryStr;

    // try (Session session = ProfileMicroserviceApplication.driver.session()) {
    // try (Transaction trans = session.beginTransaction()) {
    // queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT exists(nProfile.userName)";
    // trans.run(queryStr);
    //
    // queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT exists(nProfile.password)";
    // trans.run(queryStr);
    //
    // queryStr = "CREATE CONSTRAINT ON (nProfile:profile) ASSERT nProfile.userName IS UNIQUE";
    // trans.run(queryStr);
    //
    // trans.success();
    // }
    // session.close();
    // }

  }

  @Override
  public DbQueryStatus createUserProfile(String userName, String fullName, String password) {
    DbQueryStatus newProfile = null;
    if (userName == null || fullName == null || password == null) {
      newProfile = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      return newProfile;
    }

    try (Session session = driver.session()) {
      StatementResult result =
          session.run("MATCH (nProfile: profile{ userName: {user} }) RETURN nProfile",
              Values.parameters("user", userName));
      if (result.hasNext()) {
        newProfile = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_GENERIC);
        return newProfile;
      }
    }

    try (Session session = driver.session()) {

      try (Transaction tx = session.beginTransaction()) {
        tx.run(
            "CREATE (nProfile: profile{ userName: {user}, fullName: {name}, password: {pass} }) - [:created]->(nPlaylist: playlist)",
            Values.parameters("user", userName, "name", fullName, "pass", password));
        tx.success();
        newProfile = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
      }
    }
    return newProfile;
  }

  @Override
  public DbQueryStatus followFriend(String userName, String friendUserName) {
    DbQueryStatus status = null;

    if (userName == null || friendUserName == null || (userName == friendUserName)) {
      status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      return status;
    }
    try (Session session = driver.session()) {
      StatementResult result = session.run(
          "MATCH (user: profile{userName:{follower}}) -[r: follows]-> (friend: profile{userName:{followed}})"
              + "RETURN r",
          Values.parameters("follower", userName, "followed", friendUserName));

      if (result.hasNext()) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_GENERIC);
        return status;
      }
    }

    try (Session session = driver.session()) {
      StatementResult result = session.run(
          "MATCH (user: profile{userName:{follower}}), (friend: profile{userName:{followed}})"
              + "CREATE (user)-[r: follows]->(friend)" + "RETURN r",
          Values.parameters("follower", userName, "followed", friendUserName));

      if (result.hasNext()) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
        return status;
      } else {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
        return status;
      }
    }
  }

  @Override
  public DbQueryStatus unfollowFriend(String userName, String friendUserName) {
    DbQueryStatus status = null;

    if (userName == null || friendUserName == null || (userName == friendUserName)) {
      status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      return status;
    }

    try (Session session = driver.session()) {
      StatementResult result = session.run(
          "MATCH (user: profile{userName:{follower}}) -[r: follows]-> (friend: profile{userName:{followed}})"
              + "RETURN r",
          Values.parameters("follower", userName, "followed", friendUserName));

      if (!(result.hasNext())) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_GENERIC);
        return status;
      }
    }

    try (Session session = driver.session()) {
      try (Transaction tx = session.beginTransaction()) {
        tx.run(
            "MATCH (user: profile{userName:{follower}}) -[r: follows]-> (friend: profile{userName:{followed}})"
                + "DELETE r",
            Values.parameters("follower", userName, "followed", friendUserName));
        tx.success();
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
      }
    }
    return status;
  }

  @Override
  public DbQueryStatus getAllSongFriendsLike(String userName) {
    DbQueryStatus songList = null;
    ArrayList<String> data = new ArrayList<String>();

    if (userName == null) {
      songList = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      songList.setData(data);
      return songList;
    }

    try (Session session = driver.session()) {
      StatementResult result = session.run(
          "MATCH (me: profile{userName:{user}}) -[:follows]-> (: profile) -[: created]-> (:playlist) -[:includes]-> (songs: song)"
              + "RETURN DISTINCT songs",
          Values.parameters("user", userName));
      while (result.hasNext()) {

        Record song = result.next();
        data.add((String) song.fields().get(0).value().asMap().get("songId"));
      }
      songList = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
      songList.setData(data);
      return songList;
    }
  }
}
