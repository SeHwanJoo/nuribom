package streaming.test.org.togethertrip.ui.notice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;



import java.util.ArrayList;

import retrofit2.http.HEAD;
import streaming.test.org.togethertrip.R;

public class NoticeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<itemData> itemdatas;
    RecycleAdapter recycleAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        itemdatas = new ArrayList<itemData>();

        itemdatas.add(new itemData("투투_버전 1.0 런칭","여러분 드디어 투투가 7월 1일 런칭하게 되었습니다."));
        itemdatas.add(new itemData("투투_버전 2.0 런칭","여러분 드디어 투투가 8월 1일 런칭하게 되었습니다."));
        itemdatas.add(new itemData("투투_버전 3.0 런칭","여러분 드디어 투투가 9월 1일 런칭하게 되었습니다."));
        itemdatas.add(new itemData("투투_버전 4.0 런칭","여러분 드디어 투투가 10월 1일 런칭하게 되었습니다."));
        itemdatas.add(new itemData("투투_버전 5.0 런칭","여러분 드디어 투투가 11월 1일 런칭하게 되었습니다."));
        itemdatas.add(new itemData("투투_버전 6.0 런칭","여러분 드디어 투투가 12월 1일 런칭하게 되었습니다."));


        recycleAdapter = new RecycleAdapter(itemdatas, clickListener);
        recyclerView.setAdapter(recycleAdapter);
    }

    public View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = recyclerView.getChildPosition(v);
        }
    };

}
