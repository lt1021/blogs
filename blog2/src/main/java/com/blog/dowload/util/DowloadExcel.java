package com.blog.dowload.util;

import com.blog.base.thread.ITaskRunning;
import com.blog.base.thread.TimerTask;
import com.blog.dowload.entity.FileModel;
import com.blog.microsoul.weapi.util.VirtualOSGlobals;
import com.blog.util.FileUpDownload;
import com.blog.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lt
 * @date 2021/3/5 11:53
 */
public class DowloadExcel {
    private static final Logger log = LoggerFactory.getLogger(DowloadExcel.class);
    private static final Map<String, FileModel> fileMap = new HashMap();
//
//    public DowloadExcel() {
//    }
//
//    public static String exportExcel(String model, String path, String lang, Object service, Method method, Object... obj) {
//        final String id = UUID.randomUUID().toString();
//        TimerTask inst = TimerTask.getInst();
//        inst.addTask(new ITaskRunning() {
//            private boolean running = false;
//            private String taskId = id;
//
//            public String getTaskId() {
//                return this.taskId;
//            }
//
//            public void runTask() {
//                DowloadExcel.log.info("===============下载任务开始：" + this.taskId);
//                FileModel fileModel = new FileModel();
//                fileModel.setStatus(0);
//                fileModel.setId(id);
//                DowloadExcel.fileMap.put(fileModel.getId(), fileModel);
//
//                try {
//                    Object[] data = (Object[])((Object[])method.invoke(service, obj));
//                    ExcelHelp.setExcelTital((List)((Map)data[0]).get("titalList"), model, path, false, (String)null);
//                    String target = FileUpDownload.getDirPath() + "/templates/temp/" + fileModel.getId() + ".xls";
//                    fileModel.setPath(target);
//                    DowloadExcel.fileMap.put(fileModel.getId(), fileModel);
//                    VirtualOSGlobals.export(path, lang, data, target);
//                    fileModel.setStatus(1);
//                } catch (Exception var4) {
//                    var4.printStackTrace();
//                    fileModel.setStatus(-1);
//                }
//
//            }
//
//            public boolean isTaskRunning() {
//                return this.running;
//            }
//
//            public void setTaskRunning(boolean isRuning) {
//                this.running = isRuning;
//            }
//        });
//        inst.taskBegin();
//        log.info("添加下载任务。。。。。。。。。。");
//        return id;
//    }
//
    public static String exportExcel(String path, String lang, Object service, Method method, boolean isHttp, Object... obj) {
        final String id = UUID.randomUUID().toString();
        TimerTask inst = TimerTask.getInst();
        inst.addTask(new ITaskRunning() {
            private boolean running = false;
            private String taskId = id;

            public String getTaskId() {
                return this.taskId;
            }

            public void runTask() {
                DowloadExcel.log.info("===============下载任务开始：" + this.taskId);
                FileModel fileModel = new FileModel();
                fileModel.setStatus(0);
                fileModel.setId(id);
                DowloadExcel.fileMap.put(fileModel.getId(), fileModel);

                try {
                    DowloadExcel.log.info(FileUpDownload.getTemplate() + "/templates/" + path + lang + ".xls===>>>" + FileUpDownload.getDirPath() + "/templates===" + path + lang + ".xls");
                    File destination = new File(FileUpDownload.getDirPath() + "/templates", path + lang + ".xls");
                    if (isHttp) {
                        FileUtils.copyURLToFile(new URL(FileUpDownload.getTemplate() + "/templates/" + path + lang + ".xls"), destination);
                    } else {
                        FileUtils.copyFile(new File(FileUpDownload.getDirPath() + "/templates/" + path + lang + ".xls"), destination);
                    }

                    String target = FileUpDownload.getDirPath() + "/templates/temp/" + fileModel.getId() + ".xls";
                    fileModel.setPath(target);
                    Object invoke = method.invoke(service, obj);
                    VirtualOSGlobals.export(path, lang, (Object[])((Object[])invoke), target);
                    fileModel.setStatus(1);
                } catch (Exception var5) {
                    var5.printStackTrace();
                    fileModel.setStatus(-1);
                }

            }

            public boolean isTaskRunning() {
                return this.running;
            }

            public void setTaskRunning(boolean isRuning) {
                this.running = isRuning;
            }
        });
        inst.taskBegin();
        log.info("添加下载任务。。。。。。。。。。");
        return id;
    }
//
//    public static String exportExcelCopySheet(String model, String path, String lang, Object service, Method method, Object... obj) {
//        final String id = UUID.randomUUID().toString();
//        TimerTask inst = TimerTask.getInst();
//        inst.addTask(new ITaskRunning() {
//            private boolean running = false;
//            private String taskId = id;
//
//            public String getTaskId() {
//                return this.taskId;
//            }
//
//            public void runTask() {
//                DowloadExcel.log.info("===============下载任务开始：" + this.taskId);
//                FileModel fileModel = new FileModel();
//                fileModel.setStatus(0);
//                fileModel.setId(id);
//                DowloadExcel.fileMap.put(fileModel.getId(), fileModel);
//
//                try {
//                    Object invoke = method.invoke(service, obj);
//                    Object[] datas = (Object[])((Object[])invoke);
//                    Object[] objs = new Object[datas.length - 1];
//                    if (datas[datas.length - 1] instanceof SheetCloneInfo) {
//                        SheetCloneInfo info = (SheetCloneInfo)datas[datas.length - 1];
//                        ExcelHelp.cloneSheet(model, path, info.getTargetName(), info.getTargetIndex(), info.getCount(), info.getNames());
//                        System.arraycopy(datas, 0, objs, 0, datas.length - 1);
//                    }
//
//                    String target = FileUpDownload.getDirPath() + "/templates/temp/" + fileModel.getId() + ".xls";
//                    fileModel.setPath(target);
//                    VirtualOSGlobals.export(path, lang, objs, target);
//                    fileModel.setStatus(1);
//                } catch (Exception var6) {
//                    var6.printStackTrace();
//                    fileModel.setStatus(-1);
//                }
//
//            }
//
//            public boolean isTaskRunning() {
//                return this.running;
//            }
//
//            public void setTaskRunning(boolean isRuning) {
//                this.running = isRuning;
//            }
//        });
//        inst.taskBegin();
//        log.info("添加下载任务。。。。。。。。。。");
//        return id;
//    }
//
    public static byte[] readFile(String id) {
        return FileUpDownload.readFileByBytes(((FileModel)fileMap.get(id)).getPath());
    }

    public static void deleteByMap(String id) {
        FileModel fileModel = (FileModel)fileMap.get(id);
        if (fileModel != null && !StringUtil.isEmpty(fileModel.getPath())) {
            File file = new File(fileModel.getPath());
            if (file.exists()) {
                file.delete();
            }

            fileMap.remove(id);
        }
    }
//
    public static int sure(String id) {
        FileModel fileModel = (FileModel)fileMap.get(id);
        return fileModel != null ? fileModel.getStatus() : 0;
    }

    public static void delete(String path) {
        if (!StringUtil.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

        }
    }
//
    public static void clearTempExcel() {
        FileUpDownload.delAllFile(FileUpDownload.getDirPath() + "/templates/temp/");
        fileMap.clear();
    }

//    public static String exportExcel(String path, String lang, Object service, Method method, boolean isHttp, Object... obj) {
//        return null;
//    }
}
