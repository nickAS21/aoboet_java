package io.dcloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import io.dcloud.common.constant.IntentConst;

public class PandoraEntry extends Activity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        try {
//            Intent intent = getIntent();
//
//            if (intent.getBooleanExtra(IntentConst.IS_STREAM_APP, false)) {
//                // Якщо "стрім апка", просто відкриваємо повідомлення або логіку
//                Intent dummyIntent = new Intent(this, PandoraEntryActivity.class);
//                dummyIntent.putExtra("info", "Stream App запущено");
//                startActivity(dummyIntent);
//            } else {
//                // Якщо ні — відкриваємо PandoraEntryActivity
//                intent.setClass(this, PandoraEntryActivity.class);
//                intent.putExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME, PandoraEntry.class.getName());
//                Log.d("PandoraEntry", "Starting PandoraEntryActivity with appid: " + intent.getStringExtra("appid"));
//                startActivity(intent);
//            }
//
//            // Завершення PandoraEntry через 20 мс
//            new Handler().postDelayed(this::finish, 20);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            finish(); // Закриваємо у випадку помилки
//        }
//    }
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("DCloud_DEBUG", "Intent before onCreate: " + getIntent());
        Log.i("DCloud_DEBUG", "Extras  before onCreate: " + getIntent().getExtras());
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();

            if (intent.getBooleanExtra(IntentConst.IS_STREAM_APP, false)) {
                Intent dummyIntent = new Intent(this, PandoraEntryActivity.class);
                dummyIntent.putExtra("info", "Stream App запущено");
                dummyIntent.putExtra("appid", "H5057CD3A"); // додано
                startActivity(dummyIntent);
            } else {
                intent.setClass(this, PandoraEntryActivity.class);
                intent.putExtra(IntentConst.WEBAPP_SHORT_CUT_CLASS_NAME, PandoraEntry.class.getName());
                intent.putExtra("appid", "H5057CD3A"); // додано
                startActivity(intent);
            }

            new Handler().postDelayed(this::finish, 20);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

}
