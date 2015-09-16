package com.secmem.android.app.music;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.coverappb.CommonInfo;
import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.R.drawable;
import com.secmem.android.app.coverappb.R.id;
import com.secmem.android.app.coverappb.R.layout;
import com.secmem.android.app.coverappb.coverappb;
import com.secmem.android.app.view.MusicCDRoundedDrawable;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


class RunningThread extends Thread {
  

	private MediaPlayer mPlayer;
	private MusicPlayer myPlayer;
	private SeekBar mSeek;
	private int LoopEnable;
	private Boolean isControlVisible;
	private WindowManager.LayoutParams mWindowParams;
	private WindowManager mWindowManager;
	private FrameLayout ControlPannel;
	private Handler mp_handler;
	
	public RunningThread(MediaPlayer m, Handler handler,MusicPlayer my) {
		// TODO Auto-generated constructor stub
		mPlayer = m;
		mp_handler = handler;
		myPlayer = my;
	}
	

	public void run() {    
		LoopEnable = 1;
		isControlVisible = false;
        while(LoopEnable == 1){
     	   try {
     		   Thread.sleep(50);
     	   } catch (InterruptedException e) {
     		   // TODO Auto-generated catch block
     		   e.printStackTrace();
     	   }
     	  
     	   if(LoopEnable == 1 && mPlayer.isPlaying())  {
     		   
     		  Bundle bundle = new Bundle();
			  bundle.putString("musicPlayer", ""+mPlayer.getCurrentPosition());
			  Message msg = new Message();
			  msg.setData(bundle);
			  mp_handler.sendMessage(msg);  
     	   }

        }
    }
	

	public void release(){		
		LoopEnable = 0;
	}
	
	public void setVisibleControl(Boolean b){
		isControlVisible = b;
	}
	
}

public class MusicPlayer {
	

	private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
	private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

	public static final int PLAY = 1;
	public static final int PAUSE = 2;
	public static final int STOP = 3;
	
	
	public View getPlayview() {
		return playview;
	}

	public View getMusic_infoview() {
		return music_infoview;
	}
	


	private Context mContext;
	
	private MediaPlayer mPlayer;
	private SeekBar mSeek;
	
	private final int LIST_REPEAT = 0;
	private final int SONG_REPEAT = 1;
	private final int RANDOM = 2;
	private int playState;
	
	
	private ArrayList<Integer> pList;
	private ArrayList<String> pListMusic;
	private String nowPlayingArist;
	
	private int nowPlayingPosition;


	private Boolean isControlVisible;
	
	private FrameLayout ControlPannel;
	private LinearLayout ControlLayout;

	
	
	private Animation scale_up_ani;
	private Animation scale_down_ani;
	
	private RunningThread t;
	
	
	
	private LayoutInflater inflater;
	Handler mp_handler;
	
	private View playview;
	private View music_infoview;
	
	public View return_music_info_view()
	{
		return music_info_layout;
	}
	
	ListView albumList;
	ListView songList;
	MusicAlbumAdapter mAlbumAdapter;
	SongsAdapter mSongsAdapter;
	
	RelativeLayout music_info_layout;
	ImageView music_play;
	ImageView music_play_background;
	ImageView music_before;
	ImageView music_next;
	ImageView right_option;
	ImageView left_none;
	
	ImageView music_shuffle;
	TextView music_song_name;
	TextView music_singer_name;
	
	Animation animation;
	
	private int music_duration;
	private int music_play_status;
	
	public void startMusicAnimation(){
		 music_play_background.setAnimation(null);
		 music_play_background.setVisibility(View.VISIBLE);
	     music_play_background.setAnimation(animation);
	     animation.start();
	} 
	
	public void stopMusicAnimation(){
		music_play_background.setAnimation(null);
		music_play_background.setVisibility(View.INVISIBLE);
	}
 
	
	public int getMusic_play_status() {
		return music_play_status;
	}

	public void setMusic_play_status(int music_play_status) {
		this.music_play_status = music_play_status;
	}
	
	public MediaPlayer getmPlayer() {
		return mPlayer;
	}


	public void setmPlayer(MediaPlayer mPlayer) {
		this.mPlayer = mPlayer;
	}
	
	
	public int getMusic_duration() {
		return music_duration;
	}

	public void setMusic_duration(int music_duration) {
		this.music_duration = music_duration;
	}

	boolean isSeletcedMusic=false;
	boolean isPlay = false;
	
	public void changePlayStatue(int resource){
		music_play.setImageResource(resource);
	}
	
	public int getNowPlayingPosition() {
		return nowPlayingPosition;
	}

	public void setNowPlayingPosition(int nowPlayingPosition) {
		this.nowPlayingPosition = nowPlayingPosition;
	}
	
	public void updateList(SongsAdapter s){
		pList.clear();
		pListMusic.clear();
		nowPlayingArist = s.getSongArtist();
		for(int i =0; i< s.getCount();i++){
			pList.add(s.getSongID(i));
			pListMusic.add(s.getSongTitle(i));
		}
	}
	
	public void setCurrentAlbumImage(){
		if(coverappData.musicSongList.size()!=0){
			Bitmap album = getAlbumArt(coverappData.musicSongList.get(getNowPlayingPosition()).getAlbumID(),300,300);
			if(album!=null){
				MusicCDRoundedDrawable roundalbum = new MusicCDRoundedDrawable(album);
				music_play.setImageDrawable(roundalbum);
			}else{
				music_play.setImageResource(R.drawable.icon_cd);
			}
		}else{
			music_play.setImageResource(R.drawable.icon_cd);
		}

	}
	
