package com.jm.asynctaskex1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView, View.OnClickListener {

    private RecyclerView recyclerView;
    private MainHelper helper;
    //    private List<Item> mDatas;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        helper = new MainHelper(this);
    }

    private void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
//        recyclerView.addItemDecoration();
        findViewById(R.id.btn_start_all).setOnClickListener(this);
        findViewById(R.id.btn_end_all).setOnClickListener(this);
    }

    @Override
    public void setListDatas(List<Item> datas) {
        itemAdapter.setDatas(datas);
    }

    @Override
    public void notifyDataSetChanged() {
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int itemPositon) {
        itemAdapter.notifyItemChanged(itemPositon);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_all:
                helper.startAllTask();
                break;
            case R.id.btn_end_all:
                helper.cancleAllTask();
                break;
        }
    }


    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
        private List<Item> datas = Collections.EMPTY_LIST;

        public void setDatas(List<Item> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        @Override
        public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getParent()).inflate(R.layout.item_download, null);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, int position) {
            Item item = datas.get(position);
            holder.tv1.setText(item.threadName + "    " + item.taskName);
            holder.pb.setProgress(item.progress);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tv1;
            TextView tv2;
            ProgressBar pb;

            public ItemViewHolder(View itemView) {
                super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv_thread_info);
                tv2 = (TextView) itemView.findViewById(R.id.tv_state);
                pb = (ProgressBar) itemView.findViewById(R.id.pb);
            }
        }

    }
}
