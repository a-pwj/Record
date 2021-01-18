package com.pwj.record.ui.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwj.record.R;
import com.pwj.record.bean.FileInfoBean;
import com.pwj.record.utils.FileTypeUtils;

import java.util.HashSet;
import java.util.List;


/**
 * @Author: pwj
 * @Date: 2020/6/10 18:20
 * @FileName: FileListAdapter
 * @Description: description
 */
public class FileListAdapter extends BaseAdapter {
    private List<FileInfoBean> mFileList;
    private HashSet<FileInfoBean> mSelectedFiles;
    private Context mContext;

    public FileListAdapter(Context context, List<FileInfoBean> mFileList, HashSet<FileInfoBean> mSelectedFiles) {
        this.mFileList = mFileList;
        this.mContext = context;
        this.mSelectedFiles = mSelectedFiles;
    }

    @Override
    public int getCount() {
        return this.mFileList != null ? this.mFileList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (this.mFileList == null) {
            return null;
        } else {
            return position >= this.mFileList.size() ? null : this.mFileList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_file_list, null);
        FileListAdapter.ViewHolder viewHolder = new FileListAdapter.ViewHolder();
        viewHolder.fileCheckStateImageView = (ImageView) view.findViewById(R.id.rc_wi_ad_iv_file_check_state);
        viewHolder.fileIconImageView = (ImageView) view.findViewById(R.id.rc_wi_ad_iv_file_icon);
        viewHolder.fileNameTextView = (TextView) view.findViewById(R.id.rc_wi_ad_tv_file_name);
        viewHolder.fileDetailsTextView = (TextView) view.findViewById(R.id.rc_wi_ad_tv_file_details);
        FileInfoBean file = (FileInfoBean) mFileList.get(position);
        viewHolder.fileNameTextView.setText(file.getFileName());
        if (file.isDirectory()) {
            long filesNumber = file.getFileSize();
            if (filesNumber == 0L) {
                viewHolder.fileDetailsTextView.setText("空文件夹");
            } else {
                viewHolder.fileDetailsTextView.setText(mContext.getString(R.string.ad_folder_files_number, new Object[]{filesNumber}));
            }

            viewHolder.fileIconImageView.setImageResource(FileTypeUtils.getFileIconResource(mContext, file));
        } else {
            if (this.mSelectedFiles.contains(file)) {
                viewHolder.fileCheckStateImageView.setImageResource(R.drawable.rc_ad_list_file_checked);
            } else {
                viewHolder.fileCheckStateImageView.setImageResource(R.drawable.rc_ad_list_file_unchecked);
            }

            viewHolder.fileDetailsTextView.setText(mContext.getString(R.string.ad_file_size, new Object[]{FileTypeUtils.formatFileSize(file.getFileSize())}));
            viewHolder.fileIconImageView.setImageResource(FileTypeUtils.getFileIconResource(mContext, file));
            if (file.getFileSize() > 100L * 1024L * 1024L) {
                view.setAlpha(0.4F);
            }
        }

        return view;
    }

    private class ViewHolder {
        ImageView fileCheckStateImageView;
        ImageView fileIconImageView;
        TextView fileNameTextView;
        TextView fileDetailsTextView;

        private ViewHolder() {
        }
    }
}
