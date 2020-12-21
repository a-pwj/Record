package com.pwj.fragment.utils

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.util.*

/**
 * @Author:          pwj
 * @Date:            2020/7/10 10:21
 * @FileName:        FragmentUtils
 * @Description:     description
 */
object FragmentUtils {

    private const val TYPE_ADD_FRAGMENT = 0x01
    private const val TYPE_SHOW_FRAGMENT = 0x01 shl 1
    private const val TYPE_HIDE_FRAGMENT = 0x01 shl 2
    private const val TYPE_SHOW_HIDE_FRAGMENT = 0x01 shl 3
    private const val TYPE_REPLACE_FRAGMENT = 0x01 shl 4
    private const val TYPE_REMOVE_FRAGMENT = 0x01 shl 5
    private const val TYPE_REMOVE_TO_FRAGMENT = 0x01 shl 6


    private const val ARGS_ID = "args_id"
    private const val ARGS_IS_HIDE = "args_is_hide"
    private const val ARGS_IS_ADD_STACK = "args_is_add_stack"
    private const val ARGS_TAG = "args_tag"


    ///////////////////////////////////////////////////////////////////////////
    // add method
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Add fragment.
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param enterAnim   An animation or animator resource ID used for the enter animation on the
     * view of the fragment being added or attached.
     * @param exitAnim    An animation or animator resource ID used for the exit animation on the
     * view of the fragment being removed or detached.
     */
    fun add(fm: FragmentManager, add: Fragment, @IdRes containerId: Int, @AnimatorRes @AnimRes enterAnim: Int, @AnimatorRes @AnimRes exitAnim: Int) {
        add(fm, add, containerId, null, false, enterAnim, exitAnim, 0, 0)
    }

    /**
     * Add fragment.
     *
     * @param fm             The manager of fragment.
     * @param add            The fragment will be add.
     * @param containerId    The id of container.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     * appearing Fragment.
     */
    fun add(fm: FragmentManager, add: Fragment, @IdRes containerId: Int, vararg sharedElements: View) {
        add(fm, add, containerId, null, false, *sharedElements)
    }

    /**
     * Add fragment.
     *
     * @param fm          The manager of fragment.
     * @param adds        The fragments will be add.
     * @param containerId The id of container.
     * @param showIndex   The index of fragment will be shown.
     */
    fun add(fm: FragmentManager, adds: List<Fragment>, @IdRes containerId: Int, tags: Array<String?>?, showIndex: Int) {
        add(fm, adds.toTypedArray(), containerId, tags, showIndex)
    }

    /**
     * Add fragment. show index Fragment
     *
     * @param fm          The manager of fragment.
     * @param adds        The fragments will be add.
     * @param containerId The id of container.
     * @param showIndex   The index of fragment will be shown.
     */
    fun add(fm: FragmentManager, adds: Array<Fragment>, @IdRes containerId: Int, tags: Array<String?>?, showIndex: Int) {
        if (tags == null) {
            var i = 0
            val len = adds.size
            while (i < len) {
                putArgs(adds[i], Args(containerId, null, showIndex != i, false))
                ++i
            }
        } else {
            var i = 0
            val len = adds.size
            while (i < len) {
                putArgs(adds[i], Args(containerId, tags[i], showIndex != i, false))
                ++i
            }
        }
        operateNoAnim(fm, TYPE_ADD_FRAGMENT, null, *adds)
    }

    /**
     * Add fragment.no anim, no sharedElements
     *
     * @param fm          The manager of fragment.
     * @param add         The fragment will be add.
     * @param containerId The id of container.
     * @param tag         The tag of fragment.
     * @param isHide      True to hide, false otherwise.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     */
    fun add(fm: FragmentManager, add: Fragment, @IdRes containerId: Int, tag: String?, isHide: Boolean, isAddStack: Boolean) {
        putArgs(add, Args(containerId, tag, isHide, isAddStack))
        operateNoAnim(fm, TYPE_ADD_FRAGMENT, null, add)
    }

