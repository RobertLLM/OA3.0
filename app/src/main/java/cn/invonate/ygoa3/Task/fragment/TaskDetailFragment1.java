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
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.liyangyang.ios_dialog.widget.AlertDialog;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Entry.Contacts;
import cn.invonate.ygoa3.Entry.Task;
import cn.invonate.ygoa3.Entry.TaskDetail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.httpUtil.ProgressSubscriber;
import cn.invonate.ygoa3.httpUtil.SubscriberOnNextListener;

/**
 * Created by liyangyang on 2018/1/15.
 */

public class TaskDetailFragment1 extends Fragment implements LazyFragmentPagerAdapter.Laziable {

    @BindView(R.id.list_input)
    RecyclerView listInput;
    Unbinder unbinder;

    private YGApplication app;

    private List<TaskDetail.Input> inputs;

    private TaskDetailAdapter adapter;

    private StringBuilder copy = new StringBuilder();
    private StringBuilder coordination = new StringBuilder();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputs = (List<TaskDetail.Input>) getArguments().getSerializable("inputs");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail1, container, false);
        app = (YGApplication) getActivity().getApplication();
        unbinder = ButterKnife.bind(this, view);
        listInput.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TaskDetailAdapter(inputs, getActivity());
        listInput.setAdapter(adapter);
        return view;
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
        public static final int FOUR_SINGLE = 8;

        private List<TaskDetail.Input> data;
        private LayoutInflater inflater;

        public TaskDetailAdapter(List<TaskDetail.Input> data, Context context) {
            this.data = data;
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
                case FOUR_SINGLE:
                    return new ViewHolder(inflater.inflate(R.layout.item_detail_four, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final TaskDetailAdapter.ViewHolder holder, final int position) {
            switch (getItemViewType(position)) {
                case LABEL:
                    holder.label.setText(data.get(position).getLabel());
                    holder.value.setText(data.get(position).getValue());
                    break;
                case TEXT:
                    holder.label.setText(data.get(position).getLabel());
                    holder.text.setText(data.get(position).getValue() == null ? "" : data.get(position).getValue());
                    if (data.get(position).isReadonly() != null) {
                        holder.text.setFocusable(false);
                        holder.text.setFocusableInTouchMode(false);
                    } else {
                        holder.text.setFocusableInTouchMode(true);
                        holder.text.setFocusable(true);
                        holder.text.requestFocus();
                    }

                    holder.text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            data.get(position).setValue(holder.text.getText().toString().trim());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
                case DATE:
                    holder.label.setText(data.get(position).getLabel());
                    holder.value.setText(data.get(position).getValue());
                    break;
                case SELECT:
                    holder.label.setText(data.get(position).getLabel());
                    holder.select.setAdapter(new SpinnerAdapter(data.get(position).getOptions(), inflater));
                    for (int i = 0; i < data.get(position).getOptions().size(); i++) {
                        if (data.get(position).getOptions().get(i).isSelected()) {
                            holder.select.setSelection(i);
                            data.get(position).setValue(data.get(position).getOptions().get(i).getValue());
                        }
                    }
                    holder.select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                            data.get(position).setValue(data.get(position).getOptions().get(index).getValue());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    break;
                case ACCORDION:
                    List<TaskDetail.Accordion> values = JSON.parseArray(data.get(position).getValue(), TaskDetail.Accordion.class);
                    for (TaskDetail.Accordion bean : values) {
                        bean.initMaps();
                    }
                    Log.i("values", JSON.toJSONString(values));
                    holder.label.setText(data.get(position).getLabel());
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
                case FOUR_SINGLE:
                    List<String> value = JSON.parseArray(data.get(position).getValue(), String.class);
                    holder.text1.setText(value.get(0));
                    holder.text2.setText(value.get(1));
                    holder.text3.setText(value.get(2));
                    holder.text4.setText(value.get(3));
                    String color1 = data.get(position).getColor().get(0);
                    if ("".equals(color1)) {
                        holder.text1.setTextColor(Color.parseColor("#000000"));
                    } else {
                        if (color1.startsWith("#")) {
                            holder.text1.setTextColor(Color.parseColor(color1));
                        } else {
                            holder.text1.setTextColor(Color.parseColor("#" + color1));
                        }
                    }

                    String color2 = data.get(position).getColor().get(1);
                    if ("".equals(color2)) {
                        holder.text2.setTextColor(Color.parseColor("#000000"));
                    } else {
                        if (color2.startsWith("#")) {
                            holder.text2.setTextColor(Color.parseColor(color2));
                        } else {
                            holder.text2.setTextColor(Color.parseColor("#" + color2));
                        }
                    }

                    String color3 = data.get(position).getColor().get(2);
                    if ("".equals(color3)) {
                        holder.text3.setTextColor(Color.parseColor("#000000"));
                    } else {
                        if (color3.startsWith("#")) {
                            holder.text3.setTextColor(Color.parseColor(color3));
                        } else {
                            holder.text3.setTextColor(Color.parseColor("#" + color3));
                        }
                    }

                    String color4 = data.get(position).getColor().get(3);
                    if ("".equals(color4)) {
                        holder.text4.setTextColor(Color.parseColor("#000000"));
                    } else {
                        if (color4.startsWith("#")) {
                            holder.text4.setTextColor(Color.parseColor(color4));
                        } else {
                            holder.text4.setTextColor(Color.parseColor("#" + color4));
                        }
                    }
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public int getItemViewType(int position) {
            switch (data.get(position).getType()) {
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
                case "fourSingle":
                    return FOUR_SINGLE;

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
             * foursingle
             */
            @Nullable
            @BindView(R.id.text1)
            TextView text1;

            @Nullable
            @BindView(R.id.text2)
            TextView text2;

            @Nullable
            @BindView(R.id.text3)
            TextView text3;

            @Nullable
            @BindView(R.id.text4)
            TextView text4;

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
            public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_detail_accordion_group, null);
                }
                convertView.setTag(R.layout.item_detail_accordion_group, groupPosition);
                convertView.setTag(R.layout.item_detail_accordion_child, -1);
                final TextView value = convertView.findViewById(R.id.value);
                Button btn = convertView.findViewById(R.id.btn);
                if (data.get(groupPosition).getDelurl() == null) {
                    btn.setVisibility(View.GONE);
                } else {
                    btn.setVisibility(View.VISIBLE);
                }
                btn.setText(data.get(groupPosition).getDeltext());
                value.setText(data.get(groupPosition).getHeader());
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = data.get(groupPosition).getDelurl();
                        if (url.contains("comment=")) {
                            AlertDialog dialog = new AlertDialog(getActivity()).builder();
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_input_message, null);
                            final EditText input = view.findViewById(R.id.input);
                            dialog.setView(view)
                                    .setTitle("请输入理由")
                                    .setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (input.getText().toString().length() != 0) {
                                        singlePost(data.get(groupPosition).getDelurl() + input.getText().toString());
                                    }
                                }
                            }).show();
                        } else {
                            singlePost(url);
                        }

                    }
                });
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

    /**
     * 单条驳回
     *
     * @param url
     */
    private void singlePost(String url) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Task>() {
            @Override
            public void onNext(Task data) {
                if (data.getSuccess() == 0) {
                    getActivity().finish();
                } else {
                    Toast.makeText(app, "", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpUtil.getInstance(getActivity(), false).singlePost(new ProgressSubscriber(onNextListener, getActivity(), "请求中"), url);
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
     * 获取信息
     *
     * @return
     */
    public Map<String, String> getMessage() {
        Map<String, String> params = new HashMap<>();
        for (TaskDetail.Input i : inputs) {
            if (i.isRequired() && i.getValue() == null) {
                Toast.makeText(app, i.getLabel() + "不能为空", Toast.LENGTH_SHORT).show();
                return null;
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
            }
        }
        return params;
    }


}
