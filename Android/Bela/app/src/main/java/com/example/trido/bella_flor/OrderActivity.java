package com.example.trido.bella_flor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Client;
import com.example.trido.bella_flor.entities.Order;
import com.example.trido.bella_flor.entities.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class OrderActivity extends AppCompatActivity {

    private Order or;

    private EditText edtSize, edtSize2, obs;
    private Button btnOrder;
    private Spinner spinnerClient, spinnerProduct, spinnerProduct2, spinnerPaymentForm, spinnerPaymentLong;
    private CheckBox chckNfiscal, bolet, cheq;

    private DatabaseReference fireBase, fireBaseProduct;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener, valueEventListenerProduct;

    private ArrayList<Client> cls;
    private ArrayList<Product> pds, pds2;

    private ArrayAdapter productAdapter, clientAdapter, productAdapter2, adp, payLong;


    DecimalFormat df = new DecimalFormat();

    double y = 0, y2 = 0;
    double x = 0, x2 = 0;
    double sum = 0, comission = 0, sumP1, sumP2;
    String flag = null, flag2 = null, flagComission = null, flagP1 = null, flagP2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        obs = (EditText) findViewById(R.id.editTextObs);
        edtSize = (EditText) findViewById(R.id.editTextSize);
        edtSize2 = (EditText) findViewById(R.id.editTextSize2);
        btnOrder = (Button) findViewById(R.id.FINISHORDER);
        spinnerClient = (Spinner) findViewById(R.id.spinnerClient);
        spinnerProduct = (Spinner) findViewById(R.id.spinnerProduct);
        spinnerProduct2 = (Spinner) findViewById(R.id.spinnerProduct2);
        chckNfiscal = (CheckBox) findViewById(R.id.checkNfiscal);
        bolet = (CheckBox) findViewById(R.id.bolet);
        cheq = (CheckBox) findViewById(R.id.Cheq);
        spinnerPaymentForm = (Spinner) findViewById(R.id.spinnerPaymentForm);
        spinnerPaymentLong = (Spinner) findViewById(R.id.spinnerPaymentLong);

        fireBase = configFirebase.getfireBase().child("Client");
        fireBaseProduct = configFirebase.getfireBase().child("Product");

        cls = new ArrayList<>();
        pds = new ArrayList<>();
        pds2 = new ArrayList<>();

        clientAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        productAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        productAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        payLong = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        callClient();
        callProduct();
        callProduct2();
        callPaymentForm();

        spinnerPaymentForm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinnerPaymentForm.getSelectedItem().equals("FORMA DE PAGAMENTO")){
                    payLong.clear();
                    spinnerPaymentLong.setEnabled(false);

                }if(spinnerPaymentForm.getSelectedItem().equals("A Vista")){
                    spinnerPaymentLong.setEnabled(true);
                    payLong.clear();
                    payLong.add("7 dias");
                    spinnerPaymentLong.setAdapter(payLong);

                }if (spinnerPaymentForm.getSelectedItem().equals("A Prazo")){
                    spinnerPaymentLong.setEnabled(true);
                    payLong.clear();
                    payLong.add("7 dias");
                    payLong.add("14 dias");
                    payLong.add("21 dias");
                    payLong.add("28 dias");
                    payLong.add("7,14 dias");
                    payLong.add("21,28 dias");
                    spinnerPaymentLong.setAdapter(payLong);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chckNfiscal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){//Se está checado inicializa o TimerTask

                    adp.add("A Vista");
                    spinnerPaymentForm.setAdapter(adp);
                    spinnerPaymentForm.setEnabled(false);

                }else{
                    callPaymentForm();
                    spinnerPaymentForm.setEnabled(true);
                }
            }
        });

        bolet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){//Se está checado inicializa o TimerTask
                    cheq.setEnabled(false);

                }else{

                    cheq.setEnabled(true);
                }
            }
        });

        cheq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){//Se está checado inicializa o TimerTask
                    bolet.setEnabled(false);

                }else{

                    bolet.setEnabled(true);
                }
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                or = new Order();

                if (spinnerClient.getSelectedItem().toString().equals("SELECIONE O CLIENTE") ||
                        spinnerProduct.getSelectedItem().toString().equals("SELECIONE O PRODUTO")||
                        edtSize.getText().toString().equals("") || cheq.isChecked() == false && bolet.isChecked() == false) {
                    Toast.makeText(OrderActivity.this,"Preencha todos os campos", Toast.LENGTH_LONG).show();
                } else if (spinnerProduct2.getSelectedItem().toString().equals(spinnerProduct.getSelectedItem().toString())) {
                    Toast.makeText(OrderActivity.this,"Diferencie o produto", Toast.LENGTH_LONG).show();
                } else {

                        for(int i = 0;i<cls.size();i++){
                            if (cls.get(i).getName().equals(spinnerClient.getSelectedItem().toString())){

                                or.setName(cls.get(i).getName());
                                or.setUf(cls.get(i).getUf());
                                or.setPublic_place(cls.get(i).getPublic_place());
                                or.setIe(cls.get(i).getIe());
                                or.setNumber(cls.get(i).getNumber());
                                or.setCountry(cls.get(i).getCountry());
                                or.setCep(cls.get(i).getCep());
                                or.setCnpj(cls.get(i).getCnpj());
                                or.setEmail(cls.get(i).getEmail());
                                or.setnBorhood(cls.get(i).getNeighborhood());
                                or.setPhone(cls.get(i).getCel());
                            }
                        }
                        for(int j = 0; j < pds.size();j++){

                            if (pds.get(j).getType().equals(spinnerProduct.getSelectedItem().toString())){

                                df.applyPattern("0.##");

                                or.setType(pds.get(j).getType());
                                or.setWeight(pds.get(j).getWeight());
                                or.setPricePU1(pds.get(j).getPrice());

                                if (edtSize2.getText().length() == 0){

                                    x = Double.parseDouble(pds.get(j).getPrice());
                                    y = Double.parseDouble(edtSize.getText().toString());

                                    sum = x * y;
                                    System.out.println("VALOR: "+y+", "+x+" SOMA: "+sum);
                                    sumP1 = x * y;
                                    flagP1 = String.valueOf(df.format(sumP1));
                                    or.setPricePT1(flagP1);

                                }else{

                                    x = Double.parseDouble(pds.get(j).getPrice());
                                    y = Double.parseDouble(edtSize.getText().toString());

                                    sumP1 = x * y;
                                    flagP1 = String.valueOf(df.format(sumP1));
                                    or.setPricePT1(flagP1);

                                    or.setSize2(edtSize2.getText().toString());
                                    System.out.println("QTD: "+pds2.size());

                                    for (int j2 = 0; j2 < pds2.size(); j2++){

                                        if (pds2.get(j2).getType().equals(spinnerProduct2.getSelectedItem().toString())){

                                            or.setType2(pds2.get(j2).getType());
                                            or.setWeight2(pds2.get(j2).getWeight());
                                            or.setPricePU2(pds2.get(j2).getPrice());
                                            flag2 = pds2.get(j2).getPrice();
                                            x2 = Double.parseDouble(flag2);
                                            y2 = Double.parseDouble(edtSize2.getText().toString());
                                        }
                                    }
                                    sumP2 = x2 * y2;
                                    sum = (x * y) + (x2 * y2);

                                    System.out.println("SUMP2: "+sumP2);
                                }
                                comission = (sum * 5)/100;
                                df.applyPattern("0.##");
                                flagComission = String.valueOf(df.format(comission));
                                flag = String.valueOf(df.format(sum));
                                flagP1 = String.valueOf(df.format(sumP1));
                                flagP2 = String.valueOf(df.format(sumP2));

                                System.out.println("FLAGP2: "+flagP2);

                                System.out.println("PU1: "+flagComission+"INT: "+comission+"SUM: "+sum+ "Flag"+flag);
                                or.setStatus("Pendente");
                                or.setPrice(flag);
                                or.setCommission(flagComission);
                                or.setPricePT2(flag2);
                            }
                        }
                        or.setSize(edtSize.getText().toString());
                        or.setId(getCurrentTimeStamp());
                        or.setTime(getCurrentTimeStamp());
                        or.setSalesman(LOGIN.getLogin());
                        if (chckNfiscal.isChecked()){or.setInvoice("SNF");}else{or.setInvoice("CNF");}
                        if (bolet.isChecked()){or.setPaymentType("Boleto");}else if(cheq.isChecked()){or.setPaymentType("Cheque");}
                        or.setPaymentForm(spinnerPaymentForm.getSelectedItem().toString());
                        or.setPaymentLong(spinnerPaymentLong.getSelectedItem().toString());
                        or.setDescription(obs.getText().toString());

                        SaveOrder(or);
                        ReturnMain();
                }
            }
        });
    }


    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }


    private void ReturnMain(){
        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean SaveOrder(Order or){
        try {

            firebase = configFirebase.getfireBase().child("Order");
            firebase.child(or.getId()).setValue(or);
            Toast.makeText(OrderActivity.this,"Pedido Cadastrado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void callClient() {

        clientAdapter.clear();
        spinnerClient.setAdapter(null);
        spinnerClient.setAdapter(clientAdapter);
        clientAdapter.add("SELECIONA UM CLIENTE");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cls.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Client cl = data.getValue(Client.class);

                    if (cl.getSalesman().equals(LOGIN.getLogin())){
                        cls.add(cl);
                        clientAdapter.add(cl.getName());
                    }

                }
                clientAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }
    private void callProduct() {

        productAdapter.clear();
        spinnerProduct.setAdapter(null);
        spinnerProduct.setAdapter(productAdapter);
        productAdapter.add("SEL.");

        valueEventListenerProduct = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pds.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Product pd = data.getValue(Product.class);

                    if (pd.getSalesman().equals(LOGIN.getLogin())){
                        pds.add(pd);
                        pds2.add(pd);
                        productAdapter.add(pd.getType());
                    }

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    private void callProduct2() {

        productAdapter2.clear();
        spinnerProduct2.setAdapter(null);
        spinnerProduct2.setAdapter(productAdapter);

    }

    private void callPaymentForm(){

            final String[] str2 = {"FORMA DE PAGAMENTO","A Vista", "A Prazo"};
            ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, str2);
            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPaymentForm.setAdapter(adp);
            spinnerPaymentForm.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBase.addValueEventListener(valueEventListener);
        fireBaseProduct.addValueEventListener(valueEventListenerProduct);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireBase.removeEventListener(valueEventListener);
        fireBaseProduct.removeEventListener(valueEventListenerProduct);
    }
}
