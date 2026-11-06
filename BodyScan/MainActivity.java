package com.example.bodyscan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imagemCorpo;
    Button btnCamera, btnLimpar;
    TextView resultado;

    Bitmap fotografia;

    ActivityResultLauncher<Void> abrirCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagemCorpo = findViewById(R.id.img_corpo);
        btnCamera = findViewById(R.id.btn_camera);
        btnLimpar = findViewById(R.id.btn_limpar);
        resultado = findViewById(R.id.txt_resultado);

        // Preparar a abertura da camera
        abrirCamera = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {

                    if (bitmap != null) {

                        fotografia = bitmap;

                        imagemCorpo.setImageBitmap(fotografia);

                        resultado.setText(
                                "Fotografia capturada com sucesso!\n" +
                                        "Utilize esta imagem para acompanhar " +
                                        "sua evolução física."
                        );

                    } else {

                        Toast.makeText(
                                MainActivity.this,
                                "Nenhuma fotografia foi capturada.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        // Botao para abrir a camera
        btnCamera.setOnClickListener(v -> {

            abrirCamera.launch(null);

        });

        // Botao para limpar a fotografia
        btnLimpar.setOnClickListener(v -> {

            fotografia = null;

            imagemCorpo.setImageResource(
                    android.R.drawable.ic_menu_camera
            );

            resultado.setText(
                    "Nenhuma fotografia capturada."
            );

            Toast.makeText(
                    MainActivity.this,
                    "Fotografia removida da tela.",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }
}
