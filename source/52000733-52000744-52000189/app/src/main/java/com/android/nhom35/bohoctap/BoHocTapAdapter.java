package com.android.nhom35.bohoctap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.nhom35.R;

import java.util.List;

public class BoHocTapAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<BoHocTap> boHocTapList;

    public BoHocTapAdapter(Context context, int layout, List<BoHocTap> boHocTapList) {
        this.context = context;
        this.layout = layout;
        this.boHocTapList = boHocTapList;
    }

    @Override
    public int getCount() {
        return boHocTapList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtTenBo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoHocTapAdapter.ViewHolder holder;
        if(convertView == null){
            holder = new BoHocTapAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.txtTenBo = (TextView) convertView.findViewById(R.id.tvTenBo);
            convertView.setTag(holder);
        }else {
            holder = (BoHocTapAdapter.ViewHolder) convertView.getTag();
        }
        BoHocTap BoHT = boHocTapList.get(position);
        holder.txtTenBo.setText(BoHT.getTenBo());
        return convertView;
    }
}
