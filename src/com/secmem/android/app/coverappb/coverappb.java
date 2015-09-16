package com.secmem.android.app.coverappb;

import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore.Audio;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.cover.Scover;
import com.samsung.android.sdk.cover.ScoverManager;
import com.samsung.android.sdk.cover.ScoverManager.StateListener;
import com.secmem.android.app.Data.MessageInfo;
import com.secmem.android.app.Data.NotiInfo;
import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.ViewAdapter.ArcAnimationSelectPagerAdapter;
import com.secmem.android.app.ViewAdapter.ArcAnimationSelectPagerContainer;
import com.secmem.android.app.ViewAdapter.FavoriteCallPagerAdapter;
import com.secmem.android.app.ViewAdapter.FavoriteCallPagerContainer;
import com.secmem.android.app.ViewAdapter.HomeMenuPagerAdapter;
import com.secmem.android.app.ViewAdapter.HomeMenuPagerContainer;
import com.secmem.android.app.ViewAdapter.IronThemeSelectPagerAdapter;
import com.secmem.android.app.ViewAdapter.IronThemeSelectPagerContainer;
import com.secmem.android.app.ViewAdapter.MessagePagerAdapter;
import com.secmem.android.app.ViewAdapter.MessagePagerContainer;
import com.secmem.android.app.ViewAdapter.MusicSongPagerAdapter;
import com.secmem.android.app.ViewAdapter.MusicSongPagerContainer;
import com.secmem.android.app.ViewAdapter.NotiPagerAdapter;
import com.secmem.android.app.ViewAdapter.NotificationPagerContainer;
import com.secmem.android.app.ViewAdapter.ZoomOutPageTransformer;
import com.secmem.android.app.music.MusicPlayer;
import com.secmem.android.app.util.ColorPicker;
import com.secmem.android.app.util.ColorPicker.OnColorChangedListener;
import com.secmem.android.app.util.OpacityBar;
import com.secmem.android.app.util.SaturationBar;
import com.secmem.android.app.util.ValueBar;
import com.secmem.android.app.view.AlarmView;
import com.secmem.android.app.view.ArcMenu;
import com.secmem.android.app.view.VerticalViewPager;

public class coverappb extends Service {

	

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		String action = intent.getAction();
		
		if(action!=null){
			
		
		if("music_noti_play".equals(action)){
			if(coverappData.musicSongList.size()>0){
				Bundle bundle = new Bundle();
				Message msg = new Message();
				
				switch (musicPlayer.getMusic_play_status()) {
				case MusicPlayer.STOP:
					bundle.putString("musicPlayer", "start");
					msg.setData(bundle);
					mv_handler.sendMessage(msg);
					 
					break;
				case MusicPlayer.PAUSE:

					bundle.putString("musicPlayer", "resume");
					msg.setData(bundle);
					mv_handler.sendMessage(msg);
					
					break;
				case MusicPlayer.PLAY:

					musicPlayer.changePlayStatue(R.drawable.playbutton);
					bundle.putString("musicPlayer", "pause");
					msg.setData(bundle);
					mv_handler.sendMessage(msg);
					break;

				default:
					break;
				}
			}
			
			music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
			music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
			
			Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
			if(album!=null){
				music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
			}
			
			notiManager.notify(1, music_noti);
			
		}else if("music_before_play_noti".equals(action)){
			if(coverappData.musicSongList.size()>0)
				musicPlayer.startBeforeMusic();
			
			music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
			music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
			
			Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
			if(album!=null){
				music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
			}
			
			notiManager.notify(1, music_noti);

		}else if("music_next_play_noti".equals(action)){
			
			if(coverappData.musicSongList.size()>0)
				musicPlayer.startNextMusic();
			
			music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
			music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
			
			Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
			if(album!=null){
				music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
			}
			
			notiManager.notify(1, music_noti);
		}else if("music_noti_stop".equals(action)){
			if(coverappData.musicSongList.size()>0){
				
				musicPlayer.stopMusic2();
				musicSeek.setProgress(0);
				musicPlayer.changePlayStatue(R.drawable.playbutton);
				musicPlayer.setMusic_play_status(MusicPlayer.STOP);
				Log.d(CommonInfo.TAG, "Music PLAY End!!!");
				updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
						null);
				
			}
			
			music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
			music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
			
			Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
			if(album!=null){
				music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
			}
			notiManager.cancel(1);
			
		}
		
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	 RemoteViews music_noti_remoteView;
	public RemoteViews getMusic_noti_remoteView() {
		return music_noti_remoteView;
	}
	Notification music_noti;
	
	SoundPool mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

	private GestureDetector mGesDetector;
	SwipeMenuEventHandler homeMenu_handler;
	SettingArcMenuHandler settingArcMenuHandler;
	MessageNotificationEventHandler messageNotification_handler;
	FavoriteCallSelectEventHandler favoriteCallSelectEventHandler;
	MusicSongSelectEventHandler musicSongSelectEventHandler;
	FavoriteCallHandler fv_handler;
	MusicPlayerHandler mv_handler;
	ArcAnimationSelectEventHandler arcAnimation_handler;
	IronThemeSelectEventHandler ironThemeSelect_handler;
	Handler ramInfoHandler;

	Handler animationHandler;
	protected Scover mScover;
	protected ScoverManager mCoverManager;

	private LayoutInflater inflater;

	/* 커버 뷰를 구성하는 화면 객체 */
	private ArrayList<View> coverViewList;
	private ServiceDecorView mCoverMainView;

	public ServiceDecorView getmCoverMainView() {
		return mCoverMainView;
	}

	private View home_info;
	private TextView ram_info_text;
	private RelativeLayout iman_maskViewLayoutView;
	private RelativeLayout ironImgLayout;
	private RelativeLayout home_infoLayout;
	private RelativeLayout view_hotkeypager;
	private RelativeLayout view_messagepager;
	private RelativeLayout home_menuLayout;
	private RelativeLayout arc1_battery_layout;
	private RelativeLayout arc2_battery_layout;
	private RelativeLayout arc3_battery_layout;
	private RelativeLayout arc4_battery_layout;
	private RelativeLayout arc6_battery_layout;
	private RelativeLayout arc_shield_layout;
	private RelativeLayout arc7_battery_layout;
	private RelativeLayout favoriteCallLayout;
	private RelativeLayout notificationLayout;
	private RelativeLayout message_menuLayout;
	private RelativeLayout music_select_itemLayout;
	private RelativeLayout music_submenuLayout;
	private RelativeLayout noti_menuLayout;
	private RelativeLayout custom_clockLayout;
	private RelativeLayout pop_upLayout;
	private RelativeLayout music_equalizerLayout;
	private RelativeLayout arcAnimationSelectLayout;
	private RelativeLayout ironThemeSelectLayout;

	private RelativeLayout eye_effectLayout;

	private RelativeLayout setting_menu_Layout;
	private RelativeLayout iman_mask_settingLayout;
	private RelativeLayout callingLayout;

	private RelativeLayout coverAlarmViewLayout;
	
	NotificationManager notiManager;
	
	View pop_up;
	Button cancelbtn;
	Button okbtn;
	
	
	TextView calling_person;
	TextView calling_phone;
	ArcMenu arcMenu;

	ImageView arc_1;
	ImageView arc_2;
	ImageView arc_inner;
	ImageView arc_outer;

	ImageView arc2_basic_alpha;
	ImageView arc2_outer_alpha;
	ImageView arc2_blue_alpha;
	ImageView arc2_red_alpha;
	ImageView arc2_yello_alpha;

	ImageView arc3_inner_logo;
	ImageView arc3_mid;
	ImageView arc3_outer_alpha;
	ImageView arc3_rotate;

	ImageView arc4_outer_alpha;
	ImageView arc4_gear_mid;
	ImageView arc4_gear1;
	ImageView arc4_gear2;
	ImageView arc4_gear3;
	ImageView arc4_gear4;
	ImageView arc4_gear5;
	ImageView arc4_gear6;
	ImageView arc4_main_gear;
	ImageView arc4_inner;

	ImageView arc6_solar;
	ImageView arc6_inner;
	ImageView arc6_outer;
	
	ImageView arc_shield;
	ImageView arc_shield_alpha;
	Animation arc_shield_animation;
	Animation arc_shield_animation_alpha;	
	
	
	ImageView arc7_background;
	ImageView arc7_outer;
	ImageView arc7_main_torr;
	ImageView arc7_light;
	
	Animation arc7_light_animation;
	Animation arc7_outer_animation;

	Animation arc6_solar_animation;
	Animation arc6_inner_animation;
	Animation arc6_outer_animation;

	AnimationDrawable callingAnimation;

	ImageView eyeImage;
	ImageView iman_maskView;
	int maskColorBefore = -1124094976;

	ImageView iman_maskImage;
	ImageView ironMan;
	Drawable maskIcon;

	ColorPicker picker;

	int ArcAnimationType = 0;
	int IronThemeType = 0;

	Animation arc_1_animation;
	Animation arc_2_animation;
	Animation arc_inner_animation;
	Animation arc_outer_animation;

	Animation arc2_basic_animation;
	Animation arc2_outer_animation;
	Animation arc2_blue_animation;
	Animation arc2_red_animation;
	Animation arc2_yello_animation;

	Animation arc3_outer_alpha_animation;
	Animation arc3_rotate_animation;

	Animation arc4_outer_alpha_animation;
	Animation arc4_gear_mid_animation;
	Animation arc4_gear1_animation;
	Animation arc4_gear2_animation;
	Animation arc4_gear3_animation;
	Animation arc4_gear4_animation;
	Animation arc4_gear5_animation;
	Animation arc4_gear6_animation;
	Animation arc4_main_gear_animation;
	Animation arc4_inner_animation;

	Animation eye_alpha_animation;
	Animation easter_egg_animation;

	private AnimationDrawable music_equalizer_frameAnimation;

	SeekBar musicSeek;
	View music_info_view;
	View music_player;
	MusicPlayer musicPlayer;

	private ViewPager HotKeyPager;
	private ViewPager MessagePager;
	private ViewPager MusicSongPager;
	private ViewPager FavoriteCallPager;
	private ViewPager NotificationPager;
	private ViewPager ArcAnimationSelectPager;
	private ViewPager IronThemeSelectPager;
	private VerticalViewPager NotiPager;
	private ImageView current_notification_alarm;

	HomeMenuPagerContainer mHomeMenuContainer;
	FavoriteCallPagerContainer mFavoriteCallPagerContainer;
	NotificationPagerContainer mNotificationPagerContainer;
	MessagePagerContainer mMessageContainer;
	MessagePagerAdapter messageAdapter;
	MusicSongPagerAdapter musicSongPagerAdapter;
	FavoriteCallPagerAdapter favoriteCallPagerAdapter;
	MusicSongPagerContainer musicSongPagerContainer;
	ArcAnimationSelectPagerContainer mArcAnimationSelectPagerContainer;
	IronThemeSelectPagerContainer mIronThemeSelectPagerContainer;
	// NotiPagerAdapter notiAdapter;
	NotiPagerAdapter notification_Adapter;
	boolean isfirstNotificationMessage = true;;

	boolean animationOpenMenuTimingCheck = true;
	boolean animationCloseMenuTimingCheck = true;

	private View favorite_call_layout;
	private RelativeLayout music_layout;
	private Display display;
	private String batteryValue;
	private ImageView view;
	TextView touchTxt;
	private Context mContext;
	private int CurrentPage;
	private int music_before_count = 0;
	private TableLayout tab;
	private Drawable r;

	Animation menu_open_animation;
	Animation menu_close_animation;
	Animation home_info_animation;
	Toast toast;

	boolean isSpeacker = true;

	SimpleDateFormat formatter;
	
	int[] soundId;
	
	long startTime;
	long endTime;

	int touchCount;
	/*
	 * 에니메이션 작업
	 */

	private void getSystemInfo() {
		ramInfoHandler = new Handler();
		ramcheck.run();
	}

	Runnable ramcheck = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				RandomAccessFile reader;
				int total = 0;
				int avail = 0;
				int buff = 0;
				int cached = 0;
				int rest = 0;
				int totalram = 0;
				int freeram = 0;
				int useram = 0;
				reader = new RandomAccessFile("/proc/meminfo", "r");
				Pattern p = Pattern.compile("[0-9]+");

				Matcher m = p.matcher(reader.readLine());
				m.find();
				total = Integer.parseInt(m.group());

				m = p.matcher(reader.readLine());
				m.find();
				avail = Integer.parseInt(m.group());

				m = p.matcher(reader.readLine());
				m.find();
				buff = Integer.parseInt(m.group());

				m = p.matcher(reader.readLine());
				m.find();
				cached = Integer.parseInt(m.group());
				totalram = total / 1024;
				freeram = (avail + buff + cached) / 1024;
				useram = totalram - freeram;

				rest = (int) (((double) useram / (double) totalram) * 100L);

				ram_info_text.setText(rest + "%");

