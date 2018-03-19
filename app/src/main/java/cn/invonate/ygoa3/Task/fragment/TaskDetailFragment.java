package cn.invonate.ygoa3.Task.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Contacts.Select.SelectDepartmentActivity;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.Task;
import cn.invonate.ygoa3.Entry.TaskDetail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;
import io.github.luckyandyzhang.mentionedittext.MentionEditText;
import rx.Subscriber;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class TaskDetailFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable {

    @BindView(R.id.list_input)
    RecyclerView listInput;
    Unbinder unbinder;

    private YGApplication app;
    private String businessId;
    private String taskId;
    private String workflowType;

    private TaskDetail detail;

    private TaskDetailAdapter adapter;

    private StringBuilder copy = new StringBuilder();
    private StringBuilder coordination = new StringBuilder();

    private boolean need_layout;//是否需要审批意见、抄送等
    private boolean isXt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessId = getArguments().getString("businessId");
        taskId = getArguments().getString("taskId");
        workflowType = getArguments().getString("workflowType");
        need_layout = getArguments().getBoolean("need_layout");
        isXt = getArguments().getBoolean("isXt");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
        app = (YGApplication) getActivity().getApplication();
        getTaskDetail();
        String s = null;
//        if (Math.random() < 0.5) {
        s = "{\n" +
                "    \"title\": \"费用报销单\",\n" +
                "    \"inputs\": [\n" +
                "               {\n" +
                "               \"value\": \"ces\",\n" +
                "               \"label\": \"费用事由\",\n" +
                "               \"type\": \"label\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"value\": \"详细说明\",\n" +
                "               \"label\": \"详细说明\",\n" +
                "               \"type\": \"label\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"type\": \"accordion\",\n" +
                "               \"value\": [\n" +
                "                          {\n" +
                "                          \"content\": {\n" +
                "                                      \"分摊部门\" : \"供销事业部销售处扬州办事处\",\n" +
                "                                      \"发生时间\" : \"2017-02-03\",\n" +
                "                                      \"费用名称\" : \"广告费--产品试用费\",\n" +
                "                                      \"税前金额\" : 10,\n" +
                "                                      \"税金\" : 0,\n" +
                "                                      \"汇款方式\": \"电汇/现汇\",\n" +
                "                                      \"金额\": \"10.00\",\n" +
                "                                      \"已付\": 0,\n" +
                "                                      \"附外来凭证\": \"0\",\n" +
                "                                      \"备注\": null\n" +
                "                          },\n" +
                "                          \"header\": \"供销事业部销售处扬州办事处 :10.00\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                          \"content\": {\n" +
                "                          \"已付\": 0,\n" +
                "                          \"附外来凭证\": \"0\",\n" +
                "                          \"备注\": null\n" +
                "                          },\n" +
                "                          \"header\": \"供销事业部销售处扬州办事处 :10.00\"\n" +
                "                          }\n" +
                "                          ],\n" +
                "               \"label\": \"费用明细\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"value\": [\n" +
                "                          {\n" +
                "                          \"content\": {\n" +
                "                          \"附外来凭证\": \"0\",\n" +
                "                          \"备注\": null\n" +
                "                          },\n" +
                "                          \"header\": \"供销事业部销售处扬州办事处 :10.00\"\n" +
                "                          },\n" +
                "                          {\n" +
                "                          \"content\": {\n" +
                "                          \"已付\": 0,\n" +
                "                          \"附外来凭证\": \"0\",\n" +
                "                          \"备注\": null\n" +
                "                          },\n" +
                "                          \"header\": \"供销事业部销售处扬州办事处 :10.00\"\n" +
                "                          }\n" +
                "                          ],\n" +
                "               \"label\": \"费用明细\",\n" +
                "               \"type\": \"accordion\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"value\": \"10.00\",\n" +
                "               \"label\": \"本次支付\",\n" +
                "               \"type\": \"label\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"name\": \"text\",\n" +
                "               \"label\": \"经办人\",\n" +
                "               \"type\": \"text\",\n" +
                "               \"readOnly\":false\n" +
                "               },\n" +
                "               {\n" +
                "               \"value\": \"1\",\n" +
                "               \"label\": \"收款单位\",\n" +
                "               \"type\": \"label\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"value\": \"沈浩2017-02-10\",\n" +
                "               \"label\": \"经办人\",\n" +
                "               \"type\": \"label\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"name\": \"workflowType\",\n" +
                "               \"value\": \"13\",\n" +
                "               \"type\": \"hidden\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"name\": \"businessId\",\n" +
                "               \"value\": \"9988D5B1-7A4F-4873-9A3C-03C4435534F6\",\n" +
                "               \"type\": \"hidden\"\n" +
                "               },\n" +
                "               {\n" +
                "               \"name\": \"taskId\",\n" +
                "               \"value\": \"5730055\",\n" +
                "               \"type\": \"hidden\"\n" +
                "               }\n" +
                "               ],\n" +
                "    \"success\": \"0\"\n" +
                "}\n";
//        } else {
//        s = "{\n" +
//                "    \"title\": \"汇款申请单\",\n" +
//                "    \"inputs\": [\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.sysDept.deptName\",\n" +
//                "            \"value\": \"恒创软件数据中心\",\n" +
//                "            \"label\": \"申请部门\",\n" +
//                "            \"type\": \"text\",\n" +
//                "            \"readonly\": true\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.sysDept.deptId\",\n" +
//                "            \"value\": \"8a8a83e04c736b47014c7799e7de25bc\",\n" +
//                "            \"type\": \"hidden\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.tbrq\",\n" +
//                "            \"value\": \"2017-03-20\",\n" +
//                "            \"label\": \"填报日期\",\n" +
//                "            \"type\": \"date\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.htbh\",\n" +
//                "            \"label\": \"合同编号\",\n" +
//                "            \"type\": \"text\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"textFormat\": \"number\",\n" +
//                "            \"name\": \"remitApply.ykje\",\n" +
//                "            \"label\": \"用款金额(元)\",\n" +
//                "            \"required\": true,\n" +
//                "            \"type\": \"text\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.fykm\",\n" +
//                "            \"label\": \"用款用途\",\n" +
//                "            \"required\": true,\n" +
//                "            \"type\": \"text\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.ykzt\",\n" +
//                "            \"label\": \"用款状态\",\n" +
//                "            \"type\": \"select\",\n" +
//                "            \"options\": [\n" +
//                "                {\n" +
//                "                    \"value\": \"0\",\n" +
//                "                    \"label\": \"一般\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"selected\": true,\n" +
//                "                    \"value\": \"1\",\n" +
//                "                    \"label\": \"特急\"\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.ykfs\",\n" +
//                "            \"label\": \"用款方式\",\n" +
//                "            \"type\": \"select\",\n" +
//                "            \"options\": [\n" +
//                "                {\n" +
//                "                    \"value\": \"0\",\n" +
//                "                    \"label\": \"承兑\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"selected\": true,\n" +
//                "                    \"value\": \"1\",\n" +
//                "                    \"label\": \"现汇\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"value\": \"2\",\n" +
//                "                    \"label\": \"现金\"\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.skdw\",\n" +
//                "            \"label\": \"收款单位\",\n" +
//                "            \"required\": true,\n" +
//                "            \"type\": \"text\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.ykdw\",\n" +
//                "            \"label\": \"用款单位\",\n" +
//                "            \"required\": true,\n" +
//                "            \"type\": \"text\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"name\": \"remitApply.isYs\",\n" +
//                "            \"label\": \"是否预付款\",\n" +
//                "            \"required\": true,\n" +
//                "            \"type\": \"select\",\n" +
//                "            \"options\": [\n" +
//                "                {\n" +
//                "                    \"selected\": true,\n" +
//                "                    \"value\": null,\n" +
//                "                    \"label\": \"-----\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"value\": \"0\",\n" +
//                "                    \"label\": \"否\"\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"value\": \"1\",\n" +
//                "                    \"label\": \"是\"\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"buttons\": [\n" +
//                "        {\n" +
//                "            \"name\": \"buttonUrl\",\n" +
//                "            \"label\": \"保  存\",\n" +
//                "            \"url\": \"/ydpt/addHkspd!saveHkspd.do\",\n" +
//                "            \"value\":\"\"\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"success\": \"0\"\n" +
//                "}\n";
//        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 获取流程详情
     */
    private void getTaskDetail() {
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(app, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String data) {
                Log.i("getTaskDetail ", data);
                detail = JSON.parseObject(data, TaskDetail.class);
                Log.i("data", detail.toString());
                if (need_layout) {
                    if (isXt) {
                        detail.getInputs().add(new TaskDetail.Input("cmnt", "", "审批意见", "approval", true));
                    } else {
                        detail.getInputs().add(new TaskDetail.Input("cmnt", "", "审批意见", "approval", false));
                        detail.getInputs().add(new TaskDetail.Input("ccr", "", "抄送人员", "copy", false));
                        detail.getInputs().add(new TaskDetail.Input("clr", "", "协同人员", "teamwork", false));
                    }
                }
                if (isXt) {
                    List<TaskDetail.Button> buttons = new ArrayList<>();
                    buttons.add(new TaskDetail.Button("", "确定", "/ygoa/ydpt/approveAllXt.action", "true"));
                    detail.setButtons(buttons);
                } else {
                    if (detail.getButtons() == null) {
                        List<TaskDetail.Button> buttons = new ArrayList<>();
                        buttons.add(new TaskDetail.Button("approveResult", "同意", "/ygoa/ydpt/processTask.action", "true"));
                        buttons.add(new TaskDetail.Button("approveResult", "驳回", "/ygoa/ydpt/processTask.action", "false"));
                        buttons.add(new TaskDetail.Button("", "协同", "/ygoa/ydpt/loadJumpToXt.action", ""));
                        detail.setButtons(buttons);
                    }
                }
                listInput.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new TaskDetailAdapter(detail, getActivity());
                listInput.setAdapter(adapter);
            }
        };
        HttpUtil.getInstance(getActivity(), false).getTaskDetail(subscriber, app.getUser().getSessionId(), businessId, taskId, workflowType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class TaskDetailAdapter extends RecyclerView.Adapter<TaskDetailAdapter.ViewHolder> {
        public static final int LABEL = 1;
        public static final int TEXT = 2;
        public static final int HIDDEN = 3;
        public static final int DATE = 4;
        public static final int SELECT = 5;
        public static final int ACCORDION = 6;
        public static final int FILE = 7;
        public static final int BUTTON = 8;
        public static final int APPROVAL = 9; // 审批意见
        public static final int COPY = 10; // 抄送
        public static final int TEAMWORK = 11; // 协同

        private TaskDetail detail;
        private LayoutInflater inflater;


        public TaskDetailAdapter(TaskDetail detail, Context context) {
            this.detail = detail;
            this.inflater = LayoutInflater.from(context);

        }

        @Override
        public TaskDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case LABEL:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_label, parent, false));
                case TEXT:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_text, parent, false));
                case HIDDEN:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_hidden, parent, false));
                case DATE:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_date, parent, false));
                case SELECT:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_select, parent, false));
                case ACCORDION:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_accordion, parent, false));
                case APPROVAL:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_layout, parent, false));
                case COPY:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_copy, parent, false));
                case TEAMWORK:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_copy, parent, false));
                case BUTTON:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_button, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final TaskDetailAdapter.ViewHolder holder, final int position) {
            switch (getItemViewType(position)) {
                case LABEL:
                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.value.setText(detail.getInputs().get(position).getValue());
                    break;
                case TEXT:
                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.text.setText(detail.getInputs().get(position).getValue() == null ? "" : detail.getInputs().get(position).getValue());
                    if (detail.getInputs().get(position).isReadonly()) {
                        holder.text.setFocusable(false);
                        holder.text.setFocusableInTouchMode(false);
                    } else {
                        holder.text.setFocusableInTouchMode(true);
                        holder.text.setFocusable(true);
                        holder.text.requestFocus();
                    }
                    if (detail.getInputs().get(position).isRequired()) {
                        holder.required.setVisibility(View.VISIBLE);
                    } else {
                        holder.required.setVisibility(View.GONE);
                    }

                    holder.text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            detail.getInputs().get(position).setValue(holder.text.getText().toString().trim());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
                case DATE:
                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.value.setText(detail.getInputs().get(position).getValue());
                    break;
                case SELECT:

                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.select.setAdapter(new SpinnerAdapter(detail.getInputs().get(position).getOptions(), inflater));
                    for (int i = 0; i < detail.getInputs().get(position).getOptions().size(); i++) {
                        if (detail.getInputs().get(position).getOptions().get(i).isSelected()) {
                            holder.select.setSelection(i);
                            detail.getInputs().get(position).setValue(detail.getInputs().get(position).getOptions().get(i).getValue());
                        }
                    }
                    holder.select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                            detail.getInputs().get(position).setValue(detail.getInputs().get(position).getOptions().get(index).getValue());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    break;
                case ACCORDION:
                    List<TaskDetail.Accordion> values = JSON.parseArray(detail.getInputs().get(position).getValue(), TaskDetail.Accordion.class);
                    for (TaskDetail.Accordion bean : values) {
                        bean.initMaps();
                    }
                    Log.i("values", JSON.toJSONString(values));
                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.list_accordion.setAdapter(new AccordionAdapter(values, inflater));
                    setListViewHeightBasedOnChildren(holder.list_accordion);
                    holder.list_accordion.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            setListViewHeightBasedOnChildren(holder.list_accordion);
                        }
                    });
                    holder.list_accordion.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                        @Override
                        public void onGroupCollapse(int groupPosition) {
                            setListViewHeightBasedOnChildren(holder.list_accordion);
                        }
                    });

                    break;

                case APPROVAL:
                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.suggestion.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            detail.getInputs().get(position).setValue(holder.suggestion.getText().toString().trim());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
                case COPY:
                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.edit.setText(copy);
                    holder.add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SelectDepartmentActivity.class);
                            startActivityForResult(intent, 0x321);
                        }
                    });
                    holder.edit.setMentionTextColor(R.color.colorPrimary); //optional, set highlight color of mention string
                    holder.edit.setPattern("[\\u4e00-\\u9fa5\\w\\-]+[<]\\w+[>]"); //optional, set regularExpression
                    holder.edit.setOnMentionInputListener(new MentionEditText.OnMentionInputListener() {
                        @Override
                        public void onMentionCharacterInput() {
                            //call when '@' character is inserted into EditText
                        }
                    });
                    break;

                case TEAMWORK:
                    holder.label.setText(detail.getInputs().get(position).getLabel());
                    holder.edit.setText(coordination);
                    holder.add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SelectDepartmentActivity.class);
                            startActivityForResult(intent, 0x123);
                        }
                    });
                    holder.edit.setMentionTextColor(R.color.colorPrimary); //optional, set highlight color of mention string
                    holder.edit.setPattern("@[\\u4e00-\\u9fa5\\w\\-]+.*,$"); //optional, set regularExpression
                    holder.edit.setOnMentionInputListener(new MentionEditText.OnMentionInputListener() {
                        @Override
                        public void onMentionCharacterInput() {
                            //call when '@' character is inserted into EditText
                        }
                    });
                    break;

                case BUTTON:
                    final int index;
                    index = position - detail.getInputs().size();
                    holder.btn.setText(detail.getButtons().get(index).getLabel());
                    holder.btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            processTask(detail.getButtons().get(index).getUrl(),
                                    detail.getButtons().get(index).getName(),
                                    detail.getButtons().get(index).getValue());
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return detail.getInputs().size() + detail.getButtons().size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position < detail.getInputs().size()) {// 属于input中
                switch (detail.getInputs().get(position).getType()) {
                    case "label":
                        return LABEL;
                    case "text":
                        return TEXT;
                    case "hidden":
                        return HIDDEN;
                    case "date":
                        return DATE;
                    case "select":
                        return SELECT;
                    case "accordion":
                        return ACCORDION;
                    case "approval":
                        return APPROVAL;
                    case "copy":
                        return COPY;
                    case "teamwork":
                        return TEAMWORK;
                }
            } else {
                return BUTTON;
            }

            return super.getItemViewType(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            /**
             * label
             */
            @Nullable
            @BindView(R.id.label)
            TextView label;
            @Nullable
            @BindView(R.id.value)
            TextView value;

            /**
             * text
             */
            @Nullable
            @BindView(R.id.text)
            EditText text;
            @Nullable
            @BindView(R.id.required)
            TextView required;

            /**
             * select
             */

            @Nullable
            @BindView(R.id.select)
            Spinner select;

            /**
             * accordion
             */
            @Nullable
            @BindView(R.id.list_accordion)
            ExpandableListView list_accordion;

            /**
             * APPROVAL
             */
            @Nullable
            @BindView(R.id.suggestion)
            EditText suggestion;

            /**
             * copy teamwork
             */

            @Nullable
            @BindView(R.id.edit)
            MentionEditText edit;

            @Nullable
            @BindView(R.id.add)
            ImageView add;

            /**
             * button
             */
            @Nullable
            @BindView(R.id.btn)
            Button btn;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }


        }

        class AccordionAdapter extends BaseExpandableListAdapter {

            private List<TaskDetail.Accordion> data;
            private LayoutInflater inflater;

            public AccordionAdapter(List<TaskDetail.Accordion> data, LayoutInflater inflater) {
                this.data = data;
                this.inflater = inflater;
            }

            @Override

            public int getGroupCount() {
                return data.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return data.get(groupPosition).getMaps().size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return data.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return data.get(groupPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_detail_accordion_group, null);
                }
                convertView.setTag(R.layout.item_detail_accordion_group, groupPosition);
                convertView.setTag(R.layout.item_detail_accordion_child, -1);
                TextView value = convertView.findViewById(R.id.value);
                value.setText(data.get(groupPosition).getHeader());
                return convertView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_detail_accordion_child, null);
                }
                convertView.setTag(R.layout.item_detail_accordion_group, groupPosition);
                convertView.setTag(R.layout.item_detail_accordion_child, childPosition);
                TextView key = convertView.findViewById(R.id.key);
                TextView value = convertView.findViewById(R.id.value);
                key.setText(data.get(groupPosition).getMaps().get(childPosition).getKey());
                value.setText(data.get(groupPosition).getMaps().get(childPosition).getValue());
                if (childPosition % 2 == 0) {
                    key.setBackgroundColor(Color.parseColor("#f6fbff"));
                    value.setBackgroundColor(Color.parseColor("#f6fbff"));
                } else {
                    key.setBackgroundColor(Color.parseColor("#ffffff"));
                    value.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }
        }

        private void setListViewHeightBasedOnChildren(ExpandableListView listView) {
            //获取ListView对应的Adapter
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }
            int totalHeight = 0;
            for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);  //计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            //listView.getDividerHeight()获取子项间分隔符占用的高度
            //params.height最后得到整个ListView完整显示需要的高度
            listView.setLayoutParams(params);
        }

        class SpinnerAdapter extends BaseAdapter {

            private List<TaskDetail.Option> data;
            private LayoutInflater inflater;

            public SpinnerAdapter(List<TaskDetail.Option> data, LayoutInflater inflater) {
                this.data = data;
                this.inflater = inflater;
            }

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_detail_select_text, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.text.setText(data.get(position).getLabel());
                return convertView;
            }

            class ViewHolder {
                @BindView(R.id.text)
                TextView text;

                ViewHolder(View view) {
                    ButterKnife.bind(this, view);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("SelectDepartment", requestCode + "------" + resultCode);
        if (data != null) {
            ArrayList<Contacts> select = (ArrayList<Contacts>) data.getSerializableExtra("list");
            Log.i("select", JSON.toJSONString(select));
            if (requestCode == 0x123) {
                for (Contacts c : select) {
                    coordination.append(c.getUser_name() + "<" + c.getUser_code() + ">");
                    coordination.append(",");
                }
                adapter.notifyDataSetChanged();
            } else if (requestCode == 0x321) {
                for (Contacts c : select) {
                    copy.append(c.getUser_name() + "<" + c.getUser_code() + ">");
                    copy.append(",");
                }
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 同意和拒绝
     */
    private void processTask(String url, String name, String value) {
        if (!name.equals("")) {// 同意和拒绝
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("value", value);
            for (TaskDetail.Input i : detail.getInputs()) {
                if (i.isRequired() && i.getValue() == null) {
                    Toast.makeText(app, i.getLabel() + "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (i.getType()) {
                    case "text":
                        if (i.isRequired()) {
                            params.put(i.getName(), i.getValue());
                        }
                        break;
                    case "hidden":
                        params.put(i.getName(), i.getValue());
                        break;
                    case "date":
                        params.put(i.getName(), i.getValue());
                        break;
                    case "select":
                        params.put(i.getName(), i.getValue());
                        break;
                    case "copy":
                        params.put(i.getName(), copy.toString());
                        break;
                    case "teamwork":
                        params.put(i.getName(), coordination.toString());
                        break;
                }
            }
            Log.i("params", JSON.toJSONString(params));
            SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Task>() {

                @Override
                public void onNext(Task data) {
                    Log.i("processTask", data.toString());
                    if (data.getSuccess() == 0) {
                        getActivity().finish();
                    }
                }
            };
            HttpUtil.getInstance(getActivity(), false).processTask(new ProgressSubscriber(onNextListener, getActivity(), ""), url, params);
        } else {// 协同
            Map<String, String> params = new HashMap<>();
            for (TaskDetail.Input i : detail.getInputs()) {
                if (i.isRequired() && i.getValue() == null) {
                    Toast.makeText(app, i.getLabel() + "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (i.getType()) {
                    case "text":
                        if (i.isRequired()) {
                            params.put(i.getName(), i.getValue());
                        }
                        break;
                    case "hidden":
                        params.put(i.getName(), i.getValue());
                        break;
                    case "date":
                        params.put(i.getName(), i.getValue());
                        break;
                    case "select":
                        params.put(i.getName(), i.getValue());
                        break;
                    case "copy":
                        params.put(i.getName(), copy.toString());
                        break;
                    case "teamwork":
                        params.put(i.getName(), coordination.toString());
                        break;
                }
            }
            SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Task>() {

                @Override
                public void onNext(Task data) {
                    Log.i("processTask", data.toString());
                    if (data.getSuccess() == 0) {
                        getActivity().finish();
                    }
                }
            };
            HttpUtil.getInstance(getActivity(), false).processTask(new ProgressSubscriber(onNextListener, getActivity(), ""), url, params);
        }
    }


}
