package com.loong.common.base_kt

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

open abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {


    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使用反射得到viewbinding的class
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB
        setContentView(binding.root)

        initView()
    }

    abstract fun initView()
}


// e.g.
//class TESTActivity : BaseActivity<ActivityTestactivityBinding>() {
//    override fun initView() {
//        TODO("Not yet implemented")
//    }
//}