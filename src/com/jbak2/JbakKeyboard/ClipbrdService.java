package com.jbak2.JbakKeyboard;

import java.util.Timer;
import com.jbak2.ctrl.SameThreadTimer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.ClipboardManager;
/** Сервис для забора значений по таймеру */
@SuppressWarnings("deprecation")
public class ClipbrdService extends SameThreadTimer 
{
    public ClipbrdService(Context c) {
		super(CLIPBRD_INTERVAL, CLIPBRD_INTERVAL);
        inst = this;
        m_cm = (ClipboardManager)c.getSystemService(Service.CLIPBOARD_SERVICE);
        IntentFilter filt = new IntentFilter();
        filt.addAction(Intent.ACTION_SCREEN_ON);
        filt.addAction(Intent.ACTION_SCREEN_OFF);
        c.registerReceiver(m_recv, filt);
        start();
	}
	static ClipbrdService inst;
    public void delete(Context c)
    {
        inst = null;
        c.unregisterReceiver(m_recv);
    }
    void checkClipboardString()
    {
    	if (m_cm == null)
    		return;
// проверяет изменился ли буфер, если нет то возврат
        if(!m_cm.hasText())
        {
            return;
        }
       	checkString(m_cm.getText().toString());
        	
    }
    void checkString(String str)
    {
    	if (str == null)
    		return;
    	cancel();
        try{
            if(str.equals(m_sLastClipStr))
            {
                return;
            }
            st.stor().checkClipboardString(str);
            m_sLastClipStr = str;
        }
        catch(Throwable e)
        {
            
        }
        start();
    }
    BroadcastReceiver m_recv = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String act = intent.getAction();
            if(Intent.ACTION_SCREEN_ON.equals(act))
            {
                start();
            }
            if(Intent.ACTION_SCREEN_OFF.equals(act))
            {
                cancel();
            }
        }
    };
    String m_sLastClipStr;
/** Интервал взятия значений из буфера обмена в милисекундах */ 
    public static final int CLIPBRD_INTERVAL = 5000;
    ClipboardManager m_cm;
    Timer m_timer;
	@Override
	public void onTimer(SameThreadTimer timer) {
		checkClipboardString();
	}
}