    /**
     * Add fragment.animation
     *
     * @param fm           The manager of fragment.
     * @param add          The fragment will be add.
     * @param containerId  The id of container.
     * @param tag          The tag of fragment.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     * view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     * view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     * view of the fragment being readded or reattached caused by
     * popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     * view of the fragment being removed or detached caused by
     * popBackStack() or similar methods.
     */
    fun add(
        fm: FragmentManager, add: Fragment, @IdRes containerId: Int, tag: String?, isAddStack: Boolean,
        @AnimatorRes @AnimRes enterAnim: Int, @AnimatorRes @AnimRes exitAnim: Int, @AnimatorRes @AnimRes popEnterAnim: Int, @AnimatorRes @AnimRes popExitAnim: Int
    ) {
        val ft = fm.beginTransaction()
        putArgs(add, Args(containerId, tag, false, isAddStack))
        addAnim(ft, enterAnim, exitAnim, popEnterAnim, popExitAnim)
        operate(TYPE_ADD_FRAGMENT, fm, ft, null, add)
    }

    /**
     * Add fragment. sharedElements
     *
     * @param fm             The manager of fragment.
     * @param add            The fragment will be add.
     * @param containerId    The id of container.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an  appearing Fragment.
     */
    fun add(fm: FragmentManager, add: Fragment, @IdRes containerId: Int, tag: String?, isAddStack: Boolean, vararg sharedElements: View) {
        val ft = fm.beginTransaction()
        putArgs(add, Args(containerId, tag, false, isAddStack))
        addSharedElement(ft, *sharedElements)
        operate(TYPE_ADD_FRAGMENT, fm, ft, null, add)
    }

    ///////////////////////////////////////////////////////////////////////////
    // show method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Show fragment.
     *
     * @param show The fragment will be show.
     */
    fun show(@NonNull show: Fragment) {
        putArgs(show, false)
        operateNoAnim(show.fragmentManager, TYPE_SHOW_FRAGMENT, null, show)
    }

    /**
     * Show fragment.
     *
     * @param fm The manager of fragment.
     */
    fun show(@NonNull fm: FragmentManager?) {
        val fragments: List<Fragment>? = getFragments(fm)
        if (fragments == null || fragments.isEmpty()) return
        for (show in fragments) {
            putArgs(show, false)
        }
        operateNoAnim(fm, TYPE_SHOW_FRAGMENT, null, *(fragments.toTypedArray()))
    }


    ///////////////////////////////////////////////////////////////////////////
    // hide method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Hide fragment.
     *
     * @param hide The fragment will be hide.
     */
    fun hide(@NonNull hide: Fragment) {
        putArgs(hide, true)
        operateNoAnim(hide.fragmentManager, TYPE_HIDE_FRAGMENT, null, hide)
    }

    /**
     * Hide fragment.
     *
     * @param fm The manager of fragment.
     */
    fun hide(@NonNull fm: FragmentManager?) {
        val fragments: List<Fragment>? = getFragments(fm)
        if (fragments == null || fragments.isEmpty()) return
        for (hide in fragments) {
            putArgs(hide, true)
        }
        operateNoAnim(fm, TYPE_HIDE_FRAGMENT, null, *(fragments.toTypedArray()))
    }

    ///////////////////////////////////////////////////////////////////////////
    // show hide method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Show fragment then hide other fragment.
     *
     * @param showIndex The index of fragment will be shown.
     * @param fragments The fragments will be hide.
     */
    fun showHide(showIndex: Int, @NonNull fragments: List<Fragment>) {
        showHide(fragments[showIndex], fragments)
    }

    /**
     * Show fragment then hide other fragment.
     *
     * @param show The fragment will be show.
     * @param hide The fragment will be hide.
     */
    fun showHide(@NonNull show: Fragment, @NonNull hide: List<Fragment>) {
        for (fragment in hide) {
            putArgs(fragment, fragment !== show)
        }
        operateNoAnim(show.fragmentManager, TYPE_SHOW_HIDE_FRAGMENT, show, *(hide.toTypedArray()))
    }

    /**
     * Show fragment then hide other fragment.
     *
     * @param showIndex The index of fragment will be shown.
     * @param fragments The fragment will be hide.
     */
    fun showHide(showIndex: Int, @NonNull vararg fragments: Fragment) {
        showHide(fragments[showIndex], *fragments)
    }

