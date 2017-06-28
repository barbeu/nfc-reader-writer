package com.example.tzadmin.nfc_reader_writer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tzadmin.nfc_reader_writer.Adapters.ShopAdapter;
import com.example.tzadmin.nfc_reader_writer.Messages.Message;
import com.example.tzadmin.nfc_reader_writer.Models.MoneyLogs;
import com.example.tzadmin.nfc_reader_writer.Models.Shop;
import com.example.tzadmin.nfc_reader_writer.Models.User;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Shop> shops = new ArrayList<>();
    Shop shop = null;
    GridView lv_shop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shops = new ArrayList(new Shop().selectAll());
        lv_shop = (GridView) findViewById(R.id.route_grid);
        ShopAdapter shopAdapter = new ShopAdapter(this, shops );

        lv_shop.setAdapter(shopAdapter);
        lv_shop.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        shop = shops.get(position);

        Intent intent = new Intent(this, ScanNfcActivity.class);
        intent.putExtra("name", shop.name);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(shop != null) {
                String RfcId = data.getStringExtra("RfcId");
                User user = new User().selectUserByRfcId(RfcId);
                if(user != null) {
                    if(user.getBallance() >= shop.money){
                        user.RemoveMoney(shop.money, "покупка " + shop.name);
                        user.update();

                        Toast.makeText(this, Message.SUCCESSFULLY, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, Message.MONEY_LOW, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, Message.USER_THIS_BRACER_NOT_FOUND, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
