package com.pwj.record.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwj.record.R

/**
 * A simple [Fragment] subclass.
 */
class Fragment2 : Fragment() {

    companion object {
        fun newInstance(type: Int): Fragment2 {
            val f = Fragment2()
            val args = Bundle()
            args.putInt("type", type)
            f.arguments = args
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment2, container, false)
    }

}
