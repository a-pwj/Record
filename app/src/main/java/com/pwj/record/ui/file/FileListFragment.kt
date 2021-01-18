package com.pwj.record.ui.file

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.pwj.record.R
import com.pwj.record.bean.FileInfoBean
import com.pwj.record.ext.showToast
import com.pwj.record.utils.FileTypeUtils
import java.io.File
import java.util.*

/**
 * @Author:          pwj
 * @Date:            2020/6/10 18:19
 * @FileName:        FileListFragment
 * @Description:     description
 */
class FileListFragment : Fragment(), AdapterView.OnItemClickListener, View.OnClickListener {


    companion object {
        private const val MOBILE_DIR = "directory"
        private const val ALL_FILE_FILES = 1
        private const val ALL_VIDEO_FILES = 2
        private const val ALL_AUDIO_FILES = 3
        private const val ALL_other_FILES = 4
        private const val ALL_RAM_FILES = 5
        private const val ALL_SD_FILES = 6
        private const val ROOT_DIR = 100
        private const val SD_CARD_ROOT_DIR = 101
        private const val FILE_TRAVERSE_TYPE_ONE = 200
        private const val FILE_TRAVERSE_TYPE_TWO = 201
    }

    private val TAG = this.javaClass.simpleName
    private var mFileListTitleImageBack: ImageView? = null
    private var mFilesCategoryTitleTextView: TextView? = null
    private var mFileSelectStateTextView: TextView? = null
    private var mFilesListView: ListView? = null
    private var mFileLoadingLinearLayout: LinearLayout? = null
    private var mNoFileMessageTextView: TextView? = null
    private var mFileListAdapter: FileListAdapter? = null
    private var mLoadFilesTask: AsyncTask<File?, Void?, List<FileInfoBean>?>? = null
    private var mFilesList: List<FileInfoBean>? = null
    private val mSelectedFiles: HashSet<FileInfoBean> = HashSet<FileInfoBean>()
    private var currentDir: File? = null
    private var startDir: File? = null
    private var mFileInfoMessage: String? = null
    private var fileTraverseType = 0
    private var fileFilterType = 0
    private var mPath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = this.requireActivity().intent
        val rootDirType = intent.getIntExtra("rootDirType", -1)
        fileFilterType = intent.getIntExtra("fileFilterType", -1)
        fileTraverseType = intent.getIntExtra("fileTraverseType", -1)
        mPath = intent.getStringExtra("rootDir")
        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(MOBILE_DIR)) {
            currentDir = File(arguments.getString(MOBILE_DIR))
        } else if (rootDirType == ROOT_DIR) {
            val path = Environment.getExternalStorageDirectory().path
            currentDir = File(path)
        } else if (rootDirType == SD_CARD_ROOT_DIR) {
            currentDir = File(mPath)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fr_file_list, container, false)
        mFileListTitleImageBack = view.findViewById<ImageView>(R.id.rc_ad_iv_file_list_go_back) as ImageView
        mFilesCategoryTitleTextView = view.findViewById<TextView>(R.id.rc_ad_tv_file_list_title) as TextView
        mFileSelectStateTextView = view.findViewById<TextView>(R.id.rc_ad_tv_file_list_select_state) as TextView
        mFilesListView = view.findViewById<ListView>(R.id.rc_fm_lv_storage_folder_list_files) as ListView
        mFileLoadingLinearLayout = view.findViewById<LinearLayout>(R.id.rc_fm_ll_storage_folder_list_load) as LinearLayout
        mNoFileMessageTextView = view.findViewById<TextView>(R.id.rc_fm_tv_no_file_message) as TextView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFileList()
        var text = ""
        when (fileFilterType) {
            ALL_FILE_FILES -> text = "文本"
            ALL_VIDEO_FILES -> text = "视频"
            ALL_AUDIO_FILES -> text = "音频"
            ALL_other_FILES -> text = "其他"
            ALL_RAM_FILES -> text = "手机内存"
            ALL_SD_FILES -> text = "SD卡"
        }
        mFilesCategoryTitleTextView!!.text = "选择文件"
        mFilesListView!!.onItemClickListener = this
        mFileListTitleImageBack!!.setOnClickListener(this)
        mFileSelectStateTextView!!.setOnClickListener(this)
        mFileSelectStateTextView!!.isClickable = false
        mFileSelectStateTextView!!.isSelected = false
    }

    override fun onDestroyView() {
        mFilesListView = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (mLoadFilesTask != null) {
            mLoadFilesTask!!.cancel(true)
        }
        super.onDestroy()
    }


    private fun loadFileList() {
        if (mLoadFilesTask == null) {
            mLoadFilesTask = object : AsyncTask<File?, Void?, List<FileInfoBean>?>() {
                override fun onPreExecute() {
                    if (fileTraverseType == FILE_TRAVERSE_TYPE_ONE) {
                        showLoadingFileView()
                    }
                    super.onPreExecute()
                }

                override fun doInBackground(vararg params: File?): List<FileInfoBean>? {
                    mFileInfoMessage = ""
                    return try {
                        var fileInfos = mutableListOf<FileInfoBean>()
                        if (fileTraverseType == FILE_TRAVERSE_TYPE_TWO) {
                            val files: Array<File>? = params[0]?.listFiles(FileTypeUtils.ALL_FOLDER_AND_FILES_FILTER)
                            fileInfos = FileTypeUtils.getFileInfosFromFileArray(files)
                        } else if (fileTraverseType == FILE_TRAVERSE_TYPE_ONE) {
                            startDir = File(Environment.getExternalStorageDirectory().path)
                            when (fileFilterType) {
                                1 -> {
                                    fileInfos = FileTypeUtils.getTextFilesInfo(requireContext(), startDir)
                                    mFileInfoMessage = "文本"
                                }
                                2 -> {
                                    fileInfos = FileTypeUtils.getVideoFilesInfo(requireContext(), startDir)
                                    mFileInfoMessage = "视频"
                                }
                                3 -> {
                                    fileInfos = FileTypeUtils.getAudioFilesInfo(requireContext(), startDir)
                                    mFileInfoMessage = "音频"
                                }
                                4 -> {
                                    fileInfos = FileTypeUtils.getOtherFilesInfo(requireContext(), startDir)
                                    mFileInfoMessage = "其他"
                                }
                            }
                        }
                        if (fileInfos == null) {
                            ArrayList()
                        } else if (this.isCancelled) {
                            ArrayList()
                        } else {
                            Collections.sort(fileInfos as List<FileInfoBean>, FileTypeUtils.FileNameComparator())
                            fileInfos
                        }
                    } catch (var4: Exception) {
                        ArrayList<FileInfoBean>()
                    }
                }

                override fun onCancelled() {
                    mLoadFilesTask = null
                    super.onCancelled()
                }

                override fun onPostExecute(fileInfos: List<FileInfoBean>?) {
                    mFileLoadingLinearLayout!!.visibility = View.GONE
                    mFilesListView!!.visibility = View.VISIBLE
                    mLoadFilesTask = null
                    try {
                        mFilesList = fileInfos
                        if (mFilesList!!.isEmpty()) {
                            showNoFileMessage(mFileInfoMessage)
                            return
                        }
                        mFileListAdapter =
                            FileListAdapter(this@FileListFragment.activity, mFilesList, mSelectedFiles)
                        setListViewAdapter(mFileListAdapter)
                    } catch (var3: Exception) {
                        showNoFileMessage(var3.message)
                    }
                    super.onPostExecute(fileInfos)
                }


            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentDir)
        }
    }

    private fun setListViewAdapter(fileListAdapter: FileListAdapter?) {
        mFileListAdapter = fileListAdapter
        if (mFilesListView != null) {
            mFilesListView!!.adapter = fileListAdapter
        }
    }

    private fun showLoadingFileView() {
        mFilesListView!!.visibility = View.GONE
        mNoFileMessageTextView!!.visibility = View.GONE
        mFileLoadingLinearLayout!!.visibility = View.VISIBLE
    }

    private fun showNoFileMessage(message: String?) {
        mFilesListView!!.visibility = View.GONE
        mFileLoadingLinearLayout!!.visibility = View.GONE
        mNoFileMessageTextView!!.visibility = View.VISIBLE
        mNoFileMessageTextView!!.text = "没有文件"
    }

    private fun navigateTo(folder: File) {
        val activity: FileListActivity = this.activity as FileListActivity
        val fragment = FileListFragment()
        val args = Bundle()
        args.putString("directory", folder.absolutePath)
        fragment.arguments = args
        activity.showFragment(fragment)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val selectedObject = parent.getItemAtPosition(position)
        if (selectedObject is FileInfoBean) {
            val selectedFile: FileInfoBean = selectedObject as FileInfoBean
            if (selectedFile.isDirectory()) {
                navigateTo(File(selectedFile.getFilePath()))
            } else {
                var fileMaxSize: Int = 5
                var unit = "MB"
                if (fileMaxSize >= 1024) {
                    unit = "GB"
                }
                if (selectedFile.getFileSize() > fileMaxSize.toLong() * 1024L * 1024L) {
                    if (unit == "GB") {
                        fileMaxSize /= 1024
                    }
                    showToast(String.format(this.resources.getString(R.string.fr_file_size_limit), fileMaxSize, unit))
                    return
                }
                if (mSelectedFiles.contains(selectedFile)) {
                    mSelectedFiles.remove(selectedFile)
                    mFileListAdapter!!.notifyDataSetChanged()
                } else if (!view.isSelected && mSelectedFiles.size < 1) {
                    mSelectedFiles.add(selectedFile)
                    mFileListAdapter!!.notifyDataSetChanged()
                } else {
                    showToast("您最多只能选择1个文件")
                }
                if (mSelectedFiles.size > 0) {
                    mFileSelectStateTextView!!.isClickable = true
                    mFileSelectStateTextView!!.isSelected = true
                    mFileSelectStateTextView!!.text = "发送"
                } else {
                    mFileSelectStateTextView!!.isClickable = false
                    mFileSelectStateTextView!!.isSelected = false
                    mFileSelectStateTextView!!.text = "发送"
                }
            }
        }
    }

    override fun onClick(v: View) {
        if (v === mFileSelectStateTextView) {
            val intent = Intent()
            intent.putExtra("selectedFiles", mSelectedFiles)
            requireActivity().setResult(Activity.RESULT_OK, intent)
            requireActivity().finish()
        }
        if (v === mFileListTitleImageBack) {
            requireActivity().finish()
        }
    }


}
