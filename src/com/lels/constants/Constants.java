package com.lels.constants;

public class Constants {
	/** 接口的开头 **/

	// 开发环境 http://testielts2.staff.xdf.cn /IELTS_2_DEV /upload_dev
	// 产品测试环境 http://testielts2.staff.xdf.cn /IELTS_2 /upload
	// 业务测试环境 http://ieltstest.staff.xdf.cn /IELTS /upload
	//准生产环境  http://ilearning.staff.xdf.cn/IELTS/

	public static final String URL_Base = "http://testielts2.staff.xdf.cn";
//	 public static final String URL_Base = "http://10.62.49.51:8080";
	public static final String URL_PROJECT = "/IELTS_2_DEV";
	public static final String URL_Mapped = "/upload_dev";
	
//	public static final String URL_Base = "http://ilearning.staff.xdf.cn";
//	public static final String URL_PROJECT = "/IELTS";
//	public static final String URL_Mapped = "/upload";

//	 public static final String URL_Base = "http://10.62.3.94:8080";
//	 public static final String URL_PROJECT = "/IELTS_2";
//	 public static final String URL_Mapped = "/upload_dev";

	public static final String URL_API = "/api";
	public static final String URL_UserImage = "/userImage";
	public static final String URL_ = URL_Base + URL_PROJECT + URL_API;

	// // 加载视频 单独的 url
	public static final String URL_V = URL_Base + URL_PROJECT;

	private static final String URL_A = URL_Base + URL_Mapped;

	/**
	 * 用户头像路径
	 */
	public static final String URL_TeacherIMG = URL_Base + URL_Mapped
			+ URL_UserImage + "/";
	/**
	 * 任务-获取模考、练习信息 iphone、Android 学生App
	 */
	public static final String URL_HomegetMaterialsInfo = URL_
			+ "/Home/getMaterialsInfo";
	/**
	 * 任务-获取资料信息 iphone、Android 学生App
	 */
	public static final String URL_HomegetPapersInfoByMID = URL_
			+ "/Home/getPapersInfoByMID";
	/**
	 * 任务-视频资料中试题库 iphone、Android 学生App
	 */
	public static final String URL_HomegetPapersInfo = URL_
			+ "/Home/getPapersInfo";

	/**
	 * 获取视频信息 iphone、Android 学生/教师App
	 */
	public static final String URL_STUDYONLINE_LOOKVIDEOINFO = URL_
			+ "/Material/lookVideoInfo";
	/**
	 * 视频资料查看 iphone、Android 已登录游客/学生/教师App
	 */
	public static final String URL_STUDYONLINE_MEDIA_LOOKUPVIDEO = URL_
			+ "/Material/lookUpVideoMaterials";

	/**
	 * 广告图片路径
	 */
	public static final String URL_IMAGE_ADVERT = URL_A + "/advertImage/";

	/**
	 * 教师任务提醒 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesremindTask = URL_
			+ "/TeacherClasses/remindTask";

	/**
	 * 班级待批改信息 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesgetCorrectList = URL_
			+ "/TeacherClasses/getCorrectList";
	/**
	 * 口语练习批改 页面 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassescontactCorrecting = URL_
			+ "/TeacherClasses/contactCorrecting";
	/**
	 * 口语完成批改 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesfinishKyCorrecting = URL_
			+ "/TeacherClasses/finishKyCorrecting";

	/**
	 * 写作完成批改 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesfinishXzCorrecting = URL_
			+ "/TeacherClasses/finishXzCorrecting";
	/**
	 * 口语模考批改 页面 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesvoiceCorrecting = URL_
			+ "/TeacherClasses/voiceCorrecting";
	/**
	 * 作文批改 页面 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesexamCorrecting = URL_
			+ "/TeacherClasses/examCorrecting";
	/**
	 * APP教师登录
	 */
	public static final String URL_TEACHER_LOGIN = URL_
			+ "/User/AppTeacherLogin";

