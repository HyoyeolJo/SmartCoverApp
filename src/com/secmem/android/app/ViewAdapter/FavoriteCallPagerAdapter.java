package com.secmem.android.app.ViewAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.secmem.android.app.Data.MusicSongInfo;
import com.secmem.android.app.Data.coverappData;
import com.secmem.android.app.coverappb.R;
import com.secmem.android.app.coverappb.R.id;
import com.secmem.android.app.coverappb.R.layout;
import com.secmem.android.app.coverappb.CommonInfo;
import com.secmem.android.app.view.Contact;
import com.secmem.android.app.view.MusicCDRoundedDrawable;
import com.secmem.android.app.view.RoundedDrawable;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.Contacts;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;



public class FavoriteCallPagerAdapter extends PagerAdapter {
	
	Context mContext;
	Handler handler;
	private int selectedIdx;
	private TextView favoritename;
	private int themeSet;
	private ArrayList<Contact> contactlist;
	RoundedDrawable rD;
	
	public FavoriteCallPagerAdapter(Context context, Handler handler,int themeset) {
		super();
		mContext = context;
		this.handler = handler;
		contactlist = getContactList();
		themeSet = themeset;
	}
	
	public void setSelectedIdx(int position){
		selectedIdx = position;
	}
	
	 
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
    	
		View v = new View(mContext);
		final LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
			v = inflater.inflate(R.layout.favorite_call_list_item, (ViewGroup) null, false);


			ImageView peopleImage= ((ImageView) v.findViewById(R.id.peopleImage));
			
			
			Contact acontact = contactlist.get(position);