    /**
     * Show fragment then hide other fragment.
     *
     * @param show The fragment will be show.
     * @param hide The fragment will be hide.
     */
    fun showHide(@NonNull show: Fragment, @NonNull vararg hide: Fragment) {
        for (fragment in hide) {
            putArgs(fragment, fragment !== show)
        }
        operateNoAnim(show.fragmentManager, TYPE_SHOW_HIDE_FRAGMENT, show, *hide)
    }

    /**
     * Show fragment then hide other fragment.
     *
     * @param show The fragment will be show.
     * @param hide The fragment will be hide.
     */
    fun showHide(@NonNull show: Fragment, @NonNull hide: Fragment) {
        putArgs(show, false)
        putArgs(hide, true)
        operateNoAnim(show.fragmentManager, TYPE_SHOW_HIDE_FRAGMENT, show, hide)
    }

    ///////////////////////////////////////////////////////////////////////////
    // replace method
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Replace fragment.
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     */
    fun replace(@NonNull srcFragment: Fragment, @NonNull destFragment: Fragment, destTag: String?, isAddStack: Boolean) {
        val fm = srcFragment.fragmentManager ?: return
        val args: Args = getArgs(srcFragment)
        replace(fm, destFragment, args.id, destTag, isAddStack)
    }

    /**
     * Replace fragment.
     *
     * @param srcFragment    The source of fragment.
     * @param destFragment   The destination of fragment.
     * @param destTag        The destination of fragment's tag.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     * appearing Fragment.
     */
    fun replace(
        @NonNull srcFragment: Fragment,
        @NonNull destFragment: Fragment,
        destTag: String?,
        isAddStack: Boolean,
        vararg sharedElements: View
    ) {
        val fm = srcFragment.fragmentManager ?: return
        val args = getArgs(srcFragment)
        replace(fm, destFragment, args.id, destTag, isAddStack, *sharedElements)
    }

    /**
     * Replace fragment.
     *
     * @param fm          The manager of fragment.
     * @param fragment    The new fragment to place in the container.
     * @param containerId The id of container.
     * @param destTag     The destination of fragment's tag.
     * @param isAddStack  True to add fragment in stack, false otherwise.
     */
    fun replace(@NonNull fm: FragmentManager, @NonNull fragment: Fragment, @IdRes containerId: Int, destTag: String?, isAddStack: Boolean) {
        val ft: FragmentTransaction = fm.beginTransaction()
        putArgs(fragment, Args(containerId, destTag, false, isAddStack))
        operate(TYPE_REPLACE_FRAGMENT, fm, ft, null, fragment)
    }

    /**
     * Replace fragment.
     *
     * @param fm             The manager of fragment.
     * @param fragment       The new fragment to place in the container.
     * @param containerId    The id of container.
     * @param destTag        The destination of fragment's tag.
     * @param isAddStack     True to add fragment in stack, false otherwise.
     * @param sharedElements A View in a disappearing Fragment to match with a View in an
     * appearing Fragment.
     */
    fun replace(
        @NonNull fm: FragmentManager,
        @NonNull fragment: Fragment,
        @IdRes containerId: Int,
        destTag: String?,
        isAddStack: Boolean,
        vararg sharedElements: View
    ) {
        val ft: FragmentTransaction = fm.beginTransaction()
        putArgs(fragment, Args(containerId, destTag, false, isAddStack))
        addSharedElement(ft, *sharedElements)
        operate(TYPE_REPLACE_FRAGMENT, fm, ft, null, fragment)
    }

    /**
     * Replace fragment.
     *
     * @param srcFragment  The source of fragment.
     * @param destFragment The destination of fragment.
     * @param destTag      The destination of fragment's tag.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     * view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     * view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     * view of the fragment being readded or reattached caused by
     * popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     * view of the fragment being removed or detached caused by
     * popBackStack() or similar methods.
     */
    fun replace(
        @NonNull srcFragment: Fragment, @NonNull destFragment: Fragment, destTag: String?, isAddStack: Boolean,
        @AnimatorRes @AnimRes enterAnim: Int, @AnimatorRes @AnimRes exitAnim: Int, @AnimatorRes @AnimRes popEnterAnim: Int, @AnimatorRes @AnimRes popExitAnim: Int
    ) {
        val fm = srcFragment.fragmentManager ?: return
        val args: Args = getArgs(srcFragment)
        replace(fm, destFragment, args.id, destTag, isAddStack, enterAnim, exitAnim, popEnterAnim, popExitAnim)
    }


