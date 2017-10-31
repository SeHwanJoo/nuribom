package streaming.test.org.togethertrip.ui.comment;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import streaming.test.org.togethertrip.R;
import streaming.test.org.togethertrip.application.ApplicationController;
import streaming.test.org.togethertrip.datas.CourseListDatas;
import streaming.test.org.togethertrip.datas.comment.CommentDatas;
import streaming.test.org.togethertrip.datas.comment.CommentInfo;
import streaming.test.org.togethertrip.datas.comment.CommentResult;
import streaming.test.org.togethertrip.datas.comment.CommentWriteDatas;
import streaming.test.org.togethertrip.datas.comment.CommentWriteResult;
import streaming.test.org.togethertrip.network.NetworkService;

import static streaming.test.org.togethertrip.R.id.completeBtn;

public class CommentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public TextView title;
    public EditText writeComment;
    public ImageView delete;
    public ImageView complete;
    public RecyclerView recyclerViewCommentList;
    SwipeRefreshLayout refreshlayout;
    private NetworkService service;
    private CommentInfo commentInfo;
    private ArrayList<CommentDatas> commentListDatas;
    private ArrayList<CourseListDatas> CourseListDatas;
    private  CommentRecyclerAdapter commentRecyclerAdapter;
    SharedPreferences loginInfo;
    private LinearLayoutManager linearLayoutManager;
    Intent getIntent;
    String image = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        //user_id 받아옴
        loginInfo = getSharedPreferences("loginSetting", 0);

        getIntent = getIntent();
        commentInfo = new CommentInfo();
        commentInfo.courseid =  getIntent.getIntExtra("courseid",0);
        commentInfo.userid = loginInfo.getString("nickname","");
//        commentInfo.userid ="joo";

        //변수 초기화
        delete = (ImageView)findViewById(R.id.imageview_tag_close) ;
        complete = (ImageView)findViewById(R.id.supply_enter);
        writeComment = (EditText)findViewById(R.id.edittext_comment_enter);
        recyclerViewCommentList = (RecyclerView)findViewById(R.id.recyclerview_comment_list);
        refreshlayout = (SwipeRefreshLayout)findViewById(R.id.RefreshLayout) ;
        refreshlayout.setOnRefreshListener(this);

        //서비스 객체 초기화
        service = ApplicationController.getInstance().getNetworkService();
        recyclerViewCommentList.setHasFixedSize(true);

        //각 배열에 모델 개체를 가지는 ArrayList 초기화
        commentListDatas = new ArrayList<CommentDatas>();

        //레이아웃 매니저 설정
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewCommentList.setLayoutManager(linearLayoutManager);

        commentRecyclerAdapter = new CommentRecyclerAdapter(commentListDatas, CommentActivity.this);
        recyclerViewCommentList.setAdapter(commentRecyclerAdapter);

        okNetwork(commentInfo);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComment.setText("");
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentWriteDatas commentWriteDatas = new CommentWriteDatas();
                commentWriteDatas.courseid = commentInfo.courseid;
                commentWriteDatas.userid = commentInfo.userid;
                commentWriteDatas.content = writeComment.getText().toString();

                Call<CommentWriteResult> getComment = service.writeComment(commentWriteDatas);
                getComment.enqueue(new Callback<CommentWriteResult>() {
                    @Override
                    public void onResponse(Call<CommentWriteResult> call, Response<CommentWriteResult> response) {
                        if (response.isSuccessful()) {
                            Log.d("comment: ", response.body().message);
                            if (response.body().message.equals("success")){
                                Toast.makeText(getApplication(), "댓글쓰기가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                writeComment.setText("");
                                onRefresh();
                            }
                        }else {
                            Toast.makeText(getBaseContext(), "댓글 등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentWriteResult> call, Throwable t) {
                        Toast.makeText(getBaseContext(), "통신을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

    }

    public void okNetwork(CommentInfo commentInfo){
        Call<CommentResult> getComment = service.getComment(commentInfo);
        getComment.enqueue(new Callback<CommentResult>() {

            @Override
            public void onResponse(Call<CommentResult> call, Response<CommentResult> response) {
                if (response.isSuccessful()) {
                    if(response.body().message.equals("yes")){
                        commentListDatas = response.body().result;
                        commentRecyclerAdapter.setAdapter(commentListDatas);
                    } else if(response.body().message.equals("no")){
                        Toast.makeText(getApplicationContext(),"댓글이 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<CommentResult> call, Throwable t) {
                Toast.makeText(getBaseContext(), "통신 실패", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onRefresh() {
        okNetwork(commentInfo);
        refreshlayout.setRefreshing(false);
    }
}
