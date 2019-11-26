package com.csc301.songmicroservice;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public class SongDalImpl implements SongDal {

	private final MongoTemplate db;

	@Autowired
	public SongDalImpl(MongoTemplate mongoTemplate) {
		this.db = mongoTemplate;
	}

	@Override
	public DbQueryStatus addSong(Song songToAdd) {
		DbQueryStatus status = new DbQueryStatus("Added song to DB", DbQueryExecResult.QUERY_OK);
//		this.db.insert(songToAdd);
		return status;
	}

	@Override
	public DbQueryStatus findSongById(String songId) {
		DbQueryStatus status = new DbQueryStatus("Found song from DB", DbQueryExecResult.QUERY_OK);
		Song res = this.db.findById(songId, Song.class);
//		List<Song> res = this.db.find(new Query(where("_id").is(songId)), Song.class);
//		System.out.println(res);
		if (res != null) {
			status.setData(res);
		}
		else {
			status.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			status.setMessage("Song ID not found in DB");
		}
		return status;
	}

	@Override
	public DbQueryStatus getSongTitleById(String songId) {
		DbQueryStatus status = new DbQueryStatus("Found song title from DB", DbQueryExecResult.QUERY_OK);
		Song res = this.db.findById(songId, Song.class);
		if (res != null) {
			status.setData(res.getSongName());
		}
		else {
			status.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			status.setMessage("Song ID not found in DB");
		}
		return status;
	}

	@Override
	public DbQueryStatus deleteSongById(String songId) {
		DbQueryStatus status = new DbQueryStatus("Deleted song from DB", DbQueryExecResult.QUERY_OK);
		Song res = this.db.findById(songId, Song.class);
		System.out.println(res);
		if (res != null) {
			this.db.remove(res);
		}
		else {
			status.setdbQueryExecResult(DbQueryExecResult.QUERY_ERROR_NOT_FOUND);
			status.setMessage("Song ID not found in DB");
		}
		return status;
	}

	@Override
	public DbQueryStatus updateSongFavouritesCount(String songId, boolean shouldDecrement) {
		// TODO Auto-generated method stub
		return null;
	}
}