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

    fun createComponentModule(project: Project, action: AnAction?, newModuleName: String, packageDifference: String): Boolean {
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
                        findSubdirectory(packageDifference)?.apply {
                            findSubdirectory("component")?.apply {
                                componentDir = this
                            }
                        }
                    }
                }
            }
        }

        if (componentDir == null) {
            showCommonDialog("找不到 com.$packageDifference.component")
            return false
        }

        val moduleServiceFactoryFile = componentDir!!.findFile("ModuleServiceFactory.kt")
        val serviceDir = componentDir!!.findSubdirectory("service")
        if (null == moduleServiceFactoryFile) {
            showCommonDialog("找不到 ModuleServiceFactory.kt")
            return false
        }
        if (null == serviceDir) {
            showCommonDialog("找不到 service包")
            return false
        }

        val serviceFile = serviceDir.findFile("I" + newModuleName.toCustomUpCase() + "Service.kt")
        if (serviceFile != null) {
            showCommonDialog("service包下有重复的   IXXXService类")
            return false
        }

        val moduleServiceFactoryText = moduleServiceFactoryFile.text

        if (moduleServiceFactoryText.contains(newModuleName + "Service")) {
            showCommonDialog("有重复 ModuleServiceFactory配置")
            return false
        }

        //在ModuleServiceFactory
        val resultString = StringBuilder(moduleServiceFactoryText)

        if (!resultString.contains("component.service.*")) {
            val indexPackageStart = moduleServiceFactoryText.indexOf(".component")
            resultString.insert(indexPackageStart + 10, "\nimport com." + packageDifference + ".component.service.*\nimport com.xbf.frame.app.initModule\n")
        }

        val index = resultString.indexOf("//module service end,do not delete this")
        resultString.insert(index, "val " + newModuleName + "Service by lazy { initModule(I" + newModuleName.toCustomUpCase() + "ModuleService.APPLICATION) as? I" + newModuleName.toCustomUpCase() + "ModuleService }\n\n  ")
        moduleServiceFactoryFile.viewProvider.document?.setText(resultString.toString())
        FileFormatUtils.format(project, moduleServiceFactoryFile)

        //创建IXXXModuleService
        FileIOUtils2.readTemplateFile("/newModule/IModuleService.txt", action)
                .replace(ModuleCreateManager.PACKAGE_NAME, newModuleName.toPackageName())
                .replace(ModuleCreateManager.PACKAGE_DIFFERENCE, packageDifference)
                .replace(ModuleCreateManager.MODULE_NAME_UP, newModuleName.toCustomUpCase()).let {
                    PsiFileFactory.getInstance(project)?.createFileFromText("I" + newModuleName.toCustomUpCase() + "ModuleService.kt", KotlinFileType(), it)?.let {
                        val addFile = serviceDir.add(it)
                        FileFormatUtils.format(project, addFile)
                    }
                }

        return true
    }
}