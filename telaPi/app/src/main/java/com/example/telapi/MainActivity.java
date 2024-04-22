package com.example.telapi;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView lstDespesas;
    Spinner spnMeses;
    ImageButton btnAdicionar;
    EditText edtTotal, edtSaldo;

    List<Despesa> listaDespesa = new ArrayList<>();
    ListAdapter listAdapter;
    int indice;
    DespesaDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.activity_main);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(this);

        edtTotal = findViewById(R.id.edtTotal);
        edtSaldo = findViewById(R.id.edtSaldo);

        lstDespesas = findViewById(R.id.lstDespesas);
        lstDespesas.setOnItemClickListener(this);
        dao = new DespesaDao(this);
        atualizarLista();
    }
    @Override
    protected void onResume(){
        super.onResume();
        atualizarLista();
        atualizarTotalDespesas();
    }
    private void atualizarTotalDespesas() {
        float totalDespesas = dao.somaValores();
        edtTotal.setText(String.valueOf(totalDespesas));
    }
    public void atualizarLista() {
        listaDespesa = dao.listar();
        listAdapter = new ArrayAdapter<Despesa>(this, android.R.layout.simple_list_item_1, listaDespesa);
        lstDespesas.setAdapter(listAdapter);
    }
    @Override
    public void onClick(View v){
        if(v == btnAdicionar){
            Despesa d = new Despesa();
            d.setId(0L);
            abrirCadastro("Inserir", d);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        indice = position;
        Despesa d = (Despesa) lstDespesas.getItemAtPosition(position);
        abrirCadastro("Alterar", d);
    }

    private void abrirCadastro(String acao, Despesa obj){
        Intent telaCad = new Intent(this, atv_cadastro.class);
        telaCad.putExtra("acao", acao);
        telaCad.putExtra("obj", obj);
        startActivity(telaCad);
    }

}