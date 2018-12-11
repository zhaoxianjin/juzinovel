package xcvf.top.readercore.daos;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import xcvf.top.readercore.bean.Book;
import xcvf.top.readercore.bean.BookMark;
import xcvf.top.readercore.bean.Chapter;
import xcvf.top.readercore.bean.Config;
import xcvf.top.readercore.bean.SearchWord;
import xcvf.top.readercore.bean.User;

import xcvf.top.readercore.daos.BookDao;
import xcvf.top.readercore.daos.BookMarkDao;
import xcvf.top.readercore.daos.ChapterDao;
import xcvf.top.readercore.daos.ConfigDao;
import xcvf.top.readercore.daos.SearchWordDao;
import xcvf.top.readercore.daos.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookDaoConfig;
    private final DaoConfig bookMarkDaoConfig;
    private final DaoConfig chapterDaoConfig;
    private final DaoConfig configDaoConfig;
    private final DaoConfig searchWordDaoConfig;
    private final DaoConfig userDaoConfig;

    private final BookDao bookDao;
    private final BookMarkDao bookMarkDao;
    private final ChapterDao chapterDao;
    private final ConfigDao configDao;
    private final SearchWordDao searchWordDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookDaoConfig = daoConfigMap.get(BookDao.class).clone();
        bookDaoConfig.initIdentityScope(type);

        bookMarkDaoConfig = daoConfigMap.get(BookMarkDao.class).clone();
        bookMarkDaoConfig.initIdentityScope(type);

        chapterDaoConfig = daoConfigMap.get(ChapterDao.class).clone();
        chapterDaoConfig.initIdentityScope(type);

        configDaoConfig = daoConfigMap.get(ConfigDao.class).clone();
        configDaoConfig.initIdentityScope(type);

        searchWordDaoConfig = daoConfigMap.get(SearchWordDao.class).clone();
        searchWordDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        bookDao = new BookDao(bookDaoConfig, this);
        bookMarkDao = new BookMarkDao(bookMarkDaoConfig, this);
        chapterDao = new ChapterDao(chapterDaoConfig, this);
        configDao = new ConfigDao(configDaoConfig, this);
        searchWordDao = new SearchWordDao(searchWordDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Book.class, bookDao);
        registerDao(BookMark.class, bookMarkDao);
        registerDao(Chapter.class, chapterDao);
        registerDao(Config.class, configDao);
        registerDao(SearchWord.class, searchWordDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        bookDaoConfig.clearIdentityScope();
        bookMarkDaoConfig.clearIdentityScope();
        chapterDaoConfig.clearIdentityScope();
        configDaoConfig.clearIdentityScope();
        searchWordDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public BookMarkDao getBookMarkDao() {
        return bookMarkDao;
    }

    public ChapterDao getChapterDao() {
        return chapterDao;
    }

    public ConfigDao getConfigDao() {
        return configDao;
    }

    public SearchWordDao getSearchWordDao() {
        return searchWordDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
