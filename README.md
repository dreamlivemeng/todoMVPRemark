# todoMVPRemark
todoMVP 项目的一个注释，把P层和V层进行了详细的注释，M层没注释，去掉了UI测试。  
这个demo有4个页面，分别是主页面、统计页面、新增修改页面、详情页面，具体功能可参考相关的Contract接口类。  
这个接口类又分为两部分，Contract.Presenter部分是逻辑接口，Contract.View部分是View界面接口。  

MVP模式 Model交互Presenter交互View
也就是Model和View不能直接交互，需要通过Presenter来交;  
View修改Model：View通知Presenter再由Presenter通知View。



