package main.create

import main.container.HttpCallLayout
import main.utils.replaceText
import java.lang.StringBuilder

object HttpCoreUtils {

    fun getHttpCallCommon(httpCallLayout: HttpCallLayout, isListMoreEnabled: Boolean? = null, isListMore: Boolean? = null): String {
        val httpCallContent = StringBuilder()
        val params = StringBuilder()

        var url = httpCallLayout.etUrl!!.text.toString()
        if (url.isEmpty()) {
            when {
                httpCallLayout.ckJustReturnData?.isSelected == true -> url = "TestUrl.objectData"
                httpCallLayout.ckToObjectData?.isSelected == true -> url = "TestUrl.objectData"
                httpCallLayout.ckToListData?.isSelected == true -> {
                    if (isListMore == true) url = "TestUrl.listData2" else url = "TestUrl.listData1"
                }
            }
        }

        if (isListMoreEnabled == true) {
            if (isListMore == true) {
                params.append(" page++\n\n")
            } else {
                params.append(" page = 1\n\n")
            }
        }

        val isGet = httpCallLayout.rtGetRequest!!.isSelected
        if (isGet) {
            if (isListMoreEnabled == true) {
                params.append("val params = listOf(\"page\", \"page\").toParams()\n")
            } else {
                params.append("val params = listOf(\"\", \"\").toParams()\n")
            }
            httpCallContent.append("$params\nhttpGet<String>(\$httpParams)")
        } else {
            if (isListMoreEnabled == true) {
                params.append("val params = listOf(\"page\", \"page\").toJson()\n")
            } else {
                params.append("val params = listOf(\"\", \"\").toJson()\n")
            }
            httpCallContent.append("$params\nhttpPost<String>(\$httpParams)")
        }

        if (httpCallLayout.ckToObjectData?.isSelected == true) {
            httpCallContent.replaceText("String", "TestUrl.TestObject")
        } else if (httpCallLayout.ckToListData?.isSelected == true) {
            httpCallContent.replaceText("String", "TestUrl.TestList")
        }

        //////////////前面是基础结构，下面开始配置参数
        val httpParams = StringBuilder()
        if (url.startsWith("TestUrl.")) {
            httpParams.append(url + ",this")
        } else {
            httpParams.append("\"" + url + "\"" + ",this")
        }


        httpParams.append("," + if (isGet) "httpParams = params" else "upJson = params")

        //是否加载默认头
        if (httpCallLayout.ckDefaultTokenHead!!.isSelected) {
            httpParams.append(",defaultTokenHead = true")
        }

        //////////////LCE MODE
        //不启动lce，纯粹的请求网络，用户看不见
        val httpLCEParams = StringBuilder(", lce = LCEParams(\$lceParams)")
        val lceParams = StringBuilder()

        if (httpCallLayout.ckNotStartLce!!.isSelected) {
            lceParams.append("notStartLce = true,")
        }

        if (httpCallLayout.ckLoadingCheck!!.isSelected) {
            lceParams.append("showLoadingDialog = true,")
        }
        if (httpCallLayout.ckErrorCheck!!.isSelected) {
            lceParams.append("isErrorContainerShow = true,")
        }
        if (httpCallLayout.ckNotShowContentWhenSuccess!!.isSelected) {
            lceParams.append("notShowContentWhenSuccess = true,")
        }
        if (httpCallLayout.ckModifyErrorWarn!!.isSelected) {
            lceParams.append("errorWarn = \"请修改错误返回信息\",")
        }

        if (lceParams.isNotEmpty()) {
            lceParams.replace(lceParams.lastIndexOf(","), lceParams.lastIndexOf(",") + 1, "")
            httpParams.append(", lce = LCEParams(").append(lceParams).append(")")
        }

        val firstIndex = httpCallContent.indexOf("\$httpParams")
        httpCallContent.replace(firstIndex, firstIndex + 11, httpParams.toString())

        when {
            httpCallLayout.ckJustReturnData?.isSelected == true -> httpCallContent.append("{}")
            httpCallLayout.ckToObjectData?.isSelected == true -> httpCallContent.append(".toObject {\n\$dataContent}")
            httpCallLayout.ckToListData?.isSelected == true -> httpCallContent.append(".toObject {\n\$dataContent}")
        }

        //如果要增加方法名的话，即以  方法  的形式呈现
        if (httpCallLayout.createMethod) {
            if (httpCallLayout.ckMethodShow!!.isSelected) {
                var methodName = httpCallLayout.etMethodName?.text
                if (methodName?.isEmpty() == true) {
                    methodName = "test"
                }
                httpCallContent.insert(0, "fun $methodName(){\n").append("\n}")
            }
        }

        return httpCallContent.toString()
    }

}