package xcvf.top.readercore.daos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import xcvf.top.readercore.bean.Chapter;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHAPTER".
*/
public class ChapterDao extends AbstractDao<Chapter, Void> {

    public static final String TABLENAME = "CHAPTER";

    /**
     * Properties of entity Chapter.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Chapter_name = new Property(0, String.class, "chapter_name", false, "CHAPTER_NAME");
        public final static Property Extern_bookid = new Property(1, String.class, "extern_bookid", false, "EXTERN_BOOKID");
        public final static Property Is_fetch = new Property(2, int.class, "is_fetch", false, "IS_FETCH");
        public final static Property Engine_domain = new Property(3, String.class, "engine_domain", false, "ENGINE_DOMAIN");
        public final static Property Is_download = new Property(4, boolean.class, "is_download", false, "IS_DOWNLOAD");
        public final static Property Self_page = new Property(5, String.class, "self_page", false, "SELF_PAGE");
        public final static Property Chapterid = new Property(6, int.class, "chapterid", false, "CHAPTERID");
    }


    public ChapterDao(DaoConfig config) {
        super(config);
    }
    
    public ChapterDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAPTER\" (" + //
                "\"CHAPTER_NAME\" TEXT," + // 0: chapter_name
                "\"EXTERN_BOOKID\" TEXT," + // 1: extern_bookid
                "\"IS_FETCH\" INTEGER NOT NULL ," + // 2: is_fetch
                "\"ENGINE_DOMAIN\" TEXT," + // 3: engine_domain
                "\"IS_DOWNLOAD\" INTEGER NOT NULL ," + // 4: is_download
                "\"SELF_PAGE\" TEXT UNIQUE ," + // 5: self_page
                "\"CHAPTERID\" INTEGER NOT NULL );"); // 6: chapterid
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAPTER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Chapter entity) {
        stmt.clearBindings();
 
        String chapter_name = entity.getChapter_name();
        if (chapter_name != null) {
            stmt.bindString(1, chapter_name);
        }
 
        String extern_bookid = entity.getExtern_bookid();
        if (extern_bookid != null) {
            stmt.bindString(2, extern_bookid);
        }
        stmt.bindLong(3, entity.getIs_fetch());
 
        String engine_domain = entity.getEngine_domain();
        if (engine_domain != null) {
            stmt.bindString(4, engine_domain);
        }
        stmt.bindLong(5, entity.getIs_download() ? 1L: 0L);
 
        String self_page = entity.getSelf_page();
        if (self_page != null) {
            stmt.bindString(6, self_page);
        }
        stmt.bindLong(7, entity.getChapterid());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Chapter entity) {
        stmt.clearBindings();
 
        String chapter_name = entity.getChapter_name();
        if (chapter_name != null) {
            stmt.bindString(1, chapter_name);
        }
 
        String extern_bookid = entity.getExtern_bookid();
        if (extern_bookid != null) {
            stmt.bindString(2, extern_bookid);
        }
        stmt.bindLong(3, entity.getIs_fetch());
 
        String engine_domain = entity.getEngine_domain();
        if (engine_domain != null) {
            stmt.bindString(4, engine_domain);
        }
        stmt.bindLong(5, entity.getIs_download() ? 1L: 0L);
 
        String self_page = entity.getSelf_page();
        if (self_page != null) {
            stmt.bindString(6, self_page);
        }
        stmt.bindLong(7, entity.getChapterid());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Chapter readEntity(Cursor cursor, int offset) {
        Chapter entity = new Chapter( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // chapter_name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // extern_bookid
            cursor.getInt(offset + 2), // is_fetch
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // engine_domain
            cursor.getShort(offset + 4) != 0, // is_download
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // self_page
            cursor.getInt(offset + 6) // chapterid
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Chapter entity, int offset) {
        entity.setChapter_name(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setExtern_bookid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIs_fetch(cursor.getInt(offset + 2));
        entity.setEngine_domain(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIs_download(cursor.getShort(offset + 4) != 0);
        entity.setSelf_page(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setChapterid(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Chapter entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Chapter entity) {
        return null;
    }

    @Override
    public boolean hasKey(Chapter entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
