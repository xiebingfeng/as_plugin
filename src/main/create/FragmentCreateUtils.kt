package main.create

import com.intellij.openapi.project.Project
import main.action.DirectLoadUtils
import main.action.MVVMAction
import main.config.ProjectConfig
import main.container.ClassLayout
import main.container.ContentLayout
import main.container.HttpCallLayout
import main.container.ToolBarLayout
import main.utils.FileIOUtils2
import main.utils.replaceText
import main.utils.showCommonDialog
import main.filetype.KotlinFileType
import java.lang.StringBuilder
import main.utils.FileFormatUtils


class FragmentCreateUtils(private val project: Project?, private val directLoadUtils: DirectLoadUtils) {

    private var mResultContent = StringBuilder()

    private var mPackageText = StringBuilder()
    private var mToolBarText = StringBuilder()
    private var mToolBarCustom = StringBuilder()
    private var mContentBefore = StringBuilder()
    private var mContentOnViewCreated = StringBuilder()
    private var mContentInitClick = StringBuilder()
    private var mContentFirstLoad = StringBuilder()
    private var mContentNewInstance = StringBuilder()
    private var mContentInitViewModel = StringBuilder()
    private var mContentInitBeforeLce = StringBuilder()

    private var mAnAction: MVVMAction? = null

    fun create(className: String, anAction: MVVMAction,
               layoutName: String,
               classLayout: ClassLayout,
               toolBar: ToolBarLayout,
               contentLayout: ContentLayout,
               httpCallContent: HttpCallLayout) {
        mAnAction = anAction

        mToolBarText.append("R.layout.").append(layoutName)
        mPackageText.append("import " + directLoadUtils.packageName + ".R\n")

        //从本地文件读取Fragment模板
        mResultContent.append(FileIOUtils2.readTemplateFile("TemplateFragment.txt", mAnAction))

        //生成内容
        initToolBarAndCreateXmlLayout(layoutName, className, toolBar, classLayout)
        initModuleLiveData(classLayout, httpCallContent, contentLayout)
        initViewBeforeLce(contentLayout)
        initView(contentLayout, layoutName)
        initClick(contentLayout)
        initFirstLoad(contentLayout)
        initNewInstance(classLayout, className)
        initLast(className)

        if (ProjectConfig.isDebug) {
            showCommonDialog(mResultContent.toString())
        } else {
            val fileName = className + "Fragment"
            val file = directLoadUtils.psiFileFactory!!.createFileFromText("$fileName.kt", KotlinFileType(), mResultContent)

            file.let {
                val addFile = directLoadUtils.directory?.add(it)
                FileFormatUtils.format(project, addFile)
            }
        }
    }

