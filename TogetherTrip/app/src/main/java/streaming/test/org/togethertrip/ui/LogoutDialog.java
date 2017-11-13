package streaming.test.org.togethertrip.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import streaming.test.org.togethertrip.R;

/**
 * Created by taehyung on 2017-10-26.
 */

public class LogoutDialog extends Dialog{
    Context context;
    View.OnClickListener mLeftClickListener;
    View.OnClickListener mRightClickListener;

    TextView tv_logout, tv_cancel;

    public LogoutDialog(Context context, View.OnClickListener leftListener,
                        View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.logout_dialog);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_logout = (TextView) findViewById(R.id.tv_logout);



        if(mLeftClickListener != null && mRightClickListener != null){
            tv_cancel.setOnClickListener(mLeftClickListener);
            tv_logout.setOnClickListener(mRightClickListener);
        }

    }
}
