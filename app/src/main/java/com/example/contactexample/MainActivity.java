package com.example.contactexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private ArrayList<Contact> contacts;
private ListView list;
private ActivityResultLauncher<Intent> startForAddResult;
private ActivityResultLauncher<Intent> startForViewResult;
private DBHandler db;

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

        setSupportActionBar(findViewById(R.id.toolbar2));

        db = new DBHandler(this);
        contacts = getContactList();
        list = findViewById(R.id.contactList);
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ViewContactActivity.class);
            intent.putExtra("contact", contacts.get(position));
            startForViewResult.launch(intent);
        });

        // Code to add contact
        startForAddResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                Contact contact = (Contact) intent.getSerializableExtra("contact");
                contacts.add(contact);
                db.addContact(contact);
                adapter.notifyDataSetChanged();
            }
        });

        // Code to view contact
        startForViewResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == ViewContactActivity.RESULT_CODE_DELETE) {
                Intent intent = result.getData();
                Contact contact = (Contact) intent.getSerializableExtra("deleteContact");
                for(int i = 0; i < contacts.size(); i++) {
                    if(contacts.get(i).getId() == contact.getId()) {
                        contacts.remove(i);
                        break;
                    }
                }
                db.deleteContact(contact);
//                contacts.clear();
//
//
//                // contact id
//                // find contact id in contacts arraylist
//                // remove deleted contact from arraylist
//                // notify adapter of change
//
//                contacts.addAll(db.getContacts());// filter contacts ; execept deleted contact
                adapter.notifyDataSetChanged();
            } else if(result.getResultCode() == ViewContactActivity.RESULT_CODE_EDIT) {
                Intent intent = result.getData();
                Contact contact = (Contact) intent.getSerializableExtra("editContact");
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getId() == contact.getId()) {
                        contacts.set(i, contact);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_list_menubar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addContact) {
            Intent intent = new Intent(this, AddContactActivity.class);
            startForAddResult.launch(intent);
            return true;
        }
        return false;
    }

    private ArrayList<Contact> getContactList() {
        return db.getContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

/*
CREATE TABLE Subject
CREATE TABLE Enrollment
CREATE TABLE School
 */