    /**
     * Replace fragment.
     *
     * @param fm           The manager of fragment.
     * @param fragment     The new fragment to place in the container.
     * @param containerId  The id of container.
     * @param destTag      The destination of fragment's tag.
     * @param isAddStack   True to add fragment in stack, false otherwise.
     * @param enterAnim    An animation or animator resource ID used for the enter animation on the
     * view of the fragment being added or attached.
     * @param exitAnim     An animation or animator resource ID used for the exit animation on the
     * view of the fragment being removed or detached.
     * @param popEnterAnim An animation or animator resource ID used for the enter animation on the
     * view of the fragment being readded or reattached caused by
     * popBackStack() or similar methods.
     * @param popExitAnim  An animation or animator resource ID used for the enter animation on the
     * view of the fragment being removed or detached caused by
     * popBackStack() or similar methods.
     */
    fun replace(
        @NonNull fm: FragmentManager,
        @NonNull fragment: Fragment,
        @IdRes containerId: Int,
        destTag: String?,
        isAddStack: Boolean,
        @AnimatorRes @AnimRes enterAnim: Int,
        @AnimatorRes @AnimRes exitAnim: Int,
        @AnimatorRes @AnimRes popEnterAnim: Int,
        @AnimatorRes @AnimRes popExitAnim: Int
    ) {
        val ft: FragmentTransaction = fm.beginTransaction()
        putArgs(fragment, Args(containerId, destTag, false, isAddStack))
        addAnim(ft, enterAnim, exitAnim, popEnterAnim, popExitAnim)
        operate(TYPE_REPLACE_FRAGMENT, fm, ft, null, fragment)
    }

    ///////////////////////////////////////////////////////////////////////////
    // pop method
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Pop fragment.
     *
     * @param fm The manager of fragment.
     */
    fun pop(@NonNull fm: FragmentManager) {
        pop(fm, true)
    }

    /**
     * Pop fragment.
     *
     * @param fm          The manager of fragment.
     * @param isImmediate True to pop immediately, false otherwise.
     */
    fun pop(@NonNull fm: FragmentManager, isImmediate: Boolean) {
        if (isImmediate) {
            fm.popBackStackImmediate()
        } else {
            fm.popBackStack()
        }
    }

    /**
     * Pop to fragment.
     *
     * @param fm            The manager of fragment.
     * @param popClz        The class of fragment will be popped to.
     * @param isIncludeSelf True to include the fragment, false otherwise.
     */
    fun popTo(@NonNull fm: FragmentManager, popClz: Class<out Fragment?>, isIncludeSelf: Boolean) {
        popTo(fm, popClz, isIncludeSelf, true)
    }

