package main.utils

import java.awt.Font
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel

fun JComponent.boldStyle(fontsize: Int = 14) {
    this.font = Font("宋体", Font.BOLD, fontsize)
}

fun StringBuilder.replaceText(replaceFrom: String, replaceTo: String): StringBuilder {
    this.replace(this.indexOf(replaceFrom), this.indexOf(replaceFrom) + replaceFrom.length, replaceTo)
    return this
}

fun StringBuilder.replaceText(replaceFrom: String, replaceTo: StringBuilder): StringBuilder {
    this.replace(this.indexOf(replaceFrom), this.indexOf(replaceFrom) + replaceFrom.length, replaceTo.toString())
    return this
}
