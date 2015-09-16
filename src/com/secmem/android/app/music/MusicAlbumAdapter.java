package com.secmem.android.app.music;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;








import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.CommonInfo;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MusicAlbumAdapter extends BaseAdapter {
	
	private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
	
	private ArrayList<String> mAlbumIDList;
	private ArrayList<String> mAlbumTitleList;
	private ArrayList<String> mAlbumYearList;
	private ArrayList<String> mAlbumSongsList;
	private ArrayList<String> mAlbumArtistList;
	private Context mContext;
	
	public MusicAlbumAdapter(Context c) {
		// TODO Auto-generated constructor stub
		mContext = c;
		mAlbumIDList = new ArrayList<String>();
		mAlbumTitleList = new ArrayList<String>();
		mAlbumYearList = new ArrayList<String>();
		mAlbumSongsList = new ArrayList<String>();
		mAlbumArtistList = new ArrayList<String>();
		getMusicInfo();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAlbumTitleList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View listViewItem = convertView;
		
		if(listViewItem == null){
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listViewItem = vi.inflate(R.layout.album_list, parent,false);
		}
		ImageView iv = (ImageView)listViewItem.findViewById(R.id.albumart);
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

		Bitmap albumArt = getAlbumArt(Integer.parseInt((mAlbumIDList.get(position))), 300, 300);

		if(albumArt == null) {

			albumArt = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.notalbumart);
			//iv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 300));

			iv.setScaleType(ImageView.ScaleType.FIT_XY);

		}
		
		
		iv.setImageBitmap(albumArt);
		//TextView tv = (TextView)listViewItem.findViewById(R.id.albumItem);
		//tv.setText(mAlbumTitleList.get(position));
		
		return listViewItem;
	}
	
	private void getMusicInfo() {
		/* 찾을 쿼리문 작성 */
		
		String[] proj = {MediaStore.Audio.Media.ALBUM,
						 MediaStore.Audio.Media.ALBUM_ID,
						 MediaStore.Audio.Media.ARTIST,
						 MediaStore.Audio.Media.YEAR};
		String where = MediaStore.Audio.Media.DATA + " like ?";
//		String whereArgs[] = {"%/AlbumTest/%"};
		String whereArgs[] = {"%"};
		String order = MediaStore.Audio.Media.ALBUM;
		Cursor musicCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj,where,whereArgs,order);
		//Cursor musicCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj,null,null,order);
		
		
		if(musicCursor != null && musicCursor.moveToFirst()){
				String albumTitle;
				
				int albumIDCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
				int albumTitleCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
				int albumArtistCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
				int albumYearCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.YEAR);
				//int albumTrackCol = musicCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
				Log.d(CommonInfo.TAG,  albumIDCol+":"+albumTitleCol+":"+albumArtistCol+":"+albumYearCol);
				
				do {
					albumTitle = musicCursor.getString(albumTitleCol);
					if( mAlbumTitleList.contains(albumTitle) == false) {
						mAlbumTitleList.add(albumTitle);
						mAlbumIDList.add(musicCursor.getString(albumIDCol));
						mAlbumArtistList.add(musicCursor.getString(albumArtistCol));
						mAlbumYearList.add(musicCursor.getString(albumYearCol));
						mAlbumSongsList.add("1");
						//mAlbumSongsList.add(musicCursor.getString(albumTrackCol));
					}
					else {
						int tPos = mAlbumTitleList.indexOf(albumTitle);
						int tmp = Integer.parseInt(mAlbumSongsList.get(tPos)) + 1;
						mAlbumSongsList.set(tPos, Integer.toString(tmp));
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
	
	public String[] getListData(int pos){
		String[] rString= {mAlbumIDList.get(pos),mAlbumTitleList.get(pos),mAlbumArtistList.get(pos),mAlbumYearList.get(pos),mAlbumSongsList.get(pos)};
		return rString;
	}
	
}
