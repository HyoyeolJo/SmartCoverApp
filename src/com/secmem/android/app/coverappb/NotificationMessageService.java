package com.secmem.android.app.coverappb;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class NotificationMessageService extends NotificationListenerService {
	Context context;
	private ArrayList<String> notiPackageFilterList;

	public NotificationMessageService() {
	};

	public NotificationMessageService(Context mContext) {
		context = mContext;
		// TODO Auto-generated constructor stub
	}

	public Context getContext() {

		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		intentFilter
				.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT");
		intentFilter
				.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT");

		registerReceiver(new Broad(), intentFilter);
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		Intent msgrcv = new Intent("Msg");
		String pack = sbn.getPackageName();
		Log.d(CommonInfo.TAG, "ppack"+pack);
		if(pack.equals("com.android.provider.downloads") && pack.equals("com.android.vending"))//updating and download notification alarm are not posted.
			return;
		String ticker = "unknown";
		if (sbn.getNotification().tickerText != null)
			ticker = sbn.getNotification().tickerText.toString();

		Bundle extras = sbn.getNotification().extras;
		String title = extras.getString("android.title"); // �߽��ڸ�
		String text;
		if(extras.getCharSequence("android.text")!=null)
		text = extras.getCharSequence("android.text").toString();// �߽ų���
		else text="";
		Integer userID = sbn.getUserId();
		Integer uIcon = sbn.getNotification().icon;
		String notiTime; // �߽� �ð�
		Calendar noti_Time = Calendar.getInstance();
		noti_Time.setTimeInMillis(sbn.getPostTime());

		if (noti_Time.get(Calendar.AM_PM) == 0) {
			notiTime = "AM "
					+ String.format("%d : %02d",
							noti_Time.get(Calendar.HOUR_OF_DAY),
							noti_Time.get(Calendar.MINUTE));
			Log.d(CommonInfo.TAG, "���Žð�" + notiTime);
		} else {
			notiTime = "PM "
					+ String.format("%d : %02d",
							noti_Time.get(Calendar.HOUR_OF_DAY),
							noti_Time.get(Calendar.MINUTE));
			Log.d(CommonInfo.TAG, "���Žð�" + notiTime);
		}

		Log.i(CommonInfo.TAG, "package=" + pack);
		Log.i(CommonInfo.TAG, "ticker=" + ticker);
		Log.i(CommonInfo.TAG, "title=" + title);
		Log.i(CommonInfo.TAG, "text=" + text);
		if (pack.equals("com.android.mms")) {
			Log.d(CommonInfo.TAG, "mms" + "This is mms");
		} else {
			msgrcv.putExtra("package", pack);// ��Ű����
			msgrcv.putExtra("ticker", ticker);
			msgrcv.putExtra("title", title);// �߽���
			msgrcv.putExtra("text", text);// �߽ų���
			msgrcv.putExtra("userID", userID.toString());
			msgrcv.putExtra("uIcon", uIcon.toString());
			msgrcv.putExtra("notiTime", notiTime);

			LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i("Msg", "Notification Removed");
		Intent msgrcv = new Intent("Msg");
		String delete = "delete";
		String type = "sms";
		String pack = sbn.getPackageName();
		Log.d(CommonInfo.TAG,"abc_pack = "+pack);
		if(pack.equals("com.android.provider.downloads") && pack.equals("com.android.vending"))//updating and download notification alarm are not posted.
			{
				Log.d(CommonInfo.TAG,"abc_except");
			return;}
		Bundle extras = sbn.getNotification().extras;
		String title = extras.getString("android.title"); // �߽��ڸ�
		Log.i(CommonInfo.TAG, pack);
		Log.i(CommonInfo.TAG, title);
		if (pack.equals("com.android.mms")) {
			msgrcv.putExtra("type", type);
			msgrcv.putExtra("delete", delete);
			Log.d(CommonInfo.TAG, "Don't send message");
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
		} else {

			msgrcv.putExtra("title", title);// �߽���
			msgrcv.putExtra("delete", delete);
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
		}
	}

	public boolean isInPackageFilter(StatusBarNotification sbn) {
		String packageName = sbn.getPackageName();

		for (int i = 0; i < notiPackageFilterList.size(); i++) {
			if (packageName.equals(notiPackageFilterList.get(i))) {
				return true;
			}
		}
		return false;
	}

	public String convertLongTimeToString(long sbnTime) {
		Calendar calendarTime = Calendar.getInstance();
		calendarTime.setTimeInMillis(sbnTime);

		String returnTime;
		if (calendarTime.get(Calendar.AM_PM) == 0) {
			returnTime = "AM "
					+ String.format("%d : %02d",
							calendarTime.get(Calendar.HOUR_OF_DAY),
							calendarTime.get(Calendar.MINUTE));
		} else {
			returnTime = "PM "
					+ String.format("%d : %02d",
							calendarTime.get(Calendar.HOUR_OF_DAY),
							calendarTime.get(Calendar.MINUTE));
		}
		return returnTime;
	}

	public class Broad extends BroadcastReceiver {
		Intent msgrcv = new Intent("Msg");
		String sms_time;
		String sms_name;
		String message;
		String flag;
		Context context = new NotificationMessageService().getContext();

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d("OnReceive", "Broad OnReceive");

			Log.d("onReceive", intent.getAction());
			if (intent.getAction().equals(
					"android.provider.Telephony.SMS_RECEIVED")) {
				flag = "sms";
				Bundle bundle = intent.getExtras();
				Object messages[] = (Object[]) bundle.get("pdus");
				SmsMessage smsMessage[] = new SmsMessage[messages.length];

				for (int i = 0; i < messages.length; i++) {
					// PDU �������� �Ǿ� �ִ� �޽����� �����մϴ�.
					smsMessage[i] = SmsMessage
							.createFromPdu((byte[]) messages[i]);
				}

				// SMS ���� �ð� Ȯ��
				Calendar curDate = Calendar.getInstance();
				curDate.setTimeInMillis(smsMessage[0].getTimestampMillis());

				if (curDate.get(Calendar.AM_PM) == 0) {
					sms_time = "AM "
							+ String.format("%d : %02d",
									curDate.get(Calendar.HOUR_OF_DAY),
									curDate.get(Calendar.MINUTE));
					Log.d("good", "���Žð�" + sms_time);
				} else {
					sms_time = "PM "
							+ String.format("%d : %02d",
									curDate.get(Calendar.HOUR_OF_DAY),
									curDate.get(Calendar.MINUTE));
					Log.d("good", "���Žð�" + sms_time);
				}

				// SMS �߽� ��ȣ Ȯ��
				String origNumber = smsMessage[0].getOriginatingAddress();
				// SMS �޽��� Ȯ��
				sms_name = getPhoneName(context, origNumber);
				message = smsMessage[0].getMessageBody().toString();
				if (sms_name.equals("")) {
					sms_name = origNumber;
					Log.d("���� ����", "�߽��� : " + sms_name + ", ���� : " + message);
				} else
					Log.d("���� ����", "�߽��� : " + sms_name + ", ���� : " + message);

				msgrcv.putExtra("sms_time", sms_time);
				msgrcv.putExtra("sms_name", sms_name);
				msgrcv.putExtra("message", message);
				msgrcv.putExtra("flag", flag);
				LocalBroadcastManager.getInstance(context)
						.sendBroadcast(msgrcv);
			} else if (intent
					.getAction()
					.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT")) {
				System.out.println("alarm start");
				Intent i = new Intent(CommonInfo.ALARM_STARTED_INTENT_NAME);
				LocalBroadcastManager.getInstance(context).sendBroadcast(i);
			} else if (intent
					.getAction()
					.equals("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT")) {
				System.out.println("alarm stop");
				Intent i = new Intent(CommonInfo.ALARM_STOPPED_INTENT_NAME);
				LocalBroadcastManager.getInstance(context).sendBroadcast(i);
			}

		}

	}

	private String getPhoneName(Context context, String strNum) {
		Cursor phoneCursor = null;
		String strReturn = "";
		try {
			// �ּҷ��� ����� URI
			Uri uContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

			// �ּҷ��� �̸��� ��ȭ��ȣ�� �̸�
			String strProjection = ContactsContract.CommonDataKinds.Phone.NUMBER;

			// �ּҷ��� ��� ���� �������� ������ Ŀ���� ���� (�̸����� �����ؼ� ������)
			phoneCursor = context.getContentResolver().query(uContactsUri,
					null, null, null, strProjection);
			phoneCursor.moveToFirst();

			String name = "";
			String number = "";
			String email = "";
			int nameColumn = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME);
			int numberColumn = phoneCursor.getColumnIndex(Phone.NUMBER);
			int NumberTypeColumn = phoneCursor.getColumnIndex(Phone.TYPE);

			// stop loop if find
			while (!phoneCursor.isAfterLast() && strReturn.equals("")) {
				name = phoneCursor.getString(nameColumn);
				number = phoneCursor.getString(numberColumn)
						.replaceAll("-", "");
				;

				int numberType = Integer.valueOf(phoneCursor
						.getString(NumberTypeColumn));

				Cursor emailCursor = null;
				try {
					// ���� ������ �̸��� ����Ͽ� E-mail�ּҰ� ��ϵǾ��ִ��� �˻�
					emailCursor = context
							.getContentResolver()
							.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
									new String[] { ContactsContract.CommonDataKinds.Email.DATA },
									"NUMBER" + "='" + number + "'", null, null);

					while (emailCursor.moveToNext()) {
						email = emailCursor
								.getString(emailCursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					}
				} catch (Exception e) {

				} finally {
					if (emailCursor != null) {
						emailCursor.close();
						emailCursor = null;
					}
				}

				// if find, set return values, and stop loops.
				if (number.equals(strNum)) {
					strReturn = name;
				}

				name = "";
				number = "";
				email = "";
				phoneCursor.moveToNext();
			}
		} catch (Exception e) {

		} finally {
			if (phoneCursor != null) {
				phoneCursor.close();
				phoneCursor = null;
			}
		}

		return strReturn;
	}
}
