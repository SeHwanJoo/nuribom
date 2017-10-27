//package streaming.test.org.togethertrip.viewholder;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import streaming.test.org.togethertrip.R;
//
///**
// * Created by f on 2017-10-05.
// */
//
//public class AlarmRecyclerAdapter1 extends RecyclerView.Adapter<AlarmViewHolder> {
//
//    public AlarmRecyclerAdapter1(ArrayList<CommentResult.CommentData> commentDatas, Context context, String userId) {
//        this.commentDatas = commentDatas;
//        this.context = context;
//        this.userId = userId;
//    }
//
//
//
//    ArrayList<CommentResult.CommentData> commentDatas;
//    Context context;
//    String userId;
//    DeleteDialog deleteDialog;
//    ReportDialog reportDialog;
//
//    public void setadapter(ArrayList<CommentResult.CommentData> commentDatas, Context context,String userId){
//        this.commentDatas = commentDatas;
//        this.context = context;
//        this.userId = userId;
//    }
//
//    @Override
//    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
//        AlarmViewHolder myViewHolder = new AlarmViewHolder(view,context);
//
//
//        return myViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final AlarmViewHolder holder, final int position) {
//        holder.userid.setText(commentDatas.get(position).userId);
//        holder.content.setText(commentDatas.get(position).content);
//        holder.date.setText(commentDatas.get(position).date);
//        holder.report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(userId.equals(commentDatas.get(position).userId)){
//                    deleteDialog = new DeleteDialog(context,commentDatas.get(position).commentId);
//                    deleteDialog.show();
//                    deleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            ((Activity)context).recreate();
//                        }
//                    });
//                } else{
//                    ReportData reportData = new ReportData();
//                    reportData.reportUser = userId;
//                    reportData.commentId = commentDatas.get(position).commentId;
//                    reportData.content = commentDatas.get(position).content;
//                    reportData.userId = commentDatas.get(position).userId;
//                    reportDialog = new ReportDialog(((Activity)context),reportData);
//                    reportDialog.show();
//                    reportDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return commentDatas != null ? commentDatas.size() : 0;
//    }
//}
