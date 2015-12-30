package com.xiachen.fatter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by xiachen on 15/12/7.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    private List<Bean> mDatas;
    private Context c;
    private OnItemClickLitener mOnItemClickLitener;
    private Bean deletedBean;
    private int deletePosition;


    public interface OnItemClickLitener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public ItemsAdapter(Context context) {
        this.c = context;
        queryDatas();
    }


    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemsViewHolder holder = new ItemsViewHolder(LayoutInflater.from(c)
                .inflate(R.layout.adapter_items, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, final int position) {
        Bean bean = mDatas.get(position);
        holder.tvWeight.setText(String.valueOf(bean.weight));
//            holder.tvDate.setText(String.valueOf(bean.date));
        holder.tvDate.setText(bean.date.substring(0, bean.date.length() - 3));
        holder.cbSnack.setChecked(bean.ifSnack);
        holder.cbBath.setChecked(bean.ifBath);
        holder.cbDefecation.setChecked(bean.ifDefecate);
        if (mOnItemClickLitener != null) {
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(position);
                }
            });
            holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {
        ViewGroup itemLayout;
        TextView tvWeight;
        TextView tvDate;
        CheckBox cbSnack;
        CheckBox cbBath;
        CheckBox cbDefecation;

        public ItemsViewHolder(View itemView) {
            super(itemView);
            itemLayout = (ViewGroup) itemView.findViewById(R.id.adapter_item);
            tvWeight = (TextView) itemView.findViewById(R.id.tv_weight);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            cbSnack = (CheckBox) itemView.findViewById(R.id.cb_snack);
            cbBath = (CheckBox) itemView.findViewById(R.id.cb_bath);
            cbDefecation = (CheckBox) itemView.findViewById(R.id.cb_defecation);
        }
    }

    public void addItem(int position) {
        queryDatas();
        notifyItemInserted(position + 1);
        notifyItemRangeChanged(position, mDatas.size());
    }


    public void removeItem(int position) {
        deletePosition = position;
        deletedBean = mDatas.get(position);
        mDatas.get(position).delete();
        queryDatas();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    public void refresh(Bean bean) {
        queryDatas();
        int position = mDatas.indexOf(bean);
        notifyItemChanged(position);
    }

    public void restore() {
        deletedBean.save();
        Bean bean = new Bean(deletedBean.weight, deletedBean.ifSnack, deletedBean.ifBath, deletedBean.ifDefecate);
        bean.setDate(deletedBean.date);
        bean.save();
        addItem(deletePosition);
    }


    public List<Bean> queryDatas() {
        mDatas = new Select().from(Bean.class).orderBy("Date DESC").execute();
        return mDatas;
    }

    public Bean get(int position) {
        return mDatas.get(position);
    }
}