	/**
	 * 教师端获取班级任务列表 iphone、Android 学生App
	 */
	public static final String URL_HomeloadTaskList = URL_
			+ "/Home/loadTaskList";
	/**
	 * APP 教师 在线学习 视频公开课 iphone、Android 教师App
	 */
	public static final String URL_Teacher_DATE = URL_
			+ "/onlinestudy/teacherMaterials";

	/**
	 * 教师资料列表 iphone、Android 学生App 筛选
	 */
	public static final String URL_Teacher_DATE_SCREEN = URL_
			+ "/onlinestudy/teacherMaterialsFilter";

	/**
	 * APP 教师 课表 iphone、Android 教师App
	 */
	public static final String URL_Teacher_MONTH_LESSONS = URL_
			+ "/Task/GetTeacherMonthLessonsForApp";

	/**
	 * APP 教师日历 列表 GET
	 */
	public static final String URL_TEACHER_SCHEDULE = URL_
			+ "/Task/GetTeacherMonthLessonsForApp";
	/**
	 * APP 获取教师的课次信息 生成课堂暗号 教师APP GET
	 */
	public static final String URL_GetTeacherLessonAndPassCode = URL_
			+ "/ActiveClass/GetTeacherLessonAndPassCode";

	/**
	 * APP 教师端，点击“同步到课堂” 教师APP GET
	 */
	public static final String URL_TeacherSyncActiveClass = URL_
			+ "/ActiveClass/TeacherSyncActiveClass";

	/**
	 * APP 每隔3秒向服务器发送在线的消息，获取最新在线学生列表 包括在线人数/总人数和各学生信息 教师APP GET
	 */
	public static final String URL_TeacherOrStudentGetStudentOnLine = URL_
			+ "/ActiveClass/TeacherOrStudentGetStudentOnLine";
	/**
	 * APP 教师选择临近四节课堂的其中一节课后，传后台进行确认保存 GET
	 */
	public static final String URL_SaveActiveClassPro = URL_
			+ "/ActiveClass/SaveActiveClassPro";
	/**
	 * APP 班级课程：上课中、未开课、已结课，班级课程列表 iphone、Android 教师App POST
	 */
	public static final String URL_TeacherClassesloadClasses = URL_
			+ "/TeacherClasses/loadClasses";
	/**
	 * 整套提交的(考试当中和考试结束后显示的)学生成绩单
	 */
	public static final String URL_findWholeSubmitModeStudentExamMark = URL_
			+ "/ActiveClass/findWholeSubmitModeStudentExamMark";

	/**
	 * 整套提交的学生练习报告
	 */
	public static final String URL_findWholeSubmitModeStudentExerciseReport = URL_
			+ "/ActiveClass/findWholeSubmitModeStudentExerciseReport";

	/**
	 * 单题提交的(考试当中(心跳)和考试结束后显示的)学生成绩单
	 */
	public static final String URL_findSingleSubmitModeStudentExamMark = URL_
			+ "/ActiveClass/findSingleSubmitModeStudentExamMark";

	/**
	 * 单题提交的学生练习报告
	 */
	public static final String URL_findSingleSubmitModeStudentExerciseReport = URL_
			+ "/ActiveClass/findSingleSubmitModeStudentExerciseReport";

	/**
	 * APP 教师随堂练习，本课次，任务为练习并且为随堂的练习列表
	 */
	public static final String URL_ActiveClassExerciseList = URL_
			+ "/ActiveClass/ActiveClassExerciseList";

	/**
	 * 我的收藏 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_MYCOLLECT = URL_
			+ "/Material/MyMaterialsFavoriteList";

	/**
	 * APP 班级课次信息 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesgetLessonInfo = URL_
			+ "/TeacherClasses/getLessonInfo";

	/**
	 * APP -答题卡内的增加本课次对应的其他练习如预习和复习对应的练习,搜索范围为所有试卷库信息
	 */
	public static final String URL_ActiveClassExerciseByPaperNumber = URL_
			+ "/ActiveClass/ActiveClassExerciseByPaperNumber";

