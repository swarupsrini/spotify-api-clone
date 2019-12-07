package com.csc301.profilemicroservice;

import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.stereotype.Repository;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.Values;

@Repository
public class PlaylistDriverImpl implements PlaylistDriver {

  Driver driver = ProfileMicroserviceApplication.driver;

  public static void InitPlaylistDb() {
    String queryStr;

    // try (Session session = ProfileMicroserviceApplication.driver.session()) {
    // try (Transaction trans = session.beginTransaction()) {
    // queryStr = "CREATE CONSTRAINT ON (nPlaylist:playlist) ASSERT exists(nPlaylist.plName)";
    // trans.run(queryStr);
    // trans.success();
    // }
    // session.close();
    // }
  }

  @Override
  public DbQueryStatus likeSong(String userName, String songId) {
    DbQueryStatus status = null;
    boolean song_exists = true;
    if (userName == null || songId == null) {
      status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      return status;
    }

    try (Session session = driver.session()) {
      StatementResult result =
          session.run("MATCH ( user1: profile{userName:{user}} )" + "RETURN user1",
              Values.parameters("user", userName));

      StatementResult result2 = session.run("MATCH (songs: song{songId:{Id}} )" + "RETURN songs",
          Values.parameters("Id", songId));

      if (!result.hasNext()) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
        return status;
      }
      if (!result2.hasNext()) {
        song_exists = false;
      }
    }

    try (Session session = driver.session()) {

      StatementResult result =
          session.run("MATCH ( : profile{userName:{user}})-[:created]->(type :playlist)"
              + "MATCH (uniq: song{songId:{Id}})" + "MATCH (type)-[relate:includes]->(uniq)"
              + "RETURN relate", Values.parameters("user", userName, "Id", songId));

      if (result.hasNext()) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
        return status;
      }
    }

    try (Session session = driver.session()) {
      if(song_exists == false) {
        try (Transaction tx = session.beginTransaction()) {
          tx.run(
              "CREATE (:song{songId: {Id}})",
              Values.parameters("Id", songId));
          tx.success();
        }
      }
      
      try (Transaction tx = session.beginTransaction()) {
        tx.run(
            "MATCH ( : profile{userName:{user}})-[:created]->(type :playlist)"
                + "MATCH (uniq: song{songId:{Id}})" + "CREATE (type)-[:includes]->(uniq)",
            Values.parameters("user", userName, "Id", songId));
        tx.success();
        status = new DbQueryStatus("increment", DbQueryExecResult.QUERY_OK);
      }
    }

    return status;
  }

  @Override
  public DbQueryStatus unlikeSong(String userName, String songId) {
    DbQueryStatus status = null;

    if (userName == null || songId == null) {
      status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
      return status;
    }

    try (Session session = driver.session()) {
      StatementResult result =
          session.run("MATCH ( user1: profile{userName:{user}} )" + "RETURN user1",
              Values.parameters("user", userName));
      if (!result.hasNext()) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
        return status;
      }
    }

    try (Session session = driver.session()) {

      StatementResult result =
          session.run("MATCH ( : profile{userName:{user}})-[:created]->(type :playlist)"
              + "MATCH (uniq: song{songId:{Id}})" + "MATCH (type)-[favourite: includes]->(uniq)"
              + "RETURN favourite", Values.parameters("user", userName, "Id", songId));

      if (!result.hasNext()) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
        return status;
      }
    }

    try (Session session = driver.session()) {

      try (Transaction tx = session.beginTransaction()) {
        tx.run("MATCH ( : profile{userName:{user}})-[:created]->(type :playlist)"
            + "MATCH (uniq: song{songId:{Id}})" + "MATCH (type)-[favourite: includes]->(uniq)"
            + "DELETE favourite", Values.parameters("user", userName, "Id", songId));
        tx.success();
        status = new DbQueryStatus("decrement", DbQueryExecResult.QUERY_OK);
      }
    }

    return status;
  }

  @Override
  public DbQueryStatus deleteSongFromDb(String songId) {
    DbQueryStatus status = null;
    if(songId == null) {
      status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_GENERIC);
      return status;
    }
    try (Session session = driver.session()) {
      StatementResult result2 = session.run("MATCH (songs: song{songId:{Id}}) RETURN songs",
          Values.parameters("Id", songId));
      
      if (!result2.hasNext()) {
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
        return status;
      }
    }
    
    try (Session session = driver.session()) {

      try (Transaction tx = session.beginTransaction()) {
        tx.run("MATCH ( songs: song{songId: {Id}}) DETACH DELETE songs",
            Values.parameters("Id", songId));
        tx.success();
        status = new DbQueryStatus("", DbQueryExecResult.QUERY_OK);
      }
    }

    return status;
  }
}
