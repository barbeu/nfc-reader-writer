package com.example.tzadmin.nfc_reader_writer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tzadmin.nfc_reader_writer.Adapters.MoneyAdapter;
import com.example.tzadmin.nfc_reader_writer.Messages.Message;
import com.example.tzadmin.nfc_reader_writer.Models.Group;
import com.example.tzadmin.nfc_reader_writer.Models.User;

public class ValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        startActivityForResult(new Intent(this, ScanNfcActivity.class), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            String RfcId = data.getStringExtra("RfcId");
            User user = new User().selectUserByRfcId(RfcId);
            if(user != null) {

                ((ListView) findViewById(R.id.lv_history_valid)).
                        setAdapter(new MoneyAdapter(this, user.getMoneyLog()));

                ((TextView) findViewById(R.id.tv_valid_fio)).
                        setText(Message.concatFio(user));

                ((TextView) findViewById(R.id.tv_valid_points)).
                        setText("Баллы: " + String.valueOf(user.getBallance()));

                ((TextView) findViewById(R.id.tv_valid_rating)).
                        setText("Рейтинг: " + String.valueOf(user.getRating()));

                if(user.getRoute() != null && !user.routeid.equals(-1))
                    ((TextView) findViewById(R.id.tv_valid_routes)).
                            setText("Маршрут: " + user.getRoute().name);
                else
                    ((TextView) findViewById(R.id.tv_valid_routes)).
                            setText(Message.NO_ROUTE);

                //if(user.get)

                try {
                    Group userGroup = user.getGroup();
                    if (userGroup != null && !user.groupid.equals(-1)) {
                        Resources res = getResources();
                        String mDrawableName = userGroup.totemimage;
                        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
                        Drawable drawable = res.getDrawable(resID );

                        findViewById(R.id.image_valid).
                                setBackground(drawable);

                        ((TextView) findViewById(R.id.nameClan_valid)).
                                setText(userGroup.totemname);
                    } else {
                         findViewById(R.id.image_valid).
                                 setBackgroundResource(R.drawable.ic_spiker_not_found);

                        ((TextView) findViewById(R.id.nameClan_valid)).
                                setText(Message.NO_CLAN);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, Message.USER_THIS_BRACER_NOT_FOUND, Toast.LENGTH_LONG).show();
                finish();
            }
        } else
            finish();
    }
}
