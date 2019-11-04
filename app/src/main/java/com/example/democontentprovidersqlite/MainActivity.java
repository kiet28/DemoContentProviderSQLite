package com.example.democontentprovidersqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnQLTacGia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQLTacGia = findViewById(R.id.button_QLTacGia);
        btnQLTacGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dgAuthor = new Dialog(MainActivity.this);
                dgAuthor.setContentView(R.layout.author_editor);
                dgAuthor.setTitle("Quản lý tác giả");
                final EditText etAuthorID = dgAuthor.findViewById(R.id.editText_AuthorID);
                final EditText etAuthorName = dgAuthor.findViewById(R.id.editText_AuthorName);
                final EditText etAuthorAddress = dgAuthor.findViewById(R.id.editText_AuthorAddress);
                final EditText etAuthorEmail = dgAuthor.findViewById(R.id.editText_AuthorEmail);
                final GridView gvAuthor = dgAuthor.findViewById(R.id.gridView_Author);

                Button btnAuthorSave = dgAuthor.findViewById(R.id.button_AuthorSave);
                btnAuthorSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ContentValues author = new ContentValues();
                        try {
                            author.put("id_author", Integer.parseInt(etAuthorID.getText().toString()));
                            author.put("name", etAuthorName.getText().toString());
                            author.put("address", etAuthorAddress.getText().toString());
                            author.put("email", etAuthorEmail.getText().toString());
                            String uri = "content://com.example.democontentprovidersqlite.AuthorProvider";
                            Uri aut = Uri.parse(uri);
                            Uri insertUri = getContentResolver().insert(aut, author);
                            if (insertUri != null) {
                                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                etAuthorID.setText("");
                                etAuthorName.setText("");
                                etAuthorAddress.setText("");
                                etAuthorEmail.setText("");
                                etAuthorID.requestFocus();
                            } else {
                                Toast.makeText(MainActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (NumberFormatException ex) {
                            Toast.makeText(MainActivity.this, "ID Tác giả không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button btnAuthorUpdate = dgAuthor.findViewById(R.id.button_AuthorUpdate);
                btnAuthorUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            ContentValues author = new ContentValues();
                            author.put("id_author", Integer.parseInt(etAuthorID.getText().toString()));
                            author.put("name", etAuthorName.getText().toString());
                            author.put("address", etAuthorAddress.getText().toString());
                            author.put("email", etAuthorEmail.getText().toString());
                            String uri = "content://com.example.democontentprovidersqlite.AuthorProvider";
                            Uri aut = Uri.parse(uri);
                            int updateUri = getContentResolver().update(aut, author, "id_author = ?", new String[]{etAuthorID.getText().toString()});
                            if (updateUri > 0) {
                                Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (NumberFormatException ex) {
                            Toast.makeText(MainActivity.this, "ID Tác giả không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button btnAuthorSelect = dgAuthor.findViewById(R.id.button_AuthorSelect);
                btnAuthorSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> listString = new ArrayList<>();
                        String uri = "content://com.example.democontentprovidersqlite.AuthorProvider";
                        Uri aut = Uri.parse(uri);
                        Cursor cursor = getContentResolver().query(aut,null,null,null,"id_author");
                        if(cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                listString.add(cursor.getInt(0)+"");
                                listString.add(cursor.getString(1));
                                listString.add(cursor.getString(2));
                                listString.add(cursor.getString(3));
                                cursor.moveToNext();
                            } while(!cursor.isAfterLast());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1,listString);

                            gvAuthor.setAdapter(adapter);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Dữ liệu rỗng", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button btnAuthorDelete = dgAuthor.findViewById(R.id.button_AuthorDelete);
                btnAuthorDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uri = "content://com.example.democontentprovidersqlite.AuthorProvider";
                        Uri aut = Uri.parse(uri);
                        int row = getContentResolver().delete(aut,"id_author = ?", new String[]{etAuthorID.getText().toString()});
                        if(row > 0) {
                            Toast.makeText(MainActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                gvAuthor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int rounderPos = (Math.round(i/4)) * 4;
                        etAuthorID.setText(adapterView.getItemAtPosition(rounderPos).toString());
                    }
                });

                Button btnAuthorExit = dgAuthor.findViewById(R.id.button_AuthorThoat);
                btnAuthorExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dgAuthor.dismiss();
                    }
                });

                dgAuthor.show();
                Window window = dgAuthor.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            }
        });
    }
}
