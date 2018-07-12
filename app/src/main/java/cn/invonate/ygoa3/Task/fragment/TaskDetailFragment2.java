package cn.invonate.ygoa3.Task.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Entry.TaskDetail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;
import cn.invonate.ygoa3.httpUtil.HttpUtil;
import cn.invonate.ygoa3.main.BasePicActivity;
import cn.invonate.ygoa3.main.FileWebActivity;

/**
 * Created by liyangyang on 2018/3/21.
 */

public class TaskDetailFragment2 extends Fragment {
    @BindView(R.id.list_input)
    RecyclerView listInput;

    Unbinder unbinder;

    List<TaskDetail.Input> inputs;

    private static String img[] = {"bmp", "jpg", "jpeg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
            "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf"};

    private YGApplication app;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputs = (List<TaskDetail.Input>) getArguments().getSerializable("inputs");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail2, container, false);
        unbinder = ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        listInput.setLayoutManager(new LinearLayoutManager(getActivity()));
        FileAdapter adapter = new FileAdapter(inputs, getActivity());
        listInput.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

        private List<TaskDetail.Input> data;
        private Context context;

        public FileAdapter(List<TaskDetail.Input> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_task_file_single, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FileAdapter.ViewHolder holder, final int position) {
            holder.name.setText(data.get(position).getLabel());
            holder.size.setText(data.get(position).getSize());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    Bundle bundle = new Bundle();
                    String url = "";
                    switch (data.get(position).getType()) {
                        case "fujian"://
                        case "wjpsfujian"://
                        case "attachment"://
                        case "completedUrl":
                            if (is_img(data.get(position).getLabel())) {
                                ArrayList<String> pic = new ArrayList<>();
                                pic.add(HttpUtil.URL_FILE + data.get(position).getUrl());
                                intent = new Intent(getActivity(), BasePicActivity.class);
                                bundle.putStringArrayList("imgs", pic);
                                bundle.putInt("index", 0);
                            } else {
                                //
                                intent = new Intent(getActivity(), FileWebActivity.class);
                                bundle.putString("url", "https://view.officeapps.live.com/op/view.aspx?src=ygoa.yong-gang.cn/ygoa/upload/"+data.get(position).getUrl());
                            }
                            break;
                        case "ifbreport":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb=5"
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "ifbdownload":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb=6"
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "ifbdownloadFromDisk":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb=7"
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "jzcwdownload":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb=8"
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "cwcwdownload":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb=0"
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "sbazgsdownload":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb="
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "ifbdownloadHtDisk":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb="
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "download":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb=10"
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                        case "ygsbDownload":
                            intent = new Intent(getActivity(), FileWebActivity.class);
                            url = HttpUtil.BASE_URL + "/ygoa/DownloadAttachmentOther?url="
                                    + data.get(position).getPk()
                                    + "&system_lb=4"
                                    + "&wdlx=" + data.get(position).getWdlx();
                            bundle.putString("url", url);
                            break;
                    }
                    if (intent != null) {
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }

        /**
         * 判断后缀是否图片
         *
         * @param name
         * @return
         */
        private boolean is_img(String name) {
            String[] names = name.split(".");
            if (names.length > 1) {
                for (String img : img) {
                    if (names[1].equals(img)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.name)
            TextView name;
            @BindView(R.id.size)
            TextView size;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

}
