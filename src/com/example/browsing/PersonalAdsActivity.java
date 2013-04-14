package com.example.browsing;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.example.maple_android.MapleHttpClient;
import com.example.maple_android.R;
import com.example.maple_android.Utility;
import com.facebook.Session;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PersonalAdsActivity extends Activity {

	private static final String TAG = "PersonalAds";
	private GridView mGridview; // the view we are using to display the ads
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_ads);
		mGridview = (GridView) findViewById(R.id.gridviewAds);
		requestUserAds();
	}

	/**
	 * Stub method for now to get user id
	 * @param accessToken
	 * @return
	 */
	private String getUserId(String accessToken) {
		return "3";
	}
	
	private void requestUserAds() {
		Session session = Session.getActiveSession();
		final String user_id = getUserId(session.getAccessToken());
		RequestParams params = new RequestParams();
		
		// No params means just getting the most popular ads
		params.put("user_id", user_id);
		MapleHttpClient.get("posts", params, new AsyncHttpResponseHandler(){
			// Example json response: http://maplesyrup.herokuapp.com/posts?user_id=3
			@Override
			public void onSuccess(int statusCode, String response) {
				Log.d(TAG, response);	
				try {
					JSONArray jObjectAds = new JSONArray(response);
					mGridview.setAdapter(new ImageAdapter(getApplicationContext(), jObjectAds));
					Log.d(TAG, "Number ads created for user_id " + user_id + ": " + jObjectAds.length());
				} catch (JSONException e) {
					Log.d(TAG, "Could not parse JSON; unexpected response from the server.");	
					e.printStackTrace();
				}
			}
			
			@Override
		    public void onFailure(Throwable error, String response) {
				Toast.makeText(getApplicationContext(), "Sugar! We ran into a problem fetching user ads!", Toast.LENGTH_LONG).show();
		    }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * Respond to each tab button
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		return Utility.myOnOptionsItemSelected(this, item);
	}
	
}
