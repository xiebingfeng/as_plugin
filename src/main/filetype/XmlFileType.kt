package main.filetype

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

/**
 * Created by Seal.Wu on 2018/4/18.
 */

class XmlFileType : FileType {
    override fun getCharset(p0: VirtualFile, p1: ByteArray): String? {
        return null
    }

    override fun getDefaultExtension(): String {
        return ".xml"
    }

    override fun getIcon(): Icon? {
        return null
    }

    override fun getName(): String {
        return "Xml file"
    }

    override fun getDescription(): String {
        return "Xml source file"

    }

    override fun isBinary(): Boolean {
        return false
    }

    override fun isReadOnly(): Boolean {
        return false
    }
}
