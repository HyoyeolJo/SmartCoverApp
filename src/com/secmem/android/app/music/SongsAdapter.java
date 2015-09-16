package com.secmem.android.app.music;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import com.secmem.android.app.Data.MusicSongInfo;
import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.CommonInfo;
import com.secmem.android.app.coverappb.coverappb;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ParseException;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SongsAdapter extends BaseAdapter {
	
	private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	
	private ArrayList<String> mSongsIDList;
	private ArrayList<String> mSongsTitleList;
	private ArrayList<String> mSingerList;
	private ArrayList<Integer> mAlbumList;
	
	
	
	private String mSongsArtist;
	private String mAlbumTitle;
	private Context mContext;
	
	SongsAdapter(Context c) {
		// TODO Auto-generated constructor stub
		mContext = c;
		mSongsIDList = new ArrayList<String>();
		mSongsTitleList = new ArrayList<String>();
		mSingerList = new ArrayList<String>();
		mAlbumList = new ArrayList<Integer>();
		
		getAlbumMusicData(); 
	}
	
	public void setAlbum(String AlbumTitle){
		mAlbumTitle = AlbumTitle;
		mSongsIDList.clear();
		mSongsTitleList.clear();
		this.notifyDataSetChanged();
	//	getMusicInfo();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSongsIDList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public int getSongID(int position) {
		return Integer.parseInt(mSongsIDList.get(position));
	}
	
	public String getSongTitle(int position){
		return mSongsTitleList.get(position);
	}
	
	public String getSongArtist(){
		return mSongsArtist;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View listViewItem = convertView;
		
		if(listViewItem == null){
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listViewItem = vi.inflate(R.layout.songs_list, parent,false);
		}
		TextView tv = (TextView) listViewItem.findViewById(R.id.songtitle);	
		TextView singer = (TextView) listViewItem.findViewById(R.id.singer);
		ImageView album = (ImageView) listViewItem.findViewById(R.id.albumimg);
		
		Bitmap bb = getAlbumArt(coverappData.musicSongList.get(position).getAlbumID(),300,300);
		album.setImageBitmap(bb);

		
		tv.setText(coverappData.musicSongList.get(position).getMusicTitle());
		singer.setText(coverappData.musicSongList.get(position).getSinger());
		return listViewItem;
 
	}
	
	
	private void getAlbumMusicData(){
		String[] proj = {MediaStore.Audio.Media.ALBUM,
				 MediaStore.Audio.Media.ALBUM_ID,
				 MediaStore.Audio.Media.ARTIST,
				 MediaStore.Audio.Media.YEAR};
		String where = MediaStore.Audio.Media.DATA + " like ?";
		//String whereArgs[] = {"%/AlbumTest/%"};
		String whereArgs[] = {"%"};
		String order = MediaStore.Audio.Media.ALBUM;
		Cursor musicCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj,where,whereArgs,order);
		
		if(musicCursor != null && musicCursor.moveToFirst()){
				String albumTitle;
				
				int albumIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
				int albumTitleCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
				int albumArtistCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
				int albumYearCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
				albumTitle =  musicCursor.getString(albumTitleCol);
				//int albumTrackCol = musicCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
				
					
				do {
					albumTitle = musicCursor.getString(albumTitleCol);

					getMusicInfo(Integer.parseInt(musicCursor.getString(albumIDCol)),albumTitle);

				}while(musicCursor.moveToNext());
		}
		
	}
		
	
	
	private void getMusicInfo(int AlbumID, String AlbumTitle){

	      String[] proj = {MediaStore.Audio.Media.TITLE,
	                    MediaStore.Audio.Media._ID,
	                    MediaStore.Audio.Media.ARTIST};
	      String where = MediaStore.Audio.Media.DATA + " like ? and " + MediaStore.Audio.Media.ALBUM + " like ?";
	      String whereArgs[] = {"%",AlbumTitle};
	      String order = MediaStore.Audio.Media.TRACK;
	      Cursor musicCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj,where,whereArgs,order);
		
		if(musicCursor != null && musicCursor.moveToFirst()){
				String SongTitle;
				String SongID;

				int SongsIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
				int SongsTitleCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
				int SongsArtistCol =musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
				//int albumTrackCol = musicCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
				

				
				do {
					SongTitle = musicCursor.getString(SongsTitleCol);
					SongID = musicCursor.getString(SongsIDCol);
					
					mSongsArtist = musicCursor.getString(SongsArtistCol);
					
					boolean checkDuplicate = false;
					for(int i=0; i<mSongsIDList.size();i++){
						if(mSongsTitleList.get(i).equals(SongTitle) && mSingerList.get(i).equals(mSongsArtist)){
							checkDuplicate = true;
							break;
						}
					}
					
					if(!checkDuplicate){
						mSongsTitleList.add(SongTitle);
						mSingerList.add(mSongsArtist);
						mSongsIDList.add(SongID);
						mAlbumList.add(AlbumID);
						
						MusicSongInfo music = new MusicSongInfo();
						music.setMusicTitle(SongTitle);
						music.setSinger(mSongsArtist);
						music.setAlbumID(AlbumID);
						music.setSongID(SongID);
						
						
						coverappData.musicSongList.add(music);

					}
					
					
				
					
				}while(musicCursor.moveToNext());
			musicCursor.close();
		}
		
				
	}
	
public Bitmap getAlbumArt(int id,int w,int h){
		
		w-=2;
		h-=2;
		
		ContentResolver res = mContext.getContentResolver();
		Uri uri = ContentUris.withAppendedId(sArtworkUri, id);
		
		if(uri != null) {
			ParcelFileDescriptor fd = null;
			try {
				fd = res.openFileDescriptor(uri, "r");
				int sampleSize = 1;
				
				sBitmapOptionsCache.inJustDecodeBounds = true;
				BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);
				
				int nextWidth = sBitmapOptionsCache.outWidth >> 1;
				int nextHeight = sBitmapOptionsCache.outHeight >> 1;
				
				while(nextHeight > h && nextWidth > w){
					sampleSize <<= 1;
					nextWidth >>= 1;
					nextHeight >>= 1;
				}
				
				sBitmapOptionsCache.inSampleSize = sampleSize;
				sBitmapOptionsCache.inJustDecodeBounds = false;
				Bitmap b = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);
				
				if(b != null) {
					if(sBitmapOptionsCache.outWidth != w || sBitmapOptionsCache.outHeight != h){
						Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
	                    b.recycle();
	                    b = tmp;
					}
				}
				
				return b;
				
			}catch(FileNotFoundException e){}
			
			finally {
				try {
					if( fd != null)
						fd.close();
				}catch(IOException e){}
			}
		}
		
		return null;
	}
}
