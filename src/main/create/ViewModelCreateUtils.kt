package main.create

import main.action.DirectLoadUtils
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import main.config.ProjectConfig
import main.container.ContentLayout
import main.container.HttpCallLayout
import main.utils.FileFormatUtils
import main.utils.FileIOUtils2
import main.filetype.KotlinFileType
import main.utils.showCommonDialog

class ViewModelCreateUtils(private val directLoadUtils: DirectLoadUtils, private val project: Project?) {

    private val mResultContent = StringBuilder()

    private var mHttpCallContent = StringBuilder()
    private var mPackageText = StringBuilder()
    private var mContentBefore = StringBuilder()
    private var mContentLoadMore = StringBuilder()

    fun create(className: String, anAction: AnAction, httpCallContent: HttpCallLayout, contentLayout: ContentLayout) {
        val tempFileName = "TemplateViewModel.txt"
        mResultContent.append(FileIOUtils2.readTemplateFile(tempFileName, anAction)
                .replace("\$packagename", directLoadUtils.packageDeclare))

        if (ProjectConfig.isNormalLayout) {
            httpCallContent.ckToObjectData?.isSelected = true
        } else {
            httpCallContent.ckToListData?.isSelected = true
        }

        if (httpCallContent.cbNetworkEnable!!.isSelected) {
            replaceHttpCall(httpCallContent, contentLayout)
        }

        replaceLast(className)

        if (ProjectConfig.isDebug) {
            showCommonDialog(mResultContent.toString())
        } else {
            val fileName = className + "ViewModel"
            val file = directLoadUtils.psiFileFactory?.createFileFromText("$fileName.kt", KotlinFileType(), mResultContent)
            file?.let {
                val addFile = directLoadUtils.directory?.add(it)
                FileFormatUtils.format(project, addFile)
            }
        }
    }

    private fun replaceHttpCall(httpCallLayout: HttpCallLayout, contentLayout: ContentLayout) {
        val isGet = httpCallLayout.rtGetRequest!!.isSelected
        if (isGet) {
            mPackageText.append("import com.krt.base.ext.toParams\n")
            mPackageText.append("import com.krt.network.httpGet\n")
        } else {
            mPackageText.append("import com.krt.base.ext.toJson\n")
            mPackageText.append("import com.krt.network.httpPost\n")
        }

        mHttpCallContent.append(HttpCoreUtils.getHttpCallCommon(httpCallLayout))
        if (mHttpCallContent.indexOf("lce =") > 0) {
            mPackageText.append("import com.krt.network.base.LCEParams\n")
        }

        var dataContent = ""
        if (ProjectConfig.isNormalLayout) {
            mContentBefore.append("val firstComingLiveData = MutableLiveData<TestUrl.TestData>()")
            dataContent = "firstComingLiveData.value = it"
        } else {
            mContentBefore.append("val firstComingLiveData = MutableLiveData<List<TestUrl.TestData>>()")
            dataContent = "firstComingLiveData.value = it"

            if (contentLayout.ckLoadMore!!.isSelected) {
                mContentBefore.append("\n\nval listMoreLiveData = MutableLiveData<List<TestUrl.TestData>>()")
                val loadMoreData = "listMoreLiveData.value = it"
                mContentLoadMore.append("fun loadMoreData() {\n" + HttpCoreUtils.getHttpCallCommon(httpCallLayout) + "\n}")
                mContentLoadMore.replace(mContentLoadMore.indexOf("\$dataContent"), mContentLoadMore.indexOf("\$dataContent") + 12, loadMoreData)
            }
        }

        if (mHttpCallContent.contains("TestUrl.")) {
            mPackageText.append("import com.krt.network.test.TestUrl\n")
        }

        mHttpCallContent.replace(mHttpCallContent.indexOf("\$dataContent"), mHttpCallContent.indexOf("\$dataContent") + 12, dataContent)
    }

    private fun replaceLast(className: String) {
        mResultContent.replace(mResultContent.indexOf("\$extrapackagename"), mResultContent.indexOf("\$extrapackagename") + 17, mPackageText.toString())
                .replace(mResultContent.indexOf("\$httpCall"), mResultContent.indexOf("\$httpCall") + 9, mHttpCallContent.toString())
                .replace(mResultContent.indexOf("\$contentBefore"), mResultContent.indexOf("\$contentBefore") + 14, mContentBefore.toString())
                .replace(mResultContent.indexOf("\$contentLoadMore"), mResultContent.indexOf("\$contentLoadMore") + 16, mContentLoadMore.toString())
                .replace(mResultContent.indexOf("\$className"), mResultContent.indexOf("\$className") + 10, className)
    }
}