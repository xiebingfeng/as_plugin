package main.utils

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.JBColor
import main.config.ProjectConfig
import java.awt.Color
import javax.swing.JOptionPane

fun showCommonDialog(text: String) {
    if (ProjectConfig.isDebug) {
        JOptionPane.showMessageDialog(null, text, "", JOptionPane.PLAIN_MESSAGE);
    } else {
        Messages.showMessageDialog("", text, Messages.getInformationIcon())
    }
}

/**
 * 显示dialog
 *
 * @param editor
 * @param result 内容
 * @param time   显示时间，单位秒
 */
fun showPopupBalloon(editor: Editor, result: String, time: Int) {
    val factory = JBPopupFactory.getInstance();// Java GUI
    factory.createHtmlTextBalloonBuilder(result, null, JBColor(Color(116, 214, 238), Color(76, 112, 117)), null)
            .setFadeoutTime((time * 1000).toLong())
            .createBalloon()
            .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
}