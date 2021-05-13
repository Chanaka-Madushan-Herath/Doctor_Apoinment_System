package com.cmadushan.android.dr

public interface MyDrawerController {
    fun setDrawer_Locked()
    fun setDrawer_Unlocked()
    fun loadnavheader(name: CharSequence,email: CharSequence,url:String)

}