    private fun initToolBarAndCreateXmlLayout(layoutName: String,
                                              className: String,
                                              toolBar: ToolBarLayout,
                                              classLayout: ClassLayout) {
        //普通模式下
        if (toolBar.normalTitleLayout!!.isSelected) {
            //设置EventBus TODO
            if (!toolBar.ckEventBus!!.isSelected) {
                mToolBarText.append(",\nautoCheckEventBus = false")
            }
            //设置普通模式
            mToolBarText.append(",\ntoolBarStyle = ToolBarStyle.NORMAL")
            //设置标题
            val middleTitle = toolBar.etTitleName!!.text.toString().trim()
            mToolBarText.append(",\nmiddleTitle = \"").append(middleTitle).append("\"")

            //默认后退功能
            if (toolBar.ckBack!!.isSelected) {
                if (mToolBarCustom.isNotEmpty()) {
                    mToolBarCustom.append(",\n")
                }
                mToolBarCustom.append("customBackPop = true")
            }
            //默认ToolBar背景色
            if (toolBar.ckToolBarBackGround!!.isSelected) {
                if (mToolBarCustom.isNotEmpty()) {
                    mToolBarCustom.append(",\n")
                }
                mToolBarCustom.append("customToolBarBackGround = true")
            }

            //默认主背景色
            if (toolBar.ckMainBackGround!!.isSelected) {
                if (mToolBarCustom.isNotEmpty()) {
                    mToolBarCustom.append(",\n")
                }
                mToolBarCustom.append("customBackground = true")
            }

            //刚进入界面时默认显示还是隐藏
            if (classLayout.contentVisibleCB!!.isSelected) {
                mToolBarText.append(",\nisContentDefaultInvisible = true")
            }

            //标题栏下显示默认横线
            if (toolBar.ckBottomLine!!.isSelected) {
                mToolBarText.append(",\ntoolBarBottomLineVisible = true")
            }

            //右边按钮设置
            if (toolBar.titleRight!!.isSelected) {
                if (toolBar.rbRightViewText!!.isSelected) {
                    mToolBarText.append(",\nrightViewStyle = ToolBarViewStyle.TEXT")
                }
                if (toolBar.rbRightViewImage!!.isSelected) {
                    mToolBarText.append(",\nrightViewStyle = ToolBarViewStyle.ICON")
                }

                if (toolBar.rightColorWhite!!.isSelected) {
                    mToolBarText.append(",\nrightViewTextFontColor = getColor(R.color.base_white)")
                }
                if (toolBar.rightColorBlack!!.isSelected) {
                    mToolBarText.append(",\nrightViewTextFontColor = getColor(R.color.base_black)")
                }
                if (toolBar.rightColorCustom!!.isSelected) {
                    mToolBarText.append(",\nrightViewTextFontColor = getColor(R.color.base_white)")
                }
                mPackageText.append("import com.xbf.frame.frame.toolbar.style.ToolBarViewStyle\n")
                mPackageText.append("import com.xbf.frame.ext.getColor\n")

                //获取右按钮显示字
                val rightViewText = toolBar.rightViewTitle!!.text.toString().trim()
                if (rightViewText.isNotEmpty()) {
                    mToolBarText.append(",\nrightViewText = \"").append(rightViewText).append("\"")
                    mToolBarText.append(",\nrightViewClickListener = {}")
                }
            }
        }

        if (mToolBarCustom.isNotEmpty()) {
            mPackageText.append("import com.krt.base.ext.toCustom\n")
            mToolBarCustom.insert(0, ".toCustom(").append(")\n")
        }

        var layoutContent = ""
        //是否普通布局
        if (ProjectConfig.isNormalLayout) {
            layoutContent = FileIOUtils2.readTemplateFile("/layout/TemplateNormalLayout.txt", mAnAction)
        } else {
            layoutContent = FileIOUtils2.readTemplateFile("/layout/TemplateListViewLayout.txt", mAnAction)
            createListAdapter(className)
        }

        if (!ProjectConfig.isDebug) {
            var resDir = directLoadUtils.srcCurrentDir?.findSubdirectory("res")
            if (resDir == null) {
                resDir = directLoadUtils.srcCurrentDir?.createSubdirectory("res")
            }

            var layoutDir = resDir?.findSubdirectory("layout")
            if (layoutDir == null) {
                layoutDir = resDir?.createSubdirectory("layout")
            }

            //创建xml文件
            val xmlFile = directLoadUtils.psiFileFactory?.createFileFromText("$layoutName.xml", KotlinFileType(), layoutContent)
            xmlFile?.let {
                val addFile = layoutDir?.add(it)
                FileFormatUtils.format(project, addFile)
            }
        }
    }

