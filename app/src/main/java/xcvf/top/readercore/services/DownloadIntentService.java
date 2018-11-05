package xcvf.top.readercore.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.impl.FileDownloader;
import xcvf.top.readercore.utils.Constant;

/**
 * 下载
 */
public class DownloadIntentService extends IntentService {

    public static String PROGRESS = "DownloadIntentService.progress";
    ArrayList<Chapter> chapterList;
    int mCount;
    int finishCount = 0;
    Chapter chapter;

    /**
     * 启动一个下载任务
     *
     * @param context
     */
    public static void startDownloadService(Context context, Chapter chapter, ArrayList<Chapter> chapterList) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.putExtra("chapters", chapterList);
        intent.putExtra("chapter", chapter);
        context.startService(intent);
    }


    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        chapterList = intent.getParcelableArrayListExtra("chapters");
        chapter = intent.getParcelableExtra("chapter");
        mCount = chapterList == null ? 0 : chapterList.size();
        for (int i = 0; i < mCount; i++) {
            final Chapter chapter = chapterList.get(i);
            String dest = Constant.getCachePath(getBaseContext(), chapter.self_page);
            FileDownloader.downloadUrl(Constant.buildChapterFilePath(chapter.self_page), dest);
            Intent itn = new Intent(PROGRESS);
            finishCount++;
            itn.putExtra("info", "正在下载" + finishCount + "/" + mCount);
            itn.putExtra("bookid", chapter.extern_bookid);
            itn.putExtra("finish", finishCount == mCount ? 1 : 0);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(itn);
            LogUtils.e("sendBroadCast="+dest);
        }
    }
}
