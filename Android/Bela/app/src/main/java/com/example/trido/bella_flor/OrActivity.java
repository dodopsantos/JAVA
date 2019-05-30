package com.example.trido.bella_flor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trido.bella_flor.adapter.OrderListAdapter;
import com.example.trido.bella_flor.dao.configFirebase;
import com.example.trido.bella_flor.entities.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.trido.bella_flor.LoginActivity.LOGIN;

public class OrActivity extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;
    private ListView listViewOrder;
    private ArrayAdapter adapter;
    private ArrayList<Order> ors;
    private DatabaseReference fireBase;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    String fl = null;
    Uri URI = null;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);

    public static Order ORD = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_or);

        ors = new ArrayList<>();
        listViewOrder  = (ListView) findViewById(R.id.listItensOrde);
        adapter = new OrderListAdapter(this, ors);
        listViewOrder.setAdapter(adapter);
        fireBase = configFirebase.getfireBase().child("Order");

        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Order order = ors.get(position);

                ORD = ors.get(position);

                if (order.getStatus().equals("Pendente")){
                    showOrderDialog(order.getId(), order.getName(), ORD);
                }else{
                    System.out.println("IUJ: "+order.getStatus());
                    showOrderDelete(order.getId());
                }


            }
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ors.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Order cl = data.getValue(Order.class);
                    if (cl.getSalesman().equals(LOGIN.getLogin()) || LOGIN.getLogin().equals("jefferson")) {
                        if (cl.getStatus().equals("Finalizado")) {

                        } else {
                            ors.add(cl);
                        }
                    }

                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void showOrderDelete(final String id) {
        AlertDialog.Builder dialobBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.order_finish,null);

        dialobBuilder.setView(dialogView);

        final Button btnFinish = (Button) dialogView.findViewById(R.id.btnEnd);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ORD.getStatus().equals("Pendente")){
                    ORD.setStatus("Autorizado");
                    SaveOrder(ORD);
                }else if (ORD.getStatus().equals("Autorizado")){
                    ORD.setStatus("Finalizado");
                    SaveOrder(ORD);
                }

                generatePDF();
                //
            }
        });

        dialobBuilder.setTitle("Deseja finalizar o predido ?");

        AlertDialog alertDialog = dialobBuilder.create();
        alertDialog.show();
    }

    private boolean SaveOrder(Order or){
        try {

            firebase = configFirebase.getfireBase().child("Order");
            firebase.child(or.getId()).setValue(or);
            Toast.makeText(OrActivity.this,"Pedido Finalizado!", Toast.LENGTH_LONG).show();

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void SendEmail(){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("mailto"));
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"tridops@gmail.com, jeffersonrepresentacoes@hotmail.com"/*ORD.getEmail()+", jnxindustria@gmail.com, pereira230156@gmail.com"*/});
        email.putExtra(Intent.EXTRA_SUBJECT,
                "Pedido: ");

        File f = new File(fl);
            email.putExtra(Intent.EXTRA_STREAM, URI.fromFile(f));

        startActivity(Intent.createChooser(email, "ENVIAR E-MAIL"));
        finish();

    }

    private void showOrderDialog(final String id, String name, Order ord) {
        AlertDialog.Builder dialobBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.order_dialog,null);

        dialobBuilder.setView(dialogView);

        final Button btnConfirm = (Button) dialogView.findViewById(R.id.btnConfirm);
        final Button btnFinish = (Button) dialogView.findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(id);

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UpdateOrderActivity.class);
                startActivity(intent);
                finish();

            }
        });

        dialobBuilder.setTitle("Pedido de "+ name);

        AlertDialog alertDialog = dialobBuilder.create();
        alertDialog.show();
    }

    private void deleteOrder(String orderID) {
        DatabaseReference drOrder = FirebaseDatabase.getInstance().getReference("Order").child(orderID);
        drOrder.removeValue();

        Toast.makeText(this, "Pedido deletado", Toast.LENGTH_LONG).show();
        Del();
    }
    private void Del(){
        Intent intent = new Intent(OrActivity.this, OrActivity.class);
        startActivity(intent);
        finish();
    }
    private void generatePDF(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){

                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, STORAGE_CODE);

            }else {
                savePDF();
            }
        }else {
            savePDF();
        }
    }

    private void savePDF(){
        Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = Environment.getExternalStorageDirectory()+"/"+mFileName+".pdf";
        fl = mFilePath;

        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();

            addContent(mDoc);

            mDoc.close();

            SendEmail();

            Toast.makeText(this, mFileName+".pdf\nSalvo em\n"+mFilePath, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addContent(Document document) throws DocumentException {

        try {
            Drawable d = getResources().getDrawable(R.drawable.logologin);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(100,100);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);
        }catch (Exception e){

        }

        Paragraph catPart = new Paragraph();
        createTable(catPart);

        document.add(catPart);

    }
    private static void createTable(Paragraph subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(10);

        PdfPCell c1 = new PdfPCell(new Phrase("JNX INDUSTRIA DE ALIMENTOS LTDA"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(10);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("BELLA FLOR ALIMENTOS"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(10);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("LAGES S.C."));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(10);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("TELEFONE (49)3224-6778"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(10);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(10);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("PEDIDO DE VENDA"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(10);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(10);
        table.addCell(c1);

        // Linha 1
        c1 = new PdfPCell(new Phrase("Data Pedido:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getTime()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(3);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Representante"));
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getSalesman().toUpperCase()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(3);
        table.addCell(c1);

        // Linha 2
        c1 = new PdfPCell(new Phrase("Razão Social:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getName().toUpperCase()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(8);
        table.addCell(c1);

        // Linha 3
        c1 = new PdfPCell(new Phrase("CNPJ:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getCnpj()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("IE:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getIe()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(4);
        table.addCell(c1);

        // Linha 4
        c1 = new PdfPCell(new Phrase("END:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getPublic_place()+", "+ORD.getNumber()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Bairro:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getnBorhood()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(4);
        table.addCell(c1);

        // Linha 5
        c1 = new PdfPCell(new Phrase("Cidade:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getCountry()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(3);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Estado"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getUf()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CEP:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getCep()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        // Linha 6
        c1 = new PdfPCell(new Phrase("Fone:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getPhone()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(4);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("E-mail:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getEmail()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(4);
        table.addCell(c1);

        // Linha 7
        c1 = new PdfPCell(new Phrase("Forma pagamento:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(3);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getPaymentForm()+" - "+ORD.getPaymentLong()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(3);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Cheq.:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        if (ORD.getPaymentType().equals("Cheque")){
            c1 = new PdfPCell(new Phrase(" ( x ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }else{
            c1 = new PdfPCell(new Phrase(" ( ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }


        c1 = new PdfPCell(new Phrase("Boleto:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        if (ORD.getPaymentType().equals("Boleto")){
            c1 = new PdfPCell(new Phrase(" ( x ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }else{
            c1 = new PdfPCell(new Phrase(" ( ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        // Linha 8
        c1 = new PdfPCell(new Phrase("Regime especial de tributação:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(5);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" - "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Com N"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        if (ORD.getInvoice().equals("CNF")){
            c1 = new PdfPCell(new Phrase(" ( x ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }else{
            c1 = new PdfPCell(new Phrase(" (  ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        c1 = new PdfPCell(new Phrase("Sem N"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        if (ORD.getInvoice().equals("SNF")){
            c1 = new PdfPCell(new Phrase(" ( x ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }else{
            c1 = new PdfPCell(new Phrase(" (  ) "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        // Linha 9
        c1 = new PdfPCell(new Phrase("OBS.:"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getDescription()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(9);
        table.addCell(c1);

        // Linha divisória
        c1 = new PdfPCell(new Phrase("Des.Produto"));
        c1.setBackgroundColor(new BaseColor(59, 206, 206));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Qtd."));
        c1.setBackgroundColor(new BaseColor(59, 206, 206));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Desc."));
        c1.setBackgroundColor(new BaseColor(59, 206, 206));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Valor unitário"));
        c1.setBackgroundColor(new BaseColor(59, 206, 206));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Valor bruto"));
        c1.setBackgroundColor(new BaseColor(59, 206, 206));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Valor liquido"));
        c1.setBackgroundColor(new BaseColor(59, 206, 206));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        // Linha 1
        c1 = new PdfPCell(new Phrase(""+ORD.getType()+" "+ORD.getWeight()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(""+ORD.getSize()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" - "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("R$"+ORD.getPricePU1()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("R$"+ORD.getPricePT1()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("R$"+ORD.getPricePT1()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        // Linha 2
        if (ORD.getType2() != null){

            c1 = new PdfPCell(new Phrase(""+ORD.getType2()+" "+ORD.getWeight2()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setColspan(2);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(""+ORD.getSize2()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(" - "));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("R$"+ORD.getPricePU2()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setColspan(2);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("R$"+ORD.getPricePT2()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setColspan(2);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("R$"+ORD.getPricePT2()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setColspan(2);
            table.addCell(c1);
        }

        // Linha Final
        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(new BaseColor(59, 206, 206));
        c1.setColspan(6);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total: R$"+ORD.getPrice()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(4);
        table.addCell(c1);

        subCatPart.add(table);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF();
                }else{
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireBase.removeEventListener(valueEventListener);
    }
}
