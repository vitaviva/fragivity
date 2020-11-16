package com.github.legacy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ReportFragment
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author wangpeng.rocky@bytedance.com
 */
@Navigator.Name("my_fragment_navigator")
open class MyNavigator<T : Fragment>(context: Context, manager: FragmentManager, containerId: Int) :
    FragmentNavigator(
        context, manager,
        containerId
    ) {

    override fun instantiateFragment(
        context: Context,
        fragmentManager: FragmentManager,
        className: String,
        args: Bundle?
    ): Fragment {
        return ReportFragment()
    }

    protected fun capture(): Type {
        val superclass = javaClass.genericSuperclass
        return (superclass as ParameterizedType?)!!.actualTypeArguments[0]
    }

}


inline fun <reified T : Fragment> createNavigator(
    context: Context,
    manager: FragmentManager,
    containerId: Int
) =
    object : MyNavigator<T>(context, manager, containerId) {

        override fun instantiateFragment(
            context: Context,
            fragmentManager: FragmentManager,
            className: String,
            args: Bundle?
        ): Fragment {
            val t = capture() as? Class<Fragment>
            return t?.newInstance() ?: throw RuntimeException("")
        }
    }
//@NavDestination.ClassType(Fragment::class)
//class MyDest(id: String) : NavDestination(id) {
//
//}