	/**
	 * APP 教师端-随堂-答题卡内的增加本课次，教师点击"确定"按钮保存
	 */
	public static final String URL_ActiveClassExerciseByPaperNumberSave = URL_
			+ "/ActiveClass/ActiveClassExerciseByPaperNumberSave";

	/**
	 * APP 老师，删除随堂练习
	 */
	public static final String URL_ActiveClassExerciseDelete = URL_
			+ "/ActiveClass/ActiveClassExerciseDelete";

	/**
	 * APP 老师选题时，展示的随堂练习试卷的具体题目信息
	 */
	public static final String URL_ActiveClassExerciseDetail = URL_
			+ "/ActiveClass/ActiveClassExerciseDetail";
	/**
	 * APP 教师端，保存教师选择的课堂练习试题
	 */
	public static final String URL_ActiveClassExerciseChooseQuestions = URL_
			+ "/ActiveClass/ActiveClassExerciseChooseQuestions";

	/**
	 * 教师端，点击"开始"/"结束"按钮，控制单个随堂练习试卷的作答
	 */
	public static final String URL_ActiveClassExerciseStartOrStop = URL_
			+ "/ActiveClass/ActiveClassExerciseStartOrStop";

	/**
	 * 教师端，教师端，点击"下课"按钮
	 */
	public static final String URL_FinishActiveClass = URL_
			+ "/ActiveClass/FinishActiveClass";

	/**
	 * APP 班级学生信息 iphone、Android 教师App
	 */
	public static final String URL_TeacherClassesgetClassStus = URL_
			+ "/TeacherClasses/getClassStus";

	/**
	 * 我的消息 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_MESSAGE = URL_
			+ "/TeacherHome/loadMessages";

	/**
	 * 删除我的消息 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_MESSAGE_DELECT = URL_
			+ "/TeacherHome/deleteMessage";

	/**
	 * 教师端学员成绩 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_RESULT_STUDENTINFOS = URL_
			+ "/TeacherHome/loadStuInfosByClass";

	/**
	 * 教师个人中心首页 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_HEADPAGE = URL_
			+ "/TeacherHome/getTeacherInfo";

	/**
	 * 教师个人中心首页 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_MESSAGE_REMIND = URL_
			+ "/TeacherHome/teaAllMessageNoReadCount";

	/**
	 * 系统消息-更新具体人员的系统消息的阅读(或删除)状态 iphone、Android、Ipad 教师App、学生App
	 */
	public static final String URL_MYSELF_MESSAGE_REMIND_READED = URL_
			+ "/Message/ReadOrDelMessage";

	/**
	 * 教师个人中心首页 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_MESSAGE_REMIND_DELECT = URL_
			+ "/Message/ReadOrDelMessage";

	/**
	 * 联系我们 iphone、Android 教师App
	 */
	public static final String URL_MYSELF_SENDMESSAGE_TOUS = URL_
			+ "/Message/AddSuggestionInfo";
	/**
	 * 获取广告位信息 iphone、Android 学生/教师App
	 */
	public static final String URL_UserloadAdvertisements = URL_
			+ "/User/loadAdvertisements";
	/**
	 * 添加、取消收藏 iphone、Android 学生/教师App/Material/AddOrCancelMaterialsFavorite
	 */
	public static final String URL_STUDYONLINE_COLLECT_DATA = URL_
			+ "/Material/AddOrCancelMaterialsFavorite";

	/**
	 * 查看资料详情 = 视频资料
	 */
	public static final String URL_STUDYONLINE_STUDY_PUBLICCLASS_DETAIL_VIDEO = URL_V
			+ "/materials/selectVideoMaterialsById?mId=";

