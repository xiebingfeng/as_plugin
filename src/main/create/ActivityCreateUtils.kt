package main.create

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import main.action.DirectLoadUtils
import main.config.ProjectConfig
import main.container.ClassLayout
import main.filetype.KotlinFileType
import main.utils.FileFormatUtils
import main.utils.FileIOUtils2
import main.utils.replaceText
import main.utils.showCommonDialog

class ActivityCreateUtils(private val project: Project?, private val directLoadUtils: DirectLoadUtils) {

    private var mResultContent = StringBuilder()

    fun create(className: String, anAction: AnAction, classLayout: ClassLayout) {
        if (classLayout.ckActivityContainer?.isSelected == false) {
            return
        }

        mResultContent.append(FileIOUtils2.readTemplateFile("TemplateActivity.txt", anAction))

        initLast(className)

        if (ProjectConfig.isDebug) {
//            showCommonDialog(mResultContent.toString())
        } else {
            val fileName = className + "Activity"
            val file = directLoadUtils.psiFileFactory!!.createFileFromText("$fileName.kt", KotlinFileType(), mResultContent)

            file.let {
                val addFile = directLoadUtils.directory?.add(it)
                FileFormatUtils.format(project, addFile)
            }
        }
    }

    private fun initLast(className: String) {
        mResultContent.replaceText(Package_Name, directLoadUtils.packageDeclare)

        mResultContent = StringBuilder(mResultContent.toString().replace(Class_Name, className))
    }

    companion object {
        private const val Package_Name = "\$packagename"
        private const val Class_Name = "\$className"
    }

}