			if (acontact != null) {

				favoritename= ((TextView) v.findViewById(R.id.favorite_call_name));
				favoritename.setText(acontact.getName());
				favoritename.setSelected(true);
				switch(themeSet)
				{
				case 0:
					favoritename.setBackgroundResource(R.drawable.layout_bg_blue);
					break;
				case 1:
					favoritename.setBackgroundResource(R.drawable.layout_bg_gray);
					break;
				case 2:
					favoritename.setBackgroundResource(R.drawable.layout_bg_mint);
					break;
				case 3:
					favoritename.setBackgroundResource(R.drawable.layout_bg_red);
					break;
				case 4:
					favoritename.setBackgroundResource(R.drawable.layout_bg_yellow);
					break;
				default:
					break;
				}
				Bitmap bm = openPhoto(acontact.getPhotoid());
				// 사진없으면 기본 사진 보여주기
				if (bm != null) {
					//holder.iv_photoid.setImageBitmap(bm);
					rD = new RoundedDrawable(bm);
					peopleImage.setImageDrawable(rD);
				} else {
					peopleImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.person_icon));
				}
			}

			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(position == selectedIdx ){
						Log.d(CommonInfo.TAG, "clicked : "+position);
						Bundle bundle = new Bundle();
						bundle.putString("favoriteCallPhone", contactlist.get(position).getPhonenum());
						bundle.putString("favoriteCallName", contactlist.get(position).getName());
						Message msg = new Message();
						msg.setData(bundle);
						handler.sendMessage(msg);
					}

				}
			});
			
			
			
			
		Log.d(CommonInfo.TAG, "FavoriteCallPagerAdapter instantiateItem : "+position);

        container.addView(v);
        
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
  //      Log.d(CommonInfo.TAG, "destroyItem : "+position);
   //     Log.d(CommonInfo.TAG, "destroyItem "+contactlist.get(position).getName());
    }

    @Override
    public int getCount() {
        return contactlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
    
		

	/**
	 * 연락처를 가져오는 메소드.
	 * 
	 * @return
	 */

	public void updateContactList() {

		Log.d(CommonInfo.TAG, "getContactList()");
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 즐겨찾기 연락처
																	// 그림정보
				ContactsContract.CommonDataKinds.Phone.NUMBER, // 즐겨찾기 연락처 번호
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 즐겨찾기 이름
				ContactsContract.CommonDataKinds.Phone.STARRED // 즐겨찾기에 저장된 리스트만
																// 찾기 위한 쿼리조건
		};

		String[] selectionArgs = null;
		String selection = ContactsContract.CommonDataKinds.Phone.STARRED
				+ "='1'"; // 즐겨찾기에 저장된 연락처 목록만 가져옴
		String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";//이름으로 오름차순 정렬
		//Cursor contactCursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
		
		Cursor contactCursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
		contactlist.clear();
		
		
		if (contactCursor.moveToFirst()) {
			do {
				Log.d("number", "관리 " + contactCursor.getString(1));
				String phonenumber = contactCursor.getString(1).replaceAll("-",
						"");
				if (phonenumber.length() == 10) {
					phonenumber = phonenumber.substring(0, 3) + "-"
							+ phonenumber.substring(3, 6) + "-"
							+ phonenumber.substring(6);
				} else if (phonenumber.length() > 8) {
					phonenumber = phonenumber.substring(0, 3) + "-"
							+ phonenumber.substring(3, 7) + "-"
							+ phonenumber.substring(7);
				}
 
				Contact acontact = new Contact();
				acontact.setPhotoid(contactCursor.getLong(0));
				acontact.setPhonenum(phonenumber);
				acontact.setName(contactCursor.getString(2));

				contactlist.add(acontact);
			} while (contactCursor.moveToNext());
		}



	}
	
	
	
	

	/**
	 * 연락처를 가져오는 메소드.
	 * 
	 * @return
	 */

	public ArrayList<Contact> getContactList() {

		Log.d(CommonInfo.TAG, "getContactList()");
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 즐겨찾기 연락처
																	// 그림정보
				ContactsContract.CommonDataKinds.Phone.NUMBER, // 즐겨찾기 연락처 번호
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 즐겨찾기 이름
				ContactsContract.CommonDataKinds.Phone.STARRED // 즐겨찾기에 저장된 리스트만
																// 찾기 위한 쿼리조건
		};

		String[] selectionArgs = null;
		String selection = ContactsContract.CommonDataKinds.Phone.STARRED
				+ "='1'"; // 즐겨찾기에 저장된 연락처 목록만 가져옴
		String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";//이름으로 오름차순 정렬
		//Cursor contactCursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
		
		Cursor contactCursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
	
		ArrayList<Contact> contactlist = new ArrayList<Contact>();
		
		if (contactCursor.moveToFirst()) {
			do {
				Log.d("number", "관리 " + contactCursor.getString(1));
				String phonenumber = contactCursor.getString(1).replaceAll("-",
						"");
				if (phonenumber.length() == 10) {
					phonenumber = phonenumber.substring(0, 3) + "-"
							+ phonenumber.substring(3, 6) + "-"
							+ phonenumber.substring(6);
				} else if (phonenumber.length() > 8) {
					phonenumber = phonenumber.substring(0, 3) + "-"
							+ phonenumber.substring(3, 7) + "-"
							+ phonenumber.substring(7);
				}
 
				Contact acontact = new Contact();
				acontact.setPhotoid(contactCursor.getLong(0));
				acontact.setPhonenum(phonenumber);
				acontact.setName(contactCursor.getString(2));

				contactlist.add(acontact);
			} while (contactCursor.moveToNext());
		}

		return contactlist;

	}
	
	/**
	 * 연락처 사진 id를 가지고 사진에 들어갈 bitmap을 생성.
	 * 
	 * @param contactId
	 *            연락처 사진 ID
	 * @return bitmap 연락처 사진
	 */
	private Bitmap openPhoto(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		InputStream input = ContactsContract.Contacts
				.openContactPhotoInputStream(mContext.getContentResolver(),
						contactUri);

		if (input != null) {
			
			return BitmapFactory.decodeStream(input);
		}

		return null;
	}
}