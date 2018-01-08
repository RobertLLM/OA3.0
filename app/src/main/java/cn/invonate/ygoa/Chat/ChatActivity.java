package cn.invonate.ygoa.Chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.invonate.ygoa.BaseActivity;
import cn.invonate.ygoa.Chat.adapter.ExpressionAdapter;
import cn.invonate.ygoa.Chat.adapter.ExpressionPagerAdapter;
import cn.invonate.ygoa.Chat.utils.SmileUtils;
import cn.invonate.ygoa.Chat.utils.TextModule;
import cn.invonate.ygoa.Chat.view.ExpandGridView;
import cn.invonate.ygoa.Chat.view.PasteEditText;
import cn.invonate.ygoa.R;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;

    public static final String COPY_IMAGE = "EASEMOBIMG";

    private static AbstractXMPPConnection conn;

    private static ChatManager chatManager;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.view_temp)
    View viewTemp;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_setting_group)
    ImageView ivSettingGroup;
    @BindView(R.id.top_bar)
    RelativeLayout topBar;
    @BindView(R.id.btn_set_mode_voice)
    Button btnSetModeVoice;
    @BindView(R.id.btn_set_mode_keyboard)
    Button btnSetModeKeyboard;
    @BindView(R.id.btn_press_to_speak)
    LinearLayout btnPressToSpeak;
    @BindView(R.id.et_sendmessage)
    PasteEditText etSendmessage;
    @BindView(R.id.iv_emoticons_normal)
    ImageView ivEmoticonsNormal;
    @BindView(R.id.iv_emoticons_checked)
    ImageView ivEmoticonsChecked;
    @BindView(R.id.edittext_layout)
    RelativeLayout edittextLayout;
    @BindView(R.id.btn_more)
    Button btnMore;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.rl_bottom)
    LinearLayout rlBottom;
    @BindView(R.id.vPager)
    ViewPager vPager;
    @BindView(R.id.ll_face_container)
    LinearLayout llFaceContainer;
    @BindView(R.id.btn_take_picture)
    ImageView btnTakePicture;
    @BindView(R.id.btn_picture)
    ImageView btnPicture;
    @BindView(R.id.btn_location)
    ImageView btnLocation;
    @BindView(R.id.btn_video)
    ImageView btnVideo;
    @BindView(R.id.btn_voice_call)
    ImageView btnVoiceCall;
    @BindView(R.id.container_voice_call)
    LinearLayout containerVoiceCall;
    @BindView(R.id.btn_video_call)
    ImageView btnVideoCall;
    @BindView(R.id.container_video_call)
    LinearLayout containerVideoCall;
    @BindView(R.id.ll_btn_container)
    LinearLayout llBtnContainer;
    @BindView(R.id.more)
    LinearLayout more;
    @BindView(R.id.bar_bottom)
    LinearLayout barBottom;
    @BindView(R.id.pb_load_more)
    ProgressBar pbLoadMore;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.mic_image)
    ImageView micImage;
    @BindView(R.id.recording_hint)
    TextView recordingHint;
    @BindView(R.id.recording_container)
    RelativeLayout recordingContainer;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    private ClipboardManager clipboard;
    private InputMethodManager manager;

    private Drawable[] micImages;// 发送语音的动画

    private List<String> reslist; // emoji列表

    @SuppressLint("HandlerLeak")
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    private String from = "022023";
    private String to = "033523";

    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        clipboard = (ClipboardManager) getSystemService(
                Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{
                getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06),
                getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08),
                getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10),
                getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12),
                getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14),};
        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        vPager.setAdapter(new ExpressionPagerAdapter(views));
        btnSend.setOnClickListener(this);
        ivEmoticonsNormal.setOnClickListener(this);
        ivEmoticonsChecked.setOnClickListener(this);

        etSendmessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittextLayout.setBackgroundResource(
                            R.drawable.input_bar_bg_active);
                } else {
                    edittextLayout.setBackgroundResource(
                            R.drawable.input_bar_bg_normal);
                }

            }
        });
        etSendmessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edittextLayout.setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                ivEmoticonsNormal.setVisibility(View.VISIBLE);
                ivEmoticonsChecked.setVisibility(View.INVISIBLE);
                llFaceContainer.setVisibility(View.GONE);
                llBtnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        etSendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cAndl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void cAndl() throws IOException, InterruptedException, XMPPException, SmackException {
        InetAddress addr = InetAddress.getByName("192.168.3.97");
        HostnameVerifier verifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }
        };
        DomainBareJid serviceName = JidCreate.domainBareFrom("com.innovate.ygoa");
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setHost("192.168.3.97") // # it will be resolved by setHostAddress method
                .setUsernameAndPassword(from, "123456")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setXmppDomain(serviceName)
                .setHostnameVerifier(verifier)
                .setHostAddress(addr)
                .setDebuggerEnabled(true)
                .build();
        conn = new XMPPTCPConnection(config);
        conn.connect();

        if (conn.isConnected()) {
            Log.d("XMPP", "Connected");
        }

        conn.login();

        if (conn.isAuthenticated()) {
            chatManager = ChatManager.getInstanceFor(conn);
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    Log.i("incomingMessage", message.toXML().toString());
                }
            });
            chatManager.addOutgoingListener(new OutgoingChatMessageListener() {
                @Override
                public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
                    Log.i("outgoingMessage", message.toXML().toString());
                }
            });
        }
    }

    /**
     * 发送消息
     *
     * @param msg 发送内容
     */
    public synchronized void sendMessages(String msg) {
        try {
            EntityBareJid _from = null;
            EntityBareJid _to = null;
            TextModule tm = new TextModule(from, "url", msg);
            switch (type) {
                case CHATTYPE_SINGLE:
                    _to = JidCreate.entityBareFrom(to + "@com.innovate.ygoa");
                    break;
                case CHATTYPE_GROUP:
                    _to = JidCreate.entityBareFrom(from + "com.innovate.ygoa/" + UUID.randomUUID());
                    break;
            }
            Message message = new Message(_to, Message.Type.chat);
            Chat chat = chatManager.chatWith(_to);
            message.setFrom(conn.getUser());
            message.setTo(_to);
            message.setBody(msg);
            message.addExtension(tm);
            chat.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittextLayout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btnSetModeKeyboard.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        btnPressToSpeak.setVisibility(View.VISIBLE);
        ivEmoticonsNormal.setVisibility(View.VISIBLE);
        ivEmoticonsChecked.setVisibility(View.INVISIBLE);
        llBtnContainer.setVisibility(View.VISIBLE);
        llFaceContainer.setVisibility(View.GONE);

    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        edittextLayout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        btnSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        etSendmessage.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        btnPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(etSendmessage.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void more(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            llBtnContainer.setVisibility(View.VISIBLE);
            llFaceContainer.setVisibility(View.GONE);
        } else {
            if (llFaceContainer.getVisibility() == View.VISIBLE) {
                llFaceContainer.setVisibility(View.GONE);
                llBtnContainer.setVisibility(View.VISIBLE);
                ivEmoticonsNormal.setVisibility(View.VISIBLE);
                ivEmoticonsChecked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            ivEmoticonsNormal.setVisibility(View.VISIBLE);
            ivEmoticonsChecked.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow()
                .getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 消息图标点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
            String s = etSendmessage.getText().toString();
            sendMessages(s);
        } else if (id == R.id.btn_take_picture) {
            //selectPicFromCamera();// 点击照相图标
        } else if (id == R.id.btn_picture) {
            //selectPicFromLocal(); // 点击图片图标
        } else if (id == R.id.btn_location) { // 位置
//            startActivityForResult(new Intent(this, BaiduMapActivity.class),
//                    REQUEST_CODE_MAP);
        } else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
            more.setVisibility(View.VISIBLE);
            ivEmoticonsNormal.setVisibility(View.INVISIBLE);
            ivEmoticonsChecked.setVisibility(View.VISIBLE);
            llBtnContainer.setVisibility(View.GONE);
            llFaceContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
            ivEmoticonsNormal.setVisibility(View.VISIBLE);
            ivEmoticonsChecked.setVisibility(View.INVISIBLE);
            llBtnContainer.setVisibility(View.VISIBLE);
            llFaceContainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);

        }
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (btnSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            @SuppressWarnings("rawtypes")
                            Class clz = Class
                                    .forName("com.fanxin.app.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            etSendmessage.append(
                                    SmileUtils.getSmiledText(ChatActivity.this,
                                            (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils
                                    .isEmpty(etSendmessage.getText())) {

                                int selectionStart = etSendmessage
                                        .getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = etSendmessage.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                        if (SmileUtils
                                                .containsKey(cs.toString()))
                                            etSendmessage.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                            etSendmessage.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                        etSendmessage.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    Log.i("error", e.toString());
                }

            }
        });
        return view;
    }

    /**
     * @param getSum
     * @return
     */
    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

}
