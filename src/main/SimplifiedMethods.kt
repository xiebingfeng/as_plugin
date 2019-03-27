package main

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import java.util.*


/**
 * do the main.action that could be roll-back
 */
fun executeCouldRollBackAction(project: Project?, action: (Project?) -> Unit) {
    CommandProcessor.getInstance().executeCommand(project, {
        ApplicationManager.getApplication().noneModalityState
        ApplicationManager.getApplication().runWriteAction {
            action.invoke(project)
        }

    }, "insertKotlin", "JsonToKotlin")
}

fun executeAfterTime(project: Project?, action: (Project?) -> Unit) {
    val timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            try {
                ApplicationManager.getApplication().invokeAndWait()
                {
                    executeCouldRollBackAction(project) {
                        action.invoke(it)
                    }
                }
            } catch (e: Exception) {

            }
        }
    }, 100)
}