				ramInfoHandler.postDelayed(ramcheck, 2000);
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void startArcAnimation(int ArcAnimationType) {
		switch (ArcAnimationType) {
		case 0:
			updateViewVisibility(arc1_battery_layout);
			arc_1.setVisibility(View.VISIBLE);
			arc_2.setVisibility(View.VISIBLE);
			arc_inner.setVisibility(View.VISIBLE);
			arc_outer.setVisibility(View.VISIBLE);

			arc_1.setAnimation(null);
			arc_2.setAnimation(null);
			arc_inner.setAnimation(null);
			arc_outer.setAnimation(null);

			arc_1_animation.cancel();
			arc_2_animation.cancel();
			arc_inner_animation.cancel();
			arc_outer_animation.cancel();

			arc_1_animation.reset();
			arc_2_animation.reset();
			arc_inner_animation.reset();
			arc_outer_animation.reset();

			arc_1_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc1_rotate);
			arc_1_animation.setStartOffset(0);
			arc_2_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc2_rotate);
			arc_2_animation.setStartOffset(0);
			arc_inner_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc_inner_alpha);
			arc_inner_animation.setStartOffset(0);
			arc_outer_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc_outer_alpha);
			arc_outer_animation.setStartOffset(0);

			arc_1.startAnimation(arc_1_animation);
			arc_2.startAnimation(arc_2_animation);
			arc_inner.setAnimation(arc_inner_animation);
			arc_outer.setAnimation(arc_outer_animation);


			break;
		case 1:
			updateViewVisibility(arc2_battery_layout);
			arc2_basic_alpha.setVisibility(View.VISIBLE);
			arc2_outer_alpha.setVisibility(View.VISIBLE);
			arc2_blue_alpha.setVisibility(View.VISIBLE);
			arc2_red_alpha.setVisibility(View.VISIBLE);
			arc2_yello_alpha.setVisibility(View.VISIBLE);

			arc2_basic_alpha.setAnimation(null);
			arc2_outer_alpha.setAnimation(null);
			arc2_blue_alpha.setAnimation(null);
			arc2_red_alpha.setAnimation(null);
			arc2_yello_alpha.setAnimation(null);

			arc2_basic_animation.cancel();
			arc2_outer_animation.cancel();
			arc2_blue_animation.cancel();
			arc2_red_animation.cancel();
			arc2_yello_animation.cancel();

			arc2_basic_animation.reset();
			arc2_outer_animation.reset();
			arc2_blue_animation.reset();
			arc2_red_animation.reset();
			arc2_yello_animation.reset();

			arc2_basic_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc2_basic_alpha);
			arc2_basic_animation.setStartOffset(0);
			arc2_outer_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc2_outer_alpha);
			arc2_outer_animation.setStartOffset(0);
			arc2_blue_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc2_blue_alpha);
			arc2_blue_animation.setStartOffset(0);
			arc2_red_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc2_red_alpha);
			arc2_red_animation.setStartOffset(0);
			arc2_yello_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc2_yello_alpha);
			arc2_yello_animation.setStartOffset(0);
			arc2_basic_alpha.setAnimation(arc2_basic_animation);
			arc2_outer_alpha.setAnimation(arc2_outer_animation);
			arc2_blue_alpha.setAnimation(arc2_blue_animation);
			arc2_red_alpha.setAnimation(arc2_red_animation);
			arc2_yello_alpha.setAnimation(arc2_yello_animation);
			break;
		case 2:
			updateViewVisibility(arc3_battery_layout);
			arc3_inner_logo.setVisibility(View.VISIBLE);
			arc3_mid.setVisibility(View.VISIBLE);
			arc3_outer_alpha.setVisibility(View.VISIBLE);
			arc3_rotate.setVisibility(View.VISIBLE);

			arc3_inner_logo.setAnimation(null);
			arc3_mid.setAnimation(null);
			arc3_outer_alpha.setAnimation(null);
			arc3_rotate.setAnimation(null);

			arc3_outer_alpha_animation.cancel();
			arc3_rotate_animation.cancel();

			arc3_outer_alpha_animation.reset();
			arc3_rotate_animation.reset();

			arc3_outer_alpha_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc3_outer_alpha);
			arc3_outer_alpha_animation.setStartOffset(0);
			arc3_rotate_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc3_rotate);
			arc3_rotate_animation.setStartOffset(0);

			arc3_outer_alpha.setAnimation(arc3_outer_alpha_animation);
			arc3_rotate.setAnimation(arc3_rotate_animation);

			ObjectAnimator animation = ObjectAnimator.ofFloat(arc3_inner_logo,
					"rotationY", 0.0f, 360f);
			animation.setDuration(2000);
			animation.setRepeatCount(ObjectAnimator.INFINITE);
			animation.setInterpolator(new LinearInterpolator());
			animation.start();

			break;
		case 3:
			updateViewVisibility(arc4_battery_layout);
			arc4_gear1.setVisibility(View.VISIBLE);
			arc4_gear2.setVisibility(View.VISIBLE);
			arc4_gear3.setVisibility(View.VISIBLE);
			arc4_gear4.setVisibility(View.VISIBLE);
			arc4_gear5.setVisibility(View.VISIBLE);
			arc4_gear6.setVisibility(View.VISIBLE);
			arc4_gear_mid.setVisibility(View.VISIBLE);
			arc4_inner.setVisibility(View.VISIBLE);
			arc4_main_gear.setVisibility(View.VISIBLE);
			arc4_outer_alpha.setVisibility(View.VISIBLE);

			arc4_gear1.setAnimation(null);
			arc4_gear2.setAnimation(null);
			arc4_gear3.setAnimation(null);
			arc4_gear4.setAnimation(null);
			arc4_gear5.setAnimation(null);
			arc4_gear6.setAnimation(null);
			arc4_gear_mid.setAnimation(null);
			arc4_inner.setAnimation(null);
			arc4_main_gear.setAnimation(null);
			arc4_outer_alpha.setAnimation(null);

			arc4_gear1_animation.cancel();
			arc4_gear2_animation.cancel();
			arc4_gear3_animation.cancel();
			arc4_gear4_animation.cancel();
			arc4_gear5_animation.cancel();
			arc4_gear6_animation.cancel();
			arc4_main_gear_animation.cancel();
			arc4_inner_animation.cancel();
			arc4_outer_alpha_animation.cancel();
			arc4_gear_mid_animation.cancel();

			arc4_gear1_animation.reset();
			arc4_gear2_animation.reset();
			arc4_gear3_animation.reset();
			arc4_gear4_animation.reset();
			arc4_gear5_animation.reset();
			arc4_gear6_animation.reset();
			arc4_main_gear_animation.reset();
			arc4_inner_animation.reset();
			arc4_gear_mid_animation.reset();
			arc4_outer_alpha_animation.reset();

			arc4_outer_alpha_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_outer_alpha);
			arc4_outer_alpha_animation.setStartOffset(0);
			arc4_gear_mid_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_gear_mid);
			arc4_gear_mid_animation.setStartOffset(0);
			arc4_gear1_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_gear1);
			arc4_gear1_animation.setStartOffset(0);
			arc4_gear2_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_gear2);
			arc4_gear2_animation.setStartOffset(0);
			arc4_gear3_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_gear3);
			arc4_gear3_animation.setStartOffset(0);
			arc4_gear4_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_gear4);
			arc4_gear4_animation.setStartOffset(0);
			arc4_gear5_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_gear5);
			arc4_gear5_animation.setStartOffset(0);
			arc4_gear6_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_gear6);
			arc4_gear6_animation.setStartOffset(0);
			arc4_main_gear_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_main_gear);
			arc4_main_gear_animation.setStartOffset(0);
			arc4_inner_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc4_outer_alpha);
			arc4_inner_animation.setStartOffset(0);

			arc4_gear_mid.setAnimation(arc4_gear_mid_animation);
			arc4_outer_alpha.setAnimation(arc4_outer_alpha_animation);
			arc4_gear1.setAnimation(arc4_gear1_animation);
			arc4_gear2.setAnimation(arc4_gear2_animation);
			arc4_gear3.setAnimation(arc4_gear3_animation);
			arc4_gear4.setAnimation(arc4_gear4_animation);
			arc4_gear5.setAnimation(arc4_gear5_animation);
			arc4_gear6.setAnimation(arc4_gear6_animation);
			arc4_inner.setAnimation(arc4_inner_animation);
			arc4_main_gear.setAnimation(arc4_main_gear_animation);

			break;
		case 4:
			updateViewVisibility(arc6_battery_layout);
			arc6_inner.setVisibility(View.VISIBLE);
			arc6_outer.setVisibility(View.VISIBLE);
			arc6_solar.setVisibility(View.VISIBLE);

			arc6_inner.setAnimation(null);
			arc6_outer.setAnimation(null);
			arc6_solar.setAnimation(null);

			arc6_inner_animation.cancel();
			arc6_outer_animation.cancel();
			arc6_solar_animation.cancel();

			arc6_inner_animation.reset();
			arc6_outer_animation.reset();
			arc6_solar_animation.reset();

			arc6_inner_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc6_inner);
			arc6_inner_animation.setStartOffset(0);

			arc6_outer_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc6_outer_alpha);
			arc6_outer_animation.setStartOffset(0);

			arc6_solar_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc6_solar);
			arc6_solar_animation.setStartOffset(0);

			arc6_inner.setAnimation(arc6_inner_animation);
			arc6_outer.setAnimation(arc6_outer_animation);
			arc6_solar.setAnimation(arc6_solar_animation);

			break;
		case 5:
			updateViewVisibility(arc_shield_layout);
			arc_shield.setVisibility(View.VISIBLE);
			arc_shield.setAnimation(null);
			
			arc_shield_animation.cancel();
			arc_shield_animation.reset();
			
			
			arc_shield_animation_alpha.cancel();
			arc_shield_animation_alpha.reset();


			arc_shield_animation = AnimationUtils.loadAnimation(mContext,
					R.anim.arc_sheild_rotate);
			arc_shield_animation.setStartOffset(0);
			
			arc_shield_animation_alpha = AnimationUtils.loadAnimation(mContext, R.anim.arc_sheild_alpha);
			arc_shield_animation_alpha.setStartOffset(0);
			

			arc_shield.setAnimation(arc_shield_animation);
			arc_shield_alpha.setAnimation(arc_shield_animation_alpha);
			
			break;
		case 6:
			updateViewVisibility(arc7_battery_layout);
			
			arc7_outer.setVisibility(View.VISIBLE);
			arc7_outer.setAnimation(null);
			
			arc7_light.setVisibility(View.VISIBLE);
			arc7_light.setAnimation(null);
			
			arc7_outer_animation.cancel();
			arc7_outer_animation.reset();
			
			arc7_light_animation.cancel();
			arc7_light_animation.reset();
			
			
			arc7_outer_animation = AnimationUtils.loadAnimation(mContext,R.anim.arc7_outer_alpha);
			arc7_outer_animation.setStartOffset(0);
			
			arc7_light_animation = AnimationUtils.loadAnimation(mContext, R.anim.arc7_light);
			arc7_light_animation.setStartOffset(0);
			

			arc7_outer.setAnimation(arc7_outer_animation);
			arc7_light.setAnimation(arc7_light_animation);
			
			break;
			

		default:
			break;
		}

	}

	public void endArcAnimation() {
		arc_1.setAnimation(null);
		arc_2.setAnimation(null);
		arc_inner.setAnimation(null);
		arc_outer.setAnimation(null);

		arc_1_animation.cancel();
		arc_2_animation.cancel();
		arc_inner_animation.cancel();
		arc_outer_animation.cancel();

		arc_1_animation.reset();
		arc_2_animation.reset();
		arc_inner_animation.reset();
		arc_outer_animation.reset();

		arc_1.setVisibility(View.GONE);
		arc_2.setVisibility(View.GONE);
		arc_inner.setVisibility(View.GONE);
		arc_outer.setVisibility(View.GONE);

		arc2_basic_alpha.setAnimation(null);
		arc2_outer_alpha.setAnimation(null);
		arc2_blue_alpha.setAnimation(null);
		arc2_red_alpha.setAnimation(null);
		arc2_yello_alpha.setAnimation(null);

		arc2_basic_animation.cancel();
		arc2_outer_animation.cancel();
		arc2_blue_animation.cancel();
		arc2_red_animation.cancel();
		arc2_yello_animation.cancel();

		arc2_basic_animation.reset();
		arc2_outer_animation.reset();
		arc2_blue_animation.reset();
		arc2_red_animation.reset();
		arc2_yello_animation.reset();

		arc2_basic_alpha.setVisibility(View.GONE);
		arc2_outer_alpha.setVisibility(View.GONE);
		arc2_blue_alpha.setVisibility(View.GONE);
		arc2_red_alpha.setVisibility(View.GONE);
		arc2_yello_alpha.setVisibility(View.GONE);

		arc3_inner_logo.setAnimation(null);
		arc3_mid.setAnimation(null);
		arc3_outer_alpha.setAnimation(null);
		arc3_rotate.setAnimation(null);

		arc3_outer_alpha_animation.cancel();
		arc3_rotate_animation.cancel();

		arc3_outer_alpha_animation.reset();
		arc3_rotate_animation.reset();

		arc3_inner_logo.setVisibility(View.GONE);
		arc3_mid.setVisibility(View.GONE);
		arc3_outer_alpha.setVisibility(View.GONE);
		arc3_rotate.setVisibility(View.GONE);

		eyeImage.setAnimation(null);
		eye_alpha_animation.cancel();
		eye_alpha_animation.reset();
		
		easter_egg_animation.cancel();
		easter_egg_animation.reset();;

		eyeImage.setVisibility(View.GONE);
		animationOpenMenuTimingCheck = true;
		animationCloseMenuTimingCheck = true;
		mCoverMainView.setEnabled(true);

		arcMenu.getmHintView().setEnabled(true);
		arcMenu.getmArcLayout().setEnabled(true);
		arcMenu.getControlLayout().setEnabled(true);
		int itemCount = arcMenu.getmArcLayout().getChildCount();
		for (int i = 0; i < itemCount; i++) {
            View item = arcMenu.getmArcLayout().getChildAt(i);
            item.setEnabled(true);
        }
		mIronThemeSelectPagerContainer.setCoverCheck(true);
        

	}

	/*
	 * 배터리 정보 접근 세터,게터
	 */
	public String getBatteryValue() {
		return batteryValue;
	}

	public void setBatteryValue(String batteryValue) {
		this.batteryValue = batteryValue;
	}

	/**
	 * Hot Key 모드에서 Swipe 전환에 따른 메뉴 선택시 화면 업데이트에 대한 Handler 구현
	 * 
	 */

	class SwipeMenuEventHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String command = bundle.getString("menu");
			Log.d(CommonInfo.TAG, command);

			touchTxt.setText(command);
			if (command.equals("sms") && animationOpenMenuTimingCheck
					&& animationCloseMenuTimingCheck) {
				updateCoverView(CommonInfo.TYPE_SMS_UPDATE, null);
			} else if (command.equals("call") && animationOpenMenuTimingCheck
					&& animationCloseMenuTimingCheck) {
				updateCoverView(CommonInfo.TYPE_FAVORITECALL_UPDATE, null);
			} else if (command.equals("music") && animationOpenMenuTimingCheck
					&& animationCloseMenuTimingCheck) {
				updateCoverView(CommonInfo.TYPE_MUSIC_UPDATE, null);
			} else if (command.equals("setting")
					&& animationOpenMenuTimingCheck
					&& animationCloseMenuTimingCheck) {
				updateCoverView(CommonInfo.TYPE_SETTING_UPDATE, null);
			} else if (command.equals("custom") && animationOpenMenuTimingCheck
					&& animationCloseMenuTimingCheck) {
				updateCoverView(CommonInfo.TYPE_CUSTOM_UPDATE, null);
			}
			playSounds(1);
			eyeImage.startAnimation(eye_alpha_animation);

		}
	}

	class SettingArcMenuHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String command = bundle.getString("ArcMenu");
			if (command.equals("menuClose")) {
				updateCoverView(CommonInfo.TYPE_HOME_MENU, null);
			} else if (command.equals("menuOpen")) {

			} else {
				int selectedIdx = Integer.parseInt(command);
				touchTxt.setText("ArcMenu :" + selectedIdx);
				switch (selectedIdx) {
				case 1:
					updateCoverView(CommonInfo.TYPE_ARCANIMATION_MENU_UPDATE, null);
					break;
				case 2:
					updateCoverView(CommonInfo.TYPE_IRONTHEME_MENU_UPDATE, null);
					break;
				case 3:
					updateCoverView(CommonInfo.TYPE_MASKSETTING_UPDATE, null);
					break;
				default:
					break;
				}
			}
			
			eyeImage.startAnimation(eye_alpha_animation);

		}

	}

	/**
	 * 뮤직플레이어 화면에 구성된 widget에 대한 Event 발생 시 화면을 업데이트 하기 위한 Handler 구현
	 * 
	 */
	class MusicPlayerHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			String command = bundle.getString("musicPlayer");
			touchTxt.setText(command);

			if (coverappData.musicSongList.size() > 0) {

				if (command.equals("start")) {
					musicPlayer.startMusic(musicSongPagerContainer
							.getSelectedIndex());
					musicSeek.setProgress(0);
					musicSeek.setMax(musicPlayer.getMusic_duration());
					String totalPlayTime = (String) formatter
							.format(new Timestamp(musicPlayer
									.getMusic_duration()));
					((TextView) music_info_view
							.findViewById(R.id.music_totalTime))
							.setText(totalPlayTime);
					musicPlayer.setMusic_play_status(MusicPlayer.PLAY);
					updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
							null);
					
					music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
					music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
					
					Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
					if(album!=null){
						music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
					}
					notiManager.notify(1, music_noti);
				} else if (command.equals("resume")) {
					music_noti_remoteView.setImageViewResource(R.id.music_noti_play, R.drawable.stopbutton);
					musicPlayer.resumeMusic();
					musicPlayer.setMusic_play_status(MusicPlayer.PLAY);
					updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
							null);
					
					music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
					music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
					
					Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
					if(album!=null){
						music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
					}
					notiManager.notify(1, music_noti);
				} else if (command.equals("pause")) {
					music_noti_remoteView.setImageViewResource(R.id.music_noti_play, R.drawable.playbutton);
					musicPlayer.pauseMusic(true);
					musicPlayer.setMusic_play_status(MusicPlayer.PAUSE);
					
					updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
							null);
					//notiManager.cancel(1);
					notiManager.notify(1, music_noti);
				} else if (command.equals("end")) {
					musicSeek.setProgress(0);
					musicPlayer.changePlayStatue(R.drawable.playbutton);
					musicPlayer.setMusic_play_status(MusicPlayer.STOP);
					Log.d(CommonInfo.TAG, "Music PLAY End!!!");
					musicPlayer.startNextMusic();
					updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
							null);
					notiManager.cancel(1);
				} else if (command.equals("next")) {
					musicSeek.setProgress(0);
					// musicPlayer.changePlayStatue(R.drawable.stopbutton);
					musicSeek.setMax(musicPlayer.getMusic_duration());
					String totalPlayTime = (String) formatter
							.format(new Timestamp(musicPlayer
									.getMusic_duration()));
					((TextView) music_info_view
							.findViewById(R.id.music_totalTime))
							.setText(totalPlayTime);
					musicPlayer.setMusic_play_status(MusicPlayer.PLAY);

					((TextView) music_info_view
							.findViewById(R.id.music_song_name))
							.setText(coverappData.musicSongList.get(
									musicPlayer.getNowPlayingPosition())
									.getMusicTitle());
					((TextView) music_info_view
							.findViewById(R.id.music_singer_name))
							.setText(coverappData.musicSongList.get(
									musicPlayer.getNowPlayingPosition())
									.getSinger());
					updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
							null);
					
					music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
					music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
					
					Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
					if(album!=null){
						music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
					}
					notiManager.notify(1, music_noti);
				} else if (command.equals("before")) {
					musicSeek.setProgress(0);

					musicSeek.setMax(musicPlayer.getMusic_duration());
					String totalPlayTime = (String) formatter
							.format(new Timestamp(musicPlayer
									.getMusic_duration()));
					((TextView) music_info_view
							.findViewById(R.id.music_totalTime))
							.setText(totalPlayTime);
					musicPlayer.setMusic_play_status(MusicPlayer.PLAY);

					((TextView) music_info_view
							.findViewById(R.id.music_song_name))
							.setText(coverappData.musicSongList.get(
									musicPlayer.getNowPlayingPosition())
									.getMusicTitle());
					((TextView) music_info_view
							.findViewById(R.id.music_singer_name))
							.setText(coverappData.musicSongList.get(
									musicPlayer.getNowPlayingPosition())
									.getSinger());
					updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
							null);
					
					music_noti_remoteView.setTextViewText(R.id.music_noti_song, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getMusicTitle());
					music_noti_remoteView.setTextViewText(R.id.music_noti_singer, coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getSinger());
					
					Bitmap album = musicSongPagerAdapter.getAlbumArt(coverappData.musicSongList.get(musicPlayer.getNowPlayingPosition()).getAlbumID(),300,300);
					if(album!=null){
						music_noti_remoteView.setImageViewBitmap(R.id.music_album_noti, album);
					}
					notiManager.notify(1, music_noti);
				} else {
					int progress = Integer.parseInt(command);
					musicSeek.setProgress(progress);
					String currentPlaytime = (String) formatter
							.format(new Timestamp(progress));
					touchTxt.setText(currentPlaytime);
					((TextView) music_info_view
							.findViewById(R.id.music_currentTime))
							.setText(currentPlaytime);
				}
			}

		}
	}

	class FavoriteCallSelectEventHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String phone = bundle.getString("favoriteCallPhone");
			String name = bundle.getString("favoriteCallName");

			updateCoverView(CommonInfo.TYPE_CALLING_UPDATE, null);
			calling_phone.setText(phone);
			calling_person.setText(name);

		}
	}

	class MusicSongSelectEventHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			int selectedIndex = bundle.getInt("musicSongSelect");
			Log.d(CommonInfo.TAG, "selected Index : " + selectedIndex);
			touchTxt.setText("selected Index : " + selectedIndex);

			music_select_itemLayout.setVisibility(View.GONE);
			music_layout.setVisibility(View.VISIBLE);
			music_player.setVisibility(View.VISIBLE);
			((TextView) music_info_view.findViewById(R.id.music_song_name))
					.setTextColor(Color.RED);
			((TextView) music_info_view.findViewById(R.id.music_singer_name))
					.setTextColor(Color.WHITE);

			((SeekBar) music_info_view.findViewById(R.id.music_seekBar))
					.setVisibility(View.VISIBLE);
			((TextView) music_info_view.findViewById(R.id.music_currentTime))
					.setVisibility(View.VISIBLE);
			((TextView) music_info_view.findViewById(R.id.music_totalTime))
					.setVisibility(View.VISIBLE);

			((TextView) music_info_view.findViewById(R.id.music_song_name))
					.setText(coverappData.musicSongList.get(selectedIndex)
							.getMusicTitle());
			((TextView) music_info_view.findViewById(R.id.music_singer_name))
					.setText(coverappData.musicSongList.get(selectedIndex)
							.getSinger());
			music_info_view.setVisibility(View.VISIBLE);
			musicPlayer.startMusic(selectedIndex);
			// musicPlayer.changePlayStatue(R.drawable.stopbutton);

			musicPlayer.setMusic_play_status(MusicPlayer.PLAY);
			musicSeek.setProgress(0);
			musicSeek.setMax(musicPlayer.getMusic_duration());

			String totalPlayTime = (String) formatter.format(new Timestamp(
					musicPlayer.getMusic_duration()));
			((TextView) music_info_view.findViewById(R.id.music_totalTime))
					.setText(totalPlayTime);
			
			updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE, null);
		}
	} 

	class MessageNotificationEventHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String value = bundle.getString("messageNotification");
			Log.d(CommonInfo.TAG, value);

			touchTxt.setText(value);
			if (value.equals("")) {

			}

		}
	}

	/**
	 * 즐겨찾기 화면에 구성된 widget에 대한 Event 발생 시 화면을 업데이트 하기 위한 Handler 구현
	 * 
	 */
	class FavoriteCallHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			String value = bundle.getString("fv_event");
			touchTxt.setText(value);

			/*
			 * 즐겨찾기 화면에서 뒤로 가기 버튼 클릭에 대한 화면 처리
			 */
			if (value.equals("back_event")) {
				CurrentPage = CommonInfo.TYPE_HOME_MENU;
				home_menuLayout.setVisibility(View.VISIBLE);
				favorite_call_layout.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * 설정메뉴에서 아이언맨 아크 에니메이션 테마 설정에 대한 Handler
	 * 
	 */

	class ArcAnimationSelectEventHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String command = bundle.getString("arcAnimation");

			ArcAnimationType = Integer.parseInt(command);
			touchTxt.setText("아크 배터리 테마 적용: " + ArcAnimationType);
			playSounds(2);
			eyeImage.startAnimation(eye_alpha_animation);
			updateCoverView(CommonInfo.TYPE_HOME_UPDATE, null);

		}
	}

	class IronThemeSelectEventHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			String command = bundle.getString("ironTheme");

			IronThemeType = Integer.parseInt(command);
			touchTxt.setText("아이언맨 테마 적용: " + IronThemeType);
			eyeImage.startAnimation(eye_alpha_animation);
			switch (IronThemeType) {
			case 0:
				ironMan.setImageResource(R.drawable.blue_iron_man);
				iman_maskView.setImageResource(R.drawable.mask_blue_iron_man);
				iman_maskView.setColorFilter(-1059132929,
						PorterDuff.Mode.MULTIPLY);
				mNotificationPagerContainer
						.setBackgroundResource(R.drawable.layout_bg_blue);
				maskColorBefore = -1059132929;
				notification_Adapter.returnmessagelistView().setBackgroundResource(R.drawable.layout_bg_blue);
				musicPlayer.return_music_info_view().setBackgroundResource(R.drawable.layout_bg_blue);
				musicSongPagerAdapter = new MusicSongPagerAdapter(mContext,
						musicSongSelectEventHandler,0);
				MusicSongPager.setAdapter(musicSongPagerAdapter);
				favoriteCallPagerAdapter = new FavoriteCallPagerAdapter(mContext,
						favoriteCallSelectEventHandler,0);
				FavoriteCallPager.setAdapter(favoriteCallPagerAdapter);
				((RelativeLayout)pop_up.findViewById(R.id.pop_up_layout)).setBackgroundResource(R.drawable.layout_bg_blue);
				okbtn.setBackgroundResource(R.drawable.button_left_layout_bg_blue);
				cancelbtn.setBackgroundResource(R.drawable.button_right_layout_bg_blue);
				messageAdapter = new MessagePagerAdapter(mContext,
						messageNotification_handler,0);

				MessagePager.setAdapter(messageAdapter);
				break;
			case 1:
				ironMan.setImageResource(R.drawable.gray_iron_man);
				iman_maskView.setImageResource(R.drawable.mask_gray_iron_man);
				iman_maskView.setColorFilter(2029056240,
						PorterDuff.Mode.MULTIPLY);
				mNotificationPagerContainer
						.setBackgroundResource(R.drawable.layout_bg_gray);
				maskColorBefore = 2029056240;
				notification_Adapter.returnmessagelistView().setBackgroundResource(R.drawable.layout_bg_gray);
				musicPlayer.return_music_info_view().setBackgroundResource(R.drawable.layout_bg_gray);
				musicSongPagerAdapter = new MusicSongPagerAdapter(mContext,
						musicSongSelectEventHandler,1);
				MusicSongPager.setAdapter(musicSongPagerAdapter);
				favoriteCallPagerAdapter = new FavoriteCallPagerAdapter(mContext,
						favoriteCallSelectEventHandler,1);
				FavoriteCallPager.setAdapter(favoriteCallPagerAdapter);
				((RelativeLayout)pop_up.findViewById(R.id.pop_up_layout)).setBackgroundResource(R.drawable.layout_bg_gray);
				okbtn.setBackgroundResource(R.drawable.button_left_layout_bg_gray);
				cancelbtn.setBackgroundResource(R.drawable.button_right_layout_bg_gray);
				messageAdapter = new MessagePagerAdapter(mContext,
						messageNotification_handler,1);

				MessagePager.setAdapter(messageAdapter);
				break;
			case 2:
				ironMan.setImageResource(R.drawable.mint_iron_man);
				iman_maskView.setImageResource(R.drawable.mask_mint_iron_man);
				iman_maskView.setColorFilter(1235245623,
						PorterDuff.Mode.MULTIPLY);
				mNotificationPagerContainer
						.setBackgroundResource(R.drawable.layout_bg_mint);
				maskColorBefore = 1235245623;
				notification_Adapter.returnmessagelistView().setBackgroundResource(R.drawable.layout_bg_mint);
				musicPlayer.return_music_info_view().setBackgroundResource(R.drawable.layout_bg_mint);
				musicSongPagerAdapter = new MusicSongPagerAdapter(mContext,
						musicSongSelectEventHandler,2);
				MusicSongPager.setAdapter(musicSongPagerAdapter);
				favoriteCallPagerAdapter = new FavoriteCallPagerAdapter(mContext,
						favoriteCallSelectEventHandler,2);
				FavoriteCallPager.setAdapter(favoriteCallPagerAdapter);
				((RelativeLayout)pop_up.findViewById(R.id.pop_up_layout)).setBackgroundResource(R.drawable.layout_bg_mint);
				okbtn.setBackgroundResource(R.drawable.button_left_layout_bg_mint);
				cancelbtn.setBackgroundResource(R.drawable.button_right_layout_bg_mint);
				messageAdapter = new MessagePagerAdapter(mContext,
						messageNotification_handler,2);

				MessagePager.setAdapter(messageAdapter);
				break;
			case 3:
				ironMan.setImageResource(R.drawable.red_iron_man);
				iman_maskView.setImageResource(R.drawable.mask_red_iron_man);
				iman_maskView.setColorFilter(-1124094976, PorterDuff.Mode.MULTIPLY);
				mNotificationPagerContainer
						.setBackgroundResource(R.drawable.layout_bg_red);
				maskColorBefore = -1124094976;
				notification_Adapter.returnmessagelistView().setBackgroundResource(R.drawable.layout_bg_red);
				musicPlayer.return_music_info_view().setBackgroundResource(R.drawable.layout_bg_red);
				musicSongPagerAdapter = new MusicSongPagerAdapter(mContext,
						musicSongSelectEventHandler,3);
				MusicSongPager.setAdapter(musicSongPagerAdapter);
				favoriteCallPagerAdapter = new FavoriteCallPagerAdapter(mContext,
						favoriteCallSelectEventHandler,3);
				FavoriteCallPager.setAdapter(favoriteCallPagerAdapter);
				((RelativeLayout)pop_up.findViewById(R.id.pop_up_layout)).setBackgroundResource(R.drawable.layout_bg_red);
				okbtn.setBackgroundResource(R.drawable.button_left_layout_bg_red);
				cancelbtn.setBackgroundResource(R.drawable.button_right_layout_bg_red);
				messageAdapter = new MessagePagerAdapter(mContext,
						messageNotification_handler,3);

				MessagePager.setAdapter(messageAdapter);
				break;
			case 4:
				ironMan.setImageResource(R.drawable.yellow_iron_man);
				iman_maskView.setImageResource(R.drawable.mask_yellow_iron_man);
				iman_maskView.setColorFilter(737214404,
						PorterDuff.Mode.MULTIPLY);
				mNotificationPagerContainer
						.setBackgroundResource(R.drawable.layout_bg_yellow);
				maskColorBefore = 737214404;
				notification_Adapter.returnmessagelistView().setBackgroundResource(R.drawable.layout_bg_yellow);
				musicPlayer.return_music_info_view().setBackgroundResource(R.drawable.layout_bg_yellow);
				musicSongPagerAdapter = new MusicSongPagerAdapter(mContext,
						musicSongSelectEventHandler,4);
				MusicSongPager.setAdapter(musicSongPagerAdapter);
				favoriteCallPagerAdapter = new FavoriteCallPagerAdapter(mContext,
						favoriteCallSelectEventHandler,4);
				FavoriteCallPager.setAdapter(favoriteCallPagerAdapter);
				((RelativeLayout)pop_up.findViewById(R.id.pop_up_layout)).setBackgroundResource(R.drawable.layout_bg_yellow);
				okbtn.setBackgroundResource(R.drawable.button_left_layout_bg_yellow);
				cancelbtn.setBackgroundResource(R.drawable.button_right_layout_bg_yellow);
				messageAdapter = new MessagePagerAdapter(mContext,
						messageNotification_handler,4);

				MessagePager.setAdapter(messageAdapter);
				break;

			default:
				break;
			}
			iman_maskView.invalidate();
			playSounds(2);
			updateCoverView(CommonInfo.TYPE_HOME_UPDATE, null);

		}
	}

	/*
	 * 배터리 잔량 상태에 대한 BroadcastReceiver 감지 되는 경우 : 배터리 상태 변경, 배터리 잔량이 낮은 경우, 충전
	 * 케이블이 연결된 경우, 충전 케이블이 해제된 경우 배터리 상태가 변경이 된 경우 남은 배터리의 퍼센트 값을 갱신해준다
	 */
	BroadcastReceiver battary_recever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
				int ratio = (level * 100) / scale;

				Log.d(CommonInfo.TAG, "Battary : " + String.valueOf(ratio)
						+ "%");

				setBatteryValue(String.valueOf(ratio) + "%");

			}
			if (action.equals(Intent.ACTION_BATTERY_LOW)) {
				toast.setText("ACTION_BATTERY_LOW");
				toast.show();
			}
			if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
				toast.setText("ACTION_POWER_CONNECTED");
				toast.show();
				Log.d(CommonInfo.TAG, "ACTION_POWER_CONNECTED");
				setBatteryValue("connected");
			}

			if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
				toast.setText("ACTION_POWER_DISCONNECTED");
				toast.show();
			}

			updateCoverView(CommonInfo.TYPE_BATTARY_UPDATE, null);

		}
	};

	OnGestureListener mGestureListner = new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			double x = e.getX();
			double y = e.getY();
			/**
			 * 배터리 영역에 대한 터치 여부 판별
			 * 
			 */


		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}
	};

	/*
	 * 스마트 커버의 상태를 감지하기 위한 리스너 구현
	 */
	protected StateListener mStateListener = new StateListener() {
		@Override
		public void onCoverOpenStateChanged(boolean state) {
			// TODO Auto-generated method stub
			if (state) {
				Log.d(CommonInfo.TAG, "cover open");
				coverOpenEvent();
				coverappData.messageNotificationList.clear();


			} else {
				Log.d(CommonInfo.TAG, "cover close");
				isfirstNotificationMessage = true;
				MessageInfo temp = new MessageInfo();
				temp.setMessage("메시지가 없습니다.");
				coverappData.messageNotificationList.add(temp);

				MessagePager.setAdapter(messageAdapter);

				coverClosedEvent();

			}
		}

		@Override
		public void onCoverAttachStateChanged(boolean state) {
			// TODO Auto-generated method stub
		}
	};

	Handler audioHandler = new Handler();
	boolean audioFlag = false;
	ContentObserver audiofileChangeObserver = new ContentObserver(audioHandler) {
		public void onChange(boolean selfChange) {
			musicSongPagerAdapter.getAlbumMusicData();
			MusicSongPager.setAdapter(musicSongPagerAdapter);
			if (audioFlag == false) {
				music_before_count = musicSongPagerAdapter.getCount();
				audioFlag = true;
			} else {
				if (music_before_count != musicSongPagerAdapter.getCount()) {

					musicPlayer.setNowPlayingPosition(0);

					Log.d(CommonInfo.TAG,
							"Count : !!!!!!!!!!!!!!!! ::: !!!!!!!!!::: "
									+ musicSongPagerAdapter.getCount()
									+ selfChange);

					if (musicSongPagerAdapter.getCount() == 0) {
						if (musicPlayer.isPlaying()) {
							musicPlayer.setCurrentAlbumImage();
							musicPlayer.pauseMusic(true);
							musicPlayer.setMusic_play_status(MusicPlayer.PAUSE);
						}
						((TextView) music_info_view
								.findViewById(R.id.music_song_name))
								.setText("음악파일을 찾을 수 없습니다");
						((TextView) music_info_view
								.findViewById(R.id.music_singer_name))
								.setText("");
						musicSeek.setProgress(0);
						musicSeek.setMax(0);
						updateCoverView(CommonInfo.TYPE_MUSIC_UPDATE, null);
						((TextView) music_info_view
								.findViewById(R.id.music_currentTime))
								.setText("00:00");
						((TextView) music_info_view
								.findViewById(R.id.music_totalTime))
								.setText("00:00");
						updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
								null);
					} else if (musicSongPagerAdapter.getCount() > 0) {
						if (musicPlayer.isPlaying()) {
							musicPlayer.setCurrentAlbumImage();
							musicPlayer.stopMusicAnimation();
							musicPlayer.startMusic(0);
							musicSeek.setProgress(0);
							musicSeek.setMax(musicPlayer.getMusic_duration());
							String totalPlayTime = (String) formatter
									.format(new Timestamp(musicPlayer
											.getMusic_duration()));
							((TextView) music_info_view
									.findViewById(R.id.music_totalTime))
									.setText(totalPlayTime);
							musicPlayer.setMusic_play_status(MusicPlayer.PLAY);
							updateCoverView(
									CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE,
									null);
						} else {
							musicSeek.setProgress(0);
							((TextView) music_info_view
									.findViewById(R.id.music_currentTime))
									.setText("00:00");
							((TextView) music_info_view
									.findViewById(R.id.music_totalTime))
									.setText("00:00");
							musicPlayer.stopMusic2();
						}

						((TextView) music_info_view
								.findViewById(R.id.music_song_name))
								.setText(coverappData.musicSongList.get(0)
										.getMusicTitle());
						((TextView) music_info_view
								.findViewById(R.id.music_singer_name))
								.setText(coverappData.musicSongList.get(0)
										.getSinger());
					}

					MusicSongPager.setOffscreenPageLimit(musicSongPagerAdapter
							.getCount());
					musicSongPagerContainer
							.setMusic_pager_Totalcount(musicSongPagerAdapter
									.getCount());
					MusicSongPager.setCurrentItem(0, true);
					musicSongPagerContainer.setSelected(true);
					musicSongPagerContainer.updateCountText();
					Log.d(CommonInfo.TAG, "오디오 파일 리스트 갱신");

					audioFlag = false;
				}
			}
		}

		// }
	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		Log.d(CommonInfo.TAG, "onCreate()");
		


		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		toast.setText("커버 앱 서비스 시작");
		toast.show();
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		String pattern = "mm:ss";
		formatter = new SimpleDateFormat(pattern);

		homeMenu_handler = new SwipeMenuEventHandler();
		settingArcMenuHandler = new SettingArcMenuHandler();
		messageNotification_handler = new MessageNotificationEventHandler();
		musicSongSelectEventHandler = new MusicSongSelectEventHandler();
		favoriteCallSelectEventHandler = new FavoriteCallSelectEventHandler();
		arcAnimation_handler = new ArcAnimationSelectEventHandler();
		ironThemeSelect_handler = new IronThemeSelectEventHandler();
		fv_handler = new FavoriteCallHandler();
		mv_handler = new MusicPlayerHandler();
		animationHandler = new Handler();

		coverappData.viewNotificationList.clear();

		ApplicationInfo appInfo;
		try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(),
					0);
			mContext = new ContextThemeWrapper(this, appInfo.theme);
			
			soundId = new int[10];
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				createNewSoundPool();
				soundId[0] = sounds.load(mContext, R.raw.home_menu, 1);
				soundId[1] = sounds.load(mContext, R.raw.middle_click, 1);
				soundId[2] = sounds.load(mContext, R.raw.final_check, 1);
				soundId[3] = sounds.load(mContext, R.raw.close_cover, 1);
				soundId[4] = sounds.load(mContext, R.raw.iron_man_hide, 1);
				soundId[5] = sounds.load(mContext, R.raw.home_menuopen, 1); 

			}else{
				createOldSoundPool();
				soundId[0] = sounds.load(mContext, R.raw.home_menu, 1);
				soundId[1] = sounds.load(mContext, R.raw.middle_click, 1);
				soundId[2] = sounds.load(mContext, R.raw.final_check, 1);
				soundId[3] = sounds.load(mContext, R.raw.close_cover, 1);
				soundId[4] = sounds.load(mContext, R.raw.iron_man_hide, 1);
				soundId[5] = sounds.load(mContext, R.raw.home_menuopen, 1); 
			}

			Uri uriAudios = Audio.Media.EXTERNAL_CONTENT_URI;

			ContentResolver cr = mContext.getContentResolver();
			cr.registerContentObserver(uriAudios, true, audiofileChangeObserver);

			initCoverView();
			mGesDetector = new GestureDetector(mContext, mGestureListner);
			display = ((WindowManager) mContext
					.getSystemService(mContext.WINDOW_SERVICE))
					.getDefaultDisplay();
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean deviceSupportCoverSDK = true;
		mScover = new Scover();

		try {
			mScover.initialize(this);
			mCoverManager = new ScoverManager(this);

			// Register a listener to get cover state changes
			mCoverManager.setStateListener(mStateListener);

			Point point = mCoverManager.getTouchResolution();
			int rowCount = point.y;
			int colCount = point.x;
			// byte coverShape=new byte[rowCount][colCount];

			/**
			 * 스마트 커버가 닫혔을 때 터치 영역에 대한 설정
			 * 
			 */
		

			byte[][] coverShape = {
					{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

			mCoverManager.setTouchMask(coverShape);

		} catch (IllegalArgumentException e) {
			deviceSupportCoverSDK = false;
		} catch (SsdkUnsupportedException e) {
			deviceSupportCoverSDK = false;
		} finally {
			if (!deviceSupportCoverSDK) {
				Log.e(CommonInfo.TAG, "This device does not support cover SDK!");
				if (mCoverMainView != null) {
					mCoverManager.removeView();
					mCoverMainView = null;

				}
				return;
			}
		}
		Log.d(CommonInfo.TAG, "init completed");

		LocalBroadcastManager.getInstance(this).registerReceiver(onNotice,
				new IntentFilter("Msg"));

		IntentFilter alarmFilter = new IntentFilter();
		alarmFilter.addAction(CommonInfo.ALARM_STARTED_INTENT_NAME);
		alarmFilter.addAction(CommonInfo.ALARM_STOPPED_INTENT_NAME);

		LocalBroadcastManager.getInstance(this).registerReceiver(alarm_recever,
				alarmFilter);
		/**
		 * 배터리 상태 변경에 대한 IntentFilter 등록
		 */
		IntentFilter battery_filter = new IntentFilter();
		battery_filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		battery_filter.addAction(Intent.ACTION_BATTERY_LOW);
		battery_filter.addAction(Intent.ACTION_POWER_CONNECTED);
		battery_filter.addAction(Intent.ACTION_POWER_DISCONNECTED);

		IntentFilter mIFNetwork = new IntentFilter();
		mIFNetwork
				.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(network_recever, mIFNetwork);
		registerReceiver(battary_recever, battery_filter);
		getSystemInfo();
	}

	// -------------------------------------------------------------------------------------------------------
	// broadcast로 intent에 저장된 notificaiton 및 broadcast 정보를 가져온다.
	private BroadcastReceiver onNotice = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			TranslateAnimation ani = new TranslateAnimation(
					// notification에서 감지된 Icon list 생성시 애니메이션을 적용하여 보여줌
					Animation.RELATIVE_TO_SELF, -1.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			ani.setDuration(500);
			ani.setFillAfter(true);
			ani.setFillEnabled(true);

			String notiTime = null;
			String smsTime = null;
			String sms_name = null;
			String message = null;
			String pack = null;// 패키지명
			String title = null;// 발신자
			String text = null;// 발신내용
			Integer userID = null;
			Integer uIcon = null;
			String type = null;
			String delete = intent.getStringExtra("delete");
			String flag = intent.getStringExtra("flag"); // Message와 나머지 noti정보
															// 구분
			Drawable r = null; // Noti에서 감지되는 아이콘 리소스가져오기
			Log.d(CommonInfo.TAG, "delete is not null? " + delete);
			if (delete != null) {
				delete = null;
				type = intent.getStringExtra("type");
				Log.d(CommonInfo.TAG, "Line_type = " + type);
				if (type != null) {
					int start = 0;
					boolean listFlag = true;
					while (true) {
						listFlag = true;
						for (int i = start; i < coverappData.viewNotificationList
								.size(); i++) {
							Log.d(CommonInfo.TAG, "type"
									+ coverappData.viewNotificationList.get(i)
											.getType());
							if ((coverappData.viewNotificationList.get(i)
									.getType()).equals(type)) {
								/* 메세지 내용도 지워야함 */

								notification_Adapter
										.returnmessagelistView()
										.setVisibility(
												notification_Adapter
														.returnmessagelistView().GONE);

								coverappData.viewNotificationList.remove(i);

	

								NotificationPager
										.setAdapter(notification_Adapter);

								start = i;
								listFlag = false;
								break;
							}
						}
						if (listFlag == true)
							break;
					}

					if (coverappData.viewNotificationList.size() == 0) {
						current_notification_alarm.setVisibility(View.GONE);
						mNotificationPagerContainer.setVisibility(View.GONE);
					} else {
						current_notification_alarm.setVisibility(View.VISIBLE);
						current_notification_alarm
								.setImageDrawable(coverappData.viewNotificationList
										.get(coverappData.viewNotificationList
												.size() - 1).getImgDrawable());
						current_notification_alarm.startAnimation(ani);
					}
					type = null;

				} else {
					title = intent.getStringExtra("title");// 발신자 for(NotiInfo
					Log.d(CommonInfo.TAG, "abc_getName_match_title=" + title);
					int start = 0;
					boolean listFlag = true;
					while (true) {
						listFlag = true;
						for (int i = start; i < coverappData.viewNotificationList
								.size(); i++) {
							if (title.equals("LINE")) {
								if ((coverappData.viewNotificationList.get(i)
										.getType()).equals(title)) {
									/* 메세지 내용도 지워야함 */
								
									notification_Adapter
											.returnmessagelistView()
											.setVisibility(
													notification_Adapter
															.returnmessagelistView().GONE);
									coverappData.viewNotificationList.remove(i);

								
									NotificationPager
											.setAdapter(notification_Adapter);
									
									Log.d(CommonInfo.TAG,
											"how many child : "
													+ coverappData.viewNotificationList
															.size());

									start = i;
									listFlag = false;
									break;
								}
							} else {
								Log.d(CommonInfo.TAG,
										"getName!!!!!!!!!!!!!!!!="
												+ coverappData.viewNotificationList
														.get(i).getName());
								if (coverappData.viewNotificationList.get(i)
										.getName() != null) {
									if ((coverappData.viewNotificationList
											.get(i).getName()).equals(title)) {

										/* 메세지 내용도 지워야함 */
										
										notification_Adapter
												.returnmessagelistView()
												.setVisibility(
														notification_Adapter
																.returnmessagelistView().GONE);
										coverappData.viewNotificationList
												.remove(i);

									
										NotificationPager
												.setAdapter(notification_Adapter);
										
										Log.d(CommonInfo.TAG,
												"how many child : "
														+ coverappData.viewNotificationList
																.size());

										start = i;
										listFlag = false;
										break;
									}
								}
							}
						}
						if (listFlag == true)
							break;
					}
					if (coverappData.viewNotificationList.size() == 0) {
						current_notification_alarm.setVisibility(View.GONE);
						mNotificationPagerContainer.setVisibility(View.GONE);
					} else {
						current_notification_alarm.setVisibility(View.VISIBLE);
						current_notification_alarm
								.setImageDrawable(coverappData.viewNotificationList
										.get(coverappData.viewNotificationList
												.size() - 1).getImgDrawable());
						current_notification_alarm.startAnimation(ani);
					}

				}
			} else {
				if (flag != null) {

					smsTime = intent.getStringExtra("sms_time"); // 발신시간
					sms_name = intent.getStringExtra("sms_name"); // 발신자명
					message = intent.getStringExtra("message"); // 발신내용
				} else {
					notiTime = intent.getStringExtra("notiTime");
					pack = intent.getStringExtra("package");// 패키지명
					Log.d("dfs", "package : " + pack);
					title = intent.getStringExtra("title");// 발신자
					Log.d("dfs", "title : " + title);
					text = intent.getStringExtra("text");// 발신내용
					userID = Integer.parseInt(intent.getStringExtra("userID"));
					uIcon = Integer.parseInt(intent.getStringExtra("uIcon"));
					r = loadIconDrawable(pack, userID, uIcon); // 페키지정보를 가지고
					Log.d("package", pack);
					Log.d(CommonInfo.TAG, "texttext" + text);
				}
				ImageView img = new ImageView(getApplicationContext());
				if (flag != null) {// 메세지
					Log.d(CommonInfo.TAG, "Message log");

					MessageInfo messageInfo = new MessageInfo();
					messageInfo.setTime(smsTime);
					messageInfo.setMessage(text);
					messageInfo.setName(sms_name);
					messageInfo.setType("sms");
					messageInfo.setImgDrawable(r);

					coverappData.messageNotificationList.add(messageInfo);
					MessagePager.setAdapter(messageAdapter);
					//messageAdapter.notifyDataSetChanged();
					Log.d(CommonInfo.TAG, "childCount!!!!!! : "
							+ coverappData.messageNotificationList.size());
					MessagePager.setCurrentItem(
							coverappData.messageNotificationList.size(), true);
					mMessageContainer.setSelected(true);

					NotiInfo notiInfo = new NotiInfo();
					notiInfo.setTime(smsTime);
					notiInfo.setMessage(message);
					notiInfo.setName(sms_name);
					notiInfo.setType("sms");

					ImageView smsimg = new ImageView(getApplicationContext());
					smsimg.setImageResource(R.drawable.ic_message);
					notiInfo.setImgDrawable(smsimg.getDrawable());
					coverappData.viewNotificationList.add(notiInfo);
			
					Log.d(CommonInfo.TAG, "childCount!!!!!! : "
							+ coverappData.viewNotificationList.size());
			
				
					NotificationPager.setAdapter(notification_Adapter);
					NotificationPager.setCurrentItem(
							coverappData.viewNotificationList.size(), true);
					mNotificationPagerContainer.setSelected(true);

					current_notification_alarm.setVisibility(View.VISIBLE);
					current_notification_alarm
							.setImageDrawable(coverappData.viewNotificationList
									.get(coverappData.viewNotificationList
											.size() - 1).getImgDrawable());
					current_notification_alarm.startAnimation(ani);
					flag = null;
				} else {// 나머지
					
					if(pack==null)
						return;
					if(title==null)
						return;
					/*
					 * line package명 : jp.naver.line.android kakao package명 :
					 * com.kakao.talk
					 */
					NotiInfo notiInfo = new NotiInfo();
					if (pack.equals("jp.naver.line.android")) {
						Log.d(CommonInfo.TAG, "LINE_text = " + text);
						if (text.contains(":")) {
							StringTokenizer st = new StringTokenizer(text, ":");
							title = st.nextToken();
							text = st.nextToken();
						}

						MessageInfo messageInfo = new MessageInfo();
						messageInfo.setTime(notiTime);

						messageInfo.setName(title);
						messageInfo.setMessage(text);
						messageInfo.setType("LINE");
						notiInfo.setType("LINE");
						messageInfo.setImgDrawable(r);

						if (isfirstNotificationMessage) {
							coverappData.messageNotificationList.clear();
							MessagePager.setAdapter(messageAdapter);
							isfirstNotificationMessage = false;
						}
						coverappData.messageNotificationList.add(messageInfo);
						MessagePager.setAdapter(messageAdapter);
						Log.d(CommonInfo.TAG, "childCount!!!!!! : "
								+ coverappData.messageNotificationList.size());
						MessagePager.setCurrentItem(
								coverappData.messageNotificationList.size(),
								true);
						mMessageContainer.setSelected(true);
					//	messageAdapter.notifyDataSetChanged();
					} else if (pack.equals("com.kakao.talk")) {
						MessageInfo messageInfo = new MessageInfo();
						messageInfo.setTime(notiTime);
						messageInfo.setMessage(text);
						messageInfo.setName(title);
						messageInfo.setType("kakao");
						messageInfo.setImgDrawable(r);
						notiInfo.setType("kakao");
						coverappData.messageNotificationList.add(messageInfo);
						MessagePager.setAdapter(messageAdapter);
						//messageAdapter.notifyDataSetChanged();
						Log.d(CommonInfo.TAG, "childCount!!!!!! : "
								+ coverappData.messageNotificationList.size());
						MessagePager.setCurrentItem(
								coverappData.messageNotificationList.size(),
								true);
						mMessageContainer.setSelected(true);
					}
					else {
						notiInfo.setType("other");
					}

					Log.d(CommonInfo.TAG, "other log");
					

					Log.d(CommonInfo.TAG, "othernoti");
					boolean musiclistFlag = true;
					boolean powerlistFlag = true;

					if(title.equals("Music\nPlayer"))
					{
						int start = 0;
						int size = 0;
							for (int i = start; i < coverappData.viewNotificationList
									.size(); i++) {

								if ((coverappData.viewNotificationList.get(i)
										.getName()).equals("Music\nPlayer")) {
									size = i;
									musiclistFlag = false;
									powerlistFlag = false;
									break;
								}
							}
							
								if(musiclistFlag==false){
								(coverappData.viewNotificationList.get(size)).setTime(notiTime);
								(coverappData.viewNotificationList.get(size)).setMessage(text);
								(coverappData.viewNotificationList.get(size)).setName(title);
								(coverappData.viewNotificationList.get(size)).setImgDrawable(r);
								NotificationPager.setAdapter(notification_Adapter);
								NotificationPager.setCurrentItem(
										coverappData.viewNotificationList.size(), true);
								NotificationPager.setSelected(true);
								}
					}
					
					if(title.equals("유선 충전"))
					{
						int start = 0;
						int size = 0;
							for (int i = start; i < coverappData.viewNotificationList
									.size(); i++) {

								if ((coverappData.viewNotificationList.get(i)
										.getName()).equals("유선 충전")) {
									size = i;
									powerlistFlag = false;
									musiclistFlag = false;
									break;
								}
							}
							
								if(powerlistFlag==false){
								(coverappData.viewNotificationList.get(size)).setTime(notiTime);
								(coverappData.viewNotificationList.get(size)).setMessage(text);
								(coverappData.viewNotificationList.get(size)).setName(title);
								(coverappData.viewNotificationList.get(size)).setImgDrawable(r);
								NotificationPager.setAdapter(notification_Adapter);
								NotificationPager.setCurrentItem(
										coverappData.viewNotificationList.size(), true);
								NotificationPager.setSelected(true);
								}
					}
					
					Log.d(CommonInfo.TAG,"musiclistFlag"+musiclistFlag);
					Log.d(CommonInfo.TAG,"powerlistFlag"+powerlistFlag);
					if(powerlistFlag==true || musiclistFlag == true){
					notiInfo.setTime(notiTime);
					notiInfo.setMessage(text);
					notiInfo.setName(title);
					notiInfo.setImgDrawable(r);

					coverappData.viewNotificationList.add(notiInfo);

			
					Log.d(CommonInfo.TAG, "childCount!!!!!! : "
							+ coverappData.viewNotificationList.size());
				
					// notification_Adapter.notifyDataSetChanged();
					NotificationPager.setAdapter(notification_Adapter);

					NotificationPager.setCurrentItem(
							coverappData.viewNotificationList.size(), true);
					NotificationPager.setSelected(true);

					current_notification_alarm.setVisibility(View.VISIBLE);
					current_notification_alarm
							.setImageDrawable(coverappData.viewNotificationList
									.get(coverappData.viewNotificationList
											.size() - 1).getImgDrawable());
					current_notification_alarm.startAnimation(ani);
					}
					flag = null;
					
				}

			}

		}
	};

	// -------------------------------------------------------------------------------------------------------
	// broadcastReceiver로 네트워크 연결 상태 감지

	BroadcastReceiver network_recever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String action = intent.getAction();
			String val = "";
			TextView net = ((TextView) home_info
					.findViewById(R.id.network_info));
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

				int connectedStatus = CommonInfo.isNetworkStat(mContext);

				switch (connectedStatus) {
				case -1:
					net.setTextSize(15);
					val = "network X";
					break;
				case 1:
					net.setTextSize(17);
					val = "WIFI";
					break;
				case 2:
					net.setTextSize(17);
					val = "3G/LTE";
					break;

				default:
					break;
				}
			}

			net.setText(val);

		}
	};

	private BroadcastReceiver alarm_recever = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(CommonInfo.ALARM_STARTED_INTENT_NAME)) { // ALARM
																		// START
				Log.d(CommonInfo.TAG, "ALARM_STARTED_INTENT_NAME!!");
				updateCoverView(CommonInfo.TYPE_ALARM_UPDATE, null);
			} else if (action.equals(CommonInfo.ALARM_STOPPED_INTENT_NAME)) { // ALARM
																				// STOP
				Log.d(CommonInfo.TAG, "ALARM_STOPPED_INTENT_NAME!!");

				updateCoverView(CurrentPage, null);

			}
		}
	};

	private Drawable loadIconDrawable(String pkg, int userId, int resId) {
		Drawable cicon = null;
		try {

			Context ctx = createPackageContext(pkg, 0);
			cicon = ctx.getResources().getDrawable(resId);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return cicon;
	}

	// -------------------------------------------------------------------------------------------------------

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.d(CommonInfo.TAG, "onDestory()");
		/*
		 * 서비스 종료시 리스너 해제
		 */
		toast.setText("커버 앱 서비스 종료");
		toast.show();
		try {
			unregisterReceiver(battary_recever);
			unregisterReceiver(network_recever);
			mCoverManager.setStateListener(null);
			LocalBroadcastManager.getInstance(this)
					.unregisterReceiver(onNotice);
			LocalBroadcastManager.getInstance(this).unregisterReceiver(
					alarm_recever);

			getContentResolver().unregisterContentObserver(
					audiofileChangeObserver);
		} catch (IllegalStateException ise) {
			Log.d(CommonInfo.TAG, "unregister Fail");
		}

		coverappData.musicSongList.clear();
		coverappData.viewNotificationList.clear();
		if (musicPlayer != null)
			musicPlayer.Release();
		
		notiManager.cancel(1);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void runExpandMenuAnimation(ViewGroup panel, Context ctx) {
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(ctx, R.anim.menu_open);
		panel.setLayoutAnimation(controller);
		controller.getAnimation().setFillAfter(true);

	}

	public static void runCollapseMenuAnimation(ViewGroup panel, Context ctx) {
		LayoutAnimationController controller = AnimationUtils
				.loadLayoutAnimation(ctx, R.anim.menu_close);
		panel.setLayoutAnimation(controller);
		controller.getAnimation().setFillAfter(true);
	}

	public static Animation runFadeOutAnimationOn(Context ctx, View target) {
		Animation animation = AnimationUtils.loadAnimation(ctx,
				android.R.anim.fade_out);
		target.startAnimation(animation);

		return animation;
	}

	private void initCoverView() {
		
		notiManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(getApplicationContext());

		 
	     builder.setSmallIcon(R.drawable.noti_music_icon);
	     builder.setTicker("IronMan Cover Music Start...");
	     builder.setWhen(System.currentTimeMillis());
	     builder.setNumber(10);
	     builder.setContentTitle("Music\nPlayer");
	     builder.setContentText("현재 음악이 재생중입니다");
	     builder.setOngoing(true);
	     music_noti = builder.build();    
	     
	     
	    
	     music_noti_remoteView= new RemoteViews(getPackageName(), R.layout.music_noti_view);
	     Intent before_play_Intent = new Intent();
	     before_play_Intent.setAction("music_before_play_noti");
	     PendingIntent before_play_piIntent = PendingIntent.getService(mContext, 0, before_play_Intent, 0);
	     music_noti_remoteView.setOnClickPendingIntent(R.id.music_before_play_noti, before_play_piIntent);
	     
	     Intent next_play_Intent = new Intent();
	     next_play_Intent.setAction("music_next_play_noti");
	     PendingIntent next_play_piIntent = PendingIntent.getService(mContext, 0, next_play_Intent, 0);
	     music_noti_remoteView.setOnClickPendingIntent(R.id.music_next_play_noti, next_play_piIntent);
	     
	     Intent play_Intent = new Intent();
	     play_Intent.setAction("music_noti_play");
	     PendingIntent play_piIntent = PendingIntent.getService(mContext, 0, play_Intent, 0);
	     music_noti_remoteView.setOnClickPendingIntent(R.id.music_noti_play, play_piIntent);
	     
	     Intent stop_Intent = new Intent();
	     stop_Intent.setAction("music_noti_stop");
	     PendingIntent stop_piIntent = PendingIntent.getService(mContext, 0, stop_Intent, 0);
	     music_noti_remoteView.setOnClickPendingIntent(R.id.music_stop_noti, stop_piIntent);
	     
	     
	     
	     music_noti.contentView = music_noti_remoteView;

	    
	  

		coverViewList = new ArrayList<View>();

		menu_open_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_out);
		menu_close_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_in);

		home_info_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.home_info_animation);

		menu_open_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

				mCoverMainView.setEnabled(false);
				animationOpenMenuTimingCheck = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mCoverMainView.setEnabled(true);
				animationOpenMenuTimingCheck = true;
			}
		});

		menu_close_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

				mCoverMainView.setEnabled(false);
				animationCloseMenuTimingCheck = false;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

				mCoverMainView.setEnabled(true);
				animationCloseMenuTimingCheck = true;

			}
		});

		mCoverMainView = ServiceDecorView.create(mContext);
		// mCoverMainView.setBackgroundColor(Color.BLUE);

		mCoverMainView.setBackgroundColor(0xFF121011);

		

		iman_maskView = new ImageView(mContext);
		iman_maskView.setImageResource(R.drawable.mask_red_iron_man);

		iman_maskView.setColorFilter(-1124094976, PorterDuff.Mode.MULTIPLY);

		iman_maskViewLayoutView = new RelativeLayout(mContext);

		RelativeLayout.LayoutParams iman_maskViewParams = new RelativeLayout.LayoutParams(
				1440, 1400);
		iman_maskViewParams.setMargins(0, 0, 0, 0);

		iman_maskViewLayoutView.addView(iman_maskView, iman_maskViewParams);

		ironImgLayout = new RelativeLayout(mContext);
		// ironImgLayoutView.setBackgroundColor(0xFF121011);
		ironMan = new ImageView(mContext);
		touchTxt = new TextView(mContext);
		touchTxt.setTextColor(Color.WHITE);

		ironMan.setImageResource(R.drawable.red_iron_man);

		RelativeLayout.LayoutParams ironManParams = new RelativeLayout.LayoutParams(
				1440, 1400);
		ironManParams.setMargins(0, 0, 0, 0);
		ironImgLayout.addView(ironMan, ironManParams);

		home_infoLayout = new RelativeLayout(mContext);
		home_info = inflater.inflate(R.layout.home_info, null);
		ram_info_text = (TextView) home_info.findViewById(R.id.ram_info);

		RelativeLayout.LayoutParams home_info_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		home_info_Params.setMargins(CommonInfo.getDpToPixel(mContext, 20),
				CommonInfo.getDpToPixel(mContext, 238), 0, 0);
		home_infoLayout.addView(home_info, home_info_Params);

		Typeface mFont = Typeface
				.createFromAsset(getAssets(), "D3Euronism.ttf");

		CommonInfo.setFont(home_infoLayout, mFont);

		/*
		 * 아크 배터리 에니메이션 초기화
		 */
		arc1_battery_layout = new RelativeLayout(mContext);
		View arc1_battery = inflater.inflate(R.layout.arc1_battery, null);

		arc_1 = (ImageView) arc1_battery.findViewById(R.id.arc_1);
		arc_1.setImageResource(R.drawable.arc_right_rotate);

		arc_2 = (ImageView) arc1_battery.findViewById(R.id.arc_2);
		arc_2.setImageResource(R.drawable.arc_left_rotate);

		arc_inner = (ImageView) arc1_battery.findViewById(R.id.arc_inner_alpha);
		arc_inner.setImageResource(R.drawable.arc_inner_alpha);

		arc_outer = (ImageView) arc1_battery.findViewById(R.id.arc_outer_alpha);
		arc_outer.setImageResource(R.drawable.arc_outer_alpha);

		arc_1_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc1_rotate);
		arc_1_animation.setStartOffset(0);
		arc_2_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc2_rotate);
		arc_2_animation.setStartOffset(0);

		arc_inner_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc_inner_alpha);
		arc_inner_animation.setStartOffset(0);

		arc_outer_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc_outer_alpha);
		arc_outer_animation.setStartOffset(0);

		arc_1.setAnimation(arc_1_animation);
		arc_2.setAnimation(arc_2_animation);

		arc_inner.setAnimation(arc_inner_animation);
		arc_outer.setAnimation(arc_outer_animation);

		RelativeLayout.LayoutParams arc1_battery_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arc1_battery_Params.setMargins(CommonInfo.getDpToPixel(mContext, 141),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arc1_battery_layout.addView(arc1_battery, arc1_battery_Params);

		arc2_battery_layout = new RelativeLayout(mContext);

		View arc2_battery = inflater.inflate(R.layout.arc2_battery, null);

		arc2_basic_alpha = (ImageView) arc2_battery
				.findViewById(R.id.arc2_basic_alpha);
		arc2_basic_alpha.setImageResource(R.drawable.arc2_basic);

		arc2_outer_alpha = (ImageView) arc2_battery
				.findViewById(R.id.arc2_outer_alpha);
		arc2_outer_alpha.setImageResource(R.drawable.arc2_outer);

		arc2_blue_alpha = (ImageView) arc2_battery
				.findViewById(R.id.arc2_blue_alpha);
		arc2_blue_alpha.setImageResource(R.drawable.arc2_blue);

		arc2_red_alpha = (ImageView) arc2_battery
				.findViewById(R.id.arc2_red_alpha);
		arc2_red_alpha.setImageResource(R.drawable.arc2_red);

		arc2_yello_alpha = (ImageView) arc2_battery
				.findViewById(R.id.arc2_yello_alpha);
		arc2_yello_alpha.setImageResource(R.drawable.arc2_yellow);

		arc2_basic_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc2_basic_alpha);
		arc2_basic_animation.setStartOffset(0);

		arc2_outer_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc2_outer_alpha);
		arc2_outer_animation.setStartOffset(0);

		arc2_blue_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc2_blue_alpha);
		arc2_blue_animation.setStartOffset(0);

		arc2_red_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc2_red_alpha);
		arc2_red_animation.setStartOffset(0);

		arc2_yello_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc2_yello_alpha);
		arc2_yello_animation.setStartOffset(0);

		arc2_basic_alpha.setAnimation(arc2_basic_animation);
		arc2_outer_alpha.setAnimation(arc2_outer_animation);
		arc2_blue_alpha.setAnimation(arc2_blue_animation);
		arc2_red_alpha.setAnimation(arc2_red_animation);
		arc2_yello_alpha.setAnimation(arc2_yello_animation);

		RelativeLayout.LayoutParams arc2_battery_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arc2_battery_Params.setMargins(CommonInfo.getDpToPixel(mContext, 141),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arc2_battery_layout.addView(arc2_battery, arc2_battery_Params);

		arc3_battery_layout = new RelativeLayout(mContext);

		View arc3_battery = inflater.inflate(R.layout.arc3_battery, null);

		arc3_inner_logo = (ImageView) arc3_battery
				.findViewById(R.id.arc3_inner);
		arc3_inner_logo.setImageResource(R.drawable.arc3_inner);

		arc3_mid = (ImageView) arc3_battery.findViewById(R.id.arc3_mid_alpha);
		arc3_mid.setImageResource(R.drawable.arc3_mid);

		arc3_outer_alpha = (ImageView) arc3_battery
				.findViewById(R.id.arc3_outer_alpha);
		arc3_outer_alpha.setImageResource(R.drawable.arc3_outer);

		arc3_rotate = (ImageView) arc3_battery
				.findViewById(R.id.arc3_rotate_alpha);
		arc3_rotate.setImageResource(R.drawable.arc3_rotate);

		arc3_outer_alpha_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc3_outer_alpha);
		arc3_outer_alpha_animation.setStartOffset(0);

		arc3_rotate_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc3_rotate);
		arc3_rotate_animation.setStartOffset(0);

		arc3_outer_alpha.setAnimation(arc3_outer_alpha_animation);
		arc3_rotate.setAnimation(arc3_rotate_animation);

		RelativeLayout.LayoutParams arc3_battery_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arc3_battery_Params.setMargins(CommonInfo.getDpToPixel(mContext, 141),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arc3_battery_layout.addView(arc3_battery, arc3_battery_Params);

		arc4_battery_layout = new RelativeLayout(mContext);

		View arc4_battery = inflater.inflate(R.layout.arc4_battery, null);

		arc4_gear_mid = (ImageView) arc4_battery
				.findViewById(R.id.arc4_gear_mid);
		arc4_gear_mid.setImageResource(R.drawable.arc4_gear_mid);

		arc4_gear1 = (ImageView) arc4_battery.findViewById(R.id.arc4_gear1);
		arc4_gear1.setImageResource(R.drawable.arc4_gear1);

		arc4_gear2 = (ImageView) arc4_battery.findViewById(R.id.arc4_gear2);
		arc4_gear2.setImageResource(R.drawable.arc4_gear2);

		arc4_gear3 = (ImageView) arc4_battery.findViewById(R.id.arc4_gear3);
		arc4_gear3.setImageResource(R.drawable.arc4_gear3);

		arc4_gear4 = (ImageView) arc4_battery.findViewById(R.id.arc4_gear4);
		arc4_gear4.setImageResource(R.drawable.arc4_gear4);

		arc4_gear5 = (ImageView) arc4_battery.findViewById(R.id.arc4_gear5);
		arc4_gear5.setImageResource(R.drawable.arc4_gear5);

		arc4_gear6 = (ImageView) arc4_battery.findViewById(R.id.arc4_gear6);
		arc4_gear6.setImageResource(R.drawable.arc4_gear6);

		arc4_main_gear = (ImageView) arc4_battery
				.findViewById(R.id.arc4_main_gear);
		arc4_main_gear.setImageResource(R.drawable.arc4_main_gear);

		arc4_inner = (ImageView) arc4_battery.findViewById(R.id.arc4_inner);
		arc4_inner.setImageResource(R.drawable.arc4_inner);

		arc4_outer_alpha = (ImageView) arc4_battery
				.findViewById(R.id.arc4_outer_alpha);
		arc4_outer_alpha.setImageResource(R.drawable.arc4_outer);

		arc4_outer_alpha_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_outer_alpha);
		arc4_outer_alpha_animation.setStartOffset(0);
		arc4_gear_mid_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_gear_mid);
		arc4_gear_mid_animation.setStartOffset(0);
		arc4_gear1_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_gear1);
		arc4_gear1_animation.setStartOffset(0);
		arc4_gear2_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_gear2);
		arc4_gear2_animation.setStartOffset(0);
		arc4_gear3_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_gear3);
		arc4_gear3_animation.setStartOffset(0);
		arc4_gear4_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_gear4);
		arc4_gear4_animation.setStartOffset(0);
		arc4_gear5_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_gear5);
		arc4_gear5_animation.setStartOffset(0);
		arc4_gear6_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_gear6);
		arc4_gear6_animation.setStartOffset(0);
		arc4_main_gear_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_main_gear);
		arc4_main_gear_animation.setStartOffset(0);
		arc4_inner_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc4_outer_alpha);
		arc4_inner_animation.setStartOffset(0);

		arc4_gear_mid.setAnimation(arc4_gear_mid_animation);
		arc4_outer_alpha.setAnimation(arc4_outer_alpha_animation);
		arc4_gear1.setAnimation(arc4_gear1_animation);
		arc4_gear2.setAnimation(arc4_gear2_animation);
		arc4_gear3.setAnimation(arc4_gear3_animation);
		arc4_gear4.setAnimation(arc4_gear4_animation);
		arc4_gear5.setAnimation(arc4_gear5_animation);
		arc4_gear6.setAnimation(arc4_gear6_animation);
		arc4_inner.setAnimation(arc4_inner_animation);
		arc4_main_gear.setAnimation(arc4_main_gear_animation);

		RelativeLayout.LayoutParams arc4_battery_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arc4_battery_Params.setMargins(CommonInfo.getDpToPixel(mContext, 141),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arc4_battery_layout.addView(arc4_battery, arc4_battery_Params);

		arc6_battery_layout = new RelativeLayout(mContext);

		View arc6_battery = inflater.inflate(R.layout.arc6_battery, null);

		arc6_outer = (ImageView) arc6_battery.findViewById(R.id.arc6_outer);
		arc6_outer.setImageResource(R.drawable.arc6_outer);

		arc6_solar = (ImageView) arc6_battery.findViewById(R.id.arc6_solar);
		arc6_solar.setImageResource(R.drawable.arc6_solar);

		arc6_inner = (ImageView) arc6_battery.findViewById(R.id.arc6_inner);
		arc6_inner.setImageResource(R.drawable.arc6_inner);

		arc6_inner_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc6_inner);
		arc6_inner_animation.setStartOffset(0);

		arc6_outer_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc6_outer_alpha);
		arc6_outer_animation.setStartOffset(0);

		arc6_solar_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc6_solar);
		arc6_solar_animation.setStartOffset(0);

		arc6_inner.setAnimation(arc6_inner_animation);
		arc6_outer.setAnimation(arc6_outer_animation);
		arc6_solar.setAnimation(arc6_solar_animation);

		RelativeLayout.LayoutParams arc6_battery_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arc6_battery_Params.setMargins(CommonInfo.getDpToPixel(mContext, 141),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arc6_battery_layout.addView(arc6_battery, arc6_battery_Params);
		
		
		
		
		
		arc_shield_layout = new RelativeLayout(mContext);
		View arc_sheild_battery = inflater.inflate(R.layout.arc_sheild_battery, null);
		
		arc_shield = (ImageView) arc_sheild_battery.findViewById(R.id.arc_sheild);
		arc_shield.setImageResource(R.drawable.captain_shield_before);
		
		arc_shield_alpha = (ImageView) arc_sheild_battery.findViewById(R.id.arc_sheild_alpha);
		arc_shield_alpha.setImageResource(R.drawable.captain_shield_after);

		
		arc_shield_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.arc_sheild_rotate);
		arc_shield_animation.setStartOffset(0);
		
		arc_shield_animation_alpha = AnimationUtils.loadAnimation(mContext, R.anim.arc_sheild_alpha);
		arc_shield_animation_alpha.setStartOffset(0);
		

		arc_shield.setAnimation(arc_shield_animation);
		arc_shield_alpha.setAnimation(arc_shield_animation_alpha);



		RelativeLayout.LayoutParams arc_sheild_battery_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arc_sheild_battery_Params.setMargins(CommonInfo.getDpToPixel(mContext, 141),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arc_shield_layout.addView(arc_sheild_battery, arc_sheild_battery_Params);
		
		
		
		arc7_battery_layout = new RelativeLayout(mContext);
		View arc7_battery_battery = inflater.inflate(R.layout.arc7_battery, null);
		
		arc7_background = (ImageView) arc7_battery_battery.findViewById(R.id.arc7_background);
		arc7_background.setImageResource(R.drawable.arc7_background);
		
		arc7_outer = (ImageView) arc7_battery_battery.findViewById(R.id.arc7_outer);
		arc7_outer.setImageResource(R.drawable.arc7_outer);
		
		
		arc7_main_torr = (ImageView) arc7_battery_battery.findViewById(R.id.arc7_main_torr);
		arc7_main_torr.setImageResource(R.drawable.arc7_main_torr);
		
		arc7_light = (ImageView) arc7_battery_battery.findViewById(R.id.arc7_light);
		arc7_light.setImageResource(R.drawable.arc7_light);
		

		
		arc7_outer_animation = AnimationUtils.loadAnimation(mContext,R.anim.arc7_outer_alpha);
		arc7_outer_animation.setStartOffset(0);
		
		arc7_light_animation = AnimationUtils.loadAnimation(mContext, R.anim.arc7_light);
		arc7_light_animation.setStartOffset(0);
		

		arc7_outer.setAnimation(arc7_outer_animation);
		arc7_light.setAnimation(arc7_light_animation);



		RelativeLayout.LayoutParams arc7_battery_battery_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arc7_battery_battery_Params.setMargins(CommonInfo.getDpToPixel(mContext, 141),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arc7_battery_layout.addView(arc7_battery_battery, arc7_battery_battery_Params);
		
		
		
		

		custom_clockLayout = new RelativeLayout(mContext);
		View custom_clock = inflater.inflate(R.layout.custom_clock, null);
		custom_clockLayout.addView(custom_clock);

		coverAlarmViewLayout = new RelativeLayout(mContext);
		AlarmView alarm_view = new AlarmView(mContext);
		RelativeLayout.LayoutParams alarm_view_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		alarm_view_Params.setMargins(CommonInfo.getDpToPixel(mContext, 135),
				CommonInfo.getDpToPixel(mContext, 134), 0, 0);
		coverAlarmViewLayout.addView(alarm_view, alarm_view_Params);

		home_menuLayout = new RelativeLayout(mContext);
		View home_menu = inflater.inflate(R.layout.home_menu, null);
		RelativeLayout.LayoutParams home_menu_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		home_menu_Params.setMargins(0, CommonInfo.getDpToPixel(mContext, 243),
				0, 0);
		home_menuLayout.addView(home_menu, home_menu_Params);
		mHomeMenuContainer = (HomeMenuPagerContainer) home_menu
				.findViewById(R.id.pager_container);
		HotKeyPager = mHomeMenuContainer.getViewPager();

		HomeMenuPagerAdapter homeMenuAdapter = new HomeMenuPagerAdapter(
				mContext, homeMenu_handler);
		HotKeyPager.setAdapter(homeMenuAdapter);

		HotKeyPager.setPageTransformer(true, new ZoomOutPageTransformer(0.7f,
				1.0f));
		HotKeyPager.setOffscreenPageLimit(homeMenuAdapter.getCount());
		HotKeyPager.setCurrentItem(0, true);
		mHomeMenuContainer.setSelected(true);

		HotKeyPager.setPageMargin(15);
		HotKeyPager.setClipChildren(false);
		

		message_menuLayout = new RelativeLayout(mContext);
		View message_nofication = inflater.inflate(R.layout.message_nofication,
				null);
		RelativeLayout.LayoutParams message_nofication_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		message_nofication_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 70),
				CommonInfo.getDpToPixel(mContext, 200),
				CommonInfo.getDpToPixel(mContext, 70), 0);
		message_menuLayout.addView(message_nofication,
				message_nofication_Params);
		mMessageContainer = (MessagePagerContainer) message_nofication
				.findViewById(R.id.pager_container);
		MessagePager = mMessageContainer.getViewPager();

		messageAdapter = new MessagePagerAdapter(mContext,
				messageNotification_handler,3);

		MessagePager.setAdapter(messageAdapter);

		MessagePager.setOffscreenPageLimit(messageAdapter.getCount());
		MessagePager.setCurrentItem(0, true);
		mMessageContainer.setSelected(true);
		MessagePager.setPageMargin(0);
		MessagePager.setClipChildren(false);

		favoriteCallLayout = new RelativeLayout(mContext);
		View favoritecall_pager = inflater.inflate(
				R.layout.favorite_select_pager, null);
		ImageView Sel_button = (ImageView) favoritecall_pager
				.findViewById(R.id.fv_button);

		pop_upLayout = new RelativeLayout(mContext);
		pop_up = inflater.inflate(R.layout.pop_up, null);

		RelativeLayout.LayoutParams pop_up_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pop_up_Params.setMargins(CommonInfo.getDpToPixel(mContext, 70),
				CommonInfo.getDpToPixel(mContext, 170),
				CommonInfo.getDpToPixel(mContext, 70), 0);

		pop_upLayout.addView(pop_up, pop_up_Params);

		okbtn = (Button) pop_up.findViewById(R.id.pop_up_okbtn);
		okbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW,

				Uri.parse("content://contacts/people/"));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				mContext.startActivity(intent);
				pop_upLayout.setVisibility(View.GONE);
			}
		});

		cancelbtn = (Button) pop_up.findViewById(R.id.pop_up_cancelbtn);

		cancelbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop_upLayout.setVisibility(View.GONE);
			}
		});
		((RelativeLayout)pop_up.findViewById(R.id.pop_up_layout)).setBackgroundResource(R.drawable.layout_bg_red);
		okbtn.setBackgroundResource(R.drawable.button_left_layout_bg_red);
		cancelbtn.setBackgroundResource(R.drawable.button_right_layout_bg_red);
		Sel_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * 주소록 앱 실행
				 */
				// TODO Auto-generated method stub
				Log.d(CommonInfo.TAG, "즐겨찾기추가");
				updateCoverView(CommonInfo.TYPE_POP_UP, null);

			}
		});

		favoriteCallLayout.addView(favoritecall_pager);
		mFavoriteCallPagerContainer = (FavoriteCallPagerContainer) favoritecall_pager
				.findViewById(R.id.pager_container);
		TextView favoritecall_pager_count = (TextView) favoritecall_pager
				.findViewById(R.id.favorite_call_page_count);
		FavoriteCallPager = mFavoriteCallPagerContainer.getViewPager();
		favoriteCallPagerAdapter = new FavoriteCallPagerAdapter(mContext,
				favoriteCallSelectEventHandler,3);
		FavoriteCallPager.setAdapter(favoriteCallPagerAdapter);
		mFavoriteCallPagerContainer.setFavorite_pager_count(
				favoritecall_pager_count, favoriteCallPagerAdapter.getCount());
		mFavoriteCallPagerContainer.updateCountText();

		FavoriteCallPager.setPageTransformer(true, new ZoomOutPageTransformer(
				0.95f, 0.5f));
		FavoriteCallPager.setOffscreenPageLimit(favoriteCallPagerAdapter
				.getCount());
		FavoriteCallPager.setCurrentItem(0, true);
		mFavoriteCallPagerContainer.setSelected(true);
		FavoriteCallPager.setPageMargin(0);
		FavoriteCallPager.setClipChildren(false);

		music_select_itemLayout = new RelativeLayout(mContext);
		View music_select_pager = inflater.inflate(R.layout.music_select_pager,
				null);

		RelativeLayout.LayoutParams music_select_pager_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		music_select_pager_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 72),
				CommonInfo.getDpToPixel(mContext, 163),
				CommonInfo.getDpToPixel(mContext, 70), 0);
		music_select_itemLayout.addView(music_select_pager,
				music_select_pager_Params);
		musicSongPagerContainer = (MusicSongPagerContainer) music_select_pager
				.findViewById(R.id.pager_container);
		TextView music_pager_count = (TextView) music_select_pager
				.findViewById(R.id.music_page_count);

		MusicSongPager = musicSongPagerContainer.getViewPager();
		musicSongPagerAdapter = new MusicSongPagerAdapter(mContext,
				musicSongSelectEventHandler,3);
		MusicSongPager.setAdapter(musicSongPagerAdapter);

		musicSongPagerContainer.setMusic_pager_count(music_pager_count,
				musicSongPagerAdapter.getCount());
		musicSongPagerContainer.updateCountText();

		MusicSongPager.setPageTransformer(true, new ZoomOutPageTransformer(
				0.75f, 0.5f));
		MusicSongPager.setOffscreenPageLimit(5);
		MusicSongPager.setCurrentItem(0, true);
		musicSongPagerContainer.setSelected(true);
		MusicSongPager.setPageMargin(0);
		MusicSongPager.setClipChildren(false);

		music_layout = new RelativeLayout(mContext);
		musicPlayer = new MusicPlayer(mContext, mv_handler);

		music_player = musicPlayer.getPlayview();
		RelativeLayout.LayoutParams music_layout_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		music_layout_Params.setMargins(CommonInfo.getDpToPixel(mContext, 60),
				CommonInfo.getDpToPixel(mContext, 218), 0, 0);
		music_layout.addView(music_player, music_layout_Params);

		music_info_view = musicPlayer.getMusic_infoview();
		RelativeLayout.LayoutParams music_info_layout_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		music_info_layout_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 80),
				CommonInfo.getDpToPixel(mContext, 160),
				CommonInfo.getDpToPixel(mContext, 80), 0);
		music_layout.addView(music_info_view, music_info_layout_Params);
		((TextView) music_info_view.findViewById(R.id.music_song_name))
				.setSelected(true);
		((TextView) music_info_view.findViewById(R.id.music_singer_name))
				.setSelected(true);

		if (coverappData.musicSongList.size() > 0) {
			music_pager_count.setText("1/" + coverappData.musicSongList.size());
			((TextView) music_info_view.findViewById(R.id.music_song_name))
					.setText(coverappData.musicSongList.get(0).getMusicTitle());
			((TextView) music_info_view.findViewById(R.id.music_singer_name))
					.setText(coverappData.musicSongList.get(0).getSinger());

		}

		musicSeek = ((SeekBar) music_info_view.findViewById(R.id.music_seekBar));

		music_submenuLayout = new RelativeLayout(mContext);

		View music_list_menu = (View) inflater.inflate(
				R.layout.music_list_menu, null);
		RelativeLayout.LayoutParams music_list_menu_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		music_list_menu_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 288),
				CommonInfo.getDpToPixel(mContext, 177), 0, 0);
		music_submenuLayout.addView(music_list_menu, music_list_menu_Params);

		music_list_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (coverappData.musicSongList.size() > 0) {
					if (music_player.getVisibility() == View.GONE) {
						music_player.setVisibility(View.VISIBLE);
						((TextView) music_info_view
								.findViewById(R.id.music_song_name))
								.setTextColor(Color.RED);
						((TextView) music_info_view
								.findViewById(R.id.music_singer_name))
								.setTextColor(Color.WHITE);
						((SeekBar) music_info_view
								.findViewById(R.id.music_seekBar))
								.setVisibility(View.VISIBLE);
						((TextView) music_info_view
								.findViewById(R.id.music_currentTime))
								.setVisibility(View.VISIBLE);
						((TextView) music_info_view
								.findViewById(R.id.music_totalTime))
								.setVisibility(View.VISIBLE);
						music_info_view.setVisibility(View.VISIBLE);
						music_select_itemLayout.setVisibility(View.GONE);
						CurrentPage = CommonInfo.TYPE_MUSIC_UPDATE;
					} else {
						music_player.setVisibility(View.GONE);
						music_info_view.setVisibility(View.GONE);
						music_select_itemLayout.setVisibility(View.VISIBLE);
						CurrentPage = CommonInfo.TYPE_MUSICSELECT_UPDATE;
					}
				}
			}
		});

		musicSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				musicPlayer.getmPlayer().seekTo(musicSeek.getProgress());

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Log.d(CommonInfo.TAG, "onProgressChanged " + progress);

				String currentPlayTime = (String) formatter
						.format(new Timestamp(musicSeek.getProgress()));
				((TextView) music_info_view
						.findViewById(R.id.music_currentTime))
						.setText(currentPlayTime);
			}
		});


		music_info_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (coverappData.musicSongList.size() > 0) {
					if (music_player.getVisibility() == View.GONE) {
						music_player.setVisibility(View.VISIBLE);
						((TextView) music_info_view
								.findViewById(R.id.music_song_name))
								.setTextColor(Color.RED);
						((TextView) music_info_view
								.findViewById(R.id.music_singer_name))
								.setTextColor(Color.WHITE);
						((SeekBar) music_info_view
								.findViewById(R.id.music_seekBar))
								.setVisibility(View.VISIBLE);
						((TextView) music_info_view
								.findViewById(R.id.music_currentTime))
								.setVisibility(View.VISIBLE);
						((TextView) music_info_view
								.findViewById(R.id.music_totalTime))
								.setVisibility(View.VISIBLE);
						music_select_itemLayout.setVisibility(View.GONE);
						CurrentPage = CommonInfo.TYPE_MUSIC_UPDATE;
					} else {
						music_player.setVisibility(View.GONE);
						((TextView) music_info_view
								.findViewById(R.id.music_song_name))
								.setTextColor(Color.TRANSPARENT);
						((TextView) music_info_view
								.findViewById(R.id.music_singer_name))
								.setTextColor(Color.TRANSPARENT);
						((SeekBar) music_info_view
								.findViewById(R.id.music_seekBar))
								.setVisibility(View.GONE);
						((TextView) music_info_view
								.findViewById(R.id.music_currentTime))
								.setVisibility(View.GONE);
						((TextView) music_info_view
								.findViewById(R.id.music_totalTime))
								.setVisibility(View.GONE);
						music_info_view.setVisibility(View.GONE);
						music_select_itemLayout.setVisibility(View.VISIBLE);
						CurrentPage = CommonInfo.TYPE_MUSICSELECT_UPDATE;
					}
				}
			}
		});

		music_equalizerLayout = new RelativeLayout(mContext);
		View equalizer_view = inflater.inflate(R.layout.equalizer, null);

		RelativeLayout.LayoutParams equalizer_view_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		equalizer_view_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 173),
				CommonInfo.getDpToPixel(mContext, 22), 0, 0);

		music_equalizerLayout.addView(equalizer_view, equalizer_view_Params);

		FrameLayout equalizer_frame = (FrameLayout) equalizer_view
				.findViewById(R.id.equalizer_frame);
		equalizer_frame.setBackgroundResource(R.drawable.frame_animation2);
		music_equalizer_frameAnimation = (AnimationDrawable) equalizer_frame
				.getBackground();
		music_equalizer_frameAnimation.start();

		// notificationLayout2
		notificationLayout = new RelativeLayout(mContext);
		View notification_pager = inflater.inflate(R.layout.alarm_notification,
				null);

		notificationLayout.addView(notification_pager);
		mNotificationPagerContainer = (NotificationPagerContainer) notification_pager
				.findViewById(R.id.current_pager_container);
		mNotificationPagerContainer
				.setBackgroundResource(R.drawable.layout_bg_red);
		NotificationPager = mNotificationPagerContainer.getViewPager();
		notification_Adapter = new NotiPagerAdapter(mContext,
				notification_pager);
		NotificationPager.setAdapter(notification_Adapter);

		
		NotificationPager.setOffscreenPageLimit(1000);
		NotificationPager.setCurrentItem(0, true);
		mNotificationPagerContainer.setSelected(true);
		NotificationPager.setPageMargin(0);
		NotificationPager.setClipChildren(false);
		mNotificationPagerContainer.setVisibility(View.GONE);
		notification_Adapter.returnmessagelistView().setBackgroundResource(R.drawable.layout_bg_red);
		current_notification_alarm = (ImageView) notification_pager
				.findViewById(R.id.current_notification_alarm);
		current_notification_alarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				if (mNotificationPagerContainer.getVisibility() == View.VISIBLE) {
					Animation animationscaledown = AnimationUtils
							.loadAnimation(mContext,
									R.anim.scale_down_animation);
					animationscaledown
							.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									mNotificationPagerContainer
											.setVisibility(View.GONE);
									notification_Adapter
											.returnmessagelistView()
											.setVisibility(View.GONE);

								}
							});
					mNotificationPagerContainer
							.startAnimation(animationscaledown);
				} else {
					Animation animationscaleup = AnimationUtils.loadAnimation(
							mContext, R.anim.scale_up_animation);
					mNotificationPagerContainer.setVisibility(View.VISIBLE);
					mNotificationPagerContainer
							.startAnimation(animationscaleup);
					NotificationPager.setCurrentItem(
							coverappData.viewNotificationList.size(), true);
					mNotificationPagerContainer.setSelected(true);

				}

			}
		});
		current_notification_alarm.setVisibility(View.GONE);
		setting_menu_Layout = new RelativeLayout(mContext);
		View setting_menu_view = inflater.inflate(R.layout.setting_menu, null);
		// setting_menu_view.setBackgroundColor(Color.YELLOW);
		arcMenu = (ArcMenu) setting_menu_view.findViewById(R.id.arc_menu_2);
		arcMenu.setHandler(settingArcMenuHandler);
		arcMenu.initArcMenu(arcMenu, CommonInfo.SETTING_MENU_ITEM_DRAWABLES);

		RelativeLayout.LayoutParams setting_menu_view_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setting_menu_view_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 44),
				CommonInfo.getDpToPixel(mContext, 148), 0, 0);

		setting_menu_Layout
				.addView(setting_menu_view, setting_menu_view_Params);

		iman_mask_settingLayout = new RelativeLayout(mContext);

		final View iman_mask_setting_view = inflater.inflate(
				R.layout.iman_mask_setting, null);

		iman_maskImage = (ImageView) iman_mask_setting_view
				.findViewById(R.id.iron_mask);
		picker = (ColorPicker) iman_mask_setting_view.findViewById(R.id.picker);

		iman_maskImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				touchTxt.setText("아이언맨 Mask 변경");
				maskColorBefore = picker.getColor();
				playSounds(2);
				eyeImage.startAnimation(eye_alpha_animation);
				updateCoverView(CommonInfo.TYPE_HOME_UPDATE, null);
			}
		});
		maskIcon = getResources().getDrawable(R.drawable.iron_man_mask);

		SaturationBar saturationBar = (SaturationBar) iman_mask_setting_view
				.findViewById(R.id.saturationbar);
		ValueBar valueBar = (ValueBar) iman_mask_setting_view
				.findViewById(R.id.valuebar);
		OpacityBar opacityBar = (OpacityBar) iman_mask_setting_view
				.findViewById(R.id.opacitybar);

		picker.addSaturationBar(saturationBar);
		picker.addValueBar(valueBar);
		picker.addOpacityBar(opacityBar);
		// To get the color

		// To set the old selected color u can do it like this
		picker.setOldCenterColor(picker.getColor());
		// adds listener to the colorpicker which is implemented
		// in the activity
		picker.setOnColorChangedListener(new OnColorChangedListener() {

			@Override
			public void onColorChanged(int color) {
				// TODO Auto-generated method stub

				Log.d(CommonInfo.TAG, "color:::" + color);

				iman_maskView.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
				maskIcon.invalidateSelf();
				iman_maskView.invalidate();
			
			}
		});

		// to turn of showing the old color
		picker.setShowOldCenterColor(false);

		// iman_mask_setting_view.setBackgroundColor(Color.YELLOW);

		RelativeLayout.LayoutParams iman_mask_setting_view_Params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iman_mask_setting_view_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 116),
				CommonInfo.getDpToPixel(mContext, 20), 0, 0);

		iman_mask_settingLayout.addView(iman_mask_setting_view,
				iman_mask_setting_view_Params);

		eye_effectLayout = new RelativeLayout(mContext);
		eyeImage = new ImageView(mContext);

		eye_alpha_animation = AnimationUtils.loadAnimation(mContext,
				R.anim.eye_alpha);
		eye_alpha_animation.setStartOffset(0);
		
		easter_egg_animation = AnimationUtils.loadAnimation(mContext, R.anim.esater_egg);
		easter_egg_animation.setStartOffset(0);

		eye_alpha_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				eyeImage.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				eyeImage.setVisibility(View.GONE);
			}
		});
		
		easter_egg_animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				eyeImage.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				eyeImage.setVisibility(View.GONE);
			}
		});

		// eyeImage.setAnimation(eye_alpha_animation);

		RelativeLayout.LayoutParams eyeImage_Params = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		eyeImage_Params.setMargins(CommonInfo.getDpToPixel(mContext, 129),
				CommonInfo.getDpToPixel(mContext, 58), 0, 0);
		// eyeImage.setBackgroundColor(Color.YELLOW);
		eyeImage.setImageResource(R.drawable.eye_effect);
		eye_effectLayout.addView(eyeImage, eyeImage_Params);

		arcAnimationSelectLayout = new RelativeLayout(mContext);
		View arcAnimation_view = inflater.inflate(
				R.layout.arc_animation_select, null);
		RelativeLayout.LayoutParams arcAnimation_view_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		arcAnimation_view_Params.setMargins(
				CommonInfo.getDpToPixel(mContext, 2),
				CommonInfo.getDpToPixel(mContext, 245), 0, 0);
		arcAnimationSelectLayout.addView(arcAnimation_view,
				arcAnimation_view_Params);
		mArcAnimationSelectPagerContainer = (ArcAnimationSelectPagerContainer) arcAnimation_view
				.findViewById(R.id.pager_container);

		ArcAnimationSelectPager = mArcAnimationSelectPagerContainer
				.getViewPager();

		ArcAnimationSelectPagerAdapter arcAnimationAdapter = new ArcAnimationSelectPagerAdapter(
				mContext, arcAnimation_handler);
		ArcAnimationSelectPager.setAdapter(arcAnimationAdapter);

		ArcAnimationSelectPager.setPageTransformer(true,
				new ZoomOutPageTransformer(0.7f, 1.0f));
		ArcAnimationSelectPager.setOffscreenPageLimit(arcAnimationAdapter
				.getCount());
		ArcAnimationSelectPager.setCurrentItem(0, true);
		mArcAnimationSelectPagerContainer.setSelected(true);

		ArcAnimationSelectPager.setPageMargin(15);
		ArcAnimationSelectPager.setClipChildren(false);

		ironThemeSelectLayout = new RelativeLayout(mContext);
		View ironThemeSelect_view = inflater.inflate(
				R.layout.iron_theme_select, null);
		// ironThemeSelect_view.setBackgroundColor(Color.BLUE);
		RelativeLayout.LayoutParams ironThemeSelect_view_Params = new RelativeLayout.LayoutParams(
				1440, 1400);
		ironThemeSelect_view_Params.setMargins(0, 0, 0, 0);
		ironThemeSelectLayout.addView(ironThemeSelect_view,
				ironThemeSelect_view_Params);
		mIronThemeSelectPagerContainer = (IronThemeSelectPagerContainer) ironThemeSelect_view
				.findViewById(R.id.pager_container);
		ImageView closeThemeSetting = (ImageView) ironThemeSelect_view
				.findViewById(R.id.closeThemeSetting);
		closeThemeSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int itemCount = arcMenu.getmArcLayout().getChildCount();
	            for (int i = 0; i < itemCount; i++) {
	                View item = arcMenu.getmArcLayout().getChildAt(i);
	                item.setVisibility(View.VISIBLE);
	            }
				playSounds(1);
				updateCoverView(CommonInfo.TYPE_SETTING_UPDATE, null);
			}
		});

		IronThemeSelectPager = mIronThemeSelectPagerContainer.getViewPager();

		IronThemeSelectPagerAdapter ironThemeSelectAdapter = new IronThemeSelectPagerAdapter(
				mContext, ironThemeSelect_handler);
		IronThemeSelectPager.setAdapter(ironThemeSelectAdapter);

		IronThemeSelectPager.setPageTransformer(true,
				new ZoomOutPageTransformer(0.7f, 1.0f));
		IronThemeSelectPager.setOffscreenPageLimit(ironThemeSelectAdapter
				.getCount());
		IronThemeSelectPager.setCurrentItem(0, true);
		IronThemeSelectPager.setSelected(true);
		mIronThemeSelectPagerContainer.setSelected(true);

		IronThemeSelectPager.setPageMargin(15);
		IronThemeSelectPager.setClipChildren(false);

		callingLayout = new RelativeLayout(mContext);
		View callingView = inflater.inflate(R.layout.calling, null);
		RelativeLayout.LayoutParams callingView_Params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		callingView_Params.setMargins(CommonInfo.getDpToPixel(mContext, 55),
				CommonInfo.getDpToPixel(mContext, 160), 0, 0);
		calling_person = (TextView) callingView
				.findViewById(R.id.calling_person_name);
		calling_phone = (TextView) callingView
				.findViewById(R.id.calling_person_phone);
		ImageView calling_animation = (ImageView) callingView
				.findViewById(R.id.calling_animation);
		calling_animation.setBackgroundResource(R.drawable.frame_animation);
		callingAnimation = (AnimationDrawable) calling_animation
				.getBackground();
		ImageView calling_cancle = (ImageView) callingView
				.findViewById(R.id.calling_cancle);
		calling_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateCoverView(CommonInfo.TYPE_FAVORITECALL_UPDATE, null);
			}
		});

		final ImageView calling_speaker = (ImageView) callingView
				.findViewById(R.id.calling_speaker);

		calling_speaker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSpeacker) {
					calling_speaker
							.setImageResource(R.drawable.out_speaker_set);
					isSpeacker = false;
				} else {
					calling_speaker
							.setImageResource(R.drawable.out_speaker_off);
					isSpeacker = true;
				}
			}
		});

		// calling_person.setSelected(true);
		// calling_phone.setSelected(true);

		// callingAnimation.start();
		callingLayout.addView(callingView, callingView_Params);

		mCoverMainView.addView(iman_maskViewLayoutView, iman_maskViewParams);
		mCoverMainView.addView(ironImgLayout, ironManParams);
