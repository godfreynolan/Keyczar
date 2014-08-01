package com.riis.restfulwebservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONObject;

import com.riis.restfulwebservice.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RestFulWebservice extends Activity {
	
	private String endpoint = "http://54.205.41.129:8080/RSAServer/do/decrypt/havepublic?text=";
	
	private String protocol;
	private String URL1 = "api.wunderground.com/api/";
	private String URL2 = "/conditions/q/MI/Troy.json";
	
	private SharedPreferences preferences;
	private String encryptedKey;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restfulwebservice);  
        
        PreferenceManager.setDefaultValues(this, R.xml.default_values, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
		encryptedKey = preferences.getString("EncryptedKey", null);
		
        final Button HTTP = (Button) findViewById(R.id.HTTP);
        HTTP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		        try {
		        	protocol = "http://";
		        	new WeatherCall().execute(encryptedKey);
		        } catch (Exception ex) {
				}
			}
        });
        
        final Button HTTPS = (Button) findViewById(R.id.HTTPS);
        HTTPS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		        try {
		        	protocol = "https://";
		        	new WeatherCall().execute(encryptedKey);
		        } catch (Exception ex) {
				}
			}
        }); 
    }
    
    private class WeatherCall extends AsyncTask<String, Void, Void> {
    	
        private ProgressDialog dialog = new ProgressDialog(RestFulWebservice.this);
        String responseData = "";
        
        private String fetchData(String URL) {
    		try {
    			HttpResponse response = (new DefaultHttpClient()).execute(new HttpGet(URL), (new BasicHttpContext()));	
    			StringBuilder builder = new StringBuilder();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    			String line;
    			while((line = reader.readLine()) != null) {
    				builder.append(line);
    			}
    			response.getEntity().consumeContent();
    			return builder.toString();
    		} catch (Exception ex) {
    			return ex.getMessage();
    		}
        }
        
        protected void onPreExecute() {
            dialog.setMessage("Please wait..");
            dialog.show();
            ((TextView) findViewById(R.id.output)).setText("");
        }
 
        protected Void doInBackground(String... params) {
        	try {
				responseData = fetchData(protocol + URL1 + new JSONObject(fetchData(endpoint + params[0])).getString("result") + URL2);
			} catch (Exception ex) {
			}
    		return null;
        }
         
        protected void onPostExecute(Void unused) {
        	try {
        		JSONObject observation = new JSONObject(responseData).getJSONObject("current_observation");
        		String result = "";
				result += "Location: " + observation.getJSONObject("display_location").getString("full") + "\n";
				result += "Weather: " + observation.getString("weather") + "\n";
				result += "Temperature: " + observation.getString("temperature_string");
	            ((TextView) findViewById(R.id.output)).setText(result);
			} catch (Exception ex) {
	            ((TextView) findViewById(R.id.output)).setText(responseData);
			}
            dialog.dismiss();
        }
    }
}
