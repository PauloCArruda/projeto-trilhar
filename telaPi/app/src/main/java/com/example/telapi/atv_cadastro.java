package com.example.telapi;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.DatePickerDialog;
import android.app.assist.AssistStructure;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class atv_cadastro extends AppCompatActivity implements View.OnClickListener {

    Button btnGravar, btnExcluir, btnVencimento;
    ImageButton btnVoltar;
    EditText edtDescricao, edtValor;
    String acao;
    Despesa d;
    DespesaDao dao;
    Calendar calendar;
    CheckBox chkPago;

    private void criarComponentes(){
        btnGravar = findViewById(R.id.btnGravar);
        btnGravar.setOnClickListener(this);
        btnGravar.setText(acao);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(this);

        btnExcluir = findViewById(R.id.btnExcluir);
        btnExcluir.setOnClickListener(this);

        if(acao.equals("Inserir"))
            btnExcluir.setVisibility(View.INVISIBLE);
        else btnExcluir.setVisibility(View.VISIBLE);

        edtDescricao = findViewById(R.id.edtDescricao);
        edtValor = findViewById(R.id.edtValor);
        btnVencimento = findViewById(R.id.btnVencimento);
        btnVencimento.setOnClickListener(this);
        calendar = Calendar.getInstance();
        chkPago = findViewById(R.id.chkPago);
        chkPago.setOnCheckedChangeListener((buttonView, isChecked) -> {
            d.setPago(isChecked);
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.atv_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        acao = getIntent().getExtras().getString("acao");
        dao = new DespesaDao(this);
        criarComponentes();

        if(getIntent().getExtras().getSerializable("obj")!= null){
            d = (Despesa) getIntent().getExtras().getSerializable("obj");
            edtDescricao.setText(d.getDescricao());
            edtValor.setText(String.valueOf(d.getValor()));
        }
    }
    private void mostrarDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        // Cria o DatePickerDialog e define o estilo diretamente
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Atualiza o Calendar com a data selecionada
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Formata a data para exibir no botão de vencimento
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dataSelecionada = dateFormat.format(calendar.getTime());
                        btnVencimento.setText(dataSelecionada);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Exibe o DatePickerDialog
        datePickerDialog.show();
    }
    @Override
    public void onClick(View v){
        if (v==btnVoltar){
            finish();
        } else if (v == btnExcluir) {
            long id = dao.excluir(d);
            Toast.makeText(this, "Despesa" + d.getDescricao() + " foi excluido com sucesso!", Toast.LENGTH_LONG).show();
            finish();
        } else if (v==btnVencimento){
            mostrarDatePickerDialog();
        } else if (v == btnGravar) {
            d.setDescricao(edtDescricao.getText().toString());
            d.setValor(Double.parseDouble(edtValor.getText().toString()));
            boolean pago = chkPago.isChecked();
            d.setPago(pago);
            String novaDataVencimentoStr = btnVencimento.getText().toString();

            if (!novaDataVencimentoStr.isEmpty()) {
                // Converter a string da nova data de vencimento para um objeto Date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date novaDataVencimento = null;
                try {
                    novaDataVencimento = sdf.parse(novaDataVencimentoStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Atualizar a data de vencimento no objeto Despesa
                d.setVencimento(novaDataVencimento);
            }

            if (acao.equals("Inserir")){
                long id = dao.inserir(d);
                Toast.makeText(this,"Despesa"+ d.getDescricao()+" foi inserido com sucesso"+ id,Toast.LENGTH_LONG).show();
                 }
            else {
                long id = dao.alterar(d);
                Toast.makeText(this,"Despesa"+d.getDescricao()+" foi alterado com sucesso!",Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }
}