    private fun initModuleLiveData(classLayout: ClassLayout, httpCallContent: HttpCallLayout, contentLayout: ContentLayout) {
        mContentInitViewModel.append(" override fun initViewModelLiveData() {\n" +
                "        super.initViewModelLiveData()\n" + View_Model_Load_Data + "\n" +
                "    }\n")

        var viewModelContent = ""
        if (httpCallContent.cbNetworkEnable!!.isSelected) {

            if (ProjectConfig.isNormalLayout) {
                viewModelContent = "viewModel?.firstComingLiveData?.observe(this, Observer {\n       })"
            } else {
                mPackageText.append("import com.xbf.frame.ext.obs\n")
                viewModelContent = "viewModel?.firstComingLiveData?.observe(this, Observer {\n" +
                        "            mAdapter.setNewData(it)\n" +
                        "        })"

                if (contentLayout.ckLoadMore!!.isSelected) {
                    viewModelContent += "\n\n viewModel?.listMoreLiveData?.observe(this, Observer {\n" +
                            "            it?.let {\n" +
                            "                mAdapter.addData(it)\n" +
                            "            }\n" +
                            "        })"

                    viewModelContent += "\n\n viewModel?.isLastPageLiveData?.obs(this) {\n" +
                            "            mAdapter.loadMoreEnd(true)\n" +
                            "        }\n"
                }
            }
        }

        mContentInitViewModel.replace(mContentInitViewModel.indexOf(View_Model_Load_Data), mContentInitViewModel.indexOf(View_Model_Load_Data) + View_Model_Load_Data.length, viewModelContent)
    }

    private fun initViewBeforeLce(contentLayout: ContentLayout) {
        if (contentLayout.ckOverWriteInitViewLce!!.isSelected) {
            mContentInitBeforeLce.append("   override fun initViewBeforeLceInitialization() {\n" +
                    "        super.initViewBeforeLceInitialization()\n" +
                    "    }")
        } else {
            mContentInitBeforeLce.append("")
        }
    }

    private fun initView(contentLayout: ContentLayout, layoutName: String) {
        //普通布局  或  列表
        if (!contentLayout.normalLayout!!.isSelected) {
            var content = "\n" + FileIOUtils2.readTemplateFile("/view/TemplateListView.txt", mAnAction)
            mPackageText.append("import kotlinx.android.synthetic." + directLoadUtils.projectName + ".").append(layoutName).append(".*\n")
            mPackageText.append("import com.xbf.frame.ext.initSwipeRefreshLayout\n")

            val listParams = StringBuilder()

            //列表模式下   初始化后是否自动刷新
            if (!contentLayout.autoRefresh!!.isSelected) {
                listParams.append(", autoStartRefresh = false")
            }

            //列表模式下   是否添加分割线
            if (contentLayout.itemDecoration!!.isSelected) {
                listParams.append(", itemDecorationEnabled = true")
            }

            //列表模式下  数据为空时，是否要显示数据为空的提示
            if (!contentLayout.dataEmpty!!.isSelected) {
                listParams.append(", isEmptyViewEnabled = false")
            }

            //列表模式下  加载更多功能
            if (contentLayout.ckLoadMore!!.isSelected) {
                listParams.append(", actionLoadMore = {\n" +
                        "            viewModel?.loadMoreData()\n" +
                        "        }")
            }

            content = content.replace("\$listParams", listParams.toString())

            mContentOnViewCreated.append(content + "\n")
        }
    }

    private fun initClick(contentLayout: ContentLayout) {
        if (contentLayout.ckClickMethod!!.isSelected) {
            mContentInitClick.append(" override fun initViewClickListener() {\n" +
                    "        super.initViewClickListener()\n" +
                    "    }").append("\n")
        }
    }

    private fun initFirstLoad(contentLayout: ContentLayout) {
        //普通布局  或  列表
        if (contentLayout.normalLayout!!.isSelected) {
            val content = if (contentLayout.repeatRequestMethod!!.isSelected) {
                FileIOUtils2.readTemplateFile("/view/TemplateNormalViewVisible.txt", mAnAction)
            } else {
                FileIOUtils2.readTemplateFile("/view/TemplateNormalView.txt", mAnAction)
            }

            mContentFirstLoad.append(content + "\n")
        }
    }

