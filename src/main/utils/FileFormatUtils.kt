package main.utils

import com.intellij.codeInsight.actions.AbstractLayoutCodeProcessor
import com.intellij.codeInsight.actions.OptimizeImportsProcessor
import com.intellij.codeInsight.actions.RearrangeCodeProcessor
import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

object FileFormatUtils {

    fun format(project: Project?, file: PsiElement?) {
        var processor: AbstractLayoutCodeProcessor =
                ReformatCodeProcessor(project, file as PsiFile, null, false)
        processor = OptimizeImportsProcessor(processor)
        processor = RearrangeCodeProcessor(processor)
        processor.run()
    }

    fun format(project: Project?, file: PsiFile?) {
        var processor: AbstractLayoutCodeProcessor =
                ReformatCodeProcessor(project, file as PsiFile, null, false)
        processor = OptimizeImportsProcessor(processor)
        processor = RearrangeCodeProcessor(processor)
        processor.run()
    }

}