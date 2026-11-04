package com.example.bodyscan;

import android.os.Bundle;
import android.content.Intent;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView fotoFrontal, fotoLateral;
    EditText campoPeso, campoAltura;
    TextView campoResultado;
    Button btnFrontal, btnLateral, btnAvaliar;
    Uri imagemAtual;
    ImageView imagemSelecionada;
  
    ActivityResultLauncher<Intent> abrirCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoFrontal = findViewById(R.id.img_frontal);
        fotoLateral = findViewById(R.id.img_lateral);

        campoPeso = findViewById(R.id.edt_peso);
        campoAltura = findViewById(R.id.edt_altura);

        campoResultado = findViewById(R.id.txt_resultado);

        btnFrontal = findViewById(R.id.btn_foto_frontal);
        btnLateral = findViewById(R.id.btn_foto_lateral);
        btnAvaliar = findViewById(R.id.btn_avaliar);

        // Abrir a câmera do celular

        abrirCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {

                    if (resultado.getResultCode() == RESULT_OK) {
                        if (imagemAtual != null) {
                            imagemSelecionada.setImageURI(imagemAtual);

                            // Finaliza o salvamento da imagem
                            if (Build.VERSION.SDK_INT >= 29) {
                                ContentValues valores = new ContentValues();
                                valores.put(MediaStore.Images.Media.IS_PENDING, 0);

                                getContentResolver().update(
                                        imagemAtual,
                                        valores,
                                        null,
                                        null
                                );
                            }

                            Toast.makeText(this, "Foto salva na galeria!", Toast.LENGTH_SHORT).show();
                        }

                    } 
                    
                    else {

                        // Exclui a imagem vazia se a câmera for cancelada
                        if (imagemAtual != null) {
                            getContentResolver().delete(imagemAtual, null, null);
                            imagemAtual = null;
                        }

                        Toast.makeText(this, "Fotografia cancelada.", Toast.LENGTH_SHORT).show();
                    }
        });

        // Botão da foto frontal
      
        btnFrontal.setOnClickListener(v -> {
            imagemSelecionada = fotoFrontal;
            tirarFoto("BodyScan_Frontal");
        });

        // Botão da foto lateral
      
        btnLateral.setOnClickListener(v -> {
            imagemSelecionada = fotoLateral;
            tirarFoto("BodyScan_Lateral");
        });

        // Botão para calcular IMC
      
        btnAvaliar.setOnClickListener(v -> calcularIMC());

    }

    // Método para tirar e salvar as fotografias

    private void tirarFoto(String nomeImagem) {

        try {
            ContentValues valores = new ContentValues();

            valores.put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    nomeImagem + "_" + System.currentTimeMillis() + ".jpg"
            );

            valores.put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/jpeg"
            );

            if (Build.VERSION.SDK_INT >= 29) {
                valores.put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "Pictures/BodyScan"
                );

                valores.put(
                        MediaStore.Images.Media.IS_PENDING,
                        1
                );
            }

            imagemAtual = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    valores
            );

            if (imagemAtual == null) {
                Toast.makeText(this, "Não foi possível salvar a fotografia.", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imagemAtual);

            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            abrirCamera.launch(intent);

        } 
        catch (Exception e) {
            Toast.makeText(this, "Erro ao abrir a câmera: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Método para calcular o IMC

    private void calcularIMC() {

        String textoPeso = campoPeso.getText().toString().trim();
        String textoAltura = campoAltura.getText().toString().trim();

        if (textoPeso.isEmpty()) {
            campoPeso.setError("Digite seu peso");
            return;
        }

        if (textoAltura.isEmpty()) {
            campoAltura.setError("Digite sua altura");
            return;
        }

        try {

            float peso = Float.parseFloat(
                    textoPeso.replace(",", "."));

            float altura = Float.parseFloat(
                    textoAltura.replace(",", "."));

            if (peso <= 0 || altura <= 0) {
                Toast.makeText(this, "Digite valores maiores que zero.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (peso > 500 || altura > 3) {
                Toast.makeText(this, "Confira os valores informados.", Toast.LENGTH_SHORT).show();
                return;
            }

            float imc = peso / (altura * altura);

            String classificacao;

            if (imc < 18.5) {
                classificacao = "Abaixo do peso";
            } else if (imc < 25) {
                classificacao = "Peso adequado";
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
                    String.format(Locale.getDefault(),
                            "Resultado da Avaliação\n\n" +
                            "Peso: %.1f kg\n" +
                            "Altura: %.2f m\n" +
                            "IMC: %.2f\n\n" +
                            "Classificação: %s\n\n" +
                            "As fotografias foram salvas para " +
                            "consulta visual.",
                            peso, altura, imc, classificacao)
            );

        } 
        
        catch (NumberFormatException e) {
            Toast.makeText(this, "Digite valores numéricos válidos.", Toast.LENGTH_SHORT).show();
        }
    }
}
