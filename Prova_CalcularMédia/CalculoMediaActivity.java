package com.example.calculonotamedia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class CalculomediaActivity extends AppCompatActivity {

    EditText notaP1, notaP2, notaPI, notaAtividades;
    TextView campo_media, campo_situacao;

    float p1, p2, pi, atividades, media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notaP1 = findViewById(R.id.edt_p1);
        notaP2 = findViewById(R.id.edt_p2);
        notaPI = findViewById(R.id.edt_pi);
        notaAtividades = findViewById(R.id.edt_atividades);

        campo_media = findViewById(R.id.txt_media);
        campo_situacao = findViewById(R.id.txt_situacao);

        Button calcular = findViewById(R.id.btn_calcular);

        calcular.setOnClickListener(v -> calcularMedia());
    }

    public void calcularMedia() {

        String textoP1 = notaP1.getText().toString().trim();
        String textoP2 = notaP2.getText().toString().trim();
        String textoPI = notaPI.getText().toString().trim();
        String textoAtividades = notaAtividades.getText().toString().trim();

        if (textoP1.isEmpty() || textoP2.isEmpty() || textoPI.isEmpty() || textoAtividades.isEmpty()) {

            if (textoP1.isEmpty()) {
                notaP1.setError("Digite a nota P1");
            }

            if (textoP2.isEmpty()) {
                notaP2.setError("Digite a nota P2");
            }

            if (textoPI.isEmpty()) {
                notaPI.setError("Digite a nota PI");
            }

            if (textoAtividades.isEmpty()) {
                notaAtividades.setError("Digite a nota de Atividades");
            }

            return;
        }

        try {

            p1 = Float.parseFloat(textoP1.replace(",", "."));
            p2 = Float.parseFloat(textoP2.replace(",", "."));
            pi = Float.parseFloat(textoPI.replace(",", "."));
            atividades = Float.parseFloat(textoAtividades.replace(",", "."));

            if (p1 < 0 || p1 > 10 || p2 < 0 || p2 > 10 || pi < 0 || pi > 10 || atividades < 0 || atividades > 10) {

                campo_situacao.setText("Digite notas de 0 a 10");
                campo_media.setText("Média: --");
                return;
            }

            media = (p1 * 30 + p2 * 35 + pi * 20 + atividades * 15) / 100;

            campo_media.setText(String.format(Locale.getDefault(), "Média: %.2f", media));

            if (media < 4.0) {
                campo_situacao.setText("Reprovado");

            } else if (media < 6.0) {
                campo_situacao.setText("Exame");

            } else {
                campo_situacao.setText("Aprovado");
            }

        } catch (NumberFormatException e) {
            campo_situacao.setText("Digite notas válidas");
        }
    }
}
