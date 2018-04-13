package com.example.william.lab3.lab;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.william.lab3.PaisMapaActivity;
import com.example.william.lab3.R;

import java.util.ArrayList;
import java.util.List;

//import rest.example.com.rest.api.ApiClient;
//import rest.example.com.rest.model.Team;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    private List<String> wordList;
    private PaisesDataSource datasource;
    private ListView lstPaises;
    private EditText nomePais;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nomePais = findViewById(R.id.editText2);
        wordList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                wordList);

        datasource = new PaisesDataSource(this);
        datasource.open();
        for (Pais p: datasource.getAllPaises()) {
            wordList.add(p.getName());
        }

        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void getData(View view) {
        ApiClient.getCountryClient().getPais().enqueue(new Callback<List<Pais>>() {
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response ){
                if (response.isSuccessful() ) {
                    List<Pais> paises = response.body();
                    wordList.clear();
                    for (Pais pais : paises) {
                        List<Double> latLong = pais.getLatlng();
                        Double latitude = 0.0;
                        Double longitude = 0.0;
                        for (Double d: latLong) {
                            latitude = longitude;
                            longitude = d;
                        }
                        datasource.createPais(pais.getName(),Double.toString(latitude),Double.toString(longitude));
                        //Log.d("Teste",datasource.getAllPaises().get(0).getName());

                        wordList.add(pais.getName());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }

    public void entrarMap(View view){
        Intent intent = new Intent(this,PaisMapaActivity.class);
        //Pais p = datasource.getPais(nomePais.getText().toString());
        int index = wordList.indexOf(nomePais.getText().toString());
        if(index != -1){
            intent.putExtra("nome",datasource.getAllPaises().get(index).getName());
            intent.putExtra("lat",datasource.getAllPaises().get(index).getLatitude());
            intent.putExtra("long",datasource.getAllPaises().get(index).getLongitude());
            startActivity(intent);
        }
        else{
            Toast.makeText(this,"Nome de País digitado incorretamente. Digite Novamente!",Toast.LENGTH_SHORT);
        }

    }
}