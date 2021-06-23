package com.cmadushan.android.dr

interface MyDrawerController {
    fun setDrawer_Locked()
    fun setDrawer_Unlocked()
    fun loadnavheader(name: CharSequence,email: CharSequence,url:String)

}