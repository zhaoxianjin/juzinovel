package xcvf.top.readercore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.iscore.freereader.R;
import top.iscore.freereader.mvp.presenters.BookReadPresenter;
import top.iscore.freereader.mvp.view.BookReadView;
import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookMark;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.Page;
import xcvf.top.readercore.bean.SettingAction;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.impl.ChapterDisplayedImpl;
import xcvf.top.readercore.impl.ChapterProviderImpl;
import xcvf.top.readercore.impl.FullScreenHandler;
import xcvf.top.readercore.interfaces.Area;
import xcvf.top.readercore.interfaces.IAreaClickListener;
import xcvf.top.readercore.interfaces.IChapterListener;
import xcvf.top.readercore.interfaces.IChapterProvider;
import xcvf.top.readercore.interfaces.IPage;
import xcvf.top.readercore.interfaces.IPageScrollListener;
import xcvf.top.readercore.interfaces.OnTextConfigChangedListener;
import xcvf.top.readercore.styles.ModeConfig;
import xcvf.top.readercore.styles.ModeHandler;
import xcvf.top.readercore.styles.ModeProvider;
import xcvf.top.readercore.views.ReaderSettingView;
import xcvf.top.readercore.views.ReaderView;

/**
 * 阅读页面
 * Created by xiaw on 2018/6/30.
 */
public class ReaderActivity extends MvpActivity<BookReadView, BookReadPresenter> implements IAreaClickListener, IPageScrollListener, BookReadView {


    Book book;

    @BindView(R.id.readerView)
    ReaderView readerView;


    ChapterDisplayedImpl mChapterDisplayedImpl;
    BookMark mBookMark;
    User mUser;
    @BindView(R.id.setting_view)
    ReaderSettingView settingView;

    ChapterFragment chapterFragment;

    FullScreenHandler fullScreenHandler;