//		mCoverMainView.addView(touchTxt);
		mCoverMainView.addView(coverAlarmViewLayout);
		mCoverMainView.addView(home_infoLayout);
		mCoverMainView.addView(arc1_battery_layout);
		mCoverMainView.addView(arc2_battery_layout);
		mCoverMainView.addView(arc3_battery_layout);
		mCoverMainView.addView(arc4_battery_layout);
		mCoverMainView.addView(arc6_battery_layout);
		mCoverMainView.addView(arc_shield_layout);
		mCoverMainView.addView(arc7_battery_layout);
		mCoverMainView.addView(custom_clockLayout);
		mCoverMainView.addView(home_menuLayout);
		mCoverMainView.addView(favoriteCallLayout);
		mCoverMainView.addView(message_menuLayout);
		mCoverMainView.addView(music_layout);
		mCoverMainView.addView(music_select_itemLayout);
		mCoverMainView.addView(music_submenuLayout);
		mCoverMainView.addView(music_equalizerLayout);
		mCoverMainView.addView(setting_menu_Layout);
		mCoverMainView.addView(eye_effectLayout);
		mCoverMainView.addView(iman_mask_settingLayout);
		mCoverMainView.addView(arcAnimationSelectLayout);
		mCoverMainView.addView(ironThemeSelectLayout);
		mCoverMainView.addView(callingLayout);
		mCoverMainView.addView(notificationLayout);
		mCoverMainView.addView(pop_upLayout);

		coverViewList.add(coverAlarmViewLayout);
		coverViewList.add(home_infoLayout);
		coverViewList.add(arc1_battery_layout);
		coverViewList.add(arc2_battery_layout);
		coverViewList.add(arc3_battery_layout);
		coverViewList.add(arc4_battery_layout);
		coverViewList.add(arc6_battery_layout);
		coverViewList.add(arc_shield_layout);
		coverViewList.add(arc7_battery_layout);
		coverViewList.add(custom_clockLayout);
		coverViewList.add(home_menuLayout);
		coverViewList.add(favoriteCallLayout);
		coverViewList.add(message_menuLayout);
		coverViewList.add(music_layout);
		coverViewList.add(music_select_itemLayout);
		coverViewList.add(music_submenuLayout);
		coverViewList.add(pop_upLayout);
		coverViewList.add(setting_menu_Layout);
		coverViewList.add(arcAnimationSelectLayout);
		coverViewList.add(ironThemeSelectLayout);

		coverViewList.add(iman_mask_settingLayout);
		coverViewList.add(callingLayout);

		eyeImage.setVisibility(View.GONE);

		updateCoverView(CommonInfo.TYPE_HOME_UPDATE, null);

		startArcAnimation(ArcAnimationType);

	

		int statusBarHeight = (int) Math.ceil(25 * getResources()
				.getDisplayMetrics().density);

		/*
		 * /Long Click 이벤트
		 */

		/*
		 * 커버 전체 뷰 화면에 대한 터치 이벤트 리스너
		 */

		mCoverMainView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mGesDetector.onTouchEvent(event);
				double x = event.getX();
				double y = event.getY();

				int action = event.getAction();

				switch (action & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_POINTER_DOWN:
					Log.d(CommonInfo.TAG, "double Touch");
					if((System.currentTimeMillis() - startTime) > 500 ){
						touchCount = 0;
					}
					
					if(touchCount==0){
						touchCount++;
						startTime = System.currentTimeMillis();
					}else{
						touchCount++;
						
			//			Log.d(CommonInfo.TAG, "diff time :" + (System.currentTimeMillis() - startTime));
						if((System.currentTimeMillis() - startTime) > 500 ){
							touchCount = 0;
						}
						
						if(touchCount>1){
							endTime = System.currentTimeMillis();
							long time = endTime-startTime;
							if(time<500){
								eyeImage.startAnimation(easter_egg_animation);
								playSounds(4);
								Log.d(CommonInfo.TAG, "time : "+time);
							}
							touchCount=0;
						}
					}
					

					break;
				case MotionEvent.ACTION_UP:
					/**
					 * 배터리 영역에 대한 터치 여부 판별
					 * 
					 */
					if (Math.pow(700 - x, 2) + Math.pow(1126 - y, 2) <= Math
							.pow(150, 2)) {
						if (CurrentPage == CommonInfo.TYPE_HOME_UPDATE) {
							home_info_animation.cancel();
							home_info_animation.reset();
							home_info.setVisibility(View.GONE);
							home_menuLayout.startAnimation(menu_open_animation);


							updateCoverView(CommonInfo.TYPE_HOME_MENU, null);
							eyeImage.startAnimation(eye_alpha_animation);
							playSounds(5);
						} 
					} else {
						
											
						if (CurrentPage == CommonInfo.TYPE_HOME_MENU
								|| CurrentPage == CommonInfo.TYPE_FAVORITECALL_UPDATE
								|| CurrentPage == CommonInfo.TYPE_MUSIC_UPDATE
								|| CurrentPage == CommonInfo.TYPE_MUSICSELECT_UPDATE
								|| CurrentPage == CommonInfo.TYPE_SMS_UPDATE
								|| CurrentPage == CommonInfo.TYPE_MASKSETTING_UPDATE
								|| CurrentPage == CommonInfo.TYPE_ARCANIMATION_MENU_UPDATE
								|| CurrentPage == CommonInfo.TYPE_SETTING_UPDATE

						) {
							animationOpenMenuTimingCheck = false;
							animationCloseMenuTimingCheck = false;
							home_menuLayout
									.startAnimation(menu_close_animation);

							iman_maskView.setColorFilter(maskColorBefore,
									PorterDuff.Mode.MULTIPLY);
							updateCoverView(CommonInfo.TYPE_HOME_UPDATE, null);
							eyeImage.startAnimation(eye_alpha_animation);
							playSounds(0);
						}

						
					}

					mNotificationPagerContainer.setVisibility(View.GONE);
					notification_Adapter.returnmessagelistView().setVisibility(
							notification_Adapter.returnmessagelistView().GONE);

					break;

				default:
					break;
				}

				touchTxt.setText("W :" + display.getWidth() + "\nH :"
						+ display.getHeight() + "\n\n" + "X : " + event.getX()
						+ "\nY :" + event.getY());

				return true;
			}
		});
	}

	SoundPool sounds;
	
	public void playSounds(int idx){
		sounds.play(soundId[idx], 1, 1, 1, 0, 1);
	}

  
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	protected void createNewSoundPool() {
		AudioAttributes attributes = new AudioAttributes.Builder()
				.setUsage(AudioAttributes.USAGE_GAME)
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.build();
		sounds = new SoundPool.Builder().setAudioAttributes(attributes).build();
	}

	@SuppressWarnings("deprecation")
	protected void createOldSoundPool() {
		sounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	}

	private void updateViewVisibility(View v) {
		Log.d(CommonInfo.TAG, "set view id : " + v.hashCode());
		for (int i = 0; i < coverViewList.size(); i++) {
			View view = coverViewList.get(i);
			Log.d(CommonInfo.TAG, "view id : " + view.hashCode());
			if (view == v) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.GONE);
			}
		}
	}

	/*
	 * 모든 커버 뷰 화면의 변경은 updateCoverView(int,Object) 메서드를 통해 이루어짐. 첫번째 파라미터로 전달되는
	 * type값에 따라 화면 업데이트 요청을 구분함
	 */
	private void updateCoverView(int type, Object data) {
		if (mCoverMainView == null) {
			initCoverView();
		}
		switch (type) {

		case CommonInfo.TYPE_HOME_UPDATE:
			home_info.setVisibility(View.VISIBLE);
			startArcAnimation(ArcAnimationType);
			custom_clockLayout.setVisibility(View.VISIBLE);
			home_infoLayout.setVisibility(View.VISIBLE);
			home_infoLayout.startAnimation(home_info_animation);
			ironImgLayout.setVisibility(View.VISIBLE);
			iman_maskViewLayoutView.setVisibility(View.VISIBLE);
			// noti_menuLayout.setVisibility(View.VISIBLE);
			notificationLayout.setVisibility(View.VISIBLE);
			mNotificationPagerContainer.setVisibility(View.GONE);
			notification_Adapter.returnmessagelistView().setVisibility(
					notification_Adapter.returnmessagelistView().GONE);
			callingAnimation.stop();
			final int itemCount = arcMenu.getmArcLayout().getChildCount();
            for (int i = 0; i < itemCount; i++) {
                View item = arcMenu.getmArcLayout().getChildAt(i);
                item.setVisibility(View.VISIBLE);
            }
			CurrentPage = CommonInfo.TYPE_HOME_UPDATE;
			Log.d(CommonInfo.TAG, "TYPE_HOME_UPDATE");
			break;
		case CommonInfo.TYPE_HOME_MENU:
			updateViewVisibility(home_menuLayout);
			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_HOME_MENU;
			Log.d(CommonInfo.TAG, "TYPE_HOME_MENU");
			break;
		case CommonInfo.TYPE_BATTARY_UPDATE:

			((TextView) home_info.findViewById(R.id.battry_info))
					.setText(getBatteryValue());

			Log.d(CommonInfo.TAG, "TYPE_BATTARY_UPDATE");
			break;
		case CommonInfo.TYPE_SMS_UPDATE:
			updateViewVisibility(message_menuLayout);
			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_SMS_UPDATE;
			Log.d(CommonInfo.TAG, "TYPE_SMS_UPDATE");
			break;
		case CommonInfo.TYPE_FAVORITECALL_UPDATE:
			callingAnimation.stop();
			updateViewVisibility(favoriteCallLayout);
			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_FAVORITECALL_UPDATE;
			Log.d(CommonInfo.TAG, "TYPE_FAVORITECALL_UPDATE");
			break;
		case CommonInfo.TYPE_MUSIC_UPDATE:
			updateViewVisibility(music_layout);
			((TextView) music_layout.findViewById(R.id.music_song_name))
					.setTextColor(Color.RED);
			((TextView) music_layout.findViewById(R.id.music_singer_name))
					.setTextColor(Color.WHITE);
			((SeekBar) music_info_view.findViewById(R.id.music_seekBar))
					.setVisibility(View.VISIBLE);
			((TextView) music_info_view.findViewById(R.id.music_currentTime))
					.setVisibility(View.VISIBLE);
			((TextView) music_info_view.findViewById(R.id.music_totalTime))
					.setVisibility(View.VISIBLE);
			music_info_view.setVisibility(View.VISIBLE);
			music_player.setVisibility(View.VISIBLE);
			music_submenuLayout.setVisibility(View.VISIBLE);

			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_MUSIC_UPDATE;
			Log.d(CommonInfo.TAG, "TYPE_MUSIC_UPDATE");
			break;
		case CommonInfo.TYPE_ALARM_UPDATE:
			updateViewVisibility(coverAlarmViewLayout);

			break;
		case CommonInfo.TYPE_POP_UP:
			pop_upLayout.setVisibility(View.VISIBLE);
			break;
		case CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE:

			if (musicPlayer.getmPlayer().isPlaying()) {
				music_equalizerLayout.setVisibility(View.VISIBLE);
				music_equalizer_frameAnimation.start();
			} else {
				music_equalizerLayout.setVisibility(View.GONE);
				music_equalizer_frameAnimation.stop();
			}

		case CommonInfo.TYPE_SETTING_MENU_UPDATE:

			break;
		case CommonInfo.TYPE_SETTING_UPDATE:
			updateViewVisibility(setting_menu_Layout);
			ironImgLayout.setVisibility(View.VISIBLE);
			iman_maskViewLayoutView.setVisibility(View.VISIBLE);
			notificationLayout.setVisibility(View.VISIBLE);
//			arcMenu.openSettingMenu(false);
			arcMenu.getmArcLayout().switchState(false);
			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_SETTING_UPDATE;
 
			break;
		case CommonInfo.TYPE_MASKSETTING_UPDATE:
			updateViewVisibility(iman_mask_settingLayout);

			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_MASKSETTING_UPDATE;
			break;
		case CommonInfo.TYPE_ARCANIMATION_MENU_UPDATE:
			updateViewVisibility(arcAnimationSelectLayout);

			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_ARCANIMATION_MENU_UPDATE;
			break;
		case CommonInfo.TYPE_IRONTHEME_MENU_UPDATE:
			updateViewVisibility(ironThemeSelectLayout);
			ironImgLayout.setVisibility(View.GONE);
			iman_maskViewLayoutView.setVisibility(View.GONE);
			// noti_menuLayout.setVisibility(View.GONE);
			notificationLayout.setVisibility(View.VISIBLE);
			endArcAnimation();
			CurrentPage = CommonInfo.TYPE_IRONTHEME_MENU_UPDATE;
			break;

		case CommonInfo.TYPE_CALLING_UPDATE:
			updateViewVisibility(callingLayout);
			callingAnimation.start();
			// CurrentPage = CommonInfo.TYPE_CALLING_UPDATE;

			break;

		default:
			break;
		}
	}



	private void coverClosedEvent() {

		if (mCoverMainView != null) {

			if (mCoverMainView.isShown() != true) {
				
				mCoverManager.addView(mCoverMainView, createLayoutParams());

				eyeImage.startAnimation(eye_alpha_animation);
				if (CurrentPage == CommonInfo.TYPE_HOME_UPDATE){
					playSounds(3);
					updateCoverView(CommonInfo.TYPE_HOME_UPDATE, null);
				}
					

				favoriteCallPagerAdapter.updateContactList();
				FavoriteCallPager.setAdapter(favoriteCallPagerAdapter);

				mFavoriteCallPagerContainer
						.setFavorite_pager_count(favoriteCallPagerAdapter
								.getCount());
				FavoriteCallPager.setCurrentItem(0, true);
				mFavoriteCallPagerContainer.setSelected(true);
				mFavoriteCallPagerContainer.updateCountText();

				if (musicPlayer.getmPlayer().isPlaying()) {
					musicPlayer.startMusicAnimation();
				}

				updateCoverView(CommonInfo.TYPE_MUSIC_EQUALIZER_UPDATE, null);

			}
		}
	}

	private void coverOpenEvent() {

		if (mCoverMainView != null) {
		
			endArcAnimation();
			Log.d(CommonInfo.TAG, "coverOpenEvent");
			mCoverManager.removeView();
		}

	}

	private ViewGroup.MarginLayoutParams createBatteryLayoutParams() {
		ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(400,
				200);
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		int pixelsValue = 5; // margin in pixels
		float d = mContext.getResources().getDisplayMetrics().density;
		int margin = (int) (pixelsValue * d);

		return lp;
	}

	private WindowManager.LayoutParams createLayoutParams() {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.type = ScoverManager.TYPE_COVER_WINDOW;

	
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
		

		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		
		lp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		return lp;
	}

	public static class ServiceDecorView extends FrameLayout {

		private ServiceDecorView(Context context) {
			super(context);
		}

		private ServiceDecorView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		private ServiceDecorView(Context context, AttributeSet attrs,
				int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}

		// Create and show service UI
		public static ServiceDecorView create(Context context) {
			ServiceDecorView view = new ServiceDecorView(context);

			return view;
		}
	}

}