	public void startMusic(int position){
		nowPlayingPosition = position;
		
		
		Uri musicURI = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + String.valueOf(coverappData.musicSongList.get(nowPlayingPosition).getSongID())); 
		mPlayer.reset();
		
       try {
			mPlayer.setDataSource(mContext, musicURI);
			//
			mPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       setMusic_duration(mPlayer.getDuration());
       mPlayer.start();
       
       setCurrentAlbumImage();

       startMusicAnimation();
	}
	
	public void startNextMusic(){
		nowPlayingPosition++;
		if(nowPlayingPosition>=coverappData.musicSongList.size()) nowPlayingPosition=0;
		startMusic(nowPlayingPosition);
		
		Bundle bundle = new Bundle();
		bundle.putString("musicPlayer", "next");
		Message msg = new Message();
		msg.setData(bundle);
		mp_handler.sendMessage(msg); 
		
		setCurrentAlbumImage();
		
		startMusicAnimation();
	}
	
	public void startBeforeMusic(){
		nowPlayingPosition--;
		if(nowPlayingPosition<0) nowPlayingPosition= coverappData.musicSongList.size()-1;
		startMusic(nowPlayingPosition);
		
		Bundle bundle = new Bundle();
		bundle.putString("musicPlayer", "before");
		Message msg = new Message();
		msg.setData(bundle);
		mp_handler.sendMessage(msg); 
		
		setCurrentAlbumImage();
		startMusicAnimation();
	}
	
	public void resumeMusic(){
		mPlayer.start();
		
		setCurrentAlbumImage();
		
		startMusicAnimation();
	}
	public void pauseMusic(Boolean flag){
		mPlayer.pause();
		mSeek.setProgress(mPlayer.getCurrentPosition());

		stopMusicAnimation();
		changePlayStatue(R.drawable.playbutton);
	}
	
	public void stopMusic(){
		mPlayer.stop();
		mPlayer.release();
		stopMusicAnimation();
		changePlayStatue(R.drawable.playbutton);
	}
	
	public void stopMusic2(){
		mPlayer.stop();
		stopMusicAnimation();
		changePlayStatue(R.drawable.playbutton);
	}
	
	
	public void Release(){
		t.release();
		mPlayer.release();
	}
	
	public Boolean isInit(){
		if(pList.size() == 0 )
			return false;
		return true;
	}

	public Boolean isPlaying(){
		return mPlayer.isPlaying();
	}
	
	public MusicPlayer(Context context, Handler handler) {
		
		setMusic_play_status(MusicPlayer.STOP);
		
		mContext = context;
		mp_handler = handler; 
		
		pList = new ArrayList<Integer>();
		pListMusic = new ArrayList<String>();
		mPlayer = new MediaPlayer();
		
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				 Bundle bundle = new Bundle();
				  bundle.putString("musicPlayer", "end");
				  Message msg = new Message();
				  msg.setData(bundle);
				  mp_handler.sendMessage(msg); 
			}
		});
		
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		music_infoview = inflater.inflate(R.layout.music_info, null);
		
		mSeek = (SeekBar)music_infoview.findViewById(R.id.music_seekBar);
		playview = inflater.inflate(R.layout.musicpage, null);
		music_infoview = inflater.inflate(R.layout.music_info, null);
		music_play = (ImageView)playview.findViewById(R.id.music_play);
		music_play_background = (ImageView)playview.findViewById(R.id.music_play_background);
		animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
		animation.setStartOffset(0);
		
		music_play_background.setVisibility(View.INVISIBLE); 
	
 
		

		music_before = (ImageView)playview.findViewById(R.id.music_before);
		music_next = (ImageView)playview.findViewById(R.id.music_next);

		
		music_info_layout = (RelativeLayout)music_infoview.findViewById(R.id.music_info_layout);
		music_song_name = (TextView)music_infoview.findViewById(R.id.music_song_name);
		music_singer_name = (TextView)music_infoview.findViewById(R.id.music_singer_name);
		
		
		

		
		music_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(coverappData.musicSongList.size()>0){
					Bundle bundle = new Bundle();
					Message msg = new Message();
					
					switch (getMusic_play_status()) {
					case MusicPlayer.STOP:
		
						bundle.putString("musicPlayer", "start");
						msg.setData(bundle);
						mp_handler.sendMessage(msg);
						 
						break;
					case MusicPlayer.PAUSE:
		
						bundle.putString("musicPlayer", "resume");
						msg.setData(bundle);
						mp_handler.sendMessage(msg);
						
						break;
					case MusicPlayer.PLAY:
						changePlayStatue(R.drawable.playbutton);
						bundle.putString("musicPlayer", "pause");
						msg.setData(bundle);
						mp_handler.sendMessage(msg);
						break;
	
					default:
						break;
					}
				}
				
			}
		});
		
		music_before.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(coverappData.musicSongList.size()>0)
					startBeforeMusic();
				Log.d(CommonInfo.TAG, "music_before clicked :"+nowPlayingPosition);
			}
		});
		
		music_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(coverappData.musicSongList.size()>0)
					startNextMusic();
				Log.d(CommonInfo.TAG, "music_before clicked "+nowPlayingPosition);
				
			} 
		});
		
	

	
		
		t = new RunningThread(mPlayer,mp_handler,this);
		t.start();
	}
	public void setVisibleControl(boolean b,boolean c){
		if(b == false) {
			if(c == true)
				ControlPannel.setVisibility(View.GONE);
			else 
				ControlLayout.startAnimation(scale_down_ani);
		}
		else {
			ControlPannel.setVisibility(View.VISIBLE);
			ControlLayout.startAnimation(scale_up_ani);
		}
		isControlVisible = b;
		t.setVisibleControl(b);
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
