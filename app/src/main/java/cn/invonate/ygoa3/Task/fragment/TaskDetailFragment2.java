package cn.invonate.ygoa3.Task.fragment;

import android.content.Context;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.invonate.ygoa3.Entry.TaskDetail;
import cn.invonate.ygoa3.R;
import cn.invonate.ygoa3.YGApplication;

/**
 * Created by liyangyang on 2018/3/21.
 */

public class TaskDetailFragment2 extends Fragment {
    @BindView(R.id.list_input)
    RecyclerView listInput;

    Unbinder unbinder;

    List<TaskDetail.Input> inputs;

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
        public void onBindViewHolder(FileAdapter.ViewHolder holder, int position) {
            holder.name.setText(data.get(position).getLabel());
            holder.size.setText(data.get(position).getSize());
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
