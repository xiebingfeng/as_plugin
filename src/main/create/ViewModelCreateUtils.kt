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

    private var isListMoreEnabled = false

    fun create(className: String, anAction: AnAction, httpCallContent: HttpCallLayout, contentLayout: ContentLayout) {
        val tempFileName = "TemplateViewModel.txt"
        mResultContent.append(FileIOUtils2.readTemplateFile(tempFileName, anAction)
                .replace("\$packagename", directLoadUtils.packageDeclare))

        isListMoreEnabled = contentLayout.ckLoadMore!!.isSelected

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
            mPackageText.append("import com.xbf.base.ext.toParams\n")
            mPackageText.append("import com.xbf.network.httpGet\n")
        } else {
            mPackageText.append("import com.xbf.base.ext.toJson\n")
            mPackageText.append("import com.xbf.network.httpPost\n")
        }

        mHttpCallContent.append(HttpCoreUtils.getHttpCallCommon(httpCallLayout, isListMoreEnabled))
        if (mHttpCallContent.indexOf("lce =") > 0) {
            mPackageText.append("import com.xbf.network.base.LCEParams\n")
        }

        var dataContent = ""
        if (ProjectConfig.isNormalLayout) {
            mContentBefore.append("val firstComingLiveData = MutableLiveData<TestUrl.TestObject>()")
            dataContent = "firstComingLiveData.value = it\n"
        } else {
            mContentBefore.append("val firstComingLiveData = MutableLiveData<List<TestUrl.TestObject>>()")
            dataContent = "firstComingLiveData.value = it?.records\n"
            if (isListMoreEnabled) {
                dataContent += "    checkIsPageEnd(it)\n"
            }

            //加载更多
            if (isListMoreEnabled) {
                mContentBefore.insert(0, "    private var page = 1\n\nval isLastPageLiveData = MutableLiveData<Boolean>()\n\n")


                mContentBefore.append("\n\nval listMoreLiveData = MutableLiveData<List<TestUrl.TestObject>>()")
                var loadMoreData = "listMoreLiveData.value = it?.records\n"
                if (isListMoreEnabled) {
                    loadMoreData += "    checkIsPageEnd(it)\n"
                }

                mContentLoadMore.append("fun loadMoreData() {\n" + HttpCoreUtils.getHttpCallCommon(httpCallLayout, isListMoreEnabled, true) + "\n}")
                mContentLoadMore.replace(mContentLoadMore.indexOf("\$dataContent"), mContentLoadMore.indexOf("\$dataContent") + 12, loadMoreData)

                mContentLoadMore.append("\n\n  private fun checkIsPageEnd(it: TestUrl.TestList?) {\n" +
                        "        if (page >= it?.pages ?: 0) {\n" +
                        "            isLastPageLiveData.value = true\n" +
                        "        }\n" +
                        "    }")
            }
        }

        if (mHttpCallContent.contains("TestUrl.")) {
            mPackageText.append("import com.xbf.network.test.TestUrl\n")
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