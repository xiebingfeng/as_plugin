package main.module.create

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import main.utils.*
import main.filetype.KotlinFileType
import java.lang.StringBuilder

object ModuleCreateFindUtils {

    fun createComponentModule(project: Project, action: AnAction?, newModuleName: String, moduleRemark: String): Boolean {
        val file = project.projectFile?.parent?.parent?.findChild("modulelibs")
        if (file == null) {
            showCommonDialog("找不到 modulelibs")
            return false
        }
        val moduleLibsDir = PsiManager.getInstance(project).findDirectory(file)

        val componentModule = moduleLibsDir?.findSubdirectory("componentModule")
        if (componentModule == null) {
            showCommonDialog("找不到 componentModule")
            return false
        }

        var componentDir: PsiDirectory? = null

        componentModule.findSubdirectory("src")?.apply {
            findSubdirectory("main")?.apply {
                findSubdirectory("java")?.apply {
                    findSubdirectory("com")?.apply {
                        findSubdirectory("krt")?.apply {
                            findSubdirectory("component")?.apply {
                                componentDir = this
                            }
                        }
                    }
                }
            }
        }

        if (componentDir == null) {
            showCommonDialog("找不到 com.krt.component")
            return false
        }

        val moduleServiceFactoryFile = componentDir!!.findFile("ModuleServiceFactory.kt")
        val serviceDir = componentDir!!.findSubdirectory("service")
//        val routhpathDir = componentDir!!.findSubdirectory("routhpath")
        if (null == moduleServiceFactoryFile) {
            showCommonDialog("找不到 ModuleServiceFactory.kt")
            return false
        }
        if (null == serviceDir) {
            showCommonDialog("找不到 service包")
            return false
        }
//        if (null == routhpathDir) {
//            showCommonDialog("找不到 routhpath包")
//            return false
//        }

        val serviceFile = serviceDir.findFile("I" + newModuleName.toCustomUpCase() + "Service.kt")
        if (serviceFile != null) {
            showCommonDialog("service包下有重复的   IXXXService类")
            return false
        }

//        val routerPathFile = routhpathDir.findFile("RouterPath" + newModuleName.toCustomUpCase() + ".kt")
//        if (routerPathFile != null) {
//            showCommonDialog("routerpath包下有重复的   RouterPath类")
//            return false
//        }

        val moduleServiceFactoryText = moduleServiceFactoryFile.text

        if (moduleServiceFactoryText.contains(newModuleName + "Service")) {
            showCommonDialog("有重复 ModuleServiceFactory配置")
            return false
        }

        //在ModuleServiceFactory
        val resultString = StringBuilder(moduleServiceFactoryText)
        val index = moduleServiceFactoryText.indexOf("//module service end,do not delete this")
        resultString.insert(index, "val " + newModuleName + "Service by lazy { initModule(I" + newModuleName.toCustomUpCase() + "ModuleService.APPLICATION) as? I" + newModuleName.toCustomUpCase() + "ModuleService }\n\n  ")
        moduleServiceFactoryFile.viewProvider.document?.setText(resultString.toString())
        FileFormatUtils.format(project, moduleServiceFactoryFile)

//        //创建RouterPathXXX
//        FileIOUtils2.readTemplateFile("/newModule/RouterPath.txt", action)
//                .replace(ModuleCreateManager.MODULE_REMARK, moduleRemark)
//                .replace(ModuleCreateManager.PACKAGE_NAME, newModuleName.toPackageName())
//                .replace(ModuleCreateManager.MODULE_NAME_UP, newModuleName.toCustomUpCase()).let {
//                    PsiFileFactory.getInstance(project)?.createFileFromText("RouterPath" + newModuleName.toCustomUpCase() + ".kt", KotlinFileType(), it)?.let {
//                        val addFile = routhpathDir.add(it)
//                        FileFormatUtils.format(project, addFile)
//                    }
//                }

        //创建IXXXModuleService
        FileIOUtils2.readTemplateFile("/newModule/IModuleService.txt", action)
                .replace(ModuleCreateManager.PACKAGE_NAME, newModuleName.toPackageName())
                .replace(ModuleCreateManager.MODULE_NAME_UP, newModuleName.toCustomUpCase()).let {
                    PsiFileFactory.getInstance(project)?.createFileFromText("I" + newModuleName.toCustomUpCase() + "ModuleService.kt", KotlinFileType(), it)?.let {
                        val addFile = serviceDir.add(it)
                        FileFormatUtils.format(project, addFile)
                    }
                }

        return true
    }
}