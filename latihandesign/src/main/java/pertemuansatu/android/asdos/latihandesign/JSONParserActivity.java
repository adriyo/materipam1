package pertemuansatu.android.asdos.latihandesign;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class JSONParserActivity extends ActionBarActivity {
    String url = "http://192.168.137.1/ws_android/index.php";
    ArrayAdapter adapter;
    ArrayList<String> listJSON = new ArrayList<String>();
    ListView listView;
    String response = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonparser);
        listView = (ListView) findViewById(R.id.listViewDataJSON);
        //new JSONParser().execute();
        showFromFile();
    }

    public void showFromFile(){
        String json = null;
        InputStream ins = null;
        try{
            ins = getApplicationContext().getAssets().open("datamahasiswa.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            json = sb.toString();
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("mahasiswa");
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        listJSON.add(data.getString("nim") + "\n" + data.getString("nama") + "\n" + data.get("progdi"));
                    }
                } catch (JSONException jse) {
                    jse.printStackTrace();
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
                adapter = new ArrayAdapter(JSONParserActivity.this, android.R.layout.simple_list_item_1, listJSON);
                listView.setAdapter(adapter);
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    class JSONParser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            String json = null;
            try {
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = null;
                HttpEntity httpEntity = null;
                httpResponse = defaultHttpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                json = EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("mahasiswa");
                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        listJSON.add(data.getString("nim") + "\n" + data.getString("nama") + "\n" + data.get("progdi"));
                    }
                } catch (JSONException jse) {
                    jse.printStackTrace();
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter = new ArrayAdapter(JSONParserActivity.this, android.R.layout.simple_list_item_1, listJSON);
            listView.setAdapter(adapter);
        }

    }
}
