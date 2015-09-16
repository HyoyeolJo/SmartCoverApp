package com.secmem.android.app.ViewAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.secmem.android.app.Data.MusicSongInfo;
import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.R.id;
import com.secmem.android.app.coverappb.R.layout;
import com.secmem.android.app.coverappb.CommonInfo;
import com.secmem.android.app.view.MusicCDRoundedDrawable;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MusicSongPagerAdapter extends PagerAdapter {
	
	Context mContext;
	Handler handler;
	
	private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	
	private ArrayList<String> mSongsIDList;
	private ArrayList<String> mSongsTitleList;
	private ArrayList<String> mSingerList;
	private ArrayList<Integer> mAlbumList;
	
	private String mSongsArtist;
	private String mAlbumTitle;
	private RelativeLayout music_list;
	private int themeSet;
	MusicCDRoundedDrawable roundalbum;

	
	public MusicSongPagerAdapter(Context context, Handler handler,int themeset) {
		super();
		mContext = context;
		this.handler = handler;
		mSongsIDList = new ArrayList<String>();
		mSongsTitleList = new ArrayList<String>();
		mSingerList = new ArrayList<String>();
		mAlbumList = new ArrayList<Integer>();
		themeSet = themeset;
		getAlbumMusicData();
	}
	
	 
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
    	
		View v = new View(mContext);
		final LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
			v = inflater.inflate(R.layout.music_list_item, (ViewGroup) null, false);
			music_list = (RelativeLayout)v.findViewById(R.id.music_select_info);
			switch(themeSet)
			{
			case 0:
				music_list.setBackgroundResource(R.drawable.layout_bg_blue);
				break;
			case 1:
				music_list.setBackgroundResource(R.drawable.layout_bg_gray);
				break;
			case 2:
				music_list.setBackgroundResource(R.drawable.layout_bg_mint);
				break;
			case 3:
				music_list.setBackgroundResource(R.drawable.layout_bg_red);
				break;
			case 4:
				music_list.setBackgroundResource(R.drawable.layout_bg_yellow);
				break;
			default:
				break;
			}
			
			
			((TextView) v.findViewById(R.id.music_title_name)).setText(coverappData.musicSongList.get(position).getMusicTitle());
			((TextView) v.findViewById(R.id.music_title_name)).setSelected(true);
			((TextView) v.findViewById(R.id.music_singer_name)).setText(coverappData.musicSongList.get(position).getSinger());
			((TextView) v.findViewById(R.id.music_singer_name)).setSelected(true);
			Bitmap album = getAlbumArt(coverappData.musicSongList.get(position).getAlbumID(),300,300);
			if(album!=null){
				roundalbum = new MusicCDRoundedDrawable(album);
				((ImageView) v.findViewById(R.id.albumImage)).setImageDrawable(roundalbum);
			}
			
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(CommonInfo.TAG, "clicked : "+position);
					Bundle bundle = new Bundle();
					bundle.putInt("musicSongSelect", position);
					Message msg = new Message();
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			});
			
			
		Log.d(CommonInfo.TAG, "instantiateItem : "+position);
		Log.d(CommonInfo.TAG, "instantiateItem "+coverappData.musicSongList.get(position).getMusicTitle());
        container.addView(v);
        
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return coverappData.musicSongList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
    
    public void getAlbumMusicData(){
    	
		coverappData.musicSongList.clear();
		mSongsIDList.clear();
		mSongsTitleList.clear();
		mSingerList.clear();
		mAlbumList.clear();
    	
		String[] proj = {MediaStore.Audio.Media.ALBUM,
				 MediaStore.Audio.Media.ALBUM_ID,
				 MediaStore.Audio.Media.ARTIST,
				 MediaStore.Audio.Media.YEAR};
		String where = MediaStore.Audio.Media.DATA + " like ?";
		//String whereArgs[] = {"%/AlbumTest/%"};
		String whereArgs[] = {"%"};
		String order = MediaStore.Audio.Media.ALBUM;
		Cursor musicCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj,where,whereArgs,order);
//		Cursor musicCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj,null,null,order);
		
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

					updateMusicInfo(Integer.parseInt(musicCursor.getString(albumIDCol)),albumTitle);

				}while(musicCursor.moveToNext());
		}
		
	}
    
	private void updateMusicInfo(int AlbumID, String AlbumTitle){

	
		
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