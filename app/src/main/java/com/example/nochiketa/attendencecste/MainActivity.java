package com.example.nochiketa.attendencecste;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button mOrder, displayAllDataButton;
    TextView mItemSelected;
    String[] listItems;
    boolean[] checkItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    MYDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOrder = (Button)findViewById(R.id.btn1);
        displayAllDataButton = (Button)findViewById(R.id.btn2);

        mItemSelected = (TextView)findViewById(R.id.TV1);
        myDatabaseHelper = new MYDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        listItems = getResources().getStringArray(R.array.student_id);
        checkItems = new boolean[listItems.length];


        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle(R.string.student_list);
                mBuilder.setMultiChoiceItems(listItems, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked)
                        {
                            if(!mUserItems.contains(position))
                            {
                                mUserItems.add(position);
                            }
                            else if(mUserItems.contains(position))
                            {
                                if(isChecked)
                                {
                                    mUserItems.remove(position);
                                }
                            }
                        }

                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for(int i = 0;i<mUserItems.size(); i++)
                        {
                            item = item + listItems[mUserItems.get(i)];
                            if(i != mUserItems.size() -1)
                            {
                                item = item + ", ";
                            }
                        }
                        mItemSelected.setText("Present: \n" + item);

                        long rowId = myDatabaseHelper.insertData(item);
                       // myDatabaseHelper.insertData(item);
                        if(rowId == -1)
                        {
                            Toast.makeText(getApplicationContext(), "Not inserted", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Row "+rowId+" is successfully inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0; i<checkItems.length; i++)
                        {
                            checkItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        displayAllDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor = myDatabaseHelper.displayAllData();
                if(cursor.getCount() == 0)
                {
                    showData("Error", "No Data Found");
                    return;
                }
                StringBuffer stringBuffer = new StringBuffer();
                while (cursor.moveToNext())
                {
                    //stringBuffer.append("ID: "+ cursor.getString(0)+"\n");
                    stringBuffer.append(getString(R.string.Student_id)+ cursor.getString(1)+"\n\n");
                }
                showData("Attended Students: ", stringBuffer.toString());
            }
        });
    }
    public void showData(String title, String data)
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(title);
        mBuilder.setMessage(data);
        mBuilder.setCancelable(true);
        mBuilder.show();
    }
}