    /**
     * Pop to fragment.
     *
     * @param fm            The manager of fragment.
     * @param popClz        The class of fragment will be popped to.
     * @param isIncludeSelf True to include the fragment, false otherwise.
     * @param isImmediate   True to pop immediately, false otherwise.
     */
    fun popTo(@NonNull fm: FragmentManager, popClz: Class<out Fragment?>, isIncludeSelf: Boolean, isImmediate: Boolean) {
        if (isImmediate) {
            fm.popBackStackImmediate(popClz.name, if (isIncludeSelf) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
        } else {
            fm.popBackStack(popClz.name, if (isIncludeSelf) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
        }
    }

    /**
     * Pop all fragments.
     *
     * @param fm The manager of fragment.
     */
    fun popAll(@NonNull fm: FragmentManager) {
        popAll(fm, true)
    }

    /**
     * Pop all fragments.
     *
     * @param fm The manager of fragment.
     */
    fun popAll(@NonNull fm: FragmentManager, isImmediate: Boolean) {
        if (fm.backStackEntryCount > 0) {
            val entry = fm.getBackStackEntryAt(0)
            if (isImmediate) {
                fm.popBackStackImmediate(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            } else {
                fm.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    /**
     * Remove fragment.
     *
     * @param remove The fragment will be removed.
     */
    fun remove(@NonNull remove: Fragment) {
        operateNoAnim(remove.fragmentManager, TYPE_REMOVE_FRAGMENT, null, remove)
    }

    /**
     * Remove to fragment.
     *
     * @param removeTo      The fragment will be removed to.
     * @param isIncludeSelf True to include the fragment, false otherwise.
     */
    fun removeTo(@NonNull removeTo: Fragment, isIncludeSelf: Boolean) {
        operateNoAnim(removeTo.fragmentManager, TYPE_REMOVE_TO_FRAGMENT, if (isIncludeSelf) removeTo else null, removeTo)
    }

    /**
     * Remove all fragments.
     *
     * @param fm The manager of fragment.
     */
    fun removeAll(@NonNull fm: FragmentManager) {
        val fragments = getFragments(fm)
        if (fragments != null) {
            operateNoAnim(fm, TYPE_REMOVE_FRAGMENT, null, *fragments.toTypedArray())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // getFragments method
    ///////////////////////////////////////////////////////////////////////////

    fun getFragments(fm: FragmentManager?): List<Fragment>? {
        val fragments = fm?.fragments
        return if (fragments == null || fragments.isEmpty()) Collections.emptyList() else fragments
    }

    /**
     * Return the top fragment.
     *
     * @param fm The manager of fragment.
     * @return the top fragment
     */
    fun getTop(fm: FragmentManager): Fragment? {
        return getTopIsInStack(fm, null, false)
    }

    /**
     * Return the top fragment in stack.
     *
     * @param fm The manager of fragment.
     * @return the top fragment in stack
     */
    fun getTopInStack(@NonNull fm: FragmentManager): Fragment? {
        return getTopIsInStack(fm, null, true)
    }

    private fun getTopIsInStack(fm: FragmentManager, parentFragment: Fragment?, isInStack: Boolean): Fragment? {
        val fragments = getFragments(fm)
        if (fragments == null || fragments.isEmpty()) return parentFragment
        for (i in fragments.indices) {
            val fragment = fragments[i]
            if (fragment != null) {
                if (isInStack) {
                    val args = fragment.arguments
                    if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                        return getTopIsInStack(fragment.childFragmentManager, fragment, true)
                    } else {
                        return getTopIsInStack(fragment.childFragmentManager, fragment, false)
                    }
                }
            }
        }
        return parentFragment
    }

    /**
     * Return the top fragment which is shown.
     *
     * @param fm The manager of fragment.
     * @return the top fragment which is shown
     */
    fun getTopShow(@NonNull fm: FragmentManager): Fragment? {
        return getTopShowIsInStack(fm, null, false)
    }

    /**
     * Return the top fragment which is shown in stack.
     *
     * @param fm The manager of fragment.
     * @return the top fragment which is shown in stack
     */
    fun getTopShowInStack(@NonNull fm: FragmentManager): Fragment? {
        return getTopShowIsInStack(fm, null, true)
    }

    private fun getTopShowIsInStack(@NonNull fm: FragmentManager, parentFragment: Fragment?, isInStack: Boolean): Fragment? {
        val fragments = getFragments(fm)
        if (fragments == null || fragments.isEmpty()) return parentFragment
        for (i in fragments.indices.reversed()) {
            val fragment = fragments[i]
            if (fragment != null && fragment.isResumed && fragment.isVisible && fragment.userVisibleHint) {
                if (isInStack) {
                    val args: Bundle? = fragment.arguments
                    if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                        return getTopShowIsInStack(fragment.childFragmentManager, fragment, true)
                    }
                } else {
                    return getTopShowIsInStack(fragment.childFragmentManager, fragment, false)
                }
            }
        }
        return parentFragment
    }


    /**
     * Return all fragments in manager.
     *
     * @param fm The manager of fragment.
     * @return all fragments in manager
     */
    fun getAllFragments(@NonNull fm: FragmentManager): List<FragmentNode>? {
        return getAllFragments(fm, ArrayList())
    }

    private fun getAllFragments(@NonNull fm: FragmentManager, result: MutableList<FragmentNode>): List<FragmentNode>? {
        val fragments = getFragments(fm)
        if (fragments != null) {
            for (i in fragments.indices.reversed()) {
                val fragment = fragments[i]
                if (fragment != null) {
                    result.add(FragmentNode(fragment, getAllFragments(fragment.childFragmentManager, ArrayList())))
                }
            }
        }
        return result
    }

    /**
     * Return all fragments in stack in manager.
     *
     * @param fm The manager of fragment.
     * @return all fragments in stack in manager
     */
    fun getAllFragmentsInStack(@NonNull fm: FragmentManager): List<FragmentNode>? {
        return getAllFragmentsInStack(fm, ArrayList())
    }

    private fun getAllFragmentsInStack(@NonNull fm: FragmentManager, result: MutableList<FragmentNode>): List<FragmentNode>? {
        val fragments = getFragments(fm)
        if (fragments != null) {
            for (i in fragments.indices.reversed()) {
                val fragment = fragments[i]
                if (fragment != null) {
                    val args: Bundle? = fragment.arguments
                    if (args != null && args.getBoolean(ARGS_IS_ADD_STACK)) {
                        result.add(FragmentNode(fragment, getAllFragmentsInStack(fragment.childFragmentManager, ArrayList())))
                    }
                }
            }
        }
        return result
    }


    private fun addAnim(ft: FragmentTransaction, enter: Int, exit: Int, popEnter: Int, popExit: Int) {
        ft.setCustomAnimations(enter, exit, popEnter, popExit)
    }

    private fun addSharedElement(ft: FragmentTransaction, vararg views: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (view in views) {
                ft.addSharedElement(view, view.transitionName)
            }
        }
    }

    fun putArgs(fragment: Fragment, args: Args) {
        var bundle = fragment.arguments
        if (bundle == null) {
            bundle = Bundle()
            fragment.arguments = bundle
        }
        bundle.apply {
            putInt(ARGS_ID, args.id)
            putBoolean(ARGS_IS_HIDE, args.isHide)
            putBoolean(ARGS_IS_ADD_STACK, args.isAddStack)
            putString(ARGS_TAG, args.tag)
        }
    }

    private fun putArgs(fragment: Fragment, isHide: Boolean) {
        var bundle: Bundle? = fragment.arguments
        if (bundle == null) {
            bundle = Bundle()
            fragment.arguments = bundle
        }
        bundle.putBoolean(ARGS_IS_HIDE, isHide)
    }

    private fun getArgs(fragment: Fragment): Args {
        var bundle: Bundle? = fragment.arguments
        if (bundle == null) bundle = Bundle.EMPTY
        return Args(
            bundle!!.getInt(ARGS_ID, fragment.id),
            bundle.getBoolean(ARGS_IS_HIDE),
            bundle.getBoolean(ARGS_IS_ADD_STACK)
        )
    }

    private fun operateNoAnim(fm: FragmentManager?, type: Int, src: Fragment?, vararg dest: Fragment) {
        if (fm == null) return
        val ft: FragmentTransaction = fm.beginTransaction()
        operate(type, fm, ft, src, *dest)
    }

    private fun operate(type: Int, @NonNull fm: FragmentManager, ft: FragmentTransaction, src: Fragment?, vararg dest: Fragment) {
        if (src != null && src.isRemoving) return

        var name: String
        var args: Bundle?
        when (type) {
            TYPE_ADD_FRAGMENT -> {
                for (fragment in dest) {
                    args = fragment.arguments
                    if (args == null) return
                    name = args.getString(ARGS_TAG, fragment.javaClass.name)
                    val fragmentByTag = fm.findFragmentByTag(name)
                    if (fragmentByTag != null && fragmentByTag.isAdded) {
                        ft.remove(fragment)
                    }
                    ft.add(args.getInt(ARGS_ID), fragment, name)
                    if (args.getBoolean(ARGS_IS_HIDE)) ft.hide(fragment)
                    if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name)
                }
            }
            TYPE_HIDE_FRAGMENT -> {
                for (fragment in dest) {
                    ft.hide(fragment)
                }
            }
            TYPE_SHOW_FRAGMENT -> {
                for (fragment in dest) {
                    ft.show(fragment)
                }
            }
            TYPE_SHOW_HIDE_FRAGMENT -> {
                if (src != null) {
                    ft.show(src)
                    for (fragment in dest) {
                        if (fragment != src) {
                            ft.hide(fragment)
                        }
                    }
                }
            }
            TYPE_REPLACE_FRAGMENT -> {
                args = dest[0].arguments
                if (args == null) return
                name = args.getString(ARGS_TAG, dest[0].javaClass.name)
                ft.replace(args.getInt(ARGS_ID), dest[0], name)
                if (args.getBoolean(ARGS_IS_ADD_STACK)) ft.addToBackStack(name)
            }
            TYPE_REMOVE_FRAGMENT -> {
                for (fragment in dest) {
                    ft.remove(fragment)
                }
            }
            TYPE_REMOVE_TO_FRAGMENT -> {
                var i = dest.size - 1
                while (i >= 0) {
                    val fragment = dest[i]
                    if (fragment == dest[0]) {
                        if (src != null) ft.remove(fragment)
                        break
                    }
                    ft.remove(fragment)
                    --i
                }
            }
        }
        ft.commitAllowingStateLoss()
    }

    /**
     * Find fragment.
     *
     * @param fm      The manager of fragment.
     * @param findClz The class of fragment will be found.
     * @return the fragment matches class
     */
    fun findFragment(fm: FragmentManager, findClz: Class<out Fragment>): Fragment? {
        return fm.findFragmentByTag(findClz.name)
    }

    /**
     * Find fragment.
     *
     * @param fm  The manager of fragment.
     * @param tag The tag of fragment will be found.
     * @return the fragment matches class
     */
    fun findFragment(
        @NonNull fm: FragmentManager,
        @NonNull tag: String?
    ): Fragment? {
        return fm.findFragmentByTag(tag)
    }

    /**
     * Set background color for fragment.
     *
     * @param fragment The fragment.
     * @param color    The background color.
     */
    fun setBackgroundColor(@NonNull fragment: Fragment, @ColorInt color: Int) {
        fragment.view?.setBackgroundColor(color)
    }

    /**
     * Set background resource for fragment.
     *
     * @param fragment The fragment.
     * @param resId    The resource id.
     */
    fun setBackgroundResource(@NonNull fragment: Fragment, @DrawableRes resId: Int) {
        fragment.view?.setBackgroundResource(resId)
    }

    /**
     * Set background color for fragment.
     *
     * @param fragment   The fragment.
     * @param background The background.
     */
    fun setBackground(@NonNull fragment: Fragment, background: Drawable?) {
        val view = fragment.view ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = background
        } else {
            view.setBackgroundDrawable(background)
        }
    }

    /**
     * Dispatch the back press for fragment.
     *
     * @param fragment The fragment.
     * @return `true`: the fragment consumes the back press<br></br>`false`: otherwise
     */
    fun dispatchBackPress(@NonNull fragment: Fragment): Boolean {
        return (fragment.isResumed
                && fragment.isVisible
                && fragment.userVisibleHint
                && fragment is OnBackClickListener
                && (fragment as OnBackClickListener).onBackClick())
    }

    /**
     * Dispatch the back press for fragment.
     *
     * @param fm The manager of fragment.
     * @return `true`: the fragment consumes the back press<br></br>`false`: otherwise
     */
    fun dispatchBackPress(@NonNull fm: FragmentManager): Boolean {
        val fragments = getFragments(fm)
        if (fragments == null || fragments.isEmpty()) return false
        for (i in fragments.indices.reversed()) {
            val fragment = fragments[i]
            if (fragment != null && fragment.isResumed
                && fragment.isVisible
                && fragment.userVisibleHint
                && fragment is OnBackClickListener
                && (fragment as OnBackClickListener).onBackClick()
            ) {
                return true
            }
        }
        return false
    }

    /**
     * Return the simple name of fragment.
     *
     * @param fragment The fragment.
     * @return the simple name of fragment
     */
    fun getSimpleName(fragment: Fragment?): String? {
        return fragment?.javaClass?.simpleName ?: "null"
    }

    class Args constructor(val id: Int, val tag: String?, val isHide: Boolean, val isAddStack: Boolean) {
        constructor(id: Int, isHide: Boolean, isAddStack: Boolean) : this(id, null, isHide, isAddStack)
    }

    class FragmentNode(val fragment: Fragment, val next: List<FragmentNode>?) {

        override fun toString(): String {
            return (fragment.javaClass.simpleName + "->" + if (next == null || next.isEmpty()) "no child" else next.toString())
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////

    interface OnBackClickListener {
        fun onBackClick(): Boolean
    }

}