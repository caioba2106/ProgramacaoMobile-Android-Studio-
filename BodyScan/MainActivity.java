package com.example.bodyscan;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText valorPeso, valorAltura;
    TextView campoResultado;
    ImageView imagemCorpo;

    Uri imagemUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valorPeso = findViewById(R.id.edt_peso);
        valorAltura = findViewById(R.id.edt_altura);
        campoResultado = findViewById(R.id.txt_resultado);
        imagemCorpo = findViewById(R.id.img_corpo);

        Button calcular = findViewById(R.id.btn_calcular);
        Button camera = findViewById(R.id.btn_camera);

        calcular.setOnClickListener(v -> calcularIMC());

        camera.setOnClickListener(v -> abrirCamera());
    }

    private void calcularIMC() {

        String textoPeso = valorPeso.getText().toString().trim();
        String textoAltura = valorAltura.getText().toString().trim();

        if (textoPeso.isEmpty() || textoAltura.isEmpty()) {
            Toast.makeText(this, "Preencha peso e altura", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float peso = Float.parseFloat(
                    textoPeso.replace(",", "."));

            float altura = Float.parseFloat(
                    textoAltura.replace(",", "."));

            if (peso <= 0 || altura <= 0) {
                Toast.makeText(this, "Digite valores positivos", Toast.LENGTH_SHORT).show();
                return;
            }

            float imc = peso / (altura * altura);

            String classificacao;

            if (imc < 18.5) {
                classificacao = "Baixo peso";
            } else if (imc < 25) {
                classificacao = "Peso normal";
            } else if (imc < 30) {
                classificacao = "Sobrepeso";
            } else if (imc < 35) {
                classificacao = "Obesidade grau I";
            } else if (imc < 40) {
                classificacao = "Obesidade grau II";
            } else {
                classificacao = "Obesidade grau III";
            }

            campoResultado.setText(
                    String.format(java.util.Locale.getDefault(), "IMC: %.2f\nClassificação: %s", imc, classificacao)
            );

        }

        catch (NumberFormatException e) {
            Toast.makeText(this, "Digite números válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirCamera() {

        ContentValues valores = new ContentValues();

        valores.put(MediaStore.Images.Media.DISPLAY_NAME, "BodyScan_" + System.currentTimeMillis() + ".jpg");

        valores.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        valores.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BodyScan");

        imagemUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores);

        if (imagemUri == null) {
            Toast.makeText(this, "Não foi possível salvar a imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(
                MediaStore.EXTRA_OUTPUT, imagemUri);

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivityForResult(intent, 100);

        }

        catch (Exception e) {
            getContentResolver().delete(imagemUri, null, null);

            imagemUri = null;

            Toast.makeText(this, "Câmera não disponível", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {

            if (resultCode == RESULT_OK && imagemUri != null) {

                imagemCorpo.setImageURI(imagemUri);

                Toast.makeText(this, "Fotografia salva com sucesso!", Toast.LENGTH_SHORT).show();

            }

            else {

                if (imagemUri != null) {
                    getContentResolver().delete(imagemUri, null, null);
                }

                Toast.makeText(this, "Fotografia cancelada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
