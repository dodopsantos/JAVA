package br.edu.ifc.videira.crud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.UUID;

import br.edu.ifc.videira.crud.dao.configFirebase;
import br.edu.ifc.videira.crud.entities.Ad;

public class AnnounceActivity extends AppCompatActivity {

    private Button btnStep1, btnStep2, btnStep3;
    private EditText edtPrice, edtDescription, edtAdress, edtPhone, edtEmail;
    private Spinner edtType;
    private Ad ad;
    private DatabaseReference firebase;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private Button photoButton;
    private ImageView photo1;
    private Uri filePath;

    private String refer;

    final static int IMPORT_PICTURE = 1;
    //25/11
    private Spinner spinnerType;
    private Spinner spinnerModel;
    private Spinner spinnerBrand;
    private Spinner spinnerYear;
    private Spinner fipePrice;
    //final Button btnStep1 = findViewById(R.id.bntNextAnounce);
    private RequestQueue mQueue;
    private String brandId;
    private String urlGlobal;
    private String typeText;
    private String modelId;
    private String urlToYearF;
    private String urlToYearP;
    private String urlYear;

    private ArrayAdapter<String> brandAdapter;
    private ArrayAdapter<String> typeAdapter;
    private ArrayAdapter<String> modelAdapter;
    private ArrayAdapter<String> yearAdapter;
    private ArrayAdapter<String> fipePriceAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anounce01);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnStep1 = (Button) findViewById(R.id.bntNextAnounce);
        edtType = (Spinner) findViewById(R.id.spinnerType);
        edtPrice = (EditText) findViewById(R.id.editTextPrice);
        spinnerType = (Spinner)findViewById(R.id.spinnerType);
        spinnerModel= (Spinner) findViewById(R.id.spinnerModel);
        spinnerBrand= (Spinner) findViewById(R.id.spinnerBrand);
        spinnerYear= (Spinner) findViewById(R.id.spinnerYear);
        fipePrice= (Spinner) findViewById(R.id.fipePrice);

        brandAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        modelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        fipePriceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        mQueue = Volley.newRequestQueue(this);

        urlGlobal = "http://fipeapi.appspot.com/api/1/";
        typeText = null;
        brandId = null;
        modelId=null;
        urlToYearF=null;
        urlToYearP=null;
        urlYear=null;
        callType();

        btnStep1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ad = new Ad();

                if (spinnerType.getSelectedItem().toString().equals("SELECIONE O TIPO DO VEÍCULO") ||
                        spinnerBrand.getSelectedItem().toString().equals("SELECIONE A MARCA DO VEÍCULO")||
                        spinnerModel.getSelectedItem().toString().equals("SELECIONE O MODELO DO VEÍCULO")||
                        spinnerYear.getSelectedItem().toString().equals("SELECIONE O ANO DO VEÍCULO")||
                        edtPrice.getText().toString().equals("")) {
                    Toast.makeText(AnnounceActivity.this,"Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else {
                    ad.setType(spinnerType.getSelectedItem().toString());
                    ad.setBrand(spinnerBrand.getSelectedItem().toString());
                    ad.setModel(spinnerModel.getSelectedItem().toString());
                    ad.setYear(spinnerYear.getSelectedItem().toString());
                    ad.setPrice(edtPrice.getText().toString());
                   // setContentView(R.layout.activity_anounce02);
                    Step2();
                }
            }
        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String typeTextLocal = spinnerType.getSelectedItem().toString();

                if(typeTextLocal == "SELECIONE O TIPO DO VEÍCULO"){
                    Toast.makeText(AnnounceActivity.this,"Selecione o tipo do veículo!", Toast.LENGTH_LONG).show();
                }else if(typeTextLocal.toLowerCase() !="SELECIONE O TIPO DO VEÍCULO"){
                    typeText = typeTextLocal.toLowerCase();
                    typeTextLocal = urlGlobal.concat(typeTextLocal.toLowerCase());
                    callBrand(typeTextLocal);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String brandText = spinnerBrand.getSelectedItem().toString();

                final String brand2 = brandText;
                Log.d("brandText",brand2);

                if(brandText == "SELECIONE A MARCA DO VEÍCULO"){
                    Toast.makeText(AnnounceActivity.this,"Selecione a marca do veículo!", Toast.LENGTH_LONG).show();
                }else {

                    String type = typeText;



                    String url = urlGlobal.concat(type).concat("/marcas.json");
                    Log.d("urlTeste",url);


                    final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray jsonArray) {

                                    try {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String brand = jsonArray.getJSONObject(i).getString("name");
                                            String id;

                                            Log.d("brandSHITES",brand);

                                            if(brand.equals(brandText.toUpperCase()) ) {

                                                id =  jsonArray.getJSONObject(i).getString("id");
                                                Log.d("iddd",id);
                                                callModel(id);

                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } ,new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    mQueue.add(request);

                }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String modelText = spinnerModel.getSelectedItem().toString();

                if(modelText == "SELECIONE O MODELO DO VEÍCULO"){
                    Toast.makeText(AnnounceActivity.this,"Selecione o modelo do veículo!", Toast.LENGTH_LONG).show();
                }else {

                    String url = urlToYearF.concat(".json");
                    Log.d("urltoYeasrrr",url);

                    final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray jsonArray) {

                                    try {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String model = jsonArray.getJSONObject(i).getString("name").toUpperCase();
                                            String id;

                                            if(model.equals(modelText.toUpperCase()) ) {
                                                id =  jsonArray.getJSONObject(i).getString("id");
                                                callYear(id);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } ,new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    mQueue.add(request);
                }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String yearText = spinnerYear.getSelectedItem().toString();

                if(yearText == "SELECIONE O ANO DO VEÍCULO"){
                    Toast.makeText(AnnounceActivity.this,"Selecione o modelo do veículo!", Toast.LENGTH_LONG).show();
                }else {

                    String url = urlYear.concat(".json");
                    Log.d("urltoYeasrrr",url);

                    final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray jsonArray) {

                                    try {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String year = jsonArray.getJSONObject(i).getString("name").toUpperCase();
                                            String price;

                                            if(year.equals(yearText.toUpperCase()) ) {
                                                price =  jsonArray.getJSONObject(i).getString("id");
                                                callFipePrice(price);

                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } ,new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    mQueue.add(request);

                }}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void callType() {
        typeAdapter.clear();
        spinnerType.setAdapter(null);
        spinnerType.setAdapter(typeAdapter);
        typeAdapter.add("SELECIONE O TIPO DO VEÍCULO");
        typeAdapter.add("CARROS");
        typeAdapter.add("MOTOS");
        typeAdapter.add("CAMINHOES");
        typeAdapter.notifyDataSetChanged();

    }


    private void callBrand(String typeText) {
        Log.d("brandcall",typeText);
        String url = typeText.concat("/marcas.json");
        Log.d("brandcall",url);
        brandAdapter.clear();
        spinnerBrand.setAdapter(null);
        spinnerBrand.setAdapter(brandAdapter);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        try {

                            brandAdapter.add("SELECIONE A MARCA DO VEÍCULO");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String brand = jsonArray.getJSONObject(i).getString("name");

                                brandAdapter.add(brand);

                            }

                            brandAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void callModel(String brand) {
        String url = urlGlobal.concat(typeText).concat("/veiculos/").concat(brand).concat(".json");
        urlToYearP = urlGlobal.concat(typeText).concat("/veiculo/").concat(brand);
        urlToYearF = urlGlobal.concat(typeText).concat("/veiculos/").concat(brand);
        //urlToYearF = url;

        modelAdapter.clear();
        spinnerModel.setAdapter(null);
        spinnerModel.setAdapter(modelAdapter);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        try {

                            modelAdapter.add("SELECIONE O MODELO DO VEÍCULO");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String brand = jsonArray.getJSONObject(i).getString("name");

                                modelAdapter.add(brand);
                            }

                            modelAdapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void callYear(String year) {

        String url = urlToYearP.concat("/").concat(year).concat(".json");
        urlYear = urlToYearP.concat("/").concat(year);
        yearAdapter.clear();
        spinnerYear.setAdapter(null);
        spinnerYear.setAdapter(yearAdapter);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        try {

                            yearAdapter.add("SELECIONE O ANO DO VEÍCULO");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String year = jsonArray.getJSONObject(i).getString("name");

                                yearAdapter.add(year);
                            }

                            yearAdapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void callFipePrice(final String price){

        String url = urlYear.concat("/").concat(price).concat(".json");
        Log.d("jhasdhash",url);


        fipePriceAdapter.clear();
        fipePrice.setAdapter(null);
        fipePrice.setAdapter(fipePriceAdapter);




        /*JsonArrayRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        fipePriceAdapter.add(price);
                        fipePriceAdapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        mQueue.add(request);
        */

    }

    private void Step2(){
        setContentView(R.layout.activity_anounce02);

        photoButton = findViewById(R.id.addphotobutton);
        photo1 = findViewById(R.id.photoview1);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selecione a Imagem"), IMPORT_PICTURE);
            }
        });

        btnStep2 = (Button) findViewById(R.id.NEXT02);
        edtDescription = (EditText) findViewById(R.id.editText);

        btnStep2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (filePath == null){
                    Toast.makeText(AnnounceActivity.this,"Adicione uma imagem", Toast.LENGTH_LONG).show();
                } else if (edtDescription.getText().toString().equals("")){
                    Toast.makeText(AnnounceActivity.this,"Preencha a descrição", Toast.LENGTH_LONG).show();
                } else {
                    uploadImage();
                    ad.setDescription(edtDescription.getText().toString());
                    Step3();
                }
            }
        });
    }

    private void Step3(){
        setContentView(R.layout.activity_anounce03);

        btnStep3 = (Button) findViewById(R.id.FINISHBUTTON);
        edtAdress = (EditText) findViewById(R.id.editTextAdress);
        edtPhone = (EditText) findViewById(R.id.editTextPhone);
        edtEmail = (EditText) findViewById(R.id.editTextUserEmail);

        btnStep3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (edtAdress.getText().toString().equals("") ||
                        edtPhone.getText().toString().equals("")||
                        edtEmail.getText().toString().equals("")) {
                    Toast.makeText(AnnounceActivity.this,"Preencha os campos", Toast.LENGTH_LONG).show();
                } else {
                    downloadImage();
                }
            }
        });
    }
    private boolean SaveAd(Ad ad){
        try {

            firebase = configFirebase.getfireBase().child("Ad");
            firebase.child(ad.getAdress()).setValue(ad);
            Toast.makeText(AnnounceActivity.this,"Anuncio Cadastrado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void ReturnMain(){
        Intent intent = new Intent(AnnounceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void downloadImage(){
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sales-app-7f31e.appspot.com/").child("images/"+refer);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                ad.setPhoto1(uri.toString());
                ad.setAdress(edtAdress.getText().toString());
                ad.setPhone(edtPhone.getText().toString());
                ad.setEmail(edtEmail.getText().toString());

                SaveAd(ad);
                ReturnMain();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void uploadImage(){

        if (filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            refer = ref.toString().replace("gs://sales-app-7f31e.appspot.com/images/", "");

            Log.d("refer", refer);

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(AnnounceActivity.this,"Uploaded",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AnnounceActivity.this,"Failed"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMPORT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                photo1.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
