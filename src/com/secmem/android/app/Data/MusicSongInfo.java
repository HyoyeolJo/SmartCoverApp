package com.secmem.android.app.Data;

import android.graphics.drawable.Drawable;

public class MusicSongInfo {
	


	private String MusicTitle;
	private String Singer;
	private String MusicTime;
	private int AlbumID;
	private String SongID;
	private Drawable albumImage;
	
	public String getSongID() {
		return SongID;
	}
	public void setSongID(String songID) {
		SongID = songID;
	}
	public int getAlbumID() {
		return AlbumID;
	}
	public void setAlbumID(int albumID) {
		AlbumID = albumID;
	}
	public String getMusicTitle() {
		return MusicTitle;
	}
	public void setMusicTitle(String musicTitle) {
		MusicTitle = musicTitle;
	}
	public String getSinger() {
		return Singer;
	}
	public void setSinger(String singer) {
		Singer = singer;
	}
	public String getMusicTime() {
		return MusicTime;
	}
	public void setMusicTime(String musicTime) {
		MusicTime = musicTime;
	}
	public Drawable getAlbumImage() {
		return albumImage;
	}
	public void setAlbumImage(Drawable albumImage) {
		this.albumImage = albumImage;
	}
}