    //通过  newInstance()方法创建Fragment
    private fun initNewInstance(classLayout: ClassLayout, className: String) {
        if (classLayout.newInstanceCB!!.isSelected) {
            val content = FileIOUtils2.readTemplateFile("/instance/TimplateNewInstance.txt", mAnAction)
            content.replace(Class_Name, className)
            mContentNewInstance.append("\n" + content)

            var pairAdd = ""
            if (classLayout.pairAdd!!.isSelected) {
                mPackageText.append("import org.jetbrains.anko.bundleOf\n")
                pairAdd += "\nfragment.arguments = bundleOf(Pair(\"\", \"\"))"
            }
            mContentNewInstance.replace(mContentNewInstance.indexOf("\$pair"), mContentNewInstance.indexOf("\$pair") + 5, pairAdd)
        }
    }

    private fun createListAdapter(className: String) {
        val adapterDeclare = "private lateinit var mAdapter: " + className + "Adapter"
        mContentBefore.append(adapterDeclare)
        mPackageText.append("import " + directLoadUtils.packageDeclare + ".adapter." + className + "Adapter\n")

        if (!ProjectConfig.isDebug) {
            var adapterDir = directLoadUtils.directory?.findSubdirectory("adapter")
            if (adapterDir == null) {
                adapterDir = directLoadUtils.directory?.createSubdirectory("adapter")
            }

            val adapterFile = adapterDir?.findFile(className + "Adapter")
            if (adapterFile != null) {
                showCommonDialog("Adapter重复")
                return
            }

            var adapterContent = FileIOUtils2.readTemplateFile("/adapter/TemplateAdapter.txt", mAnAction)
            adapterContent = adapterContent
                    .replace(Extra_Package_Name, "import " + directLoadUtils.packageName + ".R\n")
                    .replace(Package_Name, directLoadUtils.packageDeclare + ".adapter")
                    .replace(Class_Name, className)

            val file = directLoadUtils.psiFileFactory?.createFileFromText(className + "Adapter.kt", KotlinFileType(), adapterContent)
            file?.let {
                val addFile = adapterDir?.add(it)
                FileFormatUtils.format(project, addFile)
            }
        }
    }

    private fun initLast(className: String) {
        if (mContentBefore.isNotEmpty()) {
            mContentBefore.insert(0, "\n")
        }

        mResultContent.replaceText(Extra_Package_Name, mPackageText)
                .replaceText(Package_Name, directLoadUtils.packageDeclare)
                .replaceText(Tool_Bar, mToolBarText)
                .replaceText(Tool_Bar_Custom, mToolBarCustom)
                .replaceText(Content_Before, mContentBefore)
                .replaceText(Content_On_View_Created, mContentOnViewCreated)
                .replaceText(Content_New_Instance, mContentNewInstance)
                .replaceText(Content_Init_View_Model, mContentInitViewModel)
                .replaceText(Content_Init_View_BEFORE_LCE, mContentInitBeforeLce)
                .replaceText(Content_Init_Click, mContentInitClick)
                .replaceText(Content_Init_First_Load, mContentFirstLoad)

        mResultContent = StringBuilder(mResultContent.toString().replace(Class_Name, className))
    }

    companion object {
        private const val Extra_Package_Name = "\$extrapackagename"
        private const val Package_Name = "\$packagename"
        private const val Tool_Bar = "\$toolbar"
        private const val Tool_Bar_Custom = "\$toolCustom"
        private const val Content_Before = "\$contentBefore"
        private const val Content_On_View_Created = "\$initView"
        private const val Content_New_Instance = "\$contentNewInstance"
        private const val Content_Init_View_BEFORE_LCE = "\$initBeforeLceInitialization"
        private const val Content_Init_View_Model = "\$contentInitViewModel"
        private const val Content_Init_Click = "\$contentInitClick"
        private const val Class_Name = "\$className"
        private const val View_Model_Load_Data = "\$viewModelLoadData"
        private const val Content_Init_First_Load = "\$contentInitFirstLoad "
    }

}
