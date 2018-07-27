package cn.invonate.ygoa3.Meeting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.ess.filepicker.FilePicker;
import com.ess.filepicker.model.EssFile;
import com.ess.filepicker.util.Const;
import com.yonggang.liyangyang.ios_dialog.widget.ActionSheetDialog;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.AddMeeting;
import cn.invonate.ygoa3.Entry.FileResult;
import cn.invonate.ygoa3.Entry.MeetMessage;
import cn.invonate.ygoa3.Entry.MeetingDetail;
import cn.invonate.ygoa3.Entry.Room;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil2;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddMeetingActivity extends BaseActivity {

    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.person_in)
    TextView personIn;
    @BindView(R.id.person_note)
    TextView personNote;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_date)
    TextView endDate;
    @BindView(R.id.end_time)
    TextView endTime;

    @BindView(R.id.img_file)
    ImageView img_file;
    @BindView(R.id.layout_file)
    LinearLayout layout_file;

    @BindView(R.id.file_img)
    ImageView file_img;
    @BindView(R.id.file_name)
    TextView file_name;
    @BindView(R.id.file_size)
    TextView file_size;
    @BindView(R.id.file_delete)
    ImageView file_delete;

    Room.ResultBean.ListBean room = new Room.ResultBean.ListBean();

    private String address_id;

    private YGApplication app;

    private ArrayList<MeetingDetail.ResultBean.AttendListBean> list_attend = new ArrayList<>();
    private Date date = new Date();

    private String path;

    private List<String> fileIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        startDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        endDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        room.setStart_h(9);
        room.setStart_m(0);
        room.setEnd_s(0);
        room.setEnd_h(10);
        room.setEnd_m(0);
        room.setEnd_s(0);
        startTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getStart_h(), room.getStart_m(), room.getStart_s())));
        endTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getEnd_h(), room.getEnd_m(), room.getEnd_s())));
        MeetingDetail.ResultBean.AttendListBean bean = new MeetingDetail.ResultBean.AttendListBean();
        bean.setUserCode(app.getUser().getUser_code());
        bean.setUserId(app.getUser().getRsbm_pk());
        bean.setUserName(app.getUser().getUser_name());
        list_attend.add(bean);
        personIn.setText(String.format("%s等%d人", list_attend.get(0).getUserName(), list_attend.size()));
    }

    @OnClick({R.id.pic_back, R.id.layout_time, R.id.pic_search, R.id.layout_address, R.id.layout_in, R.id.img_file, R.id.layout_file, R.id.file_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                finish();
                break;
            case R.id.pic_search:
                addMeeting();
                break;
            case R.id.layout_time:
                stepToLocation();
                break;
            case R.id.layout_address:
                stepToLocation();
                break;
            case R.id.layout_in:
                stepToAttend();
                break;
            case R.id.img_file:
                ActionSheetDialog dialog = new ActionSheetDialog(AddMeetingActivity.this).builder();
                dialog.setTitle("请选择操作")
                        .addSheetItem("图片", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                PhotoPicker.builder()
                                        .setPhotoCount(1)
                                        .setShowCamera(true)
                                        .setPreviewEnabled(true)
                                        .start(AddMeetingActivity.this);
                            }
                        }).addSheetItem("文件", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        FilePicker
                                .from(AddMeetingActivity.this)
                                .chooseForMimeType()
                                .setMaxCount(1)
                                .setFileTypes("doc", "xls", "ppt", "pdf", "apk", "mp3", "gif", "txt", "mp4", "zip", "rar")
                                .requestCode(0x999)
                                .start();
                    }
                }).show();
                break;
            case R.id.layout_file:

                break;
            case R.id.file_delete:
                path = null;
                fileIds = null;
                img_file.setVisibility(View.VISIBLE);
                layout_file.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123 && resultCode == 0x999) {
            if (data != null) {
                list_attend = (ArrayList<MeetingDetail.ResultBean.AttendListBean>) data.getExtras().getSerializable("list");
                if (list_attend.isEmpty()) {
                    personIn.setText("请选择");
                } else {
                    personIn.setText(String.format("%s等%d人", list_attend.get(0).getUserName(), list_attend.size()));
                }
            }
        } else if (requestCode == 0x123 && resultCode == 0x888) {
            if (data != null) {
                room = (Room.ResultBean.ListBean) data.getExtras().getSerializable("room");
                address_id = data.getExtras().getString("address_id");
                if (room.getRoomBuilding() != null && !room.getRoomBuilding().isEmpty()) {
                    if (room.getRoomFloor() != null && !room.getRoomFloor().isEmpty()) {
                        txtAddress.setText(String.format("%s楼%s层%s", room.getRoomBuilding(), room.getRoomFloor(), room.getRoomNo()));
                    } else {
                        txtAddress.setText(String.format("%s楼%s", room.getRoomBuilding(), room.getRoomNo()));
                    }
                } else {
                    if (room.getRoomFloor() != null && !room.getRoomFloor().isEmpty()) {
                        txtAddress.setText(String.format("%s层%s", room.getRoomFloor(), room.getRoomNo()));
                    } else {
                        txtAddress.setText(String.format("%s", room.getRoomNo()));
                    }
                }
                date = new Date(data.getExtras().getInt("date_y"), data.getExtras().getInt("date_m"), data.getExtras().getInt("date_d"));
                startDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                endDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                startTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getStart_h(), room.getStart_m(), room.getStart_s())));
                endTime.setText(new SimpleDateFormat("HH:mm").format(new Date(0, 0, 0, room.getEnd_h(), room.getEnd_m(), room.getEnd_s())));
            }
        } else if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            if (photos != null && !photos.isEmpty()) {
                Log.i("photos", photos.toString());
                path = photos.get(0);
                saveFile();
                showFile();
            }
        } else if (requestCode == 0x999 && resultCode == RESULT_OK) {
            ArrayList<EssFile> files = data.getParcelableArrayListExtra(Const.EXTRA_RESULT_SELECTION);
            Log.i("files", JSON.toJSONString(files));
            if (!files.isEmpty()) {
                path = files.get(0).getAbsolutePath();
                saveFile();
                showFile();
            } else {
                Toast.makeText(app, "未找到该文件", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String formatSize(long size) {
        String result = "";
        BigDecimal bigDecimal = new BigDecimal(size);
        if (size > 1024 * 1024) {// 大于1M
            result = bigDecimal.divide(new BigDecimal(1024 * 1024), 2, RoundingMode.DOWN).toString() + " MB";
        } else {
            result = bigDecimal.divide(new BigDecimal(1024), 2, RoundingMode.DOWN).toString() + " KB";
        }
        return result;
    }

    private void showFile() {
        if (path != null) {
            img_file.setVisibility(View.GONE);
            layout_file.setVisibility(View.VISIBLE);
            File file = new File(path);
            file_name.setText(file.getName());
            file_size.setText(formatSize(file.length()));
            String type = path.substring(path.lastIndexOf(".") + 1, path.length());
            switch (type) {
                case "doc":
                    Glide.with(this).load(R.mipmap.doc).centerCrop().into(file_img);
                    break;
                case "xls":
                    Glide.with(this).load(R.mipmap.xls).centerCrop().into(file_img);
                    break;
                case "ppt":
                    Glide.with(this).load(R.mipmap.ppt).centerCrop().into(file_img);
                    break;
                case "pdf":
                    Glide.with(this).load(R.mipmap.pdf).centerCrop().into(file_img);
                    break;
                case "txt":
                    Glide.with(this).load(R.mipmap.txt).centerCrop().into(file_img);
                    break;
                case "zip":
                    Glide.with(this).load(R.mipmap.zip).centerCrop().into(file_img);
                    break;
                case "jpg":
                case "png":
                case "jpeg":
                    Glide.with(this).load(path).centerCrop().into(file_img);
                    break;
                default:
                    Glide.with(this).load(R.mipmap.files).centerCrop().into(file_img);
                    break;
            }
        }
    }

    /**
     * 跳转到选择地点
     */
    private void stepToLocation() {
        Intent intent = new Intent(this, LocationActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, 0x123);
    }

    /**
     * 跳转至选择参会人
     */
    private void stepToAttend() {
        Intent intent = new Intent(this, AddAttendActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list_attend);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0x123);
    }

    /**
     *
     */
    private void addMeeting() {
        String t = title.getText().toString();
        if ("".equals(t)) {
            Toast.makeText(app, "请输入会议标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (room.getRoomId() == null) {
            Toast.makeText(app, "请选择会议地点", Toast.LENGTH_SHORT).show();
            return;
        }
        if (personNote.getText().toString().trim().equals("")) {
            Toast.makeText(app, "请输入主持人", Toast.LENGTH_SHORT).show();
            return;
        }
        if (list_attend == null || list_attend.isEmpty()) {
            Toast.makeText(app, "请选择参会人", Toast.LENGTH_SHORT).show();
            return;
        }
        AddMeeting meet = new AddMeeting();
        meet.setMeetingContent(t);
        Date start = new Date(date.getYear(), date.getMonth(), date.getDate(), room.getStart_h(), room.getStart_m(), room.getStart_s());
        Log.i("start_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start));
        meet.setStartTime(start.getTime() + "");
        Date end = new Date(date.getYear(), date.getMonth(), date.getDate(), room.getEnd_h(), room.getEnd_m(), room.getEnd_s());
        Log.i("end_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end));
        meet.setEndTime(end.getTime() + "");
        meet.setRoomId(room.getRoomId());
        meet.setRecordPersonId("");
        meet.setRecordPersonCode("");
        meet.setRecordPersonName(personNote.getText().toString().trim());
        meet.setJoinList(list_attend);
        meet.setAddressId(address_id);
        meet.setFileIds(fileIds);
        Log.i("params", JSON.toJSONString(meet));
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<MeetMessage>() {
            @Override
            public void onNext(MeetMessage data) {
                Log.i("addMeeting", data.toString());
                if ("0000".equals(data.getCode())) {
                    finish();
                    Toast.makeText(app, "预约成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(app, data.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).add_meet(new ProgressSubscriber(onNextListener, this, "预约中"), app.getUser().getRsbm_pk(), meet);
    }


    /**
     * 上传附件
     */
    private void saveFile() {
        if (path == null) {
            return;
        }
        List<MultipartBody.Part> parts = filesToMultipartBodyParts(path);
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<FileResult>() {
            @Override
            public void onNext(FileResult data) {
                Log.i("saveFile", JSON.toJSONString(data));
                if ("0000".equals(data.getCode())) {
                    fileIds = data.getResult().getFileIds();
                    Toast.makeText(AddMeetingActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMeetingActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil2.getInstance(this, false).saveFile(new ProgressSubscriber(onNextListener, this, "附件上传中"), app.getUser().getRsbm_pk(), parts);
    }

    /**
     * 将文件转化成MultipartBody.Part
     *
     * @param file
     * @return
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(String file) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/*"), new File(file));
        MultipartBody.Part part = MultipartBody.Part.createFormData("files", new File(file).getName(), requestBody);
        parts.add(part);
        return parts;
    }
}
