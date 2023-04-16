package com.conamobile.sosapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

import com.conamobile.sosapp.constants.IntentConstant;
import com.conamobile.sosapp.constants.KeyConstant;
import com.conamobile.sosapp.constants.RequestFor;
import com.conamobile.sosapp.ui.MainActivity;
import com.conamobile.sosapp.ui.SosActivity;
import com.conamobile.sosapp.util.FPowerManager;
import com.conamobile.sosapp.util.MyLogger;
import com.conamobile.sosapp.util.PhoneData;

public class MediaButtonIntentReciever extends BroadcastReceiver {
    private static final String TAG = MediaButtonIntentReciever.class.getSimpleName();

    private static long prevTime;

    private static boolean isSingleCall = false;

    public MediaButtonIntentReciever() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
//                Toast.makeText(context, "media action received", Toast.LENGTH_SHORT).show();
                if (intent.getExtras() != null) {
                    int prevVolume = intent.getExtras().getInt("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
                    int currentValue = intent.getExtras().getInt("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);

                    AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

                    //Forunlocking only one event will trigger. When phone is open ui popup will also trigger the volume change
                    boolean volumeAction = false;
                    if ((currentValue == 0 && prevVolume == 0) || currentValue == maxVolume && prevVolume == maxVolume) {
                        if (!isSingleCall) {
                            isSingleCall = true;
                        } else {
                            isSingleCall = false;
                            volumeAction = true;
                        }
                    } else if (currentValue != 0 && prevVolume != 0 && currentValue != prevVolume) {
                        volumeAction = true;
                    }

                    if (volumeAction || !FPowerManager.instance(context).isScreenOn()) //when screen is off ui volume change will not happen
                    {
                        boolean isEnabled = PhoneData.getPhoneData(context, KeyConstant.UNLOCK_STR, false);

                        if (!FPowerManager.instance(context).isScreenOn() && isEnabled) {
                            Toast.makeText(context, "wake lock done", Toast.LENGTH_SHORT).show();
                        } else if (FPowerManager.instance(context).isScreenOn() && PhoneData.getPhoneData(context, KeyConstant.VOLUME_LOCK_ENABLE_STR, false) && isEnabled) {
                            if (System.currentTimeMillis() - prevTime < 600) {
                                Toast.makeText(context, "enable sos mode", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, SosActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }

                            prevTime = System.currentTimeMillis();
                        }
                    }

                }
            } else if (intent.getAction().equals(IntentConstant.LOCK_SCREEN_ACTION_INTENT)) {
                Intent activityIntent = new Intent(context, MainActivity.class);
                activityIntent.putExtra(KeyConstant.REQUEST_FOR_STR, RequestFor.ACTIVATE_DEVICE_ADMIN);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
            } else {
                Toast.makeText(context, "unchaced intent arrived", Toast.LENGTH_SHORT).show();
                MyLogger.l(TAG, "uncached intent arrived");
            }
        } else {
            Toast.makeText(context, "null intent received", Toast.LENGTH_SHORT).show();
            MyLogger.l(TAG, "null Intent recieved");
        }
    }
}
