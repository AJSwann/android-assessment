package test.employment.phototap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GitHub extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_github);
		new GithubConnect().execute();

	}

	public class GithubConnect extends AsyncTask<Void, Void, List<String>> {
		final String TAG = "AsyncTaskParseJson.java";
		String url = "https://api.github.com/repos/rails/rails/commits";
		// ?
		JSONArray dataJsonArr = null;

		protected List<String> doInBackground(Void... arg0) {
			List<String> result = new ArrayList<String>();
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
				InputStream is = conn.getInputStream();
				BufferedReader r = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				for (String line = r.readLine(); line != null; line = r
						.readLine()) {
					sb.append(line);
				}
				JSONArray jsonArray = new JSONArray(sb.toString());
				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject currentObj = jsonArray.getJSONObject(i);
					String sha = currentObj.getString("sha");

					JSONObject objCommit = currentObj.getJSONObject("commit");
					String commitMessage = objCommit.getString("message");

					JSONObject objAuthor = objCommit.getJSONObject("author");
					String authorName = objAuthor.getString("name");

					result.add(authorName + " " + sha + " - " + commitMessage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		protected void onPostExecute(List<String> list) {
			ListView lv = (ListView) findViewById(R.id.list);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					GitHub.this, android.R.layout.simple_list_item_1, list);
			lv.setAdapter(adapter);
		}

	}
}