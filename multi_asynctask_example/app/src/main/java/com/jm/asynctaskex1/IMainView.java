package com.jm.asynctaskex1;

import java.util.List;

/**
 * Created by zhujiaming on 17/7/16.
 */

public interface IMainView {

    void setListDatas(List<Item> datas);

    void notifyDataSetChanged();

    void notifyItemChanged(int itemPositon);
}