	/**
	 * 查看资料增加查看次数 iphone、Android 学生/教师App
	 */
	public static final String URL_STUDYONLINE_ADDREADCOUNT = URL_
			+ "/Material/addReadCount";
	/**
	 * 资料查看 iphone、Android 学生、教师App
	 */
	public static final String URL_Material_lookUpMaterials = URL_
			+ "/Material/lookUpMaterials";
	/**
	 * APP退出系统 iphone、Android、Ipad 教师App、学生App
	 */
	public static final String URL_MYSELF_LOGOFFUSER = URL_
			+ "/User/AppLogoffUser";
	/**
	 * 获取广告位信息 iphone、Android 学生/教师App
	 */
	public static final String URL_MYSELF_LOADADVERTISEMENTS = URL_
			+ "/User/loadAdvertisements";

	/**
	 * 修改个性签名 iphone、Android 学生App
	 */
	public static final String URL_MYSELF_SIGNATURECHANGE = URL_
			+ "/User/SignatureChange";
	/**
	 * 随机分组 iphone、Android 教师App
	 */
	public static final String URL_ActiveClass_autoDivideIntoGroups = URL_
			+ "/ActiveClass/autoDivideIntoGroups";
	/**
	 * 判断是否已经分组 iphone、Android 教师/学生App
	 */
	public static final String URL_ActiveClass_getDivideGroupFlag = URL_
			+ "/ActiveClass/getDivideGroupFlag";
	/**
	 * 获取课堂分组信息 iphone、Android 教师App
	 */
	public static final String URL_ActiveClass_loadActiveClassGroup = URL_
			+ "/ActiveClass/loadActiveClassGroup";
	/**
	 * 获取课堂ID iphone、Android 教师/学生App
	 */
	public static final String URL_ActiveClass_getIdByPassCode = URL_
			+ "/ActiveClass/getIdByPassCode";
	/**
	 * 手动分组 iphone、Android 教师App
	 */
	public static final String URL_ActiveClass_handDivideIntoGroups = URL_
			+ "/ActiveClass/handDivideIntoGroups";
	/**
	 * 手动分组获取未分组学生 iphone、Android 教师App
	 */
	public static final String URL_ActiveClass_loadNoGroupStudents = URL_
			+ "/ActiveClass/loadNoGroupStudents";
//	public static boolean tag = false;

	/**
	 * 
	 * 课堂互动 投票接口
	 * 
	 */

	/**
	 * 教师端开始投票iphone、Android 教师App
	 */
	public static final String URL_startVote = URL_
			+ "/ActiveClass/startVote";
	/**
	 * 教师端结束投票iphone、Android 教师App
	 */
	public static final String URL_finishVote = URL_
			+ "/ActiveClass/finishVote";
	/**
	 * 教师端获取历史投票信息iphone、Android 教师App
	 */
	public static final String URL_loadVoteHisInfo = URL_
			+ "/ActiveClass/loadVoteHisInfo";
	
	/**
	 *学生端/老师端 参与投票iphone、Android 学生App
	 * */
	public static final String URL_joinVote = URL_
			+ "/ActiveClass/joinVote";
	
	
	/**
	 * 确认分组 iphone、Android 教师App
	 */
	public static final String URL_ActiveClass_confirmDiviceGroup = URL_ + "/ActiveClass/confirmDiviceGroup";
	/**
	 * 获取聊天室Token iphone、Android 学生/教师App
	 */
	public static final String URL_ActiveClass_getChatToken = URL_ + "/ActiveClass/getChatToken";
	/**
	 * 获取聊天室列表信息 iphone、Android 学生/教师App
	 */
	public static final String URL_ActiveClass_loadChatList = URL_ + "/ActiveClass/loadChatList";
	public static boolean tag = false;
	
	/**
	 * 教师端心跳汇总当前投票数据，与学生查询投票结果一致iphone、Android 教师/学生App
	 */
	public static final String URL_collectStuVotes = URL_
			+ "/ActiveClass/collectStuVotes";
	/**
	 * 放弃分组
	 */
	public static final String URL_ActiveClass_abandonDiviceGroup = URL_
			+ "/ActiveClass/abandonDiviceGroup";
	/**
	 * 重新分组 iphone、Android 教师App
	 */
	public static final String URL_ActiveClass_resetGroup = URL_
			+ "/ActiveClass/resetGroup";
}