    /**
     * 阅读页面
     *
     * @param activity
     * @param book
     */
    public static void toReadPage(Activity activity, Book book) {
        Intent intent = new Intent(activity, ReaderActivity.class);
        intent.putExtra("book", book);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);
        presenter.attachView(this);
        book = getIntent().getParcelableExtra("book");
        if (book != null) {
            book.save();
        }
        mUser = User.first(User.class);
        settingView.setBook(book);
        settingView.setSettingListener(mSettingListener);
        initReadView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
                return;
            }
        }
        checkChapters(true);
        fullScreenHandler = new FullScreenHandler(this, readerView, settingView);
        //loadData(false);
        fullScreenHandler.hide();
    }


    @Override
    protected void onResume() {
        super.onResume();
        fullScreenHandler.check();
    }

    /**
     * 设置按钮
     */
    private ReaderSettingView.ISettingListener mSettingListener = new ReaderSettingView.ISettingListener() {
        @Override
        public void onSettingChanged(SettingAction action) {
            if (action == SettingAction.ACTION_BACK) {
                finish();
            } else if (action == SettingAction.ACTION_CHAPTER) {
                chapterFragment = new ChapterFragment();
                chapterFragment.setBook(book);
                chapterFragment.setChapter(readerView.getCurrentChapter());
                chapterFragment.setSwitchChapterListener(switchChapterListener);
                chapterFragment.show(getSupportFragmentManager(), "ChapterFragment");
            } else if (action == SettingAction.ACTION_MODE) {
                Mode current = ModeProvider.getCurrentMode();
                //切换日间模式和夜间模式
                Mode dest;
                if (current == Mode.NightMode) {
                    dest = Mode.DayMode;
                } else {
                    dest = Mode.NightMode;
                }
                settingView.changeMode(dest);
                ModeHandler modeHandler = new ModeHandler(ReaderActivity.this,(ViewGroup) findViewById(R.id.activity_content));
                modeHandler.apply(dest);
                ModeConfig config = ModeProvider.get(dest);
                TextConfig.getConfig().setBackgroundColor(config.getBgResId());
                TextConfig.getConfig().setTextColor(config.getTextColorResId());
                onTextConfigChangedListener.onChanged(null);
                ModeProvider.save(config.getId(), dest);
                fullScreenHandler.check();
            }
        }
    };


    /**
     * 字体设置改了
     */
    private OnTextConfigChangedListener onTextConfigChangedListener = new OnTextConfigChangedListener() {
        @Override
        public void onChanged(TextConfig config) {
            readerView.onTextConfigChanged();
        }
    };


    /**
     * 切换章节
     */
    private ChapterFragment.switchChapterListener switchChapterListener = new ChapterFragment.switchChapterListener() {
        @Override
        public void onChapter(Chapter chapter) {
            mChapterDisplayedImpl.showChapter(true, readerView, false, Page.LOADING_PAGE, chapter);
        }
    };

    private void checkChapters(final boolean retry) {
        mBookMark = BookMark.getMark(book, mUser.getUid());
        String chapterid = "0";
        if (mBookMark == null) {
            //没有书签，从第一个章节开始
            Chapter chapter = Chapter.getNextChapter("0");
            if (chapter != null) {
                chapterid = String.valueOf(chapter.chapterid);
            }
        } else {
            chapterid = mBookMark.getChapterid();
        }
        ChapterProviderImpl.newInstance().getChapter(IChapterProvider.TYPE_DETAIL, chapterid, null, new IChapterListener() {
            @Override
            public void onChapter(Chapter srcChapter, Chapter chapter) {
                if (chapter == null) {
                    //本地没有章节内容,加载服务端的
                    if (retry) {
                        loadData(false);
                    }
                } else {
                    mChapterDisplayedImpl.showChapter(false, readerView, false, mBookMark == null ? IPage.LOADING_PAGE : mBookMark.getPage(), chapter);
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkChapters(false);
    }

    @NonNull
    @Override
    public BookReadPresenter createPresenter() {
        return new BookReadPresenter();
    }

    private void initReadView() {
        mChapterDisplayedImpl = ChapterDisplayedImpl.newInsrance();
        readerView.setAreaClickListener(this);
        readerView.setPageScrollListener(this);

    }

    @Override
    public void clickArea(Area area) {
        fullScreenHandler.check();
    }

    @Override
    public void onScroll(int current, int total, int nextOrPre) {

        if (nextOrPre == IPageScrollListener.PRE_CHAPTER) {
            //上一章节
            Chapter chapter = readerView.getCurrentChapter();
            ChapterProviderImpl.newInstance().getChapter(ChapterProviderImpl.TYPE_PRE, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                @Override
                public void onChapter(Chapter srcChapter, Chapter chapter) {
                    mChapterDisplayedImpl.showChapter(false, readerView, true, IPage.LOADING_PAGE, chapter);
                }
            });

        } else if (nextOrPre == IPageScrollListener.NEXT_CHAPTER) {
            //下一章节
            Chapter chapter = readerView.getCurrentChapter();
            ChapterProviderImpl.newInstance().getChapter(ChapterProviderImpl.TYPE_NEXT, String.valueOf(chapter.chapterid), chapter, new IChapterListener() {
                @Override
                public void onChapter(Chapter srcChapter, Chapter chapter) {
                    mChapterDisplayedImpl.showChapter(false, readerView, false, IPage.LOADING_PAGE, chapter);
                }
            });
        }

        Chapter chapter = readerView.getCurrentChapter();
        //保存书签
        if (chapter != null) {
            if (mBookMark == null) {
                mBookMark = new BookMark(mUser.getUid(), book.extern_bookid);
            }
            mBookMark.setExtern_bookid(book.extern_bookid);
            mBookMark.setChapterid(String.valueOf(chapter.chapterid));
            mBookMark.setTime_stamp(System.currentTimeMillis());
            Page page = readerView.getCurrentPage();
            mBookMark.setPage(page.getIndex());
            mBookMark.save();
        }

    }

    @Override
    public void onLoadChapterList(ArrayList<Chapter> chapters) {
        book.chapters = chapters;
        ChapterProviderImpl.newInstance().saveChapter(chapters, new IChapterListener() {
            @Override
            public void onChapter(Chapter srcChapter, Chapter destChapter) {
                checkChapters(false);
            }
        });

    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(Chapter data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadChapters(book, SPUtils.getInstance().getInt(book.extern_bookid, 0));
    }
}
