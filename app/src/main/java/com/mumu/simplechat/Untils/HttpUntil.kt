package com.mumu.simplechat.Untils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

/**
 * Created by limz on 17-12-22.
 */
object  HttpUntil {

    var name=""

    fun httpGet(urls:String) : String?
    {
        var result: String? = null
        var url: URL? = null
        var connection: HttpURLConnection? = null
        var `in`: InputStreamReader? = null
        try {
            url = URL(urls)
            connection = url!!.openConnection() as HttpURLConnection
            `in` = InputStreamReader(connection!!.getInputStream())
            val bufferedReader = BufferedReader(`in`)
            val strBuffer = StringBuffer()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                strBuffer.append(line)
                line = bufferedReader.readLine();
            }
            result = strBuffer.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (connection != null) {
                connection!!.disconnect()
            }
            if (`in` != null) {
                try {
                    `in`!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        }
        return result;
    }


    private var handler: Handler? = null
    private var my_result: String? = null
    var sessionid:String?=null

    fun httpGet(strUrlPath: String, params: Map<String, String>, encode: String): String {
        var strUrlPath = strUrlPath
        /* byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体*/
        /* String target="http://emb.mobi/register";*/
        var result: String? = null
        val append_url = getRequestData(params, encode).toString()
        strUrlPath = strUrlPath + "?" + append_url
        try {
            val url = URL(strUrlPath)
            val urlConn = url.openConnection() as HttpURLConnection
            if(sessionid != null) {
                urlConn.setRequestProperty("cookie", sessionid);
            }
            urlConn.connectTimeout = 5000//超时时间
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")//设置头部信息，其实没什么用

            //主角开始登场，不注意就是几个小时的调试，输入流
            val `in` = InputStreamReader(urlConn.inputStream)

            val buffer = BufferedReader(`in`)
            var inputLine: String? = null
            //循环逐行读取输入流中的内容

            result = ""//每次清空数据

            while (buffer.readLine().apply { inputLine = this } != null) {
                result += inputLine!! + "\n"
            }


            `in`.close()
            urlConn.disconnect()


        } catch (e: MalformedURLException) {
            result = "net problem"
            e.printStackTrace()
        } catch (ioe: IOException) {
            result = "net problem"
            ioe.printStackTrace()
            //return "err:" + ioe.message.toString()
        }

        return result!!

    }


    private fun getRequestData(params: Map<String, String>, encode: String): StringBuffer {
        val stringBuffer = StringBuffer()        //存储封装好的请求体信息
        try {
            for ((key, value) in params) {
                stringBuffer.append(key)
                        .append("=")
                        .append(URLEncoder.encode(value, encode))
                        .append("&")
            }
            stringBuffer.deleteCharAt(stringBuffer.length - 1)    //删除最后的一个"&"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return stringBuffer
    }


    fun httpPost(strUrlPath: String, params: Map<String, String>, encode: String): String {
        val data = getRequestData(params, encode).toString().toByteArray()
        println(data);
        var out:OutputStream?=null
        try {
            val url = URL(strUrlPath)

            val http = url.openConnection() as HttpURLConnection
            if(sessionid != null) {
                http.setRequestProperty("cookie", sessionid);
            }
            http.connectTimeout = 5000
            http.doInput = true
            http.doOutput = true
            http.requestMethod = "POST"
            http.useCaches = false//使用post方式不能用缓存
            //设置请求体的类型是文本类型
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            //设置请求体的长度
            http.setRequestProperty("Content-Length", data.size.toString())
            //获得输出流，向服务器写入数据
            out = http.outputStream
            out.write(data)

            val response = http.responseCode
            if (response == HttpURLConnection.HTTP_OK) {
                val inputStream = http.inputStream
                val res = dealResponseResult(inputStream)
                out.close()
                inputStream.close()
                http.disconnect()
                return res
            }


        } catch (ioe: IOException) {
            ioe.printStackTrace()
            return "FAIL_NET_PROBLEM"
        }

        return "FAIL"
    }

    fun getImag(urls:String): Bitmap {
        val url = URL(urls)
        val urlConn = url.openConnection() as HttpURLConnection
        urlConn.connectTimeout = 5000//超时时间
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        val cookieval = urlConn.getHeaderField("set-cookie")

        if (cookieval != null) {
            sessionid = cookieval!!.substring(0, cookieval!!.indexOf(";"))
        }
        val inputStream = urlConn.inputStream
        val bit = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        urlConn.disconnect()
        return bit
    }




    fun dealResponseResult(inputStream: InputStream): String {
        var resultData: String? = null      //存储处理结果
        val byteArrayOutputStream = ByteArrayOutputStream()
        val data = ByteArray(1024)
        var len = 0
        try {
            while (inputStream.read(data).apply { len = this } != -1) {
                byteArrayOutputStream.write(data, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        resultData = String(byteArrayOutputStream.toByteArray())
        return resultData
    }
}