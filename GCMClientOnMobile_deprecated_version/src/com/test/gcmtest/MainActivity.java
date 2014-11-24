/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test.gcmtest;

import static com.test.gcmtest.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.test.gcmtest.CommonUtilities.SENDER_ID;
import static com.test.gcmtest.CommonUtilities.TAG;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

/**
 * Main UI for the demo app.
 */
public class MainActivity extends Activity {

	TextView mDisplay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkNotNull(SENDER_ID, "SENDER_ID");//SENDID 就是我们在上一章建立Google API Project中得到的project ID

		setContentView(R.layout.main);
		mDisplay = (TextView) findViewById(R.id.display);
		
		registerReceiver(mHandleMessageReceiver,
				new IntentFilter(DISPLAY_MESSAGE_ACTION));

		registerGCM();
	}

	private void registerGCM() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		String regId = registerGCMService ();
		mDisplay.append(regId + "\n");	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.options_register:
			String strid = registerGCMService();
			if (!strid.isEmpty()){
				mDisplay.append("regid is:" + strid + "\n");	
				Log.i(TAG, "registering device (regId = " + strid + ")");
			}
			return true;

		case R.id.options_unregister:
			unregisterGCMService();
			return true;
			
		case R.id.options_clear:
			mDisplay.setText(null);
			return true;
			
		case R.id.options_exit:
			finish();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private String registerGCMService() {
//		if (GCMRegistrar.isRegistered(this)){
//			Log.i(TAG, "The service has been registered. we unregister first.");
//			GCMRegistrar.unregister(this);
//		}
		
		String regId = GCMRegistrar.getRegistrationId(this);
		if(regId.equals("")){
			GCMRegistrar.register(this, SENDER_ID);
			regId =GCMRegistrar.getRegistrationId(this); 
		}else{
			if (GCMRegistrar.isRegisteredOnServer(this)){
				mDisplay.append("isRegisteredOnServer is false;");
			}
		}

		GCMRegistrar.setRegisteredOnServer(this, true);
		return regId;
	}
	
	private void unregisterGCMService() {
		String strid = GCMRegistrar.getRegistrationId(this);
		if(!strid.equals("")){
			GCMRegistrar.unregister(this);
		}
		GCMRegistrar.setRegisteredOnServer(this, false);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mHandleMessageReceiver);
		
		GCMRegistrar.onDestroy(this);
		
		super.onDestroy();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(
					getString(R.string.error_config, name));
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver =
			new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			mDisplay.append("\n--------New message received below.---------\n");
			Bundle bundle = intent.getExtras();
			for (String key: bundle.keySet())
			{
				mDisplay.append("Key=" + key + ", " + "content=" + bundle.getString(key) + "\n");
			}
		}
	};
}
