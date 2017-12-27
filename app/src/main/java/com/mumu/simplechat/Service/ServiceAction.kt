package com.mumu.simplechat.Service

import android.graphics.Bitmap
import com.mumu.simplechat.Untils.HttpUntil
import com.mumu.simplechat.bean.UserInfo

import org.json.JSONObject
import java.net.ConnectException

/**
 * Created by limz on 17-12-22.
 */
class ServiceAction {
    var url = "http://192.168.1.113:8080/XuJi";
    var name=""
    fun login(user: UserInfo):String{
        println("********${user.useName}******${user.password}")
        var  pae = mapOf<String,String>("name" to user.useName,"password" to user.password);
        var encode ="utf-8";
        var urls = url+"/login"
        var jsonString = HttpUntil.httpPost(urls,pae,encode);
        return jsonString
    }

    fun getImagCode(): Bitmap? {
        var urls= "$url/valicode.do"
        try{
            return HttpUntil.getImag(urls)
        }
        catch (e: ConnectException){
            return null
        }


    }

    fun register(user: UserInfo, password2:String, code:String):String{
        var pa= mapOf<String,String>("name" to user.useName,"password" to user.password,
                "password2" to password2,"imagcode" to code)
        var encode = "utf-8"
        var urls ="$url/register"
        return HttpUntil.httpPost(urls,pa,encode)
    }

    fun getFriend(name:String):String{
        var pa= mapOf<String,String>("name" to name)
        var encode = "utf-8"
        var urls="$url/friendInfo"
        return HttpUntil.httpPost(urls,pa,encode)
    }

    fun getAllFriend(name:String):String{
        var pa= mapOf<String,String>("name" to name)
        var encode = "utf-8"
        var urls="$url/getfriend"
        return HttpUntil.httpPost(urls,pa,encode)
    }

    fun addFriend(name:String,name2:String):String{
        var pa= mapOf<String,String>("name" to name,"name2" to name2)
        var encode="utf-8"
        var urls="$url/addfriend"
        return HttpUntil.httpPost(urls,pa,encode)
    }

    fun deleteFriend(name:String,name2:String):String{
        var pa= mapOf<String,String>("name" to name,"name2" to name2)
        var encode="utf-8"
        var urls="$url/deletefriend"
        return HttpUntil.httpPost(urls,pa,encode)
    }

    fun search(name:String):String{
        var pa=mapOf<String,String>("name" to name)
        val encode="utf-8"
        val urls="$url/search"
        return HttpUntil.httpPost(urls,pa,